package com.xs.lightpuzzle.puzzle.view.textedit.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.puzzle.util.ShareData;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.textedit.model.FontItemInfo;

import static com.xs.lightpuzzle.puzzle.view.textedit.BottomEditTextView.IS_DEBUG;

/**
 * Created by urnotXS on 2018/4/11.
 */

public class FontItemView extends FrameLayout {

    private RelativeLayout.LayoutParams rlParams;
    private LinearLayout.LayoutParams llParams;
    private LayoutParams flParams;

    private ImageView mFontImageView;
    private ImageView mLoadingIcon;
    private TextView mDownLoadSize;
    private ImageView mDownLoadIcon;
    private ProgressBar mDownLoadBar;
    private TextView mDebugTextView;
    private LinearLayout mItemLayout;
    private ImageView mItemImageView;
    private RelativeLayout mItemTextLayout;
    private TextView mItemTextView;

    private FontItemInfo mItemInfo;

    public FontItemView(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        flParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                (int) (Utils.getScreenH() * 0.088));

        FrameLayout root = new FrameLayout(getContext());
        this.addView(root, flParams);
        {
            float ratio = 96 / 640.0f;
            flParams = new LayoutParams(
                    Utils.getScreenW(),
                    (int) (Utils.getScreenW()*ratio));

            flParams.gravity = Gravity.CENTER_VERTICAL;
            mFontImageView = new ImageView(getContext());
            //mFontImageView.setHierarchy(FrescoLoader.getHierarchy(mFontImageView));
            root.addView(mFontImageView, flParams);


            flParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            flParams.gravity = Gravity.CENTER_VERTICAL;
            flParams.leftMargin = (int) (Utils.getScreenW() * 0.15);
            mLoadingIcon = new ImageView(getContext());
            mLoadingIcon.setImageResource(R.drawable.typeface_loading);
            mLoadingIcon.setVisibility(View.GONE);
            addView(mLoadingIcon, flParams);

            flParams = new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    (int) (Utils.getScreenH() * 0.088));
            mItemLayout = new LinearLayout(getContext());
            mItemLayout.setOrientation(LinearLayout.HORIZONTAL);
            root.addView(mItemLayout, flParams);
            {
                //选中按钮
                llParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                llParams.leftMargin = (int) (Utils.getScreenW() * 0.026);
                llParams.rightMargin = (int) (Utils.getScreenW() * 0.026);
                llParams.gravity = Gravity.CENTER_VERTICAL;
                mItemImageView = new ImageView(getContext());
                mItemImageView.setVisibility(INVISIBLE);
                mItemImageView.setImageResource(R.drawable.text_yes);
                mItemLayout.addView(mItemImageView, llParams);

                llParams = new LinearLayout.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT);
                RelativeLayout contentLayout = new RelativeLayout(getContext());
                mItemLayout.addView(contentLayout, llParams);
                {

                    rlParams = new RelativeLayout.LayoutParams(
                            LayoutParams.FILL_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rlParams.leftMargin = (int) (Utils.getScreenW() * 0.020);
                    ImageView line = new ImageView(getContext());
                    line.setImageResource(R.drawable.write_line);
                    line.setScaleType(ImageView.ScaleType.FIT_XY);
                    line.setAlpha(50);
                    contentLayout.addView(line, rlParams);

                }
            }
        }

        flParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        flParams.rightMargin = (int) (Utils.getScreenW() * 0.043);
        flParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mDownLoadSize = new TextView(getContext());
        mDownLoadSize.setTextColor(0xff61e0c1);
        mDownLoadSize.setVisibility(GONE);
        root.addView(mDownLoadSize, flParams);

        flParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        flParams.rightMargin = (int) (Utils.getScreenW() * 0.15);
        mDownLoadIcon = new ImageView(getContext());
        mDownLoadIcon.setVisibility(GONE);
        root.addView(mDownLoadIcon, flParams);

        //下载 进度条
        flParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                ShareData.PxToDpi(5));
        flParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        flParams.rightMargin = (int) (Utils.getScreenW() * 0.043);
        //rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mDownLoadBar = new ProgressBar(
                getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        mDownLoadBar.setProgressDrawable(
                getResources().getDrawable(R.drawable.green_progressbar));
        mDownLoadBar.setMax(100);
        //初始化进度值
        mDownLoadBar.setVisibility(INVISIBLE);
        root.addView(mDownLoadBar, flParams);

        if (IS_DEBUG) {
            flParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            flParams.rightMargin = (int) (Utils.getScreenW() * 0.043);
            flParams.gravity = Gravity.CENTER;
            mDebugTextView = new TextView(getContext());
            mDebugTextView.setTextColor(Color.RED);
            root.addView(mDebugTextView, flParams);
        }
    }

    public void setItemInfo(final FontItemInfo itemInfo) {
        this.mItemInfo = itemInfo;

        //判断是否正在下载中
        if (itemInfo.getFontInfo() != null) {

            Font fontInfo = itemInfo.getFontInfo();
            if (!fontInfo.isDownloaded()) {

                if (IS_DEBUG) {
                    mDebugTextView.setText(fontInfo.getName());
                }

//                if (themeData.fontResDownload != null) {
//                    itemInfo.setBarShow(true);
//                    itemInfo.setDowning(true);
//                    itemInfo.setDownTextShow(false);
//                    themeData.fontResDownload.setCallback(new FontResDownload.Callback() {
//
//                        @Override
//                        public void success(String downloadPath) {
//                            mDownLoadSize.setVisibility(GONE);
//                            mDownLoadIcon.setVisibility(GONE);
//                            mDownLoadBar.setVisibility(GONE);
//                            itemInfo.setBarShow(false);
//                            itemInfo.setDowning(false);
//                            itemInfo.setNeedDownFont(false);
//                            itemInfo.setReadyDown(false);
//                            itemInfo.setDownTextShow(false);
//                            if (!TextUtils.isEmpty(downloadPath)) {
//                                itemInfo.setFont(downloadPath);
//                            }
//                            mFontImageView.setAlpha(255);
//                            //PLog.d("zero5", "下载成功2");
//                        }
//
//                        @Override
//                        public void start() {
//                            //PLog.d("zero5", "回调start2");
//                            itemInfo.setBarShow(true);
//                            itemInfo.setDowning(true);
//                            itemInfo.setNeedDownFont(false);
//                            itemInfo.setReadyDown(true);
//                            itemInfo.setDownTextShow(false);
//                        }
//
//                        @Override
//                        public void fail() {
//                            //Toast.makeText(getContext(), "下载失败", Toast.LENGTH_LONG).show();
//                            mDownLoadSize.setVisibility(VISIBLE);
//                            mDownLoadIcon.setVisibility(VISIBLE);
//                            mDownLoadBar.setVisibility(GONE);
//                            itemInfo.setNeedDownFont(true);
//                            itemInfo.setReadyDown(false);
//                            itemInfo.setDownTextShow(true);
//                            itemInfo.setBarShow(false);
//                            invalidate();
//
//                        }
//
//                        @Override
//                        public void downloading(int values) {
//                            final int value = values;
//                            mDownLoadBar.setProgress(value);
//                        }
//                    });
//                } else {
//                    itemInfo.setDowning(false);
//                }
            } else {
                if (IS_DEBUG) {
                    mDebugTextView.setText("");
                }
                itemInfo.setDowning(false);
            }
        }

        // 设置勾选
        if (itemInfo.isCheck()) {
            mItemImageView.setVisibility(View.VISIBLE);
        } else {
            mItemImageView.setVisibility(INVISIBLE);
        }

        if (itemInfo.isNeedDownFont()) {
            //准备下载
            if (itemInfo.isreadyDown()) {
//
//                if (itemInfo.getFontInfo().fontResDownload == null) {
//                    //点击下载
//                    itemInfo.getFontInfo().fontResDownload = new FontResDownload(itemInfo.getFontInfo(), new FontResDownload.Callback() {
//
//                        @Override
//                        public void success(String downloadPath) {
//                            mDownLoadSize.setVisibility(GONE);
//                            mDownLoadIcon.setVisibility(GONE);
//                            mDownLoadBar.setVisibility(View.GONE);
//                            itemInfo.setDowning(false);
//                            itemInfo.setNeedDownFont(false);
//                            itemInfo.setReadyDown(false);
//                            itemInfo.setDownTextShow(false);
//                            mFontImageView.setAlpha(255);
//                            itemInfo.setBarShow(false);
//                            if (!TextUtils.isEmpty(downloadPath)) {
//                                itemInfo.setFont(downloadPath);
//                            }
//                        }
//
//                        @Override
//                        public void start() {
//                            //bar.setVisibility(View.VISIBLE);
//                            itemInfo.setBarShow(true);
//                            itemInfo.setDowning(true);
//                            itemInfo.setNeedDownFont(false);
//                            itemInfo.setReadyDown(true);
//                            itemInfo.setDownTextShow(false);
//
//                        }
//
//                        @Override
//                        public void fail() {
//                            //Toast.makeText(getContext(), "下载失败", Toast.LENGTH_LONG).show();
//                            mDownLoadSize.setVisibility(VISIBLE);
//                            mDownLoadIcon.setVisibility(VISIBLE);
//                            itemInfo.setDownTextShow(true);
//                            mDownLoadBar.setVisibility(GONE);
//                            itemInfo.setBarShow(false);
//                            itemInfo.setNeedDownFont(true);
//                            itemInfo.setReadyDown(false);
//                            invalidate();
//                        }
//
//                        @Override
//                        public void downloading(int values) {
//                            //bar.setVisibility(VISIBLE);
//                            itemInfo.setBarShow(true);
//                            final int value = values;
//                            mDownLoadBar.setProgress(value);
//                        }
//                    });
//                }
            }
        }

        if (itemInfo.getFontInfo() != null) {
            if (itemInfo.getFontInfo().isDownloaded()) {
                itemInfo.setDownTextShow(false);
            }
        }

        //显示半透明Bmp
        if (itemInfo.getFontInfo() != null) {
            if (!itemInfo.getFontInfo().isDownloaded() || itemInfo.isDowning()) {
                mFontImageView.setAlpha(100);
            } else {
                mFontImageView.setAlpha(255);
                mDownLoadBar.setVisibility(GONE);
                //info.setBarShow(false);
            }
        } else {
            mFontImageView.setAlpha(255);
            mDownLoadBar.setVisibility(GONE);
        }
        //显示下载大小
        setDownLoadText(itemInfo.isDownTextShow(), itemInfo.getDownText());

        if (itemInfo.isBarShow()) {
            mDownLoadBar.setVisibility(VISIBLE);
        } else {
            mDownLoadBar.setVisibility(GONE);
        }
    }

    /**
     * 设置是否显示下载字体
     *
     * @param show
     */
    public void setDownLoadText(boolean show, String text) {
        if (show) {
            mDownLoadSize.setVisibility(VISIBLE);
            mDownLoadIcon.setVisibility(VISIBLE);
            mDownLoadSize.setText(text);
            mDownLoadIcon.setImageResource(R.drawable.downloaderfontyindao);
        } else {
            mDownLoadSize.setVisibility(GONE);
            mDownLoadIcon.setVisibility(GONE);
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
    public void setIconItemImage(final String thumb_120, final FontItemView itemView) {
        if (itemView == null || thumb_120 == null)
            return;
        itemView.mLoadingIcon.clearAnimation();
        itemView.mLoadingIcon.setAnimation(null);
//
//        Scheme temp = Scheme.HTTP;
//        if (!TextUtils.isEmpty(thumb_120) && !thumb_120.contains("http")) {
//            temp = Scheme.ASSET;
//        }
        Glide.with(getContext())
                .load(thumb_120)
                .into(itemView.mFontImageView);
//
//        FrescoLoader.loadImages(itemView.mFontImageView, getContext(), temp, thumb_120, null,
//                new BaseControllerListener() {
//            @Override
//            public void onFinalImageSet(String id, @Nullable Object imageInfo, @Nullable Animatable animatable) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (itemView.mFontImageView != null && itemView.mLoadingIcon != null) {
//
//                            itemView.mLoadingIcon.clearAnimation();
//                            itemView.mLoadingIcon.setAnimation(null);
//                            itemView.mLoadingIcon.setVisibility(View.GONE);
//                            itemView.mFontImageView.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String id, Throwable throwable) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (itemView.mFontImageView != null && itemView.mLoadingIcon != null && thumb_120.startsWith("http")) {
//                            itemView.mFontImageView.setVisibility(View.GONE);
//                            RotateAnimation ra = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            ra.setDuration(800);
//                            ra.setRepeatCount(-1);
//                            itemView.mLoadingIcon.setVisibility(View.VISIBLE);
//                            itemView.mLoadingIcon.startAnimation(ra);
//                        } else {
//                            itemView.mLoadingIcon.clearAnimation();
//                            itemView.mLoadingIcon.setVisibility(View.GONE);
//                            itemView.mFontImageView.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onSubmit(String id, Object callerContext) {
//                super.onSubmit(id, callerContext);
//            }
//        });
    }
}
