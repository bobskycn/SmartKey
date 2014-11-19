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
		// ������Ч��ʼ�����������ʼ�����������ˡ����Ǹ���-_-
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);// ��һ������Ϊͬʱ���������������������ڶ����������ͣ�����Ϊ��������
		music = sp.load(mContext, R.raw.button_sound, 1); // ����������زķŵ�res/raw���2��������Ϊ��Դ�ļ�����3��Ϊ���ֵ����ȼ�

	}

	private Context mContext;
	private SoundPool sp;// ����һ��SoundPool
	private int music;// ����һ��������load������������suondID
	private AudioManager mAudioManager;

	public void performClickAction(int mClickCount) {
		Toast.makeText(mContext, "���--" + mClickCount + "--��!",
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
		Toast.makeText(mContext, "����--" + mClickCount + "--times!",
				Toast.LENGTH_SHORT).show();
	}

	public void performSwipUpAction() {
		Toast.makeText(mContext, "�ϻ�--", Toast.LENGTH_SHORT).show();
	}

	public void performSwipDownAction() {
		Toast.makeText(mContext, "�»�--", Toast.LENGTH_SHORT).show();
	}

	public void performSwipLeftAction() {
		Toast.makeText(mContext, "��--", Toast.LENGTH_SHORT).show();
	}

	public void performSwipRightAction() {
		Toast.makeText(mContext, "�һ�--", Toast.LENGTH_SHORT).show();
	}

	public void performVibrateAction() {
		Toast.makeText(mContext, "�ƶ�������", Toast.LENGTH_SHORT).show();
		Vibrator vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 50, 100 }; // ֹͣ ����
		vibrator.vibrate(pattern, -1); // �ظ����������pattern ���ֻ����һ�Σ�index��Ϊ-1

	}

	public void performClickDownVibrateAction() {
		// Toast.makeText(mContext, "�ƶ�������", Toast.LENGTH_SHORT).show();
		Vibrator vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, 20 }; // ֹͣ ����
		vibrator.vibrate(pattern, -1); // �ظ����������pattern ���ֻ����һ�Σ�index��Ϊ-1

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
