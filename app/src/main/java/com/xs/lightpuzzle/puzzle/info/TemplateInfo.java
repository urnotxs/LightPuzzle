package com.xs.lightpuzzle.puzzle.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.TemplateData;
import com.xs.lightpuzzle.puzzle.data.lowdata.CardData;
import com.xs.lightpuzzle.puzzle.data.lowdata.FgData;
import com.xs.lightpuzzle.puzzle.data.lowdata.HeadData;
import com.xs.lightpuzzle.puzzle.data.lowdata.ImgPointData;
import com.xs.lightpuzzle.puzzle.data.lowdata.QrCodeData;
import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;
import com.xs.lightpuzzle.puzzle.data.lowdata.VariableFgData;
import com.xs.lightpuzzle.puzzle.data.lowdata.WaterMarkData;
import com.xs.lightpuzzle.puzzle.info.low.PuzzlesFgInfo;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;

import java.util.List;

/**
 * Created by xs on 2018/11/19.
 * 模板信息
 */

public class TemplateInfo {

    //子模板的rect
    private Rect rect;
    //保存时用到的rect
    private Rect outPutRect;
//
    private PuzzlesFgInfo puzzlesFgInfo;
//
//    private PuzzlesMaskInfo puzzlesMaskInfo;
//
//    private PuzzlesQrCodeInfo puzzlesQrCodeInfo;
//
//    private PuzzlesImageInfo[] puzzlesImageInfos;
//
//    private transient PuzzlesLayoutInfo puzzlesLayoutInfo;
//
//    private transient PuzzlesLayoutJointInfo puzzlesLayoutJointInfo;
//
//    private SavePolygonLayoutInfo savePolygonLayoutInfo;
//
//    private transient PuzzlesVoiceSwitchInfo[] puzzlesVoiceSwitchInfos;
//
//    private PuzzlesWaterMarkInfo puzzlesWaterMarkInfo;
//
//    private PuzzlesVarFgInfo[] puzzlesVarFgInfos;
//
//    private PuzzlesTextInfo[] puzzlesTextInfos;
//
//    private PuzzlesCardInfo puzzlesCardInfo;
//
//    private PuzzlesHeadInfo puzzlesHeadInfo;

    private int mPuzzleMode;

    private transient TemplateData templateData;

    private transient RotationImg[] rotationImgs;

    private boolean isSave;

    private boolean isSaveDraft;

//    private int mTemplateDuration = VideoConstant.TEMPLATE_DURATION_10S; // 模板时长，单位ms
//    private int mAllDuration = mTemplateDuration; // 所有视频时长之和，顺序播为模板时长之和，同时播为模板时长
//    private boolean isPlayTogether = true; // 播放模式，true:同时播，false:顺时播

    public TemplateInfo(Context context, int puzzleMode, RotationImg[] rotationImgs,
                        TemplateData templateData) {
        mPuzzleMode = puzzleMode;
        this.rotationImgs = rotationImgs;
        this.templateData = templateData;
        setRotationFilter(context, rotationImgs, templateData);
        //生成rect
        this.rect = PuzzlesUtils.getRect(puzzleMode, templateData);
        this.outPutRect = new Rect(0, 0, templateData.getOutPutWidth(), templateData.getOutPutHeight());
        initAll(templateData, rotationImgs);
//        this.puzzlesDrawInfo = new PuzzlesDrawInfo(puzzleMode, rect, outPutRect, templateData, rotationImgs);
    }

    public TemplateInfo(int puzzleMode, RotationImg[] rotationImgs, TemplateData templateData) {
        mPuzzleMode = puzzleMode;
        this.rotationImgs = rotationImgs;
        this.templateData = templateData;
        //生成rect
        this.rect = PuzzlesUtils.getRect(puzzleMode, templateData);
        this.outPutRect = new Rect(0, 0, templateData.getOutPutWidth(), templateData.getOutPutHeight());
        initAll(templateData, rotationImgs);
//        this.puzzlesDrawInfo = new PuzzlesDrawInfo(puzzleMode, rect, outPutRect, templateData, rotationImgs);
    }

    public void init() {
        // TODO: 2018/11/19 init()
    }

    /**
     * Rect 有变，跟默认数据有差异，需要重置的时候，例如添加或者替换子模板等模板变动操作
     */
    public void resetInit() {
        initAll(templateData, rotationImgs);
    }

    //初始化 Puzzles info
    private void initAll(TemplateData templateData, RotationImg[] rotationImgs) {
        if (templateData == null) {
            return;
        }

        initPuzzleFgInfos(templateData.getFgData());
        initPuzzlesMaskInfo(templateData.getMaskPic());

        initPuzzlesQrCodeInfo(templateData.getQrCodeData());
        if (mPuzzleMode == PuzzleMode.MODE_LAYOUT) {
            initPuzzlesLayoutInfos(templateData.getImgPointDatas(), rotationImgs, templateData.getOutPutWidth());
        } else if (mPuzzleMode == PuzzleMode.MODE_LAYOUT_JOIN) {
            // 基础拼接
            initPuzzlesLayoutJointInfo(rotationImgs);

        } else {
            initPuzzlesImageInfos(templateData.getImgPointDatas(), rotationImgs);
            if (mPuzzleMode == PuzzleMode.MODE_VIDEO) {
                initPuzzlesVoiceSwitchInfo(templateData.getImgPointDatas());
            }
        }
        initPuzzlesWaterMarkInfo(templateData.getWaterMarkData());
        initPuzzlesVarFgInfos(templateData.getVarFgDatas());
        initPuzzlesTextInfos(templateData.getTextData());
        initPuzzlesCardInfo(templateData.getCardData());
        initPuzzlesHeadInfo(templateData.getHeadData());

    }

    /**
     * 上下移动子模板调用
     *
     * @param scrollYOffset 移动动偏移量
     */
    public void resetRectAndScrollYOffset(int scrollYOffset) {
        reSetAllDataRect(rect, outPutRect, scrollYOffset);
    }

    public void reSetAllDataRect(Rect rect, Rect outPutRect, int scrollYOffset) {
        this.rect = rect;
        this.outPutRect = outPutRect;

        if (puzzlesFgInfo != null) {
            puzzlesFgInfo.setRect(rect);
            puzzlesFgInfo.setOutPutRect(outPutRect);
            puzzlesFgInfo.init();
        }
//
//        if (puzzlesMaskInfo != null) {
//            puzzlesMaskInfo.setRect(rect);
//            puzzlesMaskInfo.setOutPutRect(outPutRect);
//            puzzlesMaskInfo.init();
//        }
//
//        if (puzzlesQrCodeInfo != null) {
//            puzzlesQrCodeInfo.setRect(rect);
//            puzzlesQrCodeInfo.setOutPutRect(outPutRect);
//            puzzlesQrCodeInfo.init();
//        }
//
//        if (mPuzzleMode == PuzzleMode.MODE_LAYOUT) {
//            if (puzzlesLayoutInfo != null) {
//                puzzlesLayoutInfo.setRect(rect);
//                puzzlesLayoutInfo.init();
//            }
//
//        } else {
//            if (puzzlesImageInfos != null && puzzlesImageInfos.length > 0) {
//                for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                    puzzlesImageInfo.setRect(rect);
//                    puzzlesImageInfo.setOutPutRect(outPutRect);
//                    puzzlesImageInfo.init();
//                    puzzlesImageInfo.translateOffset(scrollYOffset);
//                }
//            }
//        }
//
//        if (puzzlesWaterMarkInfo != null) {
//            puzzlesWaterMarkInfo.setRect(rect);
//            puzzlesWaterMarkInfo.setOutPutRect(outPutRect);
//            puzzlesWaterMarkInfo.init();
//        }
//
//
//        if (puzzlesVarFgInfos != null && puzzlesVarFgInfos.length > 0) {
//            for (PuzzlesVarFgInfo puzzlesVarFgInfo : puzzlesVarFgInfos) {
//                puzzlesVarFgInfo.setRect(rect);
//                puzzlesVarFgInfo.setOutPutRect(outPutRect);
//                puzzlesVarFgInfo.init();
//            }
//        }
//
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length > 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                puzzlesTextInfo.setRect(rect);
//                puzzlesTextInfo.setOutPutRect(outPutRect);
//                puzzlesTextInfo.translateOffset(scrollYOffset);
//            }
//        }
//
//        if (puzzlesCardInfo != null) {
//            puzzlesCardInfo.setRect(rect);
//            puzzlesCardInfo.setOutPutRect(outPutRect);
//            puzzlesCardInfo.init();
//        }
//
//        if (puzzlesHeadInfo != null) {
//            puzzlesHeadInfo.setRect(rect);
//            puzzlesHeadInfo.setOutPutRect(outPutRect);
//            puzzlesHeadInfo.init();
//        }
    }

    //只有保存才会用到
    public void reInit() {
        if (puzzlesFgInfo != null) {
            puzzlesFgInfo.init();
        }
//        if (puzzlesMaskInfo != null) {
//            puzzlesMaskInfo.init();
//        }
//        if (puzzlesQrCodeInfo != null) {
//            puzzlesQrCodeInfo.init();
//        }
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.init();
//            }
//        }
//
//        if (savePolygonLayoutInfo != null) {
//            savePolygonLayoutInfo.setRect(outPutRect);
//            savePolygonLayoutInfo.init();
//        }
//
//        if (puzzlesWaterMarkInfo != null) {
//            puzzlesWaterMarkInfo.init();
//        }
//        if (puzzlesVarFgInfos != null && puzzlesVarFgInfos.length != 0) {
//            for (PuzzlesVarFgInfo puzzlesVarFgInfo : puzzlesVarFgInfos) {
//                puzzlesVarFgInfo.init();
//            }
//        }
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length != 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                puzzlesTextInfo.init();
//            }
//        }
//        if (puzzlesCardInfo != null) {
//            puzzlesCardInfo.init();
//        }
//        if (puzzlesHeadInfo != null) {
//            puzzlesHeadInfo.init();
//        }
    }

    public void setSave() {
        if (puzzlesFgInfo != null) {
            puzzlesFgInfo.setSave(true);
        }
//        if (puzzlesMaskInfo != null) {
//            puzzlesMaskInfo.setSave(true);
//        }
//        if (puzzlesQrCodeInfo != null) {
//            puzzlesQrCodeInfo.setSave(true);
//        }
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.setSave(true);
//            }
//        }
//
//        isSave = true;
//
//        if (puzzlesWaterMarkInfo != null) {
//            puzzlesWaterMarkInfo.setSave(true);
//        }
//        if (puzzlesVarFgInfos != null && puzzlesVarFgInfos.length != 0) {
//            for (PuzzlesVarFgInfo puzzlesVarFgInfo : puzzlesVarFgInfos) {
//                puzzlesVarFgInfo.setSave(true);
//            }
//        }
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length != 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                puzzlesTextInfo.setSave(true);
//            }
//        }
//        if (puzzlesCardInfo != null) {
//            puzzlesCardInfo.setSave(true);
//        }
//        if (puzzlesHeadInfo != null) {
//            puzzlesHeadInfo.setSave(true);
//        }
    }

    public void setDraftSave() {
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.setSelectForLong(false);
//            }
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            puzzlesLayoutJointInfo.clearSelectedPiece();
//        }
//        isSaveDraft = true;
    }

    public void initBitmap(Context context, PuzzlesBgTextureInfo bgTextureInfo) {
        if (puzzlesFgInfo != null) {
            puzzlesFgInfo.initBitmap(context);
        }
//        if (puzzlesMaskInfo != null) {
//            puzzlesMaskInfo.initBitmap(context);
//            if (bgTextureInfo != null) {
//                puzzlesMaskInfo.changeBgTexture(bgTextureInfo.getColorTextureBitmap(), bgTextureInfo.getBgColor());
//            }
//        }
//        if (puzzlesQrCodeInfo != null) {
//            puzzlesQrCodeInfo.initBitmap(context);
//        }
//
//        if (puzzlesVoiceSwitchInfos != null && puzzlesVoiceSwitchInfos.length != 0) {
//            for (PuzzlesVoiceSwitchInfo puzzlesVoiceSwitchInfo : puzzlesVoiceSwitchInfos) {
//                puzzlesVoiceSwitchInfo.initBitmap(context);
//            }
//        }
//
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.initBitmap(context);
//            }
//        }
//        if (isSave) {
//            if (savePolygonLayoutInfo != null) {
//                savePolygonLayoutInfo.initBitmap(context);
//            }
//        } else {
//            if (puzzlesLayoutInfo != null) {
//                puzzlesLayoutInfo.initBitmap(context);
//            }
//            if (puzzlesLayoutJointInfo != null) {
//                puzzlesLayoutJointInfo.initBitmap(context);
//            }
//        }
//
//        if (puzzlesWaterMarkInfo != null) {
//            if (bgTextureInfo != null) {
//                puzzlesWaterMarkInfo.setColor(bgTextureInfo.getWaterColor());
//            }
//            puzzlesWaterMarkInfo.initBitmap(context);
//        }
//        if (puzzlesVarFgInfos != null && puzzlesVarFgInfos.length != 0) {
//            for (PuzzlesVarFgInfo puzzlesVarFgInfo : puzzlesVarFgInfos) {
//                if (bgTextureInfo != null) {
//                    puzzlesVarFgInfo.setColor(bgTextureInfo.getWaterColor());
//                }
//                puzzlesVarFgInfo.initBitmap(context);
//            }
//        }
//
//
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length != 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                puzzlesTextInfo.initBitmap(context);
//            }
//        }
//        if (puzzlesCardInfo != null) {
//            puzzlesCardInfo.initBitmap(context);
//        }
//        if (puzzlesHeadInfo != null) {
//            puzzlesHeadInfo.initBitmap(context);
//        }
    }

    public void draw(Canvas canvas) {
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.draw(canvas);
//            }
//        }
//        if (isSave) {
//            if (savePolygonLayoutInfo != null) {
//                savePolygonLayoutInfo.draw(canvas);
//            }
//        } else {
//            if (puzzlesLayoutInfo != null) {
//                puzzlesLayoutInfo.draw(canvas);
//            }
//            if (puzzlesLayoutJointInfo != null) {
//                puzzlesLayoutJointInfo.draw(canvas);
//            }
//        }
//
//        if (puzzlesHeadInfo != null) {
//            puzzlesHeadInfo.draw(canvas);
//        }
//        if (puzzlesMaskInfo != null) {
//            puzzlesMaskInfo.draw(canvas);
//        }
        if (puzzlesFgInfo != null) {
            puzzlesFgInfo.draw(canvas);
        }
//        if (puzzlesVarFgInfos != null && puzzlesVarFgInfos.length != 0) {
//            for (PuzzlesVarFgInfo puzzlesVarFgInfo : puzzlesVarFgInfos) {
//                puzzlesVarFgInfo.draw(canvas);
//            }
//        }
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length != 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                puzzlesTextInfo.draw(canvas);
//            }
//        }
//        if (puzzlesWaterMarkInfo != null) {
//            puzzlesWaterMarkInfo.draw(canvas);
//        }
//        if (puzzlesQrCodeInfo != null) {
//            puzzlesQrCodeInfo.draw(canvas);
//        }
//        if (puzzlesCardInfo != null) {
//            puzzlesCardInfo.draw(canvas);
//        }
//
//        if (!isSave) {
//            if (mPuzzleMode == PuzzleMode.MODE_VIDEO) {
//                // 如果是视频，需要在图片左上角显示原声开关的icon
//                boolean isFirstVideo = true;
//
//                if (puzzlesVoiceSwitchInfos != null) {
//                    for (int i = 0; i < puzzlesVoiceSwitchInfos.length; i++) {
//                        if (puzzlesImageInfos != null
//                                && i < puzzlesImageInfos.length
//                                && puzzlesImageInfos[i].getRotationImg() instanceof UserVideoInfo) {
//                            UserVideoInfo userVideoInfo = (UserVideoInfo) puzzlesImageInfos[i].getRotationImg();
//                            if (!userVideoInfo.isPicMixVideo()) {
//                                boolean isOpen = userVideoInfo.isSoundtrackingOpen();
//                                puzzlesVoiceSwitchInfos[i].draw(canvas, isOpen, isFirstVideo);
//                                isFirstVideo = false;
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (isSaveDraft) {
//                isSaveDraft = false;
//            } else {
//                if (rect != null && rect.top > 0) {
//                    //若为长图，画分割的虚线
//                    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//                    mPaint.setColor(Color.parseColor("#F18390"));
//                    // 需要加上这句，否则画不出东西
//                    mPaint.setStyle(Paint.Style.STROKE);
//                    mPaint.setStrokeWidth(5);
//                    mPaint.setPathEffect(new DashPathEffect(new float[]{15, 8}, 0));
//
//                    Path mPath = new Path();
//                    int centerY = rect.top;
//                    mPath.reset();
//                    mPath.moveTo(0, centerY);
//                    mPath.lineTo(rect.width(), centerY);
//                    canvas.drawPath(mPath, mPaint);
//                }
//            }
//        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean flag = false;
//        if (puzzlesVoiceSwitchInfos != null) {
//            for (int i = 0; i < puzzlesVoiceSwitchInfos.length; i++) {
//                PuzzlesVoiceSwitchInfo voiceSwitchInfo = puzzlesVoiceSwitchInfos[i];
//                if (puzzlesImageInfos != null
//                        && i < puzzlesImageInfos.length
//                        && puzzlesImageInfos[i].getRotationImg() instanceof UserVideoInfo) {
//                    UserVideoInfo userVideoInfo = (UserVideoInfo) puzzlesImageInfos[i].getRotationImg();
//                    if (!userVideoInfo.isPicMixVideo()) {
//                        if (voiceSwitchInfo.onTouchEvent(event)) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        if (puzzlesCardInfo != null) {
//            flag = puzzlesCardInfo.onTouchEvent(event);
//            if (flag) {
//                return flag;
//            }
//        }
//        if (puzzlesQrCodeInfo != null) {
//            flag = puzzlesQrCodeInfo.onTouchEvent(event);
//            if (flag) {
//                return flag;
//            }
//        }
//        if (puzzlesWaterMarkInfo != null) {
//            flag = puzzlesWaterMarkInfo.onTouchEvent(event);
//            if (flag) {
//                return flag;
//            }
//        }
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length != 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                flag = puzzlesTextInfo.onTouchEvent(event);
//                if (flag) {
//                    return flag;
//                }
//            }
//        }
//        if (puzzlesHeadInfo != null) {
//            flag = puzzlesHeadInfo.onTouchEvent(event);
//            if (flag) {
//                return flag;
//            }
//        }
//        if (puzzlesImageInfos != null) {
//            for (int i = puzzlesImageInfos.length - 1; i >= 0; i--) {
//                PuzzlesImageInfo imageInfo = puzzlesImageInfos[i];
//                if (imageInfo.onTouchEvent(event)) {
//                    return true;
//                }
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            flag = puzzlesLayoutInfo.onTouchEvent(event);
//            if (flag) {
//                return flag;
//            }
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            flag = puzzlesLayoutJointInfo.onTouchEvent(event);
//            if (flag) {
//                return flag;
//            }
//        }
        return flag;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    private void initPuzzleFgInfos(FgData fgData) {
        if (fgData == null || TextUtils.isEmpty(fgData.getFgPic())) {
            return;
        }
        if (puzzlesFgInfo == null) {
            puzzlesFgInfo = new PuzzlesFgInfo();
        }
        puzzlesFgInfo.setFgPic(fgData.getFgPic());
        puzzlesFgInfo.setRect(rect);
        puzzlesFgInfo.setOutPutRect(outPutRect);
        puzzlesFgInfo.init();
    }

    private void initPuzzlesMaskInfo(String maskPic) {
        if (TextUtils.isEmpty(maskPic)) {
            return;
        }
//        if (puzzlesMaskInfo == null) {
//            puzzlesMaskInfo = new PuzzlesMaskInfo();
//        }
//        puzzlesMaskInfo.setMaskPic(maskPic);
//        puzzlesMaskInfo.setRect(rect);
//        puzzlesMaskInfo.setOutPutRect(outPutRect);
//        puzzlesMaskInfo.init();
    }

    private void initPuzzlesQrCodeInfo(QrCodeData qrCodeData) {
        if (qrCodeData == null || qrCodeData.getQrCodePoint() == null) {
            return;
        }
//        if (puzzlesQrCodeInfo == null) {
//            puzzlesQrCodeInfo = new PuzzlesQrCodeInfo();
//        }
//        puzzlesQrCodeInfo.setQrPoint(qrCodeData.getQrCodePoint());
//        puzzlesQrCodeInfo.setQrPic(qrCodeData.getQrCodePic());
//        puzzlesQrCodeInfo.setRect(rect);
//        puzzlesQrCodeInfo.setOutPutRect(outPutRect);
//        puzzlesQrCodeInfo.init();
    }

    private void initPuzzlesImageInfos(List<ImgPointData> imgPointDatas, RotationImg[] rotationImgs) {
        if (imgPointDatas == null || imgPointDatas.size() == 0) {
            return;
        }
        if (rotationImgs == null || rotationImgs.length == 0) {
            return;
        }
        if (imgPointDatas.size() != rotationImgs.length) {
            return;
        }

//        puzzlesImageInfos = new PuzzlesImageInfo[imgPointDatas.size()];
//
//        for (int i = 0; i < imgPointDatas.size(); i++) {
//            PuzzlesImageInfo puzzlesImageInfo = new PuzzlesImageInfo();
//            puzzlesImageInfo.setId(PuzzlesInfoHelper.getInstance().getImgIdCount());
//            puzzlesImageInfo.setImgPoint(imgPointDatas.get(i).getPicPointF());
//            puzzlesImageInfo.setRect(rect);
//            puzzlesImageInfo.setOutPutRect(outPutRect);
//            puzzlesImageInfo.setRotationImg(rotationImgs[i]);
//            puzzlesImageInfo.setPuzzleMode(mPuzzleMode);
//            puzzlesImageInfo.init();
//            puzzlesImageInfos[i] = puzzlesImageInfo;
//        }
    }

    private void initPuzzlesVoiceSwitchInfo(List<ImgPointData> imgPointDatas) {
        if (imgPointDatas == null || imgPointDatas.size() == 0) {
            return;
        }
//        puzzlesVoiceSwitchInfos = new PuzzlesVoiceSwitchInfo[imgPointDatas.size()];
//
//        for (int i = 0; i < imgPointDatas.size(); i++) {
//            PuzzlesVoiceSwitchInfo puzzlesVoiceSwitchInfo = new PuzzlesVoiceSwitchInfo();
//            puzzlesVoiceSwitchInfo.setImgPoint(imgPointDatas.get(i).getPicPointF());
//            puzzlesVoiceSwitchInfo.setRect(rect);
//            puzzlesVoiceSwitchInfo.setPuzzleMode(mPuzzleMode);
//            puzzlesVoiceSwitchInfo.init();
//            puzzlesVoiceSwitchInfos[i] = puzzlesVoiceSwitchInfo;
//        }
    }

    private void initPuzzlesLayoutJointInfo(RotationImg[] rotationImgs) {
        if (rotationImgs == null || rotationImgs.length == 0) {
            return;
        }
//        if (puzzlesLayoutJointInfo == null) {
//            puzzlesLayoutJointInfo = new PuzzlesLayoutJointInfo();
//        }
//        puzzlesLayoutJointInfo.setRotationImgs(rotationImgs);
//        puzzlesLayoutJointInfo.setRect(rect);
//        puzzlesLayoutJointInfo.setOutPutRect(outPutRect);
//        puzzlesLayoutJointInfo.init();
    }

    private void initPuzzlesLayoutInfos(List<ImgPointData> imgPointDatas, RotationImg[] rotationImgs, int outPutWidth) {
        if (imgPointDatas == null || imgPointDatas.size() == 0) {
            return;
        }
        if (rotationImgs == null || rotationImgs.length == 0) {
            return;
        }
        if (imgPointDatas.size() != rotationImgs.length) {
            return;
        }
//        if (puzzlesLayoutInfo == null) {
//            puzzlesLayoutInfo = new PuzzlesLayoutInfo();
//        }
//
//        puzzlesLayoutInfo.setPicPathList(rotationImgs);
//        puzzlesLayoutInfo.setRect(rect);
//        puzzlesLayoutInfo.setSelectedRatio((rect.width() * 1.0f) / rect.height());
//        puzzlesLayoutInfo.setSelectedLayout(0);
//        List<PointF[]> mImgPoints = new ArrayList<>();
//        for (int i = 0; i < imgPointDatas.size(); i++) {
//            mImgPoints.add(imgPointDatas.get(i).getPicPointF());
//        }
//        puzzlesLayoutInfo.setData(new LayoutData().generateLayoutData(PuzzleOutputBitmapContant.PRIVATE_SIZE, mImgPoints));
//        puzzlesLayoutInfo.init();
//        puzzlesLayoutInfo.setIsFirstTime(true);
    }

    private void initPuzzlesWaterMarkInfo(WaterMarkData waterMarkData) {
        if (waterMarkData == null || waterMarkData.getWaterPoint() == null
                || TextUtils.isEmpty(waterMarkData.getWaterPic())) {
            return;
        }
//        if (puzzlesWaterMarkInfo == null) {
//            puzzlesWaterMarkInfo = new PuzzlesWaterMarkInfo();
//        }
//        puzzlesWaterMarkInfo.setWaterPic(waterMarkData.getWaterPic());
//        puzzlesWaterMarkInfo.setWaterPoint(waterMarkData.getWaterPoint());
//        puzzlesWaterMarkInfo.setVip(!AppConfiguration.getDefault().isDrawWatermark());
//        puzzlesWaterMarkInfo.setRect(rect);
//        puzzlesWaterMarkInfo.setOutPutRect(outPutRect);
//        puzzlesWaterMarkInfo.init();
    }

    private void initPuzzlesVarFgInfos(List<VariableFgData> variableFgDatas) {
        if (variableFgDatas == null || variableFgDatas.size() == 0) {
            return;
        }

//        puzzlesVarFgInfos = new PuzzlesVarFgInfo[variableFgDatas.size()];
//
//        for (int i = 0; i < variableFgDatas.size(); i++) {
//            PuzzlesVarFgInfo puzzlesVarFgInfo = new PuzzlesVarFgInfo();
//            puzzlesVarFgInfo.setVarFgPic(variableFgDatas.get(i).getVarFgPic());
//            puzzlesVarFgInfo.setVarFgPoint(variableFgDatas.get(i).getVarFgPoint());
//            puzzlesVarFgInfo.setRect(rect);
//            puzzlesVarFgInfo.setOutPutRect(outPutRect);
//            puzzlesVarFgInfo.init();
//            if (puzzlesVarFgInfos[i] != null) {
//                puzzlesVarFgInfo.setColor(puzzlesVarFgInfos[i].getColor());
//            }
//            puzzlesVarFgInfos[i] = puzzlesVarFgInfo;
//        }
    }

    private void initPuzzlesTextInfos(List<TextData> textDatas) {
        if (textDatas == null || textDatas.size() == 0) {
            return;
        }
//        puzzlesTextInfos = new PuzzlesTextInfo[textDatas.size()];
//
//        for (int i = 0; i < textDatas.size(); i++) {
//            PuzzlesTextInfo puzzlesTextInfo = new PuzzlesTextInfo();
//            puzzlesTextInfo.setId(PuzzlesInfoHelper.getInstance().getTextIdCount());
//            puzzlesTextInfo.setTextData(textDatas.get(i));
//            puzzlesTextInfo.setRect(rect);
//            puzzlesTextInfo.setOutPutRect(outPutRect);
//            puzzlesTextInfo.init();
//            if (puzzlesTextInfos[i] != null) {
//                puzzlesTextInfo.setAutoStr(puzzlesTextInfos[i].getAutoStr());
//                puzzlesTextInfo.setFontSize(puzzlesTextInfos[i].getFontSize());
//                puzzlesTextInfo.setFont(puzzlesTextInfos[i].getFont());
//                puzzlesTextInfo.changeFontColor(puzzlesTextInfos[i].getFontColor());
//                puzzlesTextInfo.setDownloadFont(puzzlesTextInfos[i].isDownloadFont());
//            }
//            puzzlesTextInfos[i] = puzzlesTextInfo;
//        }
    }

    private void initPuzzlesCardInfo(CardData cardData) {
        if (cardData == null || cardData.getCardPoint() == null) {
            return;
        }
//        if (puzzlesCardInfo == null) {
//            puzzlesCardInfo = new PuzzlesCardInfo();
//        }
//        puzzlesCardInfo.setCardData(cardData);
//        puzzlesCardInfo.setRect(rect);
//        puzzlesCardInfo.setOutPutRect(outPutRect);
//        puzzlesCardInfo.init();
    }

    private void initPuzzlesHeadInfo(HeadData headData) {
        if (headData == null || headData.getHeadPoint() == null) {
            return;
        }
//        if (puzzlesHeadInfo == null) {
//            puzzlesHeadInfo = new PuzzlesHeadInfo();
//        }
//        puzzlesHeadInfo.setHeadPic(headData.getHeadPic());
//        puzzlesHeadInfo.setHeadPoint(headData.getHeadPoint());
//        puzzlesHeadInfo.setRect(rect);
//        puzzlesHeadInfo.setOutPutRect(outPutRect);
//        puzzlesHeadInfo.init();
    }


    // --- 背景

    /**
     * 改变可变背景、字体、水印、名片信息颜色
     *
     * @param fontColor          字体颜色
     * @param bgColor
     * @param colorTextureBitmap
     */
    public void changeBgTexture(int fontColor, int bgColor, Bitmap colorTextureBitmap) {
//        if (puzzlesWaterMarkInfo != null) {
//            puzzlesWaterMarkInfo.changeWaterColor(fontColor);
//        }
//        if (puzzlesVarFgInfos != null) {
//            for (PuzzlesVarFgInfo varFgInfo : puzzlesVarFgInfos) {
//                varFgInfo.changeFgColor(fontColor);
//            }
//        }
//        if (puzzlesTextInfos != null) {
//            for (PuzzlesTextInfo textInfo : puzzlesTextInfos) {
//                textInfo.changeFontColor(fontColor);
//            }
//        }
//        if (puzzlesMaskInfo != null) {
//            puzzlesMaskInfo.changeBgTexture(colorTextureBitmap, bgColor);
//        }
//
//        if (puzzlesCardInfo != null) {
//            puzzlesCardInfo.changeFontColor(fontColor);
//        }
    }

    // --- 头像
    public void setHeadPic(Context context, String picPath) {
//        if (puzzlesHeadInfo != null) {
//            puzzlesHeadInfo.setHeadPic(picPath);
//            puzzlesHeadInfo.initBitmap(context);
//        }
    }

    // --- 水印
    public void cleanUpWaterMark() {
//        if (puzzlesWaterMarkInfo != null) {
//            puzzlesWaterMarkInfo.setVip(true);
//        }
    }

    // --- 名片
    public void setCardKeys(Context context, List<String> keys) {
//        if (puzzlesCardInfo == null) {
//            return;
//        }
//        puzzlesCardInfo.setDrawKeys(keys);
//        puzzlesCardInfo.initBitmap(context);
    }

    // --- 二维码
    public void setQrCode(Context context, String qrPic) {
//        if (puzzlesQrCodeInfo == null) {
//            return;
//        }
//        puzzlesQrCodeInfo.setQrPic(qrPic);
//        puzzlesQrCodeInfo.initBitmap(context);
    }

    // --- 图片
    public void resetId() {
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length > 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                puzzlesTextInfo.setId(PuzzlesInfoHelper.getInstance().getTextIdCount());
//                PuzzlesInfoHelper.getInstance().putTextPoint(puzzlesTextInfo.getDrawPoint());
//            }
//        }
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length > 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.setId(PuzzlesInfoHelper.getInstance().getImgIdCount());
//                PuzzlesInfoHelper.getInstance().putImgPoint(puzzlesImageInfo.getDrawPoint());
//            }
//        }
    }

    public void reSavePoints() {
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length > 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                PuzzlesInfoHelper.getInstance().putTextPoint(puzzlesTextInfo.getDrawPoint());
//            }
//        }
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length > 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                if (puzzlesImageInfo.getRotationImg() != null && !TextUtils.isEmpty(puzzlesImageInfo.getRotationImg().getPicPath())) {
//                    PuzzlesInfoHelper.getInstance().putImgPoint(puzzlesImageInfo.getDrawPoint());
//                }
//            }
//        }
    }

    public void showImageFram(Point[] imgPoint) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                imageInfo.setShowFrame(false);
//                if (imgPoint != null && imageInfo.getDrawPoint() == imgPoint) {
//                    imageInfo.setShowFrame(true);
//                }
//            }
//        }
    }

    public void setFirstImageShowBar() {
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            if (mPuzzleMode == PuzzleMode.MODE_LONG || mPuzzleMode == PuzzleMode.MODE_JOIN) {
//                puzzlesImageInfos[0].postSelectForLong(MotionEvent.ACTION_UP);
//            }
//            puzzlesImageInfos[0].showToolBar(MotionEvent.ACTION_UP);
//        }
    }

    public void setPuzzlesImagePic(Context context, RotationImg rotationImg, Point[] points) {
        int index = getPuzzlesImagePicIndex(context, rotationImg, points);
        if (mPuzzleMode != PuzzleMode.MODE_LAYOUT && mPuzzleMode != PuzzleMode.MODE_LAYOUT_JOIN) {
            if (index != -1 && index < rotationImgs.length) {
                rotationImgs[index] = rotationImg;
            }
        }
    }

    private int getPuzzlesImagePicIndex(Context context, RotationImg rotationImg, Point[] points) {
//        if (puzzlesImageInfos != null) {
//            for (int i = 0; i < puzzlesImageInfos.length; i++) {
//                if (ShapeUtils.pointEqual(puzzlesImageInfos[i].getDrawPoint(), points)) {
//                    RotationImg sourImg = puzzlesImageInfos[i].getRotationImg();
//                    if (sourImg != null) {
//                        if (sourImg.getTepFilterInfo() != null) {
//                            rotationImg.setTepFilterInfo(sourImg.getTepFilterInfo());
//                        }
//                        rotationImg.setSkinSmoothAlpha(sourImg.getSkinSmoothAlpha());
//                        rotationImg.setSkinColorAlpha(sourImg.getSkinColorAlpha());
//                        rotationImg.setChangedBeauty(sourImg.isChangedBeauty());
//                    }
//
//
//                    if (mPuzzleMode == PuzzleMode.MODE_VIDEO
//                            && sourImg instanceof UserVideoInfo
//                            && rotationImg instanceof UserVideoInfo) {
//                        UserVideoInfo srcUserVideoInfo = (UserVideoInfo) sourImg;
//                        UserVideoInfo dstUserVideoInfo = (UserVideoInfo) rotationImg;
//                        dstUserVideoInfo.keepPartParams(srcUserVideoInfo);
//                    }
//
//                    puzzlesImageInfos[i].setRotationImg(rotationImg);
//                    puzzlesImageInfos[i].initBitmap(context);
//                    PuzzlesInfoHelper.getInstance().putImgPoint(puzzlesImageInfos[i].getDrawPoint());
//                    return i;
//                }
//            }
//        }
//
//        if (puzzlesLayoutInfo != null) {
//            int index = puzzlesLayoutInfo.getRotationImgIndex();
//            if (index >= 0) {
//                RotationImg img = puzzlesLayoutInfo.getRotationImg()[index];
//                img.setPicPath(rotationImg.getPicPath());
//                puzzlesLayoutInfo.setReloadImg(index, false);
//                puzzlesLayoutInfo.initBitmap(context);
//            }
//            return index;
//        }
//
//        if (puzzlesLayoutJointInfo != null) {
//            int index = puzzlesLayoutJointInfo.getRotationImgIndex();
//            if (index >= 0) {
//                RotationImg img = puzzlesLayoutJointInfo.getRotationImg()[index];
//                img.setPicPath(rotationImg.getPicPath());
//                puzzlesLayoutJointInfo.setReloadImg(index, false);
//                puzzlesLayoutJointInfo.initBitmap(context);
//            }
//            return index;
//        }

        return -1;
    }

    public void zoomInImg(Point[] points) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                if (ShapeUtils.pointEqual(imageInfo.getDrawPoint(), points)) {
//                    imageInfo.zoomIn();
//                    return;
//                }
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            puzzlesLayoutInfo.setImageZoomIn(0);
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            puzzlesLayoutJointInfo.setImageZoomIn();
//        }
    }

    public void zoomOutImg(Point[] points) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                if (ShapeUtils.pointEqual(imageInfo.getDrawPoint(), points)) {
//                    imageInfo.zoomOut();
//                    return;
//                }
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            puzzlesLayoutInfo.setImageZoomOut(0);
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            puzzlesLayoutJointInfo.setImageZoomOut();
//        }
    }

//    public PuzzlesImageInfo getSelectInfo() {
//        PuzzlesImageInfo selectInfo = null;
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                if (imageInfo.isSelectForLong()) {
//                    selectInfo = imageInfo;
//                }
//            }
//        }
//        return selectInfo;
//    }
//
//    public PuzzlesImageInfo getSourceImageInfo(Point[] sourcePoint) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                imageInfo.setShowFrame(false);
//                if (sourcePoint != null && ShapeUtils.pointEqual(imageInfo.getDrawPoint(), sourcePoint)) {
//                    return imageInfo;
//                }
//            }
//        }
//        return null;
//    }
//
//    public PuzzlesImageInfo getChangeImageInfo(Point[] changePoint) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                imageInfo.setShowFrame(false);
//                if (changePoint != null && ShapeUtils.pointEqual(imageInfo.getDrawPoint(), changePoint)) {
//                    return imageInfo;
//                }
//            }
//        }
//        return null;
//    }

    /**
     * 旋转角度
     */
    public void setImgRotation(Context context, Point[] points) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                if (ShapeUtils.pointEqual(imageInfo.getDrawPoint(), points)) {
//                    imageInfo.setRotation(90);
//                    imageInfo.initBitmap(context);
//                    return;
//                }
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            puzzlesLayoutInfo.setImageRotate(0);
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            puzzlesLayoutJointInfo.setImageRotate();
//        }
    }

//    public void setPicFilter(Context context, TepFilterInfo tepFilterInfo, int id) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                if (imageInfo.getId() == id) {
//                    RotationImg rotationImg = imageInfo.getRotationImg();
//                    if (rotationImg != null) {
//                        if (rotationImg.getTepFilterInfo() != null
//                                && rotationImg.getTepFilterInfo().getId() == tepFilterInfo.getId()) {
//                            return;
//                        }
//                        rotationImg.setTepFilterInfo(tepFilterInfo);
//                    }
//                    imageInfo.changeFilterEffect();
//                    imageInfo.generateFinalBmp();
//                    return;
//                }
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            RotationImg rotationImg = puzzlesLayoutInfo.getRotationImg()[id];
//            if (rotationImg != null) {
//                if (rotationImg.getTepFilterInfo() != null
//                        && rotationImg.getTepFilterInfo().getName().equals(tepFilterInfo.getName())) {
//                    return;
//                }
//                rotationImg.setTepFilterInfo(tepFilterInfo);
//            }
//            puzzlesLayoutInfo.changeFilterEffect(id);
//            puzzlesLayoutInfo.generateFinal(id);
//
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            RotationImg rotationImg = puzzlesLayoutJointInfo.getRotationImg()[id];
//            if (rotationImg != null) {
//                if (rotationImg.getTepFilterInfo() != null
//                        && rotationImg.getTepFilterInfo().getName().equals(tepFilterInfo.getName())) {
//                    return;
//                }
//                rotationImg.setTepFilterInfo(tepFilterInfo);
//            }
//            puzzlesLayoutJointInfo.changeFilterEffect(id);
//            puzzlesLayoutJointInfo.generateFinal(id);
//
//        }
//    }

    public void setPicFilterAlpha(float alpha, int id) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                if (imageInfo.getId() == id) {
//                    RotationImg rotationImg = imageInfo.getRotationImg();
//                    if (rotationImg.getTepFilterInfo() != null) {
//                        rotationImg.getTepFilterInfo().setAlpha(alpha * 100);
//                    }
//                    imageInfo.changeFilterAlphaEffect();
//                    imageInfo.generateFinalBmp();
//                    return;
//                }
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            RotationImg rotationImg = puzzlesLayoutInfo.getRotationImg()[id];
//            if (rotationImg.getTepFilterInfo() != null) {
//                rotationImg.getTepFilterInfo().setAlpha(alpha * 100);
//            }
//            puzzlesLayoutInfo.changeFilterAlphaEffect(id);
//            puzzlesLayoutInfo.generateFinal(id);
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            RotationImg rotationImg = puzzlesLayoutJointInfo.getRotationImg()[id];
//            if (rotationImg.getTepFilterInfo() != null) {
//                rotationImg.getTepFilterInfo().setAlpha(alpha * 100);
//            }
//            puzzlesLayoutJointInfo.changeFilterAlphaEffect(id);
//            puzzlesLayoutJointInfo.generateFinal(id);
//        }
    }

    public void setImgSelectForLong(Point[] points) {
//        if (puzzlesImageInfos != null) {
//            for (PuzzlesImageInfo imageInfo : puzzlesImageInfos) {
//                if (ShapeUtils.pointEqual(imageInfo.getDrawPoint(), points)) {
//                    imageInfo.setSelectForLong(true);
//                } else {
//                    imageInfo.setSelectForLong(false);
//                }
//            }
//        }
    }

    public void setValues() {
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.matrixToValues();
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            puzzlesLayoutInfo.generateSavePolygonLayoutInfo();
//            savePolygonLayoutInfo = puzzlesLayoutInfo.getSavePolygonLayoutInfo();
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            puzzlesLayoutJointInfo.generateSavePolygonLayoutInfo();
//            savePolygonLayoutInfo = puzzlesLayoutJointInfo.getSavePolygonLayoutInfo();
//        }
    }

    // --- 布局
    public void onLayoutClearSelected() {
//        if (puzzlesLayoutInfo != null) {
//            puzzlesLayoutInfo.clearSelectedPiece();
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            puzzlesLayoutJointInfo.clearSelectedPiece();
//        }
    }

    public void changeLayoutView(Context context, boolean save) {
//        if (puzzlesLayoutInfo != null) {
//            if (save) {
//                //保存原有布局数据
//                puzzlesLayoutInfo.generateSavePolygonLayoutInfo();
//                this.rect = PuzzlesUtils.getRect(mPuzzleMode, templateData);
//            } else {
//                if (puzzlesLayoutInfo.getSavePolygonLayoutInfo() != null) {
//                    LayoutData layoutData = puzzlesLayoutInfo.getSavePolygonLayoutInfo().getLayoutData();
//                    puzzlesLayoutInfo.setRect(rect);
//                    puzzlesLayoutInfo.init(layoutData);
//                    puzzlesLayoutInfo.clearPieces();
//                    puzzlesLayoutInfo.addPieces(context, puzzlesLayoutInfo.getFinalBmpArray(), layoutData.getDrawableVOS(),
//                            puzzlesLayoutInfo.getSavePolygonLayoutInfo().getZoomArray());
//                }
//            }
//        }
    }

    public void changeLayoutJointView(Rect[] rect) {
        this.rect = rect[0];
        this.outPutRect = rect[1];
    }

//    public void onLayoutChanged(Context context, boolean changedCanvas,
//                                float ratio, int layout, LayoutData layoutData) {
//        if (changedCanvas) {
//            templateData.setSizeRatio(ratio);
//            int[] size = new int[2];
//            if (ratio < 1) {
//                size[0] = (int) (PuzzleOutputBitmapContant.PRIVATE_SIZE * ratio);
//                size[1] = PuzzleOutputBitmapContant.PRIVATE_SIZE;
//            } else {
//                size[0] = PuzzleOutputBitmapContant.PRIVATE_SIZE;
//                size[1] = (int) (PuzzleOutputBitmapContant.PRIVATE_SIZE / ratio);
//            }
//            templateData.setOutPutWidth(size[0]);
//            templateData.setOutPutHeight(size[1]);
//
//            this.rect = PuzzlesUtils.getRect(mPuzzleMode, templateData);
//            this.outPutRect = new Rect(0, 0, templateData.getOutPutWidth(), templateData.getOutPutHeight());
//        }
//        if (puzzlesLayoutInfo != null) {
//            puzzlesLayoutInfo.setSelectedRatio(ratio);
//            puzzlesLayoutInfo.setSelectedLayout(layout);
//            puzzlesLayoutInfo.setRect(rect);
//            layoutData.setInsidePaddingRatio(puzzlesLayoutInfo.getPiecePaddingRatio());
//            layoutData.setOutsidePaddingRatio(puzzlesLayoutInfo.getOuterPaddingRatio());
//            layoutData.setRadianRatio(puzzlesLayoutInfo.getPieceRadianRatio());
//            if (changedCanvas) {
//                puzzlesLayoutInfo.init(layoutData);
//                puzzlesLayoutInfo.clearPieces();
//                puzzlesLayoutInfo.addPiecesWithFilter();
//            } else {
//                puzzlesLayoutInfo.setLayoutData(layoutData);
//            }
//        }
//    }
//
//    public void onLayoutPaddingChanged(Context context, BottomEditLineFrameView.CHANGEDMode mode, int value) {
//        if (puzzlesLayoutInfo != null) {
//            switch (mode) {
//                case OUT_PADDING:
//                    puzzlesLayoutInfo.setOuterPadding(value / 100.0f);
//                    break;
//                case IN_PADDING:
//                    puzzlesLayoutInfo.setPiecePadding(value / 100.0f);
//                    break;
//                case RADIAN:
//                    puzzlesLayoutInfo.setPieceRadianRatio(value / 100.0f);
//                    break;
//            }
//
//        }
//    }

    // --- rotationImg
    public RotationImg getRotationImg(Point[] points) {
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                if (puzzlesImageInfo.getDrawPoint() == points) {
//                    return puzzlesImageInfo.getRotationImg();
//                }
//            }
//        }
        return null;
    }

    public RotationImg getRotationImgFromVoice(Point[] points) {
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                Rect rect = ShapeUtils.makeRect(points);
//                Rect dstRect = ShapeUtils.makeRect(puzzlesImageInfo.getDrawPoint());
//                if (dstRect.contains(rect)) {
//                    return puzzlesImageInfo.getRotationImg();
//                }
//            }
//        }
        return null;
    }

    /**
     * 获取所有图片的旋转角度
     *
     * @return List<Integer> image rotate list
     */
    public List<Integer> getImageRotate() {
//        if (puzzlesImageInfos != null) {
//            List<Integer> rotateList = new ArrayList<Integer>();
//            for (PuzzlesImageInfo info : puzzlesImageInfos) {
//                rotateList.add(info.getRotation());
//            }
//            return rotateList;
//        }
        return null;
    }

    /**
     * 设置图片的旋转角度
     *
     * @param list image rotate list
     * @return void
     */
    public void setImageRotate(List<Integer> list) {
//        if (puzzlesImageInfos != null && list != null) {
//            int size = list.size() > puzzlesImageInfos.length ? puzzlesImageInfos.length : list.size();
//            for (int i = 0; i < size; i++) {
//                puzzlesImageInfos[i].setExChangeRotation(list.get(i));
//            }
//        }
    }

    /**
     * 初始洞口的滤镜数据
     *
     * @param context
     * @param rotationImgs
     * @param templateData 获取模板数据自带的滤镜特效Index
     */
    private void setRotationFilter(Context context, RotationImg[] rotationImgs, TemplateData templateData) {
        if (templateData == null || rotationImgs == null || templateData.getFilterIndex() == -1) {
            return;
        }
//        int filterIndex = templateData.getFilterIndex();
//        List<TepFilterInfo> tepFilterInfos = FilterHelper.getPicTepFilterInfo(context);
//        if (filterIndex >= 0 && filterIndex < tepFilterInfos.size()) {
//            TepFilterInfo tepFilterInfo = tepFilterInfos.get(filterIndex);
//
//            //有滤镜或者不自动美化不使用模板滤镜
//            for (RotationImg rotationImg : rotationImgs) {
//                if (rotationImg.getTepFilterInfo() == null && templateData.isAutoBeautify()) {
//                    rotationImg.setTepFilterInfo(tepFilterInfo.clone());
//                }
//            }
//        }
    }

    public RotationImg[] getRotationImgs() {
        return rotationImgs;
    }

    // --- 文字
    public void changeTextSize(int textSize, Point[] points) {
//        if (puzzlesTextInfos != null) {
//            for (PuzzlesTextInfo textInfo : puzzlesTextInfos) {
//                if (ShapeUtils.pointEqual(textInfo.getDrawPoint(), points)) {
//                    textInfo.setFontSize(textSize);
//                    return;
//                }
//            }
//        }
    }

    public void changeTextAutoStr(String autoStr, Point[] points) {
//        if (puzzlesTextInfos != null) {
//            for (PuzzlesTextInfo textInfo : puzzlesTextInfos) {
//                if (ShapeUtils.pointEqual(textInfo.getDrawPoint(), points)) {
//                    textInfo.setAutoStr(autoStr);
//                    return;
//                }
//            }
//        }
    }

    public void changeTextFont(Context context, String font, Point[] points) {
//        if (puzzlesTextInfos != null) {
//            for (PuzzlesTextInfo textInfo : puzzlesTextInfos) {
//                if (ShapeUtils.pointEqual(textInfo.getDrawPoint(), points)) {
//                    textInfo.setFont(context, font);
//                    return;
//                }
//            }
//        }
    }

    public void changeTextColor(int color, Point[] points) {
//        if (puzzlesTextInfos != null) {
//            for (PuzzlesTextInfo textInfo : puzzlesTextInfos) {
//                if (ShapeUtils.pointEqual(textInfo.getDrawPoint(), points)) {
//                    textInfo.changeFontColor(color);
//                    return;
//                }
//            }
//        }
    }


    // --- get
    public int getPuzzleMode() {
        return mPuzzleMode;
    }

    public Rect getOutPutRect() {
        return outPutRect;
    }

    public Rect getRect() {
        return rect;
    }

    public TemplateData getTemplateData() {
        return templateData;
    }


//    public PuzzlesLayoutInfo getPuzzlesLayoutInfo() {
//        return puzzlesLayoutInfo;
//    }
//
//    public PuzzlesLayoutJointInfo getPuzzlesLayoutJointInfo() {
//        return puzzlesLayoutJointInfo;
//    }
//
//    public PuzzlesImageInfo[] getPuzzlesImageInfos() {
//        return puzzlesImageInfos;
//    }
//
//    public PuzzlesTextInfo[] getPuzzlesTextInfos() {
//        return puzzlesTextInfos;
//    }
//
//    public PuzzlesWaterMarkInfo getPuzzlesWaterMarkInfo() {
//        return puzzlesWaterMarkInfo;
//    }
//
//    public PuzzlesCardInfo getPuzzlesCardInfo() {
//        return puzzlesCardInfo;
//    }
//
//    public int getTemplateDuration() {
//        return mTemplateDuration;
//    }
//
//    public void setTemplateDuration(int templateDuration) {
//        mTemplateDuration = templateDuration;
//    }
//
//    public int getAllDuration() {
//        return mAllDuration;
//    }
//
//    public void setAllDuration(int allDuration) {
//        mAllDuration = allDuration;
//    }
//
//    public boolean isPlayTogether() {
//        return isPlayTogether;
//    }
//
//    public void setPlayTogether(boolean playTogether) {
//        isPlayTogether = playTogether;
//    }

    public void clear() {
        isSaveDraft = false;
//        if (puzzlesImageInfos != null && puzzlesImageInfos.length != 0) {
//            for (PuzzlesImageInfo puzzlesImageInfo : puzzlesImageInfos) {
//                puzzlesImageInfo.recycle();
//            }
//        }
//        if (puzzlesLayoutInfo != null) {
//            puzzlesLayoutInfo.recycle();
//        }
//        if (puzzlesLayoutJointInfo != null) {
//            puzzlesLayoutJointInfo.recycle();
//        }
//        if (puzzlesHeadInfo != null) {
//            puzzlesHeadInfo.recycle();
//        }
//        if (puzzlesMaskInfo != null) {
//            puzzlesMaskInfo.recycle();
//        }
        if (puzzlesFgInfo != null) {
            puzzlesFgInfo.recycle();
        }
//        if (puzzlesVarFgInfos != null && puzzlesVarFgInfos.length != 0) {
//            for (PuzzlesVarFgInfo puzzlesVarFgInfo : puzzlesVarFgInfos) {
//                puzzlesVarFgInfo.recycle();
//            }
//        }
//        if (puzzlesTextInfos != null && puzzlesTextInfos.length != 0) {
//            for (PuzzlesTextInfo puzzlesTextInfo : puzzlesTextInfos) {
//                puzzlesTextInfo.recycle();
//            }
//        }
//        if (puzzlesWaterMarkInfo != null) {
//            puzzlesWaterMarkInfo.recycle();
//        }
//        if (puzzlesQrCodeInfo != null) {
//            puzzlesQrCodeInfo.recycle();
//        }
//        if (puzzlesCardInfo != null) {
//            puzzlesCardInfo.recycle();
//        }
        isSave = false;
    }

}
