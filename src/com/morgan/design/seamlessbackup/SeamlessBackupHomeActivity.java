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
