package com.sprocomm.NewItem;

import com.sprocomm.MathCharmActivity;
import com.sprocomm.R;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

public class VibrationTest extends TestItem {
	
	private static final String TAG = "CpuTest";
	private SurfaceView mSurfaceView;
	private VideoView videoView;
	private TextView mTextView;
	private View test_view;
	private View mainView;
	private Activity mActivity;
	
	public VibrationTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---CpuTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;

		//initUI();
		Intent mIntent = new Intent(mContext,MathCharmActivity.class);
		mIntent.putExtra("mathcharm", 0);
		mContext.startActivity(mIntent);
		
	}

	
	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---CpuTest stop---");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		MathCharmActivity.instance.finish();
	}
}
