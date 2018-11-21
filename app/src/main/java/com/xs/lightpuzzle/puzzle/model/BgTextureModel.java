package com.xs.lightpuzzle.puzzle.model;

import android.graphics.Rect;

import com.xs.lightpuzzle.puzzle.data.BgTextureData;
import com.xs.lightpuzzle.puzzle.info.PuzzlesBgTextureInfo;

/**
 * Created by xs on 2018/11/21.
 */

public class BgTextureModel {

    public static PuzzlesBgTextureInfo getBgTextureInfo(Rect rect, Rect outPutRect, BgTextureData bgTextureData) {
        if (rect == null || bgTextureData == null) {
            return null;
        }
        PuzzlesBgTextureInfo bgTextureInfo = new PuzzlesBgTextureInfo();
        bgTextureInfo.setRect(rect);
        bgTextureInfo.setOutPutRect(outPutRect);
        bgTextureInfo.setTextureStr(bgTextureData.getTexture());
        bgTextureInfo.setEffect(bgTextureData.getEffect());
        bgTextureInfo.setAlpha(bgTextureData.getAlpha());
        bgTextureInfo.setBgColor(bgTextureData.getBgColor());
        bgTextureInfo.setWaterColor(bgTextureData.getWaterColor());

        return bgTextureInfo;
    }
}
