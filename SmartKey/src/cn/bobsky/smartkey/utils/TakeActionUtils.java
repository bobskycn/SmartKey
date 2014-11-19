package cn.bobsky.smartkey.utils;

import cn.bobsky.smartkey.R;
import cn.bobsky.smartkey.service.MyAS;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Toast;

public class TakeActionUtils {

	public TakeActionUtils(Context context) {
		mContext = context;
		// 按键音效初始化，如果不初始化，将播不了。这是个坑-_-
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		music = sp.load(mContext, R.raw.button_sound, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

	}

	private Context mContext;
	private SoundPool sp;// 声明一个SoundPool
	private int music;// 定义一个整型用load（）；来设置suondID
	private AudioManager mAudioManager;

	public void performClickAction(int mClickCount) {
		Toast.makeText(mContext, "点击--" + mClickCount + "--次!",
				Toast.LENGTH_SHORT).show();
		switch (mClickCount) {
		case 1:

			if (MyAS.getSharedInstance() != null) {
				MyAS.getSharedInstance().performGlobalAction(
						AccessibilityService.GLOBAL_ACTION_BACK);
			} else {
				Intent sSettingsIntent = new Intent(
						Settings.ACTION_ACCESSIBILITY_SETTINGS);
				sSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(sSettingsIntent);
			}

			break;
		}
	}

	public void performLongClickAction(int mClickCount) {
		Toast.makeText(mContext, "长按--" + mClickCount + "--times!",
				Toast.LENGTH_SHORT).show();
	}

	public void performSwipUpAction() {
		Toast.makeText(mContext, "上划--", Toast.LENGTH_SHORT).show();
	}

	public void performSwipDownAction() {
		Toast.makeText(mContext, "下划--", Toast.LENGTH_SHORT).show();
	}

	public void performSwipLeftAction() {
		Toast.makeText(mContext, "左划--", Toast.LENGTH_SHORT).show();
	}

	public void performSwipRightAction() {
		Toast.makeText(mContext, "右划--", Toast.LENGTH_SHORT).show();
	}

	public void performVibrateAction() {
		Toast.makeText(mContext, "移动悬浮窗", Toast.LENGTH_SHORT).show();
		Vibrator vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 50, 100 }; // 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1

	}

	public void performClickDownVibrateAction() {
		// Toast.makeText(mContext, "移动悬浮窗", Toast.LENGTH_SHORT).show();
		Vibrator vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, 20 }; // 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1

	}

	public void performClickDownSoundAction() {
		Toast.makeText(mContext, "play sound", Toast.LENGTH_SHORT).show();
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		sp.play(music, streamVolume, streamVolume, 0, 0, 1);
	}
}
