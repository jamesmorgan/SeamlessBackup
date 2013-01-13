package com.morgan.design.seamlessbackup.util;

import java.util.Properties;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.morgan.design.seamlessbackup.SeamlessBackupApplication;

public class DropboxConfig {

	protected static final String TAG = "DropboxConfig";

	public final static AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

	/**
	 * Builds and Configures the appropriate dropbox session, setting the default {@link AccessType} to <code>AccessType.APP_FOLDER</code> level. An We
	 * AuthSession is required so that we can use the Dropbox API.
	 * 
	 * @return {@link AndroidAuthSession} the configured session
	 */
	public static AndroidAuthSession buildSession(final Context context) {
		final AppKeyPair appKeyPair = DropboxConfig.loadAppKeyPair(context);
		AndroidAuthSession session;

		final String[] stored = DropboxPrefs.getKeys(context);
		if (stored != null) {
			final AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
			session = new AndroidAuthSession(appKeyPair, DropboxConfig.ACCESS_TYPE, accessToken);
		}
		else {
			session = new AndroidAuthSession(appKeyPair, DropboxConfig.ACCESS_TYPE);
		}
		return session;
	}

	/**
	 * Retrieves the {@link DropboxAPI} from the {@link Application} associated to the activity. Returns the singleton, use this in conjunction with
	 * <code>DropboxConfig.onResume</code> method to ensure an activity depending on Dropbox works as expected
	 * 
	 * @return the {@link DropboxAPI} to use with the given {@link Activity}
	 */
	public static DropboxAPI<AndroidAuthSession> getApi(final Activity activity) {
		return ((SeamlessBackupApplication) activity.getApplication()).getDropboxApi();
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a local store, rather than storing user name & password, and re-authenticating each
	 * time (which is not to be done, ever).
	 */
	public static void storeKeys(final Context context, final String key, final String secret) {
		DropboxPrefs.storeKeys(context, key, secret);
	}

	/**
	 * Clears down the saved dropbox preferences
	 * 
	 * @param context
	 */
	public static void clearKeys(final Context context) {
		DropboxPrefs.clearKeys(context);
	}

	private static AppKeyPair loadAppKeyPair(final Context context) {
		final PropertiesReader reader = new PropertiesReader(context);
		final Properties properties = reader.getProperties();

		return new AppKeyPair(properties.getProperty("dropbox.api.key"), properties.getProperty("dropbox.api.secret"));
	}

}
