package com.xs.lightpuzzle.photopicker.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.entity.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.ViewHolder>
        implements View.OnClickListener {

    private List<Photo> mPhotos;
    private RequestManager mGlide;

    public SelectedPhotoAdapter(@NonNull List<Photo> photos, RequestManager glide) {
        this.mPhotos = (photos != null ? photos : new ArrayList<Photo>());
        this.mGlide = glide;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.photo_picker_item_list_selected, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.removeImageBtn.setOnClickListener(this);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String path = mPhotos.get(position).getPath();
        Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .dontTransform()
                .override(800, 800);
        mGlide.load(uri)
                .thumbnail(0.1f)
                .apply(options)
                .into(viewHolder.photoImageView);

        viewHolder.removeImageBtn.setTag(mPhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return (mPhotos != null) ? mPhotos.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Photo) v.getTag());
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, Photo photo);
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        ImageView removeImageBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.selected_item_iv);
            removeImageBtn = itemView.findViewById(R.id.selected_item_ib_remove);
        }
    }
}
