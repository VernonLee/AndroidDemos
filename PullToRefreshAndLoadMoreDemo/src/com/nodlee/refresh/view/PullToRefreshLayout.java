package com.nodlee.refresh.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nodlee.refresh.R;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个
 * 是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头。
 * 
 * 更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 * @author 陈靖
 */
public class PullToRefreshLayout extends RelativeLayout {
	public static final String TAG = "PullToRefreshLayout";
	
	/** 一分钟的毫秒值，用于判断上次的更新时间 */  
    public static final long ONE_MINUTE = 60 * 1000;  
    /** 一小时的毫秒值，用于判断上次的更新时间 */  
    public static final long ONE_HOUR = 60 * ONE_MINUTE;  
    /** 一天的毫秒值，用于判断上次的更新时间 */  
    public static final long ONE_DAY = 24 * ONE_HOUR;  
    /** 一月的毫秒值，用于判断上次的更新时间*/  
    public static final long ONE_MONTH = 30 * ONE_DAY;  
    /** 一年的毫秒值，用于判断上次的更新时间 */  
    public static final long ONE_YEAR = 12 * ONE_MONTH;  
    /** 上次更新时间的字符串常量，用于作为SharedPreferences的键值  */  
    private static final String UPDATED_AT = "updated_at";  
	
    /** 初始状态 */
	public static final int INIT = 0;
	/**  释放刷新 */
	public static final int RELEASE_TO_REFRESH = 1;
	/**  正在刷新 */
	public static final int REFRESHING = 2;
	/** 释放加载 */
	public static final int RELEASE_TO_LOAD = 3;
	/** 正在加载 */
	public static final int LOADING = 4;
	/** 操作完毕 */
	public static final int DONE = 5;
	/** 当前状态 */
	private int state = INIT;
	/**  刷新成功 */
	public static final int SUCCEED = 0;
	/** 刷新失败 */
	public static final int FAIL = 1;
	/** 刷新回调接口 */
	private OnRefreshListener mListener;
	
	/** 按下Y坐标，上一个事件点Y坐标*/
	private float downY, lastY;
	/** 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0 */
	public float pullDownY = 0;
	/** 上拉的距离 */
	private float pullUpY = 0;
	/**  释放刷新的距离 */
	private float refreshDist = 200;
	/** 释放加载的距离 */
	private float loadmoreDist = 200;

	private MyTimer timer;
	
	/** 回滚速度 */
	public float MOVE_SPEED = 8;
	/**  第一次执行布局 */
	private boolean isLayout = false;
	/** 在刷新过程中滑动操作 */
	private boolean isTouch = false;
	/** 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化 */
	private float radio = 2;
	/** 下拉箭头的转180°动画 */
	private RotateAnimation rotateAnimation;
	/** 均匀旋转动画 */
	private RotateAnimation refreshingAnimation;

	/** 下拉头 */
	private View refreshView;
	/** 下拉的箭头 */
	private View pullView;
	/** 正在刷新的图标 */
	private View refreshingView;
	/** 最后刷新时间 */
	private TextView mRefreshTime;
	/** 刷新状态 */
	private TextView refreshStateTextView;

	/** 上拉头 */
	private View loadmoreView;
	/** 正在加载的图标 */
	private View loadingView;
	/** 加载状态 */
	private TextView loadStateTextView;

	/** 实现了Pullable接口的View */
	private View pullableView;
	/** 过滤多点触碰 */
	private int mEvents;
	/** 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉 */
	private boolean canPullDown = true;
	private boolean canPullUp = true;
	
	/** 用于存储上次更新时间 */  
    private SharedPreferences preferences;  
    /** 上次更新时间的毫秒值 */  
    private long lastUpdateTime;  
    
	/** 执行自动回滚的handler  */
	@SuppressLint("HandlerLeak")
	Handler updateHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch) {
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == REFRESHING && pullDownY <= refreshDist) {
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadmoreDist) {
					pullUpY = -loadmoreDist;
					timer.cancel();
				}

			}
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			else if (pullUpY < 0)
				pullUpY += MOVE_SPEED;
			if (pullDownY < 0) {
				// 已完成回弹
				pullDownY = 0;
				pullView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
				requestLayout();
			}
			if (pullUpY > 0) {
				// 已完成回弹
				pullUpY = 0;
				// 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
				requestLayout();
			}
			Log.d("handle", "handle");
			// 刷新布局,会自动调用onLayout
			requestLayout();
			// 没有拖拉或者回弹完成
			if (pullDownY + Math.abs(pullUpY) == 0)
				timer.cancel();
		}
	};

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public PullToRefreshLayout(Context context) {
		super(context);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);  
		timer = new MyTimer(updateHandler);
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.pull_arrow_reverse);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.pull_progress_rotate);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	private void hide() {
		timer.schedule(15);
	}

	/**
	 * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
	 */
	public void refreshFinish() {
		refreshingView.clearAnimation();
		refreshingView.setVisibility(View.GONE);
		preferences.edit().putLong(UPDATED_AT, System.currentTimeMillis()).commit();
		changeState(DONE);
		hide();
	}

	/**
	 * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
	 */
	public void loadmoreFinish() {
		loadingView.clearAnimation();
		loadingView.setVisibility(View.GONE);
		changeState(DONE);
		hide();
	}

	private void changeState(int to) {
		state = to;
		switch (state) {
		case INIT: // 下拉布局初始状态
			refreshStateTextView.setText(R.string.pull_down_to_refresh);
			pullView.clearAnimation();
			pullView.setVisibility(View.VISIBLE);
			loadStateTextView.setText(R.string.pull_up_to_load);
			break;
		case RELEASE_TO_REFRESH: // 释放刷新状态
			refreshStateTextView.setText(R.string.release_to_refresh);
			pullView.startAnimation(rotateAnimation);
			break;
		case REFRESHING: // 正在刷新状态
			pullView.clearAnimation();
			refreshingView.setVisibility(View.VISIBLE);
			pullView.setVisibility(View.INVISIBLE);
			refreshingView.startAnimation(refreshingAnimation);
			refreshStateTextView.setText(R.string.refreshing);
			break;
		case RELEASE_TO_LOAD: // 释放加载状态
			loadStateTextView.setText(R.string.release_to_load);
			break;
		case LOADING:
			loadingView.setVisibility(View.VISIBLE);
			loadingView.startAnimation(refreshingAnimation);
			loadStateTextView.setText(R.string.loading);
			break;
		case DONE: // 刷新或加载完毕，啥都不做
			break;
		}
		refreshUpdatedAtValue();
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}

	/*
	 * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP: // 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mEvents == 0) {
				if (pullDownY > 0
						|| (((Pullable) pullableView).canPullDown()
								&& canPullDown && state != LOADING)) {
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0) {
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight())
						pullDownY = getMeasuredHeight();
					if (state == REFRESHING) {
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
				} else if (pullUpY < 0
						|| (((Pullable) pullableView).canPullUp() && canPullUp && state != REFRESHING)) {
					// 可以上拉，正在刷新时不能上拉
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0) {
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight())
						pullUpY = -getMeasuredHeight();
					if (state == LOADING) {
						// 正在加载的时候触摸移动
						isTouch = true;
					}
				} else
					releasePull();
			} else
				mEvents = 0;
			lastY = ev.getY();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			if (pullDownY > 0 || pullUpY < 0)
				requestLayout();
			if (pullDownY > 0) {
				if (pullDownY <= refreshDist
						&& (state == RELEASE_TO_REFRESH || state == DONE)) {
					// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
					changeState(INIT);
				}
				if (pullDownY >= refreshDist && state == INIT) {
					// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
					changeState(RELEASE_TO_REFRESH);
				}
			} else if (pullUpY < 0) {
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpY <= loadmoreDist
						&& (state == RELEASE_TO_LOAD || state == DONE)) {
					changeState(INIT);
				}
				// 上拉操作
				if (-pullUpY >= loadmoreDist && state == INIT) {
					changeState(RELEASE_TO_LOAD);
				}

			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownY + Math.abs(pullUpY)) > 8) {
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownY > refreshDist || -pullUpY > loadmoreDist) {
			// 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
				isTouch = false;
			}
			if (state == RELEASE_TO_REFRESH) {
				changeState(REFRESHING);
				// 刷新操作
				if (mListener != null)
					mListener.onRefresh(this);
			} else if (state == RELEASE_TO_LOAD) {
				changeState(LOADING);
				// 加载操作
				if (mListener != null)
					mListener.onLoadMore(this);
			}
			hide();
		default:
			break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}

	/**
	 * @author chenjing 自动模拟手指滑动的task
	 */
	private class AutoRefreshAndLoadTask extends
			AsyncTask<Integer, Float, String> {

		@Override
		protected String doInBackground(Integer... params) {
			while (pullDownY < 4 / 3 * refreshDist) {
				pullDownY += MOVE_SPEED;
				publishProgress(pullDownY);
				try {
					Thread.sleep(params[0]);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			changeState(REFRESHING);
			// 刷新操作
			if (mListener != null)
				mListener.onRefresh(PullToRefreshLayout.this);
			hide();
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			if (pullDownY > refreshDist)
				changeState(RELEASE_TO_REFRESH);
			requestLayout();
		}
	}

	/**
	 * 自动刷新
	 */
	public void autoRefresh() {
		AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
		task.execute(20);
	}

	/**
	 * 自动加载
	 */
	public void autoLoad() {
		pullUpY = -loadmoreDist;
		requestLayout();
		changeState(LOADING);
		// 加载操作
		if (mListener != null)
			mListener.onLoadMore(this);
	}

	private void initView() {
		// 初始化下拉布局
		pullView = refreshView.findViewById(R.id.img_ptr_arrow);
		refreshStateTextView = (TextView) refreshView.findViewById(R.id.txt_ptr_title);
		refreshingView = refreshView.findViewById(R.id.progress_ptr_refreshing);
		mRefreshTime =  (TextView) refreshView.findViewById(R.id.txt_ptr_update_time);
		loadStateTextView = (TextView) loadmoreView.findViewById(R.id.txt_ptl_title);
		loadingView = loadmoreView.findViewById(R.id.progress_ptl_loading);
		refreshUpdatedAtValue();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (!isLayout) {
			// 这里是第一次进来的时候做一些初始化
			refreshView = getChildAt(0);
			pullableView = getChildAt(1);
			loadmoreView = getChildAt(2);
			isLayout = true;
			initView();
			refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
			loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();
		}
		// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		refreshView.layout(0,
				(int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
				refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pullableView.layout(0, (int) (pullDownY + pullUpY),
				pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
						+ pullableView.getMeasuredHeight());
		loadmoreView.layout(0,
				(int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
				loadmoreView.getMeasuredWidth(),
				(int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
						+ loadmoreView.getMeasuredHeight());
	}

	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	/** 
     * 刷新下拉头中上次更新时间的文字描述。 
     */  
    private void refreshUpdatedAtValue() {  
        lastUpdateTime = preferences.getLong(UPDATED_AT, -1);  
        long currentTime = System.currentTimeMillis();  
        long timePassed = currentTime - lastUpdateTime;  
        long timeIntoFormat;  
        String updateAtValue;  
        if (lastUpdateTime == -1) {  
            updateAtValue = getResources().getString(R.string.not_updated_yet);  
        } else if (timePassed < 0) {  
            updateAtValue = getResources().getString(R.string.time_error);  
        } else if (timePassed < ONE_MINUTE) {  
            updateAtValue = getResources().getString(R.string.updated_just_now);  
        } else if (timePassed < ONE_HOUR) {  
            timeIntoFormat = timePassed / ONE_MINUTE;  
            updateAtValue = timeIntoFormat + "分钟";  
        } else if (timePassed < ONE_DAY) {  
            timeIntoFormat = timePassed / ONE_HOUR;  
            updateAtValue = timeIntoFormat + "小时";  
        } else if (timePassed < ONE_MONTH) {  
            timeIntoFormat = timePassed / ONE_DAY;  
            updateAtValue = timeIntoFormat + "天";  
        } else if (timePassed < ONE_YEAR) {  
            timeIntoFormat = timePassed / ONE_MONTH;  
            updateAtValue = timeIntoFormat + "个月";  
        } else {  
            timeIntoFormat = timePassed / ONE_YEAR;  
            updateAtValue = timeIntoFormat + "年";  
        }  
        mRefreshTime.setText(updateAtValue);
    }  
    
	/**
	 * 刷新加载回调接口
	 * 
	 * @author chenjing
	 * 
	 */
	public interface OnRefreshListener {
		/**
		 * 刷新操作
		 */
		void onRefresh(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 加载操作
		 */
		void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
	}

}
