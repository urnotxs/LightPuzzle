package com.xs.lightpuzzle.opengl.gllayer.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;
import com.xs.lightpuzzle.opengl.gllayer.filter.DefaultFilter;
import com.xs.lightpuzzle.opengl.gllayer.renderer.base.BaseRenderer;
import com.xs.lightpuzzle.opengl.gllayer.util.CoordinateHelper;
import com.xs.lightpuzzle.opengl.gllayer.util.ShaderHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class DefaultRenderer extends BaseRenderer {
    private Bitmap bitmap;
    private int defaultTextureId;
    private DefaultFilter defaultFilter;
    private PuzzleVideoBuffer videoBuffer;
    private float[] vertexMatrix = new float[16];

    public DefaultRenderer(Context context) {
        super(context);
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        // 创建可以绘制纹理的基类Filter
        defaultFilter = new DefaultFilter(mContext);
        videoBuffer = CoordinateHelper.calculateDefaultBuffer();
        // 创建纹理ID，如果频繁调用onDrawFrame(),则不可将ID创建放在其中，不及时销毁会爆内存的；
        defaultTextureId = ShaderHelper.createTexture(GLES20.GL_TEXTURE_2D, bitmap);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        calculateVertexMatrix();
    }

    private void calculateVertexMatrix() {
        Matrix.setIdentityM(vertexMatrix, 0);
        float bmpRatio = bitmap.getWidth() * 1.0f / bitmap.getHeight();
        float viewRatio = mViewWidth * 1.0f / mViewHeight;
        if (viewRatio < bmpRatio){
            // 齐高放大宽
            float scale = bmpRatio / viewRatio;
            Matrix.scaleM(vertexMatrix, 0, scale, -1.0f, 1.0f);
        }else{
            // 齐宽放大高
            float scale = viewRatio / bmpRatio;
            Matrix.scaleM(vertexMatrix, 0, 1.0f, -scale, 1.0f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glViewport(0, 0, mViewWidth, mViewHeight);
        if (defaultFilter != null) {
            defaultFilter.onDraw(vertexMatrix, ShaderHelper.IDENTITY_MATRIX,
                    defaultTextureId, videoBuffer);
        }
    }
}
