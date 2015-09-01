package com.example.checkable;

import java.util.Vector;

import com.example.checkable.view.CheckableView;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class MCGridActivity extends BaseGridActivity implements OnItemClickListener {
	private Vector<String> mSelectedFoods;
	
	private static final String KEY_SELECTED_FOODS = "selectedFoods";

	@SuppressWarnings("unchecked")
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSelectedFoods = new Vector<String>();
		
		if(savedInstanceState != null) {
			mSelectedFoods = (Vector<String>) savedInstanceState
					.getSerializable(KEY_SELECTED_FOODS);
		}
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			mFoods.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		}
		mFoods.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String selectedFood = mFoodsBank[position];
		if (((CheckableView) view).isChecked()) {
			mSelectedFoods.add(selectedFood);
		} else {
			mSelectedFoods.remove(selectedFood);
		}

		// 另外一种方式，但是可靠性低
		// if (mSelectedFoods.contains(selectedFood)) {
		//     mSelectedFoods.remove(selectedFood);
		// } else {
		//    mSelectedFoods.add(selectedFood);
		// }
		Toast.makeText(MCGridActivity.this, mSelectedFoods.toString(),
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(KEY_SELECTED_FOODS, mSelectedFoods);
	}
}
