package com.morgan.design.seamlessbackup;

import android.app.Application;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.morgan.design.seamlessbackup.util.DropboxConfig;

public class SeamlessBackupApplication extends Application {

	private DropboxAPI<AndroidAuthSession> mApi;

	@Override
	public void onCreate() {
		super.onCreate();

		AndroidAuthSession session = DropboxConfig.buildSession(this);
		mApi = new DropboxAPI<AndroidAuthSession>(session);
	}

	public DropboxAPI<AndroidAuthSession> getDropboxApi() {
		return this.mApi;
	}

}
