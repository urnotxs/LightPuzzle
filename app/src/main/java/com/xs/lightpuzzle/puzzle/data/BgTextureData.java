package com.xs.lightpuzzle.puzzle.data;

import com.xs.lightpuzzle.LightPuzzleConstant;

import java.io.Serializable;

/**
 * Created by xs on 2018/4/12.
 */

public class BgTextureData implements Serializable, Cloneable {

    //background color
    private int bgColor = -1;
    //texture string
    private String texture;
    //texture alpha
    private float alpha;
    //background effect
    private String effect;

    private int waterColor = LightPuzzleConstant.INVALID_COLOR;

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getWaterColor() {
        return waterColor;
    }

    public void setWaterColor(int waterColor) {
        this.waterColor = waterColor;
    }
}
