package com.xs.lightpuzzle.puzzle.view.label;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.PuzzleConstant;
import com.xs.lightpuzzle.puzzle.data.LabelData;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.label.view.EditLabelView;
import com.xs.lightpuzzle.puzzle.view.label.widget.ChoiceLayout;
import com.xs.lightpuzzle.puzzle.view.label.widget.IconInfo;

import java.util.ArrayList;
import java.util.HashMap;

import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_BITMAP;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_ICON_TYPE;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_IS_INVERT;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_LABEL_TYPE;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_TEXT;


/**
 * Created by urnotXS on 2018/4/11.
 */

public class LabelPage extends FrameLayout implements View.OnClickListener {

    private String saveUrl = PuzzleConstant.PUZZLE_SAVE_LABEL_IMG;
    private LinearLayout mPageLayout;
    private RelativeLayout mTopBarLayout;
    private ImageView mCancelBtn;
    private ImageView mSaveBtn;
    private TextView mCenterText;
    private LinearLayout mContentLayout;
    private EditLabelView mEditLabelView;
    private ChoiceLayout mTypeChoiceLayout;
    private ChoiceLayout mStyleChoiceLayout;

    private ArrayList<IconInfo> mTypeIconInfos;
    private ArrayList<IconInfo> mStyleIconInfos;
    private EditLabelView.LABEL_TYPE mLableType = EditLabelView.LABEL_TYPE.LABEL_TYPE_2;
    private EditLabelView.ICON_TYPE mIconType = EditLabelView.ICON_TYPE.CUSTOMIZE;
    private String mEditString = "早上好";
    private boolean isInvert;
    private LabelData mLabelData;

    private LabelActivity.PageListener mListener;
    private Context mContext;

    public LabelPage(Context context, Intent intent, LabelActivity.PageListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        init(intent);
        initView(context);
    }

    public void initView(Context context) {
        LinearLayout.LayoutParams llParams;
        RelativeLayout.LayoutParams rlParams;

        mPageLayout = new LinearLayout(getContext());
        llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPageLayout.setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.bg_toolbar);
        this.addView(mPageLayout, llParams);
        {
            //头部工具条（返回 + Jane  + 保存按钮）
            mTopBarLayout = new RelativeLayout(getContext());
            BitmapDrawable bmpDraw = new BitmapDrawable(BitmapFactory.
                    decodeResource(getResources(), R.drawable.main_topbar_bg_fill));
            bmpDraw.setTileModeX(Shader.TileMode.REPEAT);
            mTopBarLayout.setBackgroundDrawable(bmpDraw);
            llParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    Utils.getRealPixel3(90));

            llParams.gravity = Gravity.TOP | Gravity.LEFT;
            mPageLayout.addView(mTopBarLayout, llParams);
            {
                // 返回到上一层按钮
                mCancelBtn = new ImageView(getContext());
                mCancelBtn.setImageResource(R.drawable.puzzles_cancel_btn);
                rlParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                rlParams.leftMargin = Utils.getRealPixel3(0);
                rlParams.topMargin = Utils.getRealPixel3(3);
                mTopBarLayout.addView(mCancelBtn, rlParams);
                mCancelBtn.setOnClickListener(this);

                mCenterText = new TextView(getContext());
                mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
                mCenterText.setTextColor(Color.WHITE);
                rlParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                rlParams.leftMargin = Utils.getRealPixel3(103);
                mCenterText.setLayoutParams(rlParams);
                mCenterText.setText(R.string.editlabelpage_title);
                mTopBarLayout.addView(mCenterText);

                mSaveBtn = new ImageView(getContext());
                mSaveBtn.setImageResource(R.drawable.puzzle_label_ok_btn);
                rlParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mSaveBtn.setLayoutParams(rlParams);
                mSaveBtn.setPadding(Utils.getRealPixel3(30),
                        Utils.getRealPixel3(3),
                        Utils.getRealPixel3(5),
                        Utils.getRealPixel3(3));
                mTopBarLayout.addView(mSaveBtn);
                mSaveBtn.setOnClickListener(this);
            }

            //布局
            mContentLayout = new LinearLayout(getContext());
            llParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mContentLayout.setOrientation(LinearLayout.VERTICAL);
            mPageLayout.addView(mContentLayout, llParams);
            {
                //输入展示框
                mEditLabelView = new EditLabelView(getContext(), mLableType, mIconType);

                llParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.getRealPixel3(237));
                mContentLayout.addView(mEditLabelView, llParams);

                //选择标签类型
                mTypeChoiceLayout = new ChoiceLayout(getContext(), R.string.editlabelpage_choose_type);
                mContentLayout.addView(mTypeChoiceLayout);
                mTypeChoiceLayout.setOnClickListener(this);
                mTypeChoiceLayout.setOnChangedShowViewListener(new ChoiceLayout.OnChangedShowViewListener() {
                    @Override
                    public void onRefresh(int position) {
                        KeyboardUtils.hideSoftInput(mPageLayout);
                        mEditLabelView.changeLabelType(EditLabelView.ICON_TYPE.values()[position]);
                    }

                    @Override
                    public void onHideKeyboart() {
                        KeyboardUtils.hideSoftInput(mPageLayout);
                    }
                });

                //选择标签样式
                mStyleChoiceLayout = new ChoiceLayout(getContext(), R.string.editlabelpage_choose_style);
                llParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                llParams.topMargin = Utils.getRealPixel3(74);
                mContentLayout.addView(mStyleChoiceLayout, llParams);
                mStyleChoiceLayout.setOnClickListener(this);
                mStyleChoiceLayout.setOnChangedShowViewListener(new ChoiceLayout.OnChangedShowViewListener() {
                    @Override
                    public void onRefresh(int position) {
                        KeyboardUtils.hideSoftInput(mPageLayout);
                        mEditLabelView.changeLabelStyle(EditLabelView.LABEL_TYPE.values()[position]);
                    }

                    @Override
                    public void onHideKeyboart() {
                        KeyboardUtils.hideSoftInput(mPageLayout);
                    }
                });
            }
        }
        setData();
    }

    private void setData() {
        if (!TextUtils.isEmpty(mEditString)) {
            mEditLabelView.setEditText(mEditString);
        }

        mTypeIconInfos = new ArrayList<>();
        for (int i = 0; i < mTypeIcons.length; i++) {
            IconInfo iconInfon = new IconInfo();
            iconInfon.setIconDrawable(
                    newSelector(getContext(), mTypeIcons[i], mTypeIconsHover[i]));
            iconInfon.setIconText(mTypeIconTexts[i]);
            iconInfon.setShowText(true);
            if (i == mIconType.ordinal()) {
                iconInfon.setSelectedStatus(true);
            }
            mTypeIconInfos.add(iconInfon);
        }

        mStyleIconInfos = new ArrayList<>();
        for (int i = 0; i < mStyleIcons.length; i++) {
            IconInfo iconInfon = new IconInfo();
            iconInfon.setIconDrawable(
                    newSelector(getContext(), mStyleIcons[i], mStyleIconsHover[i]));
            iconInfon.setIconText(mStyleIconTexts[i]);
            iconInfon.setShowText(false);
            if (i == mLableType.ordinal()) {
                iconInfon.setSelectedStatus(true);
            }
            mStyleIconInfos.add(iconInfon);
        }

        mTypeChoiceLayout.setData(mTypeIconInfos);
        mStyleChoiceLayout.setData(mStyleIconInfos);
    }

    public void init(Intent intent) {

        int iconIndex = intent.getIntExtra(LABEL_ICON_TYPE, 0);
        EditLabelView.ICON_TYPE[] iconTypes = EditLabelView.ICON_TYPE.values();
        int labelIndex = intent.getIntExtra(LABEL_LABEL_TYPE, 0);
        EditLabelView.LABEL_TYPE[] labelTypes = EditLabelView.LABEL_TYPE.values();

        HashMap<String, Object> params = new HashMap<>();
        params.put(LABEL_BITMAP, intent.getStringExtra(LABEL_BITMAP));
        params.put(LABEL_ICON_TYPE, iconTypes[iconIndex]);
        params.put(LABEL_LABEL_TYPE, labelTypes[labelIndex]);
        params.put(LABEL_TEXT, intent.getStringExtra(LABEL_TEXT));
        params.put(LABEL_IS_INVERT, intent.getBooleanExtra(LABEL_IS_INVERT, false));

        if (params.get(LABEL_TEXT) != null) {
            mLabelData = new LabelData();
            mLabelData.setLabelPic((String) params.get(LABEL_BITMAP));
            mLabelData.setIconType((EditLabelView.ICON_TYPE) params.get(LABEL_ICON_TYPE));
            mLabelData.setLabelType((EditLabelView.LABEL_TYPE) params.get(LABEL_LABEL_TYPE));
            mLabelData.setText((String) params.get(LABEL_TEXT));
            mLabelData.setInvert((Boolean) params.get(LABEL_IS_INVERT));

            mLableType = mLabelData.getLabelType();
            mIconType = mLabelData.getIconType();
            mEditString = mLabelData.getText();
            isInvert = mLabelData.isInvert();
        } else {
            mLableType = EditLabelView.LABEL_TYPE.LABEL_TYPE_1;
            mIconType = EditLabelView.ICON_TYPE.BRAND;
            mEditString = null;
            isInvert = false;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mCancelBtn) {
            KeyboardUtils.hideSoftInput(this);
            if (mListener != null) {
                // TODO: 2018/11/23
                mListener.onClickBackBtn();
            }
        } else if (view == mSaveBtn) {
            String editLabelText = mEditLabelView.getEditLabelText();
            if (TextUtils.isEmpty(editLabelText)) {
                ToastUtils.showShort(R.string.editlabelpage_content_empty);
                return;
            }
            KeyboardUtils.hideSoftInput(this);
            EditLabelView.ICON_TYPE icon_type = mEditLabelView.getIcon_type();
            EditLabelView.LABEL_TYPE label_type = mEditLabelView.getLabel_type();

            if (mListener != null) {
                // TODO: 2018/11/23
                mListener.onClickSaveBtn(icon_type.ordinal(),
                        label_type.ordinal(), editLabelText,
                        isInvert, mEditLabelView.isEdit_type());
            }
        } else if (view == mStyleChoiceLayout) {
            KeyboardUtils.hideSoftInput(this);
        } else if (view == mTypeChoiceLayout) {
            KeyboardUtils.hideSoftInput(this);
        }
    }

    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, context.getResources().getDrawable(idPressed));
        stateListDrawable.addState(new int[]{}, context.getResources().getDrawable(idNormal));
        return stateListDrawable;
    }

    public static int[] mTypeIcons = {
            R.drawable.puzzle_label_type_brand,
            R.drawable.puzzle_label_type_location,
            R.drawable.puzzle_label_type_character,
            R.drawable.puzzle_label_type_price,
            R.drawable.puzzle_label_type_weather,
            R.drawable.puzzle_label_type_customize
    };

    public static int[] mTypeIconsHover = {
            R.drawable.puzzle_label_type_brand_hover,
            R.drawable.puzzle_label_type_location_hover,
            R.drawable.puzzle_label_type_character_hover,
            R.drawable.puzzle_label_type_price_hover,
            R.drawable.puzzle_label_type_weather_hover,
            R.drawable.puzzle_label_type_customize_hover
    };

    public static int[] mStyleIcons = {
            R.drawable.puzzle_label_style_1,
            R.drawable.puzzle_label_style_2,
            R.drawable.puzzle_label_style_3,
            R.drawable.puzzle_label_style_4
    };
    public static int[] mStyleIconsHover = {
            R.drawable.puzzle_label_style_1_hover,
            R.drawable.puzzle_label_style_2_hover,
            R.drawable.puzzle_label_style_3_hover,
            R.drawable.puzzle_label_style_4_hover
    };

    public String[] mTypeIconTexts = {

            getResources().getString(R.string.editlabelpage_brand),
            getResources().getString(R.string.editlabelpage_location),
            getResources().getString(R.string.editlabelpage_people),
            getResources().getString(R.string.editlabelpage_price),
            getResources().getString(R.string.editlabelpage_weather),
            getResources().getString(R.string.editlabelpage_custom_setting)
    };

    public static String[] mStyleIconTexts = {
            "样式一",
            "样式二",
            "样式三",
            "样式四",
    };
}
