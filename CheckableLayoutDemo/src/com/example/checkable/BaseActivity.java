package com.example.checkable;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class BaseActivity extends Activity {
	public String [] mFoodsBank = new String[] {
			"香蕉", "苹果", "鸭梨", "荔枝", "菠萝", "橘子",
			"橙子", "甘蔗", "火龙果", "西瓜", "蜜桃", "哈密瓜"
	};
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			ActionBar mActionBar = getActionBar();
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(false);
			mActionBar.show();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
