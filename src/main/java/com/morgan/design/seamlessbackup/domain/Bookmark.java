package com.morgan.design.seamlessbackup.domain;

import com.google.common.base.Objects;

public class Bookmark {

	private int id;
	private String bookmark; // Flag indicating that an item is a bookmark.
	private String created; // The date the item created, in milliseconds since the epoch.
	private String date; // The date the item was last visited, in milliseconds since the epoch.
	private String fav_icon; // The favicon of the bookmark.
	private String title; // The user visible title of the bookmark or history item.
	private String url; // The URL of the bookmark or history item.
	private String visits; // The number of time the item has been visited.

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookmark() {
		return this.bookmark;
	}

	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFavIcon() {
		return this.fav_icon;
	}

	public void setFavIcon(String fav_icon) {
		this.fav_icon = fav_icon;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVisits() {
		return this.visits;
	}

	public void setVisits(String visits) {
		this.visits = visits;
	}

	@Override
	public String toString() {
		return Objects
				.toStringHelper(this).add("id", id).add("bookmark", bookmark).add("created", created).add("date", date).add("fav_icon", fav_icon)
				.add("title", title).add("url", url).add("visits", visits).toString();
	}
}
