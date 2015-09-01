package com.example.checkable.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.checkable.view.CheckableView;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class FoodAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mFoods;

	public FoodAdapter(Context context, String[] foods) {
		this.mContext = context;
		this.mFoods = foods;
	}

	@Override
	public int getCount() {
		return mFoods.length;
	}

	@Override
	public String getItem(int position) {
		return mFoods[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckableView checkableView = null;
		if (convertView == null) {
			checkableView = new CheckableView(mContext);
			convertView = checkableView;
		}
		((CheckableView) convertView).setTitle(mFoods[position]);

		return convertView;
	}
}
