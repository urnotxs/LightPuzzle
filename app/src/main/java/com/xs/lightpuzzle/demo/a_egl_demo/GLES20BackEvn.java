package com.xs.lightpuzzle.demo.a_egl_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.opengl.gllayer.base.AbstractFilter;
import com.xs.lightpuzzle.opengl.gllayer.base.DefaultFilter;
import com.xs.lightpuzzle.opengl.gllayer.util.ShaderHelper;

import java.nio.IntBuffer;

/**
 * Created by xs on 2019/1/24.
 */

public class GLES20BackEvn {
    final static String TAG = "GLES20BackEnv";
    private int mWidth;
    private int mHeight;
    private EGLHelper mEGLHelper;
    String mThreadOwner;
    private AbstractFilter mFilter;
    private int textureId;
    private Bitmap bitmap;


    public GLES20BackEvn(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mEGLHelper = new EGLHelper();
        mEGLHelper.eglInit(width, height);
    }

    public void setThreadOwner(String threadOwner) {
        mThreadOwner = threadOwner;
    }

    public void initFilter(Context context) {
        mFilter = new DefaultFilter(context, R.raw.base_vertex_shader, R.raw.gray_fragment_filter);
        textureId = ShaderHelper.createTexture(GLES20.GL_TEXTURE_2D, bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        if (mFilter == null) {
            Log.e(TAG, "getBitmap: Renderer was not set.");
            return null;
        }
        if (!Thread.currentThread().getName().equals(mThreadOwner)) {
            Log.e(TAG, "getBitmap: This thread does not own the OpenGL context.");
            return null;
        }
        mFilter.onDraw(ShaderHelper.IDENTITY_MATRIX, ShaderHelper.IDENTITY_MATRIX,
                textureId, ShaderHelper.getUnitBuffer());
        return convertToBitmap();
    }

    private Bitmap convertToBitmap() {
        int[] intArrDst = new int[mWidth * mHeight];
        IntBuffer intBuffer = IntBuffer.allocate(mWidth * mHeight);
        mEGLHelper.mGL.glReadPixels(0, 0, mWidth, mHeight,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, intBuffer);
        int[] intArrSrc = intBuffer.array();

        for (int i = 0; i < mHeight; i++) {
            System.arraycopy(intArrSrc, i * mWidth,
                    intArrDst, i * mWidth, mWidth);
        }
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(intArrDst));
        return bitmap;
    }
}
