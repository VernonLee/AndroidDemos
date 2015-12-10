package com.nodlee.shake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeListener implements SensorEventListener {
	/** 速度的阈值，当摇晃速度达到这值后产生作用 */
	private static final int SPEED_SHRESHOLD = 800;
	/** 两次检测的时间间隔 */
	private static final int UPTATE_INTERVAL_TIME = 70;

	private Context mCtx;
	private SensorManager mSensorManager;
	private OnShakeListener mListener;

	// 手机上一个位置时重力感应坐标
	private float lastX;
	private float lastY;
	private float lastZ;
	// 上次检测时间
	private long lastUpdateTime;

	public interface OnShakeListener {
		/**
		 * 在手机被晃动的时候调用
		 */
		public void onShaked();
	}

	public void setOnShakeListener(OnShakeListener listener) {
		mListener = listener;
	}

	public ShakeListener(Context context) {
		mCtx = context;
		start();
	}

	private void start() {
		mSensorManager = (SensorManager) mCtx.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			// 获取重力传感器
			Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			if (sensor != null) {
				mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
	}

	public void stop() {
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		long currentUpdateTime = System.currentTimeMillis();
		long timeInterval = currentUpdateTime - lastUpdateTime;
		if (timeInterval < UPTATE_INTERVAL_TIME)
			return;
		
		lastUpdateTime = currentUpdateTime;

		// 获得x,y,z坐标
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		// 获得x,y,z的变化值
		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;

		// 将现在的坐标变成last坐标
		lastX = x;
		lastY = y;
		lastZ = z;

		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
		// 达到速度阀值，发出提示
		if (speed >= SPEED_SHRESHOLD) {
			mListener.onShaked();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
