package com.morgan.design.seamlessbackup.util;

import java.util.Properties;

import android.content.Context;

import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

public class DropboxConfig {

	public final static AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

	/**
	 * Builds and Configures the appropriate dropbox session, setting the default {@link AccessType} to
	 * <code>AccessType.APP_FOLDER</code> level. An We AuthSession is required so that we can use the Dropbox API.
	 * 
	 * @return {@link AndroidAuthSession} the configured session
	 */
	public static AndroidAuthSession buildSession(Context context) {
		AppKeyPair appKeyPair = DropboxConfig.loadAppKeyPair(context);
		AndroidAuthSession session;

		String[] stored = DropboxPrefs.getKeys(context);
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
			session = new AndroidAuthSession(appKeyPair, DropboxConfig.ACCESS_TYPE, accessToken);
		}
		else {
			session = new AndroidAuthSession(appKeyPair, DropboxConfig.ACCESS_TYPE);
		}
		return session;
	}

	private static AppKeyPair loadAppKeyPair(Context context) {
		PropertiesReader reader = new PropertiesReader(context);
		Properties properties = reader.getProperties();

		return new AppKeyPair(properties.getProperty("dropbox.api.key"), properties.getProperty("dropbox.api.secret"));
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a local store, rather than storing user name
	 * & password, and re-authenticating each time (which is not to be done, ever).
	 */
	public static void storeKeys(Context context, String key, String secret) {
		DropboxPrefs.storeKeys(context, key, secret);
	}

	/**
	 * Clears down the saved dropbox preferences
	 * 
	 * @param context
	 */
	public static void clearKeys(Context context) {
		DropboxPrefs.clearKeys(context);
	}

}
