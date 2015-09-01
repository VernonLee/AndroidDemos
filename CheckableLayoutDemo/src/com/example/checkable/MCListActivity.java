package com.example.checkable;

import java.util.Vector;

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
public class MCListActivity extends BaseListActivity implements OnItemClickListener {
	private Vector<String> mSelectedFoods;
	private static final String KEY_SELECTED_FOODS = "selectedFoods";
	
	@SuppressWarnings("unchecked")
	@Override
	@TargetApi(11)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSelectedFoods = new Vector<String>();
		if(savedInstanceState != null) {
			mSelectedFoods = (Vector<String>) savedInstanceState
					.getSerializable(KEY_SELECTED_FOODS);
		}
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			mFoodList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
		mFoodList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String selectedFood = mFoodsBank[position];
		if(((CheckableView)view).isChecked()) {
			mSelectedFoods.add(selectedFood);
		} else {
			mSelectedFoods.remove(selectedFood);
		}
		Toast.makeText(MCListActivity.this, mSelectedFoods.toString(),
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(KEY_SELECTED_FOODS, mSelectedFoods);
	}
}
