package com.xs.lightpuzzle.puzzle.data.lowdata;

import android.graphics.PointF;
import android.text.TextUtils;

import com.xs.lightpuzzle.puzzle.util.PuzzleTextUtils;
import com.xs.lightpuzzle.puzzle.util.Time2StringUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.io.Serializable;

/**
 * Created by xs on 2017/12/28.
 */

public class TextData implements Serializable, Cloneable {

    /**
     * "Font": "青鸟华光简报宋二.TTF",
     * "FontColor": "67988f",
     * "Frame": "0.421,0.5219,0.5095,0.5219,0.5095,0.603,0.421,0.603",
     * "Frame_int": "485,1069,587,1069,587,1235,485,1235",
     * "LayoutHeight": "2048",
     * "LayoutWidth": "1152",
     * "MaxFontSize": "80",
     * "MinFontSize": "60",
     * "alignment": "Left",
     * "autoStr": "最
     * 动
     * 听",
     * "linespace": "38",
     * "maxHeight": "80",
     * "textType": "cc2",
     * "topLineDistance": "3"
     */

    //字体
    private String font;
    //字体颜色
    private int fontColor;
    //坐标
    private PointF[] polygons;

    private int layoutHeight;

    private int layoutWidth;
    //当前字体大小
    private int fontSize;
    //最大字体大小
    private int maxFontSize;
    //最小字体大小
    private int minFontSize;
    //对齐样式
    private String alignment;
    //用户输入文字或者模板文字
    private String autoStr;

    private int maxCount;
    //视频字体动画的顺序
    private int AnimationId;
    //行间距 (设计给的)
    private int lineSpace;

    private int maxHeight;

    private String textType;
    //上间距
    private int topLineDistance;

    private boolean isDownload;

    private int leftLineDistance;

    private float defautlSize = -1;

    private boolean nickname;

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public PointF[] getPolygons() {
        return polygons;
    }

    public void setPolygons(PointF[] polygons) {
        this.polygons = polygons;
    }

    public int getLayoutHeight() {
        return layoutHeight;
    }

    public void setLayoutHeight(int layoutHeight) {
        this.layoutHeight = layoutHeight;
    }

    public int getLayoutWidth() {
        return layoutWidth;
    }

    public void setLayoutWidth(int layoutWidth) {
        this.layoutWidth = layoutWidth;
    }

    public int getMaxFontSize() {
        return maxFontSize;
    }

    public void setMaxFontSize(int maxFontSize) {
        this.maxFontSize = maxFontSize;
    }

    public int getMinFontSize() {
        return minFontSize;
    }

    public void setMinFontSize(int minFontSize) {
        this.minFontSize = minFontSize;
    }

    public String getAlignment() {
        return alignment.trim();
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getAutoStr() {
        return autoStr;
    }

    public void setAutoStr(String autoStr) {
        if (TextUtils.isEmpty(autoStr)) {
            autoStr = "";
        }
        autoStr = PuzzleTextUtils.autoStringFilter(autoStr);
        //判断是否是英文，为英文换行写新规则
        boolean isEn = Utils.checkEnglishChar(autoStr);
        this.autoStr = autoStr;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getAnimationId() {
        return AnimationId;
    }

    public void setAnimationId(int animationId) {
        AnimationId = animationId;
    }

    public int getLineSpace() {
        return lineSpace;
    }

    public void setLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        //处理时间
        String time = Time2StringUtils.getTime(textType);
        if (time != null) {
            autoStr = time;
        }
        this.textType = textType;
    }

    public int getTopLineDistance() {
        return topLineDistance;
    }

    public void setTopLineDistance(int topLineDistance) {
        this.topLineDistance = topLineDistance;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public int getLeftLineDistance() {
        return leftLineDistance;
    }

    public void setLeftLineDistance(int leftLineDistance) {
        this.leftLineDistance = leftLineDistance;
    }

    public float getDefautlSize() {
        return defautlSize;
    }

    public void setDefautlSize(float defautlSize) {
        this.defautlSize = defautlSize;
    }

    public boolean isNickname() {
        return nickname;
    }

    public void setNickname(boolean nickname) {
        this.nickname = nickname;
    }

    public void setTypeface(String fontId, String typefaceUri) {
        setTypefaceId(fontId);
        setTypefaceUri(typefaceUri);
    }

    private String mTypefaceId;
    private String mTypefaceUri;

    public String getTypefaceId() {
        return mTypefaceId;
    }

    public void setTypefaceId(String typefaceId) {
        mTypefaceId = typefaceId;
    }

    public void setTypefaceUri(String typefaceUri) {
        mTypefaceUri = typefaceUri;
    }

    public String getTypefaceUri() {
        return mTypefaceUri;
    }
}
