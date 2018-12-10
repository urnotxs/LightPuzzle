package com.xs.lightpuzzle.puzzle.piece;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by xs on 2018/9/6.
 */

public class AjustmentPiece extends LayoutJointPiece {
    /**
     * 构造函数
     *
     * @param drawable
     * @param rectF
     * @param matrix
     * @param id
     */
    public AjustmentPiece(Drawable drawable, RectF rectF, Matrix matrix, int id) {
        super(drawable, rectF, matrix, id);
    }

/*    public void draw(Canvas canvas) {
        draw(canvas , false);
    }*/


}
