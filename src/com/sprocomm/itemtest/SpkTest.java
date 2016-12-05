package com.sprocomm.itemtest;

import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.AgingTest.R;
import com.sprocomm.utils.PlayMediaUtil;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

public class SpkTest extends TestItem {
	
	private static final String TAG = "SpkTest";
	private SurfaceView mSurfaceView;
	private VideoView videoView;
	private TextView mTextView;
	private View test_view;
	private View settingView;
	private Activity mActivity;
	
	private static final int[] path = new int[] {R.raw.test_music_sweep,R.raw.test_music_1,R.raw.test_music_2};
	private String uri = null;
	private int ipath;
	
	private PlayMediaUtil mPlayMediaUtil = null;
	private Timer timer;
	private PlaySPKTask mPlaySPKTask;
	private int duration;
	
	private final int status_start = 0;
	private final int status_stop = 1;
	private final int status_next = 2;

	MyHandler myHandler;
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case status_start:
				mTextView.setText("SPK test is runing");
				mTextView.setTextSize(30);
				break;
			case status_stop:
			
				break;
			case status_next:
				mTextView.setText("play next");
				mTextView.setTextSize(30);
				break;
			default:
				break;
			}
		}

	};

	
	public SpkTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		
		Log.d("AgingTest", TAG + "---SpkTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;

		mActivity = (Activity) mContext;
		settingView = mActivity.findViewById(R.id.main_view);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		mTextView = (TextView) mActivity.findViewById(R.id.textView);

		settingView.setVisibility(View.GONE);
		test_view.setVisibility(View.VISIBLE);
		mSurfaceView.setVisibility(View.GONE);
		videoView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		
		timer = new Timer();
		myHandler = new MyHandler();
		ipath = 0;
		startSpkPlay();
	}
	
	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---SpkTest stop---");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		stopSpkPlay();
		timer.cancel();
		videoView.setVisibility(View.VISIBLE);
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		mSurfaceView.setVisibility(View.GONE);
	}
	
	private void stopSpkPlay() {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---SpkTest stop play---");
		if (mPlayMediaUtil != null) {
			mPlayMediaUtil.stop();
			mPlayMediaUtil = null;
		}	
	}

	private void startSpkPlay() {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---SpkTest start play---1---");
		RecordThread thread = new RecordThread();
		thread.start();
	
	}

	class RecordThread extends Thread {
		@Override
		public void run() {
			Log.d("AgingTest", TAG + "---SpkTest start play---2---");
			if (isInTest) {
				Test();
			}
		}
		private void Test(){
			PlayMusic();
		}
	}
	
	private void PlayMusic(){
		Log.d("AgingTest", TAG + "---playMusic---ipath--->" + ipath);
		
		uri ="android.resource://" + mContext.getPackageName() + "/" + path[ipath];
		Log.d("AgingTest", TAG + "---playMusic uri--->" + uri);
		if (isInTest) {	
			myHandler.sendEmptyMessage(status_start);
			if (mPlayMediaUtil != null) {
				stopSpkPlay();
			}
			mPlayMediaUtil = new PlayMediaUtil(mContext,Uri.parse(uri));
			mPlayMediaUtil.start();
			duration = mPlayMediaUtil.MusicTime();
			Log.d("AgingTest", TAG + "---playMusic time--->" + duration);
			if (timer != null) {
				if (mPlaySPKTask != null) {
					mPlaySPKTask.cancel(); // 将原任务从队列中移除
				}
			}
			mPlaySPKTask = new PlaySPKTask();
			timer.schedule(mPlaySPKTask,duration);
		}
	}
	
	class PlaySPKTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			myHandler.sendEmptyMessage(status_next);
			Log.d("AgingTest", TAG + "---ipath--->" + ipath +"---path.length--->"+path.length );
			if (ipath < path.length -1) {
				ipath = ipath +1;
				PlayMusic();
			}else {
				ipath = 0;
				PlayMusic();
			}
			
		}
	}
}
