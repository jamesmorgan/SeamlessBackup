package com.morgan.design.seamlessbackup.domain.loader;

import static android.provider.BaseColumns._ID;
import static android.provider.Browser.SearchColumns.DATE;
import static android.provider.Browser.SearchColumns.SEARCH;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.SearchHistory;
import com.morgan.design.seamlessbackup.domain.mapper.DomainMappingFactory;

public class SearchHistoryContentLoader implements ContentLoader<SearchHistory> {

	private static Logger log = LoggerFactory.getLogger(WordDictionaryContentLoader.class);

	private static final Uri CONTENT_URI = Browser.SEARCHES_URI;
	private static final List<SearchHistory> NONE_FOUND = Lists.newArrayList();

	private static String[] SEACH_HISTORY_COLUMNS = { _ID, DATE, SEARCH };

	private static String[] ID_ONLY_COLUMN = { _ID };

	@Override
	public List<SearchHistory> loadContent(Context context) {

		//@formatter:off
		final Cursor mCursor = CursorBuilder.create(context)
				.query(CONTENT_URI)
				.withColumns(SEACH_HISTORY_COLUMNS)
				.createCursor();
		//@formatter:on

		if (null == mCursor) {
			// Some providers return null if an error occurs, others throw an exception
			log.info("Possible Error, cursor is null, no search history entries found");
			return NONE_FOUND;
		}
		else if (mCursor.getCount() < 1) {
			log.info("No search history entries found");
			return NONE_FOUND;
		}

		log.info("Found {} entries", mCursor.getCount());

		return DomainMappingFactory.mapSearchHistoryList(mCursor);
	}

	@Override
	public void updateContent(List<SearchHistory> content, Context context) {
		System.out.println("Attempting to apply search history, total found: " + content.size());

		ContentResolver contentResolver = context.getContentResolver();

		for (SearchHistory searchHistory : content) {

			//@formatter:off
			final Cursor matchedWord = CursorBuilder.create(context)
					.query(CONTENT_URI)
					.withColumns(ID_ONLY_COLUMN)
					.where(SEARCH + " = ?")
					.whereArgs(new String[] { searchHistory.getSearch() })
					.createCursor();
			//@formatter:on	

			if (null == matchedWord) {
				log.info("Possible Error, cursor is null, no Search entries found");
			}
			// No match, insert new row
			else if (0 == matchedWord.getCount()) {
				log.info("Inserting New Search: {}", searchHistory.getSearch());
				contentResolver.insert(CONTENT_URI, createStatementValues(searchHistory));
			}
			// Found direct match
			else if (1 == matchedWord.getCount()) {
				log.info("Found matching word, updating: {}", searchHistory.getSearch());

				matchedWord.moveToFirst();

				int idIndex = matchedWord.getColumnIndex(_ID);
				int idOfMatchedWord = matchedWord.getInt(idIndex);

				// Defines selection criteria for the rows you want to update
				String mUpdateClause = _ID + " = ?";
				String[] mUpdateArgs = { Integer.toString(idOfMatchedWord) };

				int mRowsUpdated = contentResolver.update(CONTENT_URI, createStatementValues(searchHistory), mUpdateClause, mUpdateArgs);
				log.info("Search Rows updated: {}", mRowsUpdated);
			}
			else {
				// Multiple matches, TODO handle this?
				System.out.println("Multiple matches, TODO handle this?");
			}
		}

	}

	private ContentValues createStatementValues(SearchHistory searchHistory) {
		ContentValues mInsertValues = new ContentValues();
		mInsertValues.put(DATE, searchHistory.getDate());
		mInsertValues.put(SEARCH, searchHistory.getSearch());
		return mInsertValues;
	}
}
