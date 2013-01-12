package com.morgan.design.seamlessbackup.domain.mapper;

import static android.provider.BaseColumns._ID;
import static android.provider.Browser.SearchColumns.DATE;
import static android.provider.Browser.SearchColumns.SEARCH;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.SearchHistory;

public class SearchHistoryMapper extends AbstractMappingHelper {

	private static Logger log = LoggerFactory.getLogger(SearchHistoryMapper.class);

	public List<SearchHistory> mapCursor(Cursor mCursor) {

		List<SearchHistory> foundConent = Lists.newArrayList();

		while (mCursor.moveToNext()) {

			SearchHistory searchHistory = new SearchHistory();
			searchHistory.setId(getInt(mCursor, _ID));
			searchHistory.setDate(getString(mCursor, DATE));
			searchHistory.setSearch(getString(mCursor, SEARCH));

			log.debug("==================================");
			log.debug("Created SearchHistory=[{}]", searchHistory);
			log.debug("==================================");

			foundConent.add(searchHistory);
		}

		return foundConent;
	}
}
