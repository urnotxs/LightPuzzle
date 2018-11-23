package com.xs.lightpuzzle.puzzle.view.signature;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.texturecolor.widget.ColorImageButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xs on 2018/8/23.
 */

public class SignaturePage extends FrameLayout {

    private Context mContext;
    private RelativeLayout mBtCloseRl;
    private ImageButton mBtClose;
    private RelativeLayout mBtSureRl;
    private ImageButton mBtSure;

    private RelativeLayout mBtEraserRl;
    private ImageButton mBtEraser;

    private RelativeLayout mBtColorRl;
    private ImageButton mBtColor;
    private RelativeLayout mBtHistoryRl;
    private ImageButton mBtHistory;

    private SignaturePanel mSignaturePad;
    private String mRestorePath;

    //颜色选择面板
    private FrameLayout mWrapPop;      // 包裹着颜色选择板
    private RelativeLayout mContainer;// 选择板
    private ImageButton mBtBack;
    private LinearLayout mColorPanel;
    private ArrayList<ColorImageButton> cirColorBts;
    private ColorImageButton bt;

    private Bitmap mBgBmp;
    private Bitmap mPopBgBmp;

    /**
     * 签名历史
     */
    private MyHorizontalScrollView mHistoryHorizontalScrollView;
    private HistoryPanel mHistoryPanel;

    /**
     * 当前颜色指针
     */
    private int colorIndex = -1;
    private int repeatIndex = -1;


    private int[] mColorArrRes = {
            0xffbcbcbc, 0xff44abb4, 0xffc45e6b, 0xff3c6b8c,
            0xff0f2446, 0xff000000, 0xffe0d8c2, 0xffbdd8cf,
            0xffc26400, 0xff6c3b4c, 0xff5f4239, 0xffffffff
    };
    /**
     * 颜色表的映射
     */
    private int colorAry[] = {0xFFBCBCBC, 0xFF44ABB4, 0xFFC45E6B, 0xFF3C6B8C,
            0xFF0F2446, 0xFF000000, 0xFFE0D8C2, 0xFFBDD8CF, 0xFFC26400,
            0xFF6C3B4C, 0xFF5F4239, 0xFFFFFFFF};

    String SIGNATURE_PATH = "signature_path";
    SignatureActivity.PageListener mListener;

    public SignaturePage(Context context, Intent intent, SignatureActivity.PageListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        mRestorePath = intent.getStringExtra(SIGNATURE_PATH);
        initView(context);
    }

    public void onBack() {
        deleteEditDirAfterNoHistory();
    }

    public void initView(Context context) {
        initUI(mRestorePath);
    }


    //容器面板动画
    private boolean isColor = false;

    private void hideContainerBar() {
        if (mContainer.getVisibility() == View.VISIBLE) {
            if (isColor) {
                mColorPanel.setVisibility(View.INVISIBLE);
            } else {
                mHistoryHorizontalScrollView.setVisibility(View.INVISIBLE);
            }
            mContainer.setVisibility(View.INVISIBLE);
            mWrapPop.setVisibility(View.INVISIBLE);
            mHistoryPanel.hideDeleteControl();//隐藏删除控件
            if (!mHistoryPanel.hasItem()) mBtHistoryRl.setAlpha(0.4f);//是否可用
        }
    }

    private void openContainerBar() {
        mWrapPop.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.VISIBLE);
        if (isColor) {
            mColorPanel.setVisibility(View.VISIBLE);
        } else {
            mHistoryHorizontalScrollView.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mHistoryPanel.getItem() == 5) {
                        mHistoryHorizontalScrollView.smoothScrollTo(
                                mHistoryPanel.getWidth() - mHistoryHorizontalScrollView.getWidth(), 0);
                    }
                }
            });
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mBtEraserRl) {
                startEraserAnimation();
                mSignaturePad.clear();
            } else if (v == mBtColorRl) {
                isColor = true;
                openContainerBar();
            } else if (v == mBtHistoryRl) {
                if (mHistoryPanel.hasItem()) {
                    isColor = false;
                    openContainerBar();
                }
            } else if (v == mBtCloseRl) {
                release();//释放
                deleteEditDirAfterNoHistory();
                // TODO: 2018/11/23
                if (mListener != null) {
                    mListener.onClickBackBtn(mHistoryPanel.getItem() != 0);
                }
//                mSite.onBack(getContext(), mHistoryPanel.getItem() != 0);
            } else if (v == mBtSureRl) {

                // 若历史第0个被删除，则签名被改变过，要重新保存
                if (mHistoryPanel.getIsDeleteFrist()) {
                    mSignaturePad.setChange(true);
                }

                deleteEditDirAfterNoHistory();

                mHistoryPanel.release(); // 历史先释放，再保存

                boolean isSave = mSignaturePad
                        .saveTransparentSignatureArray();

                String picPath = null;
                if (isSave) {
                    picPath = mSignaturePad.getSavePicPath();
                    if (mSignaturePad.hasChanged()) {
                        mHistoryPanel.deleteSignatureFile();// 是否超出容量
                        mBtHistoryRl.setAlpha(1.0f);// 不透明
                    }
                    isColor = false;
                }
                release();//释放

                picPath = SignatureUtils.copySelectSignature2EditingDir(picPath);
                if (mListener != null) {
                    mListener.onClickOkBtn(picPath);
                }
            } else if (v instanceof ColorImageButton) {
                if (bt != v) {
                    bt.setCheckState(false);
                    bt = (ColorImageButton) v;
                    bt.setCheckState(true);
                    colorIndex = bt.getIndex();
                    mSignaturePad.changeSignatureColor(colorAry[colorIndex], true);
                }
            } else if (v == mWrapPop || v == mBtBack) {
                hideContainerBar();
            }
        }
    };

    public void onClickOk(Context context, String picPath) {
        // TODO: 2018/11/22  
        picPath = SignatureUtils.copySelectSignature2EditingDir(picPath);
        HashMap<String, Object> params = new HashMap<>();
        params.put(SIGNATURE_PATH, picPath);

    }

    private void deleteEditDirAfterNoHistory() {
        if (mHistoryPanel.getItem() == 0) {
            SignatureUtils.clearEditingDir();
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (v == mBtColorRl) {
                    mBtColorRl.setAlpha(0.5f);
                } else if (v == mBtHistoryRl) {
                    mBtHistoryRl.setAlpha(0.5f);
                } else if (v == mBtCloseRl) {
                    mBtCloseRl.setAlpha(0.5f);
                } else if (v == mBtSureRl) {
                    mBtSureRl.setAlpha(0.5f);
                }
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (v == mBtColorRl) {
                    mBtColorRl.setAlpha(1.0f);
                } else if (v == mBtHistoryRl) {
                    if (mHistoryPanel.hasItem())
                        mBtHistoryRl.setAlpha(1.0f);
                } else if (v == mBtCloseRl) {
                    mBtCloseRl.setAlpha(1.0f);
                } else if (v == mBtSureRl) {
                    mBtSureRl.setAlpha(1.0f);
                }
            }
            return false;
        }
    };

    /**
     * 橡皮檫动画
     */
    private void startEraserAnimation() {
        mBtEraser.setBackgroundResource(R.drawable.signature_eraser_select);
        AnimationSet as;
        as = new AnimationSet(true);
        TranslateAnimation ta;
        ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                0.406f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(100);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtEraser.setBackgroundResource(R.drawable.ic_signature_eraser_no_select);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        as.addAnimation(ta);
        as.setFillAfter(false);
        mBtEraserRl.startAnimation(as);
    }

    private void initUI(String signatureRestorePath) {
        setBackgroundResource(R.drawable.bg_toolbar);
        RelativeLayout.LayoutParams params;

        RelativeLayout root = new RelativeLayout(getContext());
        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(root, params);

        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mSignaturePad = new SignaturePanel(mContext, signatureRestorePath);
        root.addView(mSignaturePad, params);

        params = new RelativeLayout.LayoutParams(
                Utils.getRealPixel3(96), Utils.getRealPixel3(84));
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mBtCloseRl = new RelativeLayout(mContext);
        root.addView(mBtCloseRl, params);
        mBtCloseRl.setOnClickListener(mOnClickListener);
        mBtCloseRl.setOnTouchListener(mOnTouchListener);
        {
            params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mBtClose = new ImageButton(mContext);
            mBtCloseRl.addView(mBtClose, params);
            mBtClose.setBackgroundResource(R.drawable.ic_signature_back);
            mBtClose.setClickable(false);
        }

        params = new RelativeLayout.LayoutParams(
                Utils.getRealPixel3(84), Utils.getRealPixel3(124));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mBtSureRl = new RelativeLayout(mContext);
        root.addView(mBtSureRl, params);
        mBtSureRl.setOnClickListener(mOnClickListener);
        mBtSureRl.setOnTouchListener(mOnTouchListener);
        {
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mBtSure = new ImageButton(mContext);
            mBtSureRl.addView(mBtSure, params);
            mBtSure.setBackgroundResource(R.drawable.ic_signature_sure);
            mBtSure.setClickable(false);
        }

        params = new RelativeLayout.LayoutParams(
                Utils.getRealPixel3(143), Utils.getRealPixel3(113));
        params.topMargin = Utils.getRealPixel3(16);
        params.leftMargin = Utils.getRealPixel3(-50);
        mBtEraserRl = new RelativeLayout(mContext);
        root.addView(mBtEraserRl, params);
        mBtEraserRl.setOnClickListener(mOnClickListener);
        {
            params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            mBtEraser = new ImageButton(mContext);
            mBtEraserRl.addView(mBtEraser, params);
            mBtEraser.setBackgroundResource(R.drawable.ic_signature_eraser_no_select);
            mBtEraser.setClickable(false);
        }

        params = new RelativeLayout.LayoutParams(
                Utils.getRealPixel3(106), Utils.getRealPixel3(126));
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = Utils.getRealPixel3(23);
        mBtHistoryRl = new RelativeLayout(mContext);
        root.addView(mBtHistoryRl, params);
        mBtHistoryRl.setOnClickListener(mOnClickListener);
        mBtHistoryRl.setOnTouchListener(mOnTouchListener);
        mBtHistoryRl.setId(R.id.signature_page_history);
        mBtHistoryRl.setAlpha(0.4f);//透明
        {
            params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mBtHistory = new ImageButton(mContext);
            mBtHistoryRl.addView(mBtHistory, params);
            mBtHistory.setBackgroundResource(R.drawable.ic_signature_history);
            mBtHistory.setClickable(false);
        }

        params = new RelativeLayout.LayoutParams(
                Utils.getRealPixel3(106), Utils.getRealPixel3(120));

        params.addRule(RelativeLayout.ABOVE, R.id.signature_page_history);
        params.bottomMargin = Utils.getRealPixel3(21);
        mBtColorRl = new RelativeLayout(mContext);
        root.addView(mBtColorRl, params);
        mBtColorRl.setOnClickListener(mOnClickListener);
        mBtColorRl.setOnTouchListener(mOnTouchListener);
        {
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mBtColor = new ImageButton(mContext);
            mBtColorRl.addView(mBtColor, params);
            mBtColor.setBackgroundResource(R.drawable.ic_signature_color);
            mBtColor.setClickable(false);
        }

        // 字体选色页面
        // 外包工具栏
        mWrapPop = new FrameLayout(getContext());
        FrameLayout.LayoutParams frc = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        mWrapPop.setVisibility(View.GONE);
        mWrapPop.setOnClickListener(mOnClickListener);
        root.addView(mWrapPop, frc);

        // 容器面板
        FrameLayout.LayoutParams cf = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cf.gravity = Gravity.BOTTOM;
        mContainer = new RelativeLayout(mContext);
        mWrapPop.addView(mContainer, cf);
        if (BitmapHelper.isValid(mPopBgBmp)) {
            mContainer.setBackgroundDrawable(new BitmapDrawable(getResources(), mPopBgBmp));
        } else {
            mContainer.setBackgroundColor(0xff715f5f);
        }
        mContainer.setVisibility(View.INVISIBLE);
        mContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHistoryPanel.hideDeleteControl();//隐藏删除控件
            }
        });

        // 增加返回键
        params = new RelativeLayout.LayoutParams(
                Utils.getRealPixel3(80), Utils.getRealPixel3(80));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mBtBack = new ImageButton(mContext);
        mContainer.addView(mBtBack, params);
        mBtBack.setBackgroundResource(R.drawable.ic_signature_back);
        mBtBack.setId(R.id.signature_page_back);
        mBtBack.setOnClickListener(mOnClickListener);

        // 颜色面板
        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.signature_page_back);
        params.topMargin = Utils.getRealPixel3(43);
        mColorPanel = new LinearLayout(mContext);
        mContainer.addView(mColorPanel, params);
        mColorPanel.setOrientation(LinearLayout.VERTICAL);
        mColorPanel.setVisibility(View.GONE);
        {
            // 颜色第一行
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(150));
            lparams.gravity = Gravity.CENTER_HORIZONTAL;
            LinearLayout listColor1 = new LinearLayout(mContext);
            mColorPanel.addView(listColor1, lparams);
            listColor1.setOrientation(LinearLayout.HORIZONTAL);

            // 颜色第二行
            lparams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lparams.topMargin = 10;
            lparams.bottomMargin = 43;
            lparams.gravity = Gravity.CENTER_HORIZONTAL;
            LinearLayout listColor2 = new LinearLayout(mContext);
            mColorPanel.addView(listColor2, lparams);
            listColor2.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    (int) ((Utils.getScreenW() - 18) / 6f), RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.CENTER;
            params1.weight = 1;
            cirColorBts = new ArrayList<>();
            for (int i = 0; i < mColorArrRes.length; i++) {
                bt = new ColorImageButton(mContext, mColorArrRes[i], i);
                bt.setId(i);
                cirColorBts.add(bt);
                if (i >= 0 && i < 6) {
                    listColor1.addView(bt, params1);
                } else if (i >= 0 && i < 12) {
                    listColor2.addView(bt, params1);
                }
                bt.setOnClickListener(mOnClickListener);
            }
            // 默认选择黑色
            colorIndex = 5;
            for (int i = 0; i < colorAry.length; i++) {
                int color = mSignaturePad.getPaintColor();
                if (color == colorAry[i]) {
                    colorIndex = i;
                    break;
                }
            }
            bt = cirColorBts.get(colorIndex);
            bt.setCheckState(true);
        }

        // 签名历史
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Utils.getRealPixel3(5),
                Utils.getRealPixel3(51), Utils.getRealPixel3(5), 0);
        params.addRule(RelativeLayout.BELOW, R.id.signature_page_back);

        mHistoryHorizontalScrollView = new MyHorizontalScrollView(mContext);
        mContainer.addView(mHistoryHorizontalScrollView, params);
        mHistoryHorizontalScrollView.setHorizontalScrollBarEnabled(false);
        mHistoryHorizontalScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mHistoryHorizontalScrollView.setVisibility(View.GONE);
        {
            params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHistoryPanel = new HistoryPanel(mContext, mListener);
            mHistoryHorizontalScrollView.addView(mHistoryPanel, params);
            mHistoryHorizontalScrollView.onFinishAddView(mHistoryPanel);
            if (mHistoryPanel.hasItem()) mBtHistoryRl.setAlpha(1.0f);//设置历史透明度
        }

        mHistoryHorizontalScrollView.setOnScrollStateChangedListener(
                new MyHorizontalScrollView.ScrollViewListener() {

                    @Override
                    public void onScrollChanged(MyHorizontalScrollView.ScrollType scrollType) {
                        if (scrollType == MyHorizontalScrollView.ScrollType.IDLE) {
                            if (mHistoryPanel.getItem() < 4) {
                                mHistoryHorizontalScrollView.smoothScrollTo(0, 0);
                            }
                        }
                    }
                });
    }

    /**
     * 释放内存
     */
    public void release() {
        mSignaturePad.clearBitmap();
        mHistoryPanel.release();
    }
}
