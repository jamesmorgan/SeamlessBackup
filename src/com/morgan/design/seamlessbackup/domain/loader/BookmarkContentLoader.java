package com.morgan.design.seamlessbackup.domain.loader;

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

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.domain.mapper.DomainMappingFactory;
import com.morgan.design.seamlessbackup.util.Logger;

public class BookmarkContentLoader implements ContentLoader<List<Bookmark>> {

	public static final String TAG = "ContentLoader";

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

		//@formatter:off
		final Cursor mCursor = CursorBuilder.create(context)
				.query(CONTENT_URI)
				.withColumns(BOOKMARK_COLUMNS)
				.createCursor();
		//@formatter:on

		if (null == mCursor) {
			// Some providers return null if an error occurs, others throw an exception
			Logger.i(TAG, "Possible Error, cursor is null, no bookmark entries found");
			return NONE_FOUND;
		}
		else if (mCursor.getCount() < 1) {
			Logger.i(TAG, "No bookmark entries found");
			return NONE_FOUND;
		}
		Logger.i(TAG, String.format("Found %s entries", mCursor.getCount()));

		return DomainMappingFactory.mapBookmarkList(mCursor);
	}

	@Override
	public void updateContent(List<Bookmark> content, Context context) {
		// TODO Auto-generated method stub

	}

}
