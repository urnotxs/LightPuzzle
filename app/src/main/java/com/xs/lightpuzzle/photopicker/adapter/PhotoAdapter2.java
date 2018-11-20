package com.xs.lightpuzzle.photopicker.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.IPhotoPicker;
import com.xs.lightpuzzle.photopicker.entity.Photo;

import java.io.File;
import java.util.List;

/**
 * Created by xs on 2018/11/13.
 */

public class PhotoAdapter2 extends BaseQuickAdapter<Photo, BaseViewHolder> {

    private RequestManager mGlide;
    private IPhotoPicker mPhotoPicker;
    private List<Photo> mPhotos;

    public PhotoAdapter2(@Nullable List<Photo> items, RequestManager glide) {
        super(R.layout.photo_picker_item_grid_photo, items);
        mPhotos = items;
        mGlide = glide;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Photo photo) {

        if (photo != null) {
            RequestOptions options = new RequestOptions()
                    .dontAnimate()
                    .dontTransform()
                    .override(200, 200);
            mGlide.load(new File(photo.getPath()))
                    .thumbnail(0.1f)
                    .apply(options)
                    .into((ImageView) helper.getView(R.id.photo_item_iv));

            (helper.getView(R.id.photo_item_iv)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPhotoPicker != null){
                        mPhotoPicker.selectPhoto(photo);
                    }
                }
            });
            (helper.getView(R.id.photo_item_ib_zoom_in)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPhotoPicker != null){
                        mPhotoPicker.previewPhoto(mPhotos.indexOf(photo), mPhotos);
                    }
                }
            });
        }
    }

    public void setPhotoPicker(IPhotoPicker photoPicker) {
        mPhotoPicker = photoPicker;
    }
}
