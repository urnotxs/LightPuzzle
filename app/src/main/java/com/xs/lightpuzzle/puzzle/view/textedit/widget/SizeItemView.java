package com.xs.lightpuzzle.puzzle.view.textedit.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.PuzzleConstant;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.textedit.model.SizeItemInfo;

/**
 * Created by urnotXS on 2018/4/11.
 */

public class SizeItemView extends FrameLayout {

    private RelativeLayout.LayoutParams rlParams;
    private LinearLayout.LayoutParams llParams;
    private LayoutParams flParams;

    private LinearLayout mItemLayout;
    private ImageView mItemImageView;
    private RelativeLayout mItemTextLayout;
    private TextView mItemTextView;

    private SizeItemInfo mItemInfo;

    public SizeItemView(@NonNull Context context ) {
        super(context);
        initView();
    }

    private void initView() {
        flParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                (int) (Utils.getScreenH() * 0.088));
        mItemLayout = new LinearLayout(getContext());
        mItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mItemLayout, flParams);
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
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            RelativeLayout contentLayout = new RelativeLayout(getContext());
            mItemLayout.addView(contentLayout, llParams);
            {

                rlParams = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rlParams.leftMargin = (int) (Utils.getScreenW() * 0.020);
                ImageView line = new ImageView(getContext());
                line.setImageResource(R.drawable.write_line);
                line.setScaleType(ImageView.ScaleType.FIT_XY);
                line.setAlpha(50);
                contentLayout.addView(line, rlParams);

                rlParams = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                rlParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mItemTextLayout = new RelativeLayout(getContext());
                contentLayout.addView(mItemTextLayout, rlParams);
                {
                    rlParams = new RelativeLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    rlParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mItemTextView = new TextView(getContext());
                    mItemTextView.setTextColor(Color.WHITE);
                    mItemTextLayout.addView(mItemTextView, rlParams);
                }
            }
        }
    }

    public void setItemInfo(SizeItemInfo itemInfo) {
        this.mItemInfo = itemInfo;

        // 设置勾选
        if (itemInfo.isCheck()) {
            mItemImageView.setVisibility(View.VISIBLE);
        } else {
            mItemImageView.setVisibility(INVISIBLE);
        }

        //设置字体
        mItemTextView.setText(itemInfo.getText());
        mItemTextView.setTypeface(Typeface.DEFAULT);

        if (itemInfo.getShowTextSize() == -1) {
            mItemTextView.setTextSize(itemInfo.getTextSize());
        } else {
            mItemTextView.setTextSize(itemInfo.getShowTextSize());
        }

    }

    public Typeface getFontType(String filName) {
        if (filName.equals("LiHei Pro") || filName.equals("Heiti SC")) {
            return Typeface.DEFAULT;
        }
        Typeface typeface = Typeface.createFromAsset(
                getContext().getAssets(),
                PuzzleConstant.ASSETS_FONT_PATH.FONT + filName);
        return typeface;
    }
}
