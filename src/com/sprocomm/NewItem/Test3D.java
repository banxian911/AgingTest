package com.sprocomm.NewItem;

import com.sprocomm.AgingTest.MathCharmActivity;
import com.sprocomm.AgingTest.Test3DActivity;
import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class Test3D extends TestItem {
	private static String TAG = "Test3D";
	
	public Test3D(Context context,long time,Handler handler) {
		// TODO Auto-generated constructor stub
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---Test3D start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		Intent mIntent = new Intent(mContext,Test3DActivity.class);
		mContext.startActivity(mIntent);
		
	}
	
	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		
		Log.d("AgingTest", TAG + "---Test3D stop---");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		Test3DActivity.instance.finish();
	}

}
