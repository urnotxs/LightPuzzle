package com.xs.lightpuzzle.puzzle.data.image;

import android.graphics.Point;

/**
 * Created by xs on 2018/5/21.
 */

public class ImageShowBarData {

    private boolean video;

    private Point[] points;

    private boolean soundOpen;

    public boolean isSoundOpen() {
        return soundOpen;
    }

    public void setSoundOpen(boolean soundOpen) {
        this.soundOpen = soundOpen;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }
}
