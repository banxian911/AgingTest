package com.sprocomm.itemtest;

import com.sprocomm.utils.TestItem;

import android.content.Context;
import android.os.Handler;

public class Play3DTest extends TestItem {
	
	public Play3DTest(Context context,long time,Handler handler){
		mContext = context;
		testTime = time;
		mHandler = handler;
	}

}
