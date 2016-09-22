package com.sprocomm.AgingTest;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import java.util.ArrayList;

import com.sprocomm.AgingTest.R;
import android.graphics.Color;

import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

public class TestReportActivity extends Activity implements OnClickListener {

	private static final String TAG = "TestReportActivity";
	private static final String TAGM = "AgingTest";
	private final int TESTCOUNT = 7;
	private Button setOK;
	private Button setCancel;
	private TextView playVideoText;
	private TextView play3DText;
	private TextView lcd_vibrateText;
	private TextView spkText;
	private TextView mic_receiverText;
	private TextView cameraText;
	private TextView rebootText;
	private ArrayList<TextView> edit = new ArrayList<TextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_report_view);

		playVideoText = (TextView) findViewById(R.id.video_report_text);
		play3DText = (TextView) findViewById(R.id.play_3d_report_text);
		lcd_vibrateText = (TextView) findViewById(R.id.lcd_vibrate_report_text);
		spkText = (TextView) findViewById(R.id.spk_report_text);
		mic_receiverText = (TextView) findViewById(R.id.mic_receiver_report_text);
		cameraText = (TextView) findViewById(R.id.camera_report_text);
		rebootText = (TextView) findViewById(R.id.reboot_report_text);

		edit.add(playVideoText);
		edit.add(play3DText);
		edit.add(lcd_vibrateText);
		edit.add(spkText);
		edit.add(mic_receiverText);
		edit.add(cameraText);
		edit.add(rebootText);

		setOK = (Button) findViewById(R.id.ok);
		setCancel = (Button) findViewById(R.id.cancel);
		setOK.setOnClickListener(this);
		setCancel.setOnClickListener(this);
		
		if (TESTCOUNT == AgingTest.getList().size()) {
			for (int i = 0; i < TESTCOUNT; i++) {
				boolean testResult = AgingTest.getList().get(i).isTestPass;
				Log.i(TAGM,TAG + "----testResult--->" + testResult );
				edit.get(i).setText(testResult ? R.string.TestPass: R.string.noTest);
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
