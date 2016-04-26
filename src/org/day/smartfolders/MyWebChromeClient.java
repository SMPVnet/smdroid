package org.day.smartfolders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

final class MyWebChromeClient extends WebChromeClient {

	Context myApp; 
    
	public MyWebChromeClient(Context context) {
		myApp = context;
	}
	
	@Override
	public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
	    callback.invoke(origin, true, false);
	}
	
	@Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(myApp)
        .setTitle("Smpvnet")
        .setMessage(message)
        .setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                result.confirm();
            }
        })
        .setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                result.cancel();
            }
        })
        .create()
        .show();

        return true;
    }
}
