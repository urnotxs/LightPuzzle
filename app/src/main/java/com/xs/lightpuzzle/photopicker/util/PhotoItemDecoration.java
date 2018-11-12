package com.xs.lightpuzzle.photopicker.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xs on 2018/11/12.
 */

public class PhotoItemDecoration extends RecyclerView.ItemDecoration {

    private int mMargin;

    public PhotoItemDecoration(Context context) {
        mMargin = PhotoPickerUtils.dp2px(context, 0.5f);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = mMargin;
        outRect.top = mMargin;
        outRect.right = mMargin;
        outRect.bottom = mMargin;
    }
}
