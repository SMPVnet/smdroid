package org.day.smartfolders;

//import org.day.smartfolders.util.Config;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface {
	private Activity activity;

	    public JavaScriptInterface(Activity activiy) {
	        this.activity = activiy;
	    }
	    
	    @JavascriptInterface
		public void startNavigation(String url)
		{
	    	try {
				//Uri gmmIntentUri = Uri.parse("google.navigation:q=31 longfield dr,hillsborough,nj+08844");
	    		Uri gmmIntentUri = Uri.parse(url);
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				((WebActivity) activity).doStartActivity(mapIntent);
	    	}
	    	catch (Exception ex) {
	    	}
	    }
		
}
