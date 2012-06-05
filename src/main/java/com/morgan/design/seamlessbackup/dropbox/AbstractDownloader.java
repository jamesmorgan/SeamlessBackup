package com.morgan.design.seamlessbackup.dropbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

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
import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.DropboxError;
import com.morgan.design.seamlessbackup.domain.DropboxIssue;

abstract class AbstractDownloader extends AsyncTask<Void, Long, String> {

	private static Logger log = LoggerFactory.getLogger(AbstractDownloader.class);

	public interface DropdoxBackupReciever {
		void failed(DropboxIssue dropboxIssue);

		void recieved(String jsonResult);
	}

	private final DropdoxBackupReciever onBackupReciever;

	private final BackupType mBackupType;
	private final Context mContext;
	private final DropboxAPI<?> mApi;

	private final ProgressDialog mDialog;
	private boolean mCanceled;

	private FileOutputStream mFos;
	private Long mFileLen;

	private DropboxIssue mDropboxIssue;

	protected AbstractDownloader(Context context, DropboxAPI<?> mApi, BackupType mBackupType, DropdoxBackupReciever dropdoxBackupReciever) {
		this.onBackupReciever = dropdoxBackupReciever;

		// We set the context this way so we don't accidentally leak activities
		this.mContext = context.getApplicationContext();

		this.mApi = mApi;
		this.mBackupType = mBackupType;

		this.mDialog = new ProgressDialog(context);
		this.mDialog.setMessage("Downloading Document");
		this.mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCanceled = true;
				mDropboxIssue = DropboxIssue.fromError(DropboxError.CANCELLED);
				if (mFos != null) {
					try {
						mFos.close();
					}
					catch (IOException e) {
					}
				}
			}
		});
		this.mDialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			if (mCanceled) {
				return null;
			}

			// Get the metadata for a directory
			Entry dirent = mApi.metadata(mBackupType.dir(), 1000, null, true, null);

			if (!dirent.isDir || dirent.contents == null || 0 == dirent.contents.size()) {
				mDropboxIssue = DropboxIssue.fromError(DropboxError.NOT_FOUND);
				return null;
			}

			for (Entry ent : dirent.contents) {
				System.out.println(ent.fileName());
			}

			if (mCanceled) {
				return null;
			}

			// TODO pick correct file for backup
			Entry ent = dirent.contents.get(0);
			String path = ent.path;
			mFileLen = ent.bytes;

			String cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + mBackupType.fileName() + ".temp_cache";
			try {
				mFos = new FileOutputStream(cachePath);
			}
			catch (FileNotFoundException e) {
				mDropboxIssue = DropboxIssue.fromError(DropboxError.UNABLE_TO_CREATE_TEMP_FILE);
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
				log.warn("Unable to download backup file", e);
				mDropboxIssue = DropboxIssue.fromError(DropboxError.UNABLE_TO_CREATE_TEMP_FILE);
			}
			return null;
		}
		catch (DropboxUnlinkedException e) {
			// The AuthSession wasn't properly authenticated or user unlinked.
			mDropboxIssue = DropboxIssue.fromError(DropboxError.NOT_AUTHORISED);
		}
		catch (DropboxPartialFileException e) {
			// We cancelled the operation
			mDropboxIssue = DropboxIssue.fromError(DropboxError.CANCELLED);
		}
		catch (DropboxServerException e) {
			// Translate error in to usable form
			mDropboxIssue = DropboxIssue.fromError(e);
		}
		catch (DropboxIOException e) {
			// Happens all the time, probably want to retry automatically.
			mDropboxIssue = DropboxIssue.fromError(DropboxError.IO_EXCEPTION);
		}
		catch (DropboxParseException e) {
			// Probably due to Dropbox server restarting, should retry
			mDropboxIssue = DropboxIssue.fromError(DropboxError.PARSE_EXCEPTION);
		}
		catch (DropboxException e) {
			mDropboxIssue = DropboxIssue.fromError(DropboxError.UNKNOWN_ERROR);
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
			onBackupReciever.recieved(result);
		}
		else {
			if (null != mDropboxIssue) {
				onBackupReciever.failed(mDropboxIssue);
			}
			else {
				onBackupReciever.failed(DropboxIssue.fromError(DropboxError.UNKNOWN_ERROR));
			}
		}
	}

}
