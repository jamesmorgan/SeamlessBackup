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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser;
import android.util.Base64;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.domain.mapper.DomainMappingFactory;

public class BookmarkContentLoader implements ContentLoader<List<Bookmark>> {

	private static Logger log = LoggerFactory.getLogger(BookmarkContentLoader.class);

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
			log.info("Possible Error, cursor is null, no bookmark entries found");
			return NONE_FOUND;
		}
		else if (mCursor.getCount() < 1) {
			log.info("No bookmark entries found");
			return NONE_FOUND;
		}
		log.info("Found {} entries", mCursor.getCount());

		return DomainMappingFactory.mapBookmarkList(mCursor);
	}

	@Override
	public void updateContent(List<Bookmark> content, Context context) {
		ContentResolver contentResolver = context.getContentResolver();

		System.out.println("Attempting to apply bookmarks, total found: " + content.size());

		for (Bookmark bookmark : content) {

			//@formatter:off
			final Cursor matchedWord = CursorBuilder.create(context)
					.query(CONTENT_URI)
					.withColumns(ID_ONLY_COLUMN)
					.where(URL + " = ?")
					.whereArgs(new String[] { bookmark.getUrl() })
					.createCursor();
			//@formatter:on	

			if (null == matchedWord) {
				log.info("Possible Error, cursor is null, no Bookmark entries found");
			}
			// No match, insert new row
			else if (0 == matchedWord.getCount()) {
				log.info("Inserting New Bookmark: {}", bookmark.getUrl());
				contentResolver.insert(CONTENT_URI, createStatementValues(bookmark));
			}
			// Found direct match
			else if (1 == matchedWord.getCount()) {
				log.info("Found matching Bookmark, updating: {}", bookmark.getTitle());

				matchedWord.moveToFirst();

				int idIndex = matchedWord.getColumnIndex(_ID);
				int idOfMatchedWord = matchedWord.getInt(idIndex);

				// Defines selection criteria for the rows you want to update
				String mUpdateClause = _ID + " = ?";
				String[] mUpdateArgs = { Integer.toString(idOfMatchedWord) };

				int mRowsUpdated = contentResolver.update(CONTENT_URI, createStatementValues(bookmark), mUpdateClause, mUpdateArgs);
				log.info("Bookmark Rows updated: {}", mRowsUpdated);
			}
			else {
				// Multiple matches, TODO handle this?
				System.out.println("Multiple matches, TODO handle this?");
			}
		}
	}

	private ContentValues createStatementValues(Bookmark bookmark) {
		ContentValues mInsertValues = new ContentValues();
		mInsertValues.put(BOOKMARK, bookmark.getBookmark());
		mInsertValues.put(CREATED, bookmark.getCreated());
		mInsertValues.put(DATE, bookmark.getDate());
		if (null != bookmark.getFavIcon()) {
			mInsertValues.put(FAVICON, Base64.decode(bookmark.getFavIcon(), Base64.URL_SAFE));
		}
		mInsertValues.put(TITLE, bookmark.getTitle());
		mInsertValues.put(URL, bookmark.getUrl());
		mInsertValues.put(VISITS, bookmark.getVisits());
		return mInsertValues;
	}
}
