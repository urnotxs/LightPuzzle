package com.xs.lightpuzzle.puzzle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.frame.PuzzleToolBar;
import com.xs.lightpuzzle.puzzle.util.NoDoubleClickListener;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.CustomScrollView;
import com.xs.lightpuzzle.puzzle.view.EffectiveImageButton;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzleFrame extends FrameLayout {

    private Context mContext;
    private PuzzlePresenter mPuzzlePresenter;

    private CustomScrollView mScrollView;
    private LinearLayout mPuzzleFrame;
    private PuzzleDrawView mPuzzlesDrawView;
    private LinearLayout mOperateTemplateBar;
    private RelativeLayout mRemoveTemplate, mAddTemplate;
    private Button mRemoveTemplateBtn, mAddTemplateBtn;
    //长图替换子模板按钮
    private EffectiveImageButton mReplace, mUpwards, mDownwards;



    private int mBtnH = Utils.getRealPixel3(92);
    private int mBtnTopMargin = Utils.getRealPixel3(29);



    public PuzzleFrame(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public void setPuzzlePresenter(PuzzlePresenter puzzlePresenter) {
        mPuzzlePresenter = puzzlePresenter;
        mPuzzlesDrawView.setPuzzlePresenter(puzzlePresenter);
    }

    private void initView() {

        FrameLayout.LayoutParams fParams;
        LinearLayout.LayoutParams lParams;

        fParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        fParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mScrollView = new CustomScrollView(mContext);
        mScrollView.setScrollVelocityY(2.0f);
        mScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mScrollView.setFadingEdgeLength(0);
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.setOnBorderTouchListener(mOnBorderListener);
        mScrollView.setOnScrollListener(mOnScrollListener);
        mScrollView.setOnScrollStopListener(mOnScrollStopListener);
        this.addView(mScrollView, fParams);        //添加scrollView
        {
            mPuzzleFrame = new LinearLayout(mContext);
            mPuzzleFrame.setOnClickListener(mOnClickListener);
            mPuzzleFrame.setOrientation(LinearLayout.VERTICAL);
            fParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mScrollView.addView(mPuzzleFrame, fParams);
            {
                mPuzzlesDrawView = new PuzzleDrawView(mContext) {
                    @Override
                    public int getScrollYOffset() {
                        return PuzzleFrame.this.getScrollYOffset();
                    }
                };
                lParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mPuzzleFrame.addView(mPuzzlesDrawView, lParams);

                //删除模板(仅长图会出现, 先隐藏)
                //这里的MatchParent有疑问
                lParams = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        Utils.getRealPixel3(70));
                mRemoveTemplate = new RelativeLayout(mContext);
                mRemoveTemplate.setBackgroundDrawable(
                        getResources().getDrawable(R.drawable.template_add_remove_line));
                mRemoveTemplate.setVisibility(View.GONE);
                mPuzzleFrame.addView(mRemoveTemplate, lParams);
                {
                    RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
                            Utils.getRealPixel3(37), Utils.getRealPixel3(37));
                    rl.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mRemoveTemplateBtn = new Button(mContext);
                    mRemoveTemplateBtn.setBackgroundResource(
                            R.drawable.template_remove_selector);
                    mRemoveTemplate.addView(mRemoveTemplateBtn, rl);
                }
                mRemoveTemplate.setOnClickListener(mOnClickListener);
                mRemoveTemplateBtn.setOnClickListener(mOnClickListener);
                //添加模板(仅长图会出现, 先隐藏)
                lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getRealPixel3(70));
                mAddTemplate = new RelativeLayout(mContext);
                mAddTemplate.setBackgroundDrawable(
                        getResources().getDrawable(R.drawable.template_add_remove_line));
                mPuzzleFrame.addView(mAddTemplate, lParams);
                mAddTemplate.setVisibility(View.GONE);
                {
                    RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(Utils.getRealPixel3(37), Utils.getRealPixel3(37));
                    rl.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mAddTemplateBtn = new Button(mContext);
                    mAddTemplateBtn.setBackgroundResource(
                            R.drawable.template_add_selector);
                    mAddTemplate.addView(mAddTemplateBtn, rl);
                }
                mAddTemplate.setOnClickListener(mOnClickListener);
                mAddTemplateBtn.setOnClickListener(mOnClickListener);
            }
        }

        mOperateTemplateBar = new LinearLayout(getContext());
        mOperateTemplateBar.setOrientation(LinearLayout.VERTICAL);
        fParams = new FrameLayout.LayoutParams(mBtnH, LayoutParams.WRAP_CONTENT);
        fParams.gravity = Gravity.RIGHT;
        this.addView(mOperateTemplateBar, fParams);
        {
            lParams = new LinearLayout.LayoutParams(mBtnH, mBtnH);
            mReplace = new EffectiveImageButton(mContext);
            mReplace.setImageResource(R.drawable.template_replace_btn);
            mReplace.setOnClickListener(mOnClickListener);
            mOperateTemplateBar.addView(mReplace, lParams);

            lParams = new LinearLayout.LayoutParams(mBtnH, mBtnH);
            lParams.topMargin = mBtnTopMargin;
            mUpwards = new EffectiveImageButton(mContext);
            mUpwards.setImageResource(R.drawable.template_upwnwards_btn);
            mUpwards.setOnClickListener(mOnClickListener);
            mOperateTemplateBar.addView(mUpwards, lParams);

            lParams = new LinearLayout.LayoutParams(mBtnH, mBtnH);
            lParams.topMargin = mBtnTopMargin;
            mDownwards = new EffectiveImageButton(mContext);
            mDownwards.setImageResource(R.drawable.template_downwards_btn);
            mDownwards.setOnClickListener(mOnClickListener);
            mOperateTemplateBar.addView(mDownwards, lParams);
        }
        mOperateTemplateBar.setVisibility(INVISIBLE);

        initToolBar();
        initLabelBar();
    }
    private PuzzleToolBar mPuzzleToolBar;
    private void initToolBar() {
        // 弹出的浮动工具栏
        mPuzzleToolBar = new PuzzleToolBar(mContext);
        FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(mPuzzleToolBar, fParams);
    }
    private RelativeLayout mLabelBar;
    private EffectiveImageButton mLabelBarEdit;
    private EffectiveImageButton mLabelBarDel;
    private void initLabelBar() {
        FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLabelBar = new RelativeLayout(getContext());
        mLabelBar.setVisibility(View.INVISIBLE);
        mLabelBar.setClickable(true);
        this.addView(mLabelBar, fParams);
        {
            LinearLayout mLinear = new LinearLayout(getContext());
            RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mLinear.setOrientation(LinearLayout.HORIZONTAL);
            mLinear.setBackgroundResource(R.drawable.puzzle_label_edit_btn_bg);
            mRparams.addRule(RelativeLayout.CENTER_VERTICAL);
            mRparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mLabelBar.addView(mLinear, mRparams);
            {
                LinearLayout mBtnLinear = new LinearLayout(getContext());
                LinearLayout.LayoutParams mLparams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
                mBtnLinear.setGravity(Gravity.CENTER);
                mBtnLinear.setOrientation(LinearLayout.VERTICAL);
                mLinear.addView(mBtnLinear, mLparams);
                {
                    mLabelBarDel = new EffectiveImageButton(getContext());
                    mLabelBarDel.setButtonImage(R.drawable.puzzle_label_del,
                            R.drawable.puzzle_label_del_hover);
                    mLabelBarDel.setOnClickListener(mOnClickListener);
                    mBtnLinear.addView(mLabelBarDel);
                }

                mBtnLinear = new LinearLayout(getContext());
                mLparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT, 1);
                mBtnLinear.setGravity(Gravity.CENTER);
                mBtnLinear.setOrientation(LinearLayout.VERTICAL);
                mLinear.addView(mBtnLinear, mLparams);
                {
                    mLabelBarEdit = new EffectiveImageButton(getContext());
                    mLabelBarEdit.setButtonImage(R.drawable.puzzle_label_edit,
                            R.drawable.puzzle_label_edit_hover);
                    mLabelBarEdit.setOnClickListener(mOnClickListener);
                    mBtnLinear.addView(mLabelBarEdit);
                }
            }
        }
    }
    /**
     * 获取Y滚动偏移量
     *
     * @return int ScrollView The top edge of the displayed part of your view, in pixels
     */
    public int getScrollYOffset() {
        if (mScrollView != null) {
            return mScrollView.getScrollY();
        }
        return 0;
    }
    // TODO: 2018/11/15 合法
    private boolean isLegal() {
        if (mPuzzlePresenter == null) {
            return false;
        }
        return true;
    }
    private OnClickListener mOnClickListener = new NoDoubleClickListener() {

        @Override
        public void onNoDoubleClick(View v) {
            if (!isLegal()) {
                return;
            }


        }
    };

    private CustomScrollView.OnBorderListener mOnBorderListener = new CustomScrollView.OnBorderListener() {
        @Override
        public void onBottom() {

        }

        @Override
        public void onTop() {

        }

        @Override
        public void onMove() {
            if (mOperateTemplateBar != null) {
                mOperateTemplateBar.setVisibility(INVISIBLE);
            }
        }
    };

    private CustomScrollView.OnScrollStopListener mOnScrollStopListener = new CustomScrollView.OnScrollStopListener() {

        @Override
        public void onStop(int scrollY) {
//            updateBtnLayout(scrollY);
        }
    };

    private CustomScrollView.OnScrollListener mOnScrollListener = new CustomScrollView.OnScrollListener() {
        @Override
        public void onScroll(int scrollX, int scrollY, int oldl, int oldt) {
//            onClearSelected();
//            int puzzlesFrameHeight = mPuzzleFrame.getHeight();
//            int screenHeight = puzzlesFrameHeight - scrollY - PuzzlesUtils.getBottomViewHeight() - PuzzlesUtils.getViewTop();
//            if (screenHeight > ShareData.m_screenRealHeight - PuzzlesUtils.getTopBarHeight() - PuzzlesUtils.getBottomViewHeight()) {
//                if (mBtnBgVisListener != null) {
//                    mBtnBgVisListener.onBtnBgVis(true);
//                }
//            } else {
//                if (mBtnBgVisListener != null) {
//                    mBtnBgVisListener.onBtnBgVis(false);
//                }
//            }
        }
    };
}
