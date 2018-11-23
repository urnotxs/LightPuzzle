package com.xs.lightpuzzle.materials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xs.lightpuzzle.LightPuzzleConstant;
import com.xs.lightpuzzle.Navigator;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.photopicker.PhotoPicker;
import com.xs.lightpuzzle.puzzle.PuzzleActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xs on 2018/11/2.
 */

public class MaterialListActivity extends BaseMaterialListActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "PUZZLE";

    public static final String EXTRA_STATE = "state";
    public static final String EXTRA_PHOTO_NUM = "photo_num";

    public interface STATE {

        int NORMAL = 0;
        int RESULT = 1;
    }

    @BindView(R.id.material_list_aty_dl)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.material_list_aty_nv)
    NavigationView mNavigationView;
    @BindView(R.id.material_list_aty_tb)
    Toolbar mToolbar; // 顶部工具栏，最终被滑动至不可见
    @BindView(R.id.material_list_aty_tl)
    TabLayout mTabLayout;
    @BindView(R.id.material_list_aty_vp)
    ViewPager mViewPager;
    @BindView(R.id.material_list_aty_fab)
    FloatingActionButton mButton;

    private int mState;
    private Integer mPhotoNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_list);
        ButterKnife.bind(this);
        // 首页的模板列表 或者 从拼图页点击切换模板跳转至模板列表
        init();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTemplateSelectedEvent(
            MaterialListEventBus.SelectTemplate event) {
        int position = event.getPosition();
        TemplateSet templateSet = event.getTemplateSet();

        if (templateSet.isDownloaded()) {
            Log.e(TAG, "PUZZLE");
            puzzle(position, templateSet);
        } else {
            Log.e(TAG, "DOWNLOAD");
            download(position, templateSet);
        }
    }

    private void puzzle(int position, TemplateSet templateSet) {
        postTemplateUsed(position, templateSet);

        final int category = templateSet.getCategory();
        final String id = templateSet.getId();

        switch (mState) {
            case STATE.RESULT:
                Intent data = new Intent();
                data.putExtra(PuzzleActivity.EXTRA_TEMPLATE_ID, id);
                data.putExtra(PuzzleActivity.EXTRA_TEMPLATE_CATEGORY, category);
                setResult(Activity.RESULT_OK, data);
                finish();
                break;
            case STATE.NORMAL:
            default:
                final int maxPhotoNum = templateSet.getMaxPhotoNum();
                // 跳至拼图
//                PhotoPicker.radio(this);
//                PhotoPicker.multi(this, 8, null);
                PhotoPicker.puzzle(this, id, category, PuzzleActivity.class, maxPhotoNum);
                break;
        }
    }

    private void init() {
        Intent intent = getIntent();
        mState = intent.getIntExtra(EXTRA_STATE, STATE.NORMAL);
        int photoNum = intent.getIntExtra(EXTRA_PHOTO_NUM, LightPuzzleConstant.INVALID_PHOTO_NUM);
        if (photoNum != LightPuzzleConstant.INVALID_PHOTO_NUM) {
            mPhotoNum = photoNum;
        }
    }

    private void initView() {
        setupToolbar();
        setupDrawerLayout();
        setupNavigationView();
        setupTabAndViewPager();
    }

    private void setupTabAndViewPager() {

        MaterialPagerAdapter pagerAdapter = new MaterialPagerAdapter(getSupportFragmentManager());

        SparseArray<String> nameMapper = TemplateCategoryUiMapper.getNameMapper();
        for (int i = 0; i < nameMapper.size(); i++) {

            int category = TemplateCategoryUiMapper.getCategory(i);
            String categoryName = nameMapper.get(i);

            TabLayout.Tab tab = mTabLayout.newTab().setText(categoryName);
            mTabLayout.addTab(tab, i);

            TemplateSetQuery.Builder builder = new TemplateSetQuery.Builder();
            builder.setPhotoNum(mPhotoNum);
            int materialList;

            if (category == DataConstant.TEMPLATE_ADDITIONAL_CATEGORY.HISTORY) {
                builder.setFlag(TemplateSet.FLAG.HISTORY);
                materialList = MATERIAL_LIST.HISTORY;
            } else if (category == DataConstant.TEMPLATE_ADDITIONAL_CATEGORY.LIKE) {
                builder.setFlag(TemplateSet.FLAG.LIKE);
                materialList = MATERIAL_LIST.LIKE;
            } else {
                builder.setCategory(category);
                materialList = MATERIAL_LIST.NOT_FLAG;
            }
            pagerAdapter.addFragment(
                    MaterialListFragment.newInstance(builder.build(),
                            materialList), categoryName);
        }

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(
                TemplateCategoryUiMapper.getPosition(
                        DataConstant.TEMPLATE_CATEGORY.SIMPLE));
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setupDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_material_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.material_list_aty_menu_downloaded) {
            Navigator.navigateToDownloadedListActivity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.material_list_aty_nav_menu_home) {

        } else if (id == R.id.material_list_aty_nav_menu_favorite) {

        } else if (id == R.id.material_list_aty_nav_menu_history) {

        } else if (id == R.id.material_list_aty_nav_menu_donate) {

        } else if (id == R.id.material_list_aty_nav_menu_setting) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    static class MaterialPagerAdapter extends FragmentPagerAdapter {

        private final List<MaterialListFragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitle = new ArrayList<>();

        public MaterialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(MaterialListFragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitle.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitle.get(position);
        }
    }
}
