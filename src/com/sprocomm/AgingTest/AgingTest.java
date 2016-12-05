package com.sprocomm.AgingTest;

import java.io.File;
import java.util.ArrayList;

import com.sprocomm.NewItem.AudioTest;
import com.sprocomm.NewItem.CpuTest;
import com.sprocomm.NewItem.EmmcTest;
import com.sprocomm.NewItem.LcdTest;
import com.sprocomm.NewItem.MemoryTest;
import com.sprocomm.NewItem.S3Test;
import com.sprocomm.NewItem.Test2D;
import com.sprocomm.itemtest.BatteryTest;
//import com.sprocomm.itemtest.BatteryTest;
import com.sprocomm.itemtest.CameraTest;
import com.sprocomm.itemtest.Play3DTest;
import com.sprocomm.itemtest.RebootTest;
//import com.sprocomm.itemtest.ThreeDPlayTest;
import com.sprocomm.itemtest.VideoTest;
import com.sprocomm.utils.PermissionUtil;
import com.sprocomm.utils.TestItem;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class AgingTest extends Activity implements OnCheckedChangeListener, OnClickListener {

	private static final String TAG = "AgingTest";
	private static final String TAGM = "AgingTest";

	public static final String SAVE_DATA = "testState";
	public static final String TEST_TIME = "testtime";

	public static boolean DEBUG = true;

	public static final String VIDEO_NAME = "moveTest.mp4";
	public static final String videoPath = new File(Environment.getExternalStorageDirectory(), VIDEO_NAME)
			.getAbsolutePath();

	private static final long MINUTE = 60 * 1000l;
	public static final int MSG_WAT_START = 0x01;
	public static final int MSG_WAT_STOP = 0x02;
	public static final int MSG_REBOOT_STOP = 0x03;

	private static boolean isCirculation = false;
	private static boolean isInTest;

	private static String stopTestBR = "com.sprocomm.AgingTest.StopTestBR";

	// CheckBox box_Video;
	// CheckBox box_3dplay;
	// CheckBox box_lcd_vibrate;
	// CheckBox box_spk;
	// CheckBox box_mic_receiver;
	// CheckBox box_camera;
	// CheckBox box_reboot;
	CheckBox box_isCirculation;

	private CheckBox box_reboot;
	private CheckBox box_cpu;
	private CheckBox box_audio;
	private CheckBox box_2d;
	private CheckBox box_s3;
	private CheckBox box_emmc;
	private CheckBox box_video;
	private CheckBox box_3d;
	private CheckBox box_battery;
	private CheckBox box_memory;
	private CheckBox box_lcd;
	private CheckBox box_camera;

	Context mContext;

	private Button button_start;
	private Button stop_testview;
	private Button button_select_all;
	private Button button_clear_all;
	private Button button_setting;
	private Button button_report;

	private boolean isFull = false;
	View test_view;
	PowerManager pm = null;
	WakeLock wakeLock = null;
	private int mBatteryLevel;
	// BatteryTest bt;
	private KeyguardManager kManager;
	private KeyguardManager.KeyguardLock lock;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WAT_START:
				Log.i(TAGM, TAG + "-----MSG_WAT_START-1---testlist.size()--->" + testlist.size());
				for (int i = 0; i < testlist.size(); i++) {
					TestItem item = testlist.get(i);
					Log.i(TAGM, TAG + "---i--->" + i + "---item.isNeedTest--->" + item.isNeedTest
							+ "---item.isTestEnd--->" + item.isTestEnd);
					if (item.isNeedTest && !item.isTestEnd) {
						item.startTest();
						testCheckbox.get(i).setBackgroundColor(Color.GRAY);
						isInTest = true;
						updateUI();
						return;
					}
				}
				Log.i(TAGM, TAG + "-----MSG_WAT_START-2---testlist.size()--->" + testlist.size());
				for (int j = 0; j < testlist.size(); j++) {
					testlist.get(j).isNeedTest = testCheckbox.get(j).isChecked();
					testlist.get(j).isTestEnd = false;
				}
				mHandler.sendEmptyMessage(MSG_WAT_STOP);
				if (box_isCirculation.isChecked()) {
					Log.i(TAGM, TAG + "-----MSG_WAT_START-3---testlist.size()--->" + testlist.size());
					/*
					 * for (int j2 = 0; j2 < testlist.size(); j2++) {
					 * if(testlist.get(j2).isNeedTest){ isInTest = true; //
					 * mHandler.sendEmptyMessage(MSG_WAT_START);
					 * mHandler.sendEmptyMessageDelayed(MSG_WAT_START, 2000); }
					 * }
					 */
					mHandler.sendEmptyMessageDelayed(MSG_WAT_START, 2000);
				} else {
					Log.i(TAGM, TAG + "-----MSG_WAT_START-4---");

					Intent intent = new Intent(AgingTest.this, TestReportActivity.class);
					startActivity(intent);
					//
					// new
					// AlertDialog.Builder(mContext).setTitle(R.string.dialog_title)
					// .setPositiveButton(android.R.string.ok, null)
					// .setView(getLayoutInflater().inflate(R.layout.test_state,
					// null))
					// .show();
				}

				break;
			case MSG_WAT_STOP:
				Log.i(TAGM, TAG + "-----MSG_WAT_STOP-1---");
				boolean test_next = msg.getData().getBoolean("test_next");
				Log.i(TAGM, TAG + "-----MSG_WAT_STOP-1---test_next--->" + test_next);
				for (int i = 0; i < testlist.size(); i++) {
					if (testlist.get(i).isInTest) {
						testlist.get(i).stopTest(test_next ? true : false);
						testCheckbox.get(i).setBackgroundColor(Color.TRANSPARENT);
						break;
					}
				}
				if (test_next) {
					mHandler.sendEmptyMessageDelayed(MSG_WAT_START, 500);
				} else {
					isInTest = false;
					for (int i = 0; i < testlist.size(); i++) {
						testlist.get(i).isTestEnd = false;
					}
					updateUI();
				}

				break;

			case MSG_REBOOT_STOP:
				Log.i(TAGM, TAG + "---MSG_REBOOT_STOP-1---testlist.size()--->" + testlist.size());
				for (int j = 0; j < testlist.size(); j++) {
					testlist.get(j).isNeedTest = testCheckbox.get(j).isChecked();
					testlist.get(j).isTestEnd = true;
				}
				mHandler.sendEmptyMessageDelayed(MSG_WAT_START, 2000);
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
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
		// WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		kManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		lock = kManager.newKeyguardLock("keyguardlock");
		lock.disableKeyguard();

		setContentView(R.layout.main_activity);
		/*
		 * registerReceiver(mBatteryInfoReceiver, new
		 * IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		 */
		AccessPermissions();

		registerReceiver(StopTestBR, new IntentFilter(stopTestBR));

		mContext = this;
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "sprocomm");
		wakeLock.acquire();

		initUI();
		init();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	private void initUI() {
		box_reboot = (CheckBox) findViewById(R.id.reboot);
		box_cpu = (CheckBox) findViewById(R.id.cpu);
		box_audio = (CheckBox) findViewById(R.id.audio);
		box_2d = (CheckBox) findViewById(R.id.test_2d);
		box_s3 = (CheckBox) findViewById(R.id.s3);
		box_emmc = (CheckBox) findViewById(R.id.emmc);
		box_video = (CheckBox) findViewById(R.id.video);
		box_3d = (CheckBox) findViewById(R.id.test_3d);
		box_battery = (CheckBox) findViewById(R.id.battery);
		box_memory = (CheckBox) findViewById(R.id.memory);
		box_lcd = (CheckBox) findViewById(R.id.lcd);
		box_camera = (CheckBox) findViewById(R.id.camera);

		box_isCirculation = (CheckBox) findViewById(R.id.iscirculation);

		button_start = (Button) findViewById(R.id.start);
		stop_testview = (Button) findViewById(R.id.stop_testview);
		button_select_all = (Button) findViewById(R.id.select_all);
		button_clear_all = (Button) findViewById(R.id.clear_all);
		button_setting = (Button) findViewById(R.id.setting);
		button_report = (Button) findViewById(R.id.report);

		test_view = findViewById(R.id.test_view);

		testCheckbox.clear();
		testlist.clear();

		testCheckbox.add(box_reboot);
		testCheckbox.add(box_cpu);
		testCheckbox.add(box_audio);
		testCheckbox.add(box_2d);
		testCheckbox.add(box_s3);
		testCheckbox.add(box_emmc);
		testCheckbox.add(box_video);
		testCheckbox.add(box_3d);
		testCheckbox.add(box_battery);
		testCheckbox.add(box_memory);
		testCheckbox.add(box_lcd);
		testCheckbox.add(box_camera);

		box_reboot.setOnCheckedChangeListener(this);
		box_cpu.setOnCheckedChangeListener(this);
		box_audio.setOnCheckedChangeListener(this);
		box_2d.setOnCheckedChangeListener(this);
		box_s3.setOnCheckedChangeListener(this);
		box_emmc.setOnCheckedChangeListener(this);
		box_video.setOnCheckedChangeListener(this);
		box_3d.setOnCheckedChangeListener(this);
		box_battery.setOnCheckedChangeListener(this);
		box_memory.setOnCheckedChangeListener(this);
		box_lcd.setOnCheckedChangeListener(this);
		box_camera.setOnCheckedChangeListener(this);
		box_isCirculation.setOnCheckedChangeListener(this);

		button_start.setOnClickListener(this);
		stop_testview.setOnClickListener(this);

		button_select_all.setOnClickListener(this);
		button_clear_all.setOnClickListener(this);
		button_setting.setOnClickListener(this);
		button_report.setOnClickListener(this);
		initTimeSet();

	}

	private void initTimeSet() {
		long testTime = 30 * MINUTE;

		test_time = getSharedPreferences(AgingTest.TEST_TIME, Context.MODE_PRIVATE).getString("test_time",
				"30/30/30/30/30/30/30/30/30/30/30/30");
		Log.i(TAGM, TAG + "------test_time-----" + test_time);
		String[] test_time_eachS = test_time.split("/");
		int[] test_time_eachI = new int[test_time_eachS.length];
		Log.i(TAGM, TAG + "------test_time_eachS.length-----" + test_time_eachS.length);
		for (int i = 0; i < test_time_eachS.length; i++) {
			test_time_eachI[i] = Integer.parseInt(test_time_eachS[i]);
		}

		testlist.add(new RebootTest(this, test_time_eachI[0] * MINUTE, mHandler));
		testlist.add(new CpuTest(this, test_time_eachI[1] * MINUTE, mHandler));
		testlist.add(new AudioTest(this, test_time_eachI[2] * MINUTE, mHandler));
		testlist.add(new Test2D(this, test_time_eachI[3] * MINUTE, mHandler));
		testlist.add(new S3Test(this, test_time_eachI[4] * MINUTE, mHandler));
		testlist.add(new EmmcTest(this, test_time_eachI[5] * MINUTE, mHandler));
		testlist.add(new VideoTest(this, test_time_eachI[6] * MINUTE, mHandler));
		testlist.add(new Play3DTest(this, test_time_eachI[7] * MINUTE, mHandler));
		testlist.add(new BatteryTest(this, test_time_eachI[8] * MINUTE, mHandler));
		testlist.add(new MemoryTest(this, test_time_eachI[9] * MINUTE, mHandler));
		testlist.add(new LcdTest(this, test_time_eachI[10] * MINUTE, mHandler));
		testlist.add(new CameraTest(this, test_time_eachI[11] * MINUTE, mHandler));

		boolean isRebootTest = getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE)
				.getBoolean("reboot", false);
		isInTest = isRebootTest;
		Log.i(TAGM, TAG + "------isInTest-----" + isInTest);
		boolean isFromReceiver = getIntent().getBooleanExtra("fromReceiver", false);
		Log.i(TAGM, TAG + "------isFromReceiver-----" + isFromReceiver);
		if (isRebootTest) {
			TestItem rebootTest = testlist.get(12);
			rebootTest.isInTest = isFromReceiver ? false : true;
			rebootTest.isTestPass = isFromReceiver ? true : false;
			rebootTest.isTestEnd = isFromReceiver ? true : false;
			testCheckbox.get(12).setBackgroundColor(isFromReceiver ? Color.TRANSPARENT : Color.GRAY);
			if (isFromReceiver) {
				rebootTest.stopTest(true);
				mHandler.sendEmptyMessage(MSG_REBOOT_STOP);
			}

		}

	}

	public void init() {
		Log.i(TAGM, TAG + "------testlist.size()------" + testlist.size());
		boolean isCleanReport = getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE)
				.getBoolean("isCleanReport", false);
		Log.i(TAGM, TAG + "------isCleanReport------" + isCleanReport);
		for (int i = 0; i < testlist.size(); i++) {
			// Log.i("AgingTest", TAG + "------i---A----" + i);
			TestItem item = testlist.get(i);
			if (isCleanReport) {

				getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit()
						.putBoolean(item.getClass().getName(), false).commit();

				String key = testlist.get(i).getClass().getName() + "isCheck";
				getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit()
						.putBoolean(key, false).commit();

			}

			item.isTestPass = getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE)
					.getBoolean(item.getClass().getName(), false);
			String key = testlist.get(i).getClass().getName() + "isCheck";
			item.isNeedTest = getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).getBoolean(key,
					false);
			testCheckbox.get(i).setChecked(item.isNeedTest);
			box_isCirculation.setChecked(getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE)
					.getBoolean("isCirculation", false));

		}

		test_view.setVisibility(View.GONE);
		button_start.setEnabled(true);

		updateUI();
	}

	public void updateUI() {
		Log.i(TAGM, TAG + "--updateUI()--testCheckbox.size()---->" + testCheckbox.size());
		for (int i = 0; i < testCheckbox.size(); i++) {
			CheckBox checkbox = testCheckbox.get(i);
			checkbox.setTextColor(testlist.get(i).isTestPass ? Color.GREEN : Color.RED);
			checkbox.setEnabled(false);
		}
		box_isCirculation.setEnabled(false);
		button_start.setEnabled(false);

		button_clear_all.setEnabled(false);
		button_select_all.setEnabled(false);
		button_setting.setEnabled(false);

		if (isInTest) {
			// stop.setEnabled(true);
		} else {

			for (int i = 0; i < testCheckbox.size(); i++) {
				testCheckbox.get(i).setEnabled(true);
			}
			box_isCirculation.setEnabled(true);
			button_start.setEnabled(true);
			button_select_all.setEnabled(true);
			button_clear_all.setEnabled(true);
			button_setting.setEnabled(true);
			// stop.setEnabled(false);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int index = -1;
		switch (buttonView.getId()) {
		case R.id.reboot:
			index = 0;
			break;
		case R.id.cpu:
			index = 1;
			break;
		case R.id.audio:
			index = 2;
			break;
		case R.id.test_2d:
			index = 3;
			break;
		case R.id.s3:
			index = 4;
			break;
		case R.id.emmc:
			index = 5;
			break;
		case R.id.video:
			index = 6;
			break;
		case R.id.test_3d:
			index = 7;
			break;
		case R.id.battery:
			index = 8;
			break;
		case R.id.memory:
			index = 9;
			break;
		case R.id.lcd:
			index = 10;
			break;
		case R.id.camera:
			index = 11;
			break;

		case R.id.iscirculation:
			isCirculation = isChecked;
			getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit()
					.putBoolean("isCirculation", isChecked).commit();
			break;
		}
		if (index != -1) {
			testlist.get(index).isNeedTest = isChecked;
			String key = testlist.get(index).getClass().getName() + "isCheck";
			Log.i(TAGM, TAG + "----key---->" + key);
			getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE).edit().putBoolean(key, isChecked)
					.commit();
		}
		updateUI();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			isInTest = true;
			getSharedPreferences(AgingTest.SAVE_DATA, Context.MODE_WORLD_WRITEABLE)
			.edit().putBoolean("isCleanReport", false).commit();
			mHandler.sendEmptyMessage(MSG_WAT_START);
			break;
		// case R.id.stop:
		case R.id.stop_testview:
			isInTest = false;
			mHandler.sendEmptyMessage(MSG_WAT_STOP);
			break;
		case R.id.select_all:
			selectAll();
			break;

		case R.id.clear_all:
			clearAll();
			break;

		case R.id.setting:
			Intent intent = new Intent(AgingTest.this, SetTestTimeActivity.class);
			startActivity(intent);
			break;
		case R.id.report:
			Intent mintent = new Intent(AgingTest.this, TestReportActivity.class);
			startActivity(mintent);
			break;
		}
		updateUI();
	}

	@Override
	protected void onDestroy() {
		Toast.makeText(mContext, "Del the video file", Toast.LENGTH_LONG).show();
		super.onDestroy();
		unregisterReceiver(StopTestBR);
		lock.reenableKeyguard();
		if (wakeLock.isHeld())
			wakeLock.release();
		new Thread(new Runnable() {

			@Override
			public void run() {
				File tmpFile = new File(videoPath);
				if (tmpFile.exists()) {
					tmpFile.delete();
					Log.i(TAGM, TAG + "----delect the Video file end---->");
				}
			}
		}).start();
	}

	long lastTime = 0;

	@Override
	public void onBackPressed() {

		long currentTime = System.currentTimeMillis();
		Log.i(TAGM, TAG + "----currentTime---->" + currentTime + "---lastTime--->" + lastTime);
		if (currentTime - lastTime > 2000) {
			lastTime = currentTime;
			Toast.makeText(this, R.string.back_toast, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!testlist.get(0).isInTest)
			mHandler.sendEmptyMessage(MSG_WAT_STOP);
		super.onBackPressed();
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true;
	 * 
	 * }
	 */

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// if (requestCode == SET_TEST_TIME_CODE) {
	// if (resultCode == RESULT_OK) {
	// Log.i("yuanluo", "------ok---------");
	// } else if (resultCode == RESULT_CANCELED) {
	// Log.i("yuanluo", "------cancel---------");
	// }
	// }
	// super.onActivityResult(requestCode, resultCode, data);
	// }

	public static ArrayList<TestItem> getList() {
		return testlist;
	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.test_time) { Intent intent = new Intent(AgingTest.this,
	 * SetTestTimeActivity.class); startActivity(intent); return true; } else if
	 * (id == R.id.test_report) { Intent intent = new Intent(AgingTest.this,
	 * TestReportActivity.class); startActivity(intent); return true; } return
	 * super.onOptionsItemSelected(item); }
	 */
	/*
	 * private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver()
	 * {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { // TODO
	 * Auto-generated method stub String action = intent.getAction(); if
	 * (Intent.ACTION_BATTERY_CHANGED.equals(action)) { mBatteryLevel =
	 * intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 100);
	 * bt.setBatteryLevel(mBatteryLevel); } } };
	 * 
	 * public int getBatteryPer(){ return mBatteryLevel; }
	 */

	private void AccessPermissions() {
		Log.i(TAGM, TAG + "----AccessPermissions() ---->");
		int intPermission = getSharedPreferences("PermissionUtil", Context.MODE_PRIVATE).getInt("result", 1);
		if (intPermission != 0) {
			Intent intent = new Intent(this, PermissionUtil.class);
			startActivity(intent);
		}
	}

	private void selectAll() {
		Log.i(TAGM, TAG + "----selectAll()---->");
		for (int i = 0; i < testCheckbox.size(); i++) {
			testCheckbox.get(i).setChecked(true);
		}
	}

	private void clearAll() {
		Log.i(TAGM, TAG + "----clearAll---->");
		for (int i = 0; i < testCheckbox.size(); i++) {
			testCheckbox.get(i).setChecked(false);
		}
	}

	private BroadcastReceiver StopTestBR = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAGM, TAG + "----StopTestBR---->");
			String action = intent.getAction();
			if (action.equals(stopTestBR)) {
				isInTest = false;
				mHandler.sendEmptyMessage(MSG_WAT_STOP);
				updateUI();
			}
		}

	};
}
