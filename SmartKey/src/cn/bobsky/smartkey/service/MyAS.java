package cn.bobsky.smartkey.service;

import java.util.List;
import java.util.Timer;
import cn.bobsky.smartkey.view.FloatKeyView;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;

public class MyAS extends AccessibilityService {

	public MyAS() {

	}

	@Override
	protected void onServiceConnected() {
		// TODO Auto-generated method stub
		super.onServiceConnected();
		Log.i("testData", "MyAccessibilityService started!--------------");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// this.performGlobalAction(GLOBAL_ACTION_BACK);
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		final int eventType = event.getEventType();
		
		switch (eventType) {
		case AccessibilityEvent.TYPE_ANNOUNCEMENT:
			performGlobalAction(GLOBAL_ACTION_BACK);
			break;
		}
//		if (text.size() > 0) {
//			for (int i = 0; i < text.size(); i++) {
//				if (text.get(i).toString().equalsIgnoreCase("back")) {
//					Log.i("testData", "GLOBAL_ACTION_BACK was performed");
//					performGlobalAction(GLOBAL_ACTION_BACK);
//				} else if (text.get(i).toString().equalsIgnoreCase("home")) {
//					Log.i("testData", "GLOBAL_ACTION_HOME was performed");
//					performGlobalAction(GLOBAL_ACTION_HOME);
//				}
//			}
//		}
		

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

}
