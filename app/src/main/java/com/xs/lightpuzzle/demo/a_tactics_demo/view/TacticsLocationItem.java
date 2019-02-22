package com.xs.lightpuzzle.demo.a_tactics_demo.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_tactics_demo.data.TacticsModelVO;

import java.util.ArrayList;

/**
 * @author xs
 * @description
 * @since 2019/2/21
 */

public class TacticsLocationItem implements View.OnClickListener {

    private boolean isEditable;
    private TacticsModelVO itemVO;
    private TacticsBoard parentView;
    private View view;
    private TextView txtName;
    private ImageButton btnLocation;
    private LayoutInflater mInflater;
    private FrameLayout.LayoutParams layoutParams;
    private ArrayList<ImageButton> Buttons = new ArrayList<>();

    private float scale;
    private int width = 60;
    private int height = 80;

    // 用于刷新UI
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public TacticsLocationItem(TacticsBoard parentView, TacticsModelVO itemVO, boolean isEditable) {
        this.parentView = parentView;
        this.itemVO = itemVO;
        this.isEditable = isEditable;
    }

    public void addViewToScreen() {
        scale = (float) ((float) (parentView.width) / 600.0);
        getView(parentView);
        if (isEditable)
            btnLocation.setOnClickListener(this);

        layoutParams = new FrameLayout.LayoutParams(
                (int) (width * scale), (int) (height * scale));
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.setMargins(
                (int) (scale * (itemVO.GetPosX() - (width / 2))),
                (int) (scale * (itemVO.GetPosY() - (width / 2))),
                0, 0);
        parentView.addView(view, layoutParams);
    }

    private View getView(FrameLayout parentView) {
        mInflater = LayoutInflater.from(parentView.getContext());
        view = mInflater.inflate(R.layout.v_item_tactics_location, null);
        btnLocation = view.findViewById(R.id.img_location_cf);
        txtName = view.findViewById(R.id.txt_location_cf);

        if (itemVO.GetIsShow()) {
            btnLocation.setBackgroundResource(R.drawable.item_location_selected);
        } else {
            btnLocation.setBackgroundResource(R.drawable.item_location);
        }

        btnLocation.setTag(itemVO);
        Buttons.add(btnLocation);
        txtName.setText(itemVO.GetPositionName());

        //设置底部textView的背景色
        switch (itemVO.GetType()) //0-GK 1-DF 2-MF 3-MF 4-FW
        {
            case 0:
                txtName.setBackgroundResource(R.drawable.shape_location_gk);
                break;
            case 1:
                txtName.setBackgroundResource(R.drawable.shape_location_cb);
                break;
            case 2:

                break;
            case 3:
                txtName.setBackgroundResource(R.drawable.shape_location_dmf);
                break;
            case 4:
                txtName.setBackgroundResource(R.drawable.shape_location_lwf);
                break;
        }
        view.setTag(itemVO);
        return view;
    }

    private void refreshView(TacticsModelVO itemVO) {
        for (int i = 0; i < parentView.getChildCount(); i++) {
            btnLocation = parentView.getChildAt(i).findViewById(R.id.img_location_cf);
            if (itemVO == parentView.getChildAt(i).getTag()) {
                btnLocation.setBackgroundResource(R.drawable.item_location_selected);
            } else {
                btnLocation.setBackgroundResource(R.drawable.item_location);
            }
        }
    }

    @Override
    public void onClick(View v) {
        TacticsModelVO modelVO = (TacticsModelVO) v.getTag();
        refreshView(modelVO);
        String location = modelVO.GetPositionName();

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("Position", location);
        message.setData(bundle);
        message.what = 1;
        handler.sendMessage(message);
    }
}
