package com.morgan.design.seamlessbackup.domain.mapper;

import static android.provider.BaseColumns._ID;
import static android.provider.Browser.BookmarkColumns.BOOKMARK;
import static android.provider.Browser.BookmarkColumns.CREATED;
import static android.provider.Browser.BookmarkColumns.DATE;
import static android.provider.Browser.BookmarkColumns.FAVICON;
import static android.provider.Browser.BookmarkColumns.TITLE;
import static android.provider.Browser.BookmarkColumns.URL;
import static android.provider.Browser.BookmarkColumns.VISITS;

import java.util.List;

import android.database.Cursor;
import android.util.Base64;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.util.Logger;

public class BookmarkMapper extends AbstractMappingHelper {

	public static final String TAG = "BookmarkMapper";

	public List<Bookmark> mapCursor(Cursor mCursor) {

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
