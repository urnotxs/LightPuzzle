package com.xs.lightpuzzle.puzzle.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;

/**
 * 带按压效果的按钮, 并且可以设置按压时的透明度
 *
 * Created by zwq on 2015/1/6 09:29
 */
public class EffectiveImageButton extends AppCompatImageView {

    private Context mContext;
    private boolean mOrigin;
    private int mNormalResId = -1;
    private int mPressedResId = -1;
    private StateListDrawable mSelector;
    private int mAlpha = 255;
    private boolean mChecked = false;

    public EffectiveImageButton(Context context) {
        super(context);
        mContext = context;
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    /**
     * @param normalResId 正常的图片
     * @param resPress 按压时的图片
     */
    public EffectiveImageButton(Context context, int normalResId, int resPress) {
        super(context);
        mContext = context;
        setButtonImage(normalResId, resPress);
    }

    /**
     * @param normalResId 正常的图片
     * @param pressedResId 按压时的图片
     */
    public void setButtonImage(int normalResId, int pressedResId) {
        setButtonImage(normalResId, pressedResId, mAlpha);
    }

    /**
     * @param normalResId 正常的图片
     * @param pressedResId 按压时的图片
     * @param isReplaceOld 替换掉原来的图片
     */
    public void setButtonImage(int normalResId, int pressedResId, boolean isReplaceOld) {
        mOrigin = !isReplaceOld;
        setButtonImage(normalResId, pressedResId, mAlpha);
    }

    /**
     * @param normalResId 正常的图片
     * @param pressedResId 按压时的图片
     * @param alpha 按压时的透明度 0-255
     */
    public void setButtonImage(int normalResId, int pressedResId, int alpha) {
        if (!mOrigin) {
            mNormalResId = normalResId;
            mPressedResId = pressedResId;
            mAlpha = alpha;
            mOrigin = true;
            mChecked = true; // 默认选中
        }
        setSelector(normalResId, pressedResId);
    }

    public void setButtonImage(Bitmap normalBitmap, Bitmap pressedBitmap) {
        setSelector(normalBitmap, pressedBitmap);
        if (mSelector == null && normalBitmap != null) {
            setImageBitmap(normalBitmap);
        } else {
            setImageDrawable(mSelector);
        }
    }

    @SuppressWarnings("deprecation")
    private void setSelector(int normalResId, int pressedResId) {
        if (normalResId == -1 || pressedResId == -1) {
            return;
        }

        Resources res = mContext.getResources();
        mSelector = new StateListDrawable();
        mSelector.addState(new int[]{android.R.attr.state_pressed}, res.getDrawable(pressedResId));
        mSelector.addState(new int[]{android.R.attr.state_enabled}, res.getDrawable(normalResId));
        res = null;

        if (mSelector == null) {
            setImageResource(normalResId);
        } else {
            setImageDrawable(mSelector);
        }
    }

    private void setSelector(Bitmap normal, Bitmap pressed) {
        if (normal == null || pressed == null) {
            return;
        }

        Resources res = mContext.getResources();
        mSelector = new StateListDrawable();
        mSelector.addState(new int[]{android.R.attr.state_pressed},
                new BitmapDrawable(res, pressed));
        mSelector
                .addState(new int[]{android.R.attr.state_enabled}, new BitmapDrawable(res, normal));
        res = null;
    }

    /**
     * 设置选中状态: 选中: true, 未选中: false
     */
    public void setChecked(boolean isChecked) {
        setSwitchState(!isChecked);
        mChecked = isChecked;
    }

    /**
     * 设置开关状态: 开: true, 关: false
     */
    public void setSwitchState(boolean isOn) {
        if (isOn) {
            setButtonImage(mNormalResId, mPressedResId);
        } else {
            setButtonImage(mPressedResId, mNormalResId);
        }
        mChecked = isOn;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setOnFocusState() {
        setButtonImage(mNormalResId, mPressedResId);
        mChecked = true;
    }

    public void setOnReadyState() {
        setButtonImage(mPressedResId, mNormalResId);
        mChecked = false;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setAlpha(mAlpha);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            setAlpha(255);
        }
        return super.onTouchEvent(event);
    }
}