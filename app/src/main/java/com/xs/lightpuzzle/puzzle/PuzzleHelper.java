package com.xs.lightpuzzle.puzzle;

import android.content.Context;

import com.xs.lightpuzzle.puzzle.data.BgTextureData;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.SignatureData;
import com.xs.lightpuzzle.puzzle.data.TemplateData;
import com.xs.lightpuzzle.puzzle.info.PuzzlesBgTextureInfo;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.model.BgTextureModel;
import com.xs.lightpuzzle.puzzle.model.TemplateModel;

/**
 * Created by xs on 2018/11/21.
 */

public class PuzzleHelper {
    /**
     * 普通模式创建PuzzlesInfo
     *
     * @param context       context
     * @param puzzleMode    拼图类型
     * @param rotationArr   图片列表
     * @param templateData  模板
     * @param bgTextureData 默认背景纹理
     * @param signatureData 默认签名信息
     * @return PuzzlesInfo
     */
    public static PuzzlesInfo createSimplePuzzlesInfo(final Context context, int puzzleMode,
                                                      RotationImg[] rotationArr, TemplateData templateData,
                                                      BgTextureData bgTextureData, SignatureData signatureData) {

        PuzzlesInfo puzzlesInfo = TemplateModel.getSimplePuzzlesInfo(context, puzzleMode, rotationArr, templateData);
        if (puzzlesInfo == null) {
            return null;
        }

        //设置背景
        PuzzlesBgTextureInfo bgTextureInfo = BgTextureModel.getBgTextureInfo(
                puzzlesInfo.getPuzzlesRect(), puzzlesInfo.getOutPutRect(), bgTextureData);

        if (bgTextureInfo != null) {
            puzzlesInfo.addPuzzlesBgTextureInfo(bgTextureInfo);
        }

//        //设置签名
//        PuzzlesSignInfo signInfo = PuzzlesSignModel.getSignInfo(puzzlesInfo, signatureData, 0);
//
//        if (signInfo != null) {
//            puzzlesInfo.addPuzzlesSignInfo(signInfo);
//        }
//
//        //昵称
//        setNickName(puzzlesInfo);

        //TODO 名片信息 ?

        return puzzlesInfo;
    }

}
