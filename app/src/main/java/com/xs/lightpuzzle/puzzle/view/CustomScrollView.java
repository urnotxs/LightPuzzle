package com.xs.lightpuzzle.puzzle.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private OnBorderListener onBorderListener;
    private OnScrollListener onScrollListener;
    private OnScrollStopListener onScrollStopListener;
    private boolean isScroll = true;
    private int lastScrolly = 0;
    private Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (lastScrolly == CustomScrollView.this.getScrollY()) {
                    if (onScrollStopListener != null) {
                        onScrollStopListener.onStop(CustomScrollView.this.getScrollY());
                    }
                } else {
                    handler.sendMessageDelayed(handler.obtainMessage(1), 1);
                    lastScrolly = CustomScrollView.this.getScrollY();
                }
            }
        }
    };

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    private float scrollVelocityY = 1.0f;

    public void setScrollVelocityY(float scrollVelocityY) {
        this.scrollVelocityY = scrollVelocityY;
    }

    /**
     * 滑动速度
     */
    @Override
    public void fling(int velocityY) {
        super.fling((int) (velocityY * scrollVelocityY));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        doOnBorderListener();
        if (onScrollListener != null) {
            onScrollListener.onScroll(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * OnBorderListener, Called when scroll to top or bottom
     */
    public interface OnBorderListener {

        void onBottom();

        void onTop();

        void onMove();
    }

    public interface OnScrollListener {

        void onScroll(int scrollX, int scrollY, int oldl, int oldt);
    }

    public interface OnScrollStopListener{
        void onStop(int scrolly);
    }

    /**
     * 滑动监听
     */
    private void doOnBorderListener() {
        View contentView = getChildAt(0);
        //contentView.getMeasuredHeight()整个高度  包括超出屏幕
        //getScrollY() 向上滚动偏移量 为正
        //getHeight()) 可见区域高度
        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
            if (onBorderListener != null) {
                onBorderListener.onBottom();
            }
        } else if (getScrollY() == 0) {
            if (onBorderListener != null) {
                onBorderListener.onTop();
            }
        } else {
            if (onBorderListener != null) {
                onBorderListener.onMove();
            }
            handler.sendMessageDelayed(handler.obtainMessage(1), 1);
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 设置scrollview滑动边界监听器
     */
    public void setOnBorderTouchListener(OnBorderListener listener) {
        this.onBorderListener = listener;
    }

    public void setOnScrollStopListener(OnScrollStopListener onScrollStopListener){
        this.onScrollStopListener = onScrollStopListener;
    }

    /**
     * 设置scrollview是否能滑动
     */
    public void setScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(1), 5);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
