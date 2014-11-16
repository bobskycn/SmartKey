package cn.bobsky.smartkey.view;

import java.lang.reflect.Field;

import cn.bobsky.smartkey.R;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.LinearLayout;

public class FloatKeyView extends LinearLayout {

	/**
	 * ��¼С�������Ŀ��
	 */
	public static int viewWidth;

	/**
	 * ��¼С�������ĸ߶�
	 */
	public static int viewHeight;

	/**
	 * ��¼ϵͳ״̬���ĸ߶�
	 */
	private static int statusBarHeight;

	/**
	 * ���ڸ���С��������λ��
	 */
	private WindowManager windowManager;

	/**
	 * С�������Ĳ���
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * ��¼��ǰ��ָλ������Ļ�ϵĺ�����ֵ
	 */
	private float xInScreen;

	/**
	 * ��¼��ǰ��ָλ������Ļ�ϵ�������ֵ
	 */
	private float yInScreen;

	/**
	 * ��¼��ָ����ʱ����Ļ�ϵĺ������ֵ
	 */
	private float xDownInScreen;

	/**
	 * ��¼��ָ����ʱ����Ļ�ϵ��������ֵ
	 */
	private float yDownInScreen;

	/**
	 * ��¼��ָ����ʱ��С��������View�ϵĺ������ֵ
	 */
	private float xInView;

	/**
	 * ��¼��ָ����ʱ��С��������View�ϵ��������ֵ
	 */
	private float yInView;
	private Context mContext;

	/**
	 * The accessibility manager for this context. This is used to check the
	 * accessibility enabled state, as well as to send raw accessibility events.
	 */
	private final AccessibilityManager mA11yManager;

	public FloatKeyView(Context context) {
		super(context);
		mContext = context;
		mA11yManager = (AccessibilityManager) context
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.view_smart_key, this);
		View view = findViewById(R.id.smart_key_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��ָ����ʱ��¼��Ҫ����,�������ֵ����Ҫ��ȥ״̬���߶�
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			// ��ָ�ƶ���ʱ�����С��������λ��
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
			// �����ָ�뿪��Ļʱ��xDownInScreen��xInScreen��ȣ���yDownInScreen��yInScreen��ȣ�����Ϊ�����˵����¼���
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
				performClick();

			}
			break;
		default:
			break;
		}
		super.dispatchTouchEvent(event);
		return true;

	}

	@Override
	public boolean performClick() {
		// Calls the super implementation, which generates an AccessibilityEvent
		// and calls the onClick() listener on the view, if any
		super.performClick();

		// Handle the action for the custom click here
		announceForAccessibility("back");
		Log.i("testData", "2----float view was clicked!");

		return true;
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
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
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

	/**
	 * Generates and dispatches an SDK-specific spoken announcement.
	 * <p>
	 * For backwards compatibility, we're constructing an event from scratch
	 * using the appropriate event type. If your application only targets SDK
	 * 16+, you can just call View.announceForAccessibility(CharSequence).
	 * </p>
	 * 
	 * @param text
	 *            The text to announce.
	 */
	private void announceForAccessibilityCompat(CharSequence text) {
		if (!mA11yManager.isEnabled()) {
			return;
		}

		// Prior to SDK 16, announcements could only be made through FOCUSED
		// events. Jelly Bean (SDK 16) added support for speaking text verbatim
		// using the ANNOUNCEMENT event type.
		final int eventType = AccessibilityEventCompat.TYPE_ANNOUNCEMENT;

		// Construct an accessibility event with the minimum recommended
		// attributes. An event without a class name or package may be dropped.
		final AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
		event.getText().add(text);
		event.setEnabled(isEnabled());
		event.setClassName(getClass().getName());
		event.setPackageName(mContext.getPackageName());

		// JellyBean MR1 requires a source view to set the window ID.
		final AccessibilityRecordCompat record = new AccessibilityRecordCompat(
				event);
		record.setSource(this);

		// Sends the event directly through the accessibility manager. If your
		// application only targets SDK 14+, you should just call
		// getParent().requestSendAccessibilityEvent(this, event);
		mA11yManager.sendAccessibilityEvent(event);
	}
}