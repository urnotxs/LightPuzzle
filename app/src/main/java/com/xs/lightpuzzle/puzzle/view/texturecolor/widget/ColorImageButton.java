package com.xs.lightpuzzle.puzzle.view.texturecolor.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.NoDoubleClickListener;
import com.xs.lightpuzzle.puzzle.view.texturecolor.ColorLayout;

/**
 * Created by xs on 2018/1/24.
 */

public class ColorImageButton extends AppCompatImageView {
    private int mColorValue = 0xffffffff;
    private int mIndex = -1;
    private boolean mIsCheck = false;

    public ColorImageButton(Context context, int colorValue, int index) {
        super(context);
        mColorValue = colorValue;
        mIndex = index;

        // 空隙12
        Drawable drawable = getContext().getResources()
                .getDrawable(R.drawable.choose_bg_color_image1).mutate();
        this.setImageDrawable(tintDrawable(drawable, ColorStateList.valueOf(mColorValue)));
        this.setOnClickListener(mNoDoubleClickListener);
    }

    public void setCheckState(boolean check) {
        if (mIsCheck == check) {
            return;
        }

        mIsCheck = check;

        Drawable drawable;
        if (check) {
            drawable = getContext().getResources()
                    .getDrawable(R.drawable.choose_bg_color_image1_hover);
        } else {
            drawable = getContext().getResources()
                    .getDrawable(R.drawable.choose_bg_color_image1);
        }

        this.setImageDrawable(tintDrawable(drawable, ColorStateList.valueOf(mColorValue)));
    }

    public Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public int getColorValue() {
        return mColorValue;
    }

    public void setColorValue(int colorValue) {
        this.mColorValue = colorValue;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public boolean isCheck() {
        return mIsCheck;
    }

    private ColorLayout.ColorSelectCallback mColorSelectCallback;

    public void setColorSelectCallback(ColorLayout.ColorSelectCallback callback) {
        mColorSelectCallback = callback;
    }

    private NoDoubleClickListener mNoDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            if (!mIsCheck) {
                setCheckState(true);
                if (mColorSelectCallback != null) {
                    mColorSelectCallback.select(ColorImageButton.this);
                }
            }
        }
    };
}
