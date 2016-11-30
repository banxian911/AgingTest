package com.sprocomm.NewItem;

import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.os.Handler;

public class CpuTest extends TestItem {

	public CpuTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
}
