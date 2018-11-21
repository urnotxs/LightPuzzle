package com.xs.lightpuzzle.puzzle.param;

import java.io.Serializable;

/**
 * Created by xs on 2018/8/23.
 */

public class TimedPoint implements Serializable {

    public float x;
    public float y;
    public long timestamp;

    public TimedPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.timestamp = System.currentTimeMillis();
    }

    public TimedPoint(float x, float y , long time) {
        this.x = x;
        this.y = y;
        this.timestamp = time;
    }

    public float velocityFrom(TimedPoint start) {
        float velocity = (float) (distanceTo(start) / (this.timestamp - start.timestamp));
        if (velocity != velocity) return 1.2f;
        return velocity;
    }

    public float distanceTo(TimedPoint point) {
        return (float) Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2));
    }
}
