package com.morgan.design.seamlessbackup.dropbox;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxServerException;
import com.google.common.collect.Iterables;
import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.DropboxError;
import com.morgan.design.seamlessbackup.domain.DropboxIssue;
import com.morgan.design.seamlessbackup.domain.FileNameDateSorter;
import com.morgan.design.seamlessbackup.util.Prefs;

public class DropboxCleaner extends AsyncTask<Void, Long, Boolean> {

	private static Logger log = LoggerFactory.getLogger(DropboxCleaner.class);

	public interface onDropboxCleaned {
		void failed(DropboxIssue issue);
	}

	private final DropboxAPI<?> mApi;
	private final List<BackupType> mBackupTypes;
	private final Context mContext;
	private final onDropboxCleaned mOnDropboxCleaned;

	private DropboxIssue mDropboxIssue;

	public DropboxCleaner(Activity context, DropboxAPI<?> api, List<BackupType> backupTypes, onDropboxCleaned onDropboxCleaned) {
		// We set the context this way so we don't accidentally leak activities
		mContext = context.getApplicationContext();
		mBackupTypes = backupTypes;
		mApi = api;
		mOnDropboxCleaned = onDropboxCleaned;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		int filesToKeep = Prefs.getMaxBackUpFilesToKeep(mContext);
		for (BackupType backupType : mBackupTypes) {
			cleanUp(backupType, filesToKeep);
		}
		return true;
	}

	private void cleanUp(BackupType backupType, int filesToKeep) {
		try {
			// Get the metadata for a directory
			Entry dir = mApi.metadata(backupType.dir(), 1000, null, true, null);

			if (!dir.isDir || dir.contents == null || 0 == dir.contents.size()) {
				log.debug("Nothing to clean, directory is empty or contents is zero");
				return;
			}

			List<Entry> sortedFiles = FileNameDateSorter.sortByDate(dir.contents);
			if (sortedFiles.size() < filesToKeep) {
				log.debug("Nothing to clean, less than {} files found", filesToKeep);
				return;
			}

			// Move on x Number
			Iterable<Entry> fileToClean = Iterables.skip(sortedFiles, filesToKeep);

			// Clean Up everything except last X backups
			for (Entry entry : fileToClean) {
				mApi.delete(entry.path);
			}
		}
		catch (DropboxServerException e) {
			mDropboxIssue = DropboxIssue.fromServerException(e);
		}
		catch (DropboxException e) {
			mDropboxIssue = DropboxIssue.fromException(e);
		}
		catch (Exception e) {
			mDropboxIssue = DropboxIssue.fromError(DropboxError.UNKNOWN_ERROR);
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mOnDropboxCleaned.failed(mDropboxIssue);
	}
}
