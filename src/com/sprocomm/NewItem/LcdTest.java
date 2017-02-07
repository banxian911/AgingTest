package com.sprocomm.NewItem;

import com.sprocomm.AgingTest.MathCharmActivity;
import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class LcdTest extends TestItem {
	private static String TAG = "LcdTest";

	public LcdTest(Context context,long time,Handler handler) {
		// TODO Auto-generated constructor stub
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---LcdTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		Intent mIntent = new Intent(mContext,MathCharmActivity.class);
		mIntent.putExtra("mathcharm", 6);
		mContext.startActivity(mIntent);
	}
	
	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---LcdTest stop---");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		if (MathCharmActivity.instance != null) {
			MathCharmActivity.instance.finish();
		}
	}
}
