package com.example.checkable;

import com.example.checkable.view.CheckableView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class SCGridActivity extends BaseGridActivity implements OnItemClickListener {
	private static final String KEY_SELECTED_FOOD = "selectedFood";
	private String mSelectedFood;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null) {
			mSelectedFood = savedInstanceState.getString(KEY_SELECTED_FOOD);
		}
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			mFoods.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		}
		mFoods.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(((CheckableView)view).isChecked()) {
			mSelectedFood = mFoodsBank[position];
		}
		Toast.makeText(SCGridActivity.this, mSelectedFood, Toast.LENGTH_SHORT).show();
	} 
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_SELECTED_FOOD, mSelectedFood);
	}
}
