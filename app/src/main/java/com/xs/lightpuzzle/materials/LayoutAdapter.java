package com.xs.lightpuzzle.materials;

import android.util.Pair;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xs.lightpuzzle.LightPuzzleApplication;
import com.xs.lightpuzzle.R;

/**
 * Created by xs on 2018/12/4.
 */

public class LayoutAdapter extends BaseQuickAdapter<Pair<String, Integer>, BaseViewHolder> {
    private final Integer mPhotoNum;

    public LayoutAdapter(Integer photoNum) {
        super(R.layout.item_grid_template_layout);
        mPhotoNum = photoNum;
    }

    @Override
    protected void convert(BaseViewHolder helper, Pair<String, Integer> item) {
        ImageView iv = helper.getView(R.id.template_layout_item_iv);
        iv.setImageResource(getDrawableResId(item.first));
    }

    private int getDrawableResId(String drawableName) {
        return LightPuzzleApplication.getContext().getResources().getIdentifier(drawableName, "drawable",
                LightPuzzleApplication.getContext().getPackageName());
    }
}
