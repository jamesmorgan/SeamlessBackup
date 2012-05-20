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
	
	// TODO -> Obfuscation API keys including mainifest
	
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
	
	// Proof Of Concept - DONE - (19/05/2012)
	// TODO -> DONE - (19/05/2012) - Upload Dictionary to Dropbox
	// TODO -> DONE - (19/05/2012) - Download Dictionary from Dropbox
	// TODO -> DONE - (19/05/2012) - Apply Dictionary to phone
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	
	//@formatter:on
}
