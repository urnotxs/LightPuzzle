package com.xs.lightpuzzle.puzzle.a_recyclerview_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by xs on 2018/12/14.
 */

public class RecyclerViewTestLayout extends FrameLayout {

    private RecyclerView mRecyclerView;
    private TestAdapter mAdapter;

    public RecyclerViewTestLayout(@NonNull Context context) {
        super(context);
        initView(context);
        initFakeData();
    }

    private void initView(Context context) {
        RelativeLayout containerLayout = new RelativeLayout(context);
        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(containerLayout, flParams);
        {
            mRecyclerView = new RecyclerView(context);
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mRecyclerView.getItemAnimator().setChangeDuration(0); // 通过设置动画执行时间为0来解决闪烁问题
            mRecyclerView.addItemDecoration(new TestItemDecoration(new TextListener() {
                @Override
                public String getName(int position) {
                    if (position < nameList.size()){
                        return ChineseCharacterHelper.getSpells(String.valueOf(nameList.get(position).charAt(0))).toUpperCase();
                    }
                    return "";
                }
            }));
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(layoutManager);
            containerLayout.addView(mRecyclerView, rlParams);

            initAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void initAdapter() {
        if (mAdapter != null) {
            mAdapter = null;
        }
        mAdapter = new TestAdapter(getContext(), nameList);
    }

    private ArrayList<String> nameList = new ArrayList<>();

    private void initFakeData() {
        ArrayList<String> fakeNameList = new ArrayList<>();
        fakeNameList.add("肖霜");
        fakeNameList.add("佳一");
        fakeNameList.add("佳阳");
        fakeNameList.add("孝泳");
        fakeNameList.add("笑容");
        fakeNameList.add("小二");
        fakeNameList.add("炎阳");
        fakeNameList.add("李欧");
        fakeNameList.add("齐颖");
        fakeNameList.add("李宇春");
        fakeNameList.add("高斯");
        fakeNameList.add("周迅");
        fakeNameList.add("里皮");
        fakeNameList.add("高圆圆");
        fakeNameList.add("杨浩");
        fakeNameList.add("陈鑫");

        for (int i = 0; i < fakeNameList.size(); i++) {
            String hostName = fakeNameList.get(i);
            String hostNameChar = ChineseCharacterHelper.getSpells(String.valueOf(hostName.charAt(0))).toUpperCase();
            if (!nameList.contains(hostName)) {
                for (int j = i; j < fakeNameList.size(); j++) {
                    String name = fakeNameList.get(j);
                    String nameChar = ChineseCharacterHelper.getSpells(String.valueOf(name.charAt(0))).toUpperCase();
                    if (hostNameChar.equals(nameChar)){
                        nameList.add(name);
                    }
                }
            }
        }
    }

    public interface TextListener{
        String getName(int position);
    }

}
