package com.xs.lightpuzzle.puzzle;

import android.content.Context;
import android.view.View;

/**
 * Created by xs on 2018/11/20.
 */

public abstract class PuzzleDrawView extends View {

    private PuzzlePresenter mPuzzlePresenter;

    public abstract int getScrollYOffset();

    public PuzzleDrawView(Context context) {
        super(context);
    }

    public void setPuzzlePresenter(PuzzlePresenter puzzlePresenter) {
        mPuzzlePresenter = puzzlePresenter;
    }

}
