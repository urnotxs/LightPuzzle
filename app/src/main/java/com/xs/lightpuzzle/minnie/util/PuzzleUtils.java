package com.xs.lightpuzzle.minnie.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xs.lightpuzzle.yszx.Scheme;

import java.io.IOException;

/**
 * Created by xs on 2018/11/13.
 */

public class PuzzleUtils {

    public static void tryToRecycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static int[] getBitmapSize(Context context, String uri) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Scheme scheme = Scheme.ofUri(uri);
        switch (scheme) {
            case FILE:
                BitmapFactory.decodeFile(scheme.crop(uri), options);
                break;
            case ASSETS:
                BitmapFactory
                        .decodeStream(context.getAssets().open(scheme.crop(uri)), null, options);
                break;
            case DRAWABLE:
                BitmapFactory.decodeResource(context.getResources(),
                        Integer.parseInt(scheme.crop(uri)), options);
                break;
            case HTTP:
            case HTTPS:
            case CONTENT:
            case UNKNOWN:
            default:
                throw new IllegalArgumentException("URI must be local resources");
        }
        return new int[]{options.outWidth, options.outHeight};
    }

}
