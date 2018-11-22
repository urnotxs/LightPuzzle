package com.xs.lightpuzzle.puzzle.info.low;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.LightPuzzleConstant;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.info.DrawView;


/**
 * Created by Lin on 2018/4/12.
 */

public class PuzzlesMaskInfo implements DrawView {

    private String maskPic;

    private transient Rect rect;
    //保存时用到的rect
    private Rect outPutRect;

    private int bgColor = LightPuzzleConstant.INVALID_COLOR;

    private transient boolean save;

    private transient Bitmap maskBitmap, colorMaskBitmap;

    public void setMaskPic(String maskPic) {
        this.maskPic = maskPic;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
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
        if (context == null) {
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
        if (TextUtils.isEmpty(maskPic)) {
            return;
        }
        synchronized (this) {
            maskBitmap = JaneBitmapFactory.decode(context, maskPic, new ImageSize(finalRect.width(), finalRect.height()));
        }
        changeBgTexture(null, bgColor);
    }

    public void changeBgTexture(Bitmap colorTextureBitmap, int bgColor) {
        if (BitmapHelper.isInvalid(maskBitmap)) {
            return;
        }

        if (BitmapHelper.isInvalid(colorTextureBitmap)) {  //背景
            colorMaskBitmap = Bitmap.createBitmap(maskBitmap.getWidth(), maskBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            DrawFilter drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            Canvas canvas = new Canvas(colorMaskBitmap);
            canvas.setDrawFilter(drawFilter);
            if (bgColor != LightPuzzleConstant.INVALID_COLOR) {
                this.bgColor = bgColor;
                canvas.drawColor(bgColor);
            }
        }else if(colorTextureBitmap != null){
            colorMaskBitmap = colorTextureBitmap;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
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
        canvas.drawBitmap(colorMaskBitmap, finalRect.left, finalRect.top, null);
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
        maskBitmap = null;
    }
}
