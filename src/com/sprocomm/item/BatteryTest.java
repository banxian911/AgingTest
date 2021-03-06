package com.sprocomm.item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;

import com.sprocomm.TestItem;

public class BatteryTest extends TestItem{
	private int batteryLevel;
	
	public BatteryTest(Context context,long time, Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
	}

	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		if(batteryLevel >= 75){
			isInTest = false;
			isTestPass = true;
			isTestEnd = true;
		}else{
			isTestPass = false;
			isTestEnd = true;
		}
	}
	
	public void setBatteryLevel(int level){
		batteryLevel = level;
	}
}
