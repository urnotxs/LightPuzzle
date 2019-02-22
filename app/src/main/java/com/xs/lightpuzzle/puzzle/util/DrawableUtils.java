package com.xs.lightpuzzle.puzzle.util;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by xs on 2019/1/10.
 */

public class DrawableUtils {

    /**
     * 设置圆角并填充颜色
     *
     * @param color
     * @param radius
     * @return
     */
    public static ShapeDrawable shapeDrawable(int color, int radius) {
        return shapeDrawable(true, true, true, true, color, radius);
    }

    /**
     * 圆角边框
     *
     * @param tl     左上
     * @param tr     右上
     * @param br     右下
     * @param bl     左下
     * @param color  颜色
     * @param radius 圆角大小
     * @return
     */
    public static ShapeDrawable shapeDrawable(boolean tl, boolean tr, boolean br, boolean bl, int color, int radius) {
        float[] outerRadii = new float[8];
        if (tl) {
            outerRadii[0] = radius;
            outerRadii[1] = radius;
        }
        if (tr) {
            outerRadii[2] = radius;
            outerRadii[3] = radius;
        }
        if (br) {
            outerRadii[4] = radius;
            outerRadii[5] = radius;
        }
        if (bl) {
            outerRadii[6] = radius;
            outerRadii[7] = radius;
        }
        // 构造一个圆角矩形,可以使用其他形状，这样ShapeDrawable 就会根据形状来绘制。
        RoundRectShape round = new RoundRectShape(outerRadii, null, null);
        // 如果要构造直角矩形可以
        //RectShape rectShape = new RectShape();
        // 组合圆角矩形和ShapeDrawable
        ShapeDrawable shape = new ShapeDrawable(round);
        // 设置形状的颜色
        shape.getPaint().setColor(color);
        // 设置绘制方式为填充
        shape.getPaint().setStyle(Paint.Style.FILL);
        return shape;
    }
}
