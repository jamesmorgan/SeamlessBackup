package com.morgan.design.seamlessbackup;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.adaptor.types.SelectableBackupType;
import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.Constants;
import com.morgan.design.seamlessbackup.domain.loader.ContentLoader;
import com.morgan.design.seamlessbackup.dropbox.DropboxUploader;
import com.morgan.design.seamlessbackup.service.BackupCreator;

@ContentView(R.layout.selectable_backup)
public class SelectableBackupActivity extends AbstractAuthenticatedListActivity {

	private static Logger log = LoggerFactory.getLogger(SelectableBackupActivity.class);

	@InjectView(R.id.trigger_backup)
	private Button triggerBackup;

	private final List<BackupType> selectedTypes = Lists.newArrayList();
	private final boolean validSelection = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new SelectableBackUpTypesAdaptor(this, Constants.backupTypes));
		getListView().setItemsCanFocus(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		triggerBackup.setEnabled(validSelection);
	}

	public void onStartBackUp(View view) {
		log.debug(selectedTypes.toString());

		for (BackupType backupType : selectedTypes) {
			log.debug("Running backup for type {}", backupType);
			try {
				ContentLoader<?> newInstance = backupType.getContentLoader().newInstance();
				List<?> loadContent = newInstance.loadContent(this);
				File file = new BackupCreator().createFile(loadContent, backupType);
				new DropboxUploader(this, mApi, backupType, file).execute();
			}
			catch (InstantiationException e) {
				showToast("Unable to backup %s", backupType.prettyName());
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				showToast("Unable to backup %s", backupType.prettyName());
				e.printStackTrace();
			}
		}
	}

	private void onCheckboxClicked(final BackupType backupType, boolean isChecked) {
		if (isChecked) {
			selectedTypes.add(backupType);
		}
		else {
			selectedTypes.remove(backupType);
		}
		triggerBackup.setEnabled(!selectedTypes.isEmpty());
	}

	// ////////////////////
	// Selection Adaptor //
	// ////////////////////

	public class SelectableBackUpTypesAdaptor extends ArrayAdapter<SelectableBackupType> {
		private final Context context;
		private final List<SelectableBackupType> backTypes;

		public SelectableBackUpTypesAdaptor(Context context, List<SelectableBackupType> backupTypes) {
			super(context, R.layout.selectable_backup_type_adaptor_layout, backupTypes);
			this.context = context;
			this.backTypes = backupTypes;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;

			SelectableBackupType backType = this.backTypes.get(position);

			if (view == null) {
				final LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.selectable_backup_type_adaptor_layout, null);
				holder = new ViewHolder();
				holder.backupTypeCheckbox = (CheckBox) view.findViewById(R.id.backup_type_checkbox);
				holder.backupTypeDescription = (TextView) view.findViewById(R.id.backup_type_description);
				holder.selectableBackupType = backType;
			}
			else {
				holder = (ViewHolder) view.getTag();
			}

			if (null != backType) {
				holder.backupTypeCheckbox.setText(backType.getCheckboxName());
				holder.backupTypeCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						onCheckboxClicked(holder.selectableBackupType.getBackupType(), isChecked);
					}
				});
				holder.backupTypeDescription.setText(backType.getCheckboxDescription(context));
				holder.backupTypeDescription.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						holder.toggleChecked();
					}
				});
			}
			return view;
		}

		@Override
		public SelectableBackupType getItem(final int position) {
			return this.backTypes.get(position);
		}

		class ViewHolder {
			CheckBox backupTypeCheckbox;
			TextView backupTypeDescription;
			SelectableBackupType selectableBackupType;

			public void toggleChecked() {
				if (backupTypeCheckbox.isChecked()) {
					backupTypeCheckbox.setChecked(false);
				}
				else {
					backupTypeCheckbox.setChecked(true);
				}
			}
		}
	}
}
