package com.xs.lightpuzzle.puzzle.view.textedit.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.puzzle.view.textedit.model.FontItemInfo;
import com.xs.lightpuzzle.puzzle.view.textedit.widget.FontItemView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
                itemView.refreshCheckedUI(true);//标记被选中
            }else{
                itemView.refreshCheckedUI(false);//标记被选中
            }

            FontItemInfo itemInfo = mFontDatas.get(position);
            itemView.setIconItemImage(itemInfo.getImageUrl(), itemView);//设置图片

            Font font = itemInfo.getFontInfo();
            String id = font.getId();
            if (font.isDownloaded()) {
                // 已经下载
                itemView.refreshDownloadedUI();
            } else {
                if (mDownloadingMap.containsKey(id)) {
                    // 正在下载
                    itemView.refreshDownloadingUI(mDownloadingMap.get(id));
                } else {
                    // 没有下载
                    itemView.refreshUnDownloadUI();
                    itemView.setDownLoadText(itemInfo.getDownText());
                }
            }
        }
        return convertView;
    }

    private final Map<String, Integer> mDownloadingMap = new HashMap<>();
    private final Set<String> mCurrDownloadedSet = new HashSet<>();

    public void addDownloading(String id, int progress, int position) {
        mDownloadingMap.put(id, progress);
        notifyDataSetChanged();
    }

    public void removeDownloading(String id, int position) {
        mDownloadingMap.remove(id);
        mCurrDownloadedSet.add(id);
        FontItemInfo itemInfo = mFontDatas.get(position);
        if (!TextUtils.isEmpty(itemInfo.getFontInfo().getFilePath())) {
            itemInfo.setFont(itemInfo.getFontInfo().getFilePath());
        }
        notifyDataSetChanged();
    }

    private void loadThumb(Font item, AppCompatImageView iv) {
        String url = item.getThumbUrl();
        if (item.isDownloaded() && !mCurrDownloadedSet.contains(item.getId())) {
            String filePath = item.getThumbFilePath();
            Glide.with(mContext)
                    .load(new File(filePath))
                    .into(iv);
        } else {
            Glide.with(mContext)
                    .load(url)
                    .into(iv);
        }
    }
}
