package com.xs.lightpuzzle.photopicker.util;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by xs on 2018/11/12.
 */

public class AlbumLoader extends CursorLoader {

    private static final String[] PHOTO_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };

    public AlbumLoader(@NonNull Context context) {
        super(context);
        // 定义查询条件
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setProjection(PHOTO_PROJECTION);
        setSelection(MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?");
        setSelectionArgs(new String[]{"image/jpeg", "image/png", "image/jpg"});
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");
    }
}
