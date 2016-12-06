package com.sprocomm.AgingTest;

import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.utils.VideoRecorderUtil;

import android.app.Activity;
import android.content.Intent;
//import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.widget.Chronometer;
import android.view.SurfaceView;
import android.view.Window;

public class VideoRecroder extends Activity{

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
	private int startVrTime = 2*1000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("AgingTest", TAG + "---start VideoRecroder--->");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video_recroder);

		instance = this;

		timer = new Timer();
		mRecorder = new VideoRecorderUtil();
		
		mSurfaceView = (SurfaceView) findViewById(R.id.video_recorder);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this.SurfaceHolderCallback);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (timer != null) {
			mstartVrTask = new startVrTask();
			timer.schedule(mstartVrTask, startVrTime);
		}
		runTime = (Chronometer) findViewById(R.id.run_time_vr);
		runTime.start();
		runTime.setFormat("测试时间：%S");
			
	}

	
	
	private void startRecroder() {
		
		if (mRecorder != null) {
			mRecorder.startRecording(mSurfaceView);
		//	mRecorder.isRecording = true;
		}
	}

	private void stopRecroder() {
		Log.i("AgingTest", TAG + "---stop VideoRecroder--->");
		if (mRecorder != null) {
			try {
				mRecorder.stopRecording();
				mRecorder.release();
				mRecorder = null;
				/*
				 * if (camera != null) { camera.release(); camera = null; }
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	class startVrTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startRecroder();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		stopRecroder();
		
		runTime.stop();
		runTime.setBase(SystemClock.elapsedRealtime());// 复位
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

			if (mRecorder != null) {
				mRecorder.release(); // Now the object cannot be reused
				mRecorder = null;
				Log.d(TAG, "surfaceDestroyed release mRecorder");
			}
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
