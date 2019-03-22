package com.xs.lightpuzzle.opengl.gllayer.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;
import com.xs.lightpuzzle.opengl.gllayer.filter.base.AbstractFilter;
import com.xs.lightpuzzle.opengl.gllayer.util.CoordinateHelper;
import com.xs.lightpuzzle.opengl.gllayer.util.GLFramebuffer;
import com.xs.lightpuzzle.opengl.gllayer.util.ShaderHelper;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class FrameFilter extends AbstractFilter{
    private int mOESId;
    private PuzzleVideoBuffer buffer;
    private GLFramebuffer mFramebuffer;

    public FrameFilter(Context context) {
        super(context);
        init();
    }

    public FrameFilter(Context context, int vertexSourceRawId, int fragmentSourceRawId) {
        super(context, vertexSourceRawId, fragmentSourceRawId);
        init();
    }

    public void init(){
        mOESId = ShaderHelper.loadTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
        buffer = CoordinateHelper.calculateUnitBuffer();
    }

    public void onDrawOES(){
        mFramebuffer.bind(true);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        onDraw(ShaderHelper.IDENTITY_MATRIX, ShaderHelper.IDENTITY_MATRIX, mOESId, buffer);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void setTextureSize(int width, int height) {
        super.setTextureSize(width, height);
        if (mFramebuffer != null && (mFramebuffer.getWidth() != width
                || mFramebuffer.getHeight() != height)) {
            mFramebuffer.destroy();
            mFramebuffer = null;
        }
        mFramebuffer = new GLFramebuffer(width, height);
    }

    @Override
    public int getTextureTarget() {
        return GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
    }

    public int getOESId(){
        return mOESId;
    }

    public int getTextureId(){
        if (mFramebuffer != null) {
            return mFramebuffer.getTextureId();
        }
        return 0;
    }
}
