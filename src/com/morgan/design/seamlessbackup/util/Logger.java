package com.morgan.design.seamlessbackup.util;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static android.util.Log.isLoggable;
import android.util.Log;

public class Logger {

	public static void d(String tag, Object toLog) {
		if (isLoggable(tag, DEBUG)) {
			Log.d(tag, toLog.toString());
		}
	}

	public static void i(String tag, Object toLog) {
		if (isLoggable(tag, INFO)) {
			Log.i(tag, toLog.toString());
		}
	}

	public static void i(String tag, String logMsg, Throwable e) {
		if (isLoggable(tag, INFO)) {
			Log.i(tag, logMsg, e);
		}
	}

	public static void e(String tag, String logMsg, Throwable e) {
		Log.e(tag, logMsg, e);
	}

}
