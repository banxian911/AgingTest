package com.sprocomm.item;

import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

public class VibratorTest  extends TestItem{

	Vibrator vibrator = null;
	
	public VibratorTest(Context context,long time,Handler handler) {
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
		
		vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{200,5000,200,5000}, 0);
	}

	@Override
	public void stopTest(boolean isPass) {
		super.stopTest(isPass);
		System.out.println("XIONG ----"+this.getClass().getName()+"-----stop----");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		if(vibrator != null)vibrator.cancel();
		vibrator = null;
	}
}
