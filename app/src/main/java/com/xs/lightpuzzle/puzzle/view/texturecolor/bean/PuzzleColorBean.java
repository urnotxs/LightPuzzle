package com.xs.lightpuzzle.puzzle.view.texturecolor.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lsq on 2018/4/10.
 */

public class PuzzleColorBean {

    @SerializedName("colorTextureBlendModeArray")
    private List<String> mBledModes;
    @SerializedName("colorRGBStringArray")
    private List<ColorInfosBean> mColorInfos;
    @SerializedName("colorFontColorArray")
    private List<String> mFontColors;

    public List<String> getBledModes() {
        return mBledModes;
    }

    public void setBledModes(List<String> bledModes) {
        this.mBledModes = bledModes;
    }

    public List<ColorInfosBean> getColorInfos() {
        return mColorInfos;
    }

    public void setColorInfos(List<ColorInfosBean> colorInfos) {
        this.mColorInfos = colorInfos;
    }

    public List<String> getFontColors() {
        return mFontColors;
    }

    public void setFontColors(List<String> fontColors) {
        this.mFontColors = fontColors;
    }

    public static class ColorInfosBean {
        /**
         * mColorIcon : choose_bg_color_image1.png
         * mColorIconHover : choose_bg_color_image1_hover.png
         * mColorValue : ffffff
         * mLock : none
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
            this.mColorIcon = colorIcon;
        }

        public String getColorIconHover() {
            return mColorIconHover;
        }

        public void setColorIconHover(String colorIconHover) {
            this.mColorIconHover = colorIconHover;
        }

        public String getColorValue() {
            return mColorValue;
        }

        public void setColorValue(String colorValue) {
            this.mColorValue = colorValue;
        }

        public String getLock() {
            return mLock;
        }

        public void setLock(String lock) {
            this.mLock = lock;
        }
    }
}
