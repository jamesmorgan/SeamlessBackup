package com.morgan.design.seamlessbackup.domain;

import static com.morgan.design.seamlessbackup.domain.Constants.LOG_OUT;
import static com.morgan.design.seamlessbackup.domain.Constants.RETRY;

public enum DropboxError {

	CANCELLED("User Cancelled", false, false),

	NOT_MODIFIED("File Modfied By Alternative Party", false, false),

	UNAUTHORIZED("Unauthorized User Access", false, LOG_OUT),

	FORBIDDEN("Not Allow to Access Backup File", false, false),

	NOT_FOUND("Backup file not found", false, false),

	NOT_ACCEPTABLE("Too many entries to return", false, false),

	UNSUPPORTED_MEDIA("Invalid Media Type, can't be thumbnailed", false, false),

	INSUFFICIENT_STORAGE("User Over Quota", false, false),

	IO_EXCEPTION("Network error", RETRY, false),

	FILE_TO_BIG("This file is too big to upload", RETRY, false),

	PARSE_EXCEPTION("Dropbox error", RETRY, false),

	NOT_AUTHORISED("The AuthSession wasn't properly authenticated or user unlinked", false, LOG_OUT),

	UNABLE_TO_CREATE_LOCAL_FILE("Couldn't create a local file to store the document", false, false),

	UNKNOWN_ERROR("Unknown error", RETRY, false);

	private String error;
	private boolean shouldRetry;
	private boolean shouldLogout;

	private DropboxError(String error, boolean shouldRetry, boolean shouldLogout) {
		this.error = error;
		this.shouldRetry = shouldRetry;
		this.shouldLogout = shouldLogout;
	}

	public String getError() {
		return this.error;
	}

	public boolean shouldLogout() {
		return this.shouldLogout;
	}

	public boolean shouldRetry() {
		return this.shouldRetry;
	}

}
