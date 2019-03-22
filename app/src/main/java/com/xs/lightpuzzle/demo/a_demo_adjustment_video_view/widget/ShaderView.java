package com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.view.View;

import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * Created by xs on 2018/9/5.
 */

public class ShaderView extends View{
    private final int LINE_SIZE = Utils.getRealPixel3(4);//描边线的尺寸

    private RectF mRect;
    private float mViewWidth;
    private float mViewHeight;
    private float mRatio;
    private RectF mTopRect;
    private RectF mClipRect;
    private RectF mBottomRect;

    public ShaderView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRatio != 0
                && mTopRect != null
                && mClipRect != null
                && mBottomRect != null){
            Paint paint = new Paint();
            canvas.save();
            canvas.setDrawFilter(
                    new PaintFlagsDrawFilter(0,
                            Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            paint.setAntiAlias(true);
            paint.setColor(0x33FFFFFF);
            canvas.drawRect(mTopRect , paint);
            canvas.drawRect(mBottomRect , paint);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(LINE_SIZE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(
                    new RectF(mClipRect.left + LINE_SIZE/2 ,
                            mClipRect.top + LINE_SIZE/2,
                            mClipRect.right - LINE_SIZE/2,
                            mClipRect.bottom - LINE_SIZE/2) , paint);
            canvas.restore();
        }

    }

    public void setParams(float width , float height , RectF clipRect){
        mRect = new RectF(0, 0, width, height);
        mViewWidth = mRect.width();
        mViewHeight = mRect.height();
        mClipRect = clipRect;

        float videoRatio = mViewWidth / mViewHeight;
        mRatio = clipRect.width() / clipRect.height();

        if (mRatio > videoRatio){
            // 齐宽
            mTopRect = new RectF(mClipRect.left, mRect.top, mClipRect.right, mClipRect.top);
            mBottomRect = new RectF(mClipRect.left, mClipRect.bottom, mClipRect.right, mRect.bottom);
        }else{
            // 齐高
            mTopRect = new RectF(mRect.left, mClipRect.top, mClipRect.left, mClipRect.bottom);
            mBottomRect = new RectF(mClipRect.right, mClipRect.top, mRect.right, mClipRect.bottom);
        }
    }

}
