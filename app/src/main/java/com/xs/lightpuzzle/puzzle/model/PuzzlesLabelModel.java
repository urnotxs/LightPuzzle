package com.xs.lightpuzzle.puzzle.model;

import com.xs.lightpuzzle.puzzle.data.LabelData;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.info.PuzzlesLabelInfo;

/**
 * Created by xs on 2018/4/17.
 */

public class PuzzlesLabelModel {

    /**
     * 获取标签
     * LabelData to PuzzlesLabelInfo
     */
    public static PuzzlesLabelInfo getLabelInfo(PuzzlesInfo puzzlesInfo, LabelData labelData) {
        if (puzzlesInfo.getPuzzlesRect() == null || labelData == null) {
            return null;
        }

        PuzzlesLabelInfo labelInfo = null;
        if (labelData.isUpdate()) {
            for (int i = 0; i < puzzlesInfo.getPuzzlesLabelInfos().size(); i++) {
                if (puzzlesInfo.getPuzzlesLabelInfos().get(i).isLongTouch()) {
                    labelInfo = puzzlesInfo.getPuzzlesLabelInfos().get(i);
                }
            }
        } else {
            labelInfo = new PuzzlesLabelInfo();
            labelInfo.setPuzzleMode(puzzlesInfo.getPuzzleMode());
            labelInfo.setRect(puzzlesInfo.getPuzzlesRect());
            labelInfo.setOutPutRect(puzzlesInfo.getOutPutRect());
        }

        if (labelInfo != null) {
            labelInfo.setPicPath(labelData.getLabelPic());
            labelInfo.setText(labelData.getText());
            labelInfo.setInvert(labelData.isInvert());
            labelInfo.setIconType(labelData.getIconType());
            labelInfo.setLabelType(labelData.getLabelType());
        }

        return labelInfo;
    }

    /**
     * BackLabelData list to PuzzlesLabelInfo list
     */
//    public static List<PuzzlesLabelInfo> getPuzzlesLabelInfoList(Context context, PuzzlesInfo puzzlesInfo,
//                                                                 List<BackLabelData> backLabelList) {
//        if (backLabelList != null && backLabelList.size() > 0) {
//            List<PuzzlesLabelInfo> labelList = new ArrayList<>();
//            for (BackLabelData backLabelData : backLabelList) {
//                PuzzlesLabelInfo labelInfo = backDataToPuzzlesLabelInfo(context, backLabelData,
//                        puzzlesInfo.getPuzzleMode(), puzzlesInfo.getPuzzlesRect(), puzzlesInfo.getOutPutRect());
//                if (labelInfo != null) {
//                    labelList.add(labelInfo);
//                }
//            }
//            return labelList;
//        }
//        return null;
//    }

    /**
     * BackLabelData to PuzzlesLabelInfo
     */
//    public static PuzzlesLabelInfo backDataToPuzzlesLabelInfo(Context context, BackLabelData backLabelData,
//                                                              int puzzlesMode, Rect rect, Rect outRect) {
//        if (backLabelData == null) {
//            return null;
//        }
//        PuzzlesLabelInfo labelInfo = new PuzzlesLabelInfo(context);
//        labelInfo.setText(backLabelData.getText());
//        labelInfo.setInvert(backLabelData.isInvert());
//        labelInfo.setPoint(backLabelData.getPoint());
//        labelInfo.setLabelType(backLabelData.getLabelType());
//        labelInfo.setIconType(backLabelData.getIconType());
//        labelInfo.setRect(backLabelData.getRect());
//        labelInfo.setRect(rect);
//        labelInfo.setOutPutRect(outRect);
//        labelInfo.setPuzzleMode(puzzlesMode);
//
//        return labelInfo;
//    }

    /**
     * BackLabelData to PuzzlesLabelInfo
     */
//    public static BackLabelData puzzlesLabelInfoToBackData(PuzzlesLabelInfo puzzlesLabelInfo) {
//        if (puzzlesLabelInfo == null) {
//            return null;
//        }
//        BackLabelData backLabelData = new BackLabelData();
//        backLabelData.setIconType(puzzlesLabelInfo.getIconType());
//        backLabelData.setLabelType(puzzlesLabelInfo.getLabelType());
//        backLabelData.setPoint(puzzlesLabelInfo.getPoint());
//        backLabelData.setInvert(puzzlesLabelInfo.isInvert());
//        backLabelData.setText(puzzlesLabelInfo.getText());
//        backLabelData.setRect(puzzlesLabelInfo.getRect());
//
//        return backLabelData;
//    }


    /**
     * DraftLabelData list to BackLabelData list
     */
//    public static List<BackLabelData> draftLabelDataToBack(List<DraftLabelData> draftLabelList) {
//        if (draftLabelList == null || draftLabelList.size() == 0) {
//            return null;
//        }
//        List<BackLabelData> backLabelList = new ArrayList<>();
//        for (int i = 0; i < draftLabelList.size(); i++) {
//            DraftLabelData draftLabelData = draftLabelList.get(i);
//            BackLabelData backLabelData = new BackLabelData();
//            backLabelData.setIconType(draftLabelData.getIconType());
//            backLabelData.setLabelType(draftLabelData.getLabelType());
//            backLabelData.setPoint(draftLabelData.getPoint());
//            backLabelData.setInvert(draftLabelData.isInvert());
//            backLabelData.setText(draftLabelData.getText());
//            backLabelData.setRect(draftLabelData.getRect());
//            backLabelList.add(backLabelData);
//        }
//        return backLabelList;
//    }


    /**
     * 还原label 列表
     */
//    public static void setBackLabelInfoList(Context context, PuzzlesInfo scrPuzzlesInfo,
//                                            PuzzlesInfo dstPuzzlesInfo) {
//
//        if (scrPuzzlesInfo == null || dstPuzzlesInfo == null) {
//            return;
//        }
//
//        List<PuzzlesLabelInfo> labelList = scrPuzzlesInfo.getPuzzlesLabelInfos();
//
//        if (labelList != null && labelList.size() > 0) {
//            for (int i = 0; i < labelList.size(); i++) {
//                BackLabelData backLabelData = puzzlesLabelInfoToBackData(labelList.get(i));
//                if (backLabelData != null) {
//                    PuzzlesLabelInfo info = backDataToPuzzlesLabelInfo(context, backLabelData,
//                            dstPuzzlesInfo.getPuzzleMode(), dstPuzzlesInfo.getPuzzlesRect(),
//                            dstPuzzlesInfo.getOutPutRect());
//
//                    if (info != null) {
//                        dstPuzzlesInfo.addPuzzleLabelInfos(info);
//                    }
//                }
//            }
//        }
//
//    }

//    public static void setBackLabelInfoList(Context context,  List<BackLabelData> labelList,
//                                            PuzzlesInfo dstPuzzlesInfo){
//        if (labelList == null || dstPuzzlesInfo == null) {
//            return;
//        }
//
//        if (labelList.size() > 0) {
//            for (int i = 0; i < labelList.size(); i++) {
//                BackLabelData backLabelData = labelList.get(i);
//                if (backLabelData != null) {
//                    PuzzlesLabelInfo info = backDataToPuzzlesLabelInfo(context, backLabelData,
//                            dstPuzzlesInfo.getPuzzleMode(), dstPuzzlesInfo.getPuzzlesRect(),
//                            dstPuzzlesInfo.getOutPutRect());
//
//                    if (info != null) {
//                        dstPuzzlesInfo.addPuzzleLabelInfos(info);
//                    }
//                }
//            }
//        }
//    }

    /**
     * PuzzlesLabelInfo list to BackLabelData list
     * @param labelInfoList List<PuzzlesLabelInfo>
     * */
//    public static List<BackLabelData> puzzlesLabelInfoToBack(List<PuzzlesLabelInfo> labelInfoList) {
//        if(labelInfoList== null || labelInfoList.size() == 0){
//            return null;
//        }
//
//        List<BackLabelData> labelDataList = new ArrayList<>();
//
//        for(PuzzlesLabelInfo info :labelInfoList ){
//            BackLabelData backLabelData = puzzlesLabelInfoToBackData(info);
//            if(backLabelData != null){
//                labelDataList.add(backLabelData);
//            }
//        }
//
//        return labelDataList;
//    }
}
