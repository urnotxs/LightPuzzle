package com.xs.lightpuzzle.puzzle;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;

/**
 * Created by xs on 2018/11/20.
 */

public interface PuzzleView extends MvpView {

    void setPageData(PuzzlesInfo puzzlesInfo);

    //普通层级刷新
    void invalidateView();

    //改变大小级刷新
    void invalidateView(int width, int height, int templateSize);

    //改变大小级相对位置
    void invalidateView(int width, int height, int templateSize, int differ);

    //既要改变大小也要滑动的刷新
    void invalidateViewToScroll(int width, int height, int templateSize, int bottom);

}
