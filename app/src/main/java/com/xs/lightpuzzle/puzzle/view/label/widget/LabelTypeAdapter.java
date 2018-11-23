package com.xs.lightpuzzle.puzzle.view.label.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.R;

import java.util.List;

/**
 * Created by urnot_XS on 2018/4/11.
 */

public class LabelTypeAdapter extends RecyclerView.Adapter<LabelTypeAdapter.ViewHolder> {

    private Context mContext;
    private List<IconInfo> mIconInfos;

    interface OnItemClickListener {
        void OnItemClick(int position, boolean isClick);
    }

    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public LabelTypeAdapter(Context context, List<IconInfo> iconInfos) {
        mContext = context;
        this.mIconInfos = iconInfos;
    }

    public void setData(List<IconInfo> iconInfos) {
        mIconInfos = iconInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_recycle_lable_type, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        IconInfo iconInfo = mIconInfos.get(position);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(position, false);
                }
            }
        });
        if (iconInfo.isShowText()) {
            holder.textView.setVisibility(View.VISIBLE);
        } else {
            holder.textView.setVisibility(View.GONE);
        }
        holder.textView.setText(iconInfo.getIconText());
        holder.imageView.setImageDrawable(iconInfo.getIconDrawable());
        holder.imageView.setSelected(iconInfo.getSelectedStatus());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(position, true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIconInfos.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.lable_page_type);
            imageView = view.findViewById(R.id.lable_page_type_imageview);
            textView = view.findViewById(R.id.lable_page_type_textview);
        }
    }
}