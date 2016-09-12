package com.sprocomm.itemtest;

import com.sprocomm.R;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.VideoView;

public class LcdAndVibrateTest extends TestItem implements Callback,Runnable{
	
	private static final String TAG = "LcdAndVibrateTest";
	
	private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
	
	private VideoView videoView;
	private View test_view;
	private View settingView;
	private Activity mActivity;

	private Vibrator vibrator = null;
	
	/** 
     * 与SurfaceHolder绑定的Canvas 
     */  
    private Canvas mCanvas;  
   
    /** 
     * 线程的控制开关 
     */  
    private boolean isRunning;  
    private Handler mUiHandler = new Handler();;
    private Runnable mRunnable;
	
	int mIndex = 0;
	private static final int[] COLOR_ARRAY = new int[] {
            Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE};
   private static final int TIMES = 4;
   
	public LcdAndVibrateTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---Lcd_Vibrate start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		mActivity =  (Activity)mContext;
		//mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		settingView = mActivity.findViewById(R.id.setting_view);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);	
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		
		settingView.setVisibility(View.GONE);
		test_view.setVisibility(View.VISIBLE);
		mSurfaceView.setVisibility(View.VISIBLE);
		videoView.setVisibility(View.GONE);
		
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
	           
		startVibrator();
	}

	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---Lcd_Vibrate stop---");
		
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		stopVibrator();
		videoView.setVisibility(View.VISIBLE);
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		mSurfaceView.setVisibility(View.GONE);
	}
	
	private void startVibrator(){
		vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{1000,1000,1000,1000}, 0);
	}
	private void stopVibrator(){
		if(vibrator != null)vibrator.cancel();
		vibrator = null;
	}
	
	private void startLcdTest(int color) {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---Lcd_Vibrate color---" + color);
		Canvas canvas = mSurfaceHolder.lockCanvas();
		//canvas.drawColor();
		if (canvas !=null) {
			canvas.drawColor(color);
		}
		if(canvas!= null){//如果当前画布不为空						
			mSurfaceHolder.unlockCanvasAndPost(canvas);//解锁画布
		}
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		// 开启线程  
        isRunning = true; 
		new Thread(this).start();
	
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		 // 通知关闭线程  
        isRunning = false; 
	}

	private int currentAlpha=0;  //当前的不透明值
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			/*Log.d("AgingTest", TAG + "---Lcd_Vibrate mIndex---" + mIndex);
			startLcdTest(COLOR_ARRAY[mIndex]);
			mIndex++;
			Log.d("AgingTest", TAG + "---Lcd_Vibrate  COLOR_ARRAY.length---" +  COLOR_ARRAY.length);
			if (mIndex >= COLOR_ARRAY.length) {
				startLcdTest(COLOR_ARRAY[mIndex]);
				mIndex =0;
			}*/
			for (int i = 255; i>-10;i=i-10) {
				if(i<0) i=0;//如果当前不透明度小于零,将不透明度置为零
				currentAlpha = i;
				synchronized (mSurfaceHolder) {
					startLcdTest(COLOR_ARRAY[mIndex]);
				}			
			}
			mIndex++;
			Log.d("AgingTest", TAG + "---Lcd_Vibrate  COLOR_ARRAY.length---" +  COLOR_ARRAY.length);
			if (mIndex >=COLOR_ARRAY.length) {
				//startLcdTest(COLOR_ARRAY[mIndex]);
				mIndex =0;
			}
		}
	}
	
}
