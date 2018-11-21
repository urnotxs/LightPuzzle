package com.xs.lightpuzzle.puzzle.model;

import android.content.Context;

import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.TemplateData;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.info.TemplateInfo;

/**
 * Created by xs on 2018/11/21.
 */

public class TemplateModel {
    /**
     * 普通拼图进入模式
     *
     * @param puzzleMode   拼图类型
     * @param rotationImgs 图片数组
     * @param templateData 模板数据
     * @return PuzzlesInfo
     */

    public static PuzzlesInfo getSimplePuzzlesInfo(Context context, int puzzleMode, final RotationImg[] rotationImgs,
                                                   TemplateData templateData) {
        if (puzzleMode == -1 || templateData == null || rotationImgs == null) {
            return null;
        }

        PuzzlesInfo puzzlesInfo = new PuzzlesInfo();
        puzzlesInfo.setPuzzleMode(puzzleMode);
        TemplateInfo templateInfo = getTemplateInfo(context, puzzleMode, rotationImgs, templateData);
        puzzlesInfo.addTemplateInfo(templateInfo);
        puzzlesInfo.resetId();
        return puzzlesInfo;
    }

    public static TemplateInfo getTemplateInfo(Context context, int puzzleMode, RotationImg[] rotationImgs,
                                               TemplateData templateData) {
        return new TemplateInfo(context, puzzleMode, rotationImgs, templateData);
    }

}
