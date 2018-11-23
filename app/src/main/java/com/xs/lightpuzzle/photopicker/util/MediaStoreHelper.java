package com.xs.lightpuzzle.photopicker.util;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.entity.Album;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;

/**
 * Created by xs on 2018/11/12.
 */

public class MediaStoreHelper {
    final static int INDEX_ALL_PHOTOS = 0;

    public static void getAlubums(FragmentActivity activity, Callback callback) {
        LoaderManager.getInstance(activity)
                .initLoader(0, null, new AlbumLoaderCallbacks(activity, callback));
    }

    static class AlbumLoaderCallbacks implements
            LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> mContext;
        private Callback mCallback;

        public AlbumLoaderCallbacks(Context context, Callback callback) {
            mContext = new WeakReference<>(context);
            mCallback = callback;
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new AlbumLoader(mContext.get());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

            if (cursor == null) {
                return;
            }

            List<Album> albums = new ArrayList<>();

            Album allPhotoAlbum = new Album();
            allPhotoAlbum.setName(mContext.get().getString(R.string.photo_picker_aty_all_images));
            allPhotoAlbum.setId("ALL");

            while (cursor.moveToNext()) {

                String buckedId = cursor.getString(cursor.getColumnIndexOrThrow(BUCKET_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));

                int photoId = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));

                Album album = new Album();
                album.setId(buckedId);
                album.setName(name);

                if (!albums.contains(album)) {
                    album.setCoverPath(path);
                    album.addPhoto(photoId, path);
                    album.setDataAdded(cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED)));
                    albums.add(album);
                } else {
                    albums.get(albums.indexOf(album)).addPhoto(photoId, path);
                }

                allPhotoAlbum.addPhoto(photoId, path);
            }

            if (allPhotoAlbum.getPhotoPaths().size() > 0) {
                allPhotoAlbum.setCoverPath(allPhotoAlbum.getPhotoPaths().get(0));
            }

            albums.add(INDEX_ALL_PHOTOS, allPhotoAlbum);

            if (mCallback != null) {
                mCallback.onResultCallback(albums);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    }

    public interface Callback {

        void onResultCallback(List<Album> albums);
    }
}
