package com.xs.lightpuzzle.imagedecode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.CloseUtils;
import com.xs.lightpuzzle.imagedecode.core.ImageScaleType;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.imagedecode.core.ImageSizeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Created by xs on 2018/3/29.
 */

public class BitmapHelper {

    /***
     * 检查bitmap是否有效
     * @param bitmap
     * @return 有效返回true, 无效返回false
     */
    public static boolean isValid(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        return true;
    }

    /**
     * 检查bitmap是否无效
     *
     * @param bitmap
     * @return 无效返回true, 有效返回false
     */
    public static boolean isInvalid(Bitmap bitmap) {
        return !isValid(bitmap);
    }

    /**
     * 释放bitmap
     * 如果有多线程不确定什么时候释放，不推荐使用
     *
     * @param bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (isValid(bitmap)) {
            bitmap.recycle();
        }
        bitmap = null;
    }

    /**
     * 获取sd卡上图片的options
     *
     * @param filePath
     * @return
     */
    public static Options getFileOptions(String filePath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options;
    }

    /**
     * 获取assets目录下图片的options
     *
     * @param context
     * @param filePath
     * @return
     */
    public static Options getAssetsOptions(Context context, String filePath) {
        Options options = new Options();
        if (TextUtils.isEmpty(filePath)) {
            return options;
        }
        options.inJustDecodeBounds = true;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(filePath);
            BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                CloseUtils.closeIO(inputStream);
            }
        }
        return options;
    }

    /**
     * 获取资源图片的options
     *
     * @param context
     * @param resId
     * @return
     */
    public static Options getResourceOptions(Context context, int resId) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        return options;
    }

    /**
     * 获取图片的宽高比
     *
     * @param filePath
     * @return
     */
    public static float getFileWHRatio(String filePath) {
        Options options = getFileOptions(filePath);
        return 1.0f * options.outWidth / options.outHeight;
    }

    /**
     * 获取图片的高宽比
     *
     * @param filePath
     * @return
     */
    public static float getFileHWRatio(String filePath) {
        return 1.0f / getFileWHRatio(filePath);
    }

    /**
     * 获取宽高比
     *
     * @param filePath
     * @param isConsiderExifParams 是否考虑旋转角度
     * @return
     */
    public static float getFileWHRatio(String filePath, boolean isConsiderExifParams) {
        int rotation = 0;
        float ratio = getFileWHRatio(filePath);
        if (isConsiderExifParams) {
            rotation = ImageUtils.getRotateDegree(filePath);
        }

        if (rotation % 180 != 0) {
            ratio = 1.0f / ratio;
        }
        return ratio;
    }

    /**
     * 获取高宽比
     *
     * @param filePath
     * @param isConsiderExifParams 是否考虑旋转角度
     * @return
     */
    public static float getFileHWRatio(String filePath, boolean isConsiderExifParams) {
        return 1.0f / getFileWHRatio(filePath, isConsiderExifParams);
    }

    /**
     * * 获取图片的宽高比
     *
     * @param context
     * @param filePath
     * @return
     */
    public static float getAssetsWHRatio(Context context, String filePath) {
        Options options = getAssetsOptions(context, filePath);
        return 1.0f * options.outWidth / options.outHeight;
    }

    /**
     * 获取图片的高宽比
     *
     * @param context
     * @param filePath
     * @return
     */
    public static float getAssetsHWRatio(Context context, String filePath) {
        return 1.0f / getAssetsWHRatio(context, filePath);
    }

    /**
     * * 获取图片的宽高比
     *
     * @param context
     * @param resId
     * @return
     */
    public static float getResourceWHRatio(Context context, int resId) {
        Options options = getResourceOptions(context, resId);
        return 1.0f * options.outWidth / options.outHeight;
    }

    /**
     * 获取图片的高宽比
     *
     * @param context
     * @param resId
     * @return
     */
    public static float getResourceHWRatio(Context context, int resId) {
        return 1.0f / getResourceWHRatio(context, resId);
    }

    /**
     * 判断assetsbmp是否存在
     *
     * @param context
     * @param bmpPath
     * @return
     */
    public static boolean isAssetsBitmapExist(Context context, String bmpPath) {
        Options options = getAssetsOptions(context, bmpPath);
        return options.outWidth > 0 && options.outHeight > 0;
    }

    /**
     * 本地图片是否存在
     *
     * @param bmpPath
     * @return
     */
    public static boolean isFileBitmapExist(String bmpPath) {
        Options options = getFileOptions(bmpPath);
        return options.outWidth > 0 && options.outHeight > 0;
    }

    /**
     * 资源图片是否存在
     *
     * @param context
     * @param resId
     * @return
     */
    public static boolean isResourceBitmapExist(Context context, int resId) {
        Options options = getResourceOptions(context, resId);
        return options.outWidth > 0 && options.outHeight > 0;
    }


    public static long calculateDecodeMemory(List<String> picPaths,
                                             ImageSize dstSize,
                                             ImageScaleType imageScaleType) {
        return calculateDecodeMemory(picPaths, dstSize, imageScaleType, Bitmap.Config.ARGB_8888);
    }

    public static long calculateDecodeMemory(List<String> picPaths,
                                             ImageSize dstSize,
                                             ImageScaleType imageScaleType,
                                             Bitmap.Config config) {
        long result = 0;
        if (picPaths == null || picPaths.size() == 0) {
            return result;
        }

        if (dstSize == null || config == null) {
            throw new IllegalArgumentException("dstSize == null || config == null");
        }

        for (String path : picPaths) {
            if (TextUtils.isEmpty(path)) {
                continue;
            }

            Options options = getFileOptions(path);
            if (options.outWidth <= 0 || options.outHeight <= 0) {
                continue;
            }

            ImageSize srcSize = new ImageSize(options.outWidth, options.outHeight);
            float scale = ImageSizeUtils.computeConsiderExactScale(imageScaleType, srcSize, dstSize);

            ImageSize decodeSize;
            if (scale < 1) {  // 比目标还小，暂时用目标大小来计算
                decodeSize = srcSize;
            } else {
                decodeSize = new ImageSize((int) (srcSize.getWidth() / scale),
                        (int) (srcSize.getHeight() / scale));
            }

            long bmpMemory;
            bmpMemory = (long) (decodeSize.getWidth() * decodeSize.getHeight());
            switch (config) {
                case RGB_565:
                case ARGB_4444:
                case RGBA_F16:
                    bmpMemory = bmpMemory * 2;
                    break;
                case ARGB_8888:
                    bmpMemory = bmpMemory * 4;
                    break;
            }

            result = result + bmpMemory;

            Log.d("bitmapHelper: ", "picPath: " + path
                    + "  need memory: " + ImageUtils.byte2M(bmpMemory));
            Log.d("bitmapHelper: ", "result: " + ImageUtils.byte2M(result));
        }

        return result;
    }

}
