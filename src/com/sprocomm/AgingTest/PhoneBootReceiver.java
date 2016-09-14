package com.sprocomm.AgingTest;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.net.Uri;

public class PhoneBootReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("eric","onReceive");
		long startTime = context.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).getLong("startTime", System.currentTimeMillis());
		boolean isRebootTest = context.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).getBoolean("reboot", false);
		String setTime = context.getSharedPreferences(AgingTest.TEST_TIME, Context.MODE_PRIVATE).getString("test_time", "20/20/20/20/20/20");
		String[] test_time_eachS = setTime.split("/");
		int rebootTime = Integer.parseInt(test_time_eachS[0]);
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		Log.d("ian.qu","ian.qu Aging phonebootreceiver has been received; isRebootTest= " +isRebootTest);
		if(isRebootTest){
			//WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "sprocomm");
			//wakeLock.acquire();
			//pm.wakeUp(SystemClock.uptimeMillis());
			KeyguardManager kManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardManager.KeyguardLock lock = kManager.newKeyguardLock("keyguardlock");
			lock.disableKeyguard();	
			if((System.currentTimeMillis() - startTime)/1000/60 >= rebootTime){
				Intent bootActivityIntent=new Intent(context,AgingTest.class);  
	            bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            bootActivityIntent.putExtra("fromReceiver", true);
	            context.startActivity(bootActivityIntent);  
//				context.getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean("reboot", false).commit();
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
