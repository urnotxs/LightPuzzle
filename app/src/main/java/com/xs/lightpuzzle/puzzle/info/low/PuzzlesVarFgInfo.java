package com.xs.lightpuzzle.puzzle.info.low;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.LightPuzzleConstant;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.DecodeImageOptions;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.effect.MakeMixAndEffect;
import com.xs.lightpuzzle.puzzle.info.DrawView;
import com.xs.lightpuzzle.puzzle.util.ShapeUtils;

/**
 * Created by xs on 2018/4/12.
 * 可变前景图层
 */

public class PuzzlesVarFgInfo implements DrawView {

    private transient Rect rect;
    //保存时用到的rect
    private Rect outPutRect;

    private String varFgPic;

    private PointF[] varFgPoint;

    private int color = LightPuzzleConstant.INVALID_COLOR;

    private transient Bitmap varFgBitmap;

    private transient Point[] drawPoint;

    private transient Rect drawRect;

    private transient boolean save;

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setVarFgPic(String varFgPic) {
        this.varFgPic = varFgPic;
    }

    public void setVarFgPoint(PointF[] varFgPoint) {
        this.varFgPoint = varFgPoint;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public void init() {
        if (varFgPoint == null) {
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

        drawPoint = ShapeUtils.makePts(varFgPoint, finalRect);
        changeIllegalHeight();
        drawRect = ShapeUtils.makeRect(drawPoint);

        // 修正drawRect
        reviseDrawRect();
    }

    /**
     * drawRect 修正，防止素材给的数据有误
     */
    private void reviseDrawRect() {
        if (drawRect == null) {
            return;
        }

        if (drawRect.width() <= 0) {
            drawRect.right = drawRect.left + 1;
        }

        if (drawRect.height() <= 0) {
            drawRect.bottom = drawRect.top + 1;
        }
    }

    private void changeIllegalHeight() {
        // 适配个别分辨率低的机型，可能出现计算出来的高度等于零
        int height = drawPoint[3].y - drawPoint[0].y;
        if (height == 0) {
            drawPoint[2].y = drawPoint[3].y + 1;
            drawPoint[3].y = drawPoint[3].y + 1;
        }
    }

    @Override
    public void initBitmap(Context context) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(varFgPic)) {
            return;
        }
        if (drawPoint == null || drawRect == null) {
            return;
        }
        synchronized (this) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            DecodeImageOptions decodeImageOptions = new DecodeImageOptions.Builder()
                    .setDecodeingOptions(options)
                    .build();
            varFgBitmap = JaneBitmapFactory.decode(context, varFgPic,
                    new ImageSize(drawRect.width(), drawRect.height()), decodeImageOptions);

        }
        changeFgColor(0xC8BFE7);
    }

    public void changeFgColor(int color) {
        if (color == LightPuzzleConstant.INVALID_COLOR) {
            return;
        }
        this.color = color;
        if (BitmapHelper.isInvalid(varFgBitmap)) {
            return;
        }
        synchronized (this) {
            varFgBitmap = MakeMixAndEffect.createARGBImage(varFgBitmap, color);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (BitmapHelper.isInvalid(varFgBitmap)) {
            return;
        }
        if (drawRect == null) {
            return;
        }
        canvas.drawBitmap(varFgBitmap, null, drawRect, null);
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
        varFgBitmap = null;
    }

}
