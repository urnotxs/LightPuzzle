package com.xs.lightpuzzle.puzzle.view.texturecolor.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xs on 2018/4/11.
 */

public class PuzzleBlendAlphaBean {

    /**
     * alphas : 0.1,0.1,0.05,0.18,0.13,0.14,0.1,0.08,0.1,0.14,0.18,0.18,0.15,0.12,0.08,0.14,0.07,0.05,0.09,0.10,0.10,0.08,0.16,0.09,0.20,0.15,0.10,0.10,0.10,0.10,0.10,0.08,0.10,0.09,0.07
     * textures : 20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,20011,20012,20013,20014,20015,20016,20017,20018,20019,20020,20021,20022,20023,20024,20025,20026,20027,20028,20029,20030,20031,20032,20033,20034,20035
     * color : ffffff
     */

    @SerializedName("alphas")
    private String mAlphas;
    @SerializedName("textures")
    private String mTextureIds;
    @SerializedName("color")
    private String mBgColor;

    public String getAlphas() {
        return mAlphas;
    }

    public void setAlphas(String alphas) {
        this.mAlphas = alphas;
    }

    public String getTextureIds() {
        return mTextureIds;
    }

    public void setTextureIds(String textureIds) {
        this.mTextureIds = textureIds;
    }

    public String getBgColor() {
        return mBgColor;
    }

    public void setBgColor(String bgColor) {
        this.mBgColor = bgColor;
    }
}
