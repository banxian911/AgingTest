package com.sprocomm.item;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Environment;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.net.Uri;
import com.sprocomm.R;
import com.sprocomm.TestItem;

public class VideoTest  extends TestItem{
	public static final String VIDEO_NAME = "moveTest.mp4";
	public static final String videoPath = new File(Environment.getExternalStorageDirectory(),VIDEO_NAME).getAbsolutePath();
	
	VideoView videoView;
	SurfaceView surfaceView;
	View test_view;
	View view1;
	View view2;
	Activity mActivity;

	public VideoTest(Context context,long time,Handler handler) {
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		super.startTest();
		System.out.println("XIONG ----"+this.getClass().getName()+"-----start----");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		mActivity =  (Activity)mContext;
		
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		surfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);
		surfaceView.setVisibility(View.GONE);
		test_view = mActivity.findViewById(R.id.test_view);
		test_view.setVisibility(View.VISIBLE);
//		view1 = mActivity.findViewById(R.id.view1);
		view2 = mActivity.findViewById(R.id.view2);
		
//		view1.setVisibility(View.GONE);
		view2.setVisibility(View.GONE);
		
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);

		//videoView.setVideoPath(videoPath);
		String uri = "android.resource://" + mContext.getPackageName() + "/" + R.raw.move_test;
        videoView.setVideoURI(Uri.parse(uri));
		videoView.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				startTest();
			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				System.out.println("XIONG ------on error of play-----");
				startTest();
				return true;
			}
		});
		videoView.requestFocus();
		videoView.start();
	}
	

	@Override
	public void stopTest(boolean isPass) {
		super.stopTest(isPass);
		System.out.println("XIONG ----"+this.getClass().getName()+"-----stop----");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
//		view1.setVisibility(View.VISIBLE);
		view2.setVisibility(View.VISIBLE);
		
		if(videoView != null){
			videoView.pause();
			videoView.stopPlayback();
			videoView.invalidate();
		}
		test_view.setVisibility(View.GONE);
	}

}
