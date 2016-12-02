package com.sprocomm.AgingTest;

import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.UIUtils.WJMagicCurveView;
import com.sprocomm.utils.PlayMediaUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MathCharmActivity extends Activity {

	private static String TAG = "MathCharmActivity";
	private WJMagicCurveView wjMagicCurveView;
	private TextView testType;
	private Timer timer;
	private MathCharmTask mCharmTask;
	private int duration = 10 * 1000;
	private static int flag;

	private String uri = null;
	private PlayMediaUtil mPlayMediaUtil = null;

	public static MathCharmActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("AgingTest", TAG + "---MathCharmActivity start---");
		setContentView(R.layout.activity_math_charm);

		instance = this;

		wjMagicCurveView = (WJMagicCurveView) findViewById(R.id.art);
		testType = (TextView) findViewById(R.id.test_type);

		timer = new Timer();
		Intent mIntent = getIntent();
		flag = mIntent.getIntExtra("mathcharm", 0);
		Log.d("AgingTest", TAG + "---flag---" + flag);
		startTransform(flag);
	}

	private void startTransform(int flag) {
		Log.d("AgingTest", TAG + "---startTransform---flag--->" + flag);
		startText(flag);
		wjMagicCurveView.stopDraw();
		switch (flag) {
		case 0:
			line0();
			break;
		case 1:
			line1();
			AudioTestStart();
			break;
		case 2:
			line2();
			break;
		case 3:

			break;

		default:
			break;
		}

		if (timer != null) {
			mCharmTask = new MathCharmTask();
			timer.schedule(mCharmTask, duration);
		}
	}

	private void startText(int flag) {
		switch (flag) {
		case 0:
			testType.setText("cpuTest--->start");
			break;
		case 1:
			testType.setText("AudioTest--->start");
			break;
		case 2:
			testType.setText("2DTest--->start");
			break;
		default:
			break;
		}
	}

	private void AudioTestStart() {
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
		wjMagicCurveView.setRadius(300, 20, 150, 150)
		.setDurationSec(50)
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

}
