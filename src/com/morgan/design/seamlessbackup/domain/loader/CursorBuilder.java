package com.morgan.design.seamlessbackup.domain.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.common.base.Objects;

public class CursorBuilder {

	private static Logger log = LoggerFactory.getLogger(CursorBuilder.class);

	private final Context context;

	private Uri uri;
	private String[] projection;
	private String selection;
	private String[] selectionArgs;
	private String sortOrder;

	public CursorBuilder(Context context) {
		this.context = context;
	}

	public static CursorBuilder create(Context context) {
		return new CursorBuilder(context);
	}

	public CursorBuilder query(Uri uri) {
		this.uri = uri;
		return this;
	}

	public CursorBuilder withColumns(String[] projection) {
		this.projection = projection;
		return this;
	}

	public CursorBuilder sortBy(String sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	public CursorBuilder where(String selection) {
		this.selection = selection;
		return this;
	}

	public CursorBuilder whereArgs(String[] selectionArgs) {
		this.selectionArgs = selectionArgs;
		return this;
	}

	public Cursor createCursor() {
		Cursor mCursor = null;
		try {
			//@formatter:off
			mCursor = this.context.getContentResolver().query(
					this.uri, 
					this.projection, // The columns to return for each row
					this.selection, // Either null, or the word the user entered
					this.selectionArgs, // Either empty/null, or the string the user entered
					this.sortOrder); // Sort order
			//@formatter:on
		}
		catch (Exception e) {
			log.error(String.format("Exception thrown creating cusor content, Builder=[%s]", toString()), e);
		}
		return mCursor;
	}

	@Override
	public String toString() {
		return Objects
				.toStringHelper(this).add("context", context).add("uri", uri).add("projection", projection).add("selection", selection)
				.add("selectionArgs", selectionArgs).add("sortOrder", sortOrder).toString();
	}

}
