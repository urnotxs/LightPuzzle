package com.xs.lightpuzzle.puzzle.frame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.util.NoDoubleClickListener;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.ShapeUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.EffectiveImageButton;


/**
 * Created by xs on 2018/1/12.
 * 编辑工具条
 */

public class PuzzleToolBar extends RelativeLayout {

    private Context mContext;

    private RelativeLayout mBaseRLayout;

    private FrameLayout frNew;
    private RelativeLayout mFilterRLayout;
    private EffectiveImageButton mAddEffectBtn;
    private TextView mFilterTv;
    private ImageView imgNew;

    private LinearLayout mOtherLLayout;

    private RelativeLayout mClipRLayout;
    private EffectiveImageButton mClipBtn;
    private TextView mClipTv;

    private RelativeLayout mBeautyRLayout;
    private EffectiveImageButton mBeautyBtn;
    private TextView mBeautyTv;

    private RelativeLayout mReplaceRLayout;
    private EffectiveImageButton mReplacePicBtn;
    private TextView mReplaceTv;

    private RelativeLayout mRotateRLayout;
    private EffectiveImageButton mRotatePicBtn;
    private TextView mRotateTv;

    private RelativeLayout mSoundRLaoyout;
    private EffectiveImageButton mSoundBtn;
    private TextView mSoundTv;


    private EffectiveImageButton mOrderBtn;

    private RelativeLayout mZoomInRLayout;
    private EffectiveImageButton mZoomInBtn;
    private TextView mZoomInTv;

    private RelativeLayout mZoomOutRLayout;
    private EffectiveImageButton mZoomOutBtn;
    private TextView mZoomOutTv;

    private static final int VERTICAL_MARGIN = Utils.getRealPixel3(32);
    private static final int TOPBAR_HEIGHT = Utils.getRealPixel3(90);
    private static final int BOTTOMBAR_HEIGHT = Utils.getRealPixel3(111);
    private static final int MIN_MARGIN = Utils.getRealPixel3(138);
    private static final int TOOLBAR_LONG_WIDTH = Utils.getRealPixel3(548);
    private static final int TOOLBAR_SHORT_WIDTH = Utils.getRealPixel3(468);
    private static final int TOOLBAR_HEIGHT = Utils.getRealPixel3(112);
    //边框宽度范围估值，具体情况问肖同学
    private static final int BORDER_WIDTH = Utils.getRealPixel3(20);

    //当前为拼图模式or拼视频模式
    private int mPuzzleModel = PuzzleMode.MODE_WAG;
    //视频模式下，选中的为视频还是图片
    private boolean isVideoInVideoPuzzleModel = false;
    private boolean mIsOpen = false;

    private Rect mRect;
    private int mOffsetY;//长图滚动偏移量

    private int mHorizontalOffset = 0;
    private int mVerticalOffset = 0;
    private int mPuzzleViewW = 0;
    private int mPuzzleViewH = 0;
    private int mToolBarWidth = TOOLBAR_LONG_WIDTH;

    private Point[] mImagePoint;

    //是否为横屏比例布局模式
    private boolean isHorizontalRatio = false;
    //是否显示在下方
    private boolean showOnBottom = true;

    public PuzzleToolBar(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    /**
     * @param puzzleModel
     * @param videoCount  选择视频模板并只选择了图片时，此时使用短的UI（即videoCount = 0 的情况）
     */
    public void setModel(int puzzleModel, int videoCount) {
        mPuzzleModel = puzzleModel;
        switch (mPuzzleModel) {
            case PuzzleMode.MODE_WAG:
                mReplacePicBtn.setButtonImage(R.drawable.puzzle_control_change_image,
                        R.drawable.puzzle_control_change_image);
                mClipRLayout.setVisibility(GONE);
                mSoundRLaoyout.setVisibility(GONE);
                mRotatePicBtn.setVisibility(VISIBLE);
                break;
            case PuzzleMode.MODE_VIDEO:
                mRotatePicBtn.setVisibility(GONE);
                mSoundRLaoyout.setVisibility(GONE);
                mClipRLayout.setVisibility(VISIBLE);

                if (videoCount == 0) {
                    mClipRLayout.setVisibility(GONE);

                    mBaseRLayout.setBackgroundResource(R.drawable.puzzle_control_menu_bg_short);
                    ViewGroup.LayoutParams layoutParams = mBaseRLayout.getLayoutParams();
                    mToolBarWidth = TOOLBAR_SHORT_WIDTH;
                    layoutParams.width = mToolBarWidth;
                    mBaseRLayout.setLayoutParams(layoutParams);

                    layoutParams = mOtherLLayout.getLayoutParams();
                    layoutParams.width = Utils.getRealPixel3(297);
                    mOtherLLayout.setLayoutParams(layoutParams);
                } else if (videoCount > 0) {
                    mClipRLayout.setVisibility(VISIBLE);

                    ViewGroup.LayoutParams layoutParams = mBaseRLayout.getLayoutParams();
                    mToolBarWidth = TOOLBAR_LONG_WIDTH;
                    layoutParams.width = mToolBarWidth;
                    mBaseRLayout.setLayoutParams(layoutParams);
                    mBaseRLayout.setBackgroundResource(R.drawable.puzzle_control_menu_bg);

                    layoutParams = mOtherLLayout.getLayoutParams();
                    layoutParams.width = Utils.getRealPixel3(378);
                    mOtherLLayout.setLayoutParams(layoutParams);
                }

                break;
            case PuzzleMode.MODE_LAYOUT:
                mReplacePicBtn.setButtonImage(R.drawable.puzzle_control_change_image,
                        R.drawable.puzzle_control_change_image);
                mClipRLayout.setVisibility(GONE);
                mSoundRLaoyout.setVisibility(GONE);
                mRotatePicBtn.setVisibility(VISIBLE);
                break;
            default:
                mReplacePicBtn.setButtonImage(R.drawable.puzzle_control_change_image,
                        R.drawable.puzzle_control_change_image);
                mSoundRLaoyout.setVisibility(GONE);
                mClipRLayout.setVisibility(GONE);
                mRotatePicBtn.setVisibility(VISIBLE);
                break;
        }
    }

    public void setPuzzleViewWH(int puzzleViewW, int puzzleViewH) {
        mPuzzleViewW = puzzleViewW;
        mPuzzleViewH = puzzleViewH;

        float horizontalRatio1 = 16 / 9;
        float horizontalRatio2 = 4 / 3;
        float horizontalRatio3 = 3 / 2;
        float horizontalRatio4 = 2 / 1;
        float horizontalRatio5 = 1 / 1;

        if (mPuzzleViewH == 0) {
            return;
        }
        float puzzleRatio = mPuzzleViewW / mPuzzleViewH;

        if (puzzleRatio == horizontalRatio1 ||
                puzzleRatio == horizontalRatio2 ||
                puzzleRatio == horizontalRatio3 ||
                puzzleRatio == horizontalRatio4 ||
                puzzleRatio == horizontalRatio5) {
            isHorizontalRatio = true;
        } else {
            isHorizontalRatio = false;
        }

        if (mPuzzleModel == PuzzleMode.MODE_LAYOUT) {
            mVerticalOffset = (Utils.sScreenH - mPuzzleViewH - TOPBAR_HEIGHT - BOTTOMBAR_HEIGHT) / 2;
        } else {
            //1:1比例下计算出来的结果会有误差，因此换一种计算方法
            if (mPuzzleViewW == mPuzzleViewH) {
                mVerticalOffset = (Utils.getScreenH() - puzzleViewH - PuzzlesUtils.getTopBarHeight() -
                        PuzzlesUtils.getBottomViewHeight()) / 2;
            } else {
                mVerticalOffset = PuzzlesUtils.getViewTop();
            }

        }
        mHorizontalOffset = (Utils.sScreenW - mPuzzleViewW) / 2;
    }

    /**
     * 设置工具条显示位置，传入空时，隐藏工具条
     *
     * @param isVideoInVideoPuzzleModel 拼视频模式下，当前是否为视频
     * @param offsetY                   Y方向偏移量，长图超出屏幕情况下Scroll的偏移
     */
    public void setLocation(boolean isVideoInVideoPuzzleModel, Point[] points, int offsetY) {
        if (points == null) {
            hide(true);
            return;
        }
        mImagePoint = points;
        this.isVideoInVideoPuzzleModel = isVideoInVideoPuzzleModel;
        if (mPuzzleModel == PuzzleMode.MODE_VIDEO) {
            if (this.isVideoInVideoPuzzleModel) {
                mReplacePicBtn.setButtonImage(R.drawable.puzzle_video_bar_change_video,
                        R.drawable.puzzle_video_bar_change_video);
                mRotateRLayout.setVisibility(GONE);

            } else {
                mReplacePicBtn.setButtonImage(R.drawable.puzzle_control_change_image,
                        R.drawable.puzzle_control_change_image);
                mRotateRLayout.setVisibility(GONE);

            }
        }

        Rect rect = ShapeUtils.makeRect(points);

        //当处于最顶部时，出现在上方；若既是顶部也是底部，则优先出现在下方
        if (isHorizontalRatio) {
            if (rect.top < BORDER_WIDTH) {
                showOnBottom = false;
            }
            if (mPuzzleViewH - rect.bottom < BORDER_WIDTH) {
                showOnBottom = true;
            }
        }

        if (checkChangedRect(rect)) {
            //第二次点击，隐藏工具条
            hide(true);
            return;
        }
        if (mRect == null) {
            mRect = new Rect();
        }
        mRect.left = rect.left + mHorizontalOffset;
        mRect.top = rect.top + mVerticalOffset;
        mRect.right = rect.right + mHorizontalOffset;
        mRect.bottom = rect.bottom + mVerticalOffset;
        mOffsetY = offsetY;
        resetLocation();
    }

    /**
     * 外部直接设置原生开关
     *
     * @param isOpen
     */
    public void setSoundState(boolean isOpen) {
        this.mIsOpen = isOpen;
        if (isOpen) {
            mSoundBtn.setButtonImage(R.drawable.puzzle_video_bar_music_open_icon,
                    R.drawable.puzzle_video_bar_music_open_icon);
        } else {
            mSoundBtn.setButtonImage(R.drawable.puzzle_video_bar_music_close_icon,
                    R.drawable.puzzle_video_bar_music_close_icon);
        }
    }

    /**
     * 内部自己点击切换原生开关
     */
    private void changeSoundState() {
        this.mIsOpen = !this.mIsOpen;
        if (mIsOpen) {
            mSoundBtn.setButtonImage(R.drawable.puzzle_video_bar_music_open_icon,
                    R.drawable.puzzle_video_bar_music_open_icon);
        } else {
            mSoundBtn.setButtonImage(R.drawable.puzzle_video_bar_music_close_icon,
                    R.drawable.puzzle_video_bar_music_close_icon);
        }
    }

    private void initViews() {
        FrameLayout.LayoutParams fparams;
        LayoutParams rparams;

        this.setVisibility(GONE);

        mBaseRLayout = new RelativeLayout(mContext);
        rparams = new LayoutParams(mToolBarWidth, Utils.getRealPixel3(112));
        mBaseRLayout.setBackgroundResource(R.drawable.puzzle_control_menu_bg);
        this.addView(mBaseRLayout, rparams);
        {

            mFilterRLayout = new RelativeLayout(mContext);
            rparams = new LayoutParams(Utils.getRealPixel3(64), ViewGroup.LayoutParams.MATCH_PARENT);
            rparams.leftMargin = Utils.getRealPixel3(25);
            mFilterRLayout.setOnClickListener(mNoDoubleClickListener);
            mFilterRLayout.setOnTouchListener(mOnTouchListener);
            mBaseRLayout.addView(mFilterRLayout, rparams);
            //滤镜按钮
            {
                mAddEffectBtn = new EffectiveImageButton(mContext);
                rparams = new LayoutParams(Utils.getRealPixel3(64), Utils.getRealPixel3(64));
                rparams.topMargin = Utils.getRealPixel3(11);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mAddEffectBtn.setButtonImage(R.drawable.puzzle_control_chang_effect,
                        R.drawable.puzzle_control_chang_effect);
                mFilterRLayout.addView(mAddEffectBtn, rparams);

                mFilterTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(70);//上移了4px
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mFilterTv.setText(R.string.puzzletoolbar_filter);
                mFilterTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);

                mFilterTv.setTextColor(Color.WHITE);
                mFilterRLayout.addView(mFilterTv, rparams);

            }

            imgNew = new ImageView(mContext);
            rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rparams.leftMargin = Utils.getRealPixel3(71);
            imgNew.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imgNew.setImageResource(R.drawable.ic_template_category_red_dot);
            imgNew.setVisibility(View.VISIBLE);
            mBaseRLayout.addView(imgNew, rparams);

            mOtherLLayout = new LinearLayout(mContext);
            rparams = new LayoutParams(Utils.getRealPixel3(378), ViewGroup.LayoutParams.MATCH_PARENT);
            rparams.leftMargin = Utils.getRealPixel3(135);
            mOtherLLayout.setOrientation(LinearLayout.HORIZONTAL);
            mBaseRLayout.addView(mOtherLLayout, rparams);

            mClipRLayout = new RelativeLayout(mContext);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            llParams.rightMargin = Utils.getRealPixel3(27);
            mClipRLayout.setOnClickListener(mNoDoubleClickListener);
            mClipRLayout.setOnTouchListener(mOnTouchListener);
            mClipRLayout.setVisibility(GONE);
            mOtherLLayout.addView(mClipRLayout, llParams);
            // 剪辑按钮
            {
                mClipBtn = new EffectiveImageButton(mContext);
                rparams = new LayoutParams(Utils.getRealPixel3(54), Utils.getRealPixel3(54));
                rparams.topMargin = Utils.getRealPixel3(17);//上移2px
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mClipBtn.setButtonImage(R.drawable.puzzle_control_clip, R.drawable.puzzle_control_clip);
                mClipRLayout.addView(mClipBtn, rparams);

                mClipTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(68);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mClipTv.setText(R.string.puzzletoolbar_clip);
                mClipTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                mClipTv.setTextColor(Color.WHITE);
                mClipRLayout.addView(mClipTv, rparams);
            }

            mBeautyRLayout = new RelativeLayout(mContext);
            llParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            mBeautyRLayout.setOnClickListener(mNoDoubleClickListener);
            mBeautyRLayout.setOnTouchListener(mOnTouchListener);
            mOtherLLayout.addView(mBeautyRLayout, llParams);
            // 美颜按钮
            {
                mBeautyBtn = new EffectiveImageButton(mContext);
                rparams = new LayoutParams(Utils.getRealPixel3(54), Utils.getRealPixel3(54));
                rparams.topMargin = Utils.getRealPixel3(17);//上移2px
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mBeautyBtn.setButtonImage(R.drawable.puzzle_control_beauty,
                        R.drawable.puzzle_control_beauty);
                mBeautyRLayout.addView(mBeautyBtn, rparams);

                mBeautyTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(68);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mBeautyTv.setText(R.string.puzzletoolbar_beauty);
                mBeautyTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                mBeautyTv.setTextColor(Color.WHITE);
                mBeautyRLayout.addView(mBeautyTv, rparams);
            }

            mReplaceRLayout = new RelativeLayout(mContext);
            llParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            llParams.leftMargin = Utils.getRealPixel3(27);
            mReplaceRLayout.setOnClickListener(mNoDoubleClickListener);
            mReplaceRLayout.setOnTouchListener(mOnTouchListener);
            mOtherLLayout.addView(mReplaceRLayout, llParams);
            //替换按钮
            {
                mReplacePicBtn = new EffectiveImageButton(mContext);
                rparams = new LayoutParams(Utils.getRealPixel3(54), Utils.getRealPixel3(54));
                rparams.topMargin = Utils.getRealPixel3(17);//上移2px
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mReplacePicBtn.setButtonImage(R.drawable.puzzle_control_change_image,
                        R.drawable.puzzle_control_change_image);
                mReplaceRLayout.addView(mReplacePicBtn, rparams);

                mReplaceTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(68);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mReplaceTv.setText(R.string.puzzletoolbar_replace);
                mReplaceTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                mReplaceTv.setTextColor(Color.WHITE);
                mReplaceRLayout.addView(mReplaceTv, rparams);
            }

            mRotateRLayout = new RelativeLayout(mContext);
            llParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            llParams.leftMargin = Utils.getRealPixel3(27);
            mRotateRLayout.setOnClickListener(mOnClickListener);
            mRotateRLayout.setOnTouchListener(mOnTouchListener);
            mOtherLLayout.addView(mRotateRLayout, llParams);
            //旋转按钮
            {
                mRotatePicBtn = new EffectiveImageButton(mContext);
                rparams = new LayoutParams(Utils.getRealPixel3(54), Utils.getRealPixel3(54));
                rparams.topMargin = Utils.getRealPixel3(17);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mRotatePicBtn.setButtonImage(R.drawable.puzzle_control_scale_rotate,
                        R.drawable.puzzle_control_scale_rotate);
                mRotateRLayout.addView(mRotatePicBtn, rparams);

                mRotateTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(68);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mRotateTv.setText(R.string.puzzletoolbar_rotate);
                mRotateTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                mRotateTv.setTextColor(Color.WHITE);
                mRotateRLayout.addView(mRotateTv, rparams);
            }

            mSoundRLaoyout = new RelativeLayout(mContext);
            llParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            llParams.leftMargin = Utils.getRealPixel3(27);
            mSoundRLaoyout.setOnClickListener(mOnClickListener);
            mSoundRLaoyout.setOnTouchListener(mOnTouchListener);
            mSoundRLaoyout.setVisibility(GONE);
            mOtherLLayout.addView(mSoundRLaoyout, llParams);
            //音量按钮
            {
                mSoundBtn = new EffectiveImageButton(mContext);
                rparams = new LayoutParams(Utils.getRealPixel3(54), Utils.getRealPixel3(54));
                rparams.topMargin = Utils.getRealPixel3(17);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                setSoundState(mIsOpen);
                mSoundRLaoyout.addView(mSoundBtn, rparams);

                mSoundTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(68);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mSoundTv.setText(R.string.puzzletoolbar_sound);
                mSoundTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                mSoundTv.setTextColor(Color.WHITE);
                mSoundRLaoyout.addView(mSoundTv, rparams);
            }


            mZoomInRLayout = new RelativeLayout(mContext);
            llParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            llParams.leftMargin = Utils.getRealPixel3(27);
            mZoomInRLayout.setOnClickListener(mOnClickListener);
            mZoomInRLayout.setOnTouchListener(mOnTouchListener);
            mOtherLLayout.addView(mZoomInRLayout, llParams);
            //放大按钮
            {
                mZoomInBtn = new EffectiveImageButton(mContext);
                rparams = new LayoutParams(Utils.getRealPixel3(54), Utils.getRealPixel3(54));
                rparams.topMargin = Utils.getRealPixel3(17);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mZoomInBtn.setButtonImage(R.drawable.puzzle_control_scale_big,
                        R.drawable.puzzle_control_scale_big);
                mZoomInRLayout.addView(mZoomInBtn, rparams);

                mZoomInTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(68);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mZoomInTv.setText(R.string.puzzletoolbar_zoomin);
                mZoomInTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                mZoomInTv.setTextColor(Color.WHITE);
                mZoomInRLayout.addView(mZoomInTv, rparams);
            }


            mZoomOutRLayout = new RelativeLayout(mContext);
            llParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            llParams.leftMargin = Utils.getRealPixel3(27);
            mZoomOutRLayout.setOnClickListener(mOnClickListener);
            mZoomOutRLayout.setOnTouchListener(mOnTouchListener);
            mOtherLLayout.addView(mZoomOutRLayout, llParams);
            //缩小按钮
            {
                mZoomOutBtn = new EffectiveImageButton(getContext());
                rparams = new LayoutParams(Utils.getRealPixel3(54), Utils.getRealPixel3(54));
                rparams.topMargin = Utils.getRealPixel3(17);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mZoomOutBtn.setButtonImage(R.drawable.puzzle_control_scale_small,
                        R.drawable.puzzle_control_scale_small);
                mZoomOutRLayout.addView(mZoomOutBtn, rparams);

                mZoomOutTv = new TextView(mContext);
                rparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rparams.topMargin = Utils.getRealPixel3(68);
                rparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mZoomOutTv.setText(R.string.puzzletoolbar_zoomout);
                mZoomOutTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
                mZoomOutTv.setTextColor(Color.WHITE);
                mZoomOutRLayout.addView(mZoomOutTv, rparams);
            }

        }
    }

    /**
     * 检测是否是第二次点击
     *
     * @param rect
     * @return
     */
    private boolean checkChangedRect(Rect rect) {
        if (mRect != null && rect != null) {
            boolean leftSame = mRect.left == rect.left + mHorizontalOffset;
            boolean topSame = mRect.top == rect.top + mVerticalOffset;
            boolean rightSame = mRect.right == rect.right + mHorizontalOffset;
            boolean bottomSame = mRect.bottom == rect.bottom + mVerticalOffset;
            if (leftSame && topSame && rightSame && bottomSame) {
                mRect = null;
                return true;
            }
        }
        return false;
    }

    private void resetLocation() {

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) PuzzleToolBar.this.getLayoutParams();

        int toolbarW = mToolBarWidth;
        int toolbarH = TOOLBAR_HEIGHT;

        int toolbarX = (mRect.right - mRect.left) / 2 + mRect.left - toolbarW / 2;
        if (toolbarX < 0) {
            if (mRect.left < 0) {
                toolbarX = 0;
            } else {
                toolbarX = mRect.left;
            }
        }

        if (toolbarX + toolbarW > Utils.sScreenW) {
            toolbarX = Utils.sScreenW - toolbarW;
        }

        int toolbarY = mRect.bottom + VERTICAL_MARGIN;
        if (Utils.sScreenH - mRect.bottom - BOTTOMBAR_HEIGHT < MIN_MARGIN + TOPBAR_HEIGHT) {
            toolbarY = mRect.top - VERTICAL_MARGIN - toolbarH;
            if (toolbarY < 0) {
                toolbarY = mRect.bottom - VERTICAL_MARGIN - toolbarH;
            }
        }

        if (mOffsetY > 0 || mPuzzleModel == PuzzleMode.MODE_LAYOUT_JOIN || mPuzzleModel == PuzzleMode.MODE_LONG) {
            // 长图或者拼接才会有Y轴偏移量

            //长图模式要兼容顶部和底部
            if (toolbarY < mOffsetY) {
                toolbarY = mRect.bottom + VERTICAL_MARGIN;
            }

            toolbarY = (toolbarY - mOffsetY < 0) ? VERTICAL_MARGIN :
                    (toolbarY - mOffsetY > Utils.sScreenH - BOTTOMBAR_HEIGHT - TOPBAR_HEIGHT - toolbarH ? VERTICAL_MARGIN : toolbarY - mOffsetY);

        } else {
            // 普通拼图

            if (mPuzzleModel == PuzzleMode.MODE_LAYOUT) {    //布局模式下的横屏比例下，顶部的工具条，如果有空间，则放在上方

                if (isHorizontalRatio) {

                    if (!showOnBottom) {
                        if (mRect.top - VERTICAL_MARGIN > toolbarH) {
                            toolbarY = mRect.top - VERTICAL_MARGIN - toolbarH;
                        }
                    }
                }

            }

        }

        layoutParams.topMargin = toolbarY;
        layoutParams.leftMargin = toolbarX;

        PuzzleToolBar.this.setLayoutParams(layoutParams);
        this.setVisibility(VISIBLE);
    }

    private NoDoubleClickListener mNoDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {

        }
    };

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (view == mFilterRLayout) {
                    mFilterRLayout.setAlpha(0.4f);
                } else if (view == mReplaceRLayout) {
                    mReplaceRLayout.setAlpha(0.4f);
                } else if (view == mRotatePicBtn) {
                    mRotatePicBtn.setAlpha(0.4f);
                } else if (view == mSoundRLaoyout) {
                    mSoundRLaoyout.setAlpha(0.4f);
                } else if (view == mBeautyRLayout) {
                    mBeautyRLayout.setAlpha(0.4f);
                } else if (view == mZoomInRLayout) {
                    mZoomInRLayout.setAlpha(0.4f);
                } else if (view == mZoomOutRLayout) {
                    mZoomOutRLayout.setAlpha(0.4f);
                } else if (view == mClipRLayout) {
                    mClipRLayout.setAlpha(0.4f);
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (view == mFilterRLayout) {
                    mFilterRLayout.setAlpha(1.0f);
                } else if (view == mReplaceRLayout) {
                    mReplaceRLayout.setAlpha(1.0f);
                } else if (view == mRotatePicBtn) {
                    mRotatePicBtn.setAlpha(1.0f);
                } else if (view == mSoundRLaoyout) {
                    mSoundRLaoyout.setAlpha(1.0f);
                } else if (view == mBeautyRLayout) {
                    mBeautyRLayout.setAlpha(1.0f);
                } else if (view == mZoomInRLayout) {
                    mZoomInRLayout.setAlpha(1.0f);
                } else if (view == mZoomOutRLayout) {
                    mZoomOutRLayout.setAlpha(1.0f);
                } else if (view == mClipRLayout) {
                    mClipRLayout.setAlpha(1.0f);
                }
            }

            return false;
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void hide(boolean clearRectState) {
        this.setVisibility(GONE);
        if (clearRectState) {
            mRect = null;
        }
    }
}
