package com.sprocomm.item;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Handler;

import com.sprocomm.R;
import com.sprocomm.utils.TestItem;

public class ReceiverTest extends TestItem {

	SoundPool pool = null;
	
	public ReceiverTest(Context context,long time,Handler handler) {
		mContext = context;
		testTime = time;
		mHandler = handler;
	}
	
	@Override
	public void startTest() {
		super.startTest();
		System.out.println("XIONG ----"+this.getClass().getName()+"-----start----");
		isInTest = true;
		isTestPass = false;
		isTestEnd = false;
		
		pool= new SoundPool(2,AudioManager.STREAM_VOICE_CALL,0);
		AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
		final int soundID  = pool.load(mContext, R.raw.pizzicato, 1);
		pool.setVolume(AudioManager.STREAM_VOICE_CALL, 1, 1);
		
		pool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				pool.play(soundID, 1, 1, 1, -1, 1);
			}
		});
	}

	@Override
	public void stopTest(boolean isPass) {
		super.stopTest(isPass);
		System.out.println("XIONG ----"+this.getClass().getName()+"-----stop----");
		isInTest = false;
		isTestPass = isPass;
		isTestEnd = isPass;
		
		if(pool != null){
			pool.pause(AudioManager.STREAM_MUSIC);
			pool.stop(AudioManager.STREAM_MUSIC);
			pool.release();
		}
		pool = null;
	}

}
