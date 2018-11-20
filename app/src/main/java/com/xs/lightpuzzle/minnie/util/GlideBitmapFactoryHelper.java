//package com.xs.lightpuzzle.minnie.util;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//
//import com.blankj.utilcode.util.ImageUtils;
//import com.glidebitmappool.GlideBitmapFactory;
//import com.xs.lightpuzzle.yszx.Scheme;
//
///**
// * Created by xs on 2018/11/13.
// */
//
//public class GlideBitmapFactoryHelper {
//
//    public static Bitmap decode(Context context,
//                                String uri, int reqWidth, int reqHeight){
//        return decode(context, uri, reqWidth, reqHeight, true);
//    }
//
//    private static Bitmap decode(Context context, String uri,
//                                 int reqWidth, int reqHeight, boolean isPut) {
//        if (TextUtils.isEmpty(uri)) {
//            throw new IllegalArgumentException("URI can't null or empty");
//        }
//
//        Scheme scheme = Scheme.ofUri(uri);
//        String path = scheme.crop(uri);
//
//        Bitmap bitmap = null;
//        switch (scheme) {
//            case FILE:
//                if (!isPut) {
//                    bitmap = ImageUtils.getBitmap(path, reqWidth, reqHeight);
//                } else {
//                    if (PhotoMetadataUtils.shouldRotate(context, path)) {
//                        //noinspection SuspiciousNameCombination
//                        bitmap = GlideBitmapFactory.decodeFile(path, reqHeight, reqWidth);
//                        bitmap = ImageUtils
//                                .rotate(bitmap, 90, bitmap.getWidth() * 1.0F / 2, bitmap.getHeight() * 1.0F / 2, true);
//                    } else {
//                        bitmap = GlideBitmapFactory.decodeFile(path, reqWidth, reqHeight);
//                    }
//                }
//                break;
//            case DRAWABLE:
//                bitmap = GlideBitmapFactory
//                        .decodeResource(context.getResources(), Integer.parseInt(path), reqWidth, reqHeight);
//                break;
//            case ASSETS:
//                bitmap = decodeAssets(context, path, reqWidth, reqHeight);
//                break;
//            case HTTP:
//            case HTTPS:
//                throw new IllegalArgumentException("Scheme of URI can't be HTTP or HTTPS");
//            case CONTENT:
//            case UNKNOWN:
//            default:
//                break;
//        }
//        if (bitmap != null && isPut) {
////            GlideBitmapPool.putBitmap(bitmap);
//        }
//        return bitmap;
//    }
//}
