package com.morgan.design.seamlessbackup.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class DropboxPrefs {

	// You don't need to change these, leave them alone.
	private final static String ACCOUNT_PREFS_NAME = "prefs";
	private final static String ACCESS_KEY_NAME = "ACCESS_KEY";
	private final static String ACCESS_SECRET_NAME = "ACCESS_SECRET";

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a local store, rather than storing user name
	 * & password, and re-authenticating each time (which is not to be done, ever).
	 */
	protected static void storeKeys(Context context, String key, String secret) {
		// Save the access key for later
		SharedPreferences prefs = context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.putString(ACCESS_KEY_NAME, key);
		edit.putString(ACCESS_SECRET_NAME, secret);
		edit.commit();
	}

	/**
	 * Clears down the saved dropbox preferences
	 * 
	 * @param context
	 */
	protected static void clearKeys(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a local store, rather than storing user name
	 * & password, and re-authenticating each time (which is not to be done, ever).
	 * 
	 * @return Array of [access_key, access_secret], or null if none stored
	 */
	protected static String[] getKeys(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(ACCESS_KEY_NAME, null);
		String secret = prefs.getString(ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		}
		else {
			return null;
		}
	}

}
