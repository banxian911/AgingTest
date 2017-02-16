package com.sprocomm.NewItem;

import com.sprocomm.MathCharmActivity;
import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class MemoryTest extends TestItem {
	private static final String TAG = "MemoryTest";

	public MemoryTest(Context context,long time,Handler handler) {
		// TODO Auto-generated constructor stub
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---MemoryTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		Intent mIntent = new Intent(mContext,MathCharmActivity.class);
		mIntent.putExtra("mathcharm", 5);
		mContext.startActivity(mIntent);
	}
	
	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---MemoryTest stop---");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		MathCharmActivity.instance.finish();
	}
}
