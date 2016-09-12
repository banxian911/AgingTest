package com.sprocomm.item;

import java.util.ArrayList;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import java.util.ArrayList;

import com.sprocomm.AgingTest.AgingTest;
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

	private static final String TAG = "SetTestTimeActivity";
	private final int TESTCOUNT = 7;
	private Button setOK;
	private Button setCancel;
	private EditText playVideoEdit;
	private EditText play3DEdit;
	private EditText lcd_vibrateEdit;
	private EditText spkEdit;
	private EditText mic_receiverEdit;
	private EditText cameraEdit;
	private EditText rebootEdit;
    private ArrayList<EditText> edit = new ArrayList<EditText>();
    private SharedPreferences mSharepreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_settings_view);

		playVideoEdit = (EditText) findViewById(R.id.video_time_Edit);
		play3DEdit = (EditText) findViewById(R.id.play_3d_time_Edit);
        lcd_vibrateEdit = (EditText) findViewById(R.id.lcd_vibrate_time_Edit);
        spkEdit = (EditText) findViewById(R.id.spk_time_Edit);
        mic_receiverEdit = (EditText) findViewById(R.id.mic_receiver_time_Edit);
        cameraEdit = (EditText) findViewById(R.id.camera_time_Edit);
        rebootEdit = (EditText) findViewById(R.id.reboot_time_Edit);
        
        mSharepreferences = getSharedPreferences(AgingTest.TEST_TIME, Context.MODE_PRIVATE);
        String test_time = mSharepreferences.getString("test_time", "30/30/30/30/30/30/30");
        edit.add(playVideoEdit);
        edit.add(play3DEdit);
        edit.add(lcd_vibrateEdit);
        edit.add(spkEdit);
        edit.add(mic_receiverEdit);
        edit.add(cameraEdit);
        edit.add(rebootEdit);
        
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
			Log.i("AgingTest",TAG + "---onClick--set_time---" + set_time.toString());
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
