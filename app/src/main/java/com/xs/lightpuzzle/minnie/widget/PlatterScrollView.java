package com.xs.lightpuzzle.minnie.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by xs on 2018/11/13.
 */

public class PlatterScrollView extends ScrollView {

    public PlatterScrollView(Context context) {
        super(context);
    }

    public PlatterScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlatterScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlatterScrollView(Context context, AttributeSet attrs,
                             int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface OnScrollStoppedListener {

        void onScrollStopped();
    }

    private OnScrollStoppedListener mOnScrollStoppedListener;

    public void setOnScrollStoppedListener(OnScrollStoppedListener listener) {
        mOnScrollStoppedListener = listener;
    }

    private Runnable mScrollerTask;
    private int mLastScrollY;
    private int mCheckPeriod = 100;
    
    public void startScrollerTask() {
        mLastScrollY = getScrollY();
        if (mScrollerTask == null) {
            mScrollerTask = new Runnable() {

                public void run() {
                    int currScrollY = getScrollY();
                    if (mLastScrollY - currScrollY == 0) { // has stopped
                        if (mOnScrollStoppedListener != null) {
                            mOnScrollStoppedListener.onScrollStopped();
                        }
                    } else {
                        mLastScrollY = getScrollY();
                        postDelayed(mScrollerTask, mCheckPeriod);
                    }
                }
            };
        }
        postDelayed(mScrollerTask, mCheckPeriod);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    /**
     * API-23
     */
    public interface OnScrollChangedListener {

        /**
         * Called when the scroll position of a view changes.
         *
         * @param v          The view whose scroll position has changed.
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
