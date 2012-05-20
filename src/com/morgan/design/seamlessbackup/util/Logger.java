package com.morgan.design.seamlessbackup.util;

import android.util.Log;

public class Logger {

	public static void d(String tag, Object toLog) {
		if (Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, toLog.toString());
		}
	}

	public static void i(String tag, Object toLog) {
		if (Log.isLoggable(tag, Log.INFO)) {
			Log.i(tag, toLog.toString());
		}
	}

	public static void i(String tag, String logMsg, Throwable e) {
		if (Log.isLoggable(tag, Log.INFO)) {
			Log.i(tag, logMsg, e);
		}
	}

	public static void e(String tag, String logMsg, Throwable e) {
		Log.e(tag, logMsg, e);
	}

}
