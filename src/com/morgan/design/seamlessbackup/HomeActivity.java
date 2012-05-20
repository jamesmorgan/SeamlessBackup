package com.morgan.design.seamlessbackup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.TokenPair;
import com.morgan.design.seamlessbackup.util.DropboxConfig;
import com.morgan.design.seamlessbackup.util.Logger;

public class HomeActivity extends Activity {

	private final String TAG = HomeActivity.this.getClass().getName();

	protected DropboxAPI<AndroidAuthSession> mApi;

	private Button mLinkDropboxAccount;
	private Button mQuickBackup;
	private Button mScheduler;
	private Button mSettings;
	private LinearLayout mLoggedInContainer;

	private boolean mLoggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mApi = ((SeamlessBackupApplication) getApplication()).getDropboxApi();

		mLoggedInContainer = (LinearLayout) findViewById(R.id.logged_in_display);
		mLinkDropboxAccount = (Button) findViewById(R.id.auth_button);
		mQuickBackup = (Button) findViewById(R.id.quick_backup);
		mScheduler = (Button) findViewById(R.id.scheduler);
		mSettings = (Button) findViewById(R.id.settings);

		mLinkDropboxAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLoggedIn) {
					logOut();
				}
				else {
					logIn();
				}
			}
		});

		mQuickBackup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this, QuickBackupActivity.class));
			}
		});

		// Display the proper UI state if logged in or not
		setLoggedIn(mApi.getSession().isLinked());
	}

	@Override
	protected void onResume() {
		super.onResume();
		AndroidAuthSession session = mApi.getSession();

		// The next part must be inserted in the onResume() method of the
		// activity from which session.startAuthentication() was called, so
		// that Dropbox authentication completes properly.
		if (session.authenticationSuccessful()) {
			try {
				// Mandatory call to complete the auth
				session.finishAuthentication();

				// Store it locally in our app for later use
				TokenPair tokens = session.getAccessTokenPair();
				DropboxConfig.storeKeys(this, tokens.key, tokens.secret);
				setLoggedIn(true);
			}
			catch (IllegalStateException e) {
				Toast.makeText(this, "Couldn't authenticate with Dropbox:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				Logger.i(TAG, "Error authenticating", e);
			}
		}
	}

	private void logIn() {
		// Start the remote authentication
		mApi.getSession().startAuthentication(HomeActivity.this);
	}

	private void logOut() {
		mApi.getSession().unlink();// Remove credentials from the session
		DropboxConfig.clearKeys(this); // Clear our stored keys
		setLoggedIn(false); // Change UI state to display logged out version
	}

	private void setLoggedIn(boolean loggedIn) {
		mLoggedIn = loggedIn;
		if (loggedIn) {
			mLinkDropboxAccount.setText("Unlink from Dropbox");
			mLoggedInContainer.setVisibility(View.VISIBLE);
		}
		else {
			mLinkDropboxAccount.setText("Link with Dropbox");
			mLoggedInContainer.setVisibility(View.GONE);
		}
	}
}
