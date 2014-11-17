package cn.bobsky.smartkey.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class KeyWindowService extends Service {

	//private MyWindowManager myWindowManager;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MyWindowManager.createKeyWindow(getApplicationContext());
			if(MyAS.getSharedInstance()==null){
				Toast.makeText(getApplicationContext(), "MyAs==null",
						Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "MyAs!=null",
						Toast.LENGTH_SHORT).show();
			}
		return super.onStartCommand(intent, flags, startId);
	}

	

	@Override
	public void onDestroy() {
		MyWindowManager.removeSmallWindow(getApplicationContext());
		super.onDestroy();

	}
}
