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
	private static final long LONG_PRESS_TIME = 600;
	private static final long DRAG_TIME = 1000;

	int currentState = 0;

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
	private long mCurrentClickTime;

	private Handler mBaseHandler = new Handler();
	// �����߳�
	private LongPressedThread mLongPressedThread;
	// ����ȴ��߳�
	private ClickPressedThread mPrevClickThread;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		// ��ȡ�����Ļ�����꣬������Ļ���Ͻ�Ϊԭ��
		xCurrentInScreen = (int) event.getRawX();
		yCurrentInScreen = (int) event.getRawY() - getStatusBarHeight();
		;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��ָ����ʱ��¼��Ҫ����,�������ֵ����Ҫ��ȥ״̬���߶�
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			// ��¼��ǰ�����ʱ��
			mCurrentClickTime = Calendar.getInstance().getTimeInMillis();
			// ���������1
			mClickCount++;
			// ȡ����һ�ε�����߳�
			if (mPrevClickThread != null) {
				mBaseHandler.removeCallbacks(mPrevClickThread);
			}
			// ���µ�ʱ�򴥷��ӳ�500��ִ�еĵĳ���ʱ�䡣

			//mBaseHandler.postDelayed(mLongPressedThread, LONG_PRESS_TIME);
			break;
		case MotionEvent.ACTION_MOVE:
			xCurrentInScreen = event.getRawX();
			yCurrentInScreen = event.getRawY() - getStatusBarHeight();
			if (this.isMoved()) {
				mClickCount = 0; // ֻҪ�ƶ��� ��û�е���¼���
			}
			if ((Calendar.getInstance().getTimeInMillis() - mCurrentClickTime) >= DRAG_TIME) {
				// ��ָ�ƶ���ʱ�����С��������λ��			
				updateViewPosition();
			}

			break;
		case MotionEvent.ACTION_UP:
			if (!this.isMoved()) {
				long time = Calendar.getInstance().getTimeInMillis()
						- mCurrentClickTime;
				// �����ס��ʱ�䳬���˳���ʱ�䣬��ô��ʵ�����¼��Ѿ���������,���ʱ�����������
				if (time <= LONG_PRESS_TIME) {
					// ȡ��ע��ĳ����¼�
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

	public class LongPressedThread implements Runnable {
		@Override
		public void run() {
			Toast.makeText(mContext, "long press onClicked--" + mClickCount + "--times!",
					Toast.LENGTH_SHORT).show();
			// ���ﴦ�����¼�
			mClickCount = 0;
		}
	}

	public class ClickPressedThread implements Runnable {
		@Override
		public void run() {
			Toast.makeText(mContext, "onClick--" + mClickCount + "--times!",
					Toast.LENGTH_SHORT).show();
			// ���ﴦ����������¼� mClickCount Ϊ��������Ĵ���
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