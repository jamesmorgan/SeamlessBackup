package com.morgan.design.seamlessbackup;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

@ContentView(R.layout.quick_restore)
public class QuickRestoreActivity extends AbstractAuthenticatedListActivity {

	@InjectView(R.id.list_available_backups)
	private Button listBackUps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO list all backup files for each type

	}

	public void onListBackUps(View view) {

	}
}
