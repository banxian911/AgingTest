package com.sprocomm.item;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import java.util.ArrayList;

import com.sprocomm.AgingTest.AgingTest;
import com.sprocomm.R;
import android.graphics.Color;

import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

public class TestReportActivity extends Activity implements OnClickListener {

	private final int TESTCOUNT = 7;
	private Button setOK;
	private Button setCancel;
	private TextView reboot;
	private TextView sleep;
	private TextView vibrate;
	private TextView receiver;
	private TextView takepicture;
	private TextView playvideo;
	private TextView batteryTextView;
	private ArrayList<TextView> edit = new ArrayList<TextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_report_view);

		reboot = (TextView) findViewById(R.id.rebootEdit);
		sleep = (TextView) findViewById(R.id.sleepEdit);
		vibrate = (TextView) findViewById(R.id.vibrateEdit);
		receiver = (TextView) findViewById(R.id.receiverEdit);
		takepicture = (TextView) findViewById(R.id.takingEdit);
		playvideo = (TextView) findViewById(R.id.videoEdit);
		batteryTextView = (TextView) findViewById(R.id.batteryEdit);

		edit.add(reboot);
		edit.add(sleep);
		edit.add(vibrate);
		edit.add(receiver);
		edit.add(takepicture);
		edit.add(playvideo);
		edit.add(batteryTextView);

		setOK = (Button) findViewById(R.id.ok);
		setCancel = (Button) findViewById(R.id.cancel);
		setOK.setOnClickListener(this);
		setCancel.setOnClickListener(this);
		
		if (TESTCOUNT == AgingTest.getList().size()) {
			for (int i = 0; i < TESTCOUNT; i++) {
				boolean testResult = AgingTest.getList().get(i).isTestPass;
				Log.i("yuanluo", "----i----" + testResult);
				edit.get(i).setText(testResult ? "PASS" : "FAIL");
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

}
