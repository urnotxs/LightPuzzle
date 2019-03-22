package com.xs.lightpuzzle.opengl.gllayer.renderer.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public abstract class BaseRenderer implements GLSurfaceView.Renderer {
    protected Context mContext;
    protected float mRatio;
    protected int mViewWidth, mViewHeight;
    protected final float[] projectionMatrix = new float[16];

    public BaseRenderer(Context context) {
        mContext = context;
        Matrix.setIdentityM(projectionMatrix, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mViewWidth = width;
        mViewHeight = height;
        mRatio = (mViewHeight > mViewWidth ? mViewHeight : mViewWidth) * 1.0f
                / (mViewHeight < mViewWidth ? mViewHeight : mViewWidth);
        setProjectionMatrix(width, height);
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    /**
     * 根据屏幕的 width 和 height 创建投影矩阵
     * 当宽度大于高度的时候, 如果想以高度为-1f 到 1f 为基准, 则宽度是 -1f * aspectRatio 到 1f * aspectRatio
     * 反之亦然
     *
     * @param width
     * @param height
     */
    private void setProjectionMatrix(int width, int height) {
        final float aspectRatio = width > height ?
                (float) width / (float) height : (float) height / (float) width;

        if (width > height) {
            Matrix.orthoM(
                    projectionMatrix, 0,
                    -aspectRatio, aspectRatio,
                    -1, 1,
                    -1, 1);
        } else {
            Matrix.orthoM(
                    projectionMatrix, 0,
                    -1, 1,
                    -aspectRatio, aspectRatio,
                    -1, 1);
        }
    }

}
