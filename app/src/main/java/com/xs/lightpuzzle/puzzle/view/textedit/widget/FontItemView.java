package com.xs.lightpuzzle.puzzle.view.textedit.widget;

import android.content.Context;
import android.graphics.Color;
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

        flParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        flParams.gravity = Gravity.CENTER_VERTICAL;
        flParams.leftMargin = (int) (Utils.getScreenW() * 0.15);
        mLoadingIcon = new ImageView(getContext());
        mLoadingIcon.setImageResource(R.drawable.typeface_loading);
        mLoadingIcon.setVisibility(View.GONE);
        addView(mLoadingIcon, flParams);
    }

    public void refreshDownloadedUI(){
        mDownLoadSize.setVisibility(GONE);
        mDownLoadIcon.setVisibility(GONE);
        mDownLoadBar.setVisibility(GONE);
        mFontImageView.setAlpha(255);
    }

    public void refreshDownloadingUI(int progress){
        mDownLoadSize.setVisibility(GONE);
        mDownLoadIcon.setVisibility(GONE);
        mDownLoadBar.setVisibility(VISIBLE);
        mDownLoadBar.setProgress(progress);
        mFontImageView.setAlpha(100);
    }

    public void refreshUnDownloadUI(){
        mDownLoadSize.setVisibility(VISIBLE);
        mDownLoadIcon.setVisibility(VISIBLE);
        mDownLoadBar.setVisibility(GONE);
        mFontImageView.setAlpha(100);
    }

    public void refreshCheckedUI(boolean isCheck){
        // 设置勾选
        if (isCheck) {
            mItemImageView.setVisibility(View.VISIBLE);
        } else {
            mItemImageView.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置是否显示下载字体
     *
     */
    public void setDownLoadText(String text) {
        mDownLoadSize.setVisibility(VISIBLE);
        mDownLoadIcon.setVisibility(VISIBLE);
        mDownLoadSize.setText(text);
        mDownLoadIcon.setImageResource(R.drawable.downloaderfontyindao);
    }

    public void setIconItemImage(final String thumb_120, final FontItemView itemView) {
        if (itemView == null || thumb_120 == null)
            return;
        itemView.mLoadingIcon.clearAnimation();
        itemView.mLoadingIcon.setAnimation(null);
        Glide.with(getContext())
                .load(thumb_120)
                .into(itemView.mFontImageView);
    }
}
