package com.example.checkable;

import com.example.checkable.view.CheckableView;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class SCListActivity extends BaseListActivity implements OnItemClickListener {
	private String mSelectedFood;
	private static final String KEY_SELECTED_FOOD = "selectedFood";
	
	@Override
	@TargetApi(11)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null) {
			mSelectedFood = savedInstanceState.getString(KEY_SELECTED_FOOD);
		}
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			mFoodList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
		mFoodList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(((CheckableView)view).isChecked()) {
			mSelectedFood = mFoodsBank[position];
		}
		Toast.makeText(SCListActivity.this, mSelectedFood, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_SELECTED_FOOD, mSelectedFood);
	}
}
