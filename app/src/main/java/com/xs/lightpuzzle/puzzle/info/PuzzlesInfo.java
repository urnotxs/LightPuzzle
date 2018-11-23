package com.xs.lightpuzzle.puzzle.info;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.xs.lightpuzzle.puzzle.data.RotationImg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/19.
 */

public class PuzzlesInfo implements DrawView {

    private Rect puzzlesRect;
    //保存时用到的rect
    private Rect outPutRect;

    private int puzzleMode;

    private PuzzlesBgTextureInfo bgTextureInfo;

    private List<TemplateInfo> templateInfos;

    private PuzzlesSignInfo signInfo;

//    private List<PuzzlesAddTextInfo> puzzlesAddTextInfos;

//    private List<PuzzlesLabelInfo> labelInfos;

    //点击模板（移动图片,名片，二维码，水印等）信息，移动过程中放弃标签、文字等拦截事件
    private boolean isTouchTemplate = false;

    @Override
    public void init() {
        if (bgTextureInfo != null) {
            bgTextureInfo.init();
        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.init();
            }
        }
        if (signInfo != null) {
            signInfo.init();
        }
//
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.init();
//            }
//        }
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                labelInfo.init();
//            }
//        }
    }

    @Override
    public void initBitmap(Context context) {
        if (bgTextureInfo != null) {
            bgTextureInfo.initBitmap(context);
        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.initBitmap(context, bgTextureInfo);

//                if (templateInfo.getPuzzleMode() == PuzzleMode.MODE_LAYOUT_JOIN
//                        && templateInfo.getPuzzlesLayoutJointInfo() != null &&
//                        (templateInfo.getPuzzlesLayoutJointInfo().getOuterPaddingRatio() != 0
//                                || templateInfo.getPuzzlesLayoutJointInfo().getPiecePaddingRatio() != 0
//                                || templateInfo.getPuzzlesLayoutJointInfo().getPieceRadianRatio() != 0)) {
//                    // 编辑页返回至选图页再点下一步
//                    // 基础拼接记录边框参数，initBitmap之后才可以知道带边框参数的新矩形，才可以重置画布宽高
//                    changeLayoutJointView(templateInfo.getPuzzlesLayoutJointInfo().getPuzzleJointRect());
//
//                }
            }
        }

        if (signInfo != null) {
            signInfo.initBitmap(context);
        }
//
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.initBitmap(context);
//            }
//        }
//
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                labelInfo.initBitmap(context);
//            }
//        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (bgTextureInfo != null) {
            bgTextureInfo.draw(canvas);
        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.draw(canvas);
            }
        }
        if (signInfo != null) {
            signInfo.draw(canvas);
        }
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.draw(canvas);
//            }
//        }
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                labelInfo.draw(canvas);
//            }
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean flag = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isTouchTemplate = false;
        }

//        if (labelInfos != null && !isTouchTemplate) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                flag = labelInfo.onTouchEvent(event);
//                if (flag) {
//                    return true;
//                }
//            }
//        }
        if (signInfo != null && !isTouchTemplate) {
            flag = signInfo.onTouchEvent(event);
            if (flag) {
                return true;
            }
        }
//        if (puzzlesAddTextInfos != null && !isTouchTemplate) {
//            boolean isTouchAddText = false;
//
//            for (int i = 0; i < puzzlesAddTextInfos.size(); i++) {
//                if (!isTouchAddText && puzzlesAddTextInfos.get(i).onTouchEvent(event)) {
//                    isTouchAddText = true;
//                } else {
//                    puzzlesAddTextInfos.get(i).setShowFrame(false);
//                }
//            }
//            if (isTouchAddText)
//                return true;
//        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                flag = templateInfo.onTouchEvent(event);
                isTouchTemplate = flag;
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean flag = false;
        if (signInfo != null) {
            flag = signInfo.dispatchTouchEvent(event);
            if (flag) {
                return true;
            }
        }
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                flag = addTextInfo.dispatchTouchEvent(event);
//                if (flag) {
//                    return true;
//                }
//            }
//        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                flag = templateInfo.dispatchTouchEvent(event);
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }



    public List<String> getAllTemplateId() {
        List<String> ids = null;
        if (templateInfos != null) {
            ids = new ArrayList<>();
            for (int i = 0; i < templateInfos.size(); i++) {
                TemplateInfo templateInfo = templateInfos.get(i);
                ids.add(templateInfo.getTemplateData().getJsonId());
            }
        }
        return ids;
    }


    // -------operation
    // ---template

    /**
     * 添加一个templateInfo至puzzleInfo
     * 重算预览和保存的画布宽高
     * @param templateInfo
     */
    public void addTemplateInfo(TemplateInfo templateInfo) {
        if (templateInfo == null) {
            return;
        }
        if (templateInfos == null) {
            templateInfos = new ArrayList<>();
        }
        templateInfos.add(templateInfo);
        upDataPuzzlesRect();
    }

    /**
     * 长图子模板删除
     */
    public void removeTemplateInfo() {
        if (templateInfos == null || templateInfos.size() < 2) {
            return;
        }
        TemplateInfo templateInfo = templateInfos.get(templateInfos.size() - 1);
        templateInfos.remove(templateInfo);
        if (templateInfo != null) {
            templateInfo.clear();
        }
        upDataPuzzlesRect();
    }

    /**
     * 长图子模板上移，与上一个模板位置切换
     *
     * @param context       context
     * @param templateIndex 模板位置
     */
    public void replaceUpTemplate(Context context, int templateIndex) {
        if (templateInfos == null || templateInfos.size() < templateIndex || templateIndex - 1 <= 0) {
            return;
        }

        TemplateInfo aboveInfo = templateInfos.get(templateIndex - 1);

        int aboveH = aboveInfo.getRect().height();
        int belowH = templateInfos.get(templateIndex).getRect().height();

        templateInfos.remove(aboveInfo);
        templateInfos.add(templateIndex, aboveInfo);
        upDataPuzzlesRect();
        for (int i = templateIndex - 1; i < templateInfos.size() && i < templateIndex + 1; i++) {//+1只处理上下移的两个
            TemplateInfo templateInfo = templateInfos.get(i);
            if (templateInfo != null) {

                if (i == templateIndex) {
                    templateInfo.resetRectAndScrollYOffset(belowH);
                } else if (i == templateIndex - 1) {
                    templateInfo.resetRectAndScrollYOffset(-aboveH);
                }
            }
        }
    }

    /**
     * 长图子模板下移，与下一个模板位置切换
     *
     * @param context       context
     * @param templateIndex 模板位置
     */
    public void replaceDownTemplate(Context context, int templateIndex) {
        if (templateInfos == null || templateInfos.size() < templateIndex || templateIndex + 1 >= templateInfos.size()) {
            return;
        }

        TemplateInfo aboveInfo = templateInfos.get(templateIndex);

        int aboveH = aboveInfo.getRect().height();
        int belowH = templateInfos.get(templateIndex + 1).getRect().height();

        templateInfos.remove(aboveInfo);
        templateInfos.add(templateIndex + 1, aboveInfo);
        upDataPuzzlesRect();
        for (int i = templateIndex; i < templateInfos.size() && i < templateIndex + 2; i++) {//+2只处理上下移的两个
            TemplateInfo templateInfo = templateInfos.get(i);
            if (templateInfo != null) {

                if (i == templateIndex) {
                    templateInfo.resetRectAndScrollYOffset(-aboveH);
                } else if (i == templateIndex + 1) {
                    templateInfo.resetRectAndScrollYOffset(belowH);
                }
            }
        }
    }

    /**
     * 长图替换模板，需要复原文字
     *
     * @param context       context
     * @param childTemplate 替换的新子模板
     * @param templateIndex 需要替换模板的位置下标
     */
    public void changeChildTemplate(Context context, TemplateInfo childTemplate, int templateIndex) {
        if (templateInfos == null || templateInfos.size() < templateIndex) {
            return;
        }
        TemplateInfo aboveInfo = templateInfos.get(templateIndex);


        if (aboveInfo == null || childTemplate == null) {
            return;
        }

        templateInfos.remove(aboveInfo);

        int aboveH = aboveInfo.getRect().height();
        int newH = childTemplate.getRect().height();

        int scrollH = newH - aboveH;

        templateInfos.add(templateIndex, childTemplate);
        upDataPuzzlesRect();
        for (int i = templateIndex; i < templateInfos.size(); i++) {
            TemplateInfo templateInfo = templateInfos.get(i);

            //文字恢复
            if (i == templateIndex) {
                templateInfo.clear();

                templateInfo.resetInit();
                templateInfo.initBitmap(context, bgTextureInfo);
                if (bgTextureInfo != null) {
                    templateInfo.changeBgTexture(bgTextureInfo.getWaterColor(), bgTextureInfo.getBgColor(), bgTextureInfo.getColorTextureBitmap());
                }

                int waterColor = bgTextureInfo == null ? -1 : bgTextureInfo.getWaterColor();
//                PuzzlesHelper.setBackText(context, aboveInfo, childTemplate, waterColor,false);
                aboveInfo.clear();
            } else {
                templateInfo.resetRectAndScrollYOffset(scrollH);
            }
        }

    }

    /**
     * 每当添加一个模板都需要重置rect
     */
    private void upDataPuzzlesRect() {
        int width = 0;
        int height = 0;
        int outPutWidth = 0;
        int outPutHeight = 0;
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                // 计算画布宽高
                Rect childRect = templateInfo.getRect();
                int top = height;
                if (childRect != null) {
                    width = childRect.width();
                    height += childRect.height();
                    childRect.set(0, top, width, height);
                }

                // 设置输出宽高
                Rect childOutRect = templateInfo.getOutPutRect();
                if (childOutRect != null) {
                    outPutWidth = childOutRect.width();
                    outPutHeight += childOutRect.height();
                }
            }

            if (puzzlesRect == null) {
                puzzlesRect = new Rect();
            }
            puzzlesRect.set(0, 0, width, height);

            if (outPutRect == null) {
                outPutRect = new Rect();
            }
            outPutRect.set(0, 0, outPutWidth, outPutHeight);
        }

        if (bgTextureInfo != null) {
            bgTextureInfo.setRect(puzzlesRect);
            bgTextureInfo.setOutPutRect(outPutRect);
        }
        if (signInfo != null) {
            signInfo.setRect(puzzlesRect);
            signInfo.setOutPutRect(outPutRect);
        }
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.setRect(puzzlesRect);
//                addTextInfo.setOutPutRect(outPutRect);
//            }
//        }
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                labelInfo.setRect(puzzlesRect);
//                labelInfo.setOutPutRect(outPutRect);
//            }
//        }
    }

    public void setFirstInit() {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        templateInfos.get(0).setFirstImageShowBar();
    }

    /**
     * 布局上移时，隐藏标签，签名，文字
     */
    public void setAddInfoVisible(boolean visible) {
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.setCanDraw(visible);
//            }
//        }
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                labelInfo.setCanDraw(visible);
//            }
//        }
        if (signInfo != null) {
            signInfo.setCanDraw(visible);
        }
    }

    // ---image
    public void resetId() {
//        PuzzlesInfoHelper.getInstance().resetCount();
//        if (templateInfos != null) {
//            for (TemplateInfo templateInfo : templateInfos) {
//                templateInfo.resetId();
//            }
//        }
    }

    public void reSavePoints() {
//        PuzzlesInfoHelper.getInstance().clearPoints();
//        if (templateInfos != null) {
//            for (TemplateInfo templateInfo : templateInfos) {
//                templateInfo.reSavePoints();
//            }
//        }
    }

    public RotationImg getRotationImg(Point[] points) {
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                return templateInfo.getRotationImg(points);
            }
        }
        return null;
    }

    public void showImageFram(Point[] imgPoint) {
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.showImageFram(imgPoint);
            }
        }
    }

    public void exchangeImgs(Context context, Point[] sourcePoint, Point[] changePoint) {
//        if (templateInfos != null) {
//            PuzzlesImageInfo sourceImgInfo = null;
//            PuzzlesImageInfo changeImgInfo = null;
//            TemplateInfo sourceTemplateInfo = null;
//            TemplateInfo changeTemplateInfo = null;
//            for (TemplateInfo templateInfo : templateInfos) {
//                if (templateInfo.getSourceImageInfo(sourcePoint) != null) {
//                    sourceImgInfo = templateInfo.getSourceImageInfo(sourcePoint);
//                    sourceImgInfo.setShowFrame(false);
//                    sourceTemplateInfo = templateInfo;
//                }
//                if (templateInfo.getChangeImageInfo(changePoint) != null) {
//                    changeImgInfo = templateInfo.getChangeImageInfo(changePoint);
//                    changeImgInfo.setShowFrame(false);
//                    changeTemplateInfo = templateInfo;
//                }
//            }
//            if (sourceImgInfo != null && changeImgInfo != null && sourceImgInfo != changeImgInfo) {
//                RotationImg sourceImg = sourceImgInfo.getRotationImg();
//                RotationImg changeImg = changeImgInfo.getRotationImg();
//                if (sourceTemplateInfo == changeTemplateInfo) {
//                    RotationImg[] imgs = sourceTemplateInfo.getRotationImgs();
//                    int sourceIndex = -1;
//                    int changeIndex = -1;
//                    for (int i = 0; i < imgs.length; i++) {
//                        if (imgs[i] == sourceImg) {
//                            sourceIndex = i;
//                        }
//                        if (imgs[i] == changeImg) {
//                            changeIndex = i;
//                        }
//                    }
//                    RotationImg tempImg = null;
//                    if (sourceIndex != -1 && changeIndex != -1) {
//                        tempImg = imgs[sourceIndex];
//                        imgs[sourceIndex] = imgs[changeIndex];
//                        imgs[changeIndex] = tempImg;
//                    }
//                } else {
//                    RotationImg[] sourceImgs = sourceTemplateInfo.getRotationImgs();
//                    for (int i = 0; i < sourceImgs.length; i++) {
//                        if (sourceImgs[i] == sourceImg) {
//                            sourceImgs[i] = changeImg;
//                        }
//                    }
//                    RotationImg[] changeImgs = changeTemplateInfo.getRotationImgs();
//                    for (int i = 0; i < changeImgs.length; i++) {
//                        if (changeImgs[i] == changeImg) {
//                            changeImgs[i] = sourceImg;
//                        }
//                    }
//                }
//
//                // 视频模式要特殊处理部分信息
//                if (puzzleMode == PuzzleMode.MODE_VIDEO) {
//                    UserVideoInfo srcUserVideoInfo = (UserVideoInfo) sourceImg;
//                    UserVideoInfo chgUserVideoInfo = (UserVideoInfo) changeImg;
//                    // 清除一下调整页的参数和重设第一张图
//                    if (srcUserVideoInfo.isPicMixVideo()) {
//                        srcUserVideoInfo.resetPic2VideoParams();
//                    }
//
//                    if (chgUserVideoInfo.isPicMixVideo()) {
//                        chgUserVideoInfo.resetPic2VideoParams();
//                    }
//                }
//
//                sourceImgInfo.setRotationImg(changeImg);
//                changeImgInfo.setRotationImg(sourceImg);
//
//                //交换他们的选转角度
//                int sourRotation = sourceImgInfo.getRotation();
//                sourceImgInfo.setExChangeRotation(changeImgInfo.getRotation());
//                changeImgInfo.setExChangeRotation(sourRotation);
//
//                sourceImgInfo.initBitmap(context);
//                changeImgInfo.initBitmap(context);
//            }
//        }
    }

    public void setPuzzlesImagePic(Context context, RotationImg rotationImg, Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.setPuzzlesImagePic(context, rotationImg, points);
        }
    }

    public void zoomInImg(Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.zoomInImg(points);
        }
    }

    public void zoomOutImg(Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.zoomOutImg(points);
        }
    }

    public void setImgRotation(Context context, Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.setImgRotation(context, points);
        }
    }

    public void setImgSelectForLong(Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.setImgSelectForLong(points);
        }
    }

//    public void setPicFilter(Context context, TepFilterInfo tepFilterInfo, int id) {
//        if (tepFilterInfo == null || id == -1) {
//            return;
//        }
//        if (templateInfos == null || templateInfos.size() == 0) {
//            return;
//        }
//        for (TemplateInfo templateInfo : templateInfos) {
//            templateInfo.setPicFilter(context, tepFilterInfo, id);
//        }
//    }

    public void setPicFilterAlpha(float alpha, int id) {
        if (id == -1) {
            return;
        }
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.setPicFilterAlpha(alpha, id);
        }
    }

    public void onLayoutClearSelected() {
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.onLayoutClearSelected();
            }
        }
    }

    public void changeLayoutJointView(Rect[] rect) {
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.changeLayoutJointView(rect);
                upDataPuzzlesRect();
            }
        }
    }

    public void changeLayoutView(Context context, boolean save) {
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.changeLayoutView(context, save);
                if (save) {
                    upDataPuzzlesRect();
                }
            }
        }
    }

//    public void onLayoutChanged(Context context, boolean changedCanvas, float ratio, int layout, LayoutData layoutData) {
//        if (templateInfos != null) {
//            for (TemplateInfo templateInfo : templateInfos) {
//
//                templateInfo.onLayoutChanged(context, changedCanvas, ratio, layout, layoutData);
//                if (changedCanvas) {
//                    upDataPuzzlesRect();
//                }
//            }
//        }
//
//    }

//    public void onLayoutPaddingChanged(Context context, BottomEditLineFrameView.CHANGEDMode mode, int value) {
//        if (templateInfos != null) {
//            for (TemplateInfo templateInfo : templateInfos) {
//
//                templateInfo.onLayoutPaddingChanged(context, mode, value);
//
//            }
//        }
//    }

    // ---text
    public void changeTextSize(int textSize, Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.changeTextSize(textSize, points);
        }
    }

    public void changeTextAutoStr(String autoStr, Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.changeTextAutoStr(autoStr, points);
        }
    }

    public void changeTextFont(Context context, String font, Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.changeTextFont(context, font, points);
        }
    }

    public void changeTextColor(int color, Point[] points) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.changeTextColor(color, points);
        }
    }

    public void changeAddTextAutoStr(String autoStr, Point[] points) {
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                if (ShapeUtils.pointEqual(addTextInfo.getDrawPoint(), points)) {
//                    addTextInfo.setAutoStr(autoStr);
//                    return;
//                }
//            }
//        }
    }

    public void changeAddTextFont(Context context, String font, Point[] points) {
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                if (addTextInfo.getDrawPoint() == points) {
//                    addTextInfo.setFont(context, font);
//                    return;
//                }
//            }
//        }
    }

    public void changeAddTextSize(int textSize, Point[] points) {
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                if (addTextInfo.getDrawPoint() == points) {
//                    addTextInfo.setFontSize(textSize);
//                    return;
//                }
//            }
//        }
    }

    public void changeAddTextColor(int color, Point[] points) {
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                if (addTextInfo.getDrawPoint() == points) {
//                    addTextInfo.setFontColor(color);
//                    return;
//                }
//            }
//        }
    }
//
//    public void addPuzzleAddTextInfos(PuzzlesAddTextInfo puzzlesAddTextInfo) {
//        if (puzzlesAddTextInfo == null) {
//            return;
//        }
//        if (puzzlesAddTextInfos == null) {
//            puzzlesAddTextInfos = new ArrayList<>();
//        }
//        puzzlesAddTextInfos.add(puzzlesAddTextInfo);
//    }

    public void deleteAddTextItem(Point[] points) {
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                if (addTextInfo.getDrawPoint() == points) {
//                    puzzlesAddTextInfos.remove(addTextInfo);
//                    return;
//                }
//            }
//        }
    }

    // ---head
    public void setHeadPic(Context context, String picPath) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        TemplateInfo templateInfo = templateInfos.get(0);
        if (templateInfo != null) {
            templateInfo.setHeadPic(context, picPath);
        }
    }

    //---background
    public void addPuzzlesBgTextureInfo(PuzzlesBgTextureInfo bgTextureInfo) {
        if (bgTextureInfo == null) {
            return;
        }
        this.bgTextureInfo = bgTextureInfo;
    }

    public void changeBgTexture(int fontColor, int bgColor) {
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.changeBgTexture(fontColor, bgColor, bgTextureInfo.getColorTextureBitmap());
            }
        }
    }

    // ---label
//    public void addPuzzleLabelInfos(PuzzlesLabelInfo puzzlesLabelInfo) {
//        if (puzzlesLabelInfo == null) {
//            return;
//        }
//        if (labelInfos == null) {
//            labelInfos = new ArrayList<>();
//        }
//        labelInfos.add(puzzlesLabelInfo);
//
//    }

    public void deleteLabelItem() {
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                if (labelInfo.isLongTouch()) {
//                    // 删除picpath
//                    //FileUtils.deleteFile(labelInfo.getPicPath() , false);
//                    labelInfos.remove(labelInfo);
//                    return;
//                }
//            }
//        }
    }

    // ---sign
    public void addPuzzlesSignInfo(PuzzlesSignInfo puzzlesSignInfo) {
        if (puzzlesSignInfo == null) {
            return;
        }
        this.signInfo = puzzlesSignInfo;
    }

    public void resetSignShowFrame() {
        if (signInfo == null) {
            return;
        }
        signInfo.setShowFrame(false);
    }

    // ---card
    public void setCardKeys(Context context, List<String> keys) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.setCardKeys(context, keys);
        }
    }

    // ---QrCode
    public void setQrCode(Context context, String qrPic) {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.setQrCode(context, qrPic);
        }
    }

    // --- WaterMark
    public void cleanUpWaterMark() {
        if (templateInfos == null || templateInfos.size() == 0) {
            return;
        }
        for (TemplateInfo templateInfo : templateInfos) {
            templateInfo.cleanUpWaterMark();
        }
    }

    // ---save
    public void reInit() {
        if (bgTextureInfo != null) {
            bgTextureInfo.init();
        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.reInit();
            }
        }
        if (signInfo != null) {
            signInfo.init();
        }
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.init();
//            }
//        }
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                labelInfo.init();
//            }
//        }
    }

    public void setValues() {
        // 子龙用来存保存时的matrix的
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.setValues();
            }
        }
        if (signInfo != null) {
            signInfo.matrixToValues();
        }
    }

    public void setSave() {
        if (bgTextureInfo != null) {
            bgTextureInfo.setSave(true);
        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.setSave();
            }
        }
        if (signInfo != null) {
            signInfo.setSave(true);
        }
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.setSave(true);
//            }
//        }
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                labelInfo.setSave(true);
//            }
//        }
    }

    public void setDraftSave() {
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.setDraftSave();
            }
        }
        if (signInfo != null) {
            signInfo.setShowFrame(false);
        }
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.setShowFrame(false);
//            }
//        }
    }

    // ---set 、 get
    public void setPuzzleMode(int puzzleMode) {
        this.puzzleMode = puzzleMode;
    }

    public int getPuzzleMode() {
        return puzzleMode;
    }

    public Rect getPuzzlesRect() {
        return puzzlesRect;
    }

    public Rect getOutPutRect() {
        return outPutRect;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    public List<TemplateInfo> getTemplateInfos() {
        return templateInfos;
    }

    public PuzzlesBgTextureInfo getBgTextureInfo() {
        return bgTextureInfo;
    }

//    public List<PuzzlesAddTextInfo> getPuzzlesAddTextInfos() {
//        return puzzlesAddTextInfos;
//    }
//
//    public List<PuzzlesLabelInfo> getPuzzlesLabelInfos() {
//        return labelInfos;
//    }
//
    public PuzzlesSignInfo getSignInfo() {
        return signInfo;
    }


    // --------recycle

    @Override
    public void recycle() {
        if (bgTextureInfo != null) {
            bgTextureInfo.recycle();
        }
        if (templateInfos != null) {
            for (TemplateInfo templateInfo : templateInfos) {
                templateInfo.clear();
            }
        }
        recycleSign();
        recycleAddTextInfos();
        recycleLabels();
    }

    public void recycleSign() {
        if (signInfo != null) {
            signInfo.recycle();
            signInfo = null;
        }
    }

    public void recycleAddTextInfos() {
//        if (puzzlesAddTextInfos != null) {
//            for (PuzzlesAddTextInfo addTextInfo : puzzlesAddTextInfos) {
//                addTextInfo.recycle();
//            }
//            puzzlesAddTextInfos = null;
//        }
    }

    public void recycleLabels() {
//        if (labelInfos != null) {
//            for (PuzzlesLabelInfo labelInfo : labelInfos) {
//                // 删除picpath
//                //FileUtils.deleteFile(labelInfo.getPicPath() , false);
//                labelInfo.recycle();
//            }
//            labelInfos = null;
//        }
    }
}
