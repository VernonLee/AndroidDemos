package com.example.checkable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @since 2014-12-29
 * @author huailiang
 */
public class MainActivity extends Activity implements OnItemClickListener {
	private ListView mContainerList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContainerList = (ListView) findViewById(R.id.main_list);
		mContainerList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Class<?> cls = null;
		switch (position) {
		default:
		case 0: // GridView 单选
			cls = SCGridActivity.class;
			break;
		case 1: // GridView 多选
			cls = MCGridActivity.class;
			break;
		case 2: // ListView 单选
			cls = SCListActivity.class;
			break;
		case 3: // ListView 多选
			cls = MCListActivity.class;
			break;
		}

		if (cls != null) {
			startActivity(new Intent(MainActivity.this, cls));
		}
	}
}
