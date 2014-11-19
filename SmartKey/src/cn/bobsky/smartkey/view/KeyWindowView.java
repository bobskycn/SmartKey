package cn.bobsky.smartkey.view;

import java.lang.reflect.Field;
import java.util.Calendar;

import cn.bobsky.smartkey.R;
import cn.bobsky.smartkey.utils.TakeActionUtils;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
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
		mTakeActionUtils = new TakeActionUtils(context);
		LayoutInflater.from(context).inflate(R.layout.view_smart_key, this);

		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		View view = findViewById(R.id.smart_key_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}

	private Context mContext;
	private TakeActionUtils mTakeActionUtils;

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
	private static final long LONG_PRESS_TIME = 700;
	private static final long DRAG_TIME = 900;

	int currentState = 0;
	float rangfloat = 100;

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
	private long mCurrentClickTime = 0;

	private Handler mBaseHandler = new Handler();
	// 长按线程
	private LongPressedThread mLongPressedThread;
	// 点击等待线程
	private ClickPressedThread mPrevClickThread;
	private SwipeThread mSwipeThread;
	private DragingOnThread mDragingOnThread;

	private boolean isDragging = false;
	private boolean isSwiping = false;

	// private boolean isDragingMove

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// Log.i("testData", "event---" + event);
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		xCurrentInScreen = (int) event.getRawX();
		yCurrentInScreen = (int) event.getRawY() - getStatusBarHeight();

		long time = Calendar.getInstance().getTimeInMillis()
				- mCurrentClickTime;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			//mTakeActionUtils.performClickDownVibrateAction();
			mTakeActionUtils.performClickDownSoundAction();
			isDragging = false;
			isSwiping = false;
			// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			// 记录当前点击的时间
			mCurrentClickTime = Calendar.getInstance().getTimeInMillis();
			// 点击次数加1
			mClickCount++;
			Log.i("testData", "已点击次数--" + mClickCount);

			// 取消上一次点击的线程
			if (mPrevClickThread != null) {
				mBaseHandler.removeCallbacks(mPrevClickThread);
			}
			mDragingOnThread = new DragingOnThread();
			mBaseHandler.postDelayed(mDragingOnThread, DRAG_TIME);

			break;
		case MotionEvent.ACTION_MOVE:

			if (this.isMoved()) {
				if (isDragging == false) {
					// swip
					isSwiping = true;
					if (mDragingOnThread != null) {
						mBaseHandler.removeCallbacks(mDragingOnThread);
					}

					mSwipeThread = new SwipeThread();
					mBaseHandler.post(mSwipeThread);
				}

			}
			if (isSwiping == false && isDragging == true) {
				// // 手指移动的时候更新小悬浮窗的位置
				updateViewPosition();
			}

			break;
		case MotionEvent.ACTION_UP:
			isDragging = false;
			isSwiping = false;
			if (!this.isMoved()) {
				if (time <= CLICK_SPACING_TIME) {
					// 从新建立一个点击线程，延迟CLICK_SPACING_TIME执行
					mPrevClickThread = new ClickPressedThread();
					mBaseHandler.postDelayed(mPrevClickThread,
							(CLICK_SPACING_TIME - time));
					// if (mDragingOnThread != null) {
					// mBaseHandler.removeCallbacks(mDragingOnThread);
					// }
				} else if (time > CLICK_SPACING_TIME && time <= LONG_PRESS_TIME) {
					mLongPressedThread = new LongPressedThread();
					mBaseHandler.postDelayed(mLongPressedThread,
							LONG_PRESS_TIME);
				}
				if (mDragingOnThread != null) {
					mBaseHandler.removeCallbacks(mDragingOnThread);
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

	public class SwipeThread implements Runnable {

		@Override
		public void run() {
			mClickCount = 0;
			float x = xDownInScreen - xCurrentInScreen;
			float y = yDownInScreen - yCurrentInScreen;
			if (Math.abs(x) < Math.abs(y)) {
				if (y < 0) {
					mTakeActionUtils.performSwipDownAction();
				} else {
					mTakeActionUtils.performSwipUpAction();
				}
			} else {
				if (x < 0) {
					mTakeActionUtils.performSwipRightAction();
				} else {
					mTakeActionUtils.performSwipLeftAction();
				}
			}

		}

	}

	public class DragingOnThread implements Runnable {
		@Override
		public void run() {
			isDragging = true;
			if (isSwiping == false) {
				mTakeActionUtils.performVibrateAction();
			}
			mClickCount = 0;
		}
	}

	public class LongPressedThread implements Runnable {
		@Override
		public void run() {

			// 这里处理长按事件
			mTakeActionUtils.performLongClickAction(mClickCount);
			mClickCount = 0;
		}
	}

	public class ClickPressedThread implements Runnable {
		@Override
		public void run() {
			mTakeActionUtils.performClickAction(mClickCount);
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