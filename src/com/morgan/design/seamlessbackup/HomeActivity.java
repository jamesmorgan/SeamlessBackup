package com.morgan.design.seamlessbackup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.morgan.design.seamlessbackup.util.DropboxConfig;

public class HomeActivity extends AbstractAuthenticatedActivity {

	private final String TAG = HomeActivity.this.getClass().getName();

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
	public void onAuthenticationSuccessful() {
		setLoggedIn(true);
	}

	@Override
	public void onAuthenticationFailed(IllegalStateException e) {
		setLoggedIn(false);
		Toast.makeText(this, "Couldn't authenticate with Dropbox:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
