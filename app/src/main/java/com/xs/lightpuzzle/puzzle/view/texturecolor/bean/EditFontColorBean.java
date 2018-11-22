package com.xs.lightpuzzle.puzzle.view.texturecolor.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lsq on 2018/4/11.
 */

public class EditFontColorBean {

    /**
     * colorRGBString : 000000
     * colorHgihlightIcon : choose_text_color_hover_1.png
     * colorIcon : choose_text_color1.png
     * lock : none
     */
    @SerializedName("colorIcon")
    private String mColorIcon;
    @SerializedName("colorHgihlightIcon")
    private String mColorIconHover;
    @SerializedName("colorRGBString")
    private String mColorValue;
    @SerializedName("lock")
    private String mLock;

    public String getColorIcon() {
        return mColorIcon;
    }

    public void setColorIcon(String colorIcon) {
        mColorIcon = colorIcon;
    }

    public String getColorIconHover() {
        return mColorIconHover;
    }

    public void setColorIconHover(String colorIconHover) {
        mColorIconHover = colorIconHover;
    }

    public String getColorValue() {
        return mColorValue;
    }

    public void setColorValue(String colorValue) {
        mColorValue = colorValue;
    }

    public String getLock() {
        return mLock;
    }

    public void setLock(String lock) {
        mLock = lock;
    }
}
