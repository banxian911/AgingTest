package com.sprocomm.AgingTest;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.TextView;

public class TestReportActivity extends Activity implements OnClickListener {

	private static final String TAG = "TestReportActivity";
	private static final String TAGM = "AgingTest";
	private final int TESTCOUNT = 7;
	private Button setOK;
	private Button setCancel;

	private ListView mListView;
	private ArrayList<String> mArrayList = new ArrayList<String>();
	private TextView reboot;
	private TextView cpu;
	private TextView audio;
	private TextView text2d;
	private TextView s3;
	private TextView emmc;
	private TextView video;
	private TextView test3d;
	private TextView battery;
	private TextView memory;
	private TextView lcd;
	private TextView camera;
	private ArrayList<TextView> edit = new ArrayList<TextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_report_view);

		initUI();

		if (TESTCOUNT == AgingTest.getList().size()) {
			for (int i = 0; i < TESTCOUNT; i++) {
				boolean testResult = AgingTest.getList().get(i).isTestPass;
				Log.i(TAGM, TAG + "----testResult--->" + testResult);
				edit.get(i).setText(testResult ? R.string.TestPass : R.string.noTest);
				edit.get(i).setTextColor(testResult ? Color.GREEN : Color.RED);

			}
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		// testlist.get(i).getClass().getName()
		if (view.getId() == R.id.ok) {
			finish();
		}
		if (view.getId() == R.id.cancel) {
			finish();
		}

	}
	
	private void initUI(){
		reboot = (TextView) findViewById(R.id.reboot_report_text);
		cpu = (TextView) findViewById(R.id.cpu_report_text);
		audio = (TextView) findViewById(R.id.audio_report_text);
		text2d = (TextView) findViewById(R.id.test_2d_report_text);
		s3 = (TextView) findViewById(R.id.s3_report_text);
		emmc = (TextView) findViewById(R.id.emmc_report_text);
		video = (TextView) findViewById(R.id.video_report_text);
		test3d = (TextView) findViewById(R.id.test_3d_report_text);
		battery = (TextView) findViewById(R.id.battery_report_text);
		memory = (TextView) findViewById(R.id.memory_report_text);
		lcd = (TextView) findViewById(R.id.lcd_report_text);
		camera = (TextView) findViewById(R.id.camera_report_text);

		edit.add(reboot);
		edit.add(cpu);
		edit.add(audio);
		edit.add(text2d);
		edit.add(s3);
		edit.add(emmc);
		edit.add(video);

		edit.add(test3d);
		edit.add(battery);
		edit.add(memory);
		edit.add(lcd);
		edit.add(camera);


		setOK = (Button) findViewById(R.id.ok);
		setCancel = (Button) findViewById(R.id.cancel);
		setOK.setOnClickListener(this);
		setCancel.setOnClickListener(this);
	}

}
