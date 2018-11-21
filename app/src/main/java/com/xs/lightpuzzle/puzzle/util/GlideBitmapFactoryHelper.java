package com.xs.lightpuzzle.puzzle.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ImageUtils;
import com.glidebitmappool.GlideBitmapFactory;
import com.glidebitmappool.GlideBitmapPool;
import com.glidebitmappool.internal.Util;
import com.xs.lightpuzzle.yszx.Scheme;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @since 1.0.0
 */
public class GlideBitmapFactoryHelper {
    private static final String TAG = GlideBitmapFactoryHelper.class.getSimpleName();
    public static Bitmap decode(Context context, String uri, int reqWidth, int reqHeight) {
        return decode(context, uri, reqWidth, reqHeight, true);
    }

    public static Bitmap decode(Context context, String uri, int reqWidth, int reqHeight, boolean isPut) {
        if (TextUtils.isEmpty(uri)) {
            throw new IllegalArgumentException("URI can't null or empty");
        }

        Scheme scheme = Scheme.ofUri(uri);
        String path = scheme.crop(uri);

        Bitmap bitmap = null;
        switch (scheme) {
            case FILE:
                if (!isPut) {
                    bitmap = ImageUtils.getBitmap(path, reqWidth, reqHeight);
                } else {
                    if (shouldRotate(context, path)) {
                        //noinspection SuspiciousNameCombination
                        bitmap = GlideBitmapFactory.decodeFile(path, reqHeight, reqWidth);
                        bitmap = ImageUtils
                                .rotate(bitmap, 90, bitmap.getWidth() * 1.0F / 2, bitmap.getHeight() * 1.0F / 2, true);
                    } else {
                        bitmap = GlideBitmapFactory.decodeFile(path, reqWidth, reqHeight);
                    }
                }
                break;
            case DRAWABLE:
                bitmap = GlideBitmapFactory
                        .decodeResource(context.getResources(), Integer.parseInt(path), reqWidth, reqHeight);
                break;
            case ASSETS:
                bitmap = decodeAssets(context, path, reqWidth, reqHeight);
                break;
            case HTTP:
            case HTTPS:
                throw new IllegalArgumentException("Scheme of URI can't be HTTP or HTTPS");
            case CONTENT:
            case UNKNOWN:
            default:
                break;
        }
        if (bitmap != null && isPut) {
//            GlideBitmapPool.putBitmap(bitmap);
        }
        return bitmap;
    }

    @SuppressLint("ObsoleteSdkInt")
    private static Bitmap decodeAssets(Context context, String fileName, int reqWidth,
                                       int reqHeight) {
        AssetManager asset = context.getAssets();
        InputStream is;
        try {
            is = asset.open(fileName);
        } catch (IOException e) {
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        try {
            is.reset();
        } catch (IOException e) {
            return null;
        }

        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
            Bitmap inBitmap = GlideBitmapPool
                    .getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
            if (inBitmap != null && Util.canUseForInBitmap(inBitmap, options)) {
                options.inBitmap = inBitmap;
            }
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                options.inBitmap = null;
            }
            return BitmapFactory.decodeStream(is, null, options);
        }
    }

    public static boolean shouldRotate(Context context, String filePath) {
        return shouldRotate(context.getContentResolver(), Uri.fromFile(new File(filePath)));
    }

    public static boolean shouldRotate(ContentResolver resolver, Uri uri) {
        ExifInterface exif;
        try {
            exif = ExifInterfaceCompat.newInstance(getPath(resolver, uri));
        } catch (IOException e) {
            Log.e(TAG, "could not read exif info of the image: " + uri);
            return false;
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        return orientation == ExifInterface.ORIENTATION_ROTATE_90
                || orientation == ExifInterface.ORIENTATION_ROTATE_270;
    }
    private static final String SCHEME_CONTENT = "content";
    public static String getPath(ContentResolver resolver, Uri uri) {
        if (uri == null) {
            return null;
        }

        if (SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                        null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return uri.getPath();
    }

}
