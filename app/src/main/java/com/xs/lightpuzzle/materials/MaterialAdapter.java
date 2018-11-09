package com.xs.lightpuzzle.materials;

import android.support.v7.widget.AppCompatImageView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.like.LikeButton;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xs on 2018/11/8.
 */

public class MaterialAdapter extends BaseQuickAdapter<TemplateSet, BaseViewHolder> {

    private final Integer mPhotoNum;
    private final int mMaterialList;
    private final Set<String> mDownloading = new HashSet<>();
    private boolean mManager;

    public MaterialAdapter(Integer photoNum) {
        this(photoNum, MATERIAL_LIST.NOT_FLAG);
    }

    public MaterialAdapter(Integer photoNum, int materialList) {
        super(R.layout.item_grid_material);
        mPhotoNum = photoNum;
        mMaterialList = materialList;
    }

    public void addDownloading(String id) {
        if (!mDownloading.contains(id)) {
            mDownloading.add(id);
        }
    }

    public void removeDownloading(String id) {
        mDownloading.remove(id);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TemplateSet item) {

        RelativeLayout rootView = helper.getView(R.id.material_item_rl_root);
        FrameLayout labelRootView = helper.getView(R.id.material_item_fl_label_root);
        AppCompatImageView labelView = helper.getView(R.id.material_item_apiv_label);
        ProgressBar progressBar = helper.getView(R.id.material_item_pb);
        AppCompatImageView iv = helper.getView(R.id.material_item_apiv);
        LikeButton favoriteView = helper.getView(R.id.material_item_apiv_favorite);

        String id = item.getId();

        ThumbHelper.load(mContext, item, mDownloading.contains(id),
                mPhotoNum, rootView, labelRootView, labelView,
                progressBar, iv, favoriteView);

        if (mMaterialList == MATERIAL_LIST.DOWNLOADED && mManager) {
            helper.setVisible(R.id.material_item_apib_delete, true);
            helper.addOnClickListener(R.id.material_item_apib_delete);
        } else {
            helper.setVisible(R.id.material_item_apib_delete, false);
        }

        helper.setVisible(R.id.material_item_aptv_debug, true);
        String info = "id = " + id + "\n" + "order = " + item.getOrder();
        helper.setText(R.id.material_item_aptv_debug, info);
    }


    public void setManager(boolean isManager) {
        mManager = isManager;
    }

    public void applyManager(boolean isManager) {
        setManager(isManager);
        notifyDataSetChanged();
    }

    public void applyManagerContrarily() {
        applyManager(!mManager);
    }
}
