package com.xs.lightpuzzle.demo.a_opengl_surfaceview_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xs.lightpuzzle.opengl.gllayer.filter.DefaultFilter;
import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;
import com.xs.lightpuzzle.opengl.gllayer.util.CoordinateHelper;
import com.xs.lightpuzzle.opengl.gllayer.util.ShaderHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class TestRenderer implements GLSurfaceView.Renderer {
    private Context mContext;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private Bitmap bitmap;
    private int defaultTextureId;
    private DefaultFilter defaultFilter;
    private PuzzleVideoBuffer videoBuffer;

    public TestRenderer(Context context, Bitmap bitmap) {
        mContext = context;
        this.bitmap = bitmap;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 创建可以绘制纹理的基类Filter
        defaultFilter = new DefaultFilter(mContext);
        videoBuffer = CoordinateHelper.calculateUnitBuffer();
        // 创建纹理ID，如果频繁调用onDrawFrame(),则不可将ID创建放在其中，不及时销毁会爆内存的；
        defaultTextureId = ShaderHelper.createTexture(GLES20.GL_TEXTURE_2D, bitmap);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
        if (defaultFilter != null) {
            defaultFilter.onDraw(ShaderHelper.IDENTITY_MATRIX, ShaderHelper.IDENTITY_MATRIX,
                    defaultTextureId, videoBuffer);
        }
    }
}
