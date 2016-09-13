package com.sprocomm.utils;

import java.io.IOException;

import android.media.MediaRecorder;
import android.util.Log;

public class ReceiverUtil implements IVoiceManager{

	private final String TAG = "ReceiverUtil";  
    private String path;  
    private MediaRecorder mRecorder;    
    public ReceiverUtil(String path){  
        this.path = path;  
        //mRecorder = new MediaRecorder();  
    }  
      
    /* 
     * 开始录音 
     * @return boolean 
     */  
    @Override  
    public boolean start() {    
    	mRecorder = new MediaRecorder(); 
        //设置音源为Micphone    
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);    
        //设置封装格式    
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); 
        Log.d("AgingTest", TAG + "---mRecorder path ---> " + path);
        mRecorder.setOutputFile(path);    
        //设置编码格式    
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);    
    
        try {    
            mRecorder.prepare();    
        } catch (IOException e) {    
            Log.d("AgingTest", TAG + "prepare() failed");
        }
        Log.d("AgingTest", TAG + "---mRecorder is start --- ");
        //录音  
        mRecorder.start();    
        return false;  
    }  
  
    /* 
     * 停止录音 
     * @return boolean 
     */  
    @Override  
    public boolean stop() {  
    	if (mRecorder != null) {
    		mRecorder.stop();    
            mRecorder.release(); 
		}
        mRecorder = null;   
        Log.d("AgingTest", TAG + "---mRecorder is stop --- ");
        return false;  
    }  
      
}
