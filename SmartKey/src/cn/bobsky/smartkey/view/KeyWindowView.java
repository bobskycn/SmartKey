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

	// ��¼С�������Ŀ��,�߶�
	public static int viewWidth;
	public static int viewHeight;
	// ��¼ϵͳ״̬���ĸ߶�
	private static int statusBarHeight;
	// ���ڸ���С��������λ��
	private WindowManager windowManager;
	// С�������Ĳ���
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

	// ��¼��ǰ��ָλ������Ļ�ϵĺ�����ֵ,������ֵ
	private float xCurrentInScreen;
	private float yCurrentInScreen;

	// ��¼��ָ����ʱ����Ļ�ϵĺ������ֵ,�������ֵ
	private float xDownInScreen;
	private float yDownInScreen;
	// ��¼��ָ����ʱ��С��������View�ϵĺ������ֵ,�������ֵ
	private float xInView;
	private float yInView;

	// �������
	private int mClickCount = 0;
	// ��ǰ���ʱ��
	private long mCurrentClickTime = 0;

	private Handler mBaseHandler = new Handler();
	// �����߳�
	private LongPressedThread mLongPressedThread;
	// ����ȴ��߳�
	private ClickPressedThread mPrevClickThread;
	private SwipeThread mSwipeThread;
	private DragingOnThread mDragingOnThread;

	private boolean isDragging = false;
	private boolean isSwiping = false;

	// private boolean isDragingMove

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// Log.i("testData", "event---" + event);
		// ��ȡ�����Ļ�����꣬������Ļ���Ͻ�Ϊԭ��
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
			// ��ָ����ʱ��¼��Ҫ����,�������ֵ����Ҫ��ȥ״̬���߶�
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			// ��¼��ǰ�����ʱ��
			mCurrentClickTime = Calendar.getInstance().getTimeInMillis();
			// ���������1
			mClickCount++;
			Log.i("testData", "�ѵ������--" + mClickCount);

			// ȡ����һ�ε�����߳�
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
				// // ��ָ�ƶ���ʱ�����С��������λ��
				updateViewPosition();
			}

			break;
		case MotionEvent.ACTION_UP:
			isDragging = false;
			isSwiping = false;
			if (!this.isMoved()) {
				if (time <= CLICK_SPACING_TIME) {
					// ���½���һ������̣߳��ӳ�CLICK_SPACING_TIMEִ��
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
	 * �ж��Ƿ��ƶ�
	 * 
	 * @return
	 */
	private boolean isMoved() {
		// ������5��ƫ�� ���ж��Ƿ��ƶ���ʱ��
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

			// ���ﴦ�����¼�
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
	 * ��С�������Ĳ������룬���ڸ���С��������λ�á�
	 * 
	 * @param params
	 *            С�������Ĳ���
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * ����С����������Ļ�е�λ�á�
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xCurrentInScreen - xInView);
		mParams.y = (int) (yCurrentInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**
	 * ���ڻ�ȡ״̬���ĸ߶ȡ�
	 * 
	 * @return ����״̬���߶ȵ�����ֵ��
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