package org.day.smartfolders;

import java.util.ArrayList;
import java.util.List;

import org.day.smartfolders.util.Config;

import android.content.Context;

public class UrlListItem {
	public String name;
	public String url;
	
	public static List<UrlListItem> mList = new ArrayList<UrlListItem>(10);
	
	UrlListItem(String name, String url){
		this.name = name;
		this.url = url;
	}

	public static boolean addItem(String name, String url){
		UrlListItem item = new UrlListItem(name, url);		
		mList.add(item);
		return true;
	}
	public static boolean deleteItem(int index) {
		if(index < mList.size()) {
			mList.remove(index);
			return true;
		}
		return false;
	}
	
	public static void clearItems(){
		mList.clear();
	}
	
	public static List<UrlListItem> getList() {
		return mList;
	}
	
	public static void saveList(Context context) {
		String str = "";
		for(int i=0; i<mList.size(); i++) {
			UrlListItem item = mList.get(i);
			str += item.url + "\n";
		}
		Config.saveLinks(context, str);
	}
	
	public static UrlListItem contains(String name) {
		for(int i=0; i<mList.size(); i++) {
			UrlListItem item = mList.get(i);
			if(item.name.equals(name)) return item;
		}
		return null;
	}
}
