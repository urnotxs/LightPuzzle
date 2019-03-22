package com.xs.lightpuzzle.opengl.gllayer.filter.base;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.RawRes;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;
import com.xs.lightpuzzle.opengl.gllayer.util.ShaderHelper;

import java.nio.FloatBuffer;

/**
 * Created by xs on 2018/9/11.
 */
public abstract class AbstractFilter implements IFilter {
    protected Context mContext;
    protected int mProgramHandle;

    protected int aPositionLoc;
    protected int aTextureCoordLoc;
    protected int uMVPMatrixLoc;
    protected int uTexMatrixLoc;
    protected int mTextureLoc;

    protected int mIncomingWidth;
    protected int mIncomingHeight;
    protected float mRenderScale = 1.0f;

    public AbstractFilter(Context context) {
        mContext = context;
        mProgramHandle = createProgram(context);
        checkProgram();
    }

    public AbstractFilter(Context context, int programHandle) {
        mContext = context;
        mProgramHandle = programHandle;
        checkProgram();
    }

    public AbstractFilter(Context context, @RawRes int vertexSourceRawId, @RawRes int fragmentSourceRawId) {
        mContext = context;
        mProgramHandle = ShaderHelper.createProgram(context, vertexSourceRawId, fragmentSourceRawId);
        checkProgram();
    }

    public AbstractFilter(Context context, String vertexSource, String fragmentSource) {
        mContext = context;
        mProgramHandle = ShaderHelper.createProgram(vertexSource, fragmentSource);
        checkProgram();
    }

    protected void checkProgram() {
        if (mProgramHandle == 0) {
            throw new RuntimeException("Unable to create program");
        }
        getGLSLValues();
    }

    protected int createProgram(Context context) {
        return ShaderHelper.createProgram(context,
                R.raw.base_vertex_shader, R.raw.base_fragment_filter);
    }

    protected void getGLSLValues() {
        aPositionLoc = GLES20.glGetAttribLocation(mProgramHandle, "aPosition");
        aTextureCoordLoc = GLES20.glGetAttribLocation(mProgramHandle, "aTextureCoord");
        uMVPMatrixLoc = GLES20.glGetUniformLocation(mProgramHandle, "uMVPMatrix");
        uTexMatrixLoc = GLES20.glGetUniformLocation(mProgramHandle, "uTexMatrix");
        mTextureLoc = GLES20.glGetUniformLocation(mProgramHandle, "sourceImage");

    }

    @Override
    public void setTextureSize(int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
        if (width == mIncomingWidth && height == mIncomingHeight) {
            return;
        }
        mIncomingWidth = width;
        mIncomingHeight = height;
    }

    @Override
    public void setRenderScale(float renderScale) {
        mRenderScale = renderScale;
    }

    @Override
    public void onDraw(float[] mvpMatrix, float[] texMatrix, int textureId, PuzzleVideoBuffer buffer) {
        useProgram();

        bindTexture(textureId);

        bindGLSLValues(mvpMatrix, buffer.getVertexBuffer(), buffer.getCoordsPerVertex(), buffer.getVertexStride(),
                texMatrix, buffer.getTexCoordBuffer(), buffer.getTexCoordStride());

        drawArrays(0, buffer.getVertexCount());

        unbindGLSLValues();
        unbindTexture();
        disuseProgram();
    }

    protected void useProgram() {
        GLES20.glUseProgram(mProgramHandle);
    }



    /**
     * 绑定纹理
     */
    protected void bindTexture(int textureId){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(getTextureTarget(), textureId);
        GLES20.glUniform1i(mTextureLoc, 0);
    }

    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride,
                                  float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        GLES20.glUniformMatrix4fv(uMVPMatrixLoc, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(uTexMatrixLoc, 1, false, texMatrix, 0);
        GLES20.glEnableVertexAttribArray(aPositionLoc);
        GLES20.glVertexAttribPointer(aPositionLoc, coordsPerVertex,
                GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aTextureCoordLoc);
        GLES20.glVertexAttribPointer(aTextureCoordLoc, 2,
                GLES20.GL_FLOAT, false, texStride, texBuffer);
    }

    protected void drawArrays(int firstVertex, int vertexCount) {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, firstVertex, vertexCount);

    }

    protected void unbindGLSLValues() {
        GLES20.glDisableVertexAttribArray(aPositionLoc);
        GLES20.glDisableVertexAttribArray(aTextureCoordLoc);
    }

    protected void unbindTexture() {
        GLES20.glBindTexture(getTextureTarget(), 0);
    }

    protected void disuseProgram() {
        GLES20.glUseProgram(0);
    }

    @Override
    public void releaseProgram() {
        GLES20.glDeleteProgram(mProgramHandle);
        mProgramHandle = -1;
    }
}
