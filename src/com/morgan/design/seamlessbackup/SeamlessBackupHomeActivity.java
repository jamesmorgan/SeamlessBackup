package com.morgan.design.seamlessbackup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.morgan.design.seamlessbackup.util.DropboxConfig;

public class SeamlessBackupHomeActivity extends Activity {

	private final String TAG = SeamlessBackupHomeActivity.this.getClass().getName();

	private static final String SEAMLESS_BACKUP_DIR = "/SeamlessBackup/";

	private static final String DICTIONARY_PATH = "/Dictionary/";
	private static final String BOOKMARKS_PATH = "/Browser/Bookmarks/";
	private static final String SEARCH_HISTORY_PATH = "/Browser/SearchHistory/";

	DropboxAPI<AndroidAuthSession> mApi;

	// Android widgets
	private Button mSubmit;
	private Button mSyncDictionary;
	private Button mDownloadDictionary;
	private Button mSyncSearchHistory;
	private Button mSyncBookmarks;

	private LinearLayout mDisplay;

	// Flags
	private boolean mLoggedIn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mApi = ((SeamlessBackupApplication) getApplication()).getDropboxApi();

		mDisplay = (LinearLayout) findViewById(R.id.logged_in_display);
		mSubmit = (Button) findViewById(R.id.auth_button);
		mSyncDictionary = (Button) findViewById(R.id.sync_dictionary_button);
		mDownloadDictionary = (Button) findViewById(R.id.download_dictionary_button);

		mSyncBookmarks = (Button) findViewById(R.id.sync_bookmarks_button);
		mSyncSearchHistory = (Button) findViewById(R.id.sync_search_history_button);

		// mSyncDictionary.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// syncDictionary();
		// }
		// });
		//
		// mSyncBookmarks.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// syncBookmarks();
		// }
		// });
		//
		// mSyncSearchHistory.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// syncSearchHistory();
		// }
		// });
		//
		// mDownloadDictionary.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// downloadDictionary();
		// }
		// });

		mSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// This logs you out if you're logged in, or vice versa
				if (mLoggedIn) {
					logOut();
				}
				else {
					logIn();
				}
			}
		});

		// Display the proper UI state if logged in or not
		setLoggedIn(mApi.getSession().isLinked());
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// AndroidAuthSession session = mApi.getSession();
	//
	// // The next part must be inserted in the onResume() method of the
	// // activity from which session.startAuthentication() was called, so
	// // that Dropbox authentication completes properly.
	// if (session.authenticationSuccessful()) {
	// try {
	// // Mandatory call to complete the auth
	// session.finishAuthentication();
	//
	// // Store it locally in our app for later use
	// TokenPair tokens = session.getAccessTokenPair();
	// DropboxConfig.storeKeys(this, tokens.key, tokens.secret);
	// setLoggedIn(true);
	// }
	// catch (IllegalStateException e) {
	// showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
	// Log.i(TAG, "Error authenticating", e);
	// }
	// }
	// }

	// protected void syncSearchHistory() {
	// List<SearchHistory> loadContent = new SearchHistoryContentLoader().loadContent(this);
	// try {
	//
	// Gson gson = new GsonBuilder().setPrettyPrinting().create();
	// String json = gson.toJson(loadContent);
	//
	// File sdCard = Environment.getExternalStorageDirectory();
	// File dir = new File(sdCard.getAbsolutePath() + SEAMLESS_BACKUP_DIR);
	// if (!dir.exists()) {
	// Log.i(TAG, "Making DIR as not proesent");
	// dir.mkdirs();
	// }
	//
	// // ##### Write a file to the disk #####
	// File file = new File(dir, "bookmarks.json");
	// FileOutputStream fOut = new FileOutputStream(file);
	//
	// /*
	// * We have to use the openFileOutput()-method the ActivityContext provides, to protect your file from others
	// * and This is done for security-reasons. We chose MODE_WORLD_READABLE, because we have nothing to hide in
	// * our file
	// */
	// OutputStreamWriter osw = new OutputStreamWriter(fOut);
	//
	// // Write the string to the file
	// osw.write(json);
	// /*
	// * ensure that everything is really written out and close
	// */
	// osw.flush();
	// osw.close();
	//
	// new DropboxUploader(this, mApi, SEARCH_HISTORY_PATH, file).execute();
	// }
	// catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// protected void syncBookmarks() {
	// List<Bookmark> loadContent = new BookmarkContentLoader().loadContent(this);
	// try {
	//
	// Gson gson = new GsonBuilder().setPrettyPrinting().create();
	// String json = gson.toJson(loadContent);
	//
	// File sdCard = Environment.getExternalStorageDirectory();
	// File dir = new File(sdCard.getAbsolutePath() + SEAMLESS_BACKUP_DIR);
	// if (!dir.exists()) {
	// Log.i(TAG, "Making DIR as not proesent");
	// dir.mkdirs();
	// }
	//
	// // ##### Write a file to the disk #####
	// File file = new File(dir, "bookmarks.json");
	// FileOutputStream fOut = new FileOutputStream(file);
	//
	// /*
	// * We have to use the openFileOutput()-method the ActivityContext provides, to protect your file from others
	// * and This is done for security-reasons. We chose MODE_WORLD_READABLE, because we have nothing to hide in
	// * our file
	// */
	// OutputStreamWriter osw = new OutputStreamWriter(fOut);
	//
	// // Write the string to the file
	// osw.write(json);
	// /*
	// * ensure that everything is really written out and close
	// */
	// osw.flush();
	// osw.close();
	//
	// new DropboxUploader(this, mApi, BOOKMARKS_PATH, file).execute();
	// }
	// catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// protected void downloadDictionary() {
	//
	// new DropboxDownloader(this, mApi, DICTIONARY_PATH, new OnDownloadedListener() {
	// @Override
	// public void onDownloaded(List<DictionaryWord> words) {
	// if (null != words) {
	// showToast("Downloaded!!!!");
	// System.out.println("Downloaded: " + words);
	// new WordDictionaryContentLoader().updateContent(words, SeamlessBackupHomeActivity.this);
	// }
	// }
	// }).execute();
	// }
	//
	// protected void syncDictionary() {
	// List<DictionaryWord> loadContent = new WordDictionaryContentLoader().loadContent(this);
	//
	// try {
	//
	// Gson gson = new GsonBuilder().setPrettyPrinting().create();
	// String json = gson.toJson(loadContent);
	//
	// File sdCard = Environment.getExternalStorageDirectory();
	// File dir = new File(sdCard.getAbsolutePath() + SEAMLESS_BACKUP_DIR);
	// if (!dir.exists()) {
	// Log.i(TAG, "Making DIR as not proesent");
	// dir.mkdirs();
	// }
	//
	// // ##### Write a file to the disk #####
	// File file = new File(dir, "dictionary.json");
	// FileOutputStream fOut = new FileOutputStream(file);
	//
	// /*
	// * We have to use the openFileOutput()-method the ActivityContext provides, to protect your file from others
	// * and This is done for security-reasons. We chose MODE_WORLD_READABLE, because we have nothing to hide in
	// * our file
	// */
	// OutputStreamWriter osw = new OutputStreamWriter(fOut);
	//
	// // Write the string to the file
	// osw.write(json);
	// /*
	// * ensure that everything is really written out and close
	// */
	// osw.flush();
	// osw.close();
	//
	// new DropboxUploader(this, mApi, DICTIONARY_PATH, file).execute();
	// }
	// catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	private void logIn() {
		// Start the remote authentication
		mApi.getSession().startAuthentication(SeamlessBackupHomeActivity.this);
	}

	private void logOut() {
		// Remove credentials from the session
		mApi.getSession().unlink();
		// Clear our stored keys
		DropboxConfig.clearKeys(this);
		// Change UI state to display logged out version
		setLoggedIn(false);
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * Convenience function to change UI state based on being logged in
	 */
	private void setLoggedIn(boolean loggedIn) {
		mLoggedIn = loggedIn;
		if (loggedIn) {
			mSubmit.setText("Unlink from Dropbox");
			mDisplay.setVisibility(View.VISIBLE);
		}
		else {
			mSubmit.setText("Link with Dropbox");
			mDisplay.setVisibility(View.GONE);
		}
	}

}
