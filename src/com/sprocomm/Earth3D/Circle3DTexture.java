package com.sprocomm.Earth3D;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.sprocomm.AgingTest.Test3DActivity;
import com.sprocomm.AgingTest.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Circle3DTexture extends SpriteObj {
	private static final String TAG = "Circle3DTexture";
	public float r;
	public float angleSpan = 10.0f;
	
	private ShortBuffer indsBuf;
	private FloatBuffer texBuf;
	private int[] textureId = new int[]{-1, -1};
	
	public Circle3DTexture(float circle_r) {
		super();
		r = circle_r;
		initTexture(R.drawable.earth);
		initTexture(R.drawable.earthn);
		Log.i("Earth3D", TAG+ "--String.valueOf(textureId[0])-->"
		+String.valueOf(textureId[0]) + "---String.valueOf(textureId[1])--->"
				+String.valueOf(textureId[1]));
		initData();
	}

	@Override
	protected void initData() {
		int la_count,lo_count;
		la_count = (int) (180.0f/angleSpan + 1);
		lo_count = (int) (360.0f/angleSpan + 1);
		
		float[] vertixs = new float[la_count * lo_count * 3];
		float[] texCoords = new float[la_count * lo_count * 2];
		
		int tex_index = 0;
		int vertexs_index = 0;
		
		float laa_index = 0.0f;
		
		for (float la = -90; la <= 90; la+=angleSpan) {
			double la_radian = Math.toRadians(la);
			double la_cos = Math.cos(la_radian);
			double la_sin = Math.sin(la_radian);
			
			float loo_index = 0.0f;
			
			for (float lo = 0; lo <= 360; lo+=angleSpan) {
				double lo_radian = Math.toRadians(lo);
				double lo_cos = Math.cos(lo_radian);
				double lo_sin = Math.sin(lo_radian);
				
				vertixs[vertexs_index++] = (float) (r * la_cos * lo_cos);
				vertixs[vertexs_index++] = (float) (r * la_sin);
				vertixs[vertexs_index++] = (float) (r * la_cos * lo_sin);
				
				texCoords[tex_index++] = 1.0f - (float)(loo_index / (lo_count - 1));
				texCoords[tex_index++] = 1.0f - (float)(laa_index / (la_count - 1));
				
				loo_index++;
			}
			
			laa_index++;
		}
		
		Log.e("count", String.valueOf(la_count));
		Log.e("count", String.valueOf(lo_count));
		
		short[] inds = new short[la_count * lo_count * 6];
		int inds_index = 0;
		for (int lo_index = 0; lo_index < lo_count - 1; lo_index++) {
			short loo = (short) lo_index;
			short loo_next = (short) (lo_index + 1);
			
			for (int la_index = 0; la_index < la_count - 1; la_index++) {
				short laa = (short) (la_index * lo_count);
				short laa_next = (short) ((la_index+1) * lo_count);
				
				inds[inds_index++] = (short) (laa + loo);
				inds[inds_index++] = (short) (laa + loo_next);
				inds[inds_index++] = (short) (laa_next + loo_next);
				
				inds[inds_index++] = (short) (laa_next + loo_next);
				inds[inds_index++] = (short) (laa_next + loo);
				inds[inds_index++] = (short) (laa + loo);
				vCount += 6;
			}
		}
		
		vBuf = BufferUtils.getInstance().getBuf(vertixs);
		indsBuf = BufferUtils.getInstance().getBuf(inds);
		texBuf = BufferUtils.getInstance().getBuf(texCoords);
		
//		for (int i = 0; i < 99; i++) {
//			float out_f = texBuf.get(i);
//			Log.e("texBuf"+String.valueOf(i), String.valueOf(out_f));
//		}
		
		texBuf.position(0);
		if (caller != null) {
			caller.initData();
		}
	}

	@Override
	public void visit() {
		GLES20.glUseProgram(es_context.program);
		initM_Matrix();
		GLES20.glUniformMatrix4fv(getHandler("uMVPMatrix"), 1, false, MatrixUtils.getFinalMatrix(M_Matrix), 0);
		if (textureId[0] > -1) {
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
			GLES20.glUniform1i(getHandler("uTexture0"), 0);
		}
		if (textureId[1] > -1) {
			GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[1]);
			GLES20.glUniform1i(getHandler("uTexture1"), 1);
		}
		GLES20.glVertexAttribPointer(getHandler("aPosition"), 3, GLES20.GL_FLOAT, false, 3*4, vBuf);
		GLES20.glVertexAttribPointer(getHandler("aTexCoord"), 2, GLES20.GL_FLOAT, false, 2*4, texBuf);
		if (caller != null) {
			caller.visit();
		}
		openDataHandler();
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, vCount, GLES20.GL_UNSIGNED_SHORT, indsBuf);
	}

	@Override
	protected void initDataHandler() {
		if (caller != null) {
			caller.initDataHandler();
		}
		Log.i("Earth3D", TAG+ "--dataType.uniform-->"
				+dataType.uniform);
		pushHandler(dataType.uniform, "uMVPMatrix");
		pushHandler(dataType.uniform, "uTexture0");
		pushHandler(dataType.uniform, "uTexture1");
		
		pushHandler(dataType.attribute, "aPosition");
		pushHandler(dataType.attribute, "aTexCoord");
	}

	@Override
	protected void openDataHandler() {
		GLES20.glEnableVertexAttribArray(getHandler("aPosition"));
		GLES20.glEnableVertexAttribArray(getHandler("aTexCoord"));
		if (caller != null) {
			caller.openDataHandler();
		}
	}

	
	private void initTexture(int id){
		int texId = -1;
		if(textureId[0] == -1){
			GLES20.glGenTextures(1, textureId, 0);
			texId = textureId[0];
		}
		else if(textureId[0] > -1){
			GLES20.glGenTextures(1, textureId, 1);
			texId = textureId[1];
		}
//		Log.i("initTexture", String.valueOf(texId));
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		
		InputStream in_is = null;
		Bitmap bitmap = null;
		
		try {
			in_is = Test3DActivity.context.getResources().openRawResource(id);
			bitmap = BitmapFactory.decodeStream(in_is);
			Log.e("bitmap", String.valueOf(bitmap.getByteCount()));
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (bitmap != null)
				bitmap.recycle();
			try {
				if(in_is != null)
					in_is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
