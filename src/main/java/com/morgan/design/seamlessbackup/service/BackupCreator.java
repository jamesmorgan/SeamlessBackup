package com.morgan.design.seamlessbackup.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morgan.design.seamlessbackup.domain.BackupType;

public class BackupCreator {

	private static Logger log = LoggerFactory.getLogger(BackupCreator.class);

	private static final String SEAMLESS_BACKUP_DIR = "/SeamlessBackup/";

	public File createFile(List<?> loadContent, BackupType type) {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(loadContent);

			File file = makeFile(type);
			FileOutputStream fOut = new FileOutputStream(file);

			/*
			 * We have to use the openFileOutput()-method the ActivityContext provides, to protect your file from others
			 * and This is done for security-reasons. We chose MODE_WORLD_READABLE, because we have nothing to hide in
			 * our file
			 */
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// Write the string to the file
			osw.write(json);
			/*
			 * ensure that everything is really written out and close
			 */
			osw.flush();
			osw.close();

			return file;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File makeFile(BackupType type) {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + SEAMLESS_BACKUP_DIR);
		if (!dir.exists()) {
			log.info("Making DIR as not proesent");
			dir.mkdirs();
		}

		// ##### Write a file to the disk #####
		File file = new File(dir, type.fileName());
		return file;
	}

}
