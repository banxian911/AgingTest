package com.sprocomm.itemtest;


import com.sprocomm.AgingTest;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

public class RebootTest extends TestItem{
	
	private static final String TAG = "RebootTest";
	private static final String TAGM = "AgingTest";
	
	Activity mActivity;
	public RebootTest(Context context,long time,Handler handler) {
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

	@Override
	public void startTest() {
		super.startTest();
		Log.i(TAGM,TAG + "---reboot start---->" );
		mContext.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean("reboot", true).commit();
		mContext.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putLong("startTime",System.currentTimeMillis()).commit();
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		mActivity =  (Activity)mContext;
		
		PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
		pm.reboot("test");
		
//		 PowerManager pManager=(PowerManager) getSystemService(Context.POWER_SERVICE);
	}

	@Override
	public void stopTest(boolean isPass) {
		super.stopTest(isPass);
		mContext.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean("reboot", false).commit();
		mContext.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putLong("startTime",0).commit();
		Log.i(TAGM,TAG + "---reboot stop---->" );
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
	}
}
