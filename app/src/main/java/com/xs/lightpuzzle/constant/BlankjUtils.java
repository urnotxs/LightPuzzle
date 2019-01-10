package com.xs.lightpuzzle.constant;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;

import java.io.File;

/**
 * Created by xs on 2019/1/9.
 *
 * BlankJ工具库的汇总
 */

public final class BlankjUtils {

    // --- FileIOUtils

    // 读取本地文件的json字符串
    public static String readFile2String(final String filePath) {
        return FileIOUtils.readFile2String(filePath);
    }

    // 将json字符串写入本地文件
    public static boolean writeFileFromString(final String filePath, final String content) {
        return FileIOUtils.writeFileFromString(filePath, content);
    }

    // --- SDCardUtils

    // 读取SD卡的路径
    public static String getSDCardPaths() {
        return SDCardUtils.getSDCardPaths().get(0);
    }

    // --- FileUtils

    // 获取路径的文件夹名
    public static String getDirName(final String filePath) {

        return FileUtils.getDirName(filePath);
    }

    // 判断文件是否存在
    public static boolean isFileExists(final String filePath) {
        return FileUtils.isFileExists(filePath);
    }

    public static boolean deleteFile(final File file) {
        return FileUtils.deleteFile(file);
    }

    public static boolean deleteDir(final String dirPath) {
        return FileUtils.deleteDir(dirPath);//解码不出删除
    }

}
