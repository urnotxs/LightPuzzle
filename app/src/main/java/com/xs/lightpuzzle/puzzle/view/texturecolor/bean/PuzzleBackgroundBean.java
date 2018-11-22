package com.xs.lightpuzzle.puzzle.view.texturecolor.bean;

/**
 * Created by lsq on 2018/4/11.
 */

public class PuzzleBackgroundBean {
    private String mTexture; // 纹理资源
    private String mBgColor; // 背景颜色
    private String mBlendModel; // 融合模式
    private String mFontColor; // 字体颜色
    private float mAlpha; // 透明度

    public PuzzleBackgroundBean(String texture, String bgColor,
                                String blendModel, String fontColor, float alpha) {
        mTexture = texture;
        mBgColor = bgColor;
        mBlendModel = blendModel;
        mFontColor = fontColor;
        mAlpha = alpha;
    }

    public String getTexture() {
        return mTexture;
    }

    public void setTexture(String texture) {
        mTexture = texture;
    }

    public String getBgColor() {
        return mBgColor;
    }

    public void setBgColor(String bgColor) {
        mBgColor = bgColor;
    }

    public String getBlendModel() {
        return mBlendModel;
    }

    public void setBlendModel(String blendModel) {
        mBlendModel = blendModel;
    }

    public String getFontColor() {
        return mFontColor;
    }

    public void setFontColor(String fontColor) {
        mFontColor = fontColor;
    }

    public float getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float alpha) {
        mAlpha = alpha;
    }

}
