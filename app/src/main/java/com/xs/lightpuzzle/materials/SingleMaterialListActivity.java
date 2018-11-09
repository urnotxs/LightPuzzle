package com.xs.lightpuzzle.materials;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by xs on 2018/11/9.
 */

public abstract class SingleMaterialListActivity extends BaseMaterialListActivity {

    private MaterialListFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Secondary);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_material_list);
        ButterKnife.bind(this);
        init();
        initView(savedInstanceState);
    }

    private void init() {
    }

    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mFragment = new MaterialListFragment().newInstance(
                    provideQuery(), provideMaterialList());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.single_material_list_aty_fl_container, mFragment)
                    .commit();
        }
    }

    @Subscribe
    public void onSelectTemplateEvent(MaterialListEventBus.SelectTemplate event) {
        int position = event.getPosition();
        TemplateSet templateSet = event.getTemplateSet();
        if (!templateSet.isDownloaded()) {
            download(position, templateSet);
        } else {
            postTemplateUsed(position, templateSet);
            selectTemplate(event);
        }
    }

    protected abstract void selectTemplate(MaterialListEventBus.SelectTemplate selected);

    protected abstract TemplateSetQuery provideQuery();

    protected int provideMaterialList() {
        return MATERIAL_LIST.NOT_FLAG;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected MaterialAdapter getAdapter() {
        return mFragment.getAdapter();
    }
}
