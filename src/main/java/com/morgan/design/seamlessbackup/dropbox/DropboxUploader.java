package com.morgan.design.seamlessbackup.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxServerException;
import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.DropboxError;
import com.morgan.design.seamlessbackup.domain.DropboxIssue;
import com.morgan.design.seamlessbackup.domain.FileNameGenerator;
import com.morgan.design.seamlessbackup.service.DateTimeServiceImpl;

public class DropboxUploader extends AsyncTask<Void, Long, Boolean> {

	private final DropboxAPI<?> mApi;
	private final BackupType mBackupType;
	private final File mFile;

	private final long mFileLen;
	private UploadRequest mRequest;
	private final Context mContext;
	private final ProgressDialog mDialog;

	private DropboxIssue mDropboxIssue;

	public DropboxUploader(Activity context, DropboxAPI<?> api, BackupType backupType, File file) {
		// We set the context this way so we don't accidentally leak activities
		mContext = context.getApplicationContext();

		mFileLen = file.length();
		mApi = api;
		mBackupType = backupType;
		mFile = file;

		mDialog = new ProgressDialog(context);
		mDialog.setMax(100);
		mDialog.setMessage("Uploading " + file.getName());
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setProgress(0);

		mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDropboxIssue = DropboxIssue.fromError(DropboxError.CANCELLED);
				try {
					mRequest.abort();
				}
				catch (Exception e) {
				}
			}
		});
		mDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			// By creating a request, we get a handle to the putFile operation, so we can cancel it later if we want to
			FileInputStream fis = new FileInputStream(mFile);

			String path = FileNameGenerator.generateUploadPath(mBackupType, mFile, new DateTimeServiceImpl());

			mRequest = mApi.putFileOverwriteRequest(path, fis, mFile.length(), new ProgressListener() {
				@Override
				public long progressInterval() {
					// Update the progress bar every half-second or so
					return 500;
				}

				@Override
				public void onProgress(long bytes, long total) {
					publishProgress(bytes);
				}
			});

			if (mRequest != null) {
				mRequest.upload();
				return true;
			}
		}
		catch (DropboxServerException e) {
			mDropboxIssue = DropboxIssue.fromServerException(e);
		}
		catch (DropboxException e) {
			mDropboxIssue = DropboxIssue.fromException(e);
		}
		catch (FileNotFoundException e) {
			mDropboxIssue = DropboxIssue.fromError(DropboxError.UNABLE_TO_CREATE_LOCAL_FILE);
		}
		catch (Exception e) {
			mDropboxIssue = DropboxIssue.fromError(DropboxError.UNKNOWN_ERROR);
		}
		return false;
	}

	@Override
	protected void onProgressUpdate(Long... progress) {
		int percent = (int) (100.0 * (double) progress[0] / mFileLen + 0.5);
		mDialog.setProgress(percent);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (result) {
			showToast("Document successfully uploaded");
		}
		else {
			showToast(mDropboxIssue.getError().getError());
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}
}
