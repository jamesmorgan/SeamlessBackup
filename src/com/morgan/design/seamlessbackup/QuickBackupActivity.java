package com.morgan.design.seamlessbackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;
import com.morgan.design.seamlessbackup.domain.SearchHistory;
import com.morgan.design.seamlessbackup.dropbox.DropboxDownloader;
import com.morgan.design.seamlessbackup.dropbox.DropboxDownloader.OnDownloadedListener;
import com.morgan.design.seamlessbackup.dropbox.DropboxUploader;
import com.morgan.design.seamlessbackup.service.BookmarkContentLoader;
import com.morgan.design.seamlessbackup.service.SearchHistoryContentLoader;
import com.morgan.design.seamlessbackup.service.WordDictionaryContentLoader;
import com.morgan.design.seamlessbackup.util.Logger;

public class QuickBackupActivity extends Activity {

	private final String TAG = QuickBackupActivity.this.getClass().getSimpleName();

	private static final String SEAMLESS_BACKUP_DIR = "/SeamlessBackup/";

	private static final String DICTIONARY_PATH = "/Dictionary/";
	private static final String BOOKMARKS_PATH = "/Browser/Bookmarks/";
	private static final String SEARCH_HISTORY_PATH = "/Browser/SearchHistory/";

	protected DropboxAPI<AndroidAuthSession> mApi;

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

		mApi = ((SeamlessBackupApplication) getApplication()).getDropboxApi();

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
		try {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(loadContent);

			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + SEAMLESS_BACKUP_DIR);
			if (!dir.exists()) {
				Logger.i(TAG, "Making DIR as not proesent");
				dir.mkdirs();
			}

			// ##### Write a file to the disk #####
			File file = new File(dir, "bookmarks.json");
			FileOutputStream fOut = new FileOutputStream(file);

			/*
			 * We have to use the openFileOutput()-method the ActivityContext provides, to protect your file from others
			 * and This is done for security-reasons. We chose MODE_WORLD_READABLE, because we have nothing to hide in
			 * our file
			 */
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// Write the string to the file
			osw.write(json);
			/*
			 * ensure that everything is really written out and close
			 */
			osw.flush();
			osw.close();

			new DropboxUploader(this, mApi, SEARCH_HISTORY_PATH, file).execute();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void syncBookmarks() {
		List<Bookmark> loadContent = new BookmarkContentLoader().loadContent(this);
		try {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(loadContent);

			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + SEAMLESS_BACKUP_DIR);
			if (!dir.exists()) {
				Logger.i(TAG, "Making DIR as not proesent");
				dir.mkdirs();
			}

			// ##### Write a file to the disk #####
			File file = new File(dir, "bookmarks.json");
			FileOutputStream fOut = new FileOutputStream(file);

			/*
			 * We have to use the openFileOutput()-method the ActivityContext provides, to protect your file from others
			 * and This is done for security-reasons. We chose MODE_WORLD_READABLE, because we have nothing to hide in
			 * our file
			 */
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// Write the string to the file
			osw.write(json);
			/*
			 * ensure that everything is really written out and close
			 */
			osw.flush();
			osw.close();

			new DropboxUploader(this, mApi, BOOKMARKS_PATH, file).execute();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void downloadDictionary() {

		new DropboxDownloader(this, mApi, DICTIONARY_PATH, new OnDownloadedListener() {
			@Override
			public void onDownloaded(List<DictionaryWord> words) {
				if (null != words) {
					showToast("Downloaded!!!!");
					new WordDictionaryContentLoader().updateContent(words, QuickBackupActivity.this);
				}
			}
		}).execute();
	}

	protected void syncDictionary() {
		List<DictionaryWord> loadContent = new WordDictionaryContentLoader().loadContent(this);

		try {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(loadContent);

			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + SEAMLESS_BACKUP_DIR);
			if (!dir.exists()) {
				Logger.i(TAG, "Making DIR as not proesent");
				dir.mkdirs();
			}

			// ##### Write a file to the disk #####
			File file = new File(dir, "dictionary.json");
			FileOutputStream fOut = new FileOutputStream(file);

			/*
			 * We have to use the openFileOutput()-method the ActivityContext provides, to protect your file from others
			 * and This is done for security-reasons. We chose MODE_WORLD_READABLE, because we have nothing to hide in
			 * our file
			 */
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// Write the string to the file
			osw.write(json);
			/*
			 * ensure that everything is really written out and close
			 */
			osw.flush();
			osw.close();

			new DropboxUploader(this, mApi, DICTIONARY_PATH, file).execute();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
