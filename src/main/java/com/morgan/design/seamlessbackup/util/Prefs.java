package com.morgan.design.seamlessbackup.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

	static final String PREF_MAX_BACK_UP_FILES_TO_KEEP = "max_back_up_files_to_keep";

	public static SharedPreferences getPrefs(Context mContext) {
		return PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	public static int getMaxBackUpFilesToKeep(Context mContext) {
		return getPrefs(mContext).getInt(PREF_MAX_BACK_UP_FILES_TO_KEEP, 5);
	}

}
