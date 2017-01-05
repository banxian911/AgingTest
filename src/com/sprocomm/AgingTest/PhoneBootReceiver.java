package com.sprocomm.AgingTest;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class PhoneBootReceiver extends BroadcastReceiver{
	
	private static final String TAG = "PhoneBootReceiver";
	private static final String TAGM = "AgingTest";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAGM,TAG + "---onReceive start---->" );
		long startTime = context.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_PRIVATE).getLong("startTime", System.currentTimeMillis());
		boolean isRebootTest = context.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_PRIVATE).getBoolean("reboot", false);
		String setTime = context.getSharedPreferences(AgingTest.TEST_TIME, Context.MODE_PRIVATE).getString("test_time", "30/30/30/30/30/30/30/30/30/30/30/1");
		String[] test_time_eachS = setTime.split("/");
		int rebootTime = Integer.parseInt(test_time_eachS[0]);
		
		Log.i(TAGM,TAG + "---startTime--->" + startTime );
		Log.i(TAGM,TAG + "---isRebootTest--->" + isRebootTest );
		Log.i(TAGM,TAG + "---setTime--->" + setTime );
		Log.i(TAGM,TAG + "---test_time_eachS--->" + test_time_eachS );
		Log.i(TAGM,TAG + "---rebootTime--->" + rebootTime );
		
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		Log.i(TAGM,TAG + "ian.qu Aging phonebootreceiver has been received; isRebootTest= " +isRebootTest );
		if(isRebootTest){
			//PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "sprocomm");
			//wakeLock.acquire();
			
			//pm.wakeUp(SystemClock.uptimeMillis());//唤醒系统，需要系统权限
			
			KeyguardManager kManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardManager.KeyguardLock lock = kManager.newKeyguardLock("keyguardlock");
			lock.disableKeyguard();	
			if((System.currentTimeMillis() - startTime)/1000/60 >= rebootTime){
				Intent bootActivityIntent=new Intent(context,AgingTest.class);  
	            bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            bootActivityIntent.putExtra("fromReceiver", true);
	            context.startActivity(bootActivityIntent);  
//				context.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_PRIVATE).edit().putBoolean("reboot", false).commit();
			}else{
				//PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				pm.reboot("");
			}
		}
		 Uri uri = intent.getData();
		 if (uri == null) {
               return;
         }
         String host = uri.getHost();
         Intent i = new Intent("android.intent.action.MAIN");
         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         if ("1234".equals(host)) {
            i.setClass(context, AgingTest.class);
            context.startActivity(i);
         }
		
		
		
	}
}
