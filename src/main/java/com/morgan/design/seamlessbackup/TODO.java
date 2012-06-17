package com.morgan.design.seamlessbackup;

public class TODO {
	private TODO() {
	}

	//@formatter:off
	
	// TODO -> Parse SMS Content
	// TODO -> Define how to store Content
	// TODO -> Upload to Dropbox
	// TODO -> Download from Dropbox
	// TODO -> Parse download content and source to Phone
	
	// Existing backup tool: https://github.com/jberkel/sms-backup-plus
	// SMS Columns: 
	// - http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.7_r1/android/provider/Telephony.java#Telephony.TextBasedSmsColumns
	// - http://androidforums.com/application-development/158592-querying-sms-content-provider.html
	
	// Accessing objects via IDs: Uri singleUri = ContentUri.withAppendedId(UserDictionary.Words.CONTENT_URI,4);
	// Batch actions: http://developer.android.com/guide/topics/providers/content-provider-basics.html#Batch
	
	// ** Content Types: http://developer.android.com/reference/android/provider/package-summary.html
	// 1) SMS
	// 2) Contacts
	// 3) Email
	// 4) Calendar - http://developer.android.com/guide/topics/providers/calendar-provider.html
	// 5) Call Logs
	// 6) Wall papers?
	// 7) Phone Configuration?
	// 8) DONE - Word Dictionary - UserDictionary.Words.CONTENT_URI
	// 9) Browser Bookmarks
	// 10) Browser Search History
	// 11) Media
	// 12) System Preferences
	// 13) Voicemail
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	// Release V0.1-BETA
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	
	// TODO -> Google Drive Integration?
	
	// TODO -> Set up ARCA logging
	// TODO -> Set up Google Analytics
	// TODO -> Set up ORMLite DB (4.41), evaluate other options 

	// TODO -> Investigate Pro-guard
	// TODO -> Obfuscation API keys including manifest
	
	// TODO -> How to run normal JUnit tests in android project?
	
	// TODO -> Compress files, with user option, investigate best fit /algorithm
	// TODO -> Use phone rating to determine if can handle file compression easily, default on/off depending on phone
	
	// TODO -> Ability to schedule weekly backups
	// TODO -> Include DB schedule backup configuration
	// TODO -> Record backup history in DB, names, dates, applied, MD5 validation?
	// TODO -> Ability to view JSON on the phone, syntax high lighting
	
	// TODO -> Settings?
	// TODO -> Fix all Lint warnings/errors
	// TODO -> Home Screen Dashboard -> http://www.javacodegeeks.com/2012/06/android-dashboard-design-pattern.html
	
	// TODO -> Download SMS's
	// TODO -> Apply SMS's
	// TODO -> Backup SMS's

	// TODO -> Dropbox API: Deal with file not found in backup directory
	// TODO -> Dropbox API: Deal with incorrect file type/name in backup directory
	// TODO -> Dropbox API: Retry on certain errors
	
	// TODO -> Allow configurable options number of backup files to keep, default 5
	// TODO -> Handle multiple matches when applying download data
	
	// TODO -> DONE (17/06/2012) - Devise backup file chooser/parser type e.g. get latest by date
	// TODO -> DONE (17/06/2012) - Move all onClick handlers to XMl with onClick specified, no more addListener calls in code
	
	// TODO -> DONE (16/06/2012) - Added SelectableBackupActivity allowing for checkbox quick backup options
	
	// TODO -> DONE (06/06/2012) - Dropbox API (Upload) : Deal with various dropbox exceptions and return types

	// TODO -> DONE (05/06/2012) - Dropbox API (Download) : Deal with various dropbox exceptions and return types
	// TODO -> DONE (05/06/2012) - Dropbox API: Create re-usable Dropbox download'er component
	
	// TODO -> DONE (05/06/2012) - Added ability to run JUnit tests in maven on command line
	// TODO -> DONE (05/06/2012) - When uploading Datetime stamp each backup file ddMMYYYHHmmss
	
	// TODO -> DONE (05/06/2012) - Backup User Dictionary (Simple)
	// TODO -> DONE (05/06/2012) - Download User Dictionary
	// TODO -> DONE (05/06/2012) - Apply User Dictionary
	
	// TODO -> DONE (05/06/2012) - Backup Browser Search History (Simple)
	// TODO -> DONE (05/06/2012) - Download Browser Search History
	// TODO -> DONE (05/06/2012) - Apply Browser Search History
	
	// TODO -> DONE (05/06/2012) - Backup Browser Bookmarks (Simple)
	// TODO -> DONE (05/06/2012) - Download Browser Bookmarks
	// TODO -> DONE (05/06/2012) - Apply Browser Bookmarks

	// TODO -> DONE (03/06/2012) - Set up maven integration
	// TODO -> DONE (03/06/2012) - Set up Logback & slf4j for Android
	// TODO -> DONE (03/06/2012) - Set up RoboGuice
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	
	// Proof Of Concept - DONE - (19/05/2012)
	// TODO -> DONE - (19/05/2012) - Upload Dictionary to Dropbox
	// TODO -> DONE - (19/05/2012) - Download Dictionary from Dropbox
	// TODO -> DONE - (19/05/2012) - Apply Dictionary to phone
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	
	//@formatter:on
}
