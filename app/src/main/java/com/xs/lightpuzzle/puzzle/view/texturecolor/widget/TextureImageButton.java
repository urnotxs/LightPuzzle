package com.xs.lightpuzzle.puzzle.view.texturecolor.widget;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * Created by xs on 2018/1/24.
 */

public class TextureImageButton extends RelativeLayout {

    private ImageView bt;
    private ImageView lock;
    private boolean isLock;
    private boolean isCheck = false;
    private int res;
    private int tar;

    private int index;


    public TextureImageButton(Context context, int res, int tar) {
        super(context);
        this.res = res;
        this.tar = tar;
        initUI();
    }

    private void initUI() {
        LayoutParams lParams;
        lParams = new LayoutParams(Utils.getRealPixel3(80), Utils.getRealPixel3(80));
        RelativeLayout wrap = new RelativeLayout(getContext());
        this.addView(wrap, lParams);
        {
            lParams = new LayoutParams(Utils.getRealPixel3(72),
                    Utils.getRealPixel3(72));
            lParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lParams.topMargin = Utils.getRealPixel3(8);
            lParams.rightMargin = Utils.getRealPixel3(6);
            bt = new ImageView(getContext());
            bt.setImageResource(res);
            wrap.addView(bt, lParams);

            lParams = new LayoutParams(Utils.getRealPixel3(28),
                    Utils.getRealPixel3(28));
            lParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lock = new ImageView(getContext());
            wrap.addView(lock, lParams);
            lock.setImageResource(R.drawable.ic_puzzle_texture_or_filter_lock);
            lock.setVisibility(INVISIBLE);
        }
    }

    public void setCheck(boolean check) {
        //选择状态
        if (check) {
            bt.setImageResource(tar);
            isCheck = check;
        } else {
            bt.setImageResource(res);
            isCheck = check;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLockVisible(int visibility) {
        lock.setVisibility(visibility);
        if (visibility == VISIBLE) {
            isLock = true;
        } else {
            isLock = false;
        }
    }

    public boolean islock() {
        return isLock;
    }

    public boolean isCheck() {
        return isCheck;
    }
}
