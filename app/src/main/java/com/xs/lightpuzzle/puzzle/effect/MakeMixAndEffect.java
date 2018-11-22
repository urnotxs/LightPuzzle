package com.xs.lightpuzzle.puzzle.effect;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;


public class MakeMixAndEffect {

    public static final Bitmap createARGBImage(Bitmap bitmap, int color) {
        int bmpW = bitmap.getWidth();
        int bmpH = bitmap.getHeight();
        int[] argb = new int[bmpW * bmpH];
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int pix;
        int apha;
        bitmap.getPixels(argb, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < argb.length; i++) {
            int color1 = argb[i];
            if (color1 != 0) {
                apha = (color1 & 0xff000000) >> 24;
                pix = (apha << 24) | (r << 16) | (g << 8) | b;
            } else {
                pix = color1;
            }
            argb[i] = pix;
        }

        Bitmap bmp = Bitmap.createBitmap(argb, bmpW, bmpH, Config.ARGB_8888);
        return bmp;
    }
}
