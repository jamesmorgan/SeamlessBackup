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
	// Release V0.1
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	
	// TODO -> DONE (06/03/2012) - Set up maven integration
	// TODO -> DONE (06/03/2012) - Set up Logback & slf4j for Android
	// TODO -> Set up ARCA logging
	// TODO -> Set up Google Analytics
	// TODO -> Set up Android Annotations
	
	// TODO -> Investigate Pro-guard
	// TODO -> Obfuscation API keys including manifest
	// TODO -> How to run normal JUnit tests in android project
	
	// TODO -> Download User Dictionary
	// TODO -> Backup User Dictionary

	// TODO -> Download Browser Search History
	// TODO -> Backup Browser Search History
	
	// TODO -> Download Browser Bookmarks
	// TODO -> Backup Browser Bookmarks
	
	// TODO -> Download SMS's
	// TODO -> Backup SMS's
	
	// TODO -> Record backup history in DB
	// TODO -> Set up ORMLite DB
	
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
