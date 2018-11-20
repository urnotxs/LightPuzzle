package com.xs.lightpuzzle.minnie.presenter;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.xs.lightpuzzle.minnie.view.PuzzleView;

/**
 * Created by xs on 2018/11/13.
 */

public class PuzzlePresenter extends MvpBasePresenter<PuzzleView> {

    public void invalidate(){
        ifViewAttached(new ViewAction<PuzzleView>() {
            @Override
            public void run(@NonNull final PuzzleView view) {

            }
        });
    }
}
