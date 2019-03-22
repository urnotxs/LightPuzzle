package com.xs.lightpuzzle.opengl.gllayer.util;

import android.opengl.GLES20;

import java.nio.Buffer;

/**
 * Created by zwq on 2016/07/26 17:33.<br/><br/>
 * v2: 20180606，在GLFramebufferV1版基础上优化
 * 一个Framebuffer 对应一个纹理
 */
public class GLFramebuffer {

    private int mNum;
    private int mWidth, mHeight;
    private int[] mFrameBuffers;
    private int[] mFrameBuffersTextures;
    private int[] mColorBuffers;
    private int[] mDepthBuffers;
    private int[] mStencilBuffers;
    private int mClearMask;

    private int mCurrentTextureIndex = -1;
    private int mPreviousTextureIndex = -1;
    private boolean mHasBindFramebuffer;
    private boolean mHasBind;

    public GLFramebuffer(int width, int height) {
        this(1, width, height);
    }

    public GLFramebuffer(int num, int width, int height) {
        this(num, width, height, false, false, false);
    }

    public GLFramebuffer(int num, int width, int height, boolean color, boolean depth, boolean stencil) {
        this(num, width, height, color, depth, stencil, GLES20.GL_RGBA);
    }

    public GLFramebuffer(int num, int width, int height, int format) {
        this(num, width, height, false, false, false, format);
    }

    /**
     * @param num    数量
     * @param width
     * @param height
     */
    public GLFramebuffer(int num, int width, int height, boolean color, boolean depth, boolean stencil, int format) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        }
        mNum = num;
        mWidth = width;
        mHeight = height;

        if (mNum < 1) {
            mNum = 1;
        }
        mClearMask = GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT;
        //mClearMask = GLES20.GL_COLOR_BUFFER_BIT;

        mFrameBuffers = new int[mNum];
        mFrameBuffersTextures = new int[mNum];
        if (color) {
            mColorBuffers = new int[mNum];
        }
        if (depth) {
            mDepthBuffers = new int[mNum];
            //mClearMask |= GLES20.GL_DEPTH_BUFFER_BIT;
        }
        if (stencil) {
            mStencilBuffers = new int[mNum];
            //mClearMask |= GLES20.GL_STENCIL_BUFFER_BIT;
        }
        generateBufferAndTexture(mNum, format, format, GLES20.GL_UNSIGNED_BYTE, null);
    }

    private void generateBufferAndTexture(int num, int internalFormat, int format, int type, Buffer pixels) {
        //framebuffer
        GLES20.glGenFramebuffers(num, mFrameBuffers, 0);
        GLES20.glGenTextures(num, mFrameBuffersTextures, 0);
        for (int index = 0; index < num; index++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBuffersTextures[index]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, internalFormat, mWidth, mHeight, 0, format, type, pixels);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[index]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mFrameBuffersTextures[index], 0);

            if (mColorBuffers != null) {
                GLES20.glGenRenderbuffers(1, mColorBuffers, index);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mColorBuffers[index]);
                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_RGBA4, mWidth, mHeight);
            }
            if (mDepthBuffers != null) {
                GLES20.glGenRenderbuffers(1, mDepthBuffers, 0);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mDepthBuffers[index]);
                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, mWidth, mHeight);
            }
            if (mStencilBuffers != null) {
                GLES20.glGenRenderbuffers(1, mStencilBuffers, index);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mStencilBuffers[index]);
                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_STENCIL_INDEX8, mWidth, mHeight);
            }

            // bind RenderBuffers to FrameBuffer object
            if (mColorBuffers != null) {
                GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_RENDERBUFFER, mColorBuffers[index]);
            }
            if (mDepthBuffers != null) {
                GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, mDepthBuffers[index]);
            }
            if (mStencilBuffers != null) {
                GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_STENCIL_ATTACHMENT, GLES20.GL_RENDERBUFFER, mStencilBuffers[index]);
            }
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
    }

    public int getNum() {
        return mNum;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void reset() {
        mCurrentTextureIndex = -1;
        mPreviousTextureIndex = -1;
        mHasBindFramebuffer = false;
        mHasBind = true;
    }

    private int checkIndex(int index) {
        if (index < 0) {
            return 0;
        } else if (index >= mNum) {
            return mNum - 1;
        }
        return index;
    }

    public int getCurrentTextureIndex() {
        return mCurrentTextureIndex;
    }

    public boolean bindByIndex(int index, boolean clear) {
        return bindByIndex(index, clear, true);
    }

    public boolean bindByIndex(int index, boolean clear, boolean reset) {
        index = checkIndex(index);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[index]);
        if (clear) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            if (mClearMask > 0) {
                GLES20.glClear(mClearMask);
            }
        }

        if (reset) {
            mPreviousTextureIndex = mCurrentTextureIndex;
            mCurrentTextureIndex = index;
            mHasBindFramebuffer = true;
            mHasBind = true;
        }
        return true;
    }

    public boolean bindNext(boolean clear) {
        return bindByIndex((mCurrentTextureIndex + 1) % mNum, clear);
    }

    public boolean bind(boolean clear) {
        return bindByIndex(0, clear);
    }

    public boolean rebind(boolean clear) {
        return bindByIndex(mCurrentTextureIndex, clear, false);
    }

    public boolean hasBindFramebuffer() {
        return mHasBindFramebuffer;
    }

    public void setHasBind(boolean bind) {
        mHasBind = bind;
    }

    public int getBufferIdByIndex(int index) {
        index = checkIndex(index);
        return mFrameBuffers[index];
    }

    public int getCurrentBufferId() {
        return getBufferIdByIndex(mCurrentTextureIndex);
    }

    public int getTextureIdByIndex(int index) {
        index = checkIndex(index);
        return mFrameBuffersTextures[index];
    }

    public int getTextureId() {
        return getTextureIdByIndex(0);
    }

    public int getCurrentTextureId() {
        return getTextureIdByIndex(mCurrentTextureIndex);
    }

    public int getPreviousTextureId() {
        if (mPreviousTextureIndex < 0) {
            if (mHasBind) {
                mHasBind = false;
                mPreviousTextureIndex = mCurrentTextureIndex;
            }
            return -1;
        } else {
            if (!mHasBind) {
                mPreviousTextureIndex = mCurrentTextureIndex;
            }
        }
        mHasBind = false;
        return getTextureIdByIndex(mPreviousTextureIndex);
    }

    public int[] getBufferIds() {
        return mFrameBuffers;
    }

    public int[] getTextureIds() {
        return mFrameBuffersTextures;
    }

    public void destroy() {
        mHasBindFramebuffer = false;
        mHasBind = false;

        if (mColorBuffers != null) {
            GLES20.glDeleteRenderbuffers(mColorBuffers.length, mColorBuffers, 0);
            mColorBuffers = null;
        }
        if (mDepthBuffers != null) {
            GLES20.glDeleteRenderbuffers(mDepthBuffers.length, mDepthBuffers, 0);
            mDepthBuffers = null;
        }
        if (mStencilBuffers != null) {
            GLES20.glDeleteRenderbuffers(mStencilBuffers.length, mStencilBuffers, 0);
            mStencilBuffers = null;
        }
        if (mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(mFrameBuffers.length, mFrameBuffers, 0);
            mFrameBuffers = null;
        }
        if (mFrameBuffersTextures != null) {
            GLES20.glDeleteTextures(mFrameBuffersTextures.length, mFrameBuffersTextures, 0);
            mFrameBuffersTextures = null;
        }
        mClearMask = 0;
    }
}
