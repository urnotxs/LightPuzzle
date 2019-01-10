package com.xs.lightpuzzle.materials;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Space;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xs.lightpuzzle.constant.DataConstant;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xs on 2018/11/8.
 */

public class MaterialListFragment extends
        BaseMvpLceFragment<SwipeRefreshLayout, List<TemplateSet>, MaterialListView, MaterialListPresenter>
        implements MaterialListView, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_QUERY = "query";
    private static final String ARG_MATERIAL_LIST = "material_list";

    public static MaterialListFragment newInstance(
            TemplateSetQuery query, int materialList) {
        MaterialListFragment fragment = new MaterialListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUERY, query);
        args.putInt(ARG_MATERIAL_LIST, materialList);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.material_list_fg_rv)
    RecyclerView mRecyclerView;

    private List<TemplateSet> mData;
    private MaterialAdapter mAdapter;
    private LayoutAdapter mLayoutAdapter;
    private TemplateSetQuery mQuery;
    private int mMaterialList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Arguments can't be bull");
        }
        mQuery = args.getParcelable(ARG_QUERY);
        mMaterialList = args.getInt(ARG_MATERIAL_LIST, MATERIAL_LIST.NOT_FLAG);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void notifyAdapter(int position, TemplateSet templateSet) {
        mData.set(mData.indexOf(templateSet), templateSet);
        mAdapter.notifyItemChanged(position);
    }

    @Subscribe
    public void onDownloadingTemplateEven(MaterialListEventBus.DownloadingTemplate event) {
        TemplateSet templateSet = event.getTemplateSet();
        mAdapter.addDownloading(templateSet.getId());
        notifyAdapter(event.getPosition(), templateSet);
    }

    @Subscribe
    public void onDownloadedTemplateEven(MaterialListEventBus.DownloadedTemplate event) {
        TemplateSet templateSet = event.getTemplateSet();
        mAdapter.removeDownloading(templateSet.getId());
        notifyAdapter(event.getPosition(), templateSet);
    }

    @Subscribe
    public void onUsedTemplateEvent(MaterialListEventBus.UsedTemplate event) {
        TemplateSet templateSet = event.getTemplateSet();
        if (mMaterialList == MATERIAL_LIST.HISTORY) {
            if (mData.indexOf(templateSet) == -1) {
                mAdapter.addData(0, templateSet);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            notifyAdapter(event.getPosition(), templateSet);
        }
    }

    @Subscribe
    public void onTemplateDeletedEvent(MaterialListEventBus.TemplateDeleted event) {
        if (mMaterialList == MATERIAL_LIST.DOWNLOADED) {
            mAdapter.remove(event.getPosition());
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onLikeTemplateEvent(MaterialListEventBus.LikeTemplate event) {
        TemplateSet templateSet = event.getTemplateSet();
        int index = mData.indexOf(templateSet);
        if (mMaterialList == MATERIAL_LIST.LIKE) {
            if (index != -1) {
                mData.remove(index);
            }
            if (event.isLike()) {
                mData.add(0, templateSet);
            }
            mAdapter.replaceData(mData);
        } else if (index != -1) {
            mAdapter.notifyItemChanged(index);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_material_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadData(false);
    }

    private void initView() {
        contentView.setOnRefreshListener(this);
        initAdapter();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));
        if (mMaterialList == MATERIAL_LIST.LAYOUT) {
            mRecyclerView.setAdapter(mLayoutAdapter);
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator())
                .setSupportsChangeAnimations(false);
    }

    private void initAdapter() {
        if (mMaterialList == MATERIAL_LIST.LAYOUT) {
            mLayoutAdapter = new LayoutAdapter(mQuery.getPhotoNum());
            mLayoutAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    TemplateSet layout = new TemplateSet();
                    layout.setId("000");
                    layout.setUiRatio(PuzzlesUtils.getPic_size(MAP.get(position).second));
                    layout.setCategory(DataConstant.TEMPLATE_CATEGORY.LAYOUT);
                    layout.setMinPhotoNum(1);
                    layout.setMaxPhotoNum(9);
                    layout.setDownloaded(true);
                    EventBus.getDefault().post(new MaterialListEventBus
                            .SelectTemplate(position, layout));
                }
            });
        } else {
            mAdapter = new MaterialAdapter(mQuery.getPhotoNum(), mMaterialList);
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    EventBus.getDefault().post(new MaterialListEventBus
                            .SelectTemplate(position, mData.get(position)));
                }
            });
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    int id = view.getId();
                    switch (id) {
                        case R.id.material_item_apib_delete:
                            EventBus.getDefault().post(new MaterialListEventBus
                                    .DeleteTemplate(position, mData.get(position)));
                            break;
                        default:
                            break;
                    }
                }
            });
            FrameLayout footerView = new FrameLayout(getContext());
            footerView.addView(new Space(getContext()), new FrameLayout.LayoutParams(-1, SizeUtils.dp2px(16)));

            mAdapter.setFooterView(footerView);
        }

    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public MaterialListPresenter createPresenter() {
        return new SimpleMaterialListPresenter();
    }

    @Override
    public void setData(List<TemplateSet> data) {
        mData = data;
        if (mMaterialList == MATERIAL_LIST.LAYOUT) {
            mLayoutAdapter.replaceData(MAP);
        } else {
            mAdapter.replaceData(data);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadMaterials(getContext(), pullToRefresh, mQuery);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    public MaterialAdapter getAdapter() {
        return mAdapter;
    }


    private static final ArrayList<Pair<String, Integer>> MAP = new ArrayList<>();

    static {
        MAP.add(0, new Pair<>("ic_layout_entrance_1_1", LayoutFragment.ASPECT._1_1));
        MAP.add(1, new Pair<>("ic_layout_entrance_9_16", LayoutFragment.ASPECT._9_16));
        MAP.add(2, new Pair<>("ic_layout_entrance_3_4", LayoutFragment.ASPECT._3_4));
        MAP.add(3, new Pair<>("ic_layout_entrance_16_9", LayoutFragment.ASPECT._16_9));
        MAP.add(4, new Pair<>("ic_layout_entrance_4_3", LayoutFragment.ASPECT._4_3));
        MAP.add(5, new Pair<>("ic_layout_entrance_3_2", LayoutFragment.ASPECT._3_2));
        MAP.add(6, new Pair<>("ic_layout_entrance_2_1", LayoutFragment.ASPECT._2_1));
        MAP.add(7, new Pair<>("ic_layout_entrance_2_3", LayoutFragment.ASPECT._2_3));
        MAP.add(8, new Pair<>("ic_layout_entrance_1_2", LayoutFragment.ASPECT._1_2));
    }

    public interface ASPECT {
        int _1_1 = 1;
        int _9_16 = 2;
        int _3_4 = 3;
        int _16_9 = 4;
        int _4_3 = 5;
        int _3_2 = 6;
        int _2_1 = 7;
        int _2_3 = 8;
        int _1_2 = 9;
    }

    public interface LAYOUT_ID{
        int _1_1 = 1;
        int _9_16 = 2;
        int _3_4 = 3;
        int _16_9 = 4;
        int _4_3 = 5;
        int _3_2 = 6;
        int _2_1 = 7;
        int _2_3 = 8;
        int _1_2 = 9;
    }

}
