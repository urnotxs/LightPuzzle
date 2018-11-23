package com.xs.lightpuzzle.imagedecode.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by xs on 2018/4/2.
 */

public class DecodeImageOptions {
    private final ImageScaleType mImageScaleType;
    private final BitmapFactory.Options mDecodingOptions;
    private final Bitmap.Config mBitmapConfig;
    private final int mRotation;
    private final boolean isConsiderExifParams;
    private final boolean isNativeRetreatment;
    private boolean isNativeDecode; // 是否使用底层方法来decode 默认true

    private DecodeImageOptions(Builder builder) {
        mImageScaleType = builder.mImageScaleType;
        mDecodingOptions = builder.mDecodingOptions;
        mBitmapConfig = builder.mBitmapConfig;
        mRotation = builder.mRotation;
        isConsiderExifParams = builder.isConsiderExifParams;
        isNativeRetreatment = builder.isNativeRetreatment;
        isNativeDecode = builder.isNativeDecode;
    }

    public ImageScaleType getImageScaleType() {
        return mImageScaleType;
    }

    public BitmapFactory.Options getDecodingOptions() {
        return mDecodingOptions;
    }

    public Bitmap.Config getBitmapConfig() {
        return mBitmapConfig;
    }

    public int getRotation() {
        return mRotation;
    }

    public boolean isConsiderExifParams() {
        return isConsiderExifParams;
    }

    public boolean isNativeRetreatment() {
        return isNativeRetreatment;
    }

    public boolean isNativeDecode() {
        return isNativeDecode;
    }

    public static class Builder {
        private ImageScaleType mImageScaleType = ImageScaleType.EXACTLY;
        private BitmapFactory.Options mDecodingOptions;
        private Bitmap.Config mBitmapConfig = Bitmap.Config.ARGB_8888;
        private int mRotation;
        private boolean isConsiderExifParams = true;
        private boolean isNativeRetreatment;
        private boolean isNativeDecode = true;

        public Builder setImageScaleType(ImageScaleType imageScaleType) {
            if (imageScaleType == null) {
                throw new IllegalArgumentException("mImageScaleType can't be null");
            }
            mImageScaleType = imageScaleType;
            return this;
        }

        public Builder setDecodeingOptions(BitmapFactory.Options options) {
            if (options == null) {
                throw new IllegalArgumentException("mDecodingOptions can't be null");
            }
            mDecodingOptions = options;
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            if (bitmapConfig == null) {
                throw new IllegalArgumentException("mBitmapConfig can't be null");
            }
            mBitmapConfig = bitmapConfig;
            return this;
        }

        public Builder setRotation(int rotation) {
            mRotation = rotation;
            return this;
        }

        public Builder setConsiderExifParams(boolean isConsiderExifParams) {
            this.isConsiderExifParams = isConsiderExifParams;
            return this;
        }

        public Builder setNativeRetreatment(boolean nativeRetreatment) {
            isNativeRetreatment = nativeRetreatment;
            return this;
        }

        public Builder setNativeDecode(boolean nativeDecode) {
            isNativeDecode = nativeDecode;
            return this;
        }

        public DecodeImageOptions build() {
            return new DecodeImageOptions(this);
        }
    }

    public static BitmapFactory.Options createSimpleOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        return options;
    }

    public static DecodeImageOptions createSimple() {
        return new Builder().build();
    }

    public static DecodeImageOptions createSimple(ImageScaleType imageScaleType) {
        return new Builder().setImageScaleType(imageScaleType).build();
    }

    public static DecodeImageOptions createSimple(ImageScaleType imageScaleType, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return new Builder().setImageScaleType(imageScaleType).setDecodeingOptions(options).build();
    }

}
