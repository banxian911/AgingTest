package com.sprocomm.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;

public class VideoRecorderUtil {

	private static final String TAG = "VideoRecorderUtil";
	public boolean isRecording = false;
	private String lastFileName;
	private MediaRecorder mediarecorder;
	private Camera mCamera;
	int timeSize = 0;
	private Timer timer;
	private deleteVRdataTask mRdataTask;
	private int deletetime = 30 * 1000;

	public String newFileName() {
		try {
			Object localObject = Environment.getExternalStorageDirectory();
			localObject = new File(localObject + "/VRTestData/");
			if (!((File) localObject).exists()) {
				((File) localObject).mkdir();
			}
			localObject = File.createTempFile("/mov_", ".mp4", (File) localObject).getAbsolutePath();
			Log.i("AgingTest", TAG + "---localObject---" + localObject);
			return (String) localObject;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return null;
	}

	public void release() {
		if (this.mediarecorder != null) {
			this.mediarecorder.stop();
			this.mediarecorder.release();
			this.mediarecorder = null;
		}
	}

	public void startRecording(SurfaceView paramSurfaceView, int cameraId) {
		mediarecorder = new MediaRecorder();

		mCamera = getCameraInstance(cameraId);
		if (mCamera != null) {
			mCamera.setDisplayOrientation(90);
			mCamera.unlock();
			mediarecorder.setCamera(mCamera);
		}
		mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		mediarecorder.setVideoSize(640, 480);
		mediarecorder.setVideoFrameRate(30);
		mediarecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
		mediarecorder.setOrientationHint(90);
		mediarecorder.setPreviewDisplay(paramSurfaceView.getHolder().getSurface());
		lastFileName = newFileName();
		mediarecorder.setOutputFile(this.lastFileName);
		try {
			mediarecorder.prepare();
			Log.i("AgingTest", TAG + "---startRecording---");
			mediarecorder.start();
			isRecording = true;
			// runtimetest();
		} catch (Exception exception) {
			// exception.printStackTrace();
		}
	}

	public void stopRecording() {
		Log.i("AgingTest", TAG + "---stoptRecording-a--");
		if (mediarecorder != null) {
			Log.i("AgingTest", TAG + "---stoptRecording---");
			this.mediarecorder.stop();
			this.mediarecorder.release();
			this.mediarecorder = null;
			if (timer != null) {
				this.timer.cancel();
			}
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
			if ((this.lastFileName != null) && (!"".equals(this.lastFileName))) {
				File localFile = new File(this.lastFileName);
				localFile.delete();
			}
		}
	}

	private void runtimetest() {
		timeSize = 0;
		timer = new Timer();
		mRdataTask = new deleteVRdataTask();
		timer.schedule(mRdataTask, deletetime);
	}

	class deleteVRdataTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if ((lastFileName != null) && (!"".equals(lastFileName))) {
				File mFile = new File(lastFileName);
				Log.i("AgingTest", TAG + "---mFile.delete()--->" + mFile.delete());
				if (mFile.delete()) {
					lastFileName = newFileName();
					mediarecorder.setOutputFile(lastFileName);
				}
			}

		}
	}

	public static String getDate() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR); // 获取年份
		int month = ca.get(Calendar.MONTH); // 获取月份
		int day = ca.get(Calendar.DATE); // 获取日
		int minute = ca.get(Calendar.MINUTE); // 分
		int hour = ca.get(Calendar.HOUR); // 小时
		int second = ca.get(Calendar.SECOND); // 秒

		String date = "" + year + (month + 1) + day + hour + minute + second;
		Log.d("AgingTest", TAG + "date:" + date);

		return date;
	}

	/**
	 * 获取SD path
	 *
	 * @return
	 */
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString();
		}

		return null;
	}

	public static Camera getCameraInstance(int camerId) {
		Camera mCamera = null;
		try {
			mCamera = Camera.open(camerId);
		} catch (Exception e) {
			Log.d("AgingTest", TAG + "--Error is " + e.getMessage());
		}
		return mCamera;
	}

	private boolean CheckCameraHardware(Context mContext) {
		if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
				&& mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			// 摄像头存在
			return true;
		} else {
			// 摄像头不存在
			return false;
		}
	}

}
