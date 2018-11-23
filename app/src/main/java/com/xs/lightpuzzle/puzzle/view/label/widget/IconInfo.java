package com.xs.lightpuzzle.puzzle.view.label.widget;

import android.graphics.drawable.Drawable;

/**
 * Created by urnot_XS on 2018/4/11.
 */

public class IconInfo {

    private Drawable mIconDrawable;
    private String mIconText;
    private boolean mSelectedStatus;
    private boolean isShowText;

    public boolean isShowText() {
        return isShowText;
    }

    public void setShowText(boolean showText) {
        isShowText = showText;
    }

    public Drawable getIconDrawable() {
        return mIconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.mIconDrawable = iconDrawable;
    }

    public String getIconText() {
        return mIconText;
    }

    public void setIconText(String iconText) {
        this.mIconText = iconText;
    }


    public boolean getSelectedStatus() {
        return mSelectedStatus;
    }

    public void setSelectedStatus(boolean status) {
        this.mSelectedStatus = status;
    }
}