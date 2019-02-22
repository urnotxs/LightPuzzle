package com.xs.lightpuzzle.demo.a_recyclerview_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xs.lightpuzzle.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xs on 2018/12/14.
 */

public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mNameList;

    public TestAdapter(Context context, ArrayList<String> nameList) {
        mContext = context;
        mNameList = nameList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.test_recycler_item, viewGroup, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TextViewHolder holder = (TextViewHolder) viewHolder;
        holder.mTitleTextView.setText(mNameList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.subject_title)
        TextView mTitleTextView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
