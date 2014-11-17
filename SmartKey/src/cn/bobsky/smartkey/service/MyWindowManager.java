package cn.bobsky.smartkey.service;

import cn.bobsky.smartkey.view.KeyWindowView;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MyWindowManager {

	// ������View��ʵ��
	private static KeyWindowView keyWindow;
	// ������View�Ĳ���
	private static LayoutParams keyWindowParams;
	// ���ڿ�������Ļ����ӻ��Ƴ�������
	private static WindowManager mWindowManager;

	/**
	 * ����һ��С����������ʼλ��Ϊ��Ļ���Ҳ��м�λ�á�
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
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
	 * ��С����������Ļ���Ƴ���
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void removeSmallWindow(Context context) {
		if (keyWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(keyWindow);
			keyWindow = null;
		}
	}

	/**
	 * �Ƿ���������(����С�������ʹ�������)��ʾ����Ļ�ϡ�
	 * 
	 * @return ����������ʾ�������Ϸ���true��û�еĻ�����false��
	 */
	public static boolean isWindowShowing() {
		return keyWindow != null;
	}

	/**
	 * ���WindowManager��δ�������򴴽�һ���µ�WindowManager���ء�
	 * ���򷵻ص�ǰ�Ѵ�����WindowManager��
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 * @return WindowManager��ʵ�������ڿ�������Ļ����ӻ��Ƴ���������
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			
		}
		return mWindowManager;
	}
}