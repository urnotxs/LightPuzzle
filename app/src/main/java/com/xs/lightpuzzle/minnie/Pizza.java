package com.xs.lightpuzzle.minnie;

import android.content.Context;
import android.graphics.RectF;

import com.xs.lightpuzzle.minnie.abs.HostPiece;

/**
 * Created by xs on 2018/11/13.
 */

public class Pizza extends HostPiece {

    public Pizza(Context context,
                 int srcSingleLeft, int srcSingleTop,
                 int srcSingleWidth, int srcSingleHeight,
                 RectF srcContentRegion, int destSingleWidth) {
        super(context, srcSingleLeft, srcSingleTop, srcSingleWidth,
                srcSingleHeight, srcContentRegion, destSingleWidth);
    }
}
