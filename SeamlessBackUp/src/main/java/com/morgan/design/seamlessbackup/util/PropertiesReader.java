package com.morgan.design.seamlessbackup.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.res.AssetManager;

public class PropertiesReader {

	private static Logger log = LoggerFactory.getLogger(PropertiesReader.class);

	public static final String DEFAULT_PROPS = "seamless_backup_default.properties";
	public static final String LIVE_PROPS = "seamless_backup.properties";

	private final Context context;

	public PropertiesReader(Context context) {
		this.context = context;
	}

	/**
	 * Returns the given {@link Properties} for the project, looking for live properties then defaults.
	 * 
	 * @return {@link Properties} the properties to use
	 * @throws IllegalArgumentException
	 *             is no properties are found
	 */
	public Properties getProperties() {
		AssetManager assetManager = context.getAssets();
		if (livePropertiesFound(assetManager)) {
			return getProperties(assetManager, LIVE_PROPS);
		}
		else if (defaultPropertiesFound(assetManager)) {
			return getProperties(assetManager, DEFAULT_PROPS);
		}
		throw new IllegalArgumentException("Project Not Setup Correct, unable to find default or productiuon properties");
	}

	private Properties getProperties(AssetManager assetManager, String propertiesFile) {
		Properties properties = new Properties();
		try {
			properties.load(assetManager.open(propertiesFile));
		}
		catch (IOException e) {
			log.error(String.format("Unable to load properites file %s", propertiesFile), e);
		}
		return properties;
	}

	private boolean defaultPropertiesFound(AssetManager assetManager) {
		try {
			assetManager.open(DEFAULT_PROPS);
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	private boolean livePropertiesFound(AssetManager assetManager) {
		try {
			assetManager.open(LIVE_PROPS);
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

}
