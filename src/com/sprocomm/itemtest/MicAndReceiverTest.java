package com.sprocomm.itemtest;

import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.R;
import com.sprocomm.utils.ReceiverUtil;
import com.sprocomm.utils.TestItem;
import com.sprocomm.utils.PlayMediaUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MicAndReceiverTest extends TestItem {
	
	private static final String TAG = "MicAndReceiverTest";
	private SurfaceView mSurfaceView;
	private VideoView videoView;
	private TextView mTextView;
	private View test_view;
	private View settingView;
	private Activity mActivity;
	
	private String path = null;
	private ReceiverUtil mReceiverUtil;
	private PlayMediaUtil mPlayReverUtil;
	
	private Boolean isReceiver = false; 
	
	private Timer timer;
	private Receivetask myReceivetask;
	private Playtest myPlaytest;
	private int time = 30 * 1000;//录音和播放时间均为30秒
	
	final int status_recoding = 0;
    final int status_stop = 1;
    final int pass_cycle = 111;
    MyHandler myHandler;
	class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case status_recoding:
               //     tv_status.setText(R.string.record_status_recording);
                	Toast.makeText(mContext, "开始录音", Toast.LENGTH_LONG).show();
                	mTextView.setText(R.string.startReveiver);
                //	mPlayReverUtil.stop();
                    break;
                case status_stop:
                	StartPlayRever();
                    break;
                case pass_cycle:
                	
                    break;
                default:
                    break;
            }
        }

    };
	
	
	public MicAndReceiverTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---MicAndReceiverTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		mActivity =  (Activity)mContext;
		//mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		settingView = mActivity.findViewById(R.id.setting_view);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);	
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		mTextView = (TextView) mActivity.findViewById(R.id.textView);
		
		settingView.setVisibility(View.GONE);
		test_view.setVisibility(View.VISIBLE);
		mSurfaceView.setVisibility(View.GONE);
		videoView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		
		mTextView.setTextSize(30);
		
		
		timer = new Timer();
		
		path = Environment.getExternalStorageDirectory().getAbsolutePath();    
        path += "/Agingtest.amr";    
        myHandler = new MyHandler();
        mReceiverUtil = new ReceiverUtil(path);  
       // mPlayReverUtil = new PlayReverUtil(path);  
        isReceiver = true;
		startMicReceiver();
        
	}
	
	

	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---MicAndReceiverTest stop---");
		
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		timer.cancel();
		stopMicReceiver();
		videoView.setVisibility(View.VISIBLE);
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		mSurfaceView.setVisibility(View.GONE);
	}
	
	private void stopMicReceiver() {
		// TODO Auto-generated method stub
		if (mReceiverUtil !=null || mPlayReverUtil !=null) {
			mReceiverUtil.stop();
			mPlayReverUtil.stop();
			mReceiverUtil = null;
			mPlayReverUtil = null;
		}
		isReceiver =false;
	}

	private void startMicReceiver() {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---startMicReceiver() start---");	
			 RecordThread thread = new RecordThread();
		        thread.start();	
	}
	
	class RecordThread extends Thread {
        @Override
        public void run() {
        	if (isReceiver) {
        		test();
			}
            
        }
        private void test() {
        	
        	myHandler.sendEmptyMessage(status_recoding);
        	mReceiverUtil.start();
        	if (timer != null){
        	      if (myReceivetask != null){
        	    	  myReceivetask.cancel();  //将原任务从队列中移除
        	      }
        	}
        //	timer = new Timer();
        	myReceivetask = new Receivetask();
            timer.schedule(myReceivetask, time);
        }
	}
	
	class Receivetask extends TimerTask{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            
         mReceiverUtil.stop();
         myHandler.sendEmptyMessage(status_stop);
         //mHandler.sendEmptyMessage(pass_cycle);
        }
    }
    
    public void StartPlayRever() {
		// TODO Auto-generated method stub
    	Toast.makeText(mContext, "开始播放", Toast.LENGTH_LONG).show();
    	mTextView.setText(R.string.startPlayRe);
    	
    	 
        mPlayReverUtil = new PlayMediaUtil(path);  
    	mPlayReverUtil.start();
    	Log.d("AgingTest", TAG + "---play start---> ");
    	if (timer != null){
  	      if (myPlaytest != null){
  	    	  myPlaytest.cancel();  //将原任务从队列中移除
  	      }
    	}
    	myPlaytest = new Playtest();
        timer.schedule(myPlaytest, time);
	}
    
    class Playtest extends TimerTask{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (isReceiver) {
				mPlayReverUtil.stop();
				startMicReceiver();
			}
		}
	}

}
