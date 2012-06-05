package com.morgan.design.seamlessbackup.domain;

public enum BackupType {

	DICTIONARY("/Dictionary/", "dictionary.json"),

	BROWSER_BOOKMARKS("/Browser/Bookmarks/", "bookmarks.json"),

	BROWSER_SEARCH_HISTORY("/Browser/SearchHistory/", "searchhistory.json");

	private final String dir;
	private final String fileName;

	private BackupType(String dir, String fileName) {
		this.dir = dir;
		this.fileName = fileName;
	}

	public String dir() {
		return this.dir;
	}

	public String fileName() {
		return this.fileName;
	}

}
