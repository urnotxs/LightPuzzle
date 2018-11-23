package com.xs.lightpuzzle.puzzle.frame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.view.MotionEvent;
import android.view.View;

import com.xs.lightpuzzle.puzzle.PuzzlePresenter;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xs on 2018/11/20.
 */

public abstract class PuzzleDrawView extends View {

    private PuzzlePresenter mPuzzlePresenter;

    private boolean canDraw;

    public abstract int getScrollYOffset();

    public PuzzleDrawView(Context context) {
        super(context);
    }

    public void setPuzzlePresenter(PuzzlePresenter puzzlePresenter) {
        mPuzzlePresenter = puzzlePresenter;
    }

    public void invalidateView() {
        canDraw = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canDraw) {
            //开启抗锯齿
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                    | Paint.FILTER_BITMAP_FLAG));

            int saveId = 0;
            if (canvas.getHeight() > Utils.getScreenH()) {
                int scrollYOffset = getScrollYOffset();
                int left = 0;
                int top = scrollYOffset - PuzzlesUtils.getViewTop();
                int right = getWidth();
                int bottom = scrollYOffset + Utils.getScreenH() > getHeight() ? getHeight() : scrollYOffset + Utils.getScreenH();
                // 计算需要显示的画布大小，为了防止长图过长导致内存过大
                canvas.clipRect(left, top, right, bottom);
                saveId = canvas.saveLayer(left, top, right, bottom, null, Canvas.ALL_SAVE_FLAG);
            }
            canvas.drawColor(Color.WHITE);
            if (mPuzzlePresenter != null) {
                mPuzzlePresenter.draw(canvas);
            }
            if (canvas.getHeight() > Utils.getScreenH()) {
                canvas.restoreToCount(saveId);
            }
        }
    }
    private boolean isIntercept;
    public boolean isInterceptTouchEvent(){
        // 如果DrawView拦截了Touch事件，则不响应点击事件
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPuzzlePresenter == null) {
            return super.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_HIDE_OPERATION_BAR, event.getAction()));
        }
        isIntercept = false;
        if (mPuzzlePresenter.onTouchEvent(event)) {
            this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() != MotionEvent.ACTION_UP){
                isIntercept = true;
            }
            return true;
        } else {
            this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
            mPuzzlePresenter.reSetSelectForLong();
            EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_CLOSE_PIC_FILTER, event.getAction()));
        }
        invalidateView();
        return super.onTouchEvent(event);
    }

    public void recycle() {
        canDraw = false;
    }

}
