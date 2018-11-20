package com.xs.lightpuzzle.puzzle;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.AnimUtils;
import com.xs.lightpuzzle.puzzle.util.ColorUtil;
import com.xs.lightpuzzle.puzzle.util.NoDoubleClickListener;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.BtnLayout;

/**
 * Created by Lin on 2017/12/12.
 * 编辑页底部按钮
 */

public class PuzzleBottomView extends RelativeLayout {

    private Context mContext;

    private LinearLayout mBottomLayout, mBottomLeftLayout, mTextSinatureBack, mTextSinature;

    private BtnLayout mLayoutBtnLayout, mLineFrameBtnLayout, mTemplateBtnLayout,
            mBgColoreBtnLayout, mAdjustBtnLayout, mOrderBtnLayout, mAddBtnLayout, mAddTextBtnLayout,
            mAddSinatureBtnLayout, mAddLabelBtnLayout, mBackBtnLayout;

    private int mPuzzleMode;

    public PuzzleBottomView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public void setBtnBgVisible(boolean visible) {
        if (mLayoutBtnLayout != null) {
            mLayoutBtnLayout.setBackground(visible);
        }
        if (mLineFrameBtnLayout != null) {
            mLineFrameBtnLayout.setBackground(visible);
        }
        if (mTemplateBtnLayout != null) {
            mTemplateBtnLayout.setBackground(visible);
        }
        if (mBgColoreBtnLayout != null) {
            mBgColoreBtnLayout.setBackground(visible);
        }
        if (mAddBtnLayout != null) {
            mAddBtnLayout.setBackground(visible);
        }
        if (mAddTextBtnLayout != null) {
            mAddTextBtnLayout.setBackground(visible);
        }
        if (mAddSinatureBtnLayout != null) {
            mAddSinatureBtnLayout.setBackground(visible);
        }
        if (mAddLabelBtnLayout != null) {
            mAddLabelBtnLayout.setBackground(visible);
        }
        if (mBackBtnLayout != null) {
            mBackBtnLayout.setBackground(visible);
        }
    }

    /**
     * 切换模板的时候要重新显示, shit
     */

    public void setPuzzleMode(int puzzleMode) {
        this.mPuzzleMode = puzzleMode;
        LinearLayout.LayoutParams llParams = null;

        resetBottomView();

        llParams = (LinearLayout.LayoutParams) mBottomLeftLayout.getLayoutParams();
        if (puzzleMode == PuzzleMode.MODE_LAYOUT_JOIN) {
            llParams.width = Utils.getRealPixel3(520);
            mBottomLeftLayout.setLayoutParams(llParams);
            mLineFrameBtnLayout.setVisibility(VISIBLE);
            mTemplateBtnLayout.setVisibility(VISIBLE);
            mBgColoreBtnLayout.setVisibility(VISIBLE);
        } else if (puzzleMode == PuzzleMode.MODE_LAYOUT) {
            llParams.width = Utils.getRealPixel3(560);
            mBottomLeftLayout.setLayoutParams(llParams);
            mLayoutBtnLayout.setVisibility(VISIBLE);
            mLineFrameBtnLayout.setVisibility(VISIBLE);
            mTemplateBtnLayout.setVisibility(VISIBLE);
            mAdjustBtnLayout.setVisibility(GONE);
            mOrderBtnLayout.setVisibility(GONE);
        } else {
            mLayoutBtnLayout.setVisibility(GONE);
            mLineFrameBtnLayout.setVisibility(GONE);
            mTemplateBtnLayout.setVisibility(VISIBLE);
            mAdjustBtnLayout.setVisibility(GONE);
            mOrderBtnLayout.setVisibility(GONE);
            if (puzzleMode == PuzzleMode.MODE_LONG) {
                mTemplateBtnLayout.setVisibility(GONE);
                llParams.width = Utils.getRealPixel3(350);
                mBottomLeftLayout.setLayoutParams(llParams);
            } else {
                mTemplateBtnLayout.setVisibility(VISIBLE);
                llParams.width = Utils.getRealPixel3(466);
                mBottomLeftLayout.setLayoutParams(llParams);
            }
        }
        mBgColoreBtnLayout.setText(getResources().getString(R.string.longpage_background));
        mBgColoreBtnLayout.setImageDrawable(R.drawable.puzzles_color_btn);
        mAddBtnLayout.setText(getResources().getString(R.string.longpage_add));
        mAddBtnLayout.setImageDrawable(R.drawable.puzzle_edit_add_selector);
    }

    private void resetBottomView() {
        mBottomLeftLayout.removeAllViews();

        LinearLayout.LayoutParams llParams =
                new LinearLayout.LayoutParams(
                        Utils.getRealPixel3(62), LayoutParams.MATCH_PARENT, 1);

        mLayoutBtnLayout.setTextSize(mPuzzleMode);
        mLineFrameBtnLayout.setTextSize(mPuzzleMode);
        mTemplateBtnLayout.setTextSize(mPuzzleMode);
        mAdjustBtnLayout.setTextSize(mPuzzleMode);
        mOrderBtnLayout.setTextSize(mPuzzleMode);
        mBgColoreBtnLayout.setTextSize(mPuzzleMode);
        mAddBtnLayout.setTextSize(mPuzzleMode);
        mAddTextBtnLayout.setTextSize(mPuzzleMode);
        mAddSinatureBtnLayout.setTextSize(mPuzzleMode);
        mAddLabelBtnLayout.setTextSize(mPuzzleMode);

        if (mPuzzleMode == PuzzleMode.MODE_LAYOUT_JOIN) {
            mBottomLeftLayout.addView(mTemplateBtnLayout, llParams);
            mBottomLeftLayout.addView(mLineFrameBtnLayout, llParams);
            mBottomLeftLayout.addView(mBgColoreBtnLayout, llParams);
        } else {
            mBottomLeftLayout.addView(mLayoutBtnLayout, llParams);
            mBottomLeftLayout.addView(mLineFrameBtnLayout, llParams);
            mBottomLeftLayout.addView(mTemplateBtnLayout, llParams);
            mBottomLeftLayout.addView(mAdjustBtnLayout, llParams);
            mBottomLeftLayout.addView(mOrderBtnLayout, llParams);
            mBottomLeftLayout.addView(mBgColoreBtnLayout, llParams);
        }
    }

    private void init() {
        int btnWidth = Utils.getRealPixel3(62);

        LayoutParams rParams;
        rParams = new LayoutParams(LayoutParams.MATCH_PARENT, Utils.getRealPixel3(111));
        rParams.leftMargin = Utils.getRealPixel3(10);
        rParams.rightMargin = Utils.getRealPixel3(10);

        mBottomLayout = new LinearLayout(getContext());
        mBottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(mBottomLayout, rParams);
        {
            LinearLayout.LayoutParams llParams = null;
            llParams = new LinearLayout.LayoutParams(Utils.getRealPixel3(560), LayoutParams.MATCH_PARENT);
            mBottomLeftLayout = new LinearLayout(getContext());
            mBottomLeftLayout.setGravity(Gravity.CENTER);
            mBottomLeftLayout.setOrientation(LinearLayout.HORIZONTAL);
            mBottomLayout.addView(mBottomLeftLayout, llParams);
            {
                //布局按钮
                mLayoutBtnLayout = new BtnLayout(mContext);
                llParams = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
                llParams.weight = 1;
                mLayoutBtnLayout.setImageDrawable(R.drawable.puzzles_layout_btn_xml);
                mLayoutBtnLayout.setText(getResources().getString(R.string.longpage_layout));
                mLayoutBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                mLayoutBtnLayout.setOnClickListener(mOnClickListener);
                mBottomLeftLayout.addView(mLayoutBtnLayout, llParams);

                //线框按钮
                mLineFrameBtnLayout = new BtnLayout(mContext);
                llParams = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
                llParams.weight = 1;
                mLineFrameBtnLayout.setImageDrawable(R.drawable.puzzles_padding_btn_xml);
                mLineFrameBtnLayout.setText(getResources().getString(R.string.longpage_line_frame));
                mLineFrameBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                mLineFrameBtnLayout.setOnClickListener(mOnClickListener);
                mBottomLeftLayout.addView(mLineFrameBtnLayout, llParams);

                //模板按钮
                mTemplateBtnLayout = new BtnLayout(mContext);
                llParams = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
                llParams.weight = 1;
                mTemplateBtnLayout.setImageDrawable(R.drawable.puzzles_template_btn);
                mTemplateBtnLayout.setText(getResources().getString(R.string.longpage_template));
                mTemplateBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                mTemplateBtnLayout.setOnClickListener(mOnClickListener);
                mBottomLeftLayout.addView(mTemplateBtnLayout, llParams);

                // 调整按钮
                mAdjustBtnLayout = new BtnLayout(mContext);
                llParams = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
                llParams.weight = 1;
                mAdjustBtnLayout.setImageDrawable(R.drawable.puzzles_adjust_btn);
                mAdjustBtnLayout.setText(getResources().getString(R.string.longpage_adjust));
                mAdjustBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                mAdjustBtnLayout.setOnClickListener(mOnClickListener);
                mBottomLeftLayout.addView(mAdjustBtnLayout, llParams);

                // 顺序播放按钮
                mOrderBtnLayout = new BtnLayout(mContext);
                llParams = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
                llParams.weight = 1;
                mOrderBtnLayout.setImageDrawable(R.drawable.puzzles_order_btn);
                mOrderBtnLayout.setText(getResources().getString(R.string.clipvideopage_play_at_order_time));
                mOrderBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                mOrderBtnLayout.setOnClickListener(mOnClickListener);
                mOrderBtnLayout.setVisibility(GONE);
                mBottomLeftLayout.addView(mOrderBtnLayout, llParams);

                //背景按钮
                mBgColoreBtnLayout = new BtnLayout(getContext());
                llParams = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
                llParams.weight = 1;
                mBgColoreBtnLayout.setImageDrawable(R.drawable.puzzles_color_btn);
                mBgColoreBtnLayout.setText(getResources().getString(R.string.longpage_background));
                mBgColoreBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                mBgColoreBtnLayout.setOnClickListener(mOnClickListener);
                mBottomLeftLayout.addView(mBgColoreBtnLayout, llParams);
            }
            //添加按钮
            mAddBtnLayout = new BtnLayout(getContext());
            llParams = new LinearLayout.LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
            llParams.weight = 1;
            mAddBtnLayout.setImageDrawable(R.drawable.puzzle_edit_add_selector);
            mAddBtnLayout.setText(getResources().getString(R.string.longpage_add));
            mAddBtnLayout.setOnClickListener(mOnClickListener);
            mAddBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
            mBottomLayout.addView(mAddBtnLayout, llParams);
        }
        initTextSignLab();
    }

    private void initTextSignLab() {
        if (mTextSinatureBack == null) {
            int btnWidth = Utils.getRealPixel3(112);
            int side = (int) ((Utils.getScreenW() - btnWidth * 4) / (float) (55 + 48 + 48 + 48 + 55) * 55);
            int middle = (int) ((Utils.getScreenW() - btnWidth * 4) / (float) (55 + 48 + 48 + 48 + 55) * 48);
            LayoutParams rParams;
            rParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(111));
            mTextSinatureBack = new LinearLayout(getContext());
            mTextSinatureBack.setOrientation(LinearLayout.HORIZONTAL);
            this.addView(mTextSinatureBack, rParams);
            {
                LinearLayout.LayoutParams llParams = null;
                mTextSinature = new LinearLayout(getContext());
                llParams = new LinearLayout.LayoutParams(Utils.getRealPixel4(745), FrameLayout.LayoutParams.MATCH_PARENT);
                mTextSinature.setGravity(Gravity.CENTER);
                mTextSinature.setOrientation(LinearLayout.HORIZONTAL);
                mTextSinatureBack.addView(mTextSinature, llParams);
                {
                    //添加文本按钮
                    mAddTextBtnLayout = new BtnLayout(getContext());
                    llParams = new LinearLayout.LayoutParams(btnWidth, FrameLayout.LayoutParams.MATCH_PARENT);
                    llParams.leftMargin = side;
                    mAddTextBtnLayout.setImageDrawable(R.drawable.puzzle_edit_add_text_selector);
                    mAddTextBtnLayout.setText(getResources().getString(R.string.longpage_text));
                    mAddTextBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                    mAddTextBtnLayout.setOnClickListener(mOnClickListener);
                    mTextSinature.addView(mAddTextBtnLayout, llParams);

                    //添加签名按钮
                    mAddSinatureBtnLayout = new BtnLayout(getContext());
                    llParams = new LinearLayout.LayoutParams(btnWidth, FrameLayout.LayoutParams.MATCH_PARENT);
                    llParams.leftMargin = middle;
                    mAddSinatureBtnLayout.setImageDrawable(R.drawable.puzzle_edit_add_signature_selector);
                    mAddSinatureBtnLayout.setText(getResources().getString(R.string.longpage_sign));
                    mAddSinatureBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                    mAddSinatureBtnLayout.setOnClickListener(mOnClickListener);
                    mTextSinature.addView(mAddSinatureBtnLayout, llParams);

                    //标签按钮
                    mAddLabelBtnLayout = new BtnLayout(getContext());
                    llParams = new LinearLayout.LayoutParams(btnWidth, FrameLayout.LayoutParams.MATCH_PARENT);
                    llParams.leftMargin = middle;
                    mAddLabelBtnLayout.setImageDrawable(R.drawable.puzzle_edit_add_label_selector);
                    mAddLabelBtnLayout.setText(getResources().getString(R.string.longpage_label));
                    mAddLabelBtnLayout.setTextColor(ColorUtil.createColorStateList(0xffffffff, 0x66ffffff));
                    mAddLabelBtnLayout.setOnClickListener(mOnClickListener);
                    mTextSinature.addView(mAddLabelBtnLayout, llParams);


                }
                //返回>按钮
                mBackBtnLayout = new BtnLayout(getContext());
                llParams = new LinearLayout.LayoutParams(btnWidth, FrameLayout.LayoutParams.MATCH_PARENT);
                llParams.leftMargin = middle;
                llParams.rightMargin = side;
//                llParams.weight = 1;
                mBackBtnLayout.setImageDrawable(R.drawable.puzzle_edit_add_back_selector);
                mBackBtnLayout.setText(null);
                mBackBtnLayout.setOnClickListener(mOnClickListener);
                mTextSinatureBack.addView(mBackBtnLayout, llParams);
            }
            mTextSinatureBack.setVisibility(INVISIBLE);
        }
    }

    private OnClickListener mOnClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View view) {

        }
    };

    private void setTemlpateBgGone() {
        /*AnimUtils.setTransAnim(mBottomLeftLayout, 0, -1, 0, 0, 250, new AnimUtils.AnimEndCallBack() {
            @Override
            public void endCallBack(Animation animation) {
                mBottomLeftLayout.setVisibility(GONE);
            }
        });*/
        AnimUtils.setAlphaAnim(mAddBtnLayout, 1, 0, 150, new AnimUtils.AnimEndCallBack() {
            @Override
            public void endCallBack(Animation animation) {
                mAddBtnLayout.setVisibility(GONE);
                mAddBtnLayout.clearAnimation();
            }
        });


        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = AnimUtils.getTransAnim(0f, -1f, 0, 0, 150);
        AlphaAnimation alphaAnimation = AnimUtils.getAlphaAnim(1, 0, 150);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setTextSignLabelVisibilty();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomLeftLayout.setVisibility(GONE);
                mBottomLeftLayout.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBottomLeftLayout.startAnimation(animationSet);

    }

    private void setTemlpateBgVisibilty() {
        AnimUtils.setTransAnim(mBottomLeftLayout, -1, 0, 0, 0, 150, new AnimUtils.AnimEndCallBack() {
            @Override
            public void endCallBack(Animation animation) {
                mBottomLeftLayout.setVisibility(VISIBLE);
                mBottomLeftLayout.clearAnimation();
            }
        });
        mAddBtnLayout.setEnabled(false);
        AnimUtils.setAlphaAnim(mAddBtnLayout, 0, 1, 150, new AnimUtils.AnimEndCallBack() {
            @Override
            public void endCallBack(Animation animation) {
                mAddBtnLayout.setVisibility(VISIBLE);
                mAddBtnLayout.clearAnimation();
                mAddBtnLayout.setEnabled(true);
            }
        });
    }

    private void setTextSignLabelVisibilty() {
        mTextSinatureBack.setVisibility(VISIBLE);
        mBackBtnLayout.setEnabled(false);
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = AnimUtils.getTransAnim(0.15f, 0, 0, 0, 150);
        AlphaAnimation alphaAnimation = AnimUtils.getAlphaAnim(0, 1, 150);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mTextSinature.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTextSinature.startAnimation(animationSet);

        AnimUtils.setAlphaAnim(mBackBtnLayout, 0, 1, 150, new AnimUtils.AnimEndCallBack() {
            @Override
            public void endCallBack(Animation animation) {
                mBackBtnLayout.setVisibility(VISIBLE);
                mBackBtnLayout.clearAnimation();
                mBackBtnLayout.setEnabled(true);
            }
        });
    }

    private void setTextSignLabelGone() {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = AnimUtils.getTransAnim(0, 0.15f, 0, 0, 150);
        AlphaAnimation alphaAnimation = AnimUtils.getAlphaAnim(1, 0, 150);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setTemlpateBgVisibilty();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextSinatureBack.setVisibility(GONE);
//                setTemlpateBgVisibilty();
                mTextSinature.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTextSinature.startAnimation(animationSet);

        AnimUtils.setAlphaAnim(mBackBtnLayout, 1, 0, 150, new AnimUtils.AnimEndCallBack() {
            @Override
            public void endCallBack(Animation animation) {
                mBackBtnLayout.setVisibility(GONE);
                mBackBtnLayout.clearAnimation();
            }
        });
    }

    public void setAdjustOrderBtnVisibility(boolean isAdjust) {
        if (isAdjust) {
            mAdjustBtnLayout.setVisibility(VISIBLE);
            mOrderBtnLayout.setVisibility(GONE);
        } else {
            mAdjustBtnLayout.setVisibility(GONE);
            mOrderBtnLayout.setVisibility(VISIBLE);
        }
    }

    public void hideAdjustOrderBtn() {
        mAdjustBtnLayout.setVisibility(GONE);
        mOrderBtnLayout.setVisibility(GONE);
        LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) mBottomLeftLayout.getLayoutParams();
        llParams.width = Utils.getRealPixel3(466);
        mBottomLeftLayout.setLayoutParams(llParams);
    }

    /**
     * 播放模式，顺序播放/同时播放 按钮UI
     *
     * @param isSame
     */
    public void setPlayModel(boolean isSame) {
        if (isSame) {
            mOrderBtnLayout.setImageDrawable(R.drawable.puzzles_order_btn);
            mOrderBtnLayout.setText(getResources().getString(R.string.clipvideopage_play_at_order_time));
        } else {
            mOrderBtnLayout.setImageDrawable(R.drawable.puzzles_same_btn);
            mOrderBtnLayout.setText(getResources().getString(R.string.clipvideopage_play_at_same_time));
        }
    }

}
