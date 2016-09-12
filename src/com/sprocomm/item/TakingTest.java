package com.sprocomm.item;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import com.sprocomm.R;
import com.sprocomm.TestItem;

public class TakingTest  extends TestItem implements Callback{

	public static final String PICTURE_TAG = "test.jpg";
	
	public static boolean isInCamera = false;
	
	private boolean bIfPhoto = false;
	private boolean bIfPreview = false;
	private int mDisplayRotation;
	private int mDisplayOrientation;
	
	private Camera mCamera = null;
	
	private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private String currentPicturePath = "";
	
    SurfaceView surfaceView;
    Activity mActivity;
    VideoView videoView;
    View setting_view;
    View test_view;
    
	public TakingTest(Context context,long time,Handler handler) {
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
		surfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);
		setting_view = mActivity.findViewById(R.id.view2);
		videoView = (VideoView) mActivity.findViewById(R.id.videoView);
		test_view = mActivity.findViewById(R.id.test_view);
		mSurfaceView = (SurfaceView) mActivity.findViewById(R.id.camera_surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		setting_view.setVisibility(View.GONE);
		test_view.setVisibility(View.VISIBLE);
		videoView.setVisibility(View.GONE);
		surfaceView.setVisibility(View.VISIBLE);
		
		startPreview();
	}

	@Override
	public void stopTest(boolean isPass) {
		super.stopTest(isPass);
		System.out.println("XIONG ----"+this.getClass().getName()+"-----stop----");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		isInCamera = false;
		stopPreview();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				File[] tmps = Environment.getExternalStorageDirectory().listFiles();
				for (int i = 0; i < tmps.length; i++) {
					if(tmps[i].getName().endsWith(PICTURE_TAG))tmps[i].delete();
				}
			}
		}).start();
//		surfaceView.setVisibility(View.GONE);
//		setting_view.setVisibility(View.VISIBLE);
		videoView.setVisibility(View.VISIBLE);
		setting_view.setVisibility(View.VISIBLE);
		test_view.setVisibility(View.GONE);
		surfaceView.setVisibility(View.GONE);
	}
	
	public void startPreview() {
        bIfPhoto = false;

        if (mCamera == null) {
			try {
            	mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
			}catch(RuntimeException e){
				Toast.makeText(mContext, R.string.camare_not_found, Toast.LENGTH_SHORT).show();
				return;
			}
        }
    	if(bIfPreview) stopPreview();
        if (mCamera != null && !bIfPreview) {
            setCameraParameters();
            setPreviewDisplay(mSurfaceHolder);
            try {
                mCamera.startPreview();
                takingHandler.sendEmptyMessage(0);
            } catch (Throwable ex) {
                closeCamera();
            }
            bIfPreview = true;
        }
    }
	
	private void stopPreview() {
        if (mCamera != null && bIfPreview) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        bIfPreview = false;
    }
	
	private void closeCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
            bIfPreview = false;
        }
    }
	
	private void setCameraParameters() {
        Camera.Parameters mParameters = mCamera.getParameters();

        List<Integer> frameRates = mParameters.getSupportedPreviewFrameRates();
        if (frameRates != null) {
            Integer max = Collections.max(frameRates);
            mParameters.setPreviewFrameRate(max);
        }
        mParameters.setPictureSize(640, 480);
        mParameters.setFlashMode(Parameters.FLASH_MODE_ON);
        Size size = mParameters.getPictureSize();
        List<Size> sizes = mParameters.getSupportedPreviewSizes();
        Size optimalSize = getOptimalPreviewSize(
                sizes, (double) size.width / size.height);
        if (optimalSize != null) {
            mParameters.setPreviewSize(optimalSize.width, optimalSize.height);
        }
         mCamera.setParameters(mParameters);

    }
	
	private Size getOptimalPreviewSize(List<Size> sizes, double targetRatio) {
        final double ASPECT_TOLERANCE = 0.05;
    if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        int targetHeight = Math.min(display.getHeight(), display.getWidth());
        if (targetHeight <= 0) {
            WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            targetHeight = windowManager.getDefaultDisplay().getHeight();
        }
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
      if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
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
    
    public static int getDisplayOrientation(int degrees, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
    
    private void setPreviewDisplay(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
      mDisplayRotation = getDisplayRotation((Activity)mContext);
      mDisplayOrientation = getDisplayOrientation(mDisplayRotation, CameraInfo.CAMERA_FACING_BACK);
      mCamera.setDisplayOrientation(mDisplayOrientation);
        		  
        } catch (IOException e) {
            closeCamera();
            e.printStackTrace();
        }
    }
    
    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
        }
        return 0;
    }
    
    private void onTakePictures() {
		currentPicturePath = System.currentTimeMillis()+PICTURE_TAG;
        if (mCamera != null && bIfPreview) {
            if (bIfPhoto == false)
            {
                bIfPhoto = true;
                mCamera.autoFocus(autofocusCallback);
            }
        }
    }
    
    private PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {

            Bitmap bm = BitmapFactory.decodeByteArray(_data, 0, _data.length);

//            File myCaptureFile = new File(Environment.getExternalStorageDirectory().getName());
            File myCaptureFile = new File(Environment.getExternalStorageDirectory(),currentPicturePath);
            System.out.println("XIONG ------myCaptureFile="+myCaptureFile.getAbsolutePath());
            try {
            	if(!myCaptureFile.exists()){
            		myCaptureFile.createNewFile();
            	}
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                        myCaptureFile));

                bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bm.recycle();
                bos.flush();
                bos.close();
                stopPreview();
                startPreview();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    };
    
    private AutoFocusCallback autofocusCallback = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }
    };
    
    private ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
        }
    };
    
    private PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
        }
    };
    
    Handler takingHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		onTakePictures();
    	};
    };

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w, int h) {
        mSurfaceHolder = surfaceholder;
	    if(mCamera == null) return;
	    if(bIfPreview && surfaceholder.isCreating()) setPreviewDisplay(surfaceholder); 
	    else  startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}


}
