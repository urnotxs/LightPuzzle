package com.xs.lightpuzzle.demo.a_tactics_demo.view;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.CircleImageView;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_tactics_demo.data.TacticsModelVO;

/**
 * @author xs
 * @description 带头像，可选中，可移动 的战术板
 * @since 2019/2/22
 */

public class TacticsItem implements View.OnClickListener,View.OnTouchListener {
    private boolean isEditable;
    private TacticsModelVO itemVO;
    private TacticsBoard parentView;
    public FrameLayout.LayoutParams layoutParams;

    private float scale;
    private int width = 50;
    private int height = 80;

    public TacticsItem(TacticsBoard parentView, TacticsModelVO itemVO, boolean isEditable) {
        this.parentView = parentView;
        this.itemVO = itemVO;
        this.isEditable = isEditable;
    }

    private RelativeLayout itemView;
    private TextView txtPos;
    private CircleImageView imgHead;
    private ImageView imgBoard;
    private TextView txtName;

    public void addViewToScreen() {
        scale = parentView.width / 600.0f;
        itemView = new RelativeLayout(parentView.getContext());
        layoutParams = new FrameLayout.LayoutParams(
                (int) (width * scale), (int) (height * scale));
        layoutParams.gravity = Gravity.TOP;
        layoutParams.setMargins((int) (scale * (itemVO.GetPosX() - width / 2)),
                (int) (scale * (itemVO.GetPosY() - width / 2)), 0, 0);
        if (isEditable)
            itemView.setOnClickListener(this);
        parentView.addView(itemView, layoutParams);
        {
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 30) * scale));
            txtPos = new TextView(parentView.getContext());
            txtPos.setTextColor(itemView.getResources().getColor(R.color.white));
            txtPos.setGravity(Gravity.CENTER);
            itemView.addView(txtPos, rlParams);

            imgHead = new CircleImageView(parentView.getContext());
            imgHead.setBorderWidth((int) (2 * scale));
            rlParams = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 30) * scale));
            itemView.addView(imgHead, rlParams);

            imgBoard = new ImageView(parentView.getContext());
            imgBoard.setScaleType(ImageView.ScaleType.FIT_CENTER);
            rlParams = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 20) * scale));
            itemView.addView(imgBoard, rlParams);

            txtName = new TextView(parentView.getContext());
            txtName.setTextSize(10);
            txtName.setTextColor(itemView.getResources().getColor(R.color.white));
            txtName.setBackgroundResource(R.color.txtback);
            txtName.setGravity(Gravity.CENTER);
            rlParams = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((20) * scale));
            rlParams.setMargins(0, (int) ((height - 20) * scale), 0, 0);
            itemView.addView(txtName, rlParams);

            int typeColor;
            switch (itemVO.GetType()) {
                case 0:
                    typeColor = R.color.gk;
                    imgBoard.setImageResource(R.drawable.icon_gk_bg);
                    imgHead.setBorderColor(itemView.getResources().getColor(R.color.gk));
                    break;
                case 1:
                    typeColor = R.color.df;
                    imgBoard.setImageResource(R.drawable.icon_df_bg);
                    imgHead.setBorderColor(itemView.getResources().getColor(R.color.df));
                    break;
                case 2:
                    typeColor = R.color.mf;
                    imgBoard.setImageResource(R.drawable.icon_mf_bg);
                    imgHead.setBorderColor(itemView.getResources().getColor(R.color.mf));
                    break;
                case 3:
                    typeColor = R.color.mf;
                    imgBoard.setImageResource(R.drawable.icon_mf_bg);
                    imgHead.setBorderColor(itemView.getResources().getColor(R.color.mf));
                    break;
                case 4:
                    typeColor = R.color.fw;
                    imgBoard.setImageResource(R.drawable.icon_fw_bg);
                    imgHead.setBorderColor(itemView.getResources().getColor(R.color.fw));
                    break;
            }
            txtPos.setText(itemVO.GetPositionName());

            if (itemVO.HasUser()) {
                txtName.setText(itemVO.GetUserName());
                imgHead.setImageResource(R.drawable.tactics_logo);
                imgHead.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.VISIBLE);
                txtPos.setVisibility(View.INVISIBLE);
            } else {
                imgHead.setVisibility(View.GONE);
                txtName.setVisibility(View.INVISIBLE);
                txtPos.setVisibility(View.VISIBLE);
            }
            // 用户信息

            if (itemVO.GetIsShow()) {
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (parentView.selectedItem != null) {
            parentView.selectedItem.RefreshView(false);
            itemView.setOnTouchListener(null);
        }
        RefreshView(true);
        itemView.setOnTouchListener(this);
    }

    private void RefreshView(boolean isClick) {
        if (!isClick) {
            width = 50;
            height = 80;
            parentView.posItem = null;
            parentView.selectedItem = null;
            reDeployMinLayout();
        } else {
            width = 60;
            height = 96;
            parentView.selectedItem = this;
            parentView.posItem = itemVO;
            reDeployMaxLayout();
        }
    }

    private void reDeployMaxLayout() {
        layoutParams = (FrameLayout.LayoutParams) itemView.getLayoutParams();
        layoutParams.width = (int) (width * scale);
        layoutParams.height = (int) (height * scale);
        layoutParams.setMargins((int) (scale * (itemVO.GetPosX() - (width / 2))),
                (int) (scale * (itemVO.GetPosY() - (width / 2))), 0, 0);
        parentView.updateViewLayout(itemView, layoutParams);
        {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 36) * scale));
            itemView.updateViewLayout(txtPos, param);

            param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 36) * scale));
            imgHead.setBorderWidth((int) (6 * scale));
            itemView.updateViewLayout(imgHead, param);

            param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 24) * scale));
            itemView.updateViewLayout(imgBoard, param);

            param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((24) * scale));
            param.setMargins(0, (int) ((height - 24) * scale), 0, 0);
            itemView.updateViewLayout(txtName, param);
        }
    }

    private void reDeployMinLayout() {
        layoutParams = (FrameLayout.LayoutParams) itemView.getLayoutParams();
        layoutParams.width = (int) (width * scale);
        layoutParams.height = (int) (height * scale);
        layoutParams.setMargins((int) (scale * (itemVO.GetPosX() - (width / 2))),
                (int) (scale * (itemVO.GetPosY() - (width / 2))), 0, 0);
        parentView.updateViewLayout(itemView, layoutParams);
        {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 30) * scale));
            itemView.updateViewLayout(txtPos, param);

            param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 30) * scale));
            imgHead.setBorderWidth((int) (2 * scale));
            itemView.updateViewLayout(imgHead, param);

            param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((height - 20) * scale));
            itemView.updateViewLayout(imgBoard, param);

            param = new RelativeLayout.LayoutParams(
                    (int) (width * scale), (int) ((20) * scale));
            param.setMargins(0, (int) ((height - 20) * scale), 0, 0);
            itemView.updateViewLayout(txtName, param);
        }
    }

    private int startX;
    private int startY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - startX;
                int dy = (int) event.getRawY() - startY;

                int left = (int) (scale * (itemVO.GetPosX() - (width / 2)) + dx);
                int top = (int) (scale * (itemVO.GetPosY() - (height / 2)) + dy);
                if (left < 0) {
                    left = 0;
                }

                if (top < 0) {
                    top = 0;
                }

                if (left > parentView.width - width * scale) {
                    left = parentView.width - (int) (width * scale);
                }

                if (top > parentView.height - height * scale) {
                    top = parentView.height - (int) (height * scale);
                }

                layoutParams.setMargins(left, top, 0, 0);
                // layoutParams.setMargins(startX+dx,startY+dy, 0, 0);

                // layoutParams.x = lastX;
                // layoutParams.y = lastY-30;
                parentView.updateViewLayout(v, layoutParams);

                break;
            case MotionEvent.ACTION_UP:
                dx = (int) event.getRawX() - startX;
                dy = (int) event.getRawY() - startY;

                left = (int) (scale * (itemVO.GetPosX() - width / 2) + dx);
                top = (int) (scale * (itemVO.GetPosY() - height / 2) + dy);

                if (left < 0) {
                    left = 0;
                }

                if (top < 0) {
                    top = 0;
                }

                if (left > parentView.width - width * scale) {
                    left = parentView.width - (int) (width * scale);
                }

                if (top > parentView.height - height * scale) {
                    top = parentView.height - (int) (height * scale);
                }

                itemVO.SetXY(width / 2 + (float) left / scale, height / 2 + (float) top / scale);

                RefreshView(false);
                itemView.setOnTouchListener(null);
                break;
        }
        return true;
    }
}
