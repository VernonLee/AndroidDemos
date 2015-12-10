package com.nodlee.click;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener {
	private static final String TAG = "MainActivity";
	private static final boolean DEBUG = true;

	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageView = (ImageView) findViewById(R.id.img_android);
		mImageView.setOnTouchListener(this);
		mImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
		case R.id.img_android:
			Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 图片的半径
		int height = v.getMeasuredHeight();
		long radius = Math.round(height >> 1);
		// 触摸的位置
		float touchX = event.getX();
		float touchY = event.getY();
		// 触摸的位置到图片中心的距离
		float dx = touchX - radius;
		float dy = touchY - radius;
		double distance = Math.sqrt(dx * dx + dy * dy);

		if (DEBUG)
			Log.i(TAG, "touchX=" + touchX + "touchY=" + touchY 
						+ "radius=" + radius + "distance=" + distance);
		
		// 触摸点在圆内触发点击事件
		if (distance <= radius) {
			return performClick(v, event);
		}
		return true;
	}

	private boolean performClick(View target, MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			//	target.setPressed(true);
		} else if (action == MotionEvent.ACTION_UP) {
			//	target.setPressed(false);
			target.performClick();
			return true;
		}
		return false;
	}
}
