package com.morgan.design.seamlessbackup.domain;

import com.google.common.base.Objects;

public class SearchHistory {

	private int id;
	private String date;// The date the search was performed, in milliseconds since the epoch.
	private String search;// The user entered search term.

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSearch() {
		return this.search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("date", date).add("search", search).toString();
	}

}
