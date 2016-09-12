package com.sprocomm.utils;

import com.sprocomm.AgingTest.AgingTest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public abstract class TestItem {
	
	private static final String TAG = "TestItem";
	public Context mContext;
	public int MSG_WHAT = 0;
	public long testTime = 0;
	public boolean isNeedTest = false;
	public boolean isInTest = false;
	public boolean isTestPass = false;
	public boolean isTestEnd = false;
	
	public Handler mHandler = null;
	
	public void startTest(){
		mContext.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean(getClass().getName(), false).commit();
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putBoolean("test_next", true);
		msg.setData(data);
		msg.what = AgingTest.MSG_WAT_STOP;
		Log.i(TAG, "---testTime--A--->" + testTime);
		mHandler.sendMessageDelayed(msg, testTime);
	}
	public void stopTest(boolean isPass){
		mHandler.removeMessages(AgingTest.MSG_WAT_START);
		mHandler.removeMessages(AgingTest.MSG_WAT_STOP);
		mContext.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean(getClass().getName(), isPass).commit();
	}

	public void setTestTime(long second) {
		testTime = second * 60 * 1000;
		Log.i(TAG, "---testTime---B-->" + testTime);
	}
}
