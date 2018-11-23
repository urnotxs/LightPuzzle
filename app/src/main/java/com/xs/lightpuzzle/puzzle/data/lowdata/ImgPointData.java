package com.xs.lightpuzzle.puzzle.data.lowdata;

import android.graphics.Point;
import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by xs on 2018/4/12.
 */

public class ImgPointData implements Serializable, Cloneable {

    //浮点型坐标位置, 小于1
    private PointF[] picPointF;
    //整形坐标位置
    private Point[] picPoint;

    public PointF[] getPicPointF() {
        return picPointF;
    }

    public void setPicPointF(PointF[] picPointF) {
        this.picPointF = picPointF;
    }

    public Point[] getPicPoint() {
        return picPoint;
    }

    public void setPicPoint(Point[] picPoint) {
        this.picPoint = picPoint;
    }
}
