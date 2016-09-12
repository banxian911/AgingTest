package com.sprocomm.itemtest;

import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.os.Handler;

public class CameraTest extends TestItem {

	public CameraTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
}
