package com.xs.lightpuzzle.photopicker.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.photopicker.PhotoPickerActivity;
import com.xs.lightpuzzle.photopicker.adapter.PhotoPagerAdapter;
import com.xs.lightpuzzle.photopicker.entity.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/12.
 */

public class PhotoPagerFragment extends BaseFragment implements View.OnClickListener {

    ViewPager mViewPager;
    ImageButton mAddImageBtn;
    CheckBox mAddCheckBox;
    /**
     * 通过一个按钮覆盖在CheckBox上, 从而分离 逻辑 和 视图, 遮罩按钮处理点击添加事件, CheckBox辅助视图展示
     */
    Button mMaskAddBtn;

    private PhotoPagerAdapter mPagerAdapter;

    private List<Photo> mPhotos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotos = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_picker_fragment_photo_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.photo_pager_frg_vp);
        mAddImageBtn = (ImageButton) view.findViewById(R.id.photo_pager_frg_ib_add);
        mAddCheckBox = (CheckBox) view.findViewById(R.id.photo_pager_frg_cb_add);
        mMaskAddBtn = (Button) view.findViewById(R.id.photo_pager_frg_btn_mask_add);

        mMaskAddBtn.setOnClickListener(this);
        mAddImageBtn.setOnClickListener(this);

        if (mPhotoPicker != null) {
            switch (mPhotoPicker.getScene()) {
                case PhotoPickerActivity.SCENE_1_RADIO:
                    mAddImageBtn.setVisibility(View.VISIBLE);
                    mAddCheckBox.setVisibility(View.GONE);
                    mMaskAddBtn.setVisibility(View.GONE);
                    mAddImageBtn.setImageResource(R.drawable.photo_pager_frg_ic_select_selector);
                    break;
                case PhotoPickerActivity.SCENE_1_MULTI:
                case PhotoPickerActivity.SCENE_2_PUZZLE:
                    mAddImageBtn.setVisibility(View.VISIBLE);
                    mAddCheckBox.setVisibility(View.GONE);
                    mMaskAddBtn.setVisibility(View.GONE);
                    mAddImageBtn.setImageResource(R.drawable.photo_pager_frg_ic_add_selector);
                    break;
                default:
                    break;
            }

            if (mAddCheckBox.getVisibility() == View.VISIBLE) {
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {

                    }

                    @Override
                    public void onPageSelected(int i) {
                        if (mPhotoPicker.getSelectedPhotos()
                                .contains(mPhotos.get(mViewPager.getCurrentItem()))) {
                            mAddCheckBox.setChecked(true);
                        } else {
                            mAddCheckBox.setChecked(false);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });
            }
        }
    }

    private void initData() {
        mPagerAdapter = new PhotoPagerAdapter(getContext(), mPhotos, Glide.with(this));
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.photo_pager_frg_ib_add) {
            if (mPhotoPicker != null) {
                int currItem = mViewPager.getCurrentItem();
                mPhotoPicker.selectPhoto(mPhotos.get(currItem));
            }
        } else if (id == R.id.photo_pager_frg_btn_mask_add) {
            if (mPhotoPicker != null) {
                int currItem = mViewPager.getCurrentItem();
                mPhotoPicker.selectPhoto(mPhotos.get(currItem));
            }
            mAddCheckBox.setChecked(!mAddCheckBox.isChecked());
        }
    }

    public void setPhotos(List<Photo> photos) {
        if (photos != null) {
            mPagerAdapter.updateData(photos);
        }
    }

    public void setCurrentPagerItem(int item) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(item, false);
        }
    }
}
