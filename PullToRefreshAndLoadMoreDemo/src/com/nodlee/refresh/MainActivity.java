package com.nodlee.refresh;

import com.nodlee.refresh.view.PullToRefreshLayout;
import com.nodlee.refresh.view.PullToRefreshLayout.OnRefreshListener;
import com.nodlee.refresh.view.PullableListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity implements OnRefreshListener {
	private PullToRefreshLayout mRefreshLayout;
	private PullableListView mListView;
	private ArrayAdapter<String> mAdapter;
	
	private String[] animals = new String[]{
			"大象","老鼠","花猫","长颈鹿"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		mRefreshLayout.setOnRefreshListener(this);
		// 自动加载更多
		// mRefreshLayout.autoLoad();
		// 自动刷新
		// mRefreshLayout.autoRefresh();
		
		mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, animals);
		mListView = (PullableListView) findViewById(R.id.content_view);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		 mRefreshLayout.refreshFinish();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		 mRefreshLayout.loadmoreFinish();
	}
}
