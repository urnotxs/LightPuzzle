package com.xs.lightpuzzle.puzzle.view.texturecolor;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleBackgroundBean;

/**
 * Created by xs on 2018/4/11.
 */

public interface EditBgTextureView extends MvpView {
    void setUiDatas(int[] bgColors, int[] textureResIds, boolean[] isVips);

    void getPuzzleBackgroundBean(PuzzleBackgroundBean puzzleBackgroundBean);
}
