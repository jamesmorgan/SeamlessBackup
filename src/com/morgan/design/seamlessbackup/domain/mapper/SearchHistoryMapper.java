package com.morgan.design.seamlessbackup.domain.mapper;

import static android.provider.BaseColumns._ID;
import static android.provider.Browser.SearchColumns.DATE;
import static android.provider.Browser.SearchColumns.SEARCH;

import java.util.List;

import android.database.Cursor;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.SearchHistory;
import com.morgan.design.seamlessbackup.util.Logger;

public class SearchHistoryMapper extends AbstractMappingHelper {

	public static final String TAG = "SearchHistoryMapper";

	public List<SearchHistory> mapCursor(Cursor mCursor) {

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
