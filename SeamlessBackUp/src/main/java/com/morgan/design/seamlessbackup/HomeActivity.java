package com.morgan.design.seamlessbackup;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.morgan.design.seamlessbackup.util.DropboxConfig;

@ContentView(R.layout.main)
public class HomeActivity extends AbstractAuthenticatedActivity {

	@InjectView(R.id.auth_button)
	private Button mLinkDropboxAccount;

	@InjectView(R.id.quick_backup)
	private Button mQuickBackup;

	@InjectView(R.id.quick_restore)
	private Button mQuickRestore;

	@InjectView(R.id.scheduler)
	private Button mScheduler;

	@InjectView(R.id.settings)
	private Button mSettings;

	@InjectView(R.id.logged_in_display)
	private LinearLayout mLoggedInContainer;

	private boolean mLoggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the proper UI state if logged in or not
		setLoggedIn(mApi.getSession().isLinked());
	}

	public void onLinkToDropBox(View view) {
		if (mLoggedIn) {
			logOut();
		}
		else {
			logIn();
		}
	}

	public void onStartQuickBackup(View view) {
		startActivity(new Intent(HomeActivity.this, SelectableBackupActivity.class));
		// startActivity(new Intent(HomeActivity.this, QuickBackupActivity.class));
	}

	public void onStartQuickRestore(View view) {
		startActivity(new Intent(HomeActivity.this, QuickRestoreActivity.class));
	}

	public void onStartScheduler(View view) {
		// TODO
	}

	public void onStartSettings(View view) {
		// TODO
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
