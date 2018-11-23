package com.xs.lightpuzzle.yszx;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileIOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xs on 2018/11/5.
 */

public class AssetManagerHelper {

    public static String convertInputString(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            try {
                InputStream is = context.getAssets().open(path);
                return ConvertUtils.inputStream2String(is, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void copyFile(Context context, String fileName, String destDir)
            throws IOException {
        AssetManager assetManager = context.getAssets();
        String destFilePath = destDir + File.separator + fileName;
        FileIOUtils.writeFileFromIS(destFilePath, assetManager.open(fileName));
    }
}
