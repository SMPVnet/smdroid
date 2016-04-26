package org.day.smartfolders;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

public class UrlListAdpater extends ArrayAdapter<UrlListItem> {

	private final Context mContext;
	private final int id;
	private List<UrlListItem> mItems;

	public UrlListAdpater(Context context, int textViewResourceId, List<UrlListItem> values) {
		super(context, textViewResourceId, values);
		
		mContext = context;
		id       = textViewResourceId;
		mItems   = values;
	}

	@Override
	public int getCount() {
		if(mItems == null)
			return 0;		
		else
			return mItems.size();
	}
	
	@Override
	public View getView(int position, View v, ViewGroup parent) {

		if(mItems == null) return null;
		
		if (position >= mItems.size())
			return null;

		View mView = v;
		if (mView == null) {
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = vi.inflate(id, null);
		}

		UrlListItem item = mItems.get(position);		
		if (item != null) {
			TextView text = (TextView) mView.findViewById(R.id.name);
           	text.setText(item.name);
           	text.setGravity(Gravity.LEFT);
		}
		return mView;
	}
}
