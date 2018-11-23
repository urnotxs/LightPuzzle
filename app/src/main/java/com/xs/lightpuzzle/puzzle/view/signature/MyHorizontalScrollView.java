package com.xs.lightpuzzle.puzzle.view.signature;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;

import com.xs.lightpuzzle.puzzle.view.ElasticHorizontalScrollView;

/**
 * Created by xs on 2018/8/23.
 */

public class MyHorizontalScrollView extends ElasticHorizontalScrollView {

    private static final String TAG = "MyHorizontalScrollView";

    private Handler mHandler;

    private ScrollViewListener scrollViewListener;

    /**
     * 滚动状态:
     * IDLE=滚动停止
     * TOUCH_SCROLL=手指拖动滚动
     * FLING=滚动
     */
    enum ScrollType {
        IDLE, TOUCH_SCROLL, FLING
    }

    ;

    /**
     * 记录当前滚动的距离
     */
    private int currentX = -9999999;

    /**
     * 当前滚动状态
     */
    private ScrollType scrollType = ScrollType.IDLE;

    public interface ScrollViewListener {
        void onScrollChanged(ScrollType scrollType);
    }

    public MyHorizontalScrollView(Context context) {
        super(context);
        mHandler = new Handler();
    }

    public void setOnScrollStateChangedListener(ScrollViewListener listener) {
        this.scrollViewListener = listener;
    }

    /**
     * 滚动监听runnable
     */
    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (getScrollX() == currentX) {
                //滚动停止,取消监听线程
                scrollType = ScrollType.IDLE;
                if (scrollViewListener != null) {
                    scrollViewListener.onScrollChanged(scrollType);
                }
                mHandler.removeCallbacks(this);
                return;
            } else {
                //手指离开屏幕,但是view还在滚动
                scrollType = ScrollType.FLING;
                if (scrollViewListener != null) {
                    scrollViewListener.onScrollChanged(scrollType);
                }
            }
            currentX = getScrollX();
            //滚动监听间隔:milliseconds
            mHandler.postDelayed(this, 50);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.scrollType = ScrollType.TOUCH_SCROLL;
                scrollViewListener.onScrollChanged(scrollType);
                mHandler.removeCallbacks(scrollRunnable);
                break;
            case MotionEvent.ACTION_UP:
                mHandler.post(scrollRunnable);
                break;
        }
        return super.onTouchEvent(ev);
    }
}
