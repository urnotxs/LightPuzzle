package com.xs.lightpuzzle.puzzle.view.texturecolor;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleBackgroundBean;

/**
 * Created by xs on 2018/4/11.
 */

public class EditBgTexturePresenter extends MvpBasePresenter<EditBgTextureView> {
    private TextureColorHelper mTextureColorHelper;
    // 下标值
    private int mColorIndex = -1, mTextureIndex = -1;

    public EditBgTexturePresenter() {
        mTextureColorHelper = TextureColorHelper.getInstance();
    }

    public void loadUiDatas(String texture, String bgColor) {
        final int[] colors = mTextureColorHelper.loadBgColor();
        final int[] textureResIds = mTextureColorHelper.loadTextureResId();
        final boolean[] isVips = mTextureColorHelper.getTextureVips();

        mTextureIndex = mTextureColorHelper.getTextureIndex(texture);
        mColorIndex = mTextureColorHelper.getColorIndex(bgColor);
        if (mColorIndex == -1) {
            mColorIndex = 0;
        }

        ifViewAttached(new ViewAction<EditBgTextureView>() {
            @Override
            public void run(@NonNull EditBgTextureView view) {
                view.setUiDatas(colors, textureResIds, isVips);
            }
        });
    }

    public void loadPuzzleBackgroundBean() {

        ifViewAttached(new ViewAction<EditBgTextureView>() {
            @Override
            public void run(@NonNull EditBgTextureView view) {
                PuzzleBackgroundBean puzzleBackgroundBean =
                        mTextureColorHelper.getPuzzleBackgroundBean(mColorIndex, mTextureIndex);
                view.getPuzzleBackgroundBean(puzzleBackgroundBean);
            }
        });
    }

    public int getColorIndex() {
        return mColorIndex;
    }

    public void setColorIndex(int colorIndex) {
        mColorIndex = colorIndex;
    }

    public int getTextureIndex() {
        return mTextureIndex;
    }

    public void setTextureIndex(int textureIndex) {
        mTextureIndex = textureIndex;
    }
}
