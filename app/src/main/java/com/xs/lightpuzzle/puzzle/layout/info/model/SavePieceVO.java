package com.xs.lightpuzzle.puzzle.layout.info.model;

import android.graphics.RectF;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局保存时需要用到的图片信息对象
 */

public class SavePieceVO {
    private RectF pieceRect;
    private float degree;
    private float transX;
    private float transY;
    private float scale;

    public SavePieceVO(RectF pieceRect, float degree, float transX, float transY, float scale) {
        this.pieceRect = pieceRect;
        this.degree = degree;
        this.transX = transX;
        this.transY = transY;
        this.scale = scale;
    }

    public RectF getPieceRect() {
        return pieceRect;
    }

    public void setPieceRect(RectF pieceRect) {
        this.pieceRect = pieceRect;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public float getTransX() {
        return transX;
    }

    public void setTransX(float transX) {
        this.transX = transX;
    }

    public float getTransY() {
        return transY;
    }

    public void setTransY(float transY) {
        this.transY = transY;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
