package com.sprocomm.itemtest;

import com.sprocomm.R;
import com.sprocomm.utils.ApkRunInstallUtil;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Play3DTest extends TestItem {
	
	private static final String TAG = "Play3DTest";
	private SurfaceView mSurfaceView;
	private VideoView videoView;
	private TextView mTextView;
	private View test_view;
	private View settingView;
	private Activity mActivity;
	 
	public static final String APP_PACKAGE_NAME = "com.example.android.opengl";//包名
	
	public Play3DTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---Play3DTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		mActivity =  (Activity)mContext;
		settingView = mActivity.findViewById(R.id.setting_view);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);	
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		mTextView = (TextView) mActivity.findViewById(R.id.textView);
		
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		mSurfaceView.setVisibility(View.GONE);
		videoView.setVisibility(View.GONE);
		mTextView.setVisibility(View.GONE);
		
	/*	mGLView = new MyGLSurfaceView(mActivity);
		LinearLayout mLayout = (LinearLayout) mActivity.findViewById(R.id.test_Line);
		mLayout.addView(mGLView);
		mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		*/
		startPlay3D();
	}
	
	
	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---Play3DTest stop---");
		
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		stopPlay3D();
		videoView.setVisibility(View.GONE);
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		mSurfaceView.setVisibility(View.GONE);
	}
	
	private void stopPlay3D() {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---Play3DTest stop--1-");
		Log.d("AgingTest", TAG + "---ApkRunInstallUtil.isRunning---" + ApkRunInstallUtil.isRunning(mContext, APP_PACKAGE_NAME));
	//	if (ApkRunInstallUtil.isRunning(mContext, APP_PACKAGE_NAME)) {
			try {
				Log.d("AgingTest", TAG + "---Play3DTest stop--2-");
				ApkRunInstallUtil.forceStopPackage(mContext, APP_PACKAGE_NAME);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("AgingTest", TAG + "---Play3DTest stop fail ---");
				e.printStackTrace();
			}
		//} 
	}

	private void startPlay3D() {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---Play3DTest start--1-");
		if (ApkRunInstallUtil.isAppInstalled(mContext, APP_PACKAGE_NAME)) {
			ApkRunInstallUtil.RunApp(mContext, APP_PACKAGE_NAME);
		} else {
			Toast.makeText(mContext, "请安装Play3D", Toast.LENGTH_LONG).show();
			//stopTest(false);
		}

	}

}
