package com.xs.lightpuzzle.imagedecode;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.xs.lightpuzzle.imagedecode.core.DecodeImageOptions;
import com.xs.lightpuzzle.imagedecode.core.ImageDecoder;
import com.xs.lightpuzzle.imagedecode.core.ImageLoader;
import com.xs.lightpuzzle.imagedecode.core.ImageScaleType;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by lsq on 2018/4/2.
 * <p>
 * 注意：如果指定ImgeSize 则ImageScaleType 不能设置为ImageScaleType.NONE或ImageScaleType.NONO_SAFE
 * 如果只想decode 得到原图的分辨率 -> decodexxx(context, pathName,ImageScaleType.NONE)
 * <p>
 * 返回的是默认目标图片宽高的bmp ,屏幕分辨率的一半
 * decodexxx(Context context, String pathName)
 * <p>
 * 返回的是指定最短边缩放的目标区域的bmp
 * decodexxx(Context context, String pathName, ImageSize targetSize)
 * <p>
 * 返回指定类型的bmp ImageScaleType.NONE 为原图
 * decodexxx(Context context, String pathName,ImageScaleType imageScaleType)
 * <p>
 * 指定ImgeSize 则ImageScaleType 不能设置为ImageScaleType.NONE或ImageScaleType.NONO_SAFE
 * decodexxx(Context context, String pathName,ImageSize targetSize, ImageScaleType imageScaleType)
 * <p>
 * 返回指定类型的bmp
 * decodexxx(Context context, String pathName,ImageSize targetSize, DecodeImageOptions options)
 */

public class JaneBitmapFactory {

    /**
     * decode 文件来自于sd卡
     *
     * @param context
     * @param pathName
     * @return
     */
    public static Bitmap decodefile(Context context, String pathName) {
        return decodefile(context, pathName, getDefaultImageSize());
    }

    public static Bitmap decodefile(Context context, String pathName, ImageSize targetSize) {
        return decodefile(context, pathName, targetSize, ImageScaleType.EXACTLY);
    }

    public static Bitmap decodefile(Context context, String pathName,
                                    ImageScaleType imageScaleType) {
        ImageSize imageSize = getAdaptImageTypeImageSize(imageScaleType);
        return decodefile(context, pathName, imageSize, imageScaleType);
    }

    public static Bitmap decodefile(Context context, String pathName, ImageSize targetSize,
                                    ImageScaleType imageScaleType, int inSampleSize) {
        return decodefile(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType, inSampleSize));
    }

    public static Bitmap decodefile(Context context, String pathName,
                                    ImageSize targetSize, ImageScaleType imageScaleType) {
        return decodefile(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType));
    }

    public static Bitmap decodefile(Context context, String pathName,
                                    ImageSize targetSize, DecodeImageOptions options) {
        checkParamsLegal(targetSize, options);
        ImageDecoder.ImageDecodingInfo imageDecodingInfo =
                new ImageDecoder.ImageDecodingInfo(ImageLoader.Scheme.FILE.wrap(pathName),
                        targetSize, options);
        return new ImageDecoder(context).decode(imageDecodingInfo);
    }


    /**
     * decode 文件来自于assets目录
     *
     * @param context
     * @param pathName
     * @return
     */
    public static Bitmap decodeAssets(Context context, String pathName) {
        return decodeAssets(context, pathName, getDefaultImageSize());
    }

    public static Bitmap decodeAssets(Context context, String pathName, ImageSize targetSize) {
        return decodeAssets(context, pathName, targetSize, DecodeImageOptions.createSimple());
    }

    public static Bitmap decodeAssets(Context context, String pathName,
                                      ImageScaleType imageScaleType) {
        ImageSize imageSize = getAdaptImageTypeImageSize(imageScaleType);
        return decodeAssets(context, pathName, imageSize, imageScaleType);
    }

    public static Bitmap decodeAssets(Context context, String pathName,
                                      ImageSize targetSize, ImageScaleType imageScaleType) {
        return decodeAssets(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType));
    }

    public static Bitmap decodeAssets(Context context, String pathName, ImageSize targetSize,
                                      ImageScaleType imageScaleType, int inSampleSize) {
        return decodeAssets(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType, inSampleSize));
    }

    public static Bitmap decodeAssets(Context context, String pathName,
                                      ImageSize targetSize, DecodeImageOptions options) {
        ImageDecoder.ImageDecodingInfo imageDecodingInfo =
                new ImageDecoder.ImageDecodingInfo(ImageLoader.Scheme.ASSETS.wrap(pathName),
                        targetSize, options);
        return new ImageDecoder(context).decode(imageDecodingInfo);
    }


    /**
     * decode 文件来自于Resource
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap decodeResource(Context context, int resId) {
        return decodeResource(context, resId, getDefaultImageSize());
    }

    public static Bitmap decodeResource(Context context, int resId, ImageSize targetSize) {
        return decodeResource(context, resId, targetSize, DecodeImageOptions.createSimple());
    }

    public static Bitmap decodeResource(Context context, int pathName,
                                        ImageScaleType imageScaleType) {
        ImageSize imageSize = getAdaptImageTypeImageSize(imageScaleType);
        return decodeResource(context, pathName, imageSize, imageScaleType);
    }

    public static Bitmap decodeResource(Context context, int pathName,
                                        ImageSize targetSize, ImageScaleType imageScaleType) {
        return decodeResource(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType));
    }

    public static Bitmap decodeResource(Context context, int pathName, ImageSize targetSize,
                                        ImageScaleType imageScaleType, int inSampleSize) {
        return decodeResource(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType, inSampleSize));
    }

    public static Bitmap decodeResource(Context context, int resId,
                                        ImageSize targetSize, DecodeImageOptions options) {
        ImageDecoder.ImageDecodingInfo imageDecodingInfo =
                new ImageDecoder.ImageDecodingInfo(ImageLoader.Scheme.DRAWABLE.wrap(resId + ""),
                        targetSize, options);
        return new ImageDecoder(context).decode(imageDecodingInfo);
    }


    /**
     * 不推荐使用
     * 不清楚数据来源时可用该方法
     * 这里只是做了简单的判断
     *
     * @param context
     * @param object
     * @return
     */
    public static Bitmap decode(Context context, Object object) {
        return decode(context, object, getDefaultImageSize());
    }

    public static Bitmap decode(Context context, Object object, ImageSize targetSize) {
        return decode(context, object, targetSize, DecodeImageOptions.createSimple());
    }

    public static Bitmap decode(Context context, String pathName,
                                ImageScaleType imageScaleType) {
        ImageSize imageSize = getAdaptImageTypeImageSize(imageScaleType);
        return decode(context, pathName, imageSize, imageScaleType);
    }

    public static Bitmap decode(Context context, String pathName, ImageSize targetSize,
                                ImageScaleType imageScaleType, int inSampleSize) {
        return decode(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType, inSampleSize));
    }

    public static Bitmap decode(Context context, String pathName,
                                ImageSize targetSize, ImageScaleType imageScaleType) {
        return decode(context, pathName, targetSize,
                DecodeImageOptions.createSimple(imageScaleType));
    }

    public static Bitmap decode(Context context, Object object,
                                ImageSize targetSize, DecodeImageOptions options) {
        if (object instanceof String) {
            String pathName = (String) object;
            // sd卡
            if (com.blankj.utilcode.util.FileUtils.isFileExists(pathName)) {
                return decodefile(context, pathName, targetSize, options);
            } else {
                // assets
                String fileDir = com.blankj.utilcode.util.FileUtils.getDirName(pathName);
                // 去掉最后的"/"
                if (!TextUtils.isEmpty(fileDir) && fileDir.endsWith(File.separator)) {
                    fileDir = fileDir.substring(0, fileDir.length() - 1);
                }

                String fileName = com.blankj.utilcode.util.FileUtils.getFileName(pathName);
                if (isAssetsExistsFile(context, fileDir, fileName)) {
                    return decodeAssets(context, pathName, targetSize, options);
                }
            }
        }
        // Resource
        else if (object instanceof Integer) {
            int resId = (int) object;
            return decodeResource(context, resId, targetSize, options);
        }
        return null;
    }

    public static boolean isAssetsExistsFile(Context context, String fileDir, String fileName) {
        AssetManager am = context.getAssets();
        try {
            String[] names = am.list(fileDir);
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(fileName.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 如果使用没有设置ImageSize给一个默认的的ImageSize
     * 屏幕分辨率的一半
     *
     * @return
     */
    private static ImageSize getDefaultImageSize() {
        int w = Utils.getScreenW() / 2;
        int h = Utils.getScreenH() / 2;
        return new ImageSize(w, h);
    }

    /**
     * 为imageScaleType 配置imageSize
     *
     * @param imageScaleType
     * @return
     */
    private static ImageSize getAdaptImageTypeImageSize(ImageScaleType imageScaleType) {
        ImageSize targetSize = null;
        if (imageScaleType != ImageScaleType.NONE
                && imageScaleType != ImageScaleType.NONE_SAFE) {
            targetSize = getDefaultImageSize();
        }
        return targetSize;
    }

    private static void checkParamsLegal(ImageSize imageSize, DecodeImageOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("params is error! options == null");
        }
        ImageScaleType imageScaleType = options.getImageScaleType();
        if (imageSize != null && (imageScaleType == ImageScaleType.NONE
                || imageScaleType == ImageScaleType.NONE_SAFE)) {
            throw new IllegalArgumentException("params is error! " +
                    "ImageScaleType.NONE and ImageScaleType.NONE_SAFE dont set imageSize");
        }
    }

}
