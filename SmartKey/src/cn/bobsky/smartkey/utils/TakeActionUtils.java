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
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

public class TakeActionUtils {

	public TakeActionUtils(Context context) {
		mContext = context;
		initAnimation();
		// 按键音效初始化，如果不初始化，将播不了。这是个坑-_-
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		music = sp.load(mContext, R.raw.button_sound, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

	}

	private Context mContext;
	private SoundPool sp;// 声明一个SoundPool
	private int music;// 定义一个整型用load（）；来设置suondID
	private AudioManager mAudioManager;
	private Animation scaleAnimation;
	private Animation scaleSmallAnimation;
	private Animation scaleBigAnimation;
	private Animation rotateUpAnimation;
	private Animation rotateDownAnimation;
	private Animation rotateLeftAnimation;
	private Animation rotateRightAnimation;
	private Animation alphaAnimation;

	private AnimationSet animationSetUp;
	private AnimationSet animationSetDown;
	private AnimationSet animationSetLeft;
	private AnimationSet animationSetRight;

	public static final int TYPE_SCALE = 0;
	public static final int TYPE_ROTATE_UP = 1;
	public static final int TYPE_ROTATE_DOWN = 2;
	public static final int TYPE_ROTATE_LEFT = 3;
	public static final int TYPE_ROTATE_RIGHT = 4;

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

	private void initAnimation() {
		// 创建一个ScaleAnimation对象（以某个点为中心缩放）
		scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);// 初始化
		scaleAnimation.setDuration(300);// 设置动画时间
		//变小
		scaleSmallAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleSmallAnimation.setDuration(300);// 设置动画时间
		//变大
		scaleBigAnimation =  new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleBigAnimation.setDuration(300);
		scaleBigAnimation.setStartOffset(600);
		

		alphaAnimation = new AlphaAnimation(1, 0.5f);
		alphaAnimation.setDuration(600);
		alphaAnimation.setStartOffset(900);

		rotateUpAnimation = new RotateAnimation(0, 90,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateUpAnimation.setDuration(300);
		rotateUpAnimation.setStartOffset(300);

		rotateDownAnimation = new RotateAnimation(0, -90,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateDownAnimation.setStartOffset(300);
		rotateDownAnimation.setDuration(300);

		rotateLeftAnimation = new RotateAnimation(0, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateLeftAnimation.setStartOffset(300);
		rotateLeftAnimation.setDuration(300);

		rotateRightAnimation = new RotateAnimation(0, 180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateRightAnimation.setStartOffset(300);
		rotateRightAnimation.setDuration(300);
		// up
		animationSetUp = new AnimationSet(true);
		animationSetUp.addAnimation(scaleSmallAnimation);
		animationSetUp.addAnimation(rotateUpAnimation);
		animationSetUp.addAnimation(scaleAnimation);
		animationSetUp.addAnimation(alphaAnimation);
		// down
		animationSetDown = new AnimationSet(true);
		animationSetDown.addAnimation(scaleSmallAnimation);
		animationSetDown.addAnimation(rotateDownAnimation);
		animationSetDown.addAnimation(scaleAnimation);
		animationSetDown.addAnimation(alphaAnimation);
		// left
		animationSetLeft = new AnimationSet(true);
		animationSetLeft.addAnimation(scaleSmallAnimation);
		animationSetLeft.addAnimation(rotateLeftAnimation);
		animationSetLeft.addAnimation(scaleAnimation);
		animationSetLeft.addAnimation(alphaAnimation);
		// right
		animationSetRight = new AnimationSet(true);
		animationSetRight.addAnimation(scaleSmallAnimation);
		animationSetRight.addAnimation(rotateRightAnimation);
		animationSetRight.addAnimation(scaleAnimation);
		animationSetRight.addAnimation(alphaAnimation);

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

	public void performScaleAnimation(View view, int animationType) {

		Toast.makeText(mContext, "animation", Toast.LENGTH_SHORT).show();
		switch (animationType) {
		case TYPE_SCALE:
			view.startAnimation(scaleAnimation);
			break;
		case TYPE_ROTATE_DOWN:
			view.startAnimation(animationSetDown);
			break;
		case TYPE_ROTATE_LEFT:
			view.startAnimation(animationSetLeft);
			break;
		case TYPE_ROTATE_RIGHT:
			view.startAnimation(animationSetRight);
			break;
		case TYPE_ROTATE_UP:
			view.startAnimation(animationSetUp);
			break;

		default:
			break;
		}
		// view.clearAnimation();

	}
}
