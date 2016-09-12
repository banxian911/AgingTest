package com.sprocomm.AgingTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.TestListener;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.sprocomm.R;
import com.sprocomm.R.id;
import com.sprocomm.R.layout;
import com.sprocomm.R.menu;
import com.sprocomm.R.string;
import com.sprocomm.item.BatteryTest;
import com.sprocomm.item.RebootTest;
//import com.sprocomm.item.RebootTest;
import com.sprocomm.item.ReceiverTest;
import com.sprocomm.item.SetTestTimeActivity;
import com.sprocomm.item.TestReportActivity;
//import com.sprocomm.item.SleepTest;
import com.sprocomm.item.TakingTest;
import com.sprocomm.item.VibratorTest;
import com.sprocomm.itemtest.CameraTest;
import com.sprocomm.itemtest.LcdAndVibrateTest;
import com.sprocomm.itemtest.MicAndReceiverTest;
import com.sprocomm.itemtest.Play3DTest;
import com.sprocomm.itemtest.SpkTest;
//import com.sprocomm.itemtest.ThreeDPlayTest;
import com.sprocomm.itemtest.VideoTest;
import com.sprocomm.utils.TestItem;

import android.util.Log;

public class AgingTest extends Activity implements OnCheckedChangeListener,OnClickListener{
	
	private static final String TAG = "AgingTest";
	
	public static final String SAVE_DATA = "testState";
	public static final String TEST_TIME = "testtime";
	public static boolean DEBUG = true;
	
	public static final String VIDEO_NAME = "moveTest.mp4";
	public static final String videoPath = new File(Environment.getExternalStorageDirectory(),VIDEO_NAME).getAbsolutePath();
	
	private static final long MINUTE = 60 * 1000l;
	public static final int MSG_WAT_START = 0x01;
	public static final int MSG_WAT_STOP = 0x02;
	
	
	private static boolean isCirculation = false;
	private static boolean isInTest;
	
//	CheckBox box_reboot;
//	CheckBox box_sleep;
//	CheckBox box_vibrate;
//	CheckBox box_receiver;
//	CheckBox box_taking;
////	CheckBox box_Video;
//	CheckBox box_battery;
//	CheckBox box_isCirculation;
	
	CheckBox box_Video;
	CheckBox box_3dplay;
	CheckBox box_lcd_vibrate;
	CheckBox box_spk;
	CheckBox box_mic_receiver;
	CheckBox box_camera;
	CheckBox box_reboot;
	CheckBox box_isCirculation;
	Context mContext;
	
	Button start;
	Button stop;
	Button stop_testview;
	
	private boolean isFull = false;
	View test_view;
	PowerManager pm = null;
	WakeLock wakeLock = null;
	private int mBatteryLevel;
	BatteryTest bt;
	private KeyguardManager kManager;
	private KeyguardManager.KeyguardLock lock;
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WAT_START:
				Log.i(TAG, "-----MSG_WAT_START-1---");
				for (int i = 0; i < testlist.size(); i++) {
					TestItem item = testlist.get(i);
					if(item.isNeedTest && !item.isTestEnd){
						item.startTest();
						testCheckbox.get(i).setBackgroundColor(Color.GRAY);
						isInTest = true;
						updateUI();
						return;
					}
				}
				
				Log.i(TAG, "-----MSG_WAT_START-2---");
				for (int j = 0; j < testlist.size(); j++) {
					testlist.get(j).isNeedTest = testCheckbox.get(j).isChecked();
					testlist.get(j).isTestEnd = false;
				}
				mHandler.sendEmptyMessage(MSG_WAT_STOP);
				if(box_isCirculation.isChecked()){
					for (int j2 = 0; j2 < testlist.size(); j2++) {
						if(testlist.get(j2).isNeedTest){
							isInTest = true;
//							mHandler.sendEmptyMessage(MSG_WAT_START);
							mHandler.sendEmptyMessageDelayed(MSG_WAT_START, 200);
						}
					}
				}else{
					Log.i(TAG, "-----MSG_WAT_START-3---");

//					for (int j2 = 0; j2 < testlist.size(); j2++) {
//						if(!testlist.get(j2).isTestPass)return;
//						if(!testlist.get(j2).isNeedTest)return;
//						Log.i("yuanluo", "-----MSG_WAT_START-4---");

//					}
					Log.i(TAG, "-----MSG_WAT_START-5---");
		            Intent intent = new Intent(AgingTest.this, TestReportActivity.class);
		            startActivity(intent);
//
//					new AlertDialog.Builder(mContext).setTitle(R.string.dialog_title)
//					.setPositiveButton(android.R.string.ok, null)
//					.setView(getLayoutInflater().inflate(R.layout.test_state, null))
//					.show();
				}
				
				break;
			case MSG_WAT_STOP:
				boolean test_next = msg.getData().getBoolean("test_next");
				for (int i = 0; i < testlist.size(); i++) {
					if(testlist.get(i).isInTest){
						testlist.get(i).stopTest(test_next ? true : false);
						testCheckbox.get(i).setBackgroundColor(Color.TRANSPARENT);
						break;
					}
				}
				if(test_next){
//					mHandler.sendEmptyMessage(MSG_WAT_START);
					mHandler.sendEmptyMessageDelayed(MSG_WAT_START, 500);
				}else{
					isInTest = false;
					for (int i = 0; i < testlist.size(); i++) {
						testlist.get(i).isTestEnd = false;
					}
					updateUI();
				}
				break;
				
			}
		}
	};
	
	private static ArrayList<TestItem> testlist = new ArrayList<TestItem>();
	ArrayList<CheckBox> testCheckbox = new ArrayList<CheckBox>();
	
	private String test_time = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, 
        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		kManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		lock = kManager.newKeyguardLock("keyguardlock");
		lock.disableKeyguard();
		
		setContentView(R.layout.main_activity);
	/*	registerReceiver(mBatteryInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));*/
		mContext = this;
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "sprocomm");
		wakeLock.acquire();
		
		box_Video = (CheckBox) findViewById(R.id.video);
		box_3dplay = (CheckBox)findViewById(R.id.play_3d);
		box_lcd_vibrate = (CheckBox)findViewById(R.id.lcd_vibrate);
		box_spk = (CheckBox)findViewById(R.id.spk);
		box_mic_receiver = (CheckBox)findViewById(R.id.mic_receiver);
		box_camera = (CheckBox)findViewById(R.id.camera);
		box_reboot = (CheckBox) findViewById(R.id.reboot);
		box_isCirculation = (CheckBox) findViewById(R.id.iscirculation);
		
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		stop_testview = (Button) findViewById(R.id.stop_testview);
		
		test_view = findViewById(R.id.test_view);
		
		testCheckbox.clear();
		testlist.clear();

		testCheckbox.add(box_Video);
		testCheckbox.add(box_3dplay);
		testCheckbox.add(box_lcd_vibrate);
		testCheckbox.add(box_spk);
		testCheckbox.add(box_mic_receiver);
		testCheckbox.add(box_camera);
		testCheckbox.add(box_reboot);
		
		box_Video.setOnCheckedChangeListener(this);
		box_3dplay.setOnCheckedChangeListener(this);
		box_lcd_vibrate.setOnCheckedChangeListener(this);
		box_spk.setOnCheckedChangeListener(this);
		box_mic_receiver.setOnCheckedChangeListener(this);
		box_camera.setOnCheckedChangeListener(this);
		box_reboot.setOnCheckedChangeListener(this);
		box_isCirculation.setOnCheckedChangeListener(this);
		
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		stop_testview.setOnClickListener(this);
		
		long testTime = 30 * MINUTE;
		
		test_time = getSharedPreferences(AgingTest.TEST_TIME, Context.MODE_PRIVATE).getString("test_time", "30/30/30/30/30/30/30");
		Log.i("AgingTest",TAG + "------test_time-----" + test_time);
		String[] test_time_eachS = test_time.split("/");
		int[] test_time_eachI = new int[test_time_eachS.length];
		for (int i=0; i<test_time_eachS.length; i++) {
			test_time_eachI[i] = Integer.parseInt(test_time_eachS[i]);
		}


//		testlist.add(new ThreeDPlayTest(this, test_time_eachI[1] * MINUTE,mHandler));
//		testlist.add(new RebootTest(this,test_time_eachI[0] * MINUTE,mHandler));
//		//testlist.add(new SleepTest(this,test_time_eachI[1] * MINUTE,mHandler));
//		
//		testlist.add(new VibratorTest(this,test_time_eachI[2] * MINUTE,mHandler));
//		testlist.add(new ReceiverTest(this,test_time_eachI[3] * MINUTE,mHandler));
//		testlist.add(new TakingTest(this,test_time_eachI[4] * MINUTE,mHandler));
//		testlist.add(new VideoTest(this,test_time_eachI[5] * MINUTE,mHandler));
//		bt = new BatteryTest(this,test_time_eachI[6] * MINUTE,mHandler);
//		testlist.add(bt);
		
		
		testlist.add(new VideoTest(this,test_time_eachI[0] * MINUTE,mHandler));
		testlist.add(new Play3DTest(this, test_time_eachI[1] * MINUTE,mHandler));
		testlist.add(new LcdAndVibrateTest(this, test_time_eachI[2] * MINUTE, mHandler));
		testlist.add(new SpkTest(this, test_time_eachI[3] * MINUTE, mHandler));
		testlist.add(new MicAndReceiverTest(this, test_time_eachI[4] * MINUTE, mHandler));
		testlist.add(new CameraTest(this, test_time_eachI[5] * MINUTE, mHandler));	
		testlist.add(new RebootTest(this,test_time_eachI[6] * MINUTE,mHandler));
		
		boolean isRebootTest = getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).getBoolean("reboot", false);
		isInTest = isRebootTest;
		boolean isFromReceiver = getIntent().getBooleanExtra("fromReceiver", false);
		if(isRebootTest){
			TestItem rebootTest = testlist.get(0);
			rebootTest.isInTest = isFromReceiver ? false : true;
			rebootTest.isTestPass = isFromReceiver ? true : false;
			rebootTest.isTestEnd  = isFromReceiver ? true : false;
			testCheckbox.get(0).setBackgroundColor(isFromReceiver ? Color.TRANSPARENT : Color.GRAY);
			if(isFromReceiver){
				rebootTest.stopTest(true);
				mHandler.sendEmptyMessage(MSG_WAT_START);
			}
			
		}
		
		/*if(! new File(videoPath).exists()){
			new Thread(new Runnable() {
				public void run() {
					InputStream input = null;
					FileOutputStream output = null;
					try {
						input = getAssets().open(VIDEO_NAME);
						output = new FileOutputStream(videoPath);
						byte[] tmp = new byte[512];
						while ((input.read(tmp)) != -1) {
							output.write(tmp);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						if(input != null){
							try {
								input.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(output != null){
							try {
								output.flush();
								output.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();

		}*/
		
		init();
	}
	
	public void init(){
		Log.i("AgingTest",TAG + "------testlist.size()------" + testlist.size());
		for (int i = 0; i < testlist.size(); i++) {
			Log.i("AgingTest",TAG + "------i---A----" + i);
			TestItem item = testlist.get(i);
			item.isTestPass = getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).getBoolean(item.getClass().getName(), false);
			String key = testlist.get(i).getClass().getName()+"isCheck";
			item.isNeedTest = getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).getBoolean(key, false);
			testCheckbox.get(i).setChecked(item.isNeedTest);
			box_isCirculation.setChecked(getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).getBoolean("isCirculation", false));
		}
		
		test_view.setVisibility(View.GONE);
		start.setEnabled(true);
		stop.setEnabled(false);
		updateUI();
	}
	
	public void updateUI(){
		System.out.println("XIONG ------updateUI-----1---isInTest="+isInTest);
		Log.i("AgingTest",TAG + "----testCheckbox.size()---->" + testCheckbox.size());
		for (int i = 0; i < testCheckbox.size(); i++) {
			CheckBox checkbox = testCheckbox.get(i);
			checkbox.setTextColor(testlist.get(i).isTestPass ? Color.GREEN : Color.RED);
			checkbox.setEnabled(false);
		}
		box_isCirculation.setEnabled(false);
		start.setEnabled(false);
		stop.setEnabled(false);
		
		if(isInTest){
			stop.setEnabled(true);
		}else{
			
			for (int i = 0; i < testCheckbox.size(); i++) {
				testCheckbox.get(i).setEnabled(true);
			}
			box_isCirculation.setEnabled(true);
			start.setEnabled(true);
			stop.setEnabled(false);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int index = -1;
		switch (buttonView.getId()) {
			case R.id.video:
				index = 0;
				break;
			case R.id.play_3d:
				index = 1;
				break;
			case R.id.lcd_vibrate:
				index = 2;
				break;
			case R.id.spk:
				index = 3;
				break;
			case R.id.mic_receiver:
				index = 4;
				break;
			case R.id.camera:
				index = 5;
				break;
			case R.id.reboot:
				index = 6;
				break;
			case R.id.iscirculation:
				isCirculation = isChecked;
				getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean("isCirculation", isChecked).commit();
				break;
		}
		if(index != -1){
			testlist.get(index).isNeedTest = isChecked;
			String key = testlist.get(index).getClass().getName()+"isCheck";
			Log.i("AgingTest",TAG + "----key---->" + key);
			getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean(key, isChecked).commit();
		}
		updateUI();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			isInTest = true;
			mHandler.sendEmptyMessage(MSG_WAT_START);
			break;
		case R.id.stop:
		case R.id.stop_testview:
			isInTest = false;
			mHandler.sendEmptyMessage(MSG_WAT_STOP);
			break;
		}
		updateUI();
	}
	
	@Override
	protected void onDestroy() {
		Toast.makeText(mContext, "Del the video file", Toast.LENGTH_LONG).show();
		super.onDestroy();
	//	unregisterReceiver(mBatteryInfoReceiver);
		lock.reenableKeyguard();
		if(wakeLock.isHeld())wakeLock.release();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				File tmpFile = 	new File(videoPath);
				if(tmpFile.exists()){
					tmpFile.delete();
					System.out.println("XIONG ----delect the Video file end");
				}
			}
		}).start();
	}
	
	long lastTime = 0;
	@Override
	public void onBackPressed() {
		
		long currentTime = System.currentTimeMillis();
		if(currentTime-lastTime > 2000){
			lastTime = currentTime;
			Toast.makeText(this,R.string.back_toast, Toast.LENGTH_SHORT).show();
			return;
		}
		if(!testlist.get(0).isInTest)mHandler.sendEmptyMessage(MSG_WAT_STOP);
		super.onBackPressed();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
        
    }
    
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//        if (requestCode == SET_TEST_TIME_CODE) {
//            if (resultCode == RESULT_OK) {
//            	Log.i("yuanluo", "------ok---------");
//            } else if (resultCode == RESULT_CANCELED) {
//            	Log.i("yuanluo", "------cancel---------");
//            }
//        }
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	public static ArrayList<TestItem> getList() {
		return testlist;
	}
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.test_time) {
            Intent intent = new Intent(AgingTest.this, SetTestTimeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.test_report) {
            Intent intent = new Intent(AgingTest.this, TestReportActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	/*private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
	        String action = intent.getAction();
	        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
	        	mBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 100);
	        	bt.setBatteryLevel(mBatteryLevel);
	        }
	    }
	};
	
	public int getBatteryPer(){
		return mBatteryLevel;
	}*/
}
