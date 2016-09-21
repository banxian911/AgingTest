package com.sprocomm.itemtest;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sprocomm.AgingTest.R;
import com.sprocomm.itemtest.MicAndReceiverTest.MyHandler;
import com.sprocomm.itemtest.MicAndReceiverTest.Playtest;
import com.sprocomm.itemtest.MicAndReceiverTest.Receivetask;
import com.sprocomm.itemtest.MicAndReceiverTest.RecordThread;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class CameraTest extends TestItem implements Callback {

	private static final String TAG = "CameraTest";
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private VideoView videoView;
	private TextView mTextView;
	private View test_view;
	private View settingView;
	private Activity mActivity;

	private Camera mCamera = null;
	private int camer_id = 0;
	private static final int FRONT_CAMERA = 1;
	private static final int BACK_CAMERA = 0;

	private Boolean isCameraPreview = false;
	private Boolean isCameraRun = false;

	private Timer timer;
	private BackCameraTask myBackCameraTask;
	private FrontCameraTest myFrontCameraTest;
	private int time = 60 * 1000;// 前摄和后摄打开时间均为1分钟

	private final int status_start = 0;
	private final int status_stop = 1;

	MyHandler myHandler;

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case status_start:
				//closeCamera();
				stopPreview();
				Toast.makeText(mContext,R.string.startBackCamera, Toast.LENGTH_LONG).show();
				break;
			case status_stop:
				Toast.makeText(mContext,R.string.startFrontCamera, Toast.LENGTH_LONG).show();
				startFrontCamer();
				break;
			default:
				break;
			}
		}

	};

	public CameraTest(Context context, long time, Handler handler) {
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---CameraTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;

		mActivity = (Activity) mContext;
		settingView = mActivity.findViewById(R.id.setting_view);
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

		timer = new Timer();
		myHandler = new MyHandler();
		isCameraRun = true;
		startCameraPreView();

	}

	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		isCameraRun = false;
		timer.cancel();
		stopPreview();
		settingView.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		mSurfaceView.setVisibility(View.GONE);
		videoView.setVisibility(View.GONE);
	}
	
	private void stopPreview() {
		Log.d("AgingTest", TAG + "---stopPreview Camera---isCameraPreview---> " + isCameraPreview);
        if (mCamera != null && isCameraPreview) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            isCameraPreview = false;
        }
        
    }

/*	private void closeCamera() {
		Log.d("AgingTest", TAG + "---close Camera---");
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
			isCameraPreview = false;
		}
	}*/
	
	private void startCameraPreView() {
		// TODO Auto-generated method stub
		Log.d("AgingTest", TAG + "---CameraTest statView---");
		RecordThread thread = new RecordThread();
		thread.start();

	}

	class RecordThread extends Thread {
		@Override
		public void run() {
			Log.d("AgingTest", TAG + "---isCameraRun---=>" + isCameraRun);
			if (isCameraRun) {
				test();
			}

		}
		private void test() {
			Log.d("AgingTest", TAG + "---start backCamera--camera_id=>" + camer_id);
			myHandler.sendEmptyMessage(status_start);
			camer_id = BACK_CAMERA;
			startPreview();
			if (timer != null) {
				if (myBackCameraTask != null) {
					myBackCameraTask.cancel(); // 将原任务从队列中移除
				}
			}
			// timer = new Timer();
			myBackCameraTask = new BackCameraTask();
			timer.schedule(myBackCameraTask, time);
		}
	}

	class BackCameraTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//closeCamera();
			stopPreview();
			myHandler.sendEmptyMessage(status_stop);
		}
	}

	private void startFrontCamer() {
		Log.d("AgingTest", TAG + "---start FrontCamera--camera_id=>" + camer_id);
		camer_id = FRONT_CAMERA;
		startPreview();
		if (timer != null) {
			if (myFrontCameraTest != null) {
				myFrontCameraTest.cancel(); // 将原任务从队列中移除
			}
		}
		myFrontCameraTest = new FrontCameraTest();
		timer.schedule(myFrontCameraTest, time);
	}

	class FrontCameraTest extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (isCameraPreview) {
				//closeCamera();
				stopPreview();
				startCameraPreView();
			}
		}
	}

	private void startPreview() {
		Log.d("AgingTest", TAG + "---start Camera--camera_id=>" + camer_id);
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
		}
		try {
			Log.d("AgingTest", TAG + "---CameraTest open---");
			mCamera = Camera.open(camer_id);
		} catch (RuntimeException e) {
			Log.e("AgingTest", TAG + "---fail to open camera---");
			Toast.makeText(mContext,R.string.camare_not_found, Toast.LENGTH_LONG).show();
			mTextView.setVisibility(View.VISIBLE);
			mTextView.setTextSize(30);
			mTextView.setText(R.string.camare_not_found);
			e.printStackTrace();
			mCamera = null;
		}
		if (isCameraPreview) {
			stopPreview();
		}
		if (mCamera != null && !isCameraPreview) {
			setCameraParameters();
			setPreviewDisplay(mSurfaceHolder);
			try {
				mCamera.startPreview();
			} catch (Throwable ex) {
				//closeCamera();
				stopPreview();
			}
			isCameraPreview = true;
		}
	}

	private void setPreviewDisplay(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			mCamera.setPreviewDisplay(holder);
			setCameraDisplayOrientation(camer_id, mCamera);
			// mCamera.setDisplayOrientation(mDisplayOrientation);

		} catch (IOException e) {
			//closeCamera();
			stopPreview();
			e.printStackTrace();
		}
	}

	public static int getDisplayRotation(Activity activity) {
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			return 0;
		case Surface.ROTATION_90:
			return 90;
		case Surface.ROTATION_180:
			return 180;
		case Surface.ROTATION_270:
			return 270;
		}
		return 0;
	}

	private void setCameraParameters() {

		// setCameraDisplayOrientation(CAMERA_ID, mCamera);
		Parameters parameters = null;
		parameters = mCamera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		parameters.set("orientation", "portrait");

		Size size = parameters.getPictureSize();
		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = getOptimalPreviewSize(mActivity, sizes, (double) size.width / size.height);
		Size original = parameters.getPreviewSize();
		if (!original.equals(optimalSize)) {
			parameters.setPreviewSize(optimalSize.width, optimalSize.height);
			parameters = mCamera.getParameters();
		}
		Log.v("AgingTest", TAG + "---Preview size is " + optimalSize.width + "x" + optimalSize.height);
		// mPreviewWidth = optimalSize.width;
		// mPreviewHeight = optimalSize.height;
		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
	
		/**
		 * 在部分机型中出现java.lang.RuntimeException: setParameters failed
		 * 该错误有可能是主要在于代码控制分辨率的显示和真机测试分辨率不一样
		 * 解决办法：可以注释掉这一行代码，但是由此我们设置的一些参数就不能使用了。
		 */
		mCamera.setParameters(parameters);
	}

	public static Size getOptimalPreviewSize(Activity currentActivity, List<Size> sizes, double targetRatio) {
		final double ASPECT_TOLERANCE = 0.001;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		Display display = currentActivity.getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int targetHeight = Math.min(point.x, point.y);

		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
				break;
			}
		}

		if (optimalSize == null) {
			Log.w(TAG, "No preview size match the aspect ratio");
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public static void setCameraDisplayOrientation(int cameraId, Camera camera) {
		int result = getCameraDisplayOrientation(cameraId, camera);
		camera.setDisplayOrientation(result);
	}

	public static int getCameraDisplayOrientation(int cameraId, Camera camera) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int degrees = getDisplayRotation();
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;
		} else {
			result = (info.orientation - degrees + 360) % 360;
		}
		return result;
	}

	public static int getDisplayRotation() {
		return 0;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		mSurfaceHolder = holder;
		if (mCamera == null)
			return;
		if (isCameraPreview && holder.isCreating()) {
			setPreviewDisplay(holder);
		} else {
			startCameraPreView();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}
}
