package com.morgan.design.seamlessbackup.domain.mapper;

import android.database.Cursor;

abstract class AbstractMappingHelper {

	protected static int getInt(Cursor mCursor, String column) {
		return mCursor.getInt(mCursor.getColumnIndex(column));
	}

	protected static String getString(Cursor mCursor, String column) {
		return mCursor.getString(mCursor.getColumnIndex(column));
	}

	protected static byte[] getBlob(Cursor mCursor, String column) {
		return mCursor.getBlob(mCursor.getColumnIndex(column));
	}

	protected static float getFloat(Cursor mCursor, String column) {
		return mCursor.getFloat(mCursor.getColumnIndex(column));
	}

	protected static short getShort(Cursor mCursor, String column) {
		return mCursor.getShort(mCursor.getColumnIndex(column));
	}
}
