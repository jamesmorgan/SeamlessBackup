package com.morgan.design.seamlessbackup.domain.loader;

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
import com.morgan.design.seamlessbackup.domain.mapper.DomainMappingFactory;
import com.morgan.design.seamlessbackup.util.Logger;

public class SearchHistoryContentLoader implements ContentLoader<List<SearchHistory>> {

	public static final String TAG = "ContentLoader";

	private static final Uri SEARCHES_URI = Browser.SEARCHES_URI;
	private static final List<SearchHistory> NONE_FOUND = Lists.newArrayList();

	private static String[] SEACH_HISTORY_COLUMNS = { _ID, DATE, SEARCH };

	private static String[] ID_ONLY_COLUMN = { _ID };

	@Override
	public List<SearchHistory> loadContent(Context context) {

		//@formatter:off
		final Cursor mCursor = CursorBuilder.create(context)
				.query(SEARCHES_URI)
				.withColumns(SEACH_HISTORY_COLUMNS)
				.createCursor();
		//@formatter:on

		if (null == mCursor) {
			// Some providers return null if an error occurs, others throw an exception
			Logger.i(TAG, "Possible Error, cursor is null, no search history entries found");
			return NONE_FOUND;
		}
		else if (mCursor.getCount() < 1) {
			Logger.i(TAG, "No search history entries found");
			return NONE_FOUND;
		}

		Logger.i(TAG, String.format("Found %s entries", mCursor.getCount()));

		return DomainMappingFactory.mapSearchHistoryList(mCursor);
	}

	@Override
	public void updateContent(List<SearchHistory> content, Context context) {
		// TODO Auto-generated method stub

	}

}
