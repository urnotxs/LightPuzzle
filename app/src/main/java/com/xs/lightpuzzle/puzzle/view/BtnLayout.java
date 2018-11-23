package com.xs.lightpuzzle.puzzle.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.util.Utils;


/**
 * Created by xs on 2017/12/12.
 * 编辑页底部按钮
 */

public class BtnLayout extends FrameLayout {

    public ImageView mCircleImage;

    private LinearLayout mBtnLayout;
    public ImageView mImageView;
    private TextView mTextView;

    public BtnLayout(Context context) {
        super(context);
        initUI();
    }

    public void setImageDrawable(@DrawableRes int id) {
        mImageView.setImageDrawable(getContext().getResources().getDrawable(id));
    }

    public void setText(CharSequence text) {
        if (text == null) {
            mTextView.setVisibility(GONE);
        } else {
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(text);
        }
    }

    public void setTextSize(int puzzleMode) {
        if (puzzleMode == PuzzleMode.MODE_LAYOUT_JOIN
                || puzzleMode == PuzzleMode.MODE_LONG
                || puzzleMode == PuzzleMode.MODE_JOIN){
            // 有底部圆，用小字体
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        }else{
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        }
    }

    public void setTextColor(int textColor) {
        mTextView.setTextColor(textColor);
    }

    public void setTextColor(ColorStateList colorStateList) {
        mTextView.setTextColor(colorStateList);
    }

    private void initUI() {
        LayoutParams flParams = null;

        mCircleImage = new ImageView(getContext());
        flParams = new LayoutParams(Utils.getRealPixel3(108), Utils.getRealPixel3(108));
        mCircleImage.setImageResource(R.drawable.photosplicechoosebg);
        mCircleImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.puzzles_bg_btn));
        flParams.gravity = Gravity.CENTER;
        mCircleImage.setVisibility(View.GONE);
        this.addView(mCircleImage, flParams);

        mBtnLayout = new LinearLayout(getContext());
        flParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.CENTER;
        mBtnLayout.setOrientation(LinearLayout.VERTICAL);
        mBtnLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(mBtnLayout, flParams);
        {
            LinearLayout.LayoutParams llParams = null;

            mImageView = new ImageView(getContext());
            llParams = new LinearLayout.LayoutParams(Utils.getRealPixel3(62), Utils.getRealPixel3(62));
            mImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.puzzle_edit_add_selector));
            mBtnLayout.addView(mImageView, llParams);

            llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mTextView = new TextView(getContext());
            mTextView.setPadding(0, Utils.getRealPixel3(-2), 0, 0);
            mTextView.setText("模板");
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            mTextView.setTextColor(Color.WHITE);
            mTextView.setAlpha(0.7f);
            mBtnLayout.addView(mTextView, llParams);
        }
    }

    public void setBackground(boolean visible) {
        if (visible) {
            mCircleImage.setVisibility(VISIBLE);
        } else {
            mCircleImage.setVisibility(GONE);
        }
    }
}