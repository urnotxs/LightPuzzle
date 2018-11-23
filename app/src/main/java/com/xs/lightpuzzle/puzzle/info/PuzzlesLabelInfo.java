package com.xs.lightpuzzle.puzzle.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.util.ShareData;
import com.xs.lightpuzzle.puzzle.view.label.view.EditLabelView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xs on 2018/4/12.
 * 标签
 */

public class PuzzlesLabelInfo implements DrawView {
    public final static int LABEL_SIZE = 1334;
    private transient Context context;
    private Rect rect;
    // 保存时用到的rect
    private Rect outPutRect;
    private int mPuzzleMode;
    private transient boolean save;

    private int scrollY;
    private String picPath;
    private transient Paint mPaint;
    private Point mPoint;
    private String mText;
    private transient Bitmap mBitmap = null;
    private boolean isInvert = false;
    private EditLabelView.ICON_TYPE mIconType;
    private EditLabelView.LABEL_TYPE mLabelType;
    private boolean canDraw = true;

    private transient int mLastHeight;
    private int lastX = 0;
    private int lastY = 0;
    private int labelX = 0;
    private int labelY = 0;
    private boolean sTouch = false;
    private boolean sMove = false;
    private boolean sLongTouch = false;
    private transient Handler selHandler = new Handler(Looper.getMainLooper());
    private transient Runnable selTask = new Runnable() {

        @Override
        public void run() {
            if (sTouch && !sMove) {
                //TongJi.add_using_count("长按标签");
                sLongTouch = true;
                if (mPoint != null && mBitmap != null) {
                    Rect rect = new Rect(mPoint.x, mPoint.y,
                            mPoint.x + mBitmap.getWidth(),
                            mPoint.y + mBitmap.getHeight());
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                            .PUZZLES_LABEL_SHOW_BAR, 0, rect));
                }
            }
        }
    };

    public PuzzlesLabelInfo() {
    }

    public PuzzlesLabelInfo(Context context) {
        this.context = context;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public boolean isLongTouch() {
        return sLongTouch;
    }

    public void setPuzzleMode(int puzzleMode) {
        mPuzzleMode = puzzleMode;
    }

    public void setRect(Rect rect) {
        if (rect == null) {
            return;
        }
        if (this.rect == null) {
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
        }
        if (this.rect.height() != rect.height()) {
            Rect lastRect = this.rect;
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
            if (mPuzzleMode == PuzzleMode.MODE_LONG && mBitmap != null) {
                if (mPoint.y + mBitmap.getHeight() > rect.height()) {
                    mPoint.y = rect.height() - mBitmap.getHeight();
                }
                /*if (lastRect.height() <= rect.height()){
                    // 长图加页 + 更换更长的子模板
                    // 不改变标签位置
                    return;
                }else{
                    // 长图减页 + 更换更短的子模板
                    // 但是当前标签位置底部高于新矩形底部，则不改变标签位置
                    if(mPoint.y + mBitmap.getHeight() < this.rect.bottom){

                    }

                }*/
            } else {
                reload(context, rect, lastRect);
            }

        } else {
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
        }
        scrollY = 0;
    }

    @Override
    public void init() {
        // 初始画笔或者矩形
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
    }

    @Nullable
    private Bitmap getBitmap(Bitmap bitmap) {
        float simple = bitmap.getWidth() / (float) LABEL_SIZE;
        float scale = bitmap.getWidth() / (float) bitmap.getHeight();
        int width = 0;
        if (save) {
            width = Math.round(outPutRect.width() * simple);
        } else {
            width = Math.round(rect.width() * simple);
        }
        int height = Math.round(width / scale);
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    @Override
    public void initBitmap(Context context) {
        this.context = context;
        // decode图片
        // 确认位置
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        Bitmap bitmap = EditLabelView.DecodeLabel(context, getText(), getIconType(), getLabelType(), isInvert());
        mBitmap = getBitmap(bitmap);
        if (save) {
            float scale = (float) outPutRect.width() / (float) rect.width();
            mPoint.set((int) (mPoint.x * scale), (int) (mPoint.y * scale));

        } else {
            if (mPoint == null) {
                // 初始在屏幕中央
                mPoint = new Point(
                        (rect.width() - mBitmap.getWidth()) / 2,
                        rect.height() < ShareData.getScreenH()
                                ? (rect.height() - mBitmap.getHeight()) / 2
                                : (ShareData.getScreenH() - mBitmap.getHeight()) / 2 + scrollY);
            }
        }
    }

    /**
     * 长图含多个子模板时
     * 保存绘制
     */
    public void draw(Canvas canvas, int lastHeight) {
        mLastHeight = lastHeight;
        draw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canDraw) {
            canvas.save();
            canvas.setDrawFilter(
                    new PaintFlagsDrawFilter(0,
                            Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            if (mBitmap != null && !mBitmap.isRecycled()) {
                synchronized (mBitmap) {
                    canvas.translate(0, -mLastHeight);
                    canvas.drawBitmap(mBitmap, getPoint().x, getPoint().y, mPaint);
                }
            }
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null && checkLabelRect((int) event.getX(), (int) event.getY())) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    sTouch = true;
                    sLongTouch = false;
                    sMove = false;
                    lastX = (int) event.getX();
                    lastY = (int) event.getY();
                    labelX = mPoint.x;
                    labelY = mPoint.y;
                    selHandler.removeCallbacksAndMessages(null);
                    selHandler.postDelayed(selTask, 500);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    //nothing..
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (sTouch) {
                        if (Math.abs(event.getX() - lastX) > 10) {
                            sLongTouch = false;
                            sMove = true;
                            EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                                    .PUZZLES_LABEL_SHOW_BAR, event.getAction(), null));
                        }
                        mPoint.set(labelX, labelY);
                        mPoint.set((int) (mPoint.x + event.getX() - lastX), (int) (mPoint.y + event.getY() - lastY));
                        //moveTo((int) (event.getX() - lastX), (int) (event.getY() - lastY));
                        checkDrawRect(); //不能超过绘制区域
                    }

                    EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_LABEL, event.getAction()));
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    //nothing..
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    checkTouch((int) event.getX(), (int) event.getY());// 如果是click就翻转
                    sTouch = false;
                    sMove = false;
                    if (sTouch == false) {
                        return true;
                    }
                    break;
            }
            if (sTouch) {
                return true;
            } else {
                return false;
            }
        }

        sLongTouch = false;
        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                .PUZZLES_LABEL_SHOW_BAR, -1, null));

        return false;
    }

    private boolean checkLabelRect(int x, int y) {
        if (mBitmap == null) {
            return false;
        }
        if (mPoint == null) {
            return false;
        }
        Rect rect = new Rect(mPoint.x, mPoint.y,
                mPoint.x + mBitmap.getWidth(),
                mPoint.y + mBitmap.getHeight());
        return rect.contains(x, y);
    }

    private void checkDrawRect() {
        if (mPoint.x < rect.left) {
            mPoint.x = rect.left;
        }
        if (mPoint.x + mBitmap.getWidth() >= rect.right) {
            mPoint.x = rect.right - mBitmap.getWidth();
        }
        if (mPoint.y < rect.top) {
            mPoint.y = rect.top;
        }
        if (mPoint.y + mBitmap.getHeight() >= rect.bottom) {
            mPoint.y = rect.bottom - mBitmap.getHeight();
        }
    }

    private void checkTouch(int x, int y) {
        if (sTouch && !sMove && !sLongTouch) {
            //TongJi.add_using_count("左右变换标签");
            Matrix matrix = new Matrix();
            matrix.reset();
            matrix.postScale(-1, 1);
            setInvert(!isInvert());
            if (mBitmap != null && !mBitmap.isRecycled() && checkLabelRect(x, y)) {
                mBitmap.recycle();
                Bitmap bitmap = EditLabelView.DecodeLabel(context, getText(), getIconType(), getLabelType(), isInvert());
                mBitmap = getBitmap(bitmap);
                //重置坐标
                Rect rect = new Rect(mPoint.x, mPoint.y, mPoint.x + mBitmap.getWidth(), mPoint.y + mBitmap.getHeight());
                mPoint.x = mPoint.x + ((x - rect.left) - (rect.right - x));
                checkDrawRect();
                EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                        .PUZZLES_LABEL_SHOW_BAR, MotionEvent.ACTION_MOVE, null));
                EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_LABEL, 0));
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void recycle() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        mBitmap = null;
    }

    private void reload(Context context, Rect rect, Rect lastRect) {
        assert lastRect != null;
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        Bitmap bitmap = EditLabelView.DecodeLabel(context, getText(),
                getIconType(), getLabelType(), isInvert());
        mBitmap = getBitmap(bitmap);

        float x = mPoint.x / (float) lastRect.width() * rect.width();
        float y = mPoint.y / (float) lastRect.height() * rect.height();
        mPoint.x = (int) x;
        mPoint.y = (int) y;

        if (mPoint.y + mBitmap.getHeight() > rect.height()) {
            mPoint.y = rect.height() - mBitmap.getHeight();
        }
        if (mPoint.x + mBitmap.getWidth() > rect.width()) {
            mPoint.x = rect.width() - mBitmap.getWidth();
        }
    }

    public Rect getRect() {
        return rect;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public boolean isSave() {
        return save;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    public Rect getOutPutRect() {
        return outPutRect;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    public Point getPoint() {
        return mPoint;
    }

    public void setPoint(Point point) {
        this.mPoint = point;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public boolean isInvert() {
        return isInvert;
    }

    public void setInvert(boolean invert) {
        isInvert = invert;
    }

    public EditLabelView.ICON_TYPE getIconType() {
        return mIconType;
    }

    public void setIconType(EditLabelView.ICON_TYPE iconType) {
        this.mIconType = iconType;
    }

    public EditLabelView.LABEL_TYPE getLabelType() {
        return mLabelType;
    }

    public void setLabelType(EditLabelView.LABEL_TYPE labelType) {
        this.mLabelType = labelType;
    }

}
