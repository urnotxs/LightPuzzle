package com.xs.lightpuzzle.puzzle.view.textedit.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

public class SoftKeyboardStateWatcher implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);
        void onSoftKeyboardChange(int keyboardHeightInPx);
        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> listeners = new LinkedList<SoftKeyboardStateListener>();
    private final Activity activity;
    private final View activityRootView;
    private int lastSoftKeyboardHeightInPx;
    private boolean isSoftKeyboardOpened;
    private long lastTime;

    public SoftKeyboardStateWatcher(View activityRootView) {
        this(activityRootView, false, null);
    }

    public SoftKeyboardStateWatcher(View activityRootView, Activity activity) {
        this(activityRootView, false, activity);
    }

    public SoftKeyboardStateWatcher(View activityRootView, boolean isSoftKeyboardOpened, Activity activity) {
        this.activity = activity;
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r);

        final int activityRootHeight= activityRootView.getRootView().getHeight();
        final int navigationBarHeight = 0;
//        final int navigationBarHeight = SystemUiUtils.getNavigationBarHeight(activity);
        final int heightDiff = activityRootHeight - navigationBarHeight - (r.bottom - r.top);
        if (!isSoftKeyboardOpened && heightDiff > activityRootHeight/4) { // if more than activityRootHeight/4 pixels, its probably a keyboard...
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
            return;
        }

        if (isSoftKeyboardOpened && heightDiff < activityRootHeight/4 ) {
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
            return;
        }

        if (isSoftKeyboardOpened && heightDiff > activityRootHeight/4 && System.currentTimeMillis() - lastTime > 300) { // 改变键盘高度,防止频繁调用
            lastTime = System.currentTimeMillis();
            notifyonSoftKeyboardChange(heightDiff);
            return;
        }
    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }

    /**
     * Default value is zero {@code 0}.
     *
     * @return last saved keyboard height in px
     */
    public int getLastSoftKeyboardHeightInPx() {
        return lastSoftKeyboardHeightInPx;
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void removeSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.remove(listener);
    }

    public void removeAllSoftKeyboardStateListener() {
        listeners.clear();
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx;

        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyonSoftKeyboardChange(int keyboardHeightInPx) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx;

        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardChange(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }


}