package com.xs.lightpuzzle.puzzle.data.lowdata;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by Lin on 2018/4/12.
 */

public class HeadData implements Serializable, Cloneable{
    //
    private String headPic;

    private PointF[] headPoint;

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public PointF[] getHeadPoint() {
        return headPoint;
    }

    public void setHeadPoint(PointF[] headPoint) {
        this.headPoint = headPoint;
    }
}
