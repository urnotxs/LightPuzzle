package com.xs.lightpuzzle.puzzle.data.lowdata;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by Lin on 2018/4/12.
 */

public class WaterMarkData implements Serializable, Cloneable{

    private String waterPic;

    private PointF[] waterPoint;

    public String getWaterPic() {
        return waterPic;
    }

    public void setWaterPic(String waterPic) {
        this.waterPic = waterPic;
    }

    public PointF[] getWaterPoint() {
        return waterPoint;
    }

    public void setWaterPoint(PointF[] waterPoint) {
        this.waterPoint = waterPoint;
    }
}
