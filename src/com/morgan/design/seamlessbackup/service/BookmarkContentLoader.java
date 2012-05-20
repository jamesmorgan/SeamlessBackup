package com.morgan.design.seamlessbackup.service;

import static android.provider.BaseColumns._ID;
import static android.provider.Browser.BookmarkColumns.BOOKMARK;
import static android.provider.Browser.BookmarkColumns.CREATED;
import static android.provider.Browser.BookmarkColumns.DATE;
import static android.provider.Browser.BookmarkColumns.FAVICON;
import static android.provider.Browser.BookmarkColumns.TITLE;
import static android.provider.Browser.BookmarkColumns.URL;
import static android.provider.Browser.BookmarkColumns.VISITS;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser;
import android.util.Base64;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.util.Logger;

public class BookmarkContentLoader extends AbstractContentLoader implements ContentLoader<List<Bookmark>> {

	private static final Uri CONTENT_URI = Browser.BOOKMARKS_URI;
	private static final List<Bookmark> NONE_FOUND = Lists.newArrayList();

	//@formatter:off
	private static String[] BOOKMARK_COLUMNS = { 
		_ID, 
		BOOKMARK, 
		CREATED, 
		DATE, 
		FAVICON, 
		TITLE, 
		URL, 
		VISITS
	};

	private static String[] ID_ONLY_COLUMN = { 
		_ID
	};
	//@formatter:on

	@Override
	public List<Bookmark> loadContent(Context context) {

		Cursor mCursor = null;
		try {
			//@formatter:off
			mCursor = context.getContentResolver().query(
					CONTENT_URI, 
					BOOKMARK_COLUMNS, // The columns to return for each row
					null, // Either null, or the word the user entered
					null, // Either empty/null, or the string the user entered
					null); // Sort order
			//@formatter:on
		}
		catch (Exception e) {
			Logger.e(TAG, "Exception thrown looking up bookmark content", e);
			return NONE_FOUND;
		}

		if (null == mCursor) {
			// Some providers return null if an error occurs, others throw an exception
			Logger.i(TAG, "Possible Error, cursor is null, no bookmark entries found");
			return NONE_FOUND;
		}
		else if (mCursor.getCount() < 1) {
			Logger.i(TAG, "No bookmark entries found");
			return NONE_FOUND;
		}
		else {
			Logger.i(TAG, String.format("Found %s entries", mCursor.getCount()));

			List<Bookmark> foundConent = Lists.newArrayList();

			while (mCursor.moveToNext()) {

				Bookmark bookmark = new Bookmark();
				bookmark.setId(getInt(mCursor, _ID));
				bookmark.setBookmark(getString(mCursor, BOOKMARK));
				bookmark.setCreated(getString(mCursor, CREATED));
				bookmark.setDate(getString(mCursor, DATE));
				byte[] favIcon = getBlob(mCursor, FAVICON);
				if (null != favIcon) {
					bookmark.setFavIcon(Base64.encodeToString(favIcon, Base64.URL_SAFE));
				}
				bookmark.setTitle(getString(mCursor, TITLE));
				bookmark.setUrl(getString(mCursor, URL));
				bookmark.setVisits(getString(mCursor, VISITS));

				Logger.d(TAG, "==================================");
				Logger.d(TAG, bookmark);
				Logger.d(TAG, "==================================");

				foundConent.add(bookmark);
			}
			return foundConent;
		}
	}

	@Override
	public void updateContent(List<Bookmark> content, Context context) {
		// TODO Auto-generated method stub

	}

}
