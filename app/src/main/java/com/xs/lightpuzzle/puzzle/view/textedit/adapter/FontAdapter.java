package com.xs.lightpuzzle.puzzle.view.textedit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xs.lightpuzzle.puzzle.view.textedit.model.FontItemInfo;
import com.xs.lightpuzzle.puzzle.view.textedit.widget.FontItemView;

import java.util.ArrayList;

/**
 * Created by admin on 2018/4/11.
 */

public class FontAdapter extends BaseAdapter {

    private Context mContext;
    private FontItemView itemView;
    private int mFontIndex;
    private ArrayList<FontItemInfo> mFontDatas;

    public void setReadyDown(boolean readyDown , int downIndex) {
        isReadyDown = readyDown;
        mReadyDownIndex = downIndex;
    }

    private boolean isReadyDown;
    private int mReadyDownIndex;

    public FontAdapter(Context context){
        mContext = context;
        mFontDatas = new ArrayList<>();
    }

    public void setData(ArrayList<FontItemInfo> fontDatas){
        mFontDatas = fontDatas;
    }

    public void setFontIndex(int index){
        mFontIndex = index;
    }
    public int getFontIndex(){
        return mFontIndex ;
    }

    @Override
    public int getCount() {
        return mFontDatas.size();
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
            convertView = new FontItemView(mContext);
            itemView = (FontItemView) convertView;
            convertView.setTag(itemView);
        } else {
            itemView = (FontItemView) convertView.getTag();
        }

        if (mFontDatas != null && position < mFontDatas.size()) {
            if (position == mFontIndex) {
                mFontDatas.get(position).setCheck(true);//标记被选中
            }else{
                mFontDatas.get(position).setCheck(false);//标记被选中
            }

            FontItemInfo itemInfo = mFontDatas.get(position);
            if (itemInfo.isNeedDownFont()) {//是否需要下载字体
                if (mReadyDownIndex == position && isReadyDown) {
                    itemInfo.setReadyDown(true);
                    itemInfo.setDownTextShow(false);
                    isReadyDown = false;
                }
            }

            itemView.setItemInfo(itemInfo);//有下载
            itemView.setIconItemImage(itemInfo.getImageUrl(), itemView);//设置图片
        }
        return convertView;
    }


}
