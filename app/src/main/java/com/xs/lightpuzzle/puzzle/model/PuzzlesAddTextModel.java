package com.xs.lightpuzzle.puzzle.model;

import android.content.Context;

import com.xs.lightpuzzle.puzzle.info.PuzzlesAddTextInfo;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;

/**
 * Created by xs on 2018/11/28.
 */

public class PuzzlesAddTextModel {

    /**
     * 初始一个AddTextInfo(点击添加文本按钮)
     *
     * @param context
     * @param puzzlesInfo
     * @return
     */
    public static PuzzlesAddTextInfo getAddTextInfo(Context context, PuzzlesInfo puzzlesInfo) {
        if (puzzlesInfo.getPuzzlesRect() == null) {
            return null;
        }
        PuzzlesAddTextInfo addTextInfo = new PuzzlesAddTextInfo();
        //addTextInfo.setId(PuzzleConstant.sAddTextId);
        addTextInfo.setPuzzleModel(puzzlesInfo.getPuzzleMode());
        addTextInfo.setAutoStr("你好·时光");
        addTextInfo.setFont(context, "zzgfshG0v1xt.otf");
        addTextInfo.setAlignment("Center");
        addTextInfo.setFontSize(60);
        addTextInfo.setFontColor(PuzzlesUtils.strColor2Int("000000"));
        addTextInfo.setShowFrame(true);
        addTextInfo.setDownloadFont(false);
        addTextInfo.setRect(puzzlesInfo.getPuzzlesRect());
        addTextInfo.setOutPutRect(puzzlesInfo.getOutPutRect());

        return addTextInfo;
    }
}
