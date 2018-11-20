package com.xs.lightpuzzle.minnie;

import android.content.Context;
import android.graphics.RectF;

/**
 * Created by xs on 2018/11/13.
 */

public class PlatterPizza extends com.xs.lightpuzzle.minnie.abs.HostPiece{

    public PlatterPizza(Context context,
                        int srcSingleLeft, int srcSingleTop,
                        int srcSingleWidth, int srcSingleHeight,
                        RectF srcContentRegion, int destSingleWidth) {
        super(context, srcSingleLeft, srcSingleTop, srcSingleWidth,
                srcSingleHeight, srcContentRegion, destSingleWidth);
    }
/*    public PlatterPizza(Context context,
                        int srcSingleWidth, int srcSingleHeight,
                        int destSingleWidth, int destSingleHeight,
                        Map<Integer, Pizza> pizzaMap,
                        BackgroundPiece backgroundPiece, boolean isDrawEachPizzaBackground,
                        WatermarkPiece watermarkPiece, boolean isDrawEachPizzaWatermark) {
        super(context, INDEX, 0, 0, srcSingleWidth, srcSingleHeight,
                destSingleWidth, destSingleHeight);
        this.pizzaMap = pizzaMap;
        this.backgroundPiece = backgroundPiece;
        this.watermarkPiece = watermarkPiece;
        this.isDrawEachPizzaBackground = isDrawEachPizzaBackground;
        this.isDrawEachPizzaWatermark = isDrawEachPizzaWatermark;
    }*/
}
