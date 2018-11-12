package com.xs.lightpuzzle.photopicker.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.entity.Album;

import java.io.File;
import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public class AlbumAdapter extends
        BaseQuickAdapter<Album, BaseViewHolder> {

    private final RequestManager mGlide;

    public AlbumAdapter(@Nullable List<Album> items, RequestManager glide) {
        super(R.layout.photo_picker_item_list_album, items);
        mGlide = glide;
    }


    @Override
    protected void convert(BaseViewHolder helper, Album item) {
        helper.setText(R.id.album_item_tv_name, item.getName());
        helper.setText(R.id.album_item_tv_count, item.getPhotos().size() + "");
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .dontTransform()
                .override(200, 200);
        mGlide.load(new File(item.getCoverPath()))
                .thumbnail(0.1f)
                .apply(options)
                .into((ImageView) helper.getView(R.id.album_item_iv_cover));

    }
}
