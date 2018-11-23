package com.xs.lightpuzzle.puzzle.view.label.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 支持点击空白地方的监听
 * <p>
 * Created by urnot_XS on 2018/4/11.
 */
public class BlankRecyclerView extends RecyclerView {

    private GestureDetectorCompat gestureDetector;
    private BlankListener listener;

    public BlankRecyclerView(Context context) {
        super(context);
    }

    public BlankRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlankRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBlankListener(BlankListener listener) {
        this.listener = listener;
        this.gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public interface BlankListener {

        void onBlankClick();

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (gestureDetector.onTouchEvent(e)) {
            View childView = findChildViewUnder(e.getX(), e.getY());
            if (childView == null) {
                listener.onBlankClick();
                return true;
            }
        }
        return super.onTouchEvent(e);
    }
}