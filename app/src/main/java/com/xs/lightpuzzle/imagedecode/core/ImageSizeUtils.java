package com.xs.lightpuzzle.imagedecode.core;

/**
 * Created by xs on 2018/11/19.
 */

public class ImageSizeUtils {

    /**
     * 粗劣计算一下缩放比例
     *
     * @param srcSize
     * @param dstSize
     * @param imageScaleType
     * @return
     */
    public static int computePreareDecodingScale(ImageSize srcSize, ImageSize dstSize,
                                                 ImageScaleType imageScaleType) {
        int srcW = 1;
        int srcH = 1;
        if (srcSize != null) {
            srcW = srcSize.getWidth();
            srcH = srcSize.getHeight();
        }

        int dstW = 1;
        int dstH = 1;
        if (dstSize != null) {
            dstW = dstSize.getWidth();
            dstH = dstSize.getHeight();
        } else {
            imageScaleType = ImageScaleType.NONE; // 目的大小为null 设置decode类型为NONE
        }

        int scale = 1;

        if (imageScaleType == ImageScaleType.NONE) {
            scale = 1;
        } else if (imageScaleType == ImageScaleType.NONE_SAFE) {

        }
        // 刚好有一边铺满和刚好铺满居中裁剪,拉伸到目标区域
        else if (imageScaleType == ImageScaleType.EXACTLY
                || imageScaleType == ImageScaleType.EXACTLY_CENTER_CROP
                || imageScaleType == ImageScaleType.FIT_XY) {
            float imgRatio = 1.0f * srcW / srcH;
            float tarRatio = 1.0f * dstW / dstH;

            if (imgRatio > tarRatio) { // 图片高短
                if (srcH > dstH) {    // 图片比目标高
                    scale = Math.round(srcH / dstH);
                }
            } else { // 图片宽短
                if (srcW > dstW) { // 图片比目标宽
                    scale = Math.round(srcW / dstW);
                }
            }
        }
        // 完全再目标区域中
        else if (imageScaleType == ImageScaleType.CENTER_INSIDE) {
            float imgRatio = 1.0f * srcW / srcH;
            float tarRatio = 1.0f * dstW / dstH;
            if (imgRatio > tarRatio) { // 图片宽长
                if (srcW > dstW) {
                    scale = Math.round(srcW / dstW);
                }
            } else { // 图片高长
                if (srcH > dstH) {
                    scale = Math.round(srcH / dstH);
                }
            }
        }

        if (scale < 1) {
            scale = 1;
        }

        return scale;
    }

    /**
     * 准确计算缩放比例
     *
     * @param imageScaleType
     * @param bmpSize
     * @param dstSize
     * @return
     */
    public static float computeConsiderExactScale(
            ImageScaleType imageScaleType, ImageSize bmpSize, ImageSize dstSize) {
        int bmpW = bmpSize.getWidth();
        int bmpH = bmpSize.getHeight();
        int tarW = dstSize.getWidth();
        int tarH = dstSize.getHeight();
        float scale = 1.0f;
        // 无缩放
        if (imageScaleType == ImageScaleType.NONE) {
            scale = 1;
        }
        // 安全缩放
        else if (imageScaleType == ImageScaleType.NONE_SAFE) {

        }
        // 最短边铺满
        else if (imageScaleType == ImageScaleType.EXACTLY
                || imageScaleType == ImageScaleType.EXACTLY_CENTER_CROP) {
            float bmpRatio = 1.0f * bmpW / bmpH;
            float tarRatio = 1.0f * tarW / tarH;

            // 短边填满目标区域
            if (bmpRatio > tarRatio) { // 位图高短
                scale = 1.0f * tarH / bmpH;
            } else { // 位图宽短
                scale = 1.0f * tarW / bmpW;
            }
        }
        // 完全在目标内部
        else if (imageScaleType == ImageScaleType.CENTER_INSIDE) {
            float bmpRatio = 1.0f * bmpW / bmpH;
            float tarRatio = 1.0f * tarW / tarH;
            if (bmpRatio > tarRatio) { // 位图宽长
                scale = 1.0f * tarW / bmpW;
            } else { // 位图高高
                scale = 1.0f * tarH / bmpH;
            }
        }

        return scale;
    }
}
