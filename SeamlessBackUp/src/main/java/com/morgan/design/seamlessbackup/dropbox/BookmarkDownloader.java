package com.morgan.design.seamlessbackup.dropbox;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.dropbox.client2.DropboxAPI;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.Bookmark;
import com.morgan.design.seamlessbackup.domain.DropboxIssue;

public class BookmarkDownloader extends AbstractDownloader {

	public BookmarkDownloader(Context context, DropboxAPI<?> mApi, final DownloadReciever<Bookmark> reciever) {
		super(context, mApi, BackupType.BROWSER_BOOKMARKS, new DropdoxBackupReciever() {

			@Override
			public void recieved(String jsonResult) {
				@SuppressWarnings("serial")
				Type typeOfT = new TypeToken<List<Bookmark>>() {
				}.getType();

				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				List<Bookmark> words = gson.fromJson(jsonResult, typeOfT);

				reciever.recieved(words);
			}

			@Override
			public void failed(DropboxIssue dropboxIssue) {
				reciever.failed(dropboxIssue);
			}
		});
	}
}
