package com.sprocomm.item;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;

import com.sprocomm.AgingTest;
import com.sprocomm.TestItem;

public class RebootTest extends TestItem{
	
	Activity mActivity;
	public RebootTest(Context context,long time,Handler handler) {
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

	@Override
	public void startTest() {
		super.startTest();
		System.out.println("XIONG ----"+this.getClass().getName()+"-----start----");
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
		System.out.println("XIONG ----"+this.getClass().getName()+"-----stop----");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
	}
}
