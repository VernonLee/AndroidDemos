package com.nodlee.shake;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

import com.nodlee.shake.ShakeListener.OnShakeListener;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final boolean DEBUG = true;
	
	private ShakeListener mShakeListener;
	private View mDecorView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDecorView = findViewById(android.R.id.content);
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShaked() {
				if (DEBUG) Log.i(TAG, "shaked");
				
				vibrate(500);
				vibrateWindow();
			}
		});
	}

	/**
	 * 手机震动
	 * @param milliseconds
	 */
	private void vibrate(long milliseconds) {
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	
	/**
	 * 窗口震动
	 */
	private void vibrateWindow() {
		if (mDecorView == null) return;
		
		TranslateAnimation translateAnimation = new TranslateAnimation(50, 150,200, 50);
		translateAnimation.setDuration(2000);
		Interpolator interpolator = new CycleInterpolator(5);
		translateAnimation.setInterpolator(interpolator);
		mDecorView.startAnimation(translateAnimation);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mShakeListener.stop();
	}
}
