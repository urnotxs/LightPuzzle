package com.xs.lightpuzzle.photopicker.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.adapter.PhotoAdapter;
import com.xs.lightpuzzle.photopicker.entity.Album;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.photopicker.util.PhotoItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public class PhotoListFragment extends BaseFragment {

    RecyclerView mRecyclerView;

    private PhotoAdapter mAdapter;
    private Album mAlbum;
    private List<Photo> mPhotos;

    private RequestManager mGlide;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGlide = Glide.with(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_picker_fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.photo_grid_frg_rv);
    }

    private void initData() {
        mPhotos = new ArrayList<>();
        mAdapter = new PhotoAdapter(mPhotos, mGlide);

        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new PhotoItemDecoration(getContext()));

        mAdapter.setPhotoPicker(mPhotoPicker);

        setAlbum(mAlbum);
    }

    public void setAlbum(Album album) {
        mAlbum = album;
        if (mAlbum != null) {
            setPhotos(mAlbum.getPhotos());
        }
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos.clear();
        mPhotos.addAll(photos);
        mAdapter.notifyDataSetChanged();
    }
}
