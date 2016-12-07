package com.sprocomm.AgingTest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;



public class SetTestTimeActivity extends Activity implements OnClickListener {

	private static final String TAG = "SetTestTimeActivity";
	private final int TESTCOUNT = 12;
	private Button setOK;
	private Button setCancel;
	
	private EditText cpuEdit;
	private EditText memoryEdit;
	private EditText test2DEdit;
	private EditText play3DEdit;
	private EditText audioEdit;
	private EditText playVideoEdit;
	private EditText lcdEdit;
	private EditText emmcEdit;
	private EditText s3Edit;
	private EditText batteryEdit;
	private EditText cameraEdit;
	private EditText rebootEdit;
	
    private ArrayList<EditText> edit = new ArrayList<EditText>();
    private SharedPreferences mSharepreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_settings_view);

		initUI();
        
        mSharepreferences = getSharedPreferences(AgingTest.TEST_TIME, Context.MODE_PRIVATE);
        String test_time = mSharepreferences.getString("test_time", "30/30/30/30/30/30/30/30/30/30/30/1");
        edit.add(rebootEdit);
        edit.add(cpuEdit);
        edit.add(audioEdit);
        edit.add(test2DEdit);
        edit.add(s3Edit);
        edit.add(emmcEdit);
        edit.add(playVideoEdit);
        edit.add(play3DEdit);
        edit.add(batteryEdit);
        edit.add(memoryEdit);
        edit.add(lcdEdit);
        edit.add(cameraEdit);
              
		String[] test_time_eachS = test_time.split("/");
		for (int i=0; i<test_time_eachS.length; i++) {
			if (i == 11) {
				int time = Integer.parseInt(test_time_eachS[i]);
				edit.get(i).setText(""+time/60);
			}else {
				edit.get(i).setText(test_time_eachS[i]);
			}
			
		}
		
        setOK = (Button) findViewById(R.id.ok);
		setCancel = (Button) findViewById(R.id.cancel);
		setOK.setOnClickListener(this);
		setCancel.setOnClickListener(this);
		
	}

	private void initUI(){
		 cpuEdit = (EditText) findViewById(R.id.cpu_time_Edit);
		 memoryEdit = (EditText) findViewById(R.id.memory_time_Edit);
		 test2DEdit = (EditText) findViewById(R.id.test_2d_time_Edit);
		 play3DEdit = (EditText) findViewById(R.id.test_3d_time_Edit);
		 audioEdit = (EditText) findViewById(R.id.audio_time_Edit);
		 playVideoEdit = (EditText) findViewById(R.id.video_time_Edit);
		 lcdEdit = (EditText) findViewById(R.id.lcd_time_Edit);
		 emmcEdit = (EditText) findViewById(R.id.emmc_time_Edit);
		 s3Edit = (EditText) findViewById(R.id.s3_time_Edit);
		 batteryEdit = (EditText) findViewById(R.id.battery_time_Edit);
		
        rebootEdit = (EditText) findViewById(R.id.reboot_time_Edit);
        
        cameraEdit = (EditText) findViewById(R.id.camera_time_Edit);
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
					if (edit.get(i).equals(cameraEdit)) {
						time = time * 60;
						Log.i("AgingTest",TAG + "---time---" + time +"--edit.get(i)--" +edit.get(i));
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
