package org.day.smartfolders.util;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Config {

	private static String uniqueID = null;

	private static final String PREF_FILE      = "smartfolders.pref";
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
	private static final String PREF_URL_LINKS = "URL_LINKS";

	public synchronized static String getUUID(Context context) {
	    if (uniqueID == null) {
	        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
	        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
	        if (uniqueID == null) {
	            uniqueID = UUID.randomUUID().toString().toUpperCase();
	            
	            // save
	            Editor editor = sharedPrefs.edit();
	            editor.putString(PREF_UNIQUE_ID, uniqueID);
	            editor.commit();
	        }
	    }
	    if(uniqueID == null) uniqueID = "1234567890";
	    return uniqueID;
	}
	
	public synchronized static String getLinks(Context context) {
		 SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
	     return sharedPrefs.getString(PREF_URL_LINKS, "");
	}
	
	public synchronized static void saveLinks(Context context, String str) {
		SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
	    String links = sharedPrefs.getString(PREF_URL_LINKS, "");
	    
        if( !links.equals(str)) {
	        // save
	        Editor editor = sharedPrefs.edit();
            editor.putString(PREF_URL_LINKS, str);
            editor.commit();
        }       
	}
}
