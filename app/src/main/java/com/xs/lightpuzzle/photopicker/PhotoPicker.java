package com.xs.lightpuzzle.photopicker;

import android.app.Activity;
import android.content.Intent;

import com.xs.lightpuzzle.photopicker.entity.Photo;

import java.util.ArrayList;

/**
 * Created by xs on 2018/11/12.
 */

public class PhotoPicker {
    /**
     * 场景一: 跳转至选图界面选择单张图片
     */
    public static void radio(Activity activity) {
        Intent intent = new Intent(activity, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SCENE, PhotoPickerActivity.SCENE_1_RADIO);
        activity.startActivityForResult(intent, PhotoPickerActivity.RADIO_PICK_REQ_CODE);
    }

    /**
     * 场景一: 跳转至选图界面选一张或者多张图片
     *
     * @param activity 活动
     * @param maxCount 可选择图片的最大数目
     * @param originalPhotos 之前已选中的图片
     */
    public static void multi(Activity activity, int maxCount, ArrayList<Photo> originalPhotos) {
        Intent intent = new Intent(activity, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SCENE, PhotoPickerActivity.SCENE_1_MULTI);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, maxCount);
        intent.putParcelableArrayListExtra(PhotoPickerActivity.EXTRA_ORIGINAL_PHOTOS, originalPhotos);
        activity.startActivityForResult(intent, PhotoPickerActivity.MULTI_PICK_REQ_CODE);
    }
}
