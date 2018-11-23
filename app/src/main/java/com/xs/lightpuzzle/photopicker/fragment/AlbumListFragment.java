package com.xs.lightpuzzle.photopicker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.adapter.AlbumAdapter;
import com.xs.lightpuzzle.photopicker.entity.Album;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public class AlbumListFragment extends BaseFragment {


    RecyclerView mRecyclerView;

    private List<Album> mAlbums;
    private AlbumAdapter mAdapter;

    private RequestManager mGlide;

    public AlbumListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbums = new ArrayList<>();
        mGlide = Glide.with(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_picker_fragment_album_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    public void initView(View view) {
        mRecyclerView = view.findViewById(R.id.album_list_frg_rv);
    }

    public void initData() {
        mAdapter = new AlbumAdapter(mAlbums, mGlide);

        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext()).build());

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mPhotoPicker != null) {
                    mPhotoPicker.openAlbum(mAlbums.get(position));
                }
            }
        });
    }

    public void setAlbums(List<Album> albums) {
        if (albums == null) {
            return;
        }
        mAlbums.clear();
        mAlbums.addAll(albums);
        mAdapter.notifyDataSetChanged();
    }
}
