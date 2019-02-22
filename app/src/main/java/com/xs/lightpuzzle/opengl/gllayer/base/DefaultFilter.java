package com.xs.lightpuzzle.opengl.gllayer.base;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.RawRes;

import java.nio.FloatBuffer;

/**
 * Created by xs on 2018/9/11.
 */
public class DefaultFilter extends AbstractFilter {

    public DefaultFilter(Context context) {
        super(context);
    }

    public DefaultFilter(Context context, int programHandle) {
        super(context, programHandle);
    }

    public DefaultFilter(Context context, @RawRes int vertexSourceRawId, @RawRes int fragmentSourceRawId) {
        super(context, vertexSourceRawId, fragmentSourceRawId);
    }

    public DefaultFilter(Context context, String vertexSource, String fragmentSource) {
        super(context, vertexSource, fragmentSource);
    }

    @Override
    public int getTextureTarget() {
        return GLES20.GL_TEXTURE_2D;
    }

    @Override
    protected void getGLSLValues() {
        super.getGLSLValues();

        // 获取着色器新增参数的句柄Location
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride,
                                  float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer,
                coordsPerVertex, vertexStride, texMatrix, texBuffer, texStride);

        // 为着色器新增的参数的句柄传入值 , 赋值
    }
}
