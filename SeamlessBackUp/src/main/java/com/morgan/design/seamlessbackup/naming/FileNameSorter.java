package com.morgan.design.seamlessbackup.naming;

import java.util.List;

import com.dropbox.client2.DropboxAPI.Entry;

public interface FileNameSorter {

	List<Entry> sortByDate(List<Entry> contents);

}
