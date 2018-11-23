package com.xs.lightpuzzle.imagedecode.core;

/**
 * Created by xs on 2018/3/30.
 */

public class ImageSize {
    private int mWidth;
    private int mHeight;

    public ImageSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public ImageSize(int width, int height, int rotation) {
        if (rotation % 180 == 0) {
            mWidth = width;
            mHeight = height;
        } else {
            mWidth = height;
            mHeight = width;
        }
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }
}
