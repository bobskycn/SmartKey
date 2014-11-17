package cn.bobsky.smartkey.view;

import java.lang.reflect.Field;
import java.util.Calendar;

import cn.bobsky.smartkey.R;
import cn.bobsky.smartkey.service.MyAS;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.LinearLayout;
import android.widget.Toast;

public class KeyWindowView extends LinearLayout implements
		AccessibilityEventSource {
	public KeyWindowView(Context context) {
		super(context);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.view_smart_key, this);

		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		View view = findViewById(R.id.smart_key_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}

	private Context mContext;

	// 记录小悬浮窗的宽度,高度
	public static int viewWidth;
	public static int viewHeight;
	// 记录系统状态栏的高度
	private static int statusBarHeight;
	// 用于更新小悬浮窗的位置
	private WindowManager windowManager;
	// 小悬浮窗的参数
	private WindowManager.LayoutParams mParams;

	private static final long CLICK_SPACING_TIME = 300;
	private static final long LONG_PRESS_TIME = 600;
	private static final long DRAG_TIME = 1000;

	int currentState = 0;

	private static final int STATE_ONCE_CLICK = 1;
	private static final int STATE_LONG_CLICK = 2;
	private static final int STATE_ON_DRAG = 3;
	private static final int STATE_MORE_CILCK = 4;

	// 记录当前手指位置在屏幕上的横坐标值,纵坐标值
	private float xCurrentInScreen;
	private float yCurrentInScreen;

	// 记录手指按下时在屏幕上的横坐标的值,纵坐标的值
	private float xDownInScreen;
	private float yDownInScreen;
	// 记录手指按下时在小悬浮窗的View上的横坐标的值,纵坐标的值
	private float xInView;
	private float yInView;

	// 点击次数
	private int mClickCount = 0;
	// 当前点击时间
	private long mCurrentClickTime;

	private Handler mBaseHandler = new Handler();
	// 长按线程
	private LongPressedThread mLongPressedThread;
	// 点击等待线程
	private ClickPressedThread mPrevClickThread;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		xCurrentInScreen = (int) event.getRawX();
		yCurrentInScreen = (int) event.getRawY() - getStatusBarHeight();
		;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			// 记录当前点击的时间
			mCurrentClickTime = Calendar.getInstance().getTimeInMillis();
			// 点击次数加1
			mClickCount++;
			// 取消上一次点击的线程
			if (mPrevClickThread != null) {
				mBaseHandler.removeCallbacks(mPrevClickThread);
			}
			// 按下的时候触发延迟500秒执行的的长按时间。

			//mBaseHandler.postDelayed(mLongPressedThread, LONG_PRESS_TIME);
			break;
		case MotionEvent.ACTION_MOVE:
			xCurrentInScreen = event.getRawX();
			yCurrentInScreen = event.getRawY() - getStatusBarHeight();
			if (this.isMoved()) {
				mClickCount = 0; // 只要移动了 就没有点击事件了
			}
			if ((Calendar.getInstance().getTimeInMillis() - mCurrentClickTime) >= DRAG_TIME) {
				// 手指移动的时候更新小悬浮窗的位置			
				updateViewPosition();
			}

			break;
		case MotionEvent.ACTION_UP:
			if (!this.isMoved()) {
				long time = Calendar.getInstance().getTimeInMillis()
						- mCurrentClickTime;
				// 如果按住的时间超过了长按时间，那么其实长按事件已经出发生了,这个时候把数据清零
				if (time <= LONG_PRESS_TIME) {
					// 取消注册的长按事件
					//mBaseHandler.removeCallbacks(mLongPressedThread);
					mPrevClickThread = new ClickPressedThread();
					mBaseHandler.postDelayed(mPrevClickThread,
							CLICK_SPACING_TIME);
				} else if (time >= LONG_PRESS_TIME && time <= DRAG_TIME) {
					mLongPressedThread = new LongPressedThread();
					mBaseHandler.post(mLongPressedThread);
				}
			}
			

			break;
		default:
			break;
		}
		super.dispatchTouchEvent(event);
		return true;

	}

	/**
	 * 判断是否移动
	 * 
	 * @return
	 */
	private boolean isMoved() {
		// 允许有5的偏差 在判断是否移动的时候
		if (Math.abs(xDownInScreen - xCurrentInScreen) <= 5
				&& Math.abs(yDownInScreen - yCurrentInScreen) <= 5) {
			return false;
		} else {
			return true;
		}
	}

	public class LongPressedThread implements Runnable {
		@Override
		public void run() {
			Toast.makeText(mContext, "long press onClicked--" + mClickCount + "--times!",
					Toast.LENGTH_SHORT).show();
			// 这里处理长按事件
			mClickCount = 0;
		}
	}

	public class ClickPressedThread implements Runnable {
		@Override
		public void run() {
			Toast.makeText(mContext, "onClick--" + mClickCount + "--times!",
					Toast.LENGTH_SHORT).show();
			// 这里处理连续点击事件 mClickCount 为连续点击的次数
			switch (mClickCount) {
			case 1:
//				MyAccessibilityService.getSharedInstance()
//				.performGlobalAction(
//						AccessibilityService.GLOBAL_ACTION_BACK);
				
				
				if (MyAS.getSharedInstance() != null) {
					MyAS.getSharedInstance()
							.performGlobalAction(
									AccessibilityService.GLOBAL_ACTION_BACK);
				} else {
					Intent sSettingsIntent = new Intent(
							Settings.ACTION_ACCESSIBILITY_SETTINGS);
					sSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(sSettingsIntent);
				}
				break;
			}
			mClickCount = 0;
		}
	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * 更新小悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xCurrentInScreen - xInView);
		mParams.y = (int) (yCurrentInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}
}