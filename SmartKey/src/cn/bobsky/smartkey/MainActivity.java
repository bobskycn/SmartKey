package cn.bobsky.smartkey;

import cn.bobsky.smartkey.fragment.FragmentMain;
import cn.bobsky.smartkey.service.KeyWindowService;
import cn.bobsky.smartkey.utils.SmartBarUtils;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ActionBar bar = getActionBar();
		SmartBarUtils.setBackIcon(bar,
				getResources().getDrawable(R.drawable.mz_ic_sb_back));
//		bar.setHomeButtonEnabled(false);
//		bar.setDisplayUseLogoEnabled(false);
//		bar.setHomeButtonEnabled(true);
		
		 Intent intent = new Intent(MainActivity.this, KeyWindowService.class);  
         startService(intent); 
         if (savedInstanceState == null) {
             getFragmentManager().beginTransaction()
                     .add(R.id.container, new FragmentMain())
                     .commit();
         }

//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
