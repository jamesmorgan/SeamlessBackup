package com.morgan.design.seamlessbackup.domain;

import com.morgan.design.seamlessbackup.R;
import com.morgan.design.seamlessbackup.domain.loader.BookmarkContentLoader;
import com.morgan.design.seamlessbackup.domain.loader.ContentLoader;
import com.morgan.design.seamlessbackup.domain.loader.SearchHistoryContentLoader;
import com.morgan.design.seamlessbackup.domain.loader.WordDictionaryContentLoader;

public enum BackupType {

	DICTIONARY("/Dictionary/", "dictionary.json", "Dictionary", WordDictionaryContentLoader.class, R.string.description_dictionary),

	BROWSER_BOOKMARKS("/Browser/Bookmarks/", "bookmarks.json", "Browser Bookmarks", BookmarkContentLoader.class, R.string.description_browser_bookrmarks),

	BROWSER_SEARCH_HISTORY("/Browser/SearchHistory/", "searchhistory.json", "Browser Search History", SearchHistoryContentLoader.class, R.string.description_browser_search_history);

	private final String dir;
	private final String fileName;
	private final String prettyName;
	private final Class<? extends ContentLoader<?>> contentLoader;
	private final int descriptionId;

	private BackupType(String dir, String fileName, String prettyName, Class<? extends ContentLoader<?>> contentLoader, int descriptionId) {
		this.dir = dir;
		this.fileName = fileName;
		this.prettyName = prettyName;
		this.contentLoader = contentLoader;
		this.descriptionId = descriptionId;
	}

	public Class<? extends ContentLoader<?>> getContentLoader() {
		return this.contentLoader;
	}

	public String dir() {
		return this.dir;
	}

	public String fileName() {
		return this.fileName;
	}

	public int getResourceDescriptionId() {
		return this.descriptionId;
	}

	public String prettyName() {
		return this.prettyName;
	}

}
