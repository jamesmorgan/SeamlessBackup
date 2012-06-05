package com.morgan.design.seamlessbackup.domain;

import com.dropbox.client2.exception.DropboxServerException;
import com.google.common.base.Objects;

public class DropboxIssue {

	private DropboxError error;
	private String dropboxError;

	public String getDropboxError() {
		return this.dropboxError;
	}

	public DropboxError getError() {
		return this.error;
	}

	public static DropboxIssue fromError(DropboxError dropboxError) {
		DropboxIssue issue = new DropboxIssue();
		issue.error = dropboxError;
		return issue;
	}

	public static DropboxIssue fromError(DropboxServerException e) {
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
