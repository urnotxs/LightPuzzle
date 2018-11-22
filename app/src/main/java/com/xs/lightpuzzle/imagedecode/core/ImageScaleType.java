package com.xs.lightpuzzle.imagedecode.core;

/**
 * Created by xs on 2018/11/19.
 */
public enum ImageScaleType {
    NONE,           // 按照原图的宽高，内存不够直接返回null
    NONE_SAFE,      // 按照比例，内存不够会进行缩小，直到能decode出来
    EXACTLY,        // 按比例最短边适配目标宽高,刚好有一边铺满目标的宽或高
    EXACTLY_WIDTH,  // 扩展到目标宽
    EXACTLY_HEIGHT, // 扩展到目标高
    EXACTLY_CENTER_CROP,   // 按比例最短边适配目标宽高，并且居中裁剪返回目标宽高的图片
    CENTER_INSIDE, // 按比例完全在目标宽高内
    FIT_XY,        // 不按比例缩放，直接缩放到目标宽高
}
