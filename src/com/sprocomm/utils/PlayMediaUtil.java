package com.sprocomm.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;

public class PlayMediaUtil implements IVoiceManager{

	private final String TAG = "PlayMediaUtil";  
    private String path;
    private Uri uri;
    
    private int music_time;
    private Boolean is = false;
    
    private MediaPlayer mPlayer;  
    public PlayMediaUtil(String path){  
        this.path = path;  
        mPlayer = new MediaPlayer(); 
    	Log.d("AgingTest", TAG + "---ReverPlay path ---> " + path);
        //设置要播放的文件  
        try {
			mPlayer.setDataSource(path);
		}  catch (Exception e) {
			Log.d("AgingTest", TAG + "---prepare() failed ---> ");
		}
    }
    
    public PlayMediaUtil(Context context,Uri uri) {
		// TODO Auto-generated constructor stub
    	this.uri = uri;
    	
    	mPlayer = new MediaPlayer();
    	try {
			mPlayer.setDataSource(context, uri);
		} catch (Exception e) {
			Log.d("AgingTest", TAG + "---prepare() failed ---> ");
		}
	}
    
    @Override  
    public boolean start() {  
    	Log.d("AgingTest", TAG + "---mPlayer ---> "+mPlayer);
    	if (mPlayer !=null) {	
    		if (mPlayer.isPlaying()) {
				mPlayer.reset();
			}
             mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             mPlayer.setVolume(1, 1);
			try {
				mPlayer.prepare(); // 同步装载资源
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("AgingTest", TAG + "---prepare() failed --1-> ");
			}
             mPlayer.start();  
            /* //通过异步方式装载媒体资源
             mPlayer.prepareAsync();
             mPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					 Log.d("AgingTest", TAG + "---ReverPlay is start --- ");
		             //播放  
		             mPlayer.start();   
				}
			});*/
		}else {
			Log.d("AgingTest", TAG + "---mPlayer is ---> " + mPlayer);
		}	
        return false;  
    }  
    
    @Override  
    public boolean stop() {  
    	Log.d("AgingTest", TAG + "---ReverPlay is stop --- ");
    	if (mPlayer != null ) {
    		mPlayer.stop();  
            mPlayer.release();
            mPlayer = null;
		}
        return false;  
    }  
    
    public void PlayRepeat(){
    	Log.d("AgingTest", TAG + "---PlayRepeat---mPlay---> " +mPlayer);
    	if (mPlayer !=null && mPlayer.isPlaying()) {
    		Log.d("AgingTest", TAG + "---PlayRepeat---");
			mPlayer.setLooping(true);
		}
    	
    }
  
    public int MusicTime(){
    	if (mPlayer!=null && mPlayer.isPlaying()) {
			music_time = mPlayer.getDuration(); 
		}
    	Log.d("AgingTest", TAG + "---music_time---");
    	return music_time;
    }
    
    public Boolean isPlayEnd(){
    	if (mPlayer !=null && mPlayer.isPlaying()) {
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					is = true;
				}
			});
		}
    	return is;
    }
    
}
