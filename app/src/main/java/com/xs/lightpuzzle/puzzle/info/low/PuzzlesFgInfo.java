package com.xs.lightpuzzle.puzzle.info.low;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.puzzle.info.DrawView;
import com.xs.lightpuzzle.puzzle.util.GlideBitmapFactoryHelper;


/**
 * Created by Lin on 2018/4/12.
 * 前景图
 */

public class PuzzlesFgInfo implements DrawView {

    private transient Rect rect;
    //保存时用到的rect
    private Rect outPutRect;

    private String fgPic;

    private transient Bitmap fgBitmap;

    private transient boolean save;

    public void setFgPic(String fgPic) {
        this.fgPic = fgPic;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    @Override
    public void init() {

    }

    @Override
    public void initBitmap(Context context) {
        Rect finalRect = null;
        if (save) {
            if (outPutRect == null) {
                return;
            }
            finalRect = outPutRect;
        } else {
            if (rect == null) {
                return;
            }
            finalRect = rect;
        }
        if (finalRect == null) {
            return;
        }
        if (TextUtils.isEmpty(fgPic)) {
            return;
        }
        synchronized (this) {
            fgBitmap = GlideBitmapFactoryHelper
                    .decode(context, fgPic, finalRect.width(), finalRect.height(), false);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (fgBitmap == null || fgBitmap.isRecycled()) {
            return;
        }
        Rect finalRect = null;
        if (save) {
            if (outPutRect == null) {
                return;
            }
            finalRect = outPutRect;
        } else {
            if (rect == null) {
                return;
            }
            finalRect = rect;
        }
        if (finalRect == null) {
            return;
        }
        canvas.drawBitmap(fgBitmap, finalRect.left, finalRect.top, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void recycle() {
        fgBitmap = null;
    }

}
