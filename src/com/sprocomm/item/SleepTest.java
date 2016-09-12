/*package com.sprocomm.item;

import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;

public class SleepTest extends TestItem{

	private static final int MSG_WHAT_SLEEP = 1;
	private static final int MSG_WHAT_WAKEUP = 2;
	PowerManager pm ;
	WakeLock localLock ;
	
	Handler sleepHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_SLEEP:
				pm.goToSleep(SystemClock.uptimeMillis());
				sleepHandler.sendEmptyMessageDelayed(MSG_WHAT_WAKEUP, 15000);
				break;
			case MSG_WHAT_WAKEUP:
				pm.wakeUp(SystemClock.uptimeMillis());
				sleepHandler.sendEmptyMessageDelayed(MSG_WHAT_SLEEP, 15000);
				break;
			}
		}
	};
	
	public SleepTest(Context context,long time,Handler handler) {
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		super.startTest();
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
		localLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sprocomm");
		localLock.acquire();
		sleepHandler.sendEmptyMessageDelayed(MSG_WHAT_SLEEP, 100);
	}

	@Override
	public void stopTest(boolean isPass) {
		super.stopTest(isPass);
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		sleepHandler.removeMessages(MSG_WHAT_SLEEP);
		sleepHandler.removeMessages(MSG_WHAT_WAKEUP);
		localLock.release();
		if(!pm.isScreenOn()){
			pm.wakeUp(SystemClock.uptimeMillis());
		}
	}
	


}
*/