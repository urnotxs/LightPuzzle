package com.xs.lightpuzzle.demo.a_demo_adjustment_video_view;


import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public interface AdjustmentView extends MvpView {
    void initShowView();
    void seekToFinishPlay(int seekTime, int end);
    void seekToFinishNoPlay(int seekTime);
}
