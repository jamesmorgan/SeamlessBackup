package com.morgan.design.seamlessbackup;

import java.io.File;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;
import com.morgan.design.seamlessbackup.domain.SearchHistory;
import com.morgan.design.seamlessbackup.domain.loader.BookmarkContentLoader;
import com.morgan.design.seamlessbackup.domain.loader.SearchHistoryContentLoader;
import com.morgan.design.seamlessbackup.domain.loader.WordDictionaryContentLoader;
import com.morgan.design.seamlessbackup.dropbox.DropboxDownloader;
import com.morgan.design.seamlessbackup.dropbox.DropboxDownloader.OnDownloadedListener;
import com.morgan.design.seamlessbackup.dropbox.DropboxUploader;
import com.morgan.design.seamlessbackup.service.BackupCreator;

public class QuickBackupActivity extends AbstractAuthenticatedActivity {

	private final String TAG = QuickBackupActivity.this.getClass().getSimpleName();

	private Button mSyncDictionary;
	private Button mDownloadDictionary;

	private Button mSyncSearchHistory;
	private Button mDownloadSearchHistory;

	private Button mSyncBookmarks;
	private Button mDownloadBookmarks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quick_backup);

		mSyncDictionary = (Button) findViewById(R.id.sync_dictionary_button);
		mDownloadDictionary = (Button) findViewById(R.id.download_dictionary_button);

		mSyncBookmarks = (Button) findViewById(R.id.sync_bookmarks_button);
		mDownloadBookmarks = (Button) findViewById(R.id.download_bookmarks_button);

		mSyncSearchHistory = (Button) findViewById(R.id.sync_search_history_button);
		mDownloadSearchHistory = (Button) findViewById(R.id.download_search_history_button);

		mSyncDictionary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				syncDictionary();
			}
		});

		mSyncBookmarks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				syncBookmarks();
			}
		});

		mSyncSearchHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				syncSearchHistory();
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
		new DropboxDownloader(this, mApi, BackupType.DICTIONARY.dir(), new OnDownloadedListener() {
			@Override
			public void onDownloaded(List<DictionaryWord> words) {
				if (null != words) {
					showToast("Downloaded!!!!");
					new WordDictionaryContentLoader().updateContent(words, QuickBackupActivity.this);
				}
			}
		}).execute();
	}

	@Override
	protected void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
