package com.morgan.design.seamlessbackup.dropbox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
import com.dropbox.client2.exception.DropboxServerException;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.DropboxError;
import com.morgan.design.seamlessbackup.domain.DropboxIssue;
import com.morgan.design.seamlessbackup.naming.DefaultFileNameSorter;
import com.morgan.design.seamlessbackup.naming.PatternFileNameParser;

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

	protected AbstractDownloader(final Context context, final DropboxAPI<?> mApi, final BackupType mBackupType,
			final DropdoxBackupReciever dropdoxBackupReciever) {
		onBackupReciever = dropdoxBackupReciever;

		// We set the context this way so we don't accidentally leak activities
		mContext = context.getApplicationContext();

		this.mApi = mApi;
		this.mBackupType = mBackupType;

		mDialog = new ProgressDialog(context);
		mDialog.setMessage("Downloading Document");
		mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				mCanceled = true;
				mDropboxIssue = DropboxIssue.fromError(DropboxError.CANCELLED);
				if (mFos != null) {
					try {
						mFos.close();
					}
					catch (final IOException e) {
					}
				}
			}
		});
		mDialog.show();
	}

	@Override
	protected String doInBackground(final Void... params) {
		try {
			if (mCanceled) {
				return null;
			}

			// Get the metadata for a directory
			final Entry dir = mApi.metadata(mBackupType.dir(), 1000, null, true, null);

			if (!dir.isDir || dir.contents == null || 0 == dir.contents.size()) {
				mDropboxIssue = DropboxIssue.fromError(DropboxError.NOT_FOUND);
				return null;
			}

			for (final Entry ent : dir.contents) {
				System.out.println(ent.fileName());
			}

			if (mCanceled) {
				return null;
			}

			// TODO inject
			final List<Entry> sortedFiles = new DefaultFileNameSorter(new PatternFileNameParser())
					.sortByDate(dir.contents);

			// TODO check MD5

			final Entry ent = sortedFiles.get(0);
			final String path = ent.path;
			mFileLen = ent.bytes;

			final String cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + mBackupType.fileName()
					+ ".temp_cache";
			mFos = new FileOutputStream(cachePath);

			// Download it
			mApi.getFile(path, null, mFos, null);
			if (mCanceled) {
				return null;
			}

			return Files.toString(new File(cachePath), Charsets.UTF_8);
		}
		catch (final DropboxServerException e) {
			mDropboxIssue = DropboxIssue.fromServerException(e);
		}
		catch (final DropboxException e) {
			mDropboxIssue = DropboxIssue.fromException(e);
		}
		catch (final IOException e) {
			log.warn("Unable to download backup file", e);
			mDropboxIssue = DropboxIssue.fromError(DropboxError.UNABLE_TO_CREATE_LOCAL_FILE);
		}
		catch (final Exception e) {
			mDropboxIssue = DropboxIssue.fromError(DropboxError.UNKNOWN_ERROR);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(final Long... progress) {
		final int percent = (int) (100.0 * (double) progress[0] / mFileLen + 0.5);
		mDialog.setProgress(percent);
	}

	@Override
	protected void onPostExecute(final String result) {
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
