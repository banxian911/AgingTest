package com.sprocomm.NewItem;

import com.sprocomm.AgingTest.MathCharmActivity;
import com.sprocomm.AgingTest.R;
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

public class CpuTest extends TestItem {
	
	private static final String TAG = "CpuTest";
	private SurfaceView mSurfaceView;
	private VideoView videoView;
	private TextView mTextView;
	private View test_view;
	private View mainView;
	private Activity mActivity;
	
	public CpuTest(Context context,long time,Handler handler){
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
	
	private void initUI(){
		mActivity = (Activity) mContext;
		mainView = mActivity.findViewById(R.id.main_view);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		mTextView = (TextView) mActivity.findViewById(R.id.textView);

		mainView.setVisibility(View.GONE);
		test_view.setVisibility(View.VISIBLE);
		mSurfaceView.setVisibility(View.GONE);
		videoView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
	}
}
