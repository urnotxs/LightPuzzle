package com.xs.lightpuzzle.opengl.gllayer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.Log;

import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.xs.lightpuzzle.BuildConfig.DEBUG;

public class ShaderHelper {
	private static final String TAG = "ShaderHelper";

	/** Identity matrix for general use.  Don't modify or life will get weird. */
	public static final float[] IDENTITY_MATRIX;

	static {
		IDENTITY_MATRIX = new float[16];
		Matrix.setIdentityM(IDENTITY_MATRIX, 0);
	}

	/**
	 * Helper function to compile a shader.
	 *
	 * @param shaderType   The shader type.
	 * @param shaderSource The shader source code.
	 * @return An OpenGL handle to the shader.
	 */
	public static int compileShader(final int shaderType, final String shaderSource) {
		int shaderHandle = GLES20.glCreateShader(shaderType);

		if (shaderHandle != 0) {
			// Pass in the shader source.
			GLES20.glShaderSource(shaderHandle, shaderSource);

			// Compile the shader.
			GLES20.glCompileShader(shaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) {
				Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
		}

		if (shaderHandle == 0) {
			throw new RuntimeException("Error creating shader.");
		}

		return shaderHandle;
	}

	/**
	 * Helper function to compile and link a program.
	 * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
	 * @return
	 */
	public static int createAndLinkProgram(
			final int vertexShaderHandle,
			final int fragmentShaderHandle) {
		return createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, null);
	}

	/**
	 * Helper function to compile and link a program.
	 *
	 * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
	 * @param attributes           Attributes that need to be bound to the program.
	 * @return An OpenGL handle to the program.
	 */
	public static int createAndLinkProgram(
			final int vertexShaderHandle,
			final int fragmentShaderHandle,
			final String[] attributes) {

		int programHandle = GLES20.glCreateProgram();

		if (programHandle != 0) {
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			// Bind attributes
			if (attributes != null) {
				final int size = attributes.length;
				for (int i = 0; i < size; i++) {
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}
			}

			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) {
				Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}

		if (programHandle == 0) {
			throw new RuntimeException("Error creating program.");
		}

		return programHandle;
	}

	/**
	 * raw 的资源创建着色器程序
	 * @param context
	 * @param vertexRawId
	 * @param fragmentRawId
	 * @return
	 */
	public static int createProgram(Context context,@RawRes int vertexRawId,@RawRes int fragmentRawId){
		String vertexShaderSource = raw2String(context, vertexRawId);
		String fragmentShaderSource = raw2String(context, fragmentRawId);
		return createProgram(vertexShaderSource, fragmentShaderSource);
	}

	public static int createProgram(String vertexShaderSource, String fragmentShaderSource){
		final int vertexShaderHandle =
				ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER,vertexShaderSource);
		final int fragmentShaderSourceHandle=
				ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderSource);

		return ShaderHelper.createAndLinkProgram(vertexShaderHandle,fragmentShaderSourceHandle);
	}

	/**
	 * 获取gl错误
	 * @param op
	 */
	public static void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("ES20_ERROR", op + ": glError " + error);
			if (DEBUG) {
				throw new RuntimeException(op + ": glError " + error);
			}
		}
	}

	/**
	 * @param textureTarget Texture类型。
	 *                      1. 相机用 GLES11Ext.GL_TEXTURE_EXTERNAL_OES
	 *                      2. 图片用GLES20.GL_TEXTURE_2D
	 * @param minFilter     缩小过滤类型 (1.GL_NEAREST ; 2.GL_LINEAR)
	 * @param magFilter     放大过滤类型
	 * @param wrapS         X方向边缘环绕
	 * @param wrapT         Y方向边缘环绕
	 * @return 返回创建的 Texture ID
	 */
	public static int createTexture(int textureTarget, @Nullable Bitmap bitmap, int minFilter,
									int magFilter, int wrapS, int wrapT) {
		int[] textureHandle = new int[1];

		GLES20.glGenTextures(1, textureHandle, 0);
		checkGlError("glGenTextures");
		GLES20.glBindTexture(textureTarget, textureHandle[0]);
		checkGlError("glBindTexture " + textureHandle[0]);
		GLES20.glTexParameterf(textureTarget, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(textureTarget, GLES20.GL_TEXTURE_MAG_FILTER, magFilter); //线性插值
		GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_S, wrapS);
		GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_T, wrapT);

		if (bitmap != null) {
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		}

		checkGlError("glTexParameter");
		return textureHandle[0];
	}

	public static int loadTexture(int textureTarget){
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		ShaderHelper.checkGlError("glGenTextures");
		GLES20.glBindTexture(textureTarget, texture[0]);
		GLES20.glTexParameterf(textureTarget, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(textureTarget, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR); //线性插值
		GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glBindTexture(textureTarget, 0);
		return texture[0];
	}

	public static int createTexture(int textureTarget) {
		return createTexture(textureTarget, (Bitmap) null, GLES20.GL_LINEAR, GLES20.GL_LINEAR,
				GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE);
	}

	public static int createTexture(int textureTarget, Bitmap bitmap) {
		return createTexture(textureTarget, bitmap, GLES20.GL_LINEAR, GLES20.GL_LINEAR,
				GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE);
	}

	public static int createTexture(int textureTarget, Bitmap bitmap, int oldTextureId) {
		if (oldTextureId > 0) {
			int[] textureHandle = new int[1];
			textureHandle[0] = oldTextureId;

			if (bitmap != null) {
				GLES20.glBindTexture(textureTarget, textureHandle[0]);
				checkGlError("glBindTexture " + textureHandle[0]);
				GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, bitmap);
				if (!bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
			return textureHandle[0];
		}
		return createTexture(textureTarget, bitmap, GLES20.GL_LINEAR, GLES20.GL_LINEAR,
				GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE);
	}

	/***
	 * 将 RGB 数据 转化 成纹理 ID
	 * @param videoW 纹理宽
	 * @param videoH 纹理高
	 * @param rgbData 视频桢的 RGB 数据
	 * @param textureId 因为视频根据帧率刷新，频繁调用onDrawFrame,
	 *                     所以不适合多次创建纹理资源ID，
	 *                     所以最好在onCreate创建好，免得OOM
	 * @return 绑定好 RGB 数据的纹理 ID
	 */
	public static int generateRGBTexture(int videoW, int videoH, byte[] rgbData, int textureId) {
		if (rgbData == null) {
			return -1;
		}

		ByteBuffer colorByteBuffer = null;

		if (colorByteBuffer == null) {
			colorByteBuffer = ByteBuffer.allocate(videoW * videoH * 4);
		}

		//生成纹理ID
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		if (colorByteBuffer != null) {
			colorByteBuffer.clear();
			colorByteBuffer.put(rgbData, 0, videoW * videoH * 4);
		} else {
			return -1;
		}

		colorByteBuffer.position(0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0,
				GLES20.GL_RGBA, videoW, videoH, 0,
				GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, colorByteBuffer);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

		return textureId;
	}

	public static PuzzleVideoBuffer getUnitBuffer(){
		float vertexPots[] = {
				-1.0f, -1.0f,   // 0 bottom left
				1.0f, -1.0f,   // 1 bottom right
				-1.0f, 1.0f,   // 2 top left
				1.0f, 1.0f,   // 3 top right
		};
		float texturePots[] = {
				0.0f, 0.0f,     // 0 bottom left
				1.0f, 0.0f,     // 1 bottom right
				0.0f, 1.0f,     // 2 top left
				1.0f, 1.0f      // 3 top right
		};
		return new PuzzleVideoBuffer(vertexPots , texturePots);
	}

	public static String raw2String(Context context, @RawRes int rawId) {
		if (context == null || rawId == -1) {
			return null;
		}

		String result = null;
		InputStream is = context.getResources().openRawResource(rawId);
		if (is != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
			byte[] buff = new byte[1024];
			int readSize;
			try {
				while ((readSize = is.read(buff)) > -1) {
					baos.write(buff, 0, readSize);
				}
				result = new String(baos.toByteArray());
			} catch (IOException e) {
				result = null;
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
