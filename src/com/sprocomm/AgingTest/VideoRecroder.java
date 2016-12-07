package com.sprocomm.AgingTest;

import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.utils.VideoRecorderUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Camera;
import android.graphics.PixelFormat;
//import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.widget.Chronometer;
import android.widget.Toast;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class VideoRecroder extends Activity {

	private static final String TAG = "VideoRecroder";
	private static String stopTestBR = "com.sprocomm.AgingTest.StopTestBR";
	public static VideoRecroder instance = null;
	// private Camera camera;
	private VideoRecorderUtil mRecorder;
	private SurfaceHolder mSurfaceHolder;
	private SurfaceView mSurfaceView;
	private Chronometer runTime;
	private Timer timer;
	private startVrTask mstartVrTask;
	private int startVrTime = 2 * 1000;

	private static final int FRONT_CAMERA = 1;
	private static final int BACK_CAMERA = 0;
//	private int cameraID;
	private changerBCIdTesk mBCTesk;
	private changerFCIdTesk mFCTesk;
	private int changerTime = 30 * 60 * 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("AgingTest", TAG + "---start VideoRecroder--->");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		setContentView(R.layout.activity_video_recroder);

		instance = this;

		timer = new Timer();
		// myHandler = new MyHandler();

		mSurfaceView = (SurfaceView) findViewById(R.id.video_recorder);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this.SurfaceHolderCallback);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (timer != null) {
			mstartVrTask = new startVrTask();
			timer.schedule(mstartVrTask, startVrTime);
		}
		runTime = (Chronometer) findViewById(R.id.run_time_vr);
		// myHandler.sendEmptyMessageDelayed(status_start,startVrTime);
		runTime.start();
		runTime.setFormat("测试时间：%S");
	}

	private void startBackCRecroder() {

		mRecorder = new VideoRecorderUtil();
		Log.i("AgingTest", TAG + "---startBackCRecroder-->");
		if (mRecorder != null) {
			mRecorder.startRecording(mSurfaceView, BACK_CAMERA);
	
			if (timer != null) {
				if (mBCTesk != null) {
					mBCTesk.cancel();
				}
				mBCTesk = new changerBCIdTesk();
				timer.schedule(mBCTesk, changerTime);
			}
		}

	}

	private void startFrontCRecroder() {

		mRecorder = new VideoRecorderUtil();
		Log.i("AgingTest", TAG + "---startFrontCRecroder-->");
		if (mRecorder != null) {
			mRecorder.startRecording(mSurfaceView, FRONT_CAMERA);
		}

		if (timer != null) {
			if (mFCTesk != null) {
				mFCTesk.cancel();
			}
			mFCTesk = new changerFCIdTesk();
			timer.schedule(mFCTesk, changerTime);
		}
	}

	private void stopRecroder() {
		Log.i("AgingTest", TAG + "---stop VideoRecroder---mRecorder-->" + mRecorder);
		if (mRecorder != null) {
			mRecorder.stopRecording();
			mRecorder.release();
			mRecorder = null;
		}

	}

	class changerFCIdTesk extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			stopRecroder();
			startBackCRecroder();
		}

	}

	class changerBCIdTesk extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			stopRecroder();
			startFrontCRecroder();
		}

	}

	class startVrTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startBackCRecroder();
			mstartVrTask.cancel();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		stopRecroder();
		runTime.stop();
		runTime.setBase(SystemClock.elapsedRealtime());
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

	SurfaceHolder.Callback SurfaceHolderCallback = new Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mSurfaceView = null;
			mSurfaceHolder = null;

			/*
			 * if (mRecorder != null) { mRecorder.release(); // Now the object
			 * cannot be reused mRecorder = null; Log.d("AgingTest", TAG
			 * +"---surfaceDestroyed release mRecorder"); }
			 */
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mSurfaceHolder = holder;
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// TODO Auto-generated method stub
			mSurfaceHolder = holder;
		}
	};

}
