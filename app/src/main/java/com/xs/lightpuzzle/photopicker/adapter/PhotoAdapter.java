package com.xs.lightpuzzle.photopicker.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.IPhotoPicker;
import com.xs.lightpuzzle.photopicker.entity.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<Photo> mPhotos;
    private RequestManager mGlide;

    private IPhotoPicker mPhotoPicker;

    public PhotoAdapter(@NonNull List<Photo> photos, RequestManager glide) {
        this.mPhotos = (photos != null ? photos : new ArrayList<Photo>());
        this.mGlide = glide;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.photo_picker_item_grid_photo, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final Photo photo = mPhotos.get(position);
        if (photo != null) {
            RequestOptions options = new RequestOptions()
                    .dontAnimate()
                    .dontTransform()
                    .override(200, 200);
            mGlide.load(new File(photo.getPath()))
                    .thumbnail(0.1f)
                    .apply(options)
                    .into(viewHolder.photoImageView);

            if (mPhotoPicker != null) {
                viewHolder.photoImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhotoPicker.selectPhoto(photo);
                    }
                });

                viewHolder.zoomInImageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhotoPicker.previewPhoto(position, mPhotos);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mPhotos != null) ? mPhotos.size() : 0;
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        ImageView zoomInImageBtn;
        View checkedView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photo_item_iv);
            zoomInImageBtn = itemView.findViewById(R.id.photo_item_ib_zoom_in);
            checkedView = itemView.findViewById(R.id.photo_item_v_checked);
        }
    }

    public void setPhotoPicker(IPhotoPicker photoPicker) {
        mPhotoPicker = photoPicker;
    }
}

