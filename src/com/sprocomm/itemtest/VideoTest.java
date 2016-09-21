package com.sprocomm.itemtest;

import java.io.File;

import com.sprocomm.AgingTest.R;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoTest  extends TestItem{
	
	private static final String TAG = "VideoTest";
	public static final String VIDEO_NAME = "moveTest.mp4";
	public static final String videoPath = new File(Environment.getExternalStorageDirectory(),VIDEO_NAME).getAbsolutePath();
	
	private VideoView videoView;
	private SurfaceView mSurfaceView;
	private TextView mTextView;
	private View test_view;
	private View settingView;
	private Activity mActivity;

	public VideoTest(Context context,long time,Handler handler) {
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		super.startTest();
		Log.d("AgingTest", TAG + "---videoTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		mActivity =  (Activity)mContext;
		settingView = mActivity.findViewById(R.id.setting_view);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);	
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		mTextView = (TextView) mActivity.findViewById(R.id.textView);
		
		settingView.setVisibility(View.GONE);
		test_view.setVisibility(View.VISIBLE);
		mSurfaceView.setVisibility(View.GONE);
		videoView.setVisibility(View.VISIBLE);
		mTextView.setVisibility(View.GONE);
		
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);

        StartPlayVideo();
	}
	

	
	@Override
	public void stopTest(boolean isPass) {
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---videoPlay stop---");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		StopPlayVideo();
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
	}
	

	
	private void StopPlayVideo() {
		// TODO Auto-generated method stub
		if(videoView != null){
			videoView.pause();
			videoView.stopPlayback();
			videoView.invalidate();
		}
	}

	private void StartPlayVideo() {
		// TODO Auto-generated method stub
		String uri = "android.resource://" + mContext.getPackageName() + "/" + R.raw.move_test;
		Log.d("AgingTest", TAG + "---uri---" + uri);
		videoView.setVideoURI(Uri.parse(uri));
		videoView.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				startTest();
			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.d("AgingTest", TAG + "--- onError---");
				startTest();
				return true;
			}
		});
		videoView.requestFocus();
		videoView.start();
	}

	

}
