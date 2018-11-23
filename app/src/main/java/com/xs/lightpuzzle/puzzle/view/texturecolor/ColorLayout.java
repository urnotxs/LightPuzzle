package com.xs.lightpuzzle.puzzle.view.texturecolor;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.texturecolor.widget.ColorImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/1/24.
 */

public class ColorLayout extends RelativeLayout {
    private static final int PAGE_BG_COLOR_SIZE = 17;
    private static final int PAGE_TEXT_COLOR_SIZE = 22;
    public static final int BG_COLOR = 0;
    public static final int TEXT_COLOR = 1;
    private int mType = TEXT_COLOR; // 是背景还是文字
    private int mIndex;
    private int mPagePosition;
    private int[] mColorValues;

    private ColorImageButton mLastColorIbtn;
    private ColorSelectCallback mColorSelectCallback;
    private ClickColorListener mColorClickCallback;

    private List<ColorPanel> mColorPanels = new ArrayList<>();
    private ViewPager mViewPager;
    private MyPagerAdapter mPagerApdater;

    private RadioGroup mRadioButtonGroup;

    public ColorLayout(Context context, int[] colorValues, int type) {
        super(context);
        mColorValues = colorValues;
        mType = type;
        initUI();
        initListener();
    }

    private void initUI() {
        LayoutParams lParams;

        int height = mType == BG_COLOR ? Utils.getRealPixel3(314) : Utils.getRealPixel3(372);
        lParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        lParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mViewPager = new ViewPager(getContext());
        this.addView(mViewPager, lParams);
        mViewPager.setId(R.id.edit_bg_texture_page_view_page);

        int size = mType == BG_COLOR ? PAGE_BG_COLOR_SIZE : PAGE_TEXT_COLOR_SIZE;
        int pageNum = mColorValues.length / size;

        for (int i = 0; i < pageNum; i++) {
            ColorPanel colorPanel = new ColorPanel(getContext(),
                    subArr(mColorValues, i * size, (i + 1) * size),
                    false);
            mColorPanels.add(colorPanel);
        }

        mPagerApdater = new MyPagerAdapter(mColorPanels);
        mViewPager.setAdapter(mPagerApdater);
        mPagerApdater.notifyDataSetChanged();

        lParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mRadioButtonGroup = new RadioGroup(getContext());
//        lParams.topMargin = Utils.getRealPixel3(16);
        this.addView(mRadioButtonGroup, lParams);
        mRadioButtonGroup.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < pageNum; i++) {
            RadioGroup.LayoutParams llParams = new RadioGroup.LayoutParams(
                    Utils.getRealPixel3(10), Utils.getRealPixel3(10));
            llParams.rightMargin = Utils.getRealPixel3(12);
            RadioButton rb = new RadioButton(getContext());

            rb.setBackgroundDrawable(getResources().getDrawable(R.drawable.sel_puzzle_color_texture_dot));
            rb.setButtonDrawable(android.R.color.transparent);
            mRadioButtonGroup.addView(rb, llParams);
        }

        ((RadioButton) (mRadioButtonGroup.getChildAt(0))).setChecked(true);
    }

    private void initListener() {
        // 选中回调
        mColorSelectCallback = new ColorSelectCallback() {
            @Override
            public void select(ColorImageButton colorBtn) {
                if (mLastColorIbtn != colorBtn) {
                    if (mLastColorIbtn != null) {
                        mLastColorIbtn.setCheckState(false);
                    }
                    mLastColorIbtn = colorBtn;

                    int size = mType == BG_COLOR ? PAGE_BG_COLOR_SIZE : PAGE_TEXT_COLOR_SIZE;
                    mIndex = mPagePosition * size + colorBtn.getIndex();
                    if (mColorClickCallback != null) {
                        mColorClickCallback.clickCallback(mIndex);
                    }
                }
            }
        };

        for (ColorPanel colorPanel : mColorPanels) {
            colorPanel.setColorSelectCallback(mColorSelectCallback);
        }

        mViewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPagePosition = position;
                ((RadioButton) (mRadioButtonGroup.getChildAt(mPagePosition))).setChecked(true);
            }
        });
    }

    /**
     * get subArr from src fromIndex to toIndex-1
     *
     * @param src
     * @param fromIndex
     * @param toIndex
     * @return
     */
    private int[] subArr(int[] src, int fromIndex, int toIndex) {
        if (src == null) {
            return null;
        }

        int[] sub = new int[toIndex - fromIndex];
        for (int i = fromIndex; i < toIndex; i++) {
            sub[i - fromIndex] = src[i];
        }

        return sub;
    }

    public void setIndex(int index) {
        if (index < 0) {
            return;
        }

        mIndex = index;
        int size = mType == BG_COLOR ? PAGE_BG_COLOR_SIZE : PAGE_TEXT_COLOR_SIZE;
        mPagePosition = mIndex / size;

        int select = index % size;

        mViewPager.setCurrentItem(mPagePosition);

        ColorPanel panel = mColorPanels.get(mPagePosition);
        panel.setSelectCheckState(select);
        if (mLastColorIbtn != null && mLastColorIbtn.getIndex() != select) {
            mLastColorIbtn.setCheckState(false);
        }
        mLastColorIbtn = panel.getSelectCheckState(select);
    }

    public void clearSelect() {
        if (mLastColorIbtn != null) {
            mLastColorIbtn.setCheckState(false);
        }
    }

    public void unLock() {
        for (ColorPanel panel : mColorPanels) {
            panel.setLockVisibility(View.INVISIBLE);
        }
    }

    public void setColorClickCallback(ClickColorListener clickCallback) {
        mColorClickCallback = clickCallback;
    }

    public interface ColorSelectCallback {
        void select(ColorImageButton colorBtn);
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<ColorPanel> mColorPanels;

        public MyPagerAdapter(List<ColorPanel> mColorPanels) {
            this.mColorPanels = mColorPanels;
        }

        @Override
        public int getCount() {
            return mColorPanels != null ? mColorPanels.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mColorPanels.get(position));
            return mColorPanels.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mColorPanels.get(position));
        }

    }

    private class ColorPanel extends RelativeLayout implements OnClickListener {
        private int[] mColorValues;
        private boolean mIsLock;
        private RelativeLayout mLockRl = null;
        private ImageView mLockIv;
        private List<ColorImageButton> mColorImageButtons = new ArrayList<>();

        public ColorPanel(Context context, int[] colorValues, boolean isLock) {
            super(context);
            mColorValues = colorValues;
            mIsLock = isLock;
            initUI();
        }

        private void initUI() {
            LayoutParams lParams;
            LinearLayout.LayoutParams llparams;

            lParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            LinearLayout linear = new LinearLayout(getContext());
            this.addView(linear, lParams);
            linear.setOrientation(LinearLayout.VERTICAL);
            {
                // 颜色第一行
                llparams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llparams.gravity = Gravity.CENTER_HORIZONTAL;
                LinearLayout listColor1 = new LinearLayout(getContext());
                linear.addView(listColor1, llparams);

                // 颜色第二行
                llparams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llparams.gravity = Gravity.CENTER_HORIZONTAL;
                LinearLayout listColor2 = new LinearLayout(getContext());
                linear.addView(listColor2, llparams);

                // 颜色第三行
                llparams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llparams.gravity = Gravity.CENTER_HORIZONTAL;
                LinearLayout listColor3 = new LinearLayout(getContext());
                linear.addView(listColor3, llparams);

                // 字体的四行
                LinearLayout listColor4 = null;
                if (mType == TEXT_COLOR) {
                    llparams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    llparams.gravity = Gravity.CENTER_HORIZONTAL;
                    listColor4 = new LinearLayout(getContext());
                    linear.addView(listColor4, llparams);
                }

                llparams = new LinearLayout.LayoutParams(Utils.getRealPixel3(80), Utils.getRealPixel3(80));
                llparams.setMargins(Utils.getRealPixel3(18),
                        Utils.getRealPixel3(4), Utils.getRealPixel3(18), Utils.getRealPixel3(4));

                for (int i = 0; i < mColorValues.length; i++) {
                    ColorImageButton colorBtn = new ColorImageButton(getContext(), mColorValues[i], i);
                    if (i >= 0 && i < 6) {
                        listColor1.addView(colorBtn, llparams);
                    } else if (i >= 6 && i < 11) {
                        listColor2.addView(colorBtn, llparams);
                    } else if (i >= 11 && i < 17) {
                        listColor3.addView(colorBtn, llparams);
                    } else if (i >= 17 && i < 22) {
                        listColor4.addView(colorBtn, llparams);
                    }

                    mColorImageButtons.add(colorBtn);
                }
            }

            lParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mLockRl = new RelativeLayout(getContext());
            this.addView(mLockRl, lParams);
            {
                mLockIv = new ImageView(getContext());
                lParams = new LayoutParams(Utils.getRealPixel3(172), Utils.getRealPixel3(172));
                lParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                mLockRl.addView(mLockIv, lParams);
                mLockIv.setImageResource(R.drawable.puzzle_lock_color);

            }
            mLockRl.setOnClickListener(this);

            if (!mIsLock) {
                mLockRl.setVisibility(INVISIBLE);
            }
        }

        public void setIndex(int index) {
            mColorImageButtons.get(index).setCheckState(true);
        }

        private ColorSelectCallback mColorSelectCallback;

        public void setColorSelectCallback(ColorSelectCallback callback) {
            mColorSelectCallback = callback;
            if (mColorSelectCallback != null) {
                for (ColorImageButton colorImageButton : mColorImageButtons) {
                    colorImageButton.setColorSelectCallback(mColorSelectCallback);
                }
            }
        }

        public void setLockVisibility(int visibility) {
            mLockRl.setVisibility(visibility);
            if (visibility == VISIBLE) {
                mIsLock = true;
            } else {
                mIsLock = false;
            }
        }

        public void setSelectCheckState(int index) {
            mColorImageButtons.get(index).setCheckState(true);
        }

        public ColorImageButton getSelectCheckState(int index) {
            return mColorImageButtons.get(index);
        }


        @Override
        public void onClick(View v) {
//            VipDialogSite.popup(ColorPanel.this.getContext(), VipDialog.DESC.BG_COLOR,
//                    JaneStyleBlur.getBlurBackground(getContext()), new JaneCallbackAdapter() {
//                        @Override
//                        public void onComplete(Object o, int code, Bundle data) {
//                            if (code == JaneCallbackCode.SUCCESS) {
//                                ColorLayout.this.unLock();
//                            }
//                        }
//                    });
        }
    }

    public interface ClickColorListener {
        void clickCallback(int index);
    }
}
