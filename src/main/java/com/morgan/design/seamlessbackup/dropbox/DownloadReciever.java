package com.morgan.design.seamlessbackup.dropbox;

import java.util.List;

import com.morgan.design.seamlessbackup.domain.DropboxIssue;

public interface DownloadReciever<T> {

	void failed(DropboxIssue dropboxIssue);

	void recieved(List<T> results);
}
