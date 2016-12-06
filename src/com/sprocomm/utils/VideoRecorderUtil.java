package com.sprocomm.utils;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;

public class VideoRecorderUtil {
	
	private static final String TAG = "VideoRecorderUtil";
	public  boolean isRecording = false;
	private String lastFileName;
	private MediaRecorder mediarecorder;
	private Camera mCamera;
	int timeSize = 0;
	Timer timer;

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

	public void startRecording(SurfaceView paramSurfaceView) {
		mediarecorder = new MediaRecorder();
	/*	mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
		if (mCamera != null) {
			mCamera.setDisplayOrientation(90);
			mCamera.unlock();
			mediarecorder.setCamera(mCamera);
		}*/
		mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		mediarecorder.setVideoSize(640, 480);
		mediarecorder.setVideoFrameRate(30);
		mediarecorder.setVideoEncodingBitRate(3*1024*1024);
		mediarecorder.setOrientationHint(90);
		mediarecorder.setPreviewDisplay(paramSurfaceView.getHolder().getSurface());
		lastFileName = newFileName();
		mediarecorder.setOutputFile(this.lastFileName);
		try {
			mediarecorder.prepare();
			Log.i("AgingTest", TAG + "---startRecording---");
			mediarecorder.start();
			isRecording = true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void stopRecording() {
		if (this.mediarecorder != null) {
			Log.i("AgingTest", TAG + "---stoptRecording---");
			this.mediarecorder.stop();
			this.mediarecorder.release();
			this.mediarecorder = null;
			this.timer.cancel();
			if ((this.lastFileName != null) && (!"".equals(this.lastFileName))) {
				File localFile = new File(this.lastFileName);
				String str = localFile.getName().substring(0, localFile.getName().lastIndexOf(".mp4")) + "_"
						+ this.timeSize + "s.mp4";
				Log.i("AgingTest", TAG + "---stoptRecording---str--->"+str);
				localFile.renameTo(new File(localFile.getParentFile().getAbsolutePath() + "/" + str));
			}
		}
	}

	private void runtimetest() {
		Object localObject1 = VideoRecorderUtil.this;
		((VideoRecorderUtil) localObject1).timeSize += 1;
		if (VideoRecorderUtil.this.timeSize == 1800) {
		}
		localObject1 = new File(VideoRecorderUtil.this.lastFileName);
		String str = ((File) localObject1).getName().substring(0, ((File) localObject1).getName().lastIndexOf(".mp4"))
				+ "_" + VideoRecorderUtil.this.timeSize + "s.mp4";
		((File) localObject1).renameTo(new File(((File) localObject1).getParentFile().getAbsolutePath() + "/" + str));
		((File) localObject1).delete();
		VideoRecorderUtil.this.timeSize = 0;
		VideoRecorderUtil.this.lastFileName = VideoRecorderUtil.this.newFileName();
		VideoRecorderUtil.this.mediarecorder.setOutputFile(VideoRecorderUtil.this.lastFileName);

	}
	
	  /**
     * 获取系统时间
     *
     * @return
     */
    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        String date = "" + year + (month + 1) + day + hour + minute + second;
        Log.d(TAG, "date:" + date);

        return date;
    }
	
	/**
     * 获取SD path
     *
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }

        return null;
    }
}
