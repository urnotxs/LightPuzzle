package com.xs.lightpuzzle.minnie.abs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.xs.lightpuzzle.minnie.piece.Piece;

/**
 * Created by xs on 2018/11/13.
 */

public abstract class StaticLayerPiece extends Piece {

    protected StaticLayerPiece(Context context,
                               int srcSingleWidth, int srcSingleHeight,
                               int destSingleWidth, int destSingleHeight) {
        super(context, srcSingleWidth, srcSingleHeight,
                new RectF(0, 0, srcSingleWidth, srcSingleHeight),
                destSingleWidth, destSingleHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        drawDirectly(canvas);
    }
}
