package com.morgan.design.seamlessbackup.dropbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;

public class DictionaryDownload extends AsyncTask<Void, Long, String> {

	public interface OnDownloadedListener {
		void onDownloaded(List<DictionaryWord> words);
	}

	private final Context mContext;
	private final ProgressDialog mDialog;
	private final DropboxAPI<?> mApi;
	private final String mPath;

	private FileOutputStream mFos;

	private boolean mCanceled;
	private Long mFileLen;
	private String mErrorMsg;

	// Note that, since we use a single file name here for simplicity, you
	// won't be able to use this code for two simultaneous downloads.
	private final static String TEMP_CHACHE_DICTIONARY = "cache_Dictionary.gson";
	private final OnDownloadedListener onDownloadedListener;

	public DictionaryDownload(Context context, DropboxAPI<?> api, String dropboxPath, OnDownloadedListener onDownloadedListener) {
		this.onDownloadedListener = onDownloadedListener;
		// We set the context this way so we don't accidentally leak activities
		mContext = context.getApplicationContext();

		mApi = api;
		mPath = dropboxPath;

		mDialog = new ProgressDialog(context);
		mDialog.setMessage("Downloading Document");
		mDialog.setButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCanceled = true;
				mErrorMsg = "Canceled";
				if (mFos != null) {
					try {
						mFos.close();
					}
					catch (IOException e) {
					}
				}
			}
		});
		mDialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			if (mCanceled) {
				return null;
			}

			// Get the metadata for a directory
			Entry dirent = mApi.metadata(mPath, 1000, null, true, null);

			if (!dirent.isDir || dirent.contents == null) {
				// It's not a directory, or there's nothing in it
				mErrorMsg = "File or empty directory";
				return null;
			}

			for (Entry ent : dirent.contents) {
				System.out.println(ent.fileName());
			}

			if (mCanceled) {
				return null;
			}

			if (0 == dirent.contents.size()) {
				return null;
			}

			Entry ent = dirent.contents.get(0);
			String path = ent.path;
			mFileLen = ent.bytes;

			String cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + TEMP_CHACHE_DICTIONARY;
			try {
				mFos = new FileOutputStream(cachePath);
			}
			catch (FileNotFoundException e) {
				mErrorMsg = "Couldn't create a local file to store the document";
				return null;
			}

			// Download it
			mApi.getFile(path, null, mFos, null);
			if (mCanceled) {
				return null;
			}

			try {
				return Files.toString(new File(cachePath), Charsets.UTF_8);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		catch (DropboxUnlinkedException e) {
			// The AuthSession wasn't properly authenticated or user unlinked.
		}
		catch (DropboxPartialFileException e) {
			// We canceled the operation
			mErrorMsg = "Download canceled";
		}
		catch (DropboxServerException e) {
			// Server-side exception. These are examples of what could happen,
			// but we don't do anything special with them here.
			if (e.error == DropboxServerException._304_NOT_MODIFIED) {
				// won't happen since we don't pass in revision with metadata
			}
			else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
				// Unauthorized, so we should unlink them. You may want to
				// automatically log the user out in this case.
			}
			else if (e.error == DropboxServerException._403_FORBIDDEN) {
				// Not allowed to access this
			}
			else if (e.error == DropboxServerException._404_NOT_FOUND) {
				// path not found (or if it was the thumbnail, can't be
				// thumbnailed)
			}
			else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
				// too many entries to return
			}
			else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
				// can't be thumbnailed
			}
			else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
				// user is over quota
			}
			else {
				// Something else
			}
			// This gets the Dropbox error, translated into the user's language
			mErrorMsg = e.body.userError;
			if (mErrorMsg == null) {
				mErrorMsg = e.body.error;
			}
		}
		catch (DropboxIOException e) {
			// Happens all the time, probably want to retry automatically.
			mErrorMsg = "Network error.  Try again.";
		}
		catch (DropboxParseException e) {
			// Probably due to Dropbox server restarting, should retry
			mErrorMsg = "Dropbox error.  Try again.";
		}
		catch (DropboxException e) {
			// Unknown error
			mErrorMsg = "Unknown error.  Try again.";
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Long... progress) {
		int percent = (int) (100.0 * (double) progress[0] / mFileLen + 0.5);
		mDialog.setProgress(percent);
	}

	@Override
	protected void onPostExecute(String result) {
		mDialog.dismiss();
		if (null != result) {

			Type typeOfT = new TypeToken<List<DictionaryWord>>() {
			}.getType();

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			List<DictionaryWord> results = gson.fromJson(result, typeOfT);
			onDownloadedListener.onDownloaded(results);
		}
		else {
			// Couldn't download it, so show an error
			showToast(mErrorMsg);
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}

}
