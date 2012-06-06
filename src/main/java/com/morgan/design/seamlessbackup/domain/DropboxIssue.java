package com.morgan.design.seamlessbackup.domain;

import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxFileSizeException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxLocalStorageFullException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.google.common.base.Objects;

public class DropboxIssue {

	private DropboxError error;
	private String dropboxError;
	private String exception;

	public String getDropboxError() {
		return this.dropboxError;
	}

	public DropboxError getError() {
		return this.error;
	}

	public String getException() {
		return this.exception;
	}

	public static DropboxIssue fromError(DropboxError dropboxError) {
		DropboxIssue issue = new DropboxIssue();
		issue.error = dropboxError;
		return issue;
	}

	public static DropboxIssue fromException(DropboxException e) {
		DropboxIssue issue = new DropboxIssue();

		if (e instanceof DropboxUnlinkedException) {
			// The AuthSession wasn't properly authenticated or user unlinked.
			issue.error = DropboxError.NOT_AUTHORISED;
		}
		else if (e instanceof DropboxPartialFileException) {
			// We cancelled the operation
			issue.error = DropboxError.CANCELLED;
		}
		else if (e instanceof DropboxIOException) {
			// Happens all the time, probably want to retry automatically.
			issue.error = DropboxError.IO_EXCEPTION;
		}
		else if (e instanceof DropboxParseException) {
			// Probably due to Dropbox server restarting, should retry
			issue.error = DropboxError.PARSE_EXCEPTION;
		}
		else if (e instanceof DropboxFileSizeException) {
			// File size too big to upload via the API
			issue.error = DropboxError.FILE_TO_BIG;
		}
		else if (e instanceof DropboxLocalStorageFullException) {
			issue.error = DropboxError.INSUFFICIENT_STORAGE;
		}
		else {
			// Something else
			issue.error = DropboxError.UNKNOWN_ERROR;
		}

		// log exception
		issue.exception = null != e.getMessage() ? e.getMessage() : "";

		return issue;
	}

	public static DropboxIssue fromServerException(DropboxServerException e) {
		// Server-side exception translation.

		DropboxIssue issue = new DropboxIssue();

		if (e.error == DropboxServerException._304_NOT_MODIFIED) {
			// won't happen since we don't pass in revision with metadata
			issue.error = DropboxError.NOT_MODIFIED;
		}
		else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
			// Unauthorised, so we should unlink them. You may want to automatically log the user out in this case.
			issue.error = DropboxError.UNAUTHORIZED;
		}
		else if (e.error == DropboxServerException._403_FORBIDDEN) {
			// Not allowed to access this
			issue.error = DropboxError.FORBIDDEN;
		}
		else if (e.error == DropboxServerException._404_NOT_FOUND) {
			// path not found (or if it was the thumbnail, can't be thumbnailed)
			issue.error = DropboxError.NOT_FOUND;
		}
		else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
			// too many entries to return
			issue.error = DropboxError.NOT_ACCEPTABLE;
		}
		else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
			// can't be thumbnailed
			issue.error = DropboxError.UNSUPPORTED_MEDIA;
		}
		else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
			// user is over quota
			issue.error = DropboxError.INSUFFICIENT_STORAGE;
		}
		else {
			// Something else
			issue.error = DropboxError.UNKNOWN_ERROR;
		}

		// This gets the Dropbox error, translated into the user's language
		issue.dropboxError = null != e.body.userError ? e.body.userError : e.body.error;

		return issue;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("error", error).add("dropboxError", dropboxError).toString();
	}
}
