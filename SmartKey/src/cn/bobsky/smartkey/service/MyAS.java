package cn.bobsky.smartkey.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class MyAS extends AccessibilityService {

	
	private static MyAS sSharedInstance;

	protected void onServiceConnected() {
	    sSharedInstance = this;
	    Toast.makeText(getApplicationContext(), "MyAs start",
				Toast.LENGTH_SHORT).show();
	    Log.i("testData", "MyAs start");
	}

	public boolean onUnbind(Intent intent) {
	    sSharedInstance = null;
	    return super.onUnbind(intent);
	}

	public static MyAS getSharedInstance() {
	    
		return sSharedInstance;
	}


	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

	

}
