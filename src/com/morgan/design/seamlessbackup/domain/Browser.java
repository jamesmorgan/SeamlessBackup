package com.morgan.design.seamlessbackup.domain;

import java.util.List;

import com.google.common.base.Objects;

public class Browser {

	private List<Bookmark> bookmarks;
	private List<SearchHistory> searchHistories;

	public List<Bookmark> getBookmarks() {
		return this.bookmarks;
	}

	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public List<SearchHistory> getSearchHistories() {
		return this.searchHistories;
	}

	public void setSearchHistories(List<SearchHistory> searchHistories) {
		this.searchHistories = searchHistories;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("bookmarks", bookmarks).add("searchHistories", searchHistories).toString();
	}

}
