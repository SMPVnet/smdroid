package org.day.smartfolders;

import java.io.File;

import org.day.smartfolders.util.SystemUiHider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.widget.Toast;
import android.net.http.SslError;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class WebActivity extends Activity {
	
	private String mUrl;
	
	private WebView webView;
	
	private Toast  mToast;
	
	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		webView = (WebView) findViewById(R.id.webView1);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, webView, HIDER_FLAGS);
		mSystemUiHider.setup();
	}

	
	@Override
	public void onBackPressed() {

		// if browser can go back, do that
		if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
		confirmExit();
	}

	private void confirmExit() {
    	final Context ctx = this;
    	
    	new AlertDialog.Builder(ctx)
        .setMessage("Do you want to exit?")
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		finish();
            }
        })
        .setNegativeButton("No", null)
        .show();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		mUrl = getIntent().getDataString();
		
		delayedHide(100);
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				mSystemUiHider.hide();
			}
			catch (Exception ex) {
			}
			
			try {
				loadWebPage(mUrl);
			}
			catch (Exception ex) {
			}
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	public void loadWebPage(String url) {
		
		try {
			//String uid = "A" + Config.getUUID(this);
			
			//private Map <String, String> extraHeaders = new HashMap<String, String>();
			//extraHeaders.put("Smpvnet-Portal-Agent", "GitaMrutam/1.0");
			//extraHeaders.put("Smpvnet-Android-Uid", uid);
			//url += "?random=" + RandomUtils.getRandom(Integer.MAX_VALUE);
	
			webSetup();
			
			webView.setWebChromeClient(new MyWebChromeClient(this));
			
			webView.setWebViewClient(new WebViewClient() {
			    @Override
			    public boolean shouldOverrideUrlLoading(WebView view, String url) {
			         return false;
			    }
			      
			    @Override
			    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
			    	int errid = error.getPrimaryError();
			    	showToast("SSL Error: " + errid);
			        handler.proceed();
			    }
			    
			    @Override
			    public void onPageFinished (WebView view, String url) {
			    	if(url.contains("main.htm")) {
			    		view.clearHistory();
			    	}
			    	super.onPageFinished(view,  url);
			    }
			});
			
			JavaScriptInterface jsInterface = new JavaScriptInterface(this);
			webView.addJavascriptInterface(jsInterface, "JSInterface");
		
			// now load Webpage
			webView.loadUrl(url);
		}
		catch (Exception ex) {
		}
	}
	
	private void webSetup() {
		WebSettings ws = webView.getSettings();

		ws.setJavaScriptEnabled(true);     // enable javascript
		
		ws.setBuiltInZoomControls(true);   // enable pinch
		ws.setDisplayZoomControls(false);  // hide zoom controls
		ws.setDomStorageEnabled(true);     // enable https
		
		File dir = getCacheDir();
		if (!dir.exists()) {
		  dir.mkdirs();
		}
		ws.setAppCachePath(dir.getAbsolutePath());
	    ws.setAllowFileAccess(true);
	    ws.setAppCacheEnabled(true);
	    ws.setCacheMode(WebSettings.LOAD_DEFAULT);
	}
	
	protected void showToast(String str, int gravity, int x, int y) {
	    	if(mToast != null) mToast.cancel();  // cancel the last one, to avoid multiple times - Umeshbhai found this bug.

			mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
			mToast.setGravity(gravity, x, y);
			//LinearLayout toastLayout = (LinearLayout) mToast.getView();
			//TextView toastTV = (TextView) toastLayout.getChildAt(0);
			//toastTV.setTextSize(24);
			
			//toastLayout.setBackgroundResource(R.drawable.toast);
			mToast.show();	
	}
	
	protected void showToast(String str) {
		showToast(str, Gravity.BOTTOM|Gravity.CENTER, 0, 30);
	}
	
	public void doStartActivity(Intent intent) {
		startActivity(intent);
	}
}
