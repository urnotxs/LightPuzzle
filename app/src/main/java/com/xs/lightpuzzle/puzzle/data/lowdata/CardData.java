package com.xs.lightpuzzle.puzzle.data.lowdata;

import android.graphics.PointF;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lin on 2018/4/2.
 */

public class CardData implements Serializable, Cloneable {

    private PointF[] cardPoint;
    //logo color
    private int thumbImgColor;
    //loco type
    private String thumbImgType;
    //text color
    private int textColor = -1;
    //max key count
    private int maxOptionCount;
    //drafult key
    private List<String> autoOptionKey;

    public PointF[] getCardPoint() {
        return cardPoint;
    }

    public void setCardPoint(PointF[] cardPoint) {
        this.cardPoint = cardPoint;
    }

    public int getThumbImgColor() {
        return thumbImgColor;
    }

    public void setThumbImgColor(int thumbImgColor) {
        this.thumbImgColor = thumbImgColor;
    }

    public String getThumbImgType() {
        return thumbImgType;
    }

    public void setThumbImgType(String thumbImgType) {
        this.thumbImgType = thumbImgType;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getMaxOptionCount() {
        return maxOptionCount;
    }

    public void setMaxOptionCount(int maxOptionCount) {
        this.maxOptionCount = maxOptionCount;
    }

    public List<String> getAutoOptionKey() {
        return autoOptionKey;
    }

    public void setAutoOptionKey(List<String> autoOptionKey) {
        this.autoOptionKey = autoOptionKey;
    }
}
