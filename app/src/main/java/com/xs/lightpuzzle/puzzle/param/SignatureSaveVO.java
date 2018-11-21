package com.xs.lightpuzzle.puzzle.param;

import android.graphics.RectF;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xs on 2018/8/23.
 */

public class SignatureSaveVO implements Serializable{

    private int color;
    private RectF rectF; // 存放签名画板中的签名矩形
    private float width;
    private float height;
    private ArrayList<ArrayList<TimedPoint>> timedPointsArray;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public ArrayList<ArrayList<TimedPoint>> getTimedPointsArray() {
        return timedPointsArray;
    }

    public void setTimedPointsArray(ArrayList<ArrayList<TimedPoint>> timedPointsArray) {
        this.timedPointsArray = timedPointsArray;
    }
}
