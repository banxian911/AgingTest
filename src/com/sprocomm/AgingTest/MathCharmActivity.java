package com.sprocomm.AgingTest;

import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.UIUtils.WJMagicCurveView;
import com.sprocomm.utils.CpuInfo;
import com.sprocomm.utils.PlayMediaUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Chronometer;
import android.widget.TextView;

public class MathCharmActivity extends Activity{

	private static String TAG = "MathCharmActivity";
	private WJMagicCurveView wjMagicCurveView;
	private TextView testType;
	private Timer timer;
	private MathCharmTask mCharmTask;
	private int duration = 10 * 1000;
	private static int flag;

	private Chronometer runTime;
	
	private String uri = null;
	private PlayMediaUtil mPlayMediaUtil = null;

	public static MathCharmActivity instance = null;

	private static String stopTestBR = "com.sprocomm.AgingTest.StopTestBR";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("AgingTest", TAG + "---MathCharmActivity start---");
		setContentView(R.layout.activity_math_charm);

		instance = this;

		wjMagicCurveView = (WJMagicCurveView) findViewById(R.id.art);
		testType = (TextView) findViewById(R.id.test_type);
		runTime = (Chronometer)findViewById(R.id.run_time);
		
		timer = new Timer();
		Intent mIntent = getIntent();
		flag = mIntent.getIntExtra("mathcharm", 0);
		Log.d("AgingTest", TAG + "---flag---" + flag);
		
		startTest(flag);
		startTransform(flag);
		
		runTime.start();
		runTime.setFormat("测试时间：%S");
	}
	
	private void startTest(int flag){
		Log.d("AgingTest", TAG + "---startTest---flag--->" + flag);
		switch (flag) {
		case 0:
			testType.setText(cpuinfo()+"\n\n"+ "Test the CPU......");
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
		default:
			break;
		}

		if (timer != null) {
			mCharmTask = new MathCharmTask();
			timer.schedule(mCharmTask, duration);
		}
	}

	
	private String cpuinfo(){
		
		String infoStr = CpuInfo.getCpuName();
		int cpunum = CpuInfo.getNumCores();
		String cpuFre = CpuInfo.getMaxCpuFreq();
		//int freInt =Integer.parseInt(cpuFre);
		float freflo = Float.parseFloat(cpuFre)/ 1000000;
		Log.d("AgingTest", TAG + "---infoStr--->" + infoStr 
				+ "--cpuNum-->"+cpunum
				+ "--cpufre-->"+cpuFre);
		String cpuInfo = "CPU info：\n"
				+"CPU Processor:"+infoStr + "\n"
				+ "CPU core："+cpunum + "\n" 
				+ "CPU Maxfrequency："+freflo+"GHz";
		return cpuInfo;
	}

	private void AudioTestStart() {
		if (mPlayMediaUtil != null) {
			AudioTestStop();
		}
		uri = "android.resource://" + getPackageName() + "/" + R.raw.test_music_2;
		Log.d("AgingTest", TAG + "---playMusic uri--->" + uri);
		mPlayMediaUtil = new PlayMediaUtil(this, Uri.parse(uri));
		mPlayMediaUtil.start();
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
		AudioTestStop();
		runTime.stop();
		runTime.setBase(SystemClock.elapsedRealtime());// 复位
	}

	public void line0() {
		wjMagicCurveView.setPaintColor(Color.BLUE);
		wjMagicCurveView.setRadius(-1, -1, -1, -1)
		.setDurationSec(-1)
		.setLoopTotalCount(-1)
		.setSpeed(-1, -1)
		.startDraw();
	}
	
	public void line1() {
		wjMagicCurveView.setRadius(300, 1, 300, 150)
		.setDurationSec(100)
		.setLoopTotalCount(-1)
		.setSpeed(60, 28)
		.startDraw();
	}
	
	public void line2() {
		wjMagicCurveView.setPaintColor(Color.GREEN);
		wjMagicCurveView.setRadius(300, 20, 150, 150)
		.setDurationSec(50)
		.setLoopTotalCount(-1)
		.setSpeed(-1, -1)
		.startDraw();
	}
	
	public void line3() {
		wjMagicCurveView.setPaintColor(Color.LTGRAY);
		wjMagicCurveView.setRadius(320, 320, 280, 280)
		.setDurationSec(-1)
		.setLoopTotalCount(-1)
		.setSpeed(-1, -1)
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
					startTransform(flag);
					
				}
			});
			
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent mIntent = new Intent(stopTestBR);
			this.sendBroadcast(mIntent);}
			return super.onKeyDown(keyCode, event);
	/*		return false;
		} else {
			return super.onKeyDown(keyCode, event);*/
			
	} 
	

}
