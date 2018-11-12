package com.xs.lightpuzzle.photopicker;

import com.xs.lightpuzzle.photopicker.entity.Album;
import com.xs.lightpuzzle.photopicker.entity.Photo;

import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public interface IPhotoPicker {

    /** 获取当前的场景 */
    int getScene();

    /** 获取被选中的图片 */
    List<Photo> getSelectedPhotos();

    /**
     * 选择图片
     */
    void selectPhoto(Photo photo);

    /**
     * 预览图片
     */
    void previewPhoto(int currItem, List<Photo> photos);

    /**
     * 打开相册
     */
    void openAlbum(Album album);
}
