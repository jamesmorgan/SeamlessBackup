package com.morgan.design.seamlessbackup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboListActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.TokenPair;
import com.morgan.design.seamlessbackup.util.DropboxConfig;

public abstract class AbstractAuthenticatedListActivity extends RoboListActivity implements AuthenticatedActivity {

	private static Logger log = LoggerFactory.getLogger(AbstractAuthenticatedListActivity.class);

	protected DropboxAPI<AndroidAuthSession> mApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApi = DropboxConfig.getApi(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		/**
		 * Use this to handle loading and storing the session when an Activity is resumed, on success or failure the
		 * {@link AuthenticatedActivity} with be called back.
		 */
		AndroidAuthSession session = mApi.getSession();
		// The next part must be inserted in the onResume() method of the activity from which
		// session.startAuthentication() was called, so that Dropbox authentication completes properly.
		if (session.authenticationSuccessful()) {
			try {
				// Mandatory call to complete the auth
				session.finishAuthentication();

				// Store it locally in our app for later use
				TokenPair tokens = session.getAccessTokenPair();
				DropboxConfig.storeKeys(this, tokens.key, tokens.secret);
				onAuthenticationSuccessful();
			}
			catch (IllegalStateException e) {
				log.info("Error authenticating", e);
				onAuthenticationFailed(e);
			}
		}
	}

	@Override
	public void onAuthenticationSuccessful() {
		// Override if the activity must change or react to a successful Dropbox authentication
	}

	@Override
	public void onAuthenticationFailed(IllegalStateException e) {
		// Override if the activity must change or react to a failed Dropbox authentication
	}

	protected void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	protected void showToast(String msg, Object... args) {
		Toast.makeText(this, String.format(msg, args), Toast.LENGTH_LONG).show();
	}
}
