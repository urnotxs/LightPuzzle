package com.xs.lightpuzzle.opengl.gllayer.renderer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.RendererCallBack;
import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;
import com.xs.lightpuzzle.opengl.gllayer.filter.DefaultFilter;
import com.xs.lightpuzzle.opengl.gllayer.filter.FrameFilter;
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

public class AdjustmentRenderer extends BaseRenderer {
    private RendererCallBack mCallBack;

    public void setRendererCallBack(RendererCallBack callBack) {
        this.mCallBack = callBack;
    }

    private DefaultFilter mDefaultFilter;
    private FrameFilter mFrameFilter;
    private SurfaceTexture mSurfaceTexture;

    private PuzzleVideoBuffer mBuffer;
    protected float[] mTexturePots, mVertexPots;
    private float[] mTexMatrix = new float[16];

    public AdjustmentRenderer(Context context) {
        super(context);
        Matrix.setIdentityM(mTexMatrix, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        mBuffer = CoordinateHelper.calculateDefaultBuffer();

        mDefaultFilter = new DefaultFilter(mContext);

        mFrameFilter = new FrameFilter(mContext,
                R.raw.base_vertex_shader, R.raw.base_fragment_filter);

        mSurfaceTexture = new SurfaceTexture(mFrameFilter.getOESId());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        mFrameFilter.setTextureSize(mViewWidth, mViewHeight);
        if (mCallBack != null) {
            mCallBack.onSurfaceCreated(mSurfaceTexture);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mTexMatrix);
        mFrameFilter.onDrawOES();
        GLES20.glClearColor(0.43529f,0.3412f,0.3686f,1);
        mDefaultFilter.onDraw(ShaderHelper.IDENTITY_MATRIX,
                ShaderHelper.IDENTITY_MATRIX, mFrameFilter.getTextureId(), mBuffer);
    }

    public void setVerTexPots(float[] verTexPots) {
        mVertexPots = verTexPots;
        mBuffer = new PuzzleVideoBuffer(mVertexPots, mTexturePots);
    }
}
