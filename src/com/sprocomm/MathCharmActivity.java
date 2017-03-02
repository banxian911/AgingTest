package com.sprocomm;

import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.UIUtils.WJMagicCurveView;
import com.sprocomm.utils.CpuInfo;
import com.sprocomm.utils.PlayMediaUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

public class MathCharmActivity extends Activity {

	private static String TAG = "MathCharmActivity";
	private WJMagicCurveView wjMagicCurveView;
	private TextView testType;
	private Timer timer;
	private MathCharmTask mCharmTask;
	private int duration = 30 * 1000;
	private static int flag;
	
	private AudioTask mAudioTask;

	private ImageView lcdView;
	private int[] lcdBgColor = { Color.BLUE, Color.GREEN, Color.RED, Color.WHITE, Color.BLACK, Color.YELLOW };
	private int lcdBg = 0;
	private LcdTask mLcdTask;
	private int lcdTime = 1 * 1000;

	private Chronometer runTime;

	private String uri = null;
	private PlayMediaUtil mPlayMediaUtil = null;

	public static MathCharmActivity instance = null;

	private static String stopTestBR = "com.sprocomm.AgingTest.StopTestBR";

	private Vibrator vibrator = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("AgingTest", TAG + "---MathCharmActivity start---");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_math_charm);

		instance = this;

		wjMagicCurveView = (WJMagicCurveView) findViewById(R.id.art);
		testType = (TextView) findViewById(R.id.test_type);
		lcdView = (ImageView) findViewById(R.id.lcdView);
		runTime = (Chronometer) findViewById(R.id.run_time);

		timer = new Timer();
		Intent mIntent = getIntent();
		flag = mIntent.getIntExtra("mathcharm", 0);
		Log.d("AgingTest", TAG + "---flag---" + flag);

		//startTest(flag);
		//startTransform(flag);
		startAnminal(flag);
		runTime.start();
		runTime.setFormat("测试时间：%S");
	}

	private void startTest(int flag) {
		Log.d("AgingTest", TAG + "---startTest---flag--->" + flag);
		switch (flag) {
		case 0:
			testType.setText("Test the Vibtrate......");
			startVibrator();
			break;
		case 1:
			testType.setText("AudioTest is Running");
			AudioTestStart();
			break;
		case 2:
			testType.setText("2DTest is Running");
			break;
		case 3:
			testType.setText("S3Test is Running");
			break;
		case 4:
			testType.setText("EMMC is Running");
			break;
		case 5:
			testType.setText("MemoryTest is Running");
			break;
		case 6:
			testType.setText("LCDTest is Running");	
			LcdTest();
			break;
		default:
			break;
		}
	}

	private void startTransform(int flag) {
		Log.d("AgingTest", TAG + "---startTransform---flag--->" + flag);
		wjMagicCurveView.stopDraw();
		switch (flag) {
		case 0:
			line0();
			break;
		case 1:
			line1();
			break;
		case 2:
			line2();
			break;
		case 3:
			line3();
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		default:
			break;
		}
		Log.d("AgingTest", TAG + "---timer--a->" + timer);
		if (timer != null) {
			if (mCharmTask != null) {
				mCharmTask.cancel();
			}
			mCharmTask = new MathCharmTask();
			timer.schedule(mCharmTask, duration);
		}
	}

	private void startAnminal(int flag){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		Log.d("AgingTest", TAG + "---height--->" + height + "---width-->" + width );
		if ( (height <= 320 || width <= 240) && flag != 6) {
			Test2DAnminal();
		} else {
			startTransform(flag);
		}
		startTest(flag);
	}
	
	private void Test2DAnminal(){
		wjMagicCurveView.setVisibility(View.GONE);
		lcdView.setVisibility(View.VISIBLE);
		lcdView.setImageResource(R.drawable.ic_launcher);
		/*ObjectAnimator mAnimator = ObjectAnimator.ofFloat(lcdView, "scaleY", 0,8,1);
		mAnimator.setDuration(4000);
		mAnimator.start();*/
		RotateAnimation rotateAnim = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);  
		rotateAnim.setRepeatCount(10);
		AlphaAnimation alphaAnim = new AlphaAnimation(0.1f,1.0f);
		alphaAnim.setRepeatCount(10);
		ScaleAnimation scaleAnim = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		scaleAnim.setRepeatCount(10);
		AnimationSet setAnim=new AnimationSet(true);  
		setAnim.addAnimation(alphaAnim);  
		setAnim.addAnimation(scaleAnim);  
		setAnim.addAnimation(rotateAnim);  
		  
		setAnim.setDuration(3000);  
		setAnim.setFillAfter(true);
		//setAnim.setRepeatCount(10);
		setAnim.setRepeatMode(Animation.REVERSE);
		lcdView.startAnimation(setAnim);
		
		Log.d("AgingTest", TAG + "---timer--->" + timer);
		if (timer != null) {
			if (mCharmTask != null) {
				mCharmTask.cancel();
			}
			mCharmTask = new MathCharmTask();
			timer.schedule(mCharmTask, duration);
		}
	}
	
	private void LcdTest() {
		
		wjMagicCurveView.setVisibility(View.GONE);
		lcdView.setVisibility(View.VISIBLE);
		if (lcdBg >= lcdBgColor.length) {
			lcdBg = 0;
		}
		lcdView.setBackgroundColor(lcdBgColor[lcdBg]);
		lcdBg++;
		if (timer != null) {
			if (mCharmTask != null) {
				mCharmTask.cancel();
			}
			if (mLcdTask != null) {
				mLcdTask.cancel();
			}
			mLcdTask = new LcdTask();
			timer.schedule(mLcdTask, lcdTime);
		}

	}
	
	private void startVibrator() {
		if (vibrator != null){
			stopVibrator();
		}
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 100, 10, 100, 1000 }, 0);
	}

	private void stopVibrator() {
		if (vibrator != null){
			vibrator.cancel();
			vibrator = null;
		}
	}

	private String cpuinfo() {

		String infoStr = CpuInfo.getCpuName();
		int cpunum = CpuInfo.getNumCores();
		String cpuFre = CpuInfo.getMaxCpuFreq();
		// int freInt =Integer.parseInt(cpuFre);
		float freflo = Float.parseFloat(cpuFre) / 1000000;
		Log.d("AgingTest", TAG + "---infoStr--->" + infoStr + "--cpuNum-->" + cpunum + "--cpufre-->" + cpuFre);
		String cpuInfo = "CPU info：\n" + "CPU Processor:" + infoStr + "\n" + "CPU core：" + cpunum + "\n"
				+ "CPU Maxfrequency：" + freflo + "GHz";
		return cpuInfo;
	}

	private void AudioTestStart() {
		if (mPlayMediaUtil != null) {
			AudioTestStop();
		}
		uri = "android.resource://" + getPackageName() + "/" + R.raw.test_music;
		Log.d("AgingTest", TAG + "---playMusic uri--->" + uri);
		mPlayMediaUtil = new PlayMediaUtil(this, Uri.parse(uri));
		mPlayMediaUtil.start();
		
		int musictime = mPlayMediaUtil.MusicTime();
		if (timer != null) {
			if (mAudioTask != null) {
				mAudioTask.cancel();
			}
			mAudioTask = new AudioTask();
			timer.schedule(mAudioTask, musictime);
		}
		
	}

	private void AudioTestStop() {
		Log.d("AgingTest", TAG + "---stop play---");
		if (mPlayMediaUtil != null) {
			mPlayMediaUtil.stop();
			mPlayMediaUtil = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		wjMagicCurveView.destory();
		stopVibrator();
		AudioTestStop();
		runTime.stop();
		runTime.setBase(SystemClock.elapsedRealtime());// 复位
	}

	public void line0() {
		wjMagicCurveView.setPaintColor(Color.BLUE);
		wjMagicCurveView.setRadius(-1, -1, -1, -1).setDurationSec(-1).setLoopTotalCount(-1).setSpeed(-1, -1)
				.startDraw();
	}

	public void line1() {
		wjMagicCurveView.setRadius(300, 1, 300, 150).setDurationSec(100).setLoopTotalCount(-1).setSpeed(60, 28)
				.startDraw();
	}

	public void line2() {
		wjMagicCurveView.setPaintColor(Color.GREEN);
		wjMagicCurveView.setRadius(300, 20, 150, 150).setDurationSec(50).setLoopTotalCount(-1).setSpeed(-1, -1)
				.startDraw();
	}

	public void line3() {
		wjMagicCurveView.setPaintColor(Color.LTGRAY);
		wjMagicCurveView.setRadius(320, 320, 280, 280).setDurationSec(-1).setLoopTotalCount(-1).setSpeed(-1, -1)
				.startDraw();
	}

	class MathCharmTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("AgingTest", TAG + "---MathCharmTask---flag--->" + flag);

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					//startTransform(flag);
					startAnminal(flag);
				}
			});

		}
	}

	class AudioTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			AudioTestStart();
		}
		
	}
	
	class LcdTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d("AgingTest", TAG + "---LcdTask--->");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					LcdTest();
				}
			});

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent mIntent = new Intent(stopTestBR);
			this.sendBroadcast(mIntent);
		}
		return super.onKeyDown(keyCode, event);
	}

}