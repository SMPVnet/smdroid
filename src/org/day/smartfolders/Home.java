package org.day.smartfolders;

import org.day.smartfolders.util.Config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Home extends Activity {

	//private ApplicationInfo appInfo;
	private UrlListAdpater mAdapter;
	private ListView mView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);

		mView = (ListView) findViewById(R.id.listView);
	
		mView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				try {
					UrlListItem item = (UrlListItem) mView.getItemAtPosition(position);
		
					if(item != null && item.url.startsWith("http")) {
		
						String url = item.url;
						int n = url.indexOf("/f=");
						if(n > 0) {
							url = url.substring(0, n+1);
						}
						Intent intent = new Intent(Home.this, WebActivity.class);
						intent.setData(Uri.parse(url));
						startActivity(intent);
					}
				}
				catch (Exception ex){
					System.err.println(ex.getMessage());
				}
			}
		});
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	
		initList();
		
		mAdapter = new UrlListAdpater(this, R.layout.url_list, UrlListItem.getList());
		
		mView.setAdapter(mAdapter);
		
		registerForContextMenu(mView);
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	if (v.getId() == R.id.listView) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;    	    
    	    UrlListItem item = (UrlListItem) mView.getItemAtPosition(info.position);
    	    
    	    menu.setHeaderTitle(item.name);
    		menu.add(Menu.NONE, 0, 0, "Delete");
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem mi) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)mi.getMenuInfo();
	    
	    UrlListItem item = (UrlListItem) mView.getItemAtPosition(info.position);
	    
	    int menuItemIndex = mi.getItemId();
	    if(menuItemIndex == 0) {  // Delete item
	    	//Toast.makeText(this, "Delete " + item.name, Toast.LENGTH_SHORT).show();
	    	deleteItem(info.position, item.name);
	    }
    	return true;
    }
    
    public void deleteItem(final int position, String name) {
    	final Context ctx = this;
    	
    	new AlertDialog.Builder(ctx)
        .setMessage("Do you want to delete " + name + "?")
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		if(UrlListItem.deleteItem(position) == true) {
     	    		UrlListItem.saveList(ctx);
     	    		mAdapter.notifyDataSetChanged();
     	    	}
            }
        })
        .setNegativeButton("No", null)
        .show();
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
    }
   
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
  
		int id = item.getItemId();
  
		if (id == R.id.qrscan) {
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	   
	void initList() {
		UrlListItem.clearItems();
	
		String s = Config.getLinks(this);
		if(s.length() > 0) {
			String[] arr = s.split("\n");
			for(int i=0;i<arr.length; i++) {
				addUrl(arr[i]);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		 if (resultCode == RESULT_OK && requestCode == 1) {
			 if (data.hasExtra("scanURL")) {
				 String url = data.getExtras().getString("scanURL");
				 //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
				 
				 if(addUrl(url.trim()) == true) {
					 UrlListItem.saveList(this);
					 mAdapter.notifyDataSetChanged();
				 }
		     }
		 }
	}
	
	// url = http[s]://www.swday.org/mbhavferi/login.htm?f=<folder>&p=<pocket>&id=<uuid>
	private boolean addUrl(String url) {
		try {
			if(url.startsWith("http")) {
				if(url.indexOf("login.htm?") > 0) {
					int n = url.indexOf('?');
					String s = url.substring(n+1); // 
					String[] arr = s.split("&");
					
					String folder = "";					
					for(int i=0; i<arr.length; i++) {
						if(arr[i].startsWith("f=")) {
							folder = arr[i].substring(2).trim().toUpperCase();
							break;
						}
					}
					if(folder.length() > 0) {
						return addFolder(folder, url);
					}
				} else {
					// http://www.swday.org/mbhavferi/f=mBhavferi
					int n = url.lastIndexOf('/');
					if(n > 0) {
						String name = url.substring(n+1); //
						if(name.startsWith("f=")) {
							name = name.substring(2).trim();
							if(name.length() > 0) {
								return addFolder(name, url);
							}
						}
					}
				}
			}
		} catch(Exception ex) {
			
		}
		return false;
	}
	
	private boolean addFolder(String name, String url) {
		return UrlListItem.addItem(name, url);
	}
	
	/*
	private boolean isDebug() {
		if(appInfo == null) {
			PackageManager pacMan = getPackageManager();
			String pacName = getPackageName();
			appInfo = null;
			try {
			    appInfo = pacMan.getApplicationInfo(pacName, 0);
			} catch (NameNotFoundException e) {
			    Toast.makeText(this, "Could not find package " + pacName, Toast.LENGTH_SHORT).show();
			    e.printStackTrace();
			}
		}
		
		if(appInfo != null) {
		    if( (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0)
		    	return true;
		}
		return false;
	}
	*/
}
