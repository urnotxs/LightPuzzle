package com.xs.lightpuzzle.photopicker.util;

import android.content.Context;

/**
 * Created by xs on 2018/11/12.
 */

public final class PhotoPickerUtils {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
