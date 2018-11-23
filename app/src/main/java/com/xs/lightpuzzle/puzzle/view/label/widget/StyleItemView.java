package com.xs.lightpuzzle.puzzle.view.label.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.Utils;


/**
 * Created by urnotXS on 2018/4/11.
 */

public class StyleItemView extends LinearLayout implements View.OnClickListener {

    public StyleItemView(Context context) {
        super(context);
        initView();
    }

    private LinearLayout mItemLayout;

    public ImageView getItemImageView() {
        return mItemImageView;
    }


    public TextView getItemTextView() {
        return mItemTextView;
    }


    private ImageView mItemImageView;
    private TextView mItemTextView;

    private String mIconText;


    private void initView() {
        LayoutParams llParams;

        llParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(
                Utils.getRealPixel3(42),
                Utils.getRealPixel3(52),
                Utils.getRealPixel3(42), 0);
        mItemLayout = new LinearLayout(getContext());
        mItemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mItemLayout.setOrientation(LinearLayout.VERTICAL);
        mItemLayout.setClickable(true);
        addView(mItemLayout,llParams);
        {
            mItemImageView = new ImageView(getContext());
            llParams = new LayoutParams(
                    Utils.getRealPixel3(92),
                    Utils.getRealPixel3(92));
            mItemImageView.setOnClickListener(this);
            mItemLayout.addView(mItemImageView,llParams);


            mItemTextView = new TextView(getContext());
            llParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            llParams.setMargins(0, Utils.getRealPixel3(10),0,0);
            mItemTextView.setTextColor(getResources().getColor(R.color.puzzle_label_text_color));
            mItemTextView.setTextSize(11);
            mItemTextView.setVisibility(GONE);
            mItemLayout.addView(mItemTextView,llParams);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
