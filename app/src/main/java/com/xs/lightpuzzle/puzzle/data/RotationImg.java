package com.xs.lightpuzzle.puzzle.data;

import java.io.Serializable;

/**
 * Created by Lin on 2018/4/12.
 */

public class RotationImg implements Serializable, Cloneable {

    protected String picPath;

    protected int rotation;

    protected String videoPath;

    protected boolean isChangedBeauty;

    protected int skinSmoothAlpha ;// 美颜美肤默认值为35

    protected int skinColorAlpha ;// 美颜肤色默认值为35

//    protected TepFilterInfo tepFilterInfo;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isChangedBeauty() {
        return isChangedBeauty;
    }

    public void setChangedBeauty(boolean changedBeauty) {
        isChangedBeauty = changedBeauty;
    }

    public int getSkinSmoothAlpha() {
        return skinSmoothAlpha;
    }

    public void setSkinSmoothAlpha(int skinSmoothAlpha) {
        this.skinSmoothAlpha = skinSmoothAlpha;
    }

    public int getSkinColorAlpha() {
        return skinColorAlpha;
    }

    public void setSkinColorAlpha(int skinColorAlpha) {
        this.skinColorAlpha = skinColorAlpha;
    }

//    public TepFilterInfo getTepFilterInfo() {
//        return tepFilterInfo;
//    }
//
//    public void setTepFilterInfo(TepFilterInfo tepFilterInfo) {
//        this.tepFilterInfo = tepFilterInfo;
//    }
}
