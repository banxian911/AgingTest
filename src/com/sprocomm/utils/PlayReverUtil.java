package com.sprocomm.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class PlayReverUtil implements IVoiceManager{

	private final String TAG = "playReverUtil";  
    private String path;  
      
    private MediaPlayer mPlayer;  
    public PlayReverUtil(String path){  
        this.path = path;  
       // mPlayer = new MediaPlayer();  
    }  
      
    @Override  
    public boolean start() {  
    		mPlayer = new MediaPlayer(); 
        try { 
        	if (mPlayer.isPlaying()) {
				mPlayer.reset();
			}
        	Log.d("AgingTest", TAG + "---ReverPlay path ---> " + path);
             //设置要播放的文件  
             mPlayer.setDataSource(path);  
             mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             mPlayer.setVolume(1, 1);
             mPlayer.prepare();  
             Log.d("AgingTest", TAG + "---ReverPlay is start --- ");
             //播放  
             mPlayer.start();         
         }catch(Exception e){  
             Log.d("AgingTest", TAG + "---prepare() failed ---> ");
         }  
  
        return false;  
    }  
  
    @Override  
    public boolean stop() {  
    	Log.d("AgingTest", TAG + "---ReverPlay is stop --- ");
    	if (mPlayer != null ) {
    		mPlayer.stop();  
            mPlayer.release();     
		}
    	mPlayer = null;
        return false;  
    }  
  
}
