package com.sprocomm.Earth3D;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public abstract class SpriteObj {
	private static final String TAG = "SpriteObj";
	public SpriteCallerInterface caller;
	public enum dataType{
		uniform,attribute
	}
	
	public static ESContext static_es_context = new ESContext();

	public float[] scale = new float[]{1,1,1};
	public float[] position = new float[]{0,0,0};
	public float[] angle = new float[]{0,0,0};
	protected float[] M_Matrix = new float[16]; 
	protected int vCount = 0;

	protected FloatBuffer vBuf;

	protected ESContext es_context;
	private Map<String, Integer> Handlers;
	
	public SpriteObj() {
		es_context = static_es_context;
		Handlers = new HashMap<String, Integer>();
		initDataHandler();
	}
	
	abstract protected void initData();
	abstract public void visit();
	abstract protected void initDataHandler();
	abstract protected void openDataHandler();
	
	
	protected final void initM_Matrix(){
		//M_Matrix = MatrixUtils.getUnitMatrix();
		Matrix.setRotateM(M_Matrix, 0, 0, 0, 1, 0);
		Matrix.translateM(M_Matrix, 0, position[0], position[1], position[2]);
		Matrix.scaleM(M_Matrix, 0, scale[0], scale[1], scale[2]);
		Matrix.rotateM(M_Matrix, 0, angle[0], 1, 0, 0);
		Matrix.rotateM(M_Matrix, 0, angle[1], 0, 1, 0);
		Matrix.rotateM(M_Matrix, 0, angle[2], 0, 0, 1);
	}
	
	public final void createProgram(String vPath, String fPath){
		es_context = new ESContext();
		OpenglUtils.getInstance().createProgram(vPath, fPath, es_context);
		Handlers.clear();
		initDataHandler();
	}
	
	public final static void createStaticProgram(String vPath, String fPath){
		OpenglUtils.getInstance().createProgram(vPath, fPath, static_es_context);
	}
	
	public final void pushHandler(dataType type, String name){
		
		//Log.i("Earth3D", TAG+ "--type-->"+type + "---name--->"+name);
		int input_index = -1;
		if (type == dataType.uniform) {
			input_index = GLES20.glGetUniformLocation(es_context.program, name);
		//	Log.i("Earth3D", TAG+ "--input_index-->"+input_index + "---name--->"+name);
		}else if(type == dataType.attribute) {
			input_index = GLES20.glGetAttribLocation(es_context.program, name);
		}
		if (input_index > -1) {
			//Log.i("Earth3D", TAG+"--name-->"+name + "---input_index--->"+input_index);
			Handlers.put(name, input_index);
		}else{
			//Log.e(name, String.valueOf(input_index));
			//Log.i("Earth3D", TAG+ "-name-A->"+name + "---input_index--->"+input_index);
		}
	}
	
	public static int sss_ = 0;
	
	public final int getHandler(String name){
		//Log.i("Earth3D", TAG+ "--name-->"+name + "---Handlers.get(name)--->"+Handlers.get(name));
	  return Handlers.get(name);	
	}

	public final FloatBuffer getvBuf() {
		return vBuf;
	}

	public final float[] getM_Matrix() {
		return M_Matrix;
	}
	
}
