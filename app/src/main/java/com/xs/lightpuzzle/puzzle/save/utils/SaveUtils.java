package com.xs.lightpuzzle.puzzle.save.utils;

import android.content.Context;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.callback.JaneCallback;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.save.SaveBitmapHelper;

import static com.xs.lightpuzzle.constant.DirConstant.PUZZLE_SAVE;
import static com.xs.lightpuzzle.constant.DirConstant.PUZZLE_SAVE_JSON;

/**
 * Created by Lin on 2018/5/2.
 */

public class SaveUtils {

    public static void savePic(Context context, PuzzlesInfo puzzlesInfo, JaneCallback<String> callback){
        if (puzzlesInfo == null) {
            return;
        }
        puzzlesInfo.setValues();
        if (saveInfoLocal(puzzlesInfo)) {
            String jsonPath = PUZZLE_SAVE_JSON;
            if (FileUtils.isFileExists(jsonPath)) {
                new SaveBitmapHelper(context, callback).startSaveBitmapService(jsonPath);
            }
        }
    }

    private static boolean saveInfoLocal(PuzzlesInfo puzzlesInfo){
        if (!FileUtils.isFileExists(PUZZLE_SAVE)) {
            FileUtils.createOrExistsDir(PUZZLE_SAVE);
        }
        String jsonPath = PUZZLE_SAVE_JSON;
        FileUtils.deleteFile(jsonPath);
        if (puzzlesInfo != null) {
            int puzzleModel = puzzlesInfo.getPuzzleMode();
            //区分视频和图片
            if (puzzleModel != PuzzleMode.MODE_VIDEO) {
                String str = new Gson().toJson(puzzlesInfo);
                FileIOUtils.writeFileFromString(jsonPath, str);
                return true;
            }
        }
        return false;
    }

}
