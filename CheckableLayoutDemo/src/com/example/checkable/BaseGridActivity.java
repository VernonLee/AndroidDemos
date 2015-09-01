package com.example.checkable;

import android.os.Bundle;
import android.widget.GridView;

import com.example.checkable.adapter.FoodAdapter;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class BaseGridActivity extends BaseActivity {
	public GridView mFoods;
	public FoodAdapter mBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);

		mBaseAdapter = new FoodAdapter(this, mFoodsBank);
		mFoods = (GridView) findViewById(R.id.grid_foods);
		mFoods.setAdapter(mBaseAdapter);
	}
}
