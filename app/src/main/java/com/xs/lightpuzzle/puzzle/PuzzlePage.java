package com.xs.lightpuzzle.puzzle;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.layout.MvpFrameLayout;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzlePage extends MvpFrameLayout<PuzzleView, PuzzlePresenter>
        implements PuzzleView, View.OnClickListener {

    private Context mContext;

    private RelativeLayout mMainContainer;

    private RelativeLayout mTopBar;
    private ImageView mCancelBtn;
    private ImageView mSaveBtn;

    private PuzzleFrame mPuzzleFrame;// view的容器(里面承载绘图所有布局)

    private PuzzleBottomView mBottomView;

    public PuzzlePage(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        setBackgroundResource(R.color.colorPrimary);
        initContainer();
        initTopBar();
        initPuzzleFrame();
        initPuzzleBottomView();
    }

    private void initContainer() {
        mMainContainer = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mMainContainer, rParams);
    }

    private void initTopBar() {

//        RelativeLayout layout = new RelativeLayout(mContext);
//        layout.setBackgroundColor(Color.WHITE);
//        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(90));
//        mMainContainer.addView(layout, rParams);
        mTopBar = new RelativeLayout(mContext);
        mTopBar.setId(R.id.puzzle_page_top_bar);
        BitmapDrawable bmpDraw = new BitmapDrawable(BitmapFactory
                .decodeResource(getResources(), R.drawable.main_topbar_bg_fill));
        bmpDraw.setTileModeX(Shader.TileMode.REPEAT);
        mTopBar.setBackgroundDrawable(bmpDraw);
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(90));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mMainContainer.addView(mTopBar, rParams);
        {
            // 返回到上一层按钮
            mCancelBtn = new ImageView(mContext);
            mCancelBtn.setImageResource(R.drawable.puzzles_cancel_btn);
            rParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rParams.topMargin = Utils.getRealPixel3(3);
            mTopBar.addView(mCancelBtn, rParams);
            mCancelBtn.setOnClickListener(this);

            // 中间提示简拼名字
            TextView centerText = new TextView(mContext);
            centerText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            centerText.setTextColor(Color.WHITE);
            rParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rParams.leftMargin = Utils.getRealPixel3(103);
            centerText.setText(R.string.longpage_title);
            mTopBar.addView(centerText, rParams);

            mSaveBtn = new ImageView(mContext);
            mSaveBtn.setImageResource(R.drawable.puzzles_ok_btn);
            rParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mSaveBtn.setPadding(Utils.getRealPixel3(30), Utils.getRealPixel3(3),
                    0, Utils.getRealPixel3(3));
            mTopBar.addView(mSaveBtn, rParams);
            mSaveBtn.setOnClickListener(this);
        }
    }

    private void initPuzzleFrame() {
        mPuzzleFrame = new PuzzleFrame(mContext);
        RelativeLayout.LayoutParams rParams =
                new RelativeLayout.LayoutParams(Utils.getScreenW(),
                        ViewGroup.LayoutParams.MATCH_PARENT);
        rParams.addRule(RelativeLayout.BELOW, mTopBar.getId());
//        mPuzzleFrame.setBlankClickListener(mBlankClickListener);
//        mPuzzleFrame.setOnBtnBgVisListener(mOnBtnBgVisListener);
//        mPuzzleFrame.setTemplateChangeListener(mTemplateChangeClickListener);
        mMainContainer.addView(mPuzzleFrame, rParams);
    }

    private void initPuzzleBottomView() {
        mBottomView = new PuzzleBottomView(mContext);
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.bottomMargin = Utils.getRealPixel3(12);
        mMainContainer.addView(mBottomView, rParams);
    }


    @Override
    public PuzzlePresenter createPresenter() {
        return new PuzzlePresenter();
    }

    @Override
    public void invalidateView() {

    }

    @Override
    public void invalidateView(int width, int height, int templateSize) {

    }

    @Override
    public void invalidateView(int width, int height, int templateSize, int differ) {

    }

    @Override
    public void invalidateViewToScroll(int width, int height, int templateSize, int bottom) {

    }

    @Override
    public void onClick(View v) {

    }
}
