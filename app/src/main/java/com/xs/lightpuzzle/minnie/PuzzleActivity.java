package com.xs.lightpuzzle.minnie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.entity.Template;
import com.xs.lightpuzzle.data.entity.TemplateMapper;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.minnie.widget.MultiRowRadioGroup;
import com.xs.lightpuzzle.minnie.widget.PlatterScrollView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xs on 2018/11/13.
 */

public class PuzzleActivity extends AppCompatActivity {

    public static final String EXTRA_TEMPLATE_CATEGORY = "template_category";
    public static final String EXTRA_TEMPLATE_ID = "template_id";
    public static final String EXTRA_PHOTOS = "photos";
    public static final String EXTRA_PHOTO_FILE_PATHS = "photo_file_paths";

    private int mPhotoNum;
    private Template mTemplate;
    private TemplateSet mTemplateSet;

    private String mTemplateId;
    private int mTemplateCategory;
    private String mAdditionalTemplateId;
    private int mAdditionalTemplateCategory;

    private ArrayList<String> mPhotoFilePaths = new ArrayList<>();
    private Map<Integer, ArrayList<String>> mPlatterPhotoFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        ButterKnife.bind(this);
        Glide.get(PuzzleActivity.this).clearMemory();
        init();
        initView();
    }

    private void initView() {
        initPlatterScroller();
        orderPizza();
    }

    // --- Template

    private final static int SCREEN_WIDTH = ScreenUtils.getScreenWidth();
    private final static int SCREEN_HEIGHT = ScreenUtils.getScreenHeight();

    private int mDestWidth;
    private Map<Integer, TemplateMapper> mPlatterTemplate;

    private void orderPizza() {
        int pizzaViewMargin;
        if (isScrollableTemplate()) {
            pizzaViewMargin = SizeUtils.dp2px(12);
        } else {
            pizzaViewMargin = SizeUtils.dp2px(42);
        }

        mDestWidth = SCREEN_WIDTH - pizzaViewMargin * 2;

        if (mPlatterTemplate == null) {
            mPlatterTemplate = new TreeMap<>();
        }
        mPlatterTemplate.put(1,
                TemplateMapper.get(mTemplateSet, mPhotoFilePaths));

        servingPizza();
    }

    private void servingPizza() {


    }

    private void initPlatterScroller() {
        if (isScrollableTemplate()) {
            mPlatterScrollView.setOnScrollChangedListener(new PlatterScrollView.OnScrollChangedListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    // TODO: 2018/11/13
                }
            });
        }

        if (isPlatterTemplate()) {
            mPlatterScrollView.setOnScrollStoppedListener(new PlatterScrollView.OnScrollStoppedListener() {
                @Override
                public void onScrollStopped() {
                    // TODO: 2018/11/13
                }
            });

            mPlatterScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_MOVE:
                            if (mReorderLinearLayout.getVisibility() == View.VISIBLE) {
                                mReorderLinearLayout.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            mPlatterScrollView.startScrollerTask();
                            break;
                        default:
                            break;
                    }
                    return false;

                }
            });
        }
    }

    private boolean isPlatterTemplate() {
        return mTemplateCategory == DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP;
    }

    private boolean isScrollableTemplate() {
        return isPlatterTemplate() || mTemplateCategory == DataConstant.TEMPLATE_CATEGORY.COLLAGE;
    }

/*    private boolean isScrollable() {
        return isPlatterTemplate() || mPizzaView.getHeight() > mPlatterContainer.getHeight();
    }*/

    private void init() {

        ArrayList<Photo> photos = getIntent()
                .getParcelableArrayListExtra(EXTRA_PHOTOS);
        if (photos != null && !photos.isEmpty()) {
            throw new NullPointerException("Photo list is null or empty");
        }
        for (Photo photo : photos) {
            mPhotoFilePaths.add(photo.getPath());
        }
        mPlatterPhotoFile = new TreeMap<>();
        mPlatterPhotoFile.put(1, mPhotoFilePaths);

        getTemplate(getIntent(), mPhotoFilePaths.size());
    }

    private void getTemplate(Intent intent, int photoNum) {
        String templateId = intent.getStringExtra(EXTRA_TEMPLATE_ID);
        int templateCategory = intent.getIntExtra(EXTRA_TEMPLATE_CATEGORY,
                DataConstant.TEMPLATE_CATEGORY.SIMPLE);

        loadTemplate(templateId, templateCategory, photoNum);
    }

    private void loadTemplate(String templateId, int templateCategory, int photoNum) {
        TemplateSet templateSet = TemplateManager.get(templateCategory, templateId);
        if (templateSet == null) {
            throw new RuntimeException("No corresponding transformTemplate was found");
        }

        mTemplateCategory = templateCategory;
        mAdditionalTemplateId = mTemplateId = templateId;
        mAdditionalTemplateCategory = mapAdditionalTemplateCategory(templateCategory);

        try {
            mPhotoNum = photoNum;
            mTemplateSet = templateSet;
            mTemplate = templateSet.getTemplateMap().get(photoNum);
        } catch (Exception e) {
            throw new RuntimeException("Photo number over limit");
        }
    }

    private int mapAdditionalTemplateCategory(int templateCategory) {
        int additional = templateCategory;
        if (templateCategory == DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP) {
            additional = DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_SUB;
        }
        return additional;
    }


    // root
    @BindView(R.id.puzzle_aty_rl_root)
    RelativeLayout mRootRelativeLayout;
    // platter container
    @BindView(R.id.puzzle_aty_fl_platter_container)
    FrameLayout mPlatterContainer;
    // platter scroller
    @BindView(R.id.puzzle_aty_psv)
    PlatterScrollView mPlatterScrollView;
    // pizza container
    @BindView(R.id.puzzle_aty_fl_pizza_container)
    FrameLayout mPizzaContainer;
    // reorder
    @BindView(R.id.puzzle_aty_recorder_btn_add)
    AppCompatImageButton mReorderAddImageBtn;
    @BindView(R.id.puzzle_aty_recorder_btn_remove)
    AppCompatImageButton mReorderRemoveImageBtn;
    @BindView(R.id.minnie_aty_ll_reorder)
    LinearLayout mReorderLinearLayout;
    @BindView(R.id.minnie_aty_reorder_apib_downward)
    AppCompatImageButton mReorderDownwardsImageBtn;
    @BindView(R.id.minnie_aty_reorder_apib_replace)
    AppCompatImageButton mReorderReplaceImageBtn;
    @BindView(R.id.minnie_aty_reorder_apib_upward)
    AppCompatImageButton mReorderUpwardImageBtn;
    // title
    @BindView(R.id.puzzle_aty_top_rl)
    RelativeLayout mTitleBarRelativeLayout;
    // bottom
    @BindView(R.id.minnie_aty_ll_bottom_root)
    LinearLayout mBottomRootLinearLayout;
    // bottom -> transformTemplate
    @BindView(R.id.minnie_aty_bottom_apib_template)
    AppCompatImageButton mTemplateImageBtn;
    // bottom -> background
    @BindView(R.id.minnie_aty_bottom_apib_background)
    AppCompatImageButton mBackgroundImageBtn;
    // background
    @BindView(R.id.minnie_aty_ll_background_root)
    LinearLayout mBackgroundRootLinearLayout;
    @BindView(R.id.minnie_aty_background_ll)
    LinearLayout mBackgroundLinearLayout;
    @BindView(R.id.minnie_aty_background_vp_color)
    ViewPager mBackgroundColorViewPager;
    @BindView(R.id.minnie_aty_background_piv_color)
    PageIndicatorView mBackgroundColorPageIndicatorView;
    @BindView(R.id.minnie_aty_background_mrrg_texture)
    MultiRowRadioGroup mBackgroundTextureRadioGroup;
    // photo
    @BindView(R.id.minnie_aty_ll_photo_root)
    LinearLayout mPhotoLinearLayout;
    // text
    @BindView(R.id.minnie_aty_ll_text_root)
    LinearLayout mTextRootLinearLayout;
    @BindView(R.id.minnie_aty_text_ll)
    LinearLayout mTextLinearLayout;
    @BindView(R.id.minnie_aty_text_et)
    AppCompatEditText mTextEditText;
    @BindView(R.id.minnie_aty_text_apib_done)
    AppCompatImageButton mTextDoneImageBtn;
    @BindView(R.id.minnie_aty_text_apib_menu)
    AppCompatImageButton mTextMenuImageBtn;
    @BindView(R.id.minnie_aty_text_ll_menu_root)
    LinearLayout mTextMenuRootLinearLayout;
    @BindView(R.id.minnie_aty_text_aptv_typeface)
    AppCompatTextView mTextTypefaceTextView;
    @BindView(R.id.minnie_aty_text_aptv_color)
    AppCompatTextView mTextColorTextView;
    @BindView(R.id.minnie_aty_text_aptv_size)
    AppCompatTextView mTextSizeTextView;
    @BindView(R.id.minnie_aty_text_fl_menu)
    FrameLayout mTextMenuFrameLayout;
    @BindView(R.id.minnie_aty_text_rl_typeface)
    RecyclerView mTextTypefaceRecyclerView;
    @BindView(R.id.minnie_aty_text_fl_color)
    FrameLayout mTextColorFrameLayout;
    @BindView(R.id.minnie_aty_text_vp_color)
    ViewPager mTextColorViewPager;
    @BindView(R.id.minnie_aty_text_sv_size)
    ScrollView mTextSizeScrollView;
    @BindView(R.id.minnie_aty_text_fl_size)
    LinearLayout mTextSizeLinearLayout;
    @BindView(R.id.minnie_aty_fl_ball_wave_container)
    FrameLayout mBallWaveContainer;
}
