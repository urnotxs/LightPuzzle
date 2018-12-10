package com.xs.lightpuzzle.puzzle.layout.info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by xs on 2018/4/18.
 */

public class PolygonLayoutView extends RelativeLayout implements View.OnClickListener{

    private Context mContext;
    private RelativeLayout mPageLayout;
    private LinearLayout mTotalLayout;


    public PolygonLayoutView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        /*if (mPolygonLayoutView == null) {
            mPolygonLayoutView = new PolygonLayoutView(getContext());
            ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            this.addView(mPolygonLayoutView, mParams);
        } else {
            mPolygonLayoutView.setVisibility(VISIBLE);
        }*/
        ViewGroup.LayoutParams mParams;
        LayoutParams rParams;

        mPageLayout = new RelativeLayout(mContext);
        mParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT ,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mPageLayout , mParams);
        {
            mPageLayout.setOnClickListener(this);
            mTotalLayout = new LinearLayout(mContext);
            mTotalLayout.setBackgroundColor(0xff6f5c62);
            rParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTotalLayout.setOrientation(LinearLayout.VERTICAL);
            mTotalLayout.setOnClickListener(null);

            mPageLayout.addView(mTotalLayout, rParams);
            {
                LayoutView layoutView = new LayoutView(mContext);
                mTotalLayout.addView(layoutView);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
