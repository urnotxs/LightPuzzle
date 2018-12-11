package com.xs.lightpuzzle.puzzle.layout.layoutframepage;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.AnimUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局相关
 */

public class BottomEditLineFrameView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;

    private RelativeLayout mPageLayout;
    private LinearLayout mTotalLayout;
    private LinearLayout insidePaddingLayout;
    private RelativeLayout mBackbtn;
    private SeekBar mInsidePaddingSeekBar;
    private TextView mInsidePaddingTextView;
    private SeekBar mOuterPaddingSeekBar;
    private TextView mOuterPaddingTextView;
    private SeekBar mRadianSeekBar;
    private TextView mRadianTextView;

    private int mSize;
    private int mOuterPadding = 20;
    private int mInsidePadding = 50;
    private int mRadian = 0;
    public enum CHANGEDMode {
        OUT_PADDING, IN_PADDING, RADIAN;
    }
    private OnParameChangedListener mOnParameChangedListener;
    public interface OnParameChangedListener {
        void onChanged(CHANGEDMode mode, int value);
        void onDismiss();
    }
    public void setOnParameChangedListener(OnParameChangedListener onParameChangedListener) {
        mOnParameChangedListener = onParameChangedListener;
    }

    public void open(int size, float piecePaddingRatio, float outerPaddingRatio, float pieceRadianRatio) {
        mSize = size;
        mInsidePadding = (int)(piecePaddingRatio*100);
        mOuterPadding = (int)(outerPaddingRatio*100);
        mRadian = (int)(pieceRadianRatio*100);
        mOuterPaddingSeekBar.setProgress(mOuterPadding);
        mOuterPaddingTextView.setText(mOuterPadding + "");
        if (mSize > 1) {
            mInsidePaddingTextView.setText(mInsidePadding + "");
            mInsidePaddingSeekBar.setOnSeekBarChangeListener(mInsidePaddingSeekBarLinstener);
            mInsidePaddingSeekBar.setProgress(mInsidePadding);
        } else {
            insidePaddingLayout.setAlpha(0.2f);
            mInsidePaddingSeekBar.setEnabled(false);
            mInsidePaddingSeekBar.setProgress(0);
            mInsidePaddingTextView.setText(0 + "");
        }
        mRadianSeekBar.setProgress(mRadian);
        mRadianTextView.setText(mRadian + "");
    }

    public BottomEditLineFrameView(Context context) {
        super(context);
        mContext = context;
        initView(context);
        //initData();
    }

    private void initData() {
        int mSize = 5;
        int mInsidePadding = 50;
        int mOuterPadding = 50;
        int mRadian = 50;
        if (mSize == 1) {
            mInsidePaddingSeekBar.setEnabled(false);
        } else {
            mInsidePaddingSeekBar.setOnSeekBarChangeListener(mInsidePaddingSeekBarLinstener);
            mInsidePaddingSeekBar.setProgress((int) mInsidePadding);
        }

        mOuterPaddingSeekBar.setProgress((int) mOuterPadding);

        mRadianSeekBar.setProgress((int) mRadian);
    }

    private void initView(Context context) {
        ViewGroup.LayoutParams mParams;
        LinearLayout.LayoutParams lParams;
        LayoutParams rParams;

        mPageLayout = new RelativeLayout(mContext);
        mParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mPageLayout, mParams);
        {
            mPageLayout.setOnClickListener(this);

            mTotalLayout = new LinearLayout(mContext);
            //mTotalLayout.setBackgroundColor(0xff705e63);
            Rect rect = new Rect(0,0, Utils.getScreenW() , Utils.getScreenH());
            Rect dstRect = new Rect( 0 , Utils.getScreenH() - Utils.getRealPixel3(346) , Utils.getScreenW() , Utils.getScreenH());
            mTotalLayout.setBackgroundResource(R.drawable.bg_toolbar);

            rParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.getRealPixel3(346));
            rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mTotalLayout.setOrientation(LinearLayout.VERTICAL);
            mTotalLayout.setOnClickListener(null);
            mPageLayout.addView(mTotalLayout, rParams);
            {
                mBackbtn = new RelativeLayout(mContext);
                lParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.getRealPixel3(70));
                mBackbtn.setOnClickListener(this);
                mTotalLayout.addView(mBackbtn, lParams);
                {
                    // 向下推出颜色页面
                    ImageView colorBackIv = new ImageView(getContext());
                    rParams = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    colorBackIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    colorBackIv.setImageResource(R.drawable.exit_back_down);
                    rParams.addRule(CENTER_IN_PARENT);
                    mBackbtn.addView(colorBackIv, rParams);
                }

                LinearLayout parameterLayout = new LinearLayout(mContext);
                parameterLayout.setOrientation(LinearLayout.VERTICAL);
                lParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                parameterLayout.setOnClickListener(null);
                mTotalLayout.addView(parameterLayout, lParams);
                {

                    insidePaddingLayout = new LinearLayout(mContext);
                    lParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Utils.getRealPixel3(56));
                    lParams.setMargins(
                            Utils.getRealPixel3(64),
                            Utils.getRealPixel3(15), 0,
                            Utils.getRealPixel3(30));
                    insidePaddingLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
                    insidePaddingLayout.setOnClickListener(null);
                    parameterLayout.addView(insidePaddingLayout, lParams);
                    {
                        ImageView imageView = new ImageView(mContext);
                        imageView.setImageResource(R.drawable.icon_layout_padding_inside);
                        lParams = new LinearLayout.LayoutParams(
                                Utils.getRealPixel3(36),
                                Utils.getRealPixel3(36));
                        lParams.setMargins(0,
                                Utils.getRealPixel3(10), 0,
                                Utils.getRealPixel3(10));
                        insidePaddingLayout.addView(imageView, lParams);

                        RelativeLayout layout = new RelativeLayout(mContext);
                        lParams = new LinearLayout.LayoutParams(
                                Utils.getRealPixel3(56),
                                ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                        insidePaddingLayout.addView(layout, lParams);

                        rParams = new LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        rParams.addRule(CENTER_IN_PARENT);

                        mInsidePaddingSeekBar = new SeekBar(getContext());
                        mInsidePaddingSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style2));
                        mInsidePaddingSeekBar.setThumb(getResources().getDrawable(R.drawable.seekbar_thumb_selector_padding));
                        mInsidePaddingSeekBar.setMax(100);

                        mInsidePaddingSeekBar.setPadding(Utils.getRealPixel3(52), Utils.getRealPixel3(10),
                                Utils.getRealPixel3(122), Utils.getRealPixel3(10));

                        layout.addView(mInsidePaddingSeekBar, rParams);

                        mInsidePaddingTextView = new TextView(mContext);
                        mInsidePaddingTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                        mInsidePaddingTextView.setTextColor(0x66FFFFFF);
                        mInsidePaddingTextView.setGravity(Gravity.CENTER);
                        rParams = new LayoutParams(
                                Utils.getRealPixel3(112), ViewGroup.LayoutParams.MATCH_PARENT);
                        rParams.addRule(ALIGN_PARENT_RIGHT);
                        rParams.addRule(Gravity.CENTER_VERTICAL);
                        rParams.rightMargin = Utils.getRealPixel3(10);
                        layout.addView(mInsidePaddingTextView, rParams);
                    }

                    LinearLayout outerPaddingLayout = new LinearLayout(mContext);
                    outerPaddingLayout.setOnClickListener(null);
                    lParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(56));
                    lParams.setMargins(Utils.getRealPixel3(64), 0, 0, Utils.getRealPixel3(30));
                    outerPaddingLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
                    parameterLayout.addView(outerPaddingLayout, lParams);
                    {
                        ImageView imageView = new ImageView(mContext);
                        imageView.setImageResource(R.drawable.icon_layout_padding_outer);
                        lParams = new LinearLayout.LayoutParams(
                                Utils.getRealPixel3(36), Utils.getRealPixel3(36));
                        lParams.setMargins(0, Utils.getRealPixel3(10), 0, Utils.getRealPixel3(10));
                        outerPaddingLayout.addView(imageView, lParams);

                        RelativeLayout layout = new RelativeLayout(mContext);
                        lParams = new LinearLayout.LayoutParams(
                                Utils.getRealPixel3(56), ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                        outerPaddingLayout.addView(layout, lParams);

                        rParams = new LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rParams.addRule(CENTER_IN_PARENT);
                        mOuterPaddingSeekBar = new SeekBar(getContext());
                        mOuterPaddingSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style2));
                        mOuterPaddingSeekBar.setThumb(getResources().getDrawable(R.drawable.seekbar_thumb_selector_padding));
                        mOuterPaddingSeekBar.setMax(100);

                        mOuterPaddingSeekBar.setPadding(Utils.getRealPixel3(52), Utils.getRealPixel3(10),
                                Utils.getRealPixel3(122), Utils.getRealPixel3(10));
                        mOuterPaddingSeekBar.setOnSeekBarChangeListener(mOuterPaddingSeekBarLinstener);
                        layout.addView(mOuterPaddingSeekBar, rParams);

                        mOuterPaddingTextView = new TextView(mContext);
                        mOuterPaddingTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                        mOuterPaddingTextView.setTextColor(0x66FFFFFF);
                        mOuterPaddingTextView.setGravity(Gravity.CENTER);
                        rParams = new LayoutParams(
                                Utils.getRealPixel3(112), ViewGroup.LayoutParams.MATCH_PARENT);
                        rParams.addRule(ALIGN_PARENT_RIGHT);
                        rParams.addRule(Gravity.CENTER_VERTICAL);
                        rParams.rightMargin = Utils.getRealPixel3(10);
                        layout.addView(mOuterPaddingTextView, rParams);
                    }

                    LinearLayout radianLayout = new LinearLayout(mContext);
                    radianLayout.setOnClickListener(null);
                    lParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(56));
                    lParams.setMargins(Utils.getRealPixel3(64), 0, 0, Utils.getRealPixel3(33));
                    radianLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
                    parameterLayout.addView(radianLayout, lParams);
                    {
                        ImageView imageView = new ImageView(mContext);
                        imageView.setImageResource(R.drawable.icon_layout_radian);
                        lParams = new LinearLayout.LayoutParams(
                                Utils.getRealPixel3(36), Utils.getRealPixel3(36));
                        lParams.setMargins(0, Utils.getRealPixel3(10), 0, Utils.getRealPixel3(10));
                        radianLayout.addView(imageView, lParams);

                        RelativeLayout layout = new RelativeLayout(mContext);
                        lParams = new LinearLayout.LayoutParams(
                                Utils.getRealPixel3(56), ViewGroup.LayoutParams.WRAP_CONTENT, 1);

                        radianLayout.addView(layout, lParams);

                        rParams = new LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rParams.addRule(CENTER_IN_PARENT);
                        mRadianSeekBar = new SeekBar(getContext());
                        mRadianSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style2));
                        mRadianSeekBar.setThumb(getResources().getDrawable(R.drawable.seekbar_thumb_selector_padding));
                        mRadianSeekBar.setMax(100);

                        mRadianSeekBar.setPadding(Utils.getRealPixel3(52), Utils.getRealPixel3(10),
                                Utils.getRealPixel3(122), Utils.getRealPixel3(10));

                        mRadianSeekBar.setOnSeekBarChangeListener(mRadianSeekBarLinstener);
                        layout.addView(mRadianSeekBar, rParams);

                        mRadianTextView = new TextView(mContext);
                        mRadianTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                        mRadianTextView.setTextColor(0x66FFFFFF);
                        mRadianTextView.setGravity(Gravity.CENTER);
                        rParams = new LayoutParams(
                                Utils.getRealPixel3(112), ViewGroup.LayoutParams.MATCH_PARENT);
                        rParams.addRule(ALIGN_PARENT_RIGHT);
                        rParams.addRule(Gravity.CENTER_VERTICAL);
                        rParams.rightMargin = Utils.getRealPixel3(10);
                        layout.addView(mRadianTextView, rParams);
                    }
                }
            }
        }
    }

    private SeekBar.OnSeekBarChangeListener mOuterPaddingSeekBarLinstener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
            if (mOnParameChangedListener!=null){
                mOuterPadding = progress;
                mOnParameChangedListener.onChanged(CHANGEDMode.OUT_PADDING ,progress);
                mOuterPaddingTextView.setText(progress+"");
            }
            mOuterPaddingTextView.setText(progress + "");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private SeekBar.OnSeekBarChangeListener mInsidePaddingSeekBarLinstener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
            if (mOnParameChangedListener!=null){
                mInsidePadding = progress;
                mOnParameChangedListener.onChanged(CHANGEDMode.IN_PADDING ,progress);
                mInsidePaddingTextView.setText(progress+"");
            }
            mInsidePaddingTextView.setText(progress + "");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private SeekBar.OnSeekBarChangeListener mRadianSeekBarLinstener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
            if (!isDismiss){
                if (mOnParameChangedListener!=null){
                    mRadian = progress;
                    mOnParameChangedListener.onChanged(CHANGEDMode.RADIAN ,progress);
                    mRadianTextView.setText(progress+"");
                }
                mRadianTextView.setText(progress + "");
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!isDismiss){
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view == mBackbtn) {
            isDismiss = false;
            onBack();
        } else if (view == mPageLayout) {
            onBack();
        }
    }

    public void onBack() {
        if ( !isDismiss ) {
            isDismiss = true;
            if (mOnParameChangedListener!=null){
                mOnParameChangedListener.onDismiss();
            }
            AnimUtils.setTransAnim(this, 0, 0, 0, 1, 500, new AnimUtils.AnimEndCallBack() {
                @Override
                public void endCallBack(Animation animation) {
                    BottomEditLineFrameView.this.setVisibility(GONE);
                }
            });
        }
    }

    public boolean isDismiss() {
        return isDismiss;
    }

    public void setDismiss(boolean dismiss) {
        isDismiss = dismiss;
    }

    private boolean isDismiss; // 当前View是否完成退出动画 ， 完成退出动画才可以下一次弹出，做完了弹出动画才可以退出
}
