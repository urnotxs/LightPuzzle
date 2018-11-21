package com.xs.lightpuzzle.puzzle.info;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Lin on 2018/4/12.
 */

public interface DrawView {

    void init();

    void initBitmap(Context context);

    void draw(Canvas canvas);

    boolean onTouchEvent(MotionEvent event);

    boolean dispatchTouchEvent(MotionEvent event);

    void recycle();

}
