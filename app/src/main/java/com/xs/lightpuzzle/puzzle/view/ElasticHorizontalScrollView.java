package com.xs.lightpuzzle.puzzle.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

/**
 * Created by xs on 2017/12/12.
 */
public class ElasticHorizontalScrollView extends HorizontalScrollView {

    // 拖动的距离, size = 4的意思是: 只允许拖动屏幕的1/4
    private static final int size = 2;
    private View inner;
    private float x;

    private Rect normal = new Rect();

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs,
                                       int defStyle) {
        super(context, attrs, defStyle);
    }

    public ElasticHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElasticHorizontalScrollView(Context context) {
        super(context);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    //将scrollView包含的唯一的一个子布局的view添加进来
    public void onFinishAddView(View v) {
        inner = v;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    TranAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preX = x;
                float nowX = ev.getX();
                int deltaX = (int) (preX - nowX) / size;
                //			scrollBy(deltaX, 0);
                x = nowX;
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(),
                                inner.getBottom());
                        return;
                    }

                    int xx = inner.getLeft() - deltaX;
                    inner.layout(xx, inner.getTop(), inner.getRight() - deltaX, inner.getBottom());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开启动画移动
     */
    public void TranAnimation() {
        TranslateAnimation ta = new TranslateAnimation(inner.getLeft(), normal.left, 0, 0);
        ta.setDuration(200);

        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    /**
     * 是否需要开启动画
     */
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /**
     * 是否需要移动布局
     */
    public boolean isNeedMove() {
        int offset = inner.getMeasuredWidth() - getWidth();
        if (offset <= 0) {
            return false;
        }
        int scrollX = getScrollX();
        if (scrollX == 0 || scrollX == offset) {
            return true;
        }
        return false;
    }
}
