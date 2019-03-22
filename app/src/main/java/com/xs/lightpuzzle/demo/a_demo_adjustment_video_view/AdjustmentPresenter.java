package com.xs.lightpuzzle.demo.a_demo_adjustment_video_view;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class AdjustmentPresenter extends MvpBasePresenter<AdjustmentView> {

    public float getClipRatio() {
        return 1;
    }

    public void initData() {
        ifViewAttached(new ViewAction<AdjustmentView>() {
            @Override
            public void run(@NonNull AdjustmentView view) {
                view.initShowView();
            }
        });
    }
}
