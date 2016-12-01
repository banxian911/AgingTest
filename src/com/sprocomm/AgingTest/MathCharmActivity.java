package com.sprocomm.AgingTest;

import com.sprocomm.UIUtils.CJJArtLineView;
import com.sprocomm.utils.CpuInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MathCharmActivity extends Activity {

	private static String TAG = "MathCharmActivity";
	private CJJArtLineView mArtLine;
	private TextView testType;
	
	private static final int[] path = new int[] {R.raw.test_music_sweep,R.raw.test_music_1,R.raw.test_music_2};
	private String uri = null;
	
	
	public static MathCharmActivity instance = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("AgingTest", TAG + "---MathCharmActivity start---");
		setContentView(R.layout.activity_math_charm);

		instance = this;

		mArtLine = (CJJArtLineView) findViewById(R.id.art);
		testType = (TextView)findViewById(R.id.test_type);
		
		Intent mIntent = new Intent();
		int flag = mIntent.getIntExtra("mathcharm", 0);
		Log.d("AgingTest", TAG + "---flag---" + flag);
 		startCharm(flag);

	}

	private void startCharm(int flag){
		startText(flag);
		switch (flag) {
		case 0:
			line1();
			break;
		case 1:
			line2();
			break;

		default:
			break;
		}
	}
	
	private void startText(int flag){
		switch (flag) {
		case 0:
			testType.setText("cpu--->");
			break;
		case 1:
			break;

		default:
			break;
		}
	}
	
	private void AudioTestStart(){
		uri ="android.resource://" + getPackageName() + "/" + R.raw.test_music_1;
		Log.d("AgingTest", TAG + "---playMusic uri--->" + uri);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mArtLine.destory();
	}

	public void line1() {
		mArtLine.setColor("#90ff00ff");
		mArtLine.setTime(40 * 1000);
		mArtLine.setSpeedA((float) 0.76);
		mArtLine.setAngleB((float) 0.03);
		mArtLine.setaXR(200);
		mArtLine.setaYR(200);
		mArtLine.setbXR(300);
		mArtLine.setbYR(300);
	}

	public void line2() {
		mArtLine.setColor("#904CAF50");
		mArtLine.setTime(20 * 1000);
		mArtLine.setSpeedA((float) 0.12);
		mArtLine.setAngleB((float) 0.25);
		mArtLine.setaXR(300);
		mArtLine.setaYR(120);
		mArtLine.setbXR(120);
		mArtLine.setbYR(220);
	}

}
