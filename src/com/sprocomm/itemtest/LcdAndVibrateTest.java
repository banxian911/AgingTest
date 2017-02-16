package com.sprocomm.itemtest;


import com.sprocomm.R;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

public class LcdAndVibrateTest extends TestItem implements Callback {

	private static final String TAG = "LcdAndVibrateTest";

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private TextView mTextView;
	private VideoView videoView;
	private View test_view;
	private View settingView;
	private Activity mActivity;

	private Vibrator vibrator = null;

	/**
	 * 与SurfaceHolder绑定的Canvas
	 */
	private Canvas mCanvas;

	private int currentAlpha = 0; // 当前的不透明值
	int mIndex = 0;
	private static final int[] COLOR_ARRAY = new int[] { Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE };

	private Boolean isRunning = false;
	private final int status_start = 0;
	private final int status_stop = 1;
	MyHandler myHandler;

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case status_start:
				// Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
				// isRunning = true;
				InitLcdTest();
				break;
			case status_stop:
				isRunning = false;
				InitLcdTest();
				break;
			default:
				break;
			}
		}
	}

	public LcdAndVibrateTest(Context context, long time, Handler handler) {
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

		mActivity = (Activity) mContext;
		settingView = mActivity.findViewById(R.id.main_view);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		mTextView = (TextView) mActivity.findViewById(R.id.textView);

		settingView.setVisibility(View.GONE);
		test_view.setVisibility(View.VISIBLE);
		mSurfaceView.setVisibility(View.VISIBLE);
		videoView.setVisibility(View.GONE);
		mTextView.setVisibility(View.GONE);

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);

		isRunning = true;
		myHandler = new MyHandler();
		InitLcdTest();
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
		myHandler.sendEmptyMessage(status_stop);
		stopVibrator();
		videoView.setVisibility(View.VISIBLE);
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		mSurfaceView.setVisibility(View.GONE);
	}

	

	private void startVibrator() {
		vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 100, 10, 100, 1000 }, 0);
	}

	private void stopVibrator() {
		if (vibrator != null)
			vibrator.cancel();
		vibrator = null;
	}

	private void InitLcdTest() {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---Lcd Test init---");
		RecordThread thread = new RecordThread();
		thread.start();
	}

	class RecordThread extends Thread {
		@Override
		public void run() {
			Log.d("AgingTest", TAG + "---isRunning---=>" + isRunning);
			while (isRunning) {
				test();
			}
			if (isRunning) {
				myHandler.sendEmptyMessage(status_start);
			}
		}

		private void test() {
			for (int i = 255; i > -10; i = i - 10) {
				if (i < 0)
					i = 0;// 如果当前不透明度小于零,将不透明度置为零
				currentAlpha = i;
				synchronized (mSurfaceHolder) {
					startLcdTest(COLOR_ARRAY[mIndex]);
				}
			}
			Log.d("AgingTest", TAG + "---mIndex---=>" + mIndex);
			mIndex++;
			Log.d("AgingTest", TAG + "---Lcd_Vibrate  COLOR_ARRAY.length---" + COLOR_ARRAY.length);
			if (mIndex >= COLOR_ARRAY.length) {
				// startLcdTest(COLOR_ARRAY[mIndex]);
				mIndex = 0;
			}

		}
	}

	private void startLcdTest(int color) {
		// TODO Auto-generated method stub
		//Log.d("AgingTest", TAG + "---Lcd_Vibrate color---" + color);
		mCanvas = mSurfaceHolder.lockCanvas();
		// canvas.drawColor();
		if (mCanvas != null) {
			mCanvas.drawColor(color);
		}
		if (mCanvas != null) {// 如果当前画布不为空
			mSurfaceHolder.unlockCanvasAndPost(mCanvas);// 解锁画布
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		mSurfaceHolder = holder;
		/*if (isInTest) {
			
		}*/
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// // 通知关闭线程
		// isRunning = false;
	}

	/*@Override public void run() { // TODO Auto-generated method stub
	 while(isRunning){ Log.d("AgingTest", TAG + "---Lcd_Vibrate mIndex---" +
	  mIndex); startLcdTest(COLOR_ARRAY[mIndex]); mIndex++; Log.d("AgingTest",
	  TAG + "---Lcd_Vibrate  COLOR_ARRAY.length---" + COLOR_ARRAY.length); if
	 (mIndex >= COLOR_ARRAY.length) { startLcdTest(COLOR_ARRAY[mIndex]);
	  mIndex =0; } for (int i = 255; i>-10;i=i-10) { if(i<0)
	  i=0;//如果当前不透明度小于零,将不透明度置为零 currentAlpha = i; synchronized
	  (mSurfaceHolder) { startLcdTest(COLOR_ARRAY[mIndex]); } } mIndex++;
	  Log.d("AgingTest", TAG + "---Lcd_Vibrate  COLOR_ARRAY.length---" +
	  COLOR_ARRAY.length); if (mIndex >=COLOR_ARRAY.length) {
	  //startLcdTest(COLOR_ARRAY[mIndex]); mIndex =0; } } }
	 */

}
