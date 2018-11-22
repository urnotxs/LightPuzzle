package com.xs.lightpuzzle.puzzle.view.signature;

import com.blankj.utilcode.util.FileUtils;
import com.xs.lightpuzzle.minnie.PuzzleConstant;

import java.io.File;

/**
 * Created by lsq on 2018/5/28.
 */

public class SignatureUtils {

    public static String getEditingSignature() {

        String editDir = PuzzleConstant.SD_SIGNATURE_EDITING_PATH;
        // 取出正在编辑的的签名
        File[] files = new File(editDir).listFiles();
        if (files != null && files.length > 0) {
            return files[0].getAbsolutePath();
        }

        String filePath = "";
        String fileDir = PuzzleConstant.SD_SIGNATURE_HISTORY_LIST_PATH;

        File baseDir = new File(fileDir);
        if (!baseDir.exists()) {
            return filePath;
        }

        // 取出历史最新的签名
        files = baseDir.listFiles();
        if (files == null) {
            return filePath;
        }

        int index = -1;
        int time = -1;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String fileAbsPath = file.getName();
            long tmp = Long.parseLong(fileAbsPath.substring(
                    0, fileAbsPath.indexOf("_")));
            if (tmp > time) {
                index = i;
            }
        }

        if (index != -1) {
            filePath = files[index].getAbsolutePath();
        }

        // copy一份到编辑路径
        if (FileUtils.isFileExists(filePath)) {
            filePath = copySelectSignature2EditingDir(filePath);
        }
        return filePath;
    }

    public static String copySelectSignature2EditingDir(String filePath) {
        if (!FileUtils.isFileExists(filePath)) {
            return "";
        }
        String dstPath = "";
        String editDir = PuzzleConstant.SD_SIGNATURE_EDITING_PATH;
        // 正在编辑的不用删除
        if(filePath.contains(editDir)){
            return filePath;
        }

        FileUtils.deleteDir(editDir);
        String fileName = FileUtils.getFileName(filePath);
        String editPath = editDir + fileName;

        if (FileUtils.copyFile(filePath, editPath)) {
            dstPath = editPath;
        }
        return dstPath;
    }

    public static void clearEditingDir() {
        String editDir = PuzzleConstant.SD_SIGNATURE_EDITING_PATH;
        FileUtils.deleteDir(editDir);
    }

}
