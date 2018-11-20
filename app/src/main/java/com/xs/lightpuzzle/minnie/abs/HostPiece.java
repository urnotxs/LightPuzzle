package com.xs.lightpuzzle.minnie.abs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.xs.lightpuzzle.minnie.piece.Piece;

/**
 * Created by xs on 2018/11/13.
 */

public abstract class HostPiece extends Piece{

    public HostPiece(Context context,
                     int srcSingleLeft, int srcSingleTop,
                     int srcSingleWidth, int srcSingleHeight,
                     RectF srcContentRegion, int destSingleWidth) {
        super(context, srcSingleLeft, srcSingleTop,
                srcSingleWidth, srcSingleHeight,
                srcContentRegion, destSingleWidth);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void drawDirectly(Canvas canvas) {

    }
}
