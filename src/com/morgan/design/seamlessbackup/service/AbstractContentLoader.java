package com.morgan.design.seamlessbackup.service;

import android.database.Cursor;

public abstract class AbstractContentLoader {

	public static final String TAG = "ContentLoader";

	protected int getInt(Cursor mCursor, String column) {
		return mCursor.getInt(mCursor.getColumnIndex(column));
	}

	protected String getString(Cursor mCursor, String column) {
		return mCursor.getString(mCursor.getColumnIndex(column));
	}

	protected byte[] getBlob(Cursor mCursor, String column) {
		return mCursor.getBlob(mCursor.getColumnIndex(column));
	}

	protected float getFloat(Cursor mCursor, String column) {
		return mCursor.getFloat(mCursor.getColumnIndex(column));
	}

	protected short getShort(Cursor mCursor, String column) {
		return mCursor.getShort(mCursor.getColumnIndex(column));
	}
}
