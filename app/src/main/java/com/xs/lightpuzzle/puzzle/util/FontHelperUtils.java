package com.xs.lightpuzzle.puzzle.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @version 1.0
 * @function 字体的帮助类
 * @since 17/7/25
 */
public final class FontHelperUtils {

    private static Map<String, String> sZhcnToEnMap = new HashMap<>();

    static {
        sZhcnToEnMap.put("系统默认字体", "Default");

        sZhcnToEnMap.put("造字工房俊雅锐宋字体", "zzgfjyrstybcgt");
        sZhcnToEnMap.put("造字工房尚黑G0v1细体", "zzgfshG0v1xt");
        sZhcnToEnMap.put("造字工房悦黑体验版纤细体", "zzgfyhtybxxt");

        sZhcnToEnMap.put("青鸟华光简报宋二", "qnhgjbs2");

        sZhcnToEnMap.put("段宁毛笔行书", "zqlqbzcsfh"); // thumb
        sZhcnToEnMap.put("钟齐李洤标准草书符号_0", "zqlqbzcsfh"); // resource
    }

    public static String convertZhcnToEnForAssetFontRes(String fileName) {
        if (TextUtils.isEmpty(fileName) || !isContainZhcn(fileName)) {
            return fileName;
        }

        String mappedFileNameNoExtension = fileName;

        boolean isMapped = false;
        for (String k : sZhcnToEnMap.keySet()) {
            if (fileName.contains(k)) {
                mappedFileNameNoExtension = sZhcnToEnMap.get(k);
                isMapped = true;
                break;
            }
        }

        if (!isMapped) {
            return fileName;
        }

        String extension = FileUtils.getFileExtension(fileName);
        if (TextUtils.isEmpty(extension)) {
            return mappedFileNameNoExtension;
        }

        if (mappedFileNameNoExtension.equalsIgnoreCase("系统默认字体")
                && (extension.equalsIgnoreCase("otf") || extension.equalsIgnoreCase("ttf"))) {
            return mappedFileNameNoExtension;
        }

        return mappedFileNameNoExtension + "." + extension;
    }

    private static boolean isContainZhcn(String str) {
        return Pattern.compile("[\u4e00-\u9fa5]").matcher(str).find();
    }
}
