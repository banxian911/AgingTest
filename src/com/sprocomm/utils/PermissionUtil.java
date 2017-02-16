 package com.sprocomm.utils;



import com.sprocomm.R;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class PermissionUtil extends Activity{
	
	private static final String TAG = "PermissionUtil";
	private static final String TAGM = "AgingTest";
    
    private static int PERMISSION_REQUEST_CODE = 1;
    
    private static int RESULT_CODE_OK = 0;
    private static int RESULT_CODE_FAILED = 1;
    
    private int mIndexPermissionRequestCamera;
    private int mIndexPermissionRequestMicrophone;
    private int mIndexPermissionRequestStorage;
    
    private boolean mShouldRequestCameraPermission;
    private boolean mShouldRequestStoragePermission;
    private boolean mShouldRequestMicrophonePermission;
    
    private boolean mFlagHasCameraPermission;
    private boolean mFlagHasMicrophonePermission;
    private boolean mFlagHasStoragePermission;
    
    private int mNumPermissionsToRequest;
    
    /**
     * Close activity when secure app passes lock screen or screen turns
     * off.
     */
    private final BroadcastReceiver mShutdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          Log.v(TAG, "received intent, finishing: " + intent.getAction());
          finish();
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	// setContentView(R.layout.permissions);
    	 
    	// Filter for screen off so that we can finish permissions activity
         // when screen is off.
         IntentFilter filter_screen_off = new IntentFilter(Intent.ACTION_SCREEN_OFF);
         registerReceiver(mShutdownReceiver, filter_screen_off);
    	 
         // Filter for phone unlock so that we can finish permissions activity
         // via this UI path:
         //    1. from secure lock screen, user starts secure camera
         //    2. user presses home button
         //    3. user unlocks phone
         IntentFilter filter_user_unlock = new IntentFilter(Intent.ACTION_USER_PRESENT);
         registerReceiver(mShutdownReceiver, filter_user_unlock);
         
    	  mNumPermissionsToRequest = 0;
    	  checkPermission();
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	Log.v(TAGM ,TAG+"---onDestroy: unregistering receivers");
        unregisterReceiver(mShutdownReceiver);
    }
    
	@TargetApi(23)
	public void checkPermission() {

		Log.v(TAGM ,TAG+"---checkPermission()---start---");
		Log.v(TAGM ,TAG+"---mNumPermissionsToRequest--1->"+mNumPermissionsToRequest);
		 if (checkSelfPermission(Manifest.permission.CAMERA)
	                != PackageManager.PERMISSION_GRANTED) {
	            mNumPermissionsToRequest++;
	            mShouldRequestCameraPermission = true;
	        } else {
	            mFlagHasCameraPermission = true;
	        }
		
		 if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
	                != PackageManager.PERMISSION_GRANTED) {
	            mNumPermissionsToRequest++;
	            mShouldRequestMicrophonePermission = true;
	        } else {
	            mFlagHasMicrophonePermission = true;
	        }

	        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
	                != PackageManager.PERMISSION_GRANTED) {
	            mNumPermissionsToRequest++;
	            mShouldRequestStoragePermission = true;
	        } else {
	            mFlagHasStoragePermission = true;
	        }

	        Log.v(TAGM ,TAG+"---mNumPermissionsToRequest--2->"+mNumPermissionsToRequest);
	        if (mNumPermissionsToRequest != 0) {
	        	buildPermissionsRequest();
	        } else {
	            handlePermissionsSuccess();
	        }
		
    }
	
	@TargetApi(23)
	private void buildPermissionsRequest() {
		String[] permissionsToRequest = new String[mNumPermissionsToRequest];
        int permissionsRequestIndex = 0;
        if (mShouldRequestCameraPermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.CAMERA;
            mIndexPermissionRequestCamera = permissionsRequestIndex;
            permissionsRequestIndex++;
        }
        if (mShouldRequestMicrophonePermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.RECORD_AUDIO;
            mIndexPermissionRequestMicrophone = permissionsRequestIndex;
            permissionsRequestIndex++;
        }
        if (mShouldRequestStoragePermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.READ_EXTERNAL_STORAGE;
            mIndexPermissionRequestStorage = permissionsRequestIndex;
            permissionsRequestIndex++;
        }
        Log.v(TAGM, TAG+"---requestPermissions count: " + permissionsToRequest.length);
        requestPermissions(permissionsToRequest, PERMISSION_REQUEST_CODE);
	}
	
	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		 if (mShouldRequestCameraPermission) {
	            if (grantResults.length > 0 && grantResults[mIndexPermissionRequestCamera] ==
	                    PackageManager.PERMISSION_GRANTED) {
	                mFlagHasCameraPermission = true;
	            } else {
	                handlePermissionsFailure();
	            }
	        }
	        if (mShouldRequestMicrophonePermission) {
	            if (grantResults.length > 0 && grantResults[mIndexPermissionRequestMicrophone] ==
	                    PackageManager.PERMISSION_GRANTED) {
	                mFlagHasMicrophonePermission = true;
	            } else {
	                handlePermissionsFailure();
	            }
	        }
	        if (mShouldRequestStoragePermission) {
	            if (grantResults.length > 0 && grantResults[mIndexPermissionRequestStorage] ==
	                    PackageManager.PERMISSION_GRANTED) {
	                mFlagHasStoragePermission = true;
	            } else {
	                handlePermissionsFailure();
	            }
	        }
	        
	        if (mFlagHasCameraPermission && mFlagHasMicrophonePermission && mFlagHasStoragePermission) {
	            handlePermissionsSuccess();
	        }

	}
	
	 private void handlePermissionsSuccess() {
	        //SPRD:fix bug519999 Create header photo, pop permission error
	     	SharedPreferences mPreferences = getSharedPreferences(TAG, Context.MODE_PRIVATE);
	     	Editor edit = mPreferences.edit();
	     	edit.putInt("result", RESULT_CODE_OK);
	     	edit.commit();
	        finish();
	    }

	    private void handlePermissionsFailure() {
	    	
	        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.app_name))
	                .setMessage(getResources().getString(R.string.error_permissions))
	                .setCancelable(false)
	                .setOnKeyListener(new Dialog.OnKeyListener() {
	                    @Override
	                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	                        if (keyCode == KeyEvent.KEYCODE_BACK) {
	                           // finish();
	                        }
	                        return true;
	                    }
	                })
	                .setPositiveButton(getResources().getString(R.string.dialog_dismiss),
	                        new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	Editor editor = getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
	                    	editor.putInt("result",RESULT_CODE_FAILED);
	                    	editor.commit();
	                        finish();
	                    }
	                })
	                .setNegativeButton(getResources().getString(R.string.dialog_again),
	                		new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									mNumPermissionsToRequest = 0;
							    	checkPermission();
								}
							})
	                .show();
	    }
	  


}
