package com.example.checkable;

import android.os.Bundle;
import android.widget.ListView;

import com.example.checkable.adapter.FoodAdapter;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class BaseListActivity extends BaseActivity {
	public ListView mFoodList;
	private FoodAdapter mFoodAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mFoodAdapter = new FoodAdapter(this, mFoodsBank);
		mFoodList = (ListView) findViewById(R.id.list_foods);
		mFoodList.setAdapter(mFoodAdapter);
	}
}
