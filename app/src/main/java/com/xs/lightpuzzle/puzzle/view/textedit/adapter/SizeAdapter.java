package com.xs.lightpuzzle.puzzle.view.textedit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xs.lightpuzzle.puzzle.view.textedit.model.SizeItemInfo;
import com.xs.lightpuzzle.puzzle.view.textedit.widget.SizeItemView;

import java.util.ArrayList;

/**
 * Created by admin on 2018/4/11.
 */

public class SizeAdapter extends BaseAdapter {
    private Context mContext;
    private SizeItemView itemView;
    private ArrayList<SizeItemInfo> mSizeDatas;

    public SizeAdapter(Context context){
        mContext = context;
        mSizeDatas = new ArrayList<>();
    }

    public void setData(ArrayList<SizeItemInfo> sizeDatas){
        mSizeDatas = sizeDatas;
    }

    @Override
    public int getCount() {
        return mSizeDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = new SizeItemView(mContext);
            itemView = (SizeItemView) convertView;
            convertView.setTag(itemView);
        } else {
            itemView = (SizeItemView) convertView.getTag();
        }

        mSizeDatas.get(position).setTextFont("BAUBODN.TTF");
        mSizeDatas.get(position).setText("你好·时光");
        mSizeDatas.get(position).setShowTextSize(20f + 3f * (position));

        itemView.setItemInfo(mSizeDatas.get(position));
        return convertView;
    }

}
