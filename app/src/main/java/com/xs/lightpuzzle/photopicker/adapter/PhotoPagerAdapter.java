package com.xs.lightpuzzle.photopicker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.entity.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Photo> mPhotos;
    private RequestManager mGlide;

    private List<View> mViews;

    public PhotoPagerAdapter(Context context, List<Photo> photos,
                             RequestManager glide) {
        this.mContext = context;
        this.mPhotos = (photos != null ? photos : new ArrayList<Photo>());
        this.mGlide = glide;

        mViews = new ArrayList<>();
        for (int i = 0; i < mPhotos.size(); i++) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.photo_picker_item_pager_photo, null, false);
            mViews.add(view);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        Photo photo = mPhotos.get(position);

        final PhotoView photoView = view.findViewById(R.id.photo_pager_item_iv);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        if (photo != null) {

            RequestOptions options = new RequestOptions()
                    .override(720, 960);
            mGlide.load(new File(photo.getPath()))
                    .apply(options)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource,
                                                    Transition<? super Drawable> transition) {
                            photoView.setImageDrawable(resource);
                            attacher.update();
                        }
                    });
        }
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getCount() {
        return (mPhotos != null) ? mPhotos.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    public void updateData(List<Photo> photos) {
        mPhotos.clear();
        mPhotos.addAll(photos);
        for (int i = 0; i < mPhotos.size(); i++) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.photo_picker_item_pager_photo, null);
            mViews.add(view);
        }
        notifyDataSetChanged();
    }
}
