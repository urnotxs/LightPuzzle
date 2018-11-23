package com.xs.lightpuzzle.puzzle.frame;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.PuzzlePresenter;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.msgevent.LabelBarMsgEvent;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzelsLabelBarMsgCode;
import com.xs.lightpuzzle.puzzle.util.NoDoubleClickListener;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.ShareData;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.CustomScrollView;
import com.xs.lightpuzzle.puzzle.view.EffectiveImageButton;

import org.greenrobot.eventbus.EventBus;

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

    private PuzzleToolBar mPuzzleToolBar;
    private RelativeLayout mLabelBar;
    private EffectiveImageButton mLabelBarEdit;
    private EffectiveImageButton mLabelBarDel;

    private int mBtnH = Utils.getRealPixel3(92);
    private int mBtnTopMargin = Utils.getRealPixel3(29);
    private int mPuzzleMode;


    public PuzzleFrame(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    /**
     * 初始设置PuzzlesInfo，PuzzlesInfo所需要的信息在使用该方法之前要先赋值
     *
     * @param puzzlesInfo
     */
    public void setPageData(PuzzlesInfo puzzlesInfo) {
        if (puzzlesInfo != null) {
            mPuzzleMode = puzzlesInfo.getPuzzleMode();
            if (mPuzzleToolBar != null) {
                mPuzzleToolBar.setModel(puzzlesInfo.getPuzzleMode(), 0);
            }
            if (mScrollView != null) {
                mScrollView.scrollTo(0, 0);
            }
            invalidateView(puzzlesInfo.getPuzzlesRect().width(),
                    puzzlesInfo.getPuzzlesRect().height(),
                    puzzlesInfo.getTemplateInfos().size());
        }
    }

    public void invalidateView() {
        if (mPuzzlesDrawView != null) {
            mPuzzlesDrawView.invalidateView();
        }
    }

    public void invalidateView(int width, int height, int templateSize) {
        onParentMeasure(width, height, templateSize);
        if (mPuzzlesDrawView != null) {
            mPuzzlesDrawView.invalidateView();
        }
    }

    public void invalidateView(int width, int height, int templateSize, int differ) {
        onParentMeasure(width, height, templateSize);
        if (mPuzzlesDrawView != null) {
            mPuzzlesDrawView.invalidateView();
        }
    }

    public void invalidateViewToScroll(int width, int height, int templateSize, int bottom) {
        onParentMeasure(width, height, templateSize);
        if (mPuzzlesDrawView != null) {
            mPuzzlesDrawView.invalidateView();
        }
        onParentScroll(bottom);
    }

    public void onParentScroll(final int scrolly) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScrollView != null) {
                    mScrollView.smoothScrollTo(0, scrolly + mLongTemplateEditBtnHeight);
                }
            }
        }, 100);
    }

    public void onParentMeasure(int width, int height, int templateSize) {
        measureDrawView(width, height, templateSize);
        if (mPuzzleToolBar != null) {
            mPuzzleToolBar.setPuzzleViewWH(width, height);
        }
    }

    private int mPuzzlesViewHeight;
    private int mLongTemplateEditBtnHeight;

    private void measureDrawView(int drawViewWidth, int drawViewHeight, int templateSize) {

        if (drawViewWidth == -1 || drawViewHeight == -1) {
            return;
        }

        mPuzzlesViewHeight = drawViewHeight;
        if (mPuzzlesDrawView != null) {
            LinearLayout.LayoutParams lParams =
                    (LinearLayout.LayoutParams) mPuzzlesDrawView.getLayoutParams();
            if (lParams != null) {
                lParams.width = drawViewWidth;
                lParams.height = drawViewHeight;
                mPuzzlesDrawView.setLayoutParams(lParams);
            }
        }

        // TODO: 2018/11/14
        if (mPuzzleFrame != null) {
            FrameLayout.LayoutParams fParams =
                    (FrameLayout.LayoutParams) mPuzzleFrame.getLayoutParams();
            if (fParams != null) {
                //如果是长图的模式, 那么puzzleFrame的高度等于puzzleView的高度, 模板删除按钮, 模板添加按钮的总和
                if (mPuzzleMode == PuzzleMode.MODE_LONG) {
                    fParams.width = Utils.getScreenW();
                    int addTemplateHeight = (PuzzlesUtils.isOutOfMemory(
                            drawViewHeight, templateSize) ?
                            0 : PuzzlesUtils.getAddOrDelTempHeight());
                    if (addTemplateHeight > 0) {
                        mAddTemplate.setVisibility(VISIBLE);
                    } else {
                        mAddTemplate.setVisibility(GONE);
                    }
                    int delTemplateHeight = (templateSize > 1 ? PuzzlesUtils.getAddOrDelTempHeight() : 0);
                    if (delTemplateHeight > 0) {
                        mRemoveTemplate.setVisibility(VISIBLE);
                    } else {
                        mRemoveTemplate.setVisibility(GONE);
                    }
                    int leftRightPadding = (Utils.getScreenW() - drawViewWidth) / 2;
                    mLongTemplateEditBtnHeight = addTemplateHeight + delTemplateHeight;
                    fParams.height = drawViewHeight + mLongTemplateEditBtnHeight;
                    //在scrollview里面第一层的view的marginTop 和marginBottom是无效的
                    mPuzzleFrame.setPadding(leftRightPadding, PuzzlesUtils.getViewTop(), leftRightPadding,
                            PuzzlesUtils.getViewTop() + PuzzlesUtils.getBottomViewHeight());
                    mPuzzleFrame.setLayoutParams(fParams);
                } else {
                    int leftRightPadding = (Utils.getScreenW() - drawViewWidth) / 2;
                    int topBottomPadding = (ShareData.m_screenRealHeight
                            - drawViewHeight - PuzzlesUtils.getTopBarHeight()
                            - PuzzlesUtils.getBottomViewHeight()) / 2;
                    if (topBottomPadding < PuzzlesUtils.getViewTop()) {
                        topBottomPadding = PuzzlesUtils.getViewTop();
                    }
                    mAddTemplate.setVisibility(GONE);
                    mRemoveTemplate.setVisibility(GONE);
                    fParams.width = Utils.getScreenW();
                    mLongTemplateEditBtnHeight = 0;
                    fParams.height = drawViewHeight + topBottomPadding * 2;
                    mPuzzleFrame.setPadding(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding + PuzzlesUtils.getBottomViewHeight());
                    mPuzzleFrame.setLayoutParams(fParams);
                }
            }
            if (mPuzzleMode == PuzzleMode.MODE_JOIN
                    || mPuzzleMode == PuzzleMode.MODE_LAYOUT_JOIN
                    || mPuzzleMode == PuzzleMode.MODE_LONG) {
                if (mBtnBgVisListener != null) {
                    mBtnBgVisListener.onBtnBgVis(false);
                }
                if ((ShareData.m_screenRealHeight - PuzzlesUtils.getTopBarHeight()
                        - PuzzlesUtils.getBottomViewHeight() - PuzzlesUtils.getViewTop())
                        < (drawViewHeight - mScrollView.getScrollY())) {

                    if (mBtnBgVisListener != null) {
                        mBtnBgVisListener.onBtnBgVis(true);
                    }
                }
            } else {
                if (mBtnBgVisListener != null) {
                    mBtnBgVisListener.onBtnBgVis(false);
                }
                if (mOperateTemplateBar != null) {
                    mOperateTemplateBar.setVisibility(INVISIBLE);
                }
            }
        }
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

    private void initToolBar() {
        // 弹出的浮动工具栏
        mPuzzleToolBar = new PuzzleToolBar(mContext);
        FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(mPuzzleToolBar, fParams);
    }

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

    public void showLabelBar(Rect rect) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        if (rect != null) {
            mLabelBar.setVisibility(VISIBLE);
            params.leftMargin = mPuzzlesDrawView.getLeft() + rect.left + (rect.width() - mLabelBar.getWidth()) / 2;
            params.topMargin =
                    mPuzzlesDrawView.getTop() + rect.top - (int) (rect.height() * 0.98f) - mScrollView.getScrollY();
            if (params.leftMargin < 0) {
                params.leftMargin = 0;
            }
            if (params.leftMargin > Utils.getScreenW() - mLabelBar.getWidth()) {
                params.leftMargin = Utils.getScreenW() - mLabelBar.getWidth();
            }

            if (params.topMargin < mPuzzlesDrawView.getTop()) {
                params.topMargin = mPuzzlesDrawView.getTop() + rect.top + rect.height();
            }

            mLabelBar.setLayoutParams(params);
        }
    }

    public void disLabelBar() {
        if (mLabelBar.getVisibility() == VISIBLE) {
            mLabelBar.setVisibility(INVISIBLE);
        }
    }

    private OnClickListener mOnClickListener = new NoDoubleClickListener() {

        @Override
        public void onNoDoubleClick(View v) {
            if (!isLegal()) {
                return;
            }

            if (v == mLabelBarDel) {
                // 隐藏toolbar
                disLabelBar();
                // 删除labels的item
                EventBus.getDefault().post(new LabelBarMsgEvent(PuzzelsLabelBarMsgCode.LABELBAR_DEL));
                // 刷新界面
            } else if (v == mLabelBarEdit) {
                // 隐藏toolbar
                disLabelBar();
                // 打开编辑页，并传入标签的信息
                EventBus.getDefault().post(new LabelBarMsgEvent(PuzzelsLabelBarMsgCode.LABELBAR_EDIT));
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


    private OnBtnBgVisListener mBtnBgVisListener;

    public interface OnBtnBgVisListener {
        void onBtnBgVis(boolean visible);
    }

    public void setOnBtnBgVisListener(OnBtnBgVisListener onBtnBgVisListener) {
        mBtnBgVisListener = onBtnBgVisListener;
    }

    public void recycle() {
        if (mPuzzlesDrawView != null) {
            mPuzzlesDrawView.recycle();
        }
    }
}
