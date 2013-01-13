package com.morgan.design.seamlessbackup.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class DropboxPrefs {

	// These values should not need to be changed
	private final static String ACCOUNT_PREFS_NAME = "prefs";
	private final static String ACCESS_KEY_NAME = "ACCESS_KEY";
	private final static String ACCESS_SECRET_NAME = "ACCESS_SECRET";

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a local store, rather than storing user name & password, and re-authenticating each
	 * time (which is not to be done, ever).
	 */
	protected static void storeKeys(final Context context, final String key, final String secret) {
		// Save the access key for later
		final SharedPreferences prefs = context.getSharedPreferences(ACCOUNT_PREFS_NAME, Context.MODE_PRIVATE);
		final Editor edit = prefs.edit();
		edit.putString(ACCESS_KEY_NAME, key);
		edit.putString(ACCESS_SECRET_NAME, secret);
		edit.commit();
	}

	/**
	 * Clears down the saved dropbox preferences
	 * 
	 * @param context
	 */
	protected static void clearKeys(final Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(ACCOUNT_PREFS_NAME, Context.MODE_PRIVATE);
		final Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a local store, rather than storing user name & password, and re-authenticating each
	 * time (which is not to be done, ever).
	 * 
	 * @return Array of [access_key, access_secret], or null if none stored
	 */
	protected static String[] getKeys(final Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(ACCOUNT_PREFS_NAME, Context.MODE_PRIVATE);
		final String key = prefs.getString(ACCESS_KEY_NAME, null);
		final String secret = prefs.getString(ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			final String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		}
		return null;
	}

}
