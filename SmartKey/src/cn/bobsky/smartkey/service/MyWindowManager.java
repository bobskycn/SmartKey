package cn.bobsky.smartkey.service;

import cn.bobsky.smartkey.view.KeyWindowView;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MyWindowManager {

	// 悬浮窗View的实例
	private static KeyWindowView keyWindow;
	// 悬浮窗View的参数
	private static LayoutParams keyWindowParams;
	// 用于控制在屏幕上添加或移除悬浮窗
	private static WindowManager mWindowManager;

	/**
	 * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void createKeyWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (keyWindow == null) {
			keyWindow = new KeyWindowView(context);
			if (keyWindowParams == null) {
				keyWindowParams = new LayoutParams();
				keyWindowParams.type = LayoutParams.TYPE_PHONE;
				keyWindowParams.format = PixelFormat.RGBA_8888;
				// smallWindowParams.format = PixelFormat.TRANSLUCENT;
				 keyWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				 | LayoutParams.FLAG_NOT_FOCUSABLE;
			//	smallWindowParams.flags =LayoutParams.FLAG_ALT_FOCUSABLE_IM;
				keyWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				keyWindowParams.width = KeyWindowView.viewWidth;
				keyWindowParams.height = KeyWindowView.viewHeight;
				keyWindowParams.x = screenWidth;
				keyWindowParams.y = screenHeight / 2;
			}
			keyWindow.setParams(keyWindowParams);
			windowManager.addView(keyWindow, keyWindowParams);
			

		}
	}
	

	/**
	 * 将小悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void removeSmallWindow(Context context) {
		if (keyWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(keyWindow);
			keyWindow = null;
		}
	}

	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
	 * 
	 * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
	 */
	public static boolean isWindowShowing() {
		return keyWindow != null;
	}

	/**
	 * 如果WindowManager还未创建，则创建一个新的WindowManager返回。
	 * 否则返回当前已创建的WindowManager。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			
		}
		return mWindowManager;
	}
}