package com.morgan.design.seamlessbackup.service;

import static android.provider.BaseColumns._ID;
import static android.provider.Browser.SearchColumns.DATE;
import static android.provider.Browser.SearchColumns.SEARCH;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Browser;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.SearchHistory;
import com.morgan.design.seamlessbackup.util.Logger;

public class SearchHistoryContentLoader extends AbstractContentLoader implements ContentLoader<List<SearchHistory>> {

	private static final Uri CONTENT_URI = Browser.SEARCHES_URI;
	private static final List<SearchHistory> NONE_FOUND = Lists.newArrayList();

	//@formatter:off
	private static String[] SEACH_HISTORY_COLUMNS = { 
		_ID, 
		DATE, 
		SEARCH
	};

	private static String[] ID_ONLY_COLUMN = { 
		_ID
	};
	//@formatter:on

	@Override
	public List<SearchHistory> loadContent(Context context) {
		Cursor mCursor = null;
		try {
			//@formatter:off
			mCursor = context.getContentResolver().query(
					CONTENT_URI, 
					SEACH_HISTORY_COLUMNS, // The columns to return for each row
					null, // Either null, or the word the user entered
					null, // Either empty/null, or the string the user entered
					null); // Sort order
			//@formatter:on
		}
		catch (Exception e) {
			Logger.e(TAG, "Exception thrown looking up search history content", e);
			return NONE_FOUND;
		}

		if (null == mCursor) {
			// Some providers return null if an error occurs, others throw an exception
			Logger.i(TAG, "Possible Error, cursor is null, no search history entries found");
			return NONE_FOUND;
		}
		else if (mCursor.getCount() < 1) {
			Logger.i(TAG, "No search history entries found");
			return NONE_FOUND;
		}
		else {
			Logger.i(TAG, String.format("Found %s entries", mCursor.getCount()));

			List<SearchHistory> foundConent = Lists.newArrayList();

			while (mCursor.moveToNext()) {

				SearchHistory searchHistory = new SearchHistory();
				searchHistory.setId(getInt(mCursor, _ID));
				searchHistory.setDate(getString(mCursor, DATE));
				searchHistory.setSearch(getString(mCursor, SEARCH));

				Logger.d(TAG, "==================================");
				Logger.d(TAG, searchHistory);
				Logger.d(TAG, "==================================");

				foundConent.add(searchHistory);
			}
			return foundConent;
		}
	}

	@Override
	public void updateContent(List<SearchHistory> content, Context context) {
		// TODO Auto-generated method stub

	}

}
