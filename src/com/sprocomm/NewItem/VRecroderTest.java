package com.sprocomm.NewItem;

import com.sprocomm.AgingTest.VideoRecroder;
import com.sprocomm.utils.TestItem;
import com.sprocomm.utils.VideoRecorderUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class VRecroderTest extends TestItem {

	private static final String TAG = "VRecroderTest";

	public VRecroderTest(Context context, long time, Handler handler) {
		// TODO Auto-generated constructor stub
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

	@Override
	public void startTest() {
		// TODO Auto-generated method stub
		super.startTest();
		Log.d("AgingTest", TAG + "---VRecroderTest start---");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		Intent mIntent = new Intent(mContext, VideoRecroder.class);
		// mIntent.putExtra("mathcharm", 3);
		mContext.startActivity(mIntent);
	}

	@Override
	public void stopTest(boolean isPass) {
		// TODO Auto-generated method stub
		super.stopTest(isPass);
		Log.d("AgingTest", TAG + "---VRecroderTest stop---");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;

		VideoRecroder.instance.finish();
	}

}
