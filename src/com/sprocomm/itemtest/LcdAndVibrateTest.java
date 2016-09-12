package com.sprocomm.itemtest;

import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.os.Handler;

public class LcdAndVibrateTest extends TestItem {

	public LcdAndVibrateTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
}
