package com.sprocomm.item;

import java.util.ArrayList;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import java.util.ArrayList;

import com.sprocomm.AgingTest;
import com.sprocomm.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;



public class SetTestTimeActivity extends Activity implements OnClickListener {

	private final int TESTCOUNT = 7;
	private Button setOK;
	private Button setCancel;
	private EditText reboot;
	private EditText sleep;
	private EditText vibrate;
	private EditText receiver;
	private EditText takepicture;
	private EditText playvideo;
	private EditText batteryEditText;
    private ArrayList<EditText> edit = new ArrayList<EditText>();
    private SharedPreferences mSharepreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_settings_view);

        reboot = (EditText) findViewById(R.id.rebootEdit);
        sleep = (EditText) findViewById(R.id.sleepEdit);
        vibrate = (EditText) findViewById(R.id.vibrateEdit);
        receiver = (EditText) findViewById(R.id.receiverEdit);
        takepicture = (EditText) findViewById(R.id.takingEdit);
        playvideo = (EditText) findViewById(R.id.videoEdit);
        batteryEditText = (EditText) findViewById(R.id.batteryEdit);
        
        mSharepreferences = getSharedPreferences(AgingTest.TEST_TIME, Context.MODE_PRIVATE);
        String test_time = mSharepreferences.getString("test_time", "20/20/20/20/20/20/20");
        edit.add(reboot);
        edit.add(sleep);
        edit.add(vibrate);
        edit.add(receiver);
        edit.add(takepicture);
        edit.add(playvideo);
        edit.add(batteryEditText);
        
		String[] test_time_eachS = test_time.split("/");
		for (int i=0; i<test_time_eachS.length; i++) {
			edit.get(i).setText(test_time_eachS[i]);
		}
		
        setOK = (Button) findViewById(R.id.ok);
		setCancel = (Button) findViewById(R.id.cancel);
		setOK.setOnClickListener(this);
		setCancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		//testlist.get(i).getClass().getName()
		if (view.getId() == R.id.ok) {
			StringBuilder set_time = new StringBuilder();

			if (TESTCOUNT == AgingTest.getList().size()) {
				for (int i=0; i<TESTCOUNT; i++) {
					int time = 20;
					if (!edit.get(i).getText().toString().isEmpty()) {
						time = Integer.parseInt(edit.get(i).getText().toString());
					} else {
						time = 20;
					}
					AgingTest.getList().get(i).setTestTime(time);
					set_time.append(time);
					if (i < TESTCOUNT-1) {
						set_time.append("/");
					}
				}
			}
			Log.i("yuanluo", "---onClick--set_time---" + set_time.toString());
			Editor edit = mSharepreferences.edit();
			edit.putString("test_time", set_time.toString());
			edit.commit();
			finish();
		}
		if (view.getId() == R.id.cancel) {
			finish();
		}

	}
}
