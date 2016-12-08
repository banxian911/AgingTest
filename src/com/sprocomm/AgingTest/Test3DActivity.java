package com.sprocomm.AgingTest;


import com.sprocomm.Earth3D.OpenglSurfaceView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Test3DActivity extends Activity {
	private static final String TAG = "Test3DActivity";

	public static Context context;
	
	private OpenglSurfaceView gl_view;
	
	public static Test3DActivity instance = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("AgingTest", TAG + "--- start---");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_test3_d);
		
		context = this;
		
		instance = this;
		gl_view = new OpenglSurfaceView(this);
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearDisplay);
		ll.addView(gl_view);
		gl_view.requestFocus();
		gl_view.setFocusableInTouchMode(true);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//gl_view.stop
	}

}
