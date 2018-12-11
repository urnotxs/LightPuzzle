package com.xs.lightpuzzle.puzzle.layout.layoutpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.adapter.LayoutDataAdapter;
import com.xs.lightpuzzle.puzzle.adapter.PuzzleDataAdapter;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.TemplateData;
import com.xs.lightpuzzle.puzzle.info.low.PuzzlesLayoutInfo;
import com.xs.lightpuzzle.puzzle.layout.data.LayoutOrderBean;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutData;
import com.xs.lightpuzzle.puzzle.util.AnimUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.ElasticHorizontalScrollView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局相关
 */

public class BottomEditLayoutView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private RelativeLayout mPageLayout;
    private LinearLayout mTotalLayout;
    private RelativeLayout mTopLayout;
    private ElasticHorizontalScrollView mRatioChooseScrollView;
    private LinearLayout mRatioChooseView;
    private ElasticHorizontalScrollView mLayoutChooseScrollView;
    private LinearLayout mLayoutChooseView;
    private RelativeLayout mBackbtn;
    private ArrayList<ImageView> mRatioIconList;
    private ArrayList<ImageView> mRatioLineList;
    private ArrayList<ImageView> mLayoutIconList;
    private ArrayList<ImageView> mLayoutLineList;
    private int mCurrentRatio = 0;
    private int mCurrentLayout = 0;
    private int mSelectedRatio = 0;
    private int mSelectedLayout = 0;
    private int mLastRatio = 0;
    private float[] mRatioArr = new float[]{
            1f, 9.0f / 16, 3.0f / 4, 16.0f / 9, 4.0f / 3,
            3.0f / 2, 2.0f / 1, 2.0f / 3, 1.0f / 2};
    private String[] mPointStringArr = {"0,0,683,512",
            "683,0,683,512",
            "1366,0,682,512",
            "0,512,683,512",
            "0,1024,683,512",
            "683,512,1365,1024",
            "0,1536,683,512",
            "683,1536,683,512",
            "1366,1536,682,512"};
    private int mStandSize = 2048;

    private boolean isFirst;
    private ArrayList<Bitmap> mBitmaps;
    private RotationImg[] mRotationImg;
    private List<PointF[]> mImgPoints;
    private PuzzlesLayoutInfo mLayoutInfo;
    private ArrayList<PuzzlesLayoutInfo> mLayoutInfos;
    private HashMap<String, List<String>> mRatioMaps = new HashMap<>();
    private HashMap<Float, ArrayList<PuzzlesLayoutInfo>> mRatiaLayoutHashMap;

    private OnLayoutChangedListener mOnLayoutChangedListener;

    public void setOnLayoutChangedListener(OnLayoutChangedListener onLayoutChangedListener) {
        mOnLayoutChangedListener = onLayoutChangedListener;
    }

    public interface OnLayoutChangedListener {
        void onLayoutChanged(float ratio, int layout, LayoutData layoutData);

        void onViewChanged(float ratio, int layout, LayoutData layoutData);

        void onDismiss();
    }

    public void open(RotationImg[] rotationImgs, float ratio, int layout) {
        mRatiaLayoutHashMap = new HashMap<>();
        mRatioMaps = new HashMap<>();

        mRotationImg = rotationImgs;

        mSelectedLayout = layout;
        DecimalFormat df = new DecimalFormat("######0.00");
        for (int i = 0; i < mRatioArr.length; i++) {
            if (df.format(mRatioArr[i]).equals(df.format(ratio))) {
                mSelectedRatio = i;
            }
        }

        if (isFirst) {
            mCurrentLayout = mSelectedLayout;
            mCurrentRatio = mSelectedRatio;
            isFirst = false;
        }

        List<HashMap<String, List<String>>> array = new LayoutOrderBean().fromJson(mContext);
        if (array != null && array.get(rotationImgs.length - 1) != null) {
            mRatioMaps = array.get(rotationImgs.length - 1);
        }

        decodeBitmap();

        for (int j = 0; j < mRatioArr.length; j++) {
            ratio = mRatioArr[j];
            //当前比例的id排序
            List<String> idOrderArr = new ArrayList<>();
            if (mRatioMaps.containsKey(new LayoutOrderBean().getRatioString(ratio))) {
                idOrderArr = mRatioMaps.get(new LayoutOrderBean().getRatioString(ratio));
            }
            if (!mRatiaLayoutHashMap.containsKey(ratio)) {
                mRatiaLayoutHashMap.put(ratio, new ArrayList<PuzzlesLayoutInfo>());
            }
            int viewWidth, viewHeight;
            if (ratio > 1) {
                //宽长，最大宽280px
                viewWidth = Utils.getRealPixel3(280);
                viewHeight = (int) (viewWidth / ratio);
                if (viewHeight > Utils.getRealPixel3(184)) {
                    viewHeight = Utils.getRealPixel3(184);
                    viewWidth = (int) (viewHeight * ratio);
                }
            } else {
                //高长或者正方形，最大高184px
                viewHeight = Utils.getRealPixel3(184);
                viewWidth = (int) (viewHeight * ratio);
            }

            for (int i = 0; i < idOrderArr.size(); i++) {

                TemplateSet mTemplateSet = LayoutDataAdapter.get(idOrderArr.get(i), ratio);
                TemplateData templateData = PuzzleDataAdapter.getTemplateData(mTemplateSet, rotationImgs.length, PuzzleMode.MODE_LAYOUT);

                List<PointF[]> imgPoints = new ArrayList<>();
                for (int m = 0; m < templateData.getImgPointDatas().size(); m++) {
                    imgPoints.add(templateData.getImgPointDatas().get(m).getPicPointF());
                }

                PuzzlesLayoutInfo layoutInfo = new PuzzlesLayoutInfo();
                layoutInfo.setPicPathList(mRotationImg);
                layoutInfo.setRect(new Rect(0, 0, viewWidth, viewHeight));

                LayoutData layoutData = new LayoutData().generateLayoutData(mStandSize, imgPoints);
                layoutData.setOutsidePaddingRatio(0.2f);
                layoutData.setInsidePaddingRatio(0.5f);
                layoutInfo.init(layoutData);
                layoutInfo.addPieces(mContext, mBitmaps);
                mRatiaLayoutHashMap.get(ratio).add(layoutInfo);
            }
        }

        initData();
    }

    private void decodeBitmap() {
        mBitmaps = new ArrayList<>();
        for (int i = 0; i < mRotationImg.length; i++) {
            mBitmaps.add(JaneBitmapFactory.decodefile(mContext, mRotationImg[i].getPicPath(),
                    new ImageSize(200, 200)));
        }
    }

    public BottomEditLayoutView(Context context) {
        super(context);
        mContext = context;
        isFirst = true;
        initView(context);
    }

    private void initView(Context context) {

        ViewGroup.LayoutParams mParams;
        LinearLayout.LayoutParams lParams;
        LayoutParams rParams;

        mPageLayout = new RelativeLayout(context);
        mParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mPageLayout, mParams);
        {
            mPageLayout.setOnClickListener(this);
            mTotalLayout = new LinearLayout(context);
            //mTotalLayout.setBackgroundColor(0xff6f5c62);
            Rect rect = new Rect(0,0,Utils.getScreenW() , Utils.getScreenH());
            Rect dstRect = new Rect( 0 , Utils.getScreenH() - Utils.getRealPixel3(346) , Utils.getScreenW() , Utils.getScreenH());
            mTotalLayout.setBackgroundResource(R.drawable.bg_toolbar);

            rParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(346));
            rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mTotalLayout.setOrientation(LinearLayout.VERTICAL);
            mTotalLayout.setOnClickListener(null);

            mPageLayout.addView(mTotalLayout, rParams);
            {
                //顶部布局
                mTopLayout = new RelativeLayout(context);
                lParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lParams.topMargin = Utils.getRealPixel3(32);
                mTotalLayout.addView(mTopLayout, lParams);
                {
                    // 顶部比例选取
                    mRatioChooseScrollView = new ElasticHorizontalScrollView(getContext());
                    rParams = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rParams.rightMargin = Utils.getRealPixel3(144);
                    mRatioChooseScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);// 不显示滑到初始和滑到终点时不出现越界阴影
                    mRatioChooseScrollView.setHorizontalScrollBarEnabled(false);// 设置隐藏滑动条
                    mTopLayout.addView(mRatioChooseScrollView, rParams);
                    {
                        mRatioChooseView = new LinearLayout(getContext());
                        rParams = new LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        mRatioChooseView.setOrientation(LinearLayout.HORIZONTAL);
                        mRatioChooseScrollView.addView(mRatioChooseView, rParams);
                        mRatioChooseScrollView.onFinishAddView(mRatioChooseView);
                        {
                            initRatioChooseView();
                        }
                    }

                    mBackbtn = new RelativeLayout(context);
                    rParams = new LayoutParams(
                            Utils.getRealPixel3(154), Utils.getRealPixel3(44));
                    rParams.addRule(ALIGN_PARENT_RIGHT);
                    mBackbtn.setOnClickListener(this);
                    mTopLayout.addView(mBackbtn, rParams);
                    {
                        // 向下推出布局选择页面
                        RelativeLayout imgRelativeLayout = new RelativeLayout(context);
                        rParams = new LayoutParams(
                                Utils.getRealPixel3(144), Utils.getRealPixel3(44));
                        mBackbtn.addView(imgRelativeLayout, rParams);
                        {
                            ImageView colorBackIv = new ImageView(getContext());
                            rParams = new LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            colorBackIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            colorBackIv.setImageResource(R.drawable.icon_layout_exit);
                            rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            imgRelativeLayout.addView(colorBackIv, rParams);
                        }
                    }
                }

                //底部布局选取
                mLayoutChooseScrollView = new ElasticHorizontalScrollView(getContext());
                lParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

                mLayoutChooseScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);// 不显示滑到初始和滑到终点时不出现越界阴影
                mLayoutChooseScrollView.setHorizontalScrollBarEnabled(false);// 设置隐藏滑动条
                //lParams.topMargin = Utils.getRealPixel3(32);
                mTotalLayout.addView(mLayoutChooseScrollView, lParams);
                {
                    mLayoutChooseView = new LinearLayout(getContext());
                    FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);

                    mLayoutChooseScrollView.addView(mLayoutChooseView, fParams);
                    mLayoutChooseScrollView.onFinishAddView(mLayoutChooseView);
                    {
                        /*mLayoutInfos = mRatiaLayoutHashMap.get(mRatioArr[mCurrentRatio]);
                        initLayoutChooseView();*/
                        //initLayoutChooseView();
                    }
                }
            }
        }
    }

    private void initData() {
        //比例相关View Refresh
        mRatioIconList.get(mCurrentRatio).setImageResource(mDrawableHoverList[mCurrentRatio]);
        mRatioLineList.get(mCurrentRatio).setVisibility(View.GONE);
        mCurrentRatio = mSelectedRatio;
        mCurrentLayout = mSelectedLayout;
        mLastRatio = mCurrentRatio;
        mRatioIconList.get(mCurrentRatio).setImageResource(mDrawableList[mCurrentRatio]);
        mRatioLineList.get(mCurrentRatio).setVisibility(View.VISIBLE);
        mRatioChooseView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mRatioChooseScrollView.smoothScrollTo(
                                (int) mRatioChooseView.getChildAt(mSelectedRatio).getX()
                                        - Utils.getScreenW() / 2 + Utils.getRealPixel3(70), 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mRatioChooseView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                    }
                });

        //布局相关View Refresh
        initLayoutChooseView();
        mLayoutChooseView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mLayoutChooseScrollView.smoothScrollTo(
                                (int) mLayoutChooseView.getChildAt(mSelectedLayout).getX()
                                        - Utils.getScreenW() / 2 + Utils.getRealPixel3(70), 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mLayoutChooseView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                    }
                });
    }

    public void initRatioChooseView() {
        mRatioChooseView.removeAllViews();
        mRatioIconList = new ArrayList<>();
        mRatioLineList = new ArrayList<>();
        for (int i = 0; i < mDrawableList.length; i++) {
            LinearLayout ratioItemLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                lParams.leftMargin = Utils.getRealPixel3(34);
            }
            lParams.rightMargin = Utils.getRealPixel3(45);
            if (i == mDrawableList.length - 1) {
                lParams.rightMargin = Utils.getRealPixel3(30);
            }
            ratioItemLayout.setOrientation(LinearLayout.VERTICAL);
            ratioItemLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            mRatioChooseView.addView(ratioItemLayout, lParams);
            {
                ImageView ratioView = new ImageView(mContext);

                ratioView.setImageResource(mDrawableHoverList[i]);
                ratioView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                lParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lParams.bottomMargin = Utils.getRealPixel3(18);

                ratioItemLayout.addView(ratioView, lParams);
                ratioItemLayout.setId(i);
                ratioItemLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isDismiss){
                            mRatioIconList.get(mCurrentRatio).setImageResource(mDrawableHoverList[mCurrentRatio]);
                            mRatioLineList.get(mCurrentRatio).setVisibility(View.INVISIBLE);
                            mCurrentRatio = v.getId();
                            mRatioIconList.get(mCurrentRatio).setImageResource(mDrawableList[mCurrentRatio]);
                            mRatioLineList.get(mCurrentRatio).setVisibility(View.VISIBLE);
                            initLayoutChooseView();

                            mRatioChooseScrollView.smoothScrollTo(
                                    (int) mRatioChooseView.getChildAt(mCurrentRatio).getX()
                                            - Utils.getScreenW() / 2 + Utils.getRealPixel3(70), 0);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mSelectedRatio == mCurrentRatio) {
                                        if (mSelectedLayout < mLayoutChooseView.getChildCount()
                                                && mLayoutChooseView.getChildAt(mSelectedLayout) != null){
                                            mLayoutChooseScrollView.smoothScrollTo(
                                                    (int) mLayoutChooseView.getChildAt(mSelectedLayout).getX()
                                                            - Utils.getScreenW() / 2 + Utils.getRealPixel3(70), 0);
                                        }else{
                                            mLayoutChooseScrollView.smoothScrollTo(0, 0);
                                        }
                                    } else {
                                        mLayoutChooseScrollView.smoothScrollTo(0, 0);
                                    }
                                }
                            }, 100);
                        }
                    }
                });

                ImageView lineView = new ImageView(mContext);
                lineView.setImageResource(R.drawable.icon_layout_ratio_line);
                lParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ratioItemLayout.addView(lineView, lParams);

                lineView.setVisibility(View.INVISIBLE);

                mRatioIconList.add(ratioView);
                mRatioLineList.add(lineView);
            }
        }
    }

    private void initLayoutChooseView() {
        mLayoutChooseView.removeAllViews();
        mLayoutIconList = new ArrayList<>();
        mLayoutLineList = new ArrayList<>();
        if (mRatiaLayoutHashMap != null) {
            mLayoutInfos = mRatiaLayoutHashMap.get(mRatioArr[mCurrentRatio]);
            for (int i = 0; i < mLayoutInfos.size(); i++) {
                PuzzlesLayoutInfo info = mLayoutInfos.get(i);

                RelativeLayout layoutChooseView = new RelativeLayout(mContext);
                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams
                        (info.getRect().width(), info.getRect().height());
                lParams.gravity = Gravity.CENTER_VERTICAL;
                if (i == 0) {
                    lParams.leftMargin = Utils.getRealPixel3(36);
                }
                lParams.rightMargin = Utils.getRealPixel3(32);
                mLayoutChooseView.addView(layoutChooseView, lParams);
                {
                    ImageView layoutView = new ImageView(mContext);

                    LayoutParams rParams = new LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //rParams.setMargins(Utils.getRealPixel3(6),Utils.getRealPixel3(6),Utils.getRealPixel3(6),Utils.getRealPixel3(6));
                    layoutView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Bitmap bitmap = Bitmap.createBitmap(info.getRect().width(),
                            info.getRect().height(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawColor(Color.WHITE);
                    info.draw(canvas);
                    layoutView.setImageBitmap(bitmap);
                    layoutChooseView.addView(layoutView, rParams);

                    layoutView.setId(i);
                    layoutView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isDismiss){
                                if (mOnLayoutChangedListener != null) {
                                    boolean isUpdateView = false;
                                    if (mLastRatio != mCurrentRatio) {
                                        //切换了比例
                                        isUpdateView = true;
                                    } else {
                                        mLayoutLineList.get(mCurrentLayout).setVisibility(INVISIBLE);
                                    }
                                    mLastRatio = mCurrentRatio;
                                    mCurrentLayout = v.getId();
                                    mLayoutLineList.get(mCurrentLayout).setVisibility(VISIBLE);
                                    float ratio = mRatioArr[mCurrentRatio];
                                    mLayoutInfos = mRatiaLayoutHashMap.get(mRatioArr[mCurrentRatio]);
                                    mLayoutInfo = mLayoutInfos.get(mCurrentLayout);

                                    List<String> idOrderArr = mRatioMaps.get(new LayoutOrderBean().getRatioString(mRatioArr[mCurrentRatio]));
                                    //TemplatePreview templatePreview = mTemplatePreviewMaps.get(idOrderArr.get(mCurrentLayout));
                                    if (isUpdateView) {
                                        mOnLayoutChangedListener.onViewChanged(ratio, mCurrentLayout, mLayoutInfo.getLayoutData());
                                    } else {
                                        mOnLayoutChangedListener.onLayoutChanged(ratio, mCurrentLayout, mLayoutInfo.getLayoutData());
                                    }
                                    mSelectedLayout = mCurrentLayout;
                                    mSelectedRatio = mCurrentRatio;

                                    mLayoutChooseScrollView.smoothScrollTo(
                                            (int) mLayoutChooseView.getChildAt(mCurrentLayout).getX()
                                                    - Utils.getScreenW() / 2 + Utils.getRealPixel3(70), 0);
                                }
                            }
                        }
                        //}
                    });

                    ImageView lineView = new ImageView(mContext);
                    lineView.setImageResource(R.drawable.shape_puzzles_layout_scaleimg);
                    rParams = new LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutChooseView.addView(lineView, rParams);
                    if (mCurrentRatio == mSelectedRatio && i == mSelectedLayout) {
                        lineView.setVisibility(VISIBLE);
                    } else {
                        lineView.setVisibility(INVISIBLE);
                    }

                    mLayoutLineList.add(lineView);
                    mLayoutIconList.add(layoutView);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBackbtn) {
            onBack();
        } else if (view == mPageLayout) {
            onBack();
        }
    }

    public void onBack() {
        if ( !isDismiss ) {
            isDismiss = true;
            if (mOnLayoutChangedListener != null) {
                mOnLayoutChangedListener.onDismiss();
            }
            AnimUtils.setTransAnim(this, 0, 0, 0, 1, 500, new AnimUtils.AnimEndCallBack() {
                @Override
                public void endCallBack(Animation animation) {
                    BottomEditLayoutView.this.clearAnimation();
                    BottomEditLayoutView.this.setAnimation(null);
                    BottomEditLayoutView.this.setVisibility(GONE);

                }
            });
        }
    }

    public boolean isDismiss() {
        return isDismiss;
    }

    public void setDismiss(boolean dismiss) {
        isDismiss = dismiss;
    }

    private boolean isDismiss; // 当前View是否完成退出动画 // 完成退出动画才可以下一次弹出，做完了弹出动画才可以退出

    int[] mDrawableList = new int[]{
            R.drawable.icon_layout_ratio_onebyone,
            R.drawable.icon_layout_ratio_ninebysixteen,
            R.drawable.icon_layout_ratio_threebyfour,
            R.drawable.icon_layout_ratio_sixteenbynine,
            R.drawable.icon_layout_ratio_fourbythree,
            R.drawable.icon_layout_ratio_threebytwo,
            R.drawable.icon_layout_ratio_twobyone,
            R.drawable.icon_layout_ratio_twobythree,
            R.drawable.icon_layout_ratio_onebytwo
    };
    int[] mDrawableHoverList = new int[]{
            R.drawable.icon_layout_ratio_onebyone_hover,
            R.drawable.icon_layout_ratio_ninebysixteen_hover,
            R.drawable.icon_layout_ratio_threebyfour_hover,
            R.drawable.icon_layout_ratio_sixteenbynine_hover,
            R.drawable.icon_layout_ratio_fourbythree_hover,
            R.drawable.icon_layout_ratio_threebytwo_hover,
            R.drawable.icon_layout_ratio_twobyone_hover,
            R.drawable.icon_layout_ratio_twobythree_hover,
            R.drawable.icon_layout_ratio_onebytwo_hover
    };

}
