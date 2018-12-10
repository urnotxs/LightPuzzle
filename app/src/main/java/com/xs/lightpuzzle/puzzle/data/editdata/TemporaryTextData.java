package com.xs.lightpuzzle.puzzle.data.editdata;

import android.graphics.Point;

import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;

/**
 * Created by xs on 2018/4/28.
 * 数据与界面通信时会产生的数据
 */

public class TemporaryTextData {

    private TextData textData;

    private Point[] points;

    public TemporaryTextData(TextData textData, Point[] points) {
        this.textData = textData;
        this.points = points;
    }

    public TextData getTextData() {
        return textData;
    }

    public Point[] getPoints() {
        return points;
    }
}
