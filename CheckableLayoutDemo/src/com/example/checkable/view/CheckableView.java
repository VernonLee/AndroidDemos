package com.example.checkable.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.checkable.R;

/**
 * @since 2014-12-29
 * @author huailiang
 */
@SuppressLint("NewApi")
public class CheckableView extends LinearLayout implements Checkable {
	private boolean isChecked;
	private TextView tvFood;
	
	public CheckableView(Context context) {
		super(context);
		initView(context);
	}
	
	public CheckableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public CheckableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.food_item, this);
		tvFood = (TextView) rootView.findViewById(R.id.txt_food);
	}
	

	@Override
	public void setChecked(boolean checked) {
		isChecked = checked;
		tvFood.setSelected(checked);
	}

	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void toggle() {
		setChecked(!isChecked);
	}
	
	public void setTitle(String title) {
		tvFood.setText(title);
	}
}
