package com.xs.lightpuzzle.puzzle.view.texturecolor.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lsq on 2018/4/10.
 */

public class PuzzleTextureBean {

    /**
     * mTextureIconHover : choose_bg_none_hover.png
     * mTextureId : 20000
     * mTextureImage : none
     * mLock : none
     * mTextureIcon : choose_bg_none.png
     */
    @SerializedName("textureHgihlightIcon")
    private String mTextureIconHover;
    @SerializedName("textureId")
    private String mTextureId;
    @SerializedName("textureImageName")
    private String mTextureImage;
    @SerializedName("lock")
    private String mLock;
    @SerializedName("textureIcon")
    private String mTextureIcon;

    public String getTextureIconHover() {
        return mTextureIconHover;
    }

    public void setTextureIconHover(String textureIconHover) {
        this.mTextureIconHover = textureIconHover;
    }

    public String getTextureId() {
        return mTextureId;
    }

    public void setTextureId(String textureId) {
        this.mTextureId = textureId;
    }

    public String getTextureImage() {
        return mTextureImage;
    }

    public void setTextureImage(String textureImage) {
        this.mTextureImage = textureImage;
    }

    public String getLock() {
        return mLock;
    }

    public void setLock(String lock) {
        this.mLock = lock;
    }

    public String getTextureIcon() {
        return mTextureIcon;
    }

    public void setTextureIcon(String textureIcon) {
        this.mTextureIcon = textureIcon;
    }
}
