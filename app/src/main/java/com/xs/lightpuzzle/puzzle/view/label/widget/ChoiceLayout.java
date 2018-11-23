package com.xs.lightpuzzle.puzzle.view.label.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.label.view.BlankRecyclerView;

import java.util.ArrayList;


/**
 * Created by urnot_XS on 2018/4/11.
 */

public class ChoiceLayout extends LinearLayout implements View.OnClickListener {

    private LinearLayout mTitleLayout;
    private int mTitleTextId;
    private BlankRecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private LabelTypeAdapter mTypeAdapter;
    private ArrayList<IconInfo> mIconInfos;

    public interface OnChangedShowViewListener {
        void onRefresh(int position);

        void onHideKeyboart();
    }

    private OnChangedShowViewListener mChangedListener;

    public void setOnChangedShowViewListener(OnChangedShowViewListener listener) {
        mChangedListener = listener;
    }

    public ChoiceLayout(Context context, int titleId) {
        super(context);
        mTitleTextId = titleId;
        mIconInfos = new ArrayList<>();
        initView();
    }

    public void setData(ArrayList<IconInfo> iconInfos) {
        mIconInfos = iconInfos;
        mTypeAdapter.setData(mIconInfos);
        mTypeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        LayoutParams llParams;

        llParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(llParams);
        this.setOrientation(LinearLayout.VERTICAL);
        {
            //标题+分割线布局
            mTitleLayout = new LinearLayout(getContext());
            llParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            llParams.setMargins(Utils.getRealPixel3(50), 0, 0, 0);
            mTitleLayout.setGravity(Gravity.CENTER_VERTICAL);
            mTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
            this.addView(mTitleLayout, llParams);
            {
                TextView textView = new TextView(getContext());
                textView.setTextSize(12);
                textView.setTextColor(getResources().getColor(R.color.puzzle_label_title_color));
                textView.setText(mTitleTextId);
                textView.setPadding(0, 0, Utils.getRealPixel3(14), 0);
                mTitleLayout.addView(textView);

                View view = new View(getContext());
                llParams = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.getRealPixel3(1));
                view.setBackgroundColor(getResources().getColor(R.color.puzzle_label_line_color));
                mTitleLayout.addView(view, llParams);
            }

            //初始化RecyclerView
            mRecyclerView = new BlankRecyclerView(getContext());
            mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
            mRecyclerView.setVerticalScrollBarEnabled(false);
            //创建LinearLayoutManager 对象 这里使用 <span style="font-family:'Source Code Pro';">LinearLayoutManager 是线性布局的意思</span>
            mLayoutManager = new GridLayoutManager(getContext(), 4);
            //mLayoutManager = new LinearLayoutManager(getContext());
            //设置RecyclerView 布局
            mRecyclerView.setLayoutManager(mLayoutManager);
            //设置Adapter
            mTypeAdapter = new LabelTypeAdapter(getContext(), mIconInfos);
            mRecyclerView.setAdapter(mTypeAdapter);
            mRecyclerView.setBlankListener(new BlankRecyclerView.BlankListener() {
                @Override
                public void onBlankClick() {
                    if (mChangedListener != null) {
                        mChangedListener.onHideKeyboart();
                    }
                }
            });
            llParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            this.addView(mRecyclerView, llParams);
            mTypeAdapter.setItemClickListener(new LabelTypeAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(int position, boolean isClick) {
                    if (!isClick) {
                        if (mChangedListener != null) {
                            mChangedListener.onHideKeyboart();
                        }
                    } else {
                        //提醒展示区更新UI
                        if (mChangedListener != null) {
                            mChangedListener.onRefresh(position);
                        }

                        //刷新当前列表的选中状态
                        for (int i = 0; i < mTypeAdapter.getItemCount(); i++) {
                            mIconInfos.get(i).setSelectedStatus(false);
                            if (i == position) {
                                mIconInfos.get(i).setSelectedStatus(true);
                            }
                        }
                        mTypeAdapter.notifyDataSetChanged();
                    }

                }
            });
        }
    }

    @Override
    public void onClick(View view) {

    }
}
