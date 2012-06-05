package com.morgan.design.seamlessbackup;

import java.io.File;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;
import com.morgan.design.seamlessbackup.domain.DropboxIssue;
import com.morgan.design.seamlessbackup.domain.SearchHistory;
import com.morgan.design.seamlessbackup.domain.loader.BookmarkContentLoader;
import com.morgan.design.seamlessbackup.domain.loader.SearchHistoryContentLoader;
import com.morgan.design.seamlessbackup.domain.loader.WordDictionaryContentLoader;
import com.morgan.design.seamlessbackup.dropbox.BookmarkDownloader;
import com.morgan.design.seamlessbackup.dropbox.DictionWordDownloader;
import com.morgan.design.seamlessbackup.dropbox.DownloadReciever;
import com.morgan.design.seamlessbackup.dropbox.DropboxUploader;
import com.morgan.design.seamlessbackup.dropbox.SearchHistoryDownloader;
import com.morgan.design.seamlessbackup.service.BackupCreator;

@ContentView(R.layout.quick_backup)
public class QuickBackupActivity extends AbstractAuthenticatedActivity {

	@InjectView(R.id.download_search_history_button)
	private Button mDownloadSearchHistory;
	@InjectView(R.id.sync_search_history_button)
	private Button mSyncSearchHistory;

	@InjectView(R.id.download_bookmarks_button)
	private Button mDownloadBookmarks;
	@InjectView(R.id.sync_bookmarks_button)
	private Button mSyncBookmarks;

	@InjectView(R.id.download_dictionary_button)
	private Button mDownloadDictionary;
	@InjectView(R.id.sync_dictionary_button)
	private Button mSyncDictionary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Bookmarks
		mSyncBookmarks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				syncBookmarks();
			}
		});
		mDownloadBookmarks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadBookmarks();
			}
		});

		// Search History
		mSyncSearchHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				syncSearchHistory();
			}
		});
		mDownloadSearchHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadSearchHistory();
			}
		});

		// Dictionary
		mSyncDictionary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				syncDictionary();
			}
		});
		mDownloadDictionary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadDictionary();
			}
		});

	}

	protected void syncSearchHistory() {
		List<SearchHistory> loadContent = new SearchHistoryContentLoader().loadContent(this);

		File file = new BackupCreator().createFile(loadContent, BackupType.BROWSER_SEARCH_HISTORY);

		new DropboxUploader(this, mApi, BackupType.BROWSER_SEARCH_HISTORY, file).execute();
	}

	protected void syncBookmarks() {
		List<Bookmark> loadContent = new BookmarkContentLoader().loadContent(this);

		File file = new BackupCreator().createFile(loadContent, BackupType.BROWSER_BOOKMARKS);

		new DropboxUploader(this, mApi, BackupType.BROWSER_BOOKMARKS, file).execute();
	}

	protected void syncDictionary() {
		List<DictionaryWord> loadContent = new WordDictionaryContentLoader().loadContent(this);

		File file = new BackupCreator().createFile(loadContent, BackupType.DICTIONARY);

		new DropboxUploader(this, mApi, BackupType.DICTIONARY, file).execute();
	}

	protected void downloadDictionary() {
		new DictionWordDownloader(this, mApi, new DownloadReciever<DictionaryWord>() {
			@Override
			public void recieved(List<DictionaryWord> results) {
				new WordDictionaryContentLoader().updateContent(results, QuickBackupActivity.this);
			}

			@Override
			public void failed(DropboxIssue dropboxIssue) {
				showToast("Failed");
			}
		}).execute();

	}

	protected void downloadSearchHistory() {
		new SearchHistoryDownloader(this, mApi, new DownloadReciever<SearchHistory>() {
			@Override
			public void recieved(List<SearchHistory> results) {
				showToast("Downloaded: " + results);
			}

			@Override
			public void failed(DropboxIssue dropboxIssue) {
				showToast("Failed");
			}
		}).execute();
	}

	protected void downloadBookmarks() {
		new BookmarkDownloader(this, mApi, new DownloadReciever<Bookmark>() {
			@Override
			public void recieved(List<Bookmark> results) {
				showToast("Downloaded: " + results);
			}

			@Override
			public void failed(DropboxIssue dropboxIssue) {
				showToast("Failed");
			}
		}).execute();
	}

	@Override
	protected void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
