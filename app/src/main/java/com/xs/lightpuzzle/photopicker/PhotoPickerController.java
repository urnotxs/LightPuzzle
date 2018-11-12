package com.xs.lightpuzzle.photopicker;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.xs.lightpuzzle.photopicker.entity.Album;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.photopicker.fragment.AlbumListFragment;
import com.xs.lightpuzzle.photopicker.fragment.PhotoListFragment;
import com.xs.lightpuzzle.photopicker.fragment.PhotoPagerFragment;

import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

class PhotoPickerController {

    private static PhotoPickerController INSTANCE;

    static PhotoPickerController getDefault(FragmentActivity activity,
                                            int containerId) {
        if (!(activity instanceof IPhotoPicker)) {
            throw new IllegalArgumentException("Activity should implement IPhotoPicker interface");
        }
        if (INSTANCE == null) {
            synchronized (PhotoPickerController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PhotoPickerController(activity, containerId);
                }
            }
        }
        return INSTANCE;
    }

    private IPhotoPicker mPhotoPicker;

    private int mContainerId;
    private FragmentManager mFragmentManager;

    private PhotoListFragment mPhotoListFragment;
    private AlbumListFragment mAlbumListFragment;
    private PhotoPagerFragment mPhotoPagerFragment;

    public PhotoPickerController(FragmentActivity activity, int containerId) {
        mPhotoPicker = (IPhotoPicker) activity;
        mContainerId = containerId;
        mFragmentManager = activity.getSupportFragmentManager();
        init();
    }

    private void init() {
        mPhotoListFragment = new PhotoListFragment();
        mPhotoListFragment.setPhotoPicker(mPhotoPicker);

        mAlbumListFragment = new AlbumListFragment();
        mAlbumListFragment.setPhotoPicker(mPhotoPicker);

        mPhotoPagerFragment = new PhotoPagerFragment();
        mPhotoPagerFragment.setPhotoPicker(mPhotoPicker);

        mFragmentManager.beginTransaction()
                .add(mContainerId, mPhotoListFragment)
                .add(mContainerId, mAlbumListFragment)
                .add(mContainerId, mPhotoPagerFragment)
                .commit();
    }

    void showPhotoList(List<Photo> photos) {
        hideFragments();

        mFragmentManager.beginTransaction()
                .show(mPhotoListFragment)
                .commit();
        if (photos != null && !photos.isEmpty()) {
            mPhotoListFragment.setPhotos(photos);
        }
    }

    public void showAlbumList(List<Album> albums) {
        hideFragments();
        mFragmentManager.beginTransaction()
                .show(mAlbumListFragment)
                .commit();
        if (albums != null && !albums.isEmpty()) {
            mAlbumListFragment.setAlbums(albums);
        }
    }

    public void showPhotoPager(int currItem, List<Photo> photos) {
        hideFragments();

        mFragmentManager.beginTransaction()
                .show(mPhotoPagerFragment)
                .commit();
        if (photos != null && !photos.isEmpty()) {
            mPhotoPagerFragment.setPhotos(photos);
            mPhotoPagerFragment.setCurrentPagerItem(currItem);
        }
    }

    PhotoListFragment getPhotoListFragment() {
        return mPhotoListFragment;
    }

    private void hideFragments() {
        mFragmentManager.beginTransaction()
                .hide(mPhotoListFragment)
                .hide(mAlbumListFragment)
                .hide(mPhotoPagerFragment)
                .commit();
    }

    static void destroy() {
        if (INSTANCE != null) {
            INSTANCE = null;
        }
    }

}
