package com.morgan.design.seamlessbackup.domain.mapper;

import java.util.List;

import android.database.Cursor;

import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;
import com.morgan.design.seamlessbackup.domain.SearchHistory;

public class DomainMappingFactory {

	public static List<DictionaryWord> mapDictionaryWordList(Cursor mCursor) {
		return new DictionaryWordMapper().mapCursor(mCursor);
	}

	public static List<SearchHistory> mapSearchHistoryList(Cursor mCursor) {
		return new SearchHistoryMapper().mapCursor(mCursor);
	}

	public static List<Bookmark> mapBookmarkList(Cursor mCursor) {
		return new BookmarkMapper().mapCursor(mCursor);
	}

}
