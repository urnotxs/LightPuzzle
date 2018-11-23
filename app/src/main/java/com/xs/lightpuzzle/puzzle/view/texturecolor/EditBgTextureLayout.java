package com.xs.lightpuzzle.puzzle.view.texturecolor;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby3.mvp.layout.MvpRelativeLayout;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.AnimUtils;
import com.xs.lightpuzzle.puzzle.util.NoDoubleClickListener;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.ElasticHorizontalScrollView;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleBackgroundBean;
import com.xs.lightpuzzle.puzzle.view.texturecolor.widget.TextureImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/4/18.
 */

public class EditBgTextureLayout
        extends MvpRelativeLayout<EditBgTextureView, EditBgTexturePresenter>
        implements EditBgTextureView {

    private View mBlank;
    private LinearLayout mChangeBgTextureLayout;
    private RelativeLayout mBackbtn;
    private RelativeLayout mColorBackRl;

    private ColorLayout mColorLayout;
    private TextureLayout mTextureLayout;

    private TextureOrColorListener mListener;

    private ClickTextureListener mClickTextureListener;
    private ColorLayout.ClickColorListener mClickColorListener;

    private ViewCloseCallback mViewCloseCallback;

    // 编辑页传进来的值
    private String mTexture; // 纹理资源
    private String mBgColor; // 背景颜色

    private boolean isOnAttached;

    public EditBgTextureLayout(Context context, String texture,
                               int bgColor, ViewCloseCallback viewCloseCallback) {
        super(context);
        mTexture = texture;
        mBgColor = intColor2HexString(bgColor);
        mViewCloseCallback = viewCloseCallback;
        initUI();
    }

    @Override
    public EditBgTexturePresenter createPresenter() {
        if (!isOnAttached) {
            return new EditBgTexturePresenter();
        } else {
            return getPresenter();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isOnAttached) {
            isOnAttached = true;
            getPresenter().loadUiDatas(mTexture, mBgColor);
        }
    }

    @Override
    public void setUiDatas(int[] bgColors, int[] textureResIds, boolean[] isVips) {
        addMainView(bgColors, textureResIds, isVips);

        int textureIndex = getPresenter().getTextureIndex();
        if (textureIndex != -1) {
            mTextureLayout.setIndex(textureIndex);
        } else {
            mTextureLayout.setIndex(0);
        }

        int colorIndex = getPresenter().getColorIndex();
        if (colorIndex != -1) {
            mColorLayout.setIndex(colorIndex);
        }

        initListener();
    }

    private String intColor2HexString(int color) {
        String colorStr = Integer.toHexString(color);
        if (!TextUtils.isEmpty(colorStr)
                && colorStr.length() == 8) {
            colorStr = colorStr.substring(2);
        }
        return colorStr;
    }

    public void unLock() {
        mTextureLayout.unlock();
        mColorLayout.unLock();
    }

    @Override
    public void getPuzzleBackgroundBean(PuzzleBackgroundBean puzzleBackgroundBean) {
        if (mListener != null) {
            mListener.onChange(puzzleBackgroundBean);
        }
    }

    private void initListener() {
        mClickColorListener = new ColorLayout.ClickColorListener() {
            @Override
            public void clickCallback(int index) {

                if (getPresenter().getColorIndex() != index) {
                    getPresenter().setColorIndex(index);
                    getPresenter().loadPuzzleBackgroundBean();
                }
            }
        };
        mColorLayout.setColorClickCallback(mClickColorListener);

        mClickTextureListener = new ClickTextureListener() {
            @Override
            public void clickCallback(int index) {

                if (getPresenter().getTextureIndex() != index) {
                    getPresenter().setTextureIndex(index);
                    getPresenter().loadPuzzleBackgroundBean();
                }
            }
        };

        mTextureLayout.setClickTextureListener(mClickTextureListener);
    }

    private void addMainView(int[] bgColors, int[] textureResIds, boolean[] isVips) {
        LinearLayout.LayoutParams lParams = null;
        // 中间选择颜色
        mColorLayout = new ColorLayout(getContext(), bgColors, ColorLayout.BG_COLOR);
        lParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(288));
        mChangeBgTextureLayout.addView(mColorLayout, lParams);

        lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextureLayout = new TextureLayout(getContext(), textureResIds, isVips);
        lParams.topMargin = Utils.getRealPixel3(10);
        mChangeBgTextureLayout.addView(mTextureLayout, lParams);

        AnimUtils.setTransAnim(this, 0,
                0, 1, 0, 500, null);
    }

    private OnClickListener mOnClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            if (v == mBackbtn || v == mBlank || v == mColorBackRl) {
                exit();
            }
        }
    };

    public class TextureLayout extends RelativeLayout {
        // 底部纹理效果滚动条资源
        private int mTextureRes[], mTextureResHover[];
        private boolean[] isVips;
        private ElasticHorizontalScrollView mTextureSrolll;

        private int mIndex;
        private TextureImageButton mLastTextureIcon;
        private List<TextureImageButton> mAllTextureIcons = new ArrayList<>();

        public TextureLayout(Context context, int[] textureRes, boolean[] isVips) {
            super(context);
            this.isVips = isVips;
            int textureLen = textureRes.length / 2;
            mTextureRes = new int[textureLen];
            mTextureResHover = new int[textureLen];
            for (int i = 0; i < textureRes.length; i++) {
                if (i % 2 == 0) {
                    mTextureRes[i / 2] = textureRes[i];
                } else {
                    mTextureResHover[i / 2] = textureRes[i];
                }
            }
            initUI();
        }

        private void initUI() {
            LayoutParams lParams;
            // 底部背景纹路
            mTextureSrolll = new ElasticHorizontalScrollView(getContext());
            lParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 不显示滑到初始和滑到终点时不出现越界阴影
            mTextureSrolll.setOverScrollMode(View.OVER_SCROLL_NEVER);
            // 设置隐藏滑动条
            mTextureSrolll.setHorizontalScrollBarEnabled(false);
            this.addView(mTextureSrolll, lParams);
            {
                LinearLayout effLin = new LinearLayout(getContext());
                lParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                effLin.setOrientation(LinearLayout.HORIZONTAL);
                mTextureSrolll.addView(effLin, lParams);
                mTextureSrolll.onFinishAddView(effLin);
                {
                    // 依次添加纹理控件
                    for (int i = 0; i < mTextureRes.length; i++) {
                        LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        TextureImageButton cBt = new TextureImageButton(getContext(),
                                mTextureRes[i], mTextureResHover[i]);
                        iParams.leftMargin = Utils.getRealPixel3(4);
                        effLin.addView(cBt, iParams);

                        // info
//                        if (isVips[i] && !AppConfiguration.getDefault().isPurchaseColorful()) {
//                            cBt.setLockVisible(View.VISIBLE);
//                        }
                        cBt.setIndex(i);
                        cBt.setOnClickListener(mOnClickListener);
                        mAllTextureIcons.add(cBt);
                    }
                }
            }
        }

        private NoDoubleClickListener mOnClickListener = new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mLastTextureIcon != v) {
                    TextureImageButton textureImageButton = (TextureImageButton) v;
                    if (textureImageButton.islock()) {
//                        VipDialogSite.popup(EditBgTextureLayout.this.getContext(), VipDialog.DESC.BG_TEXTURE,
//                                JaneStyleBlur.getBlurBackground(getContext()), new JaneCallbackAdapter() {
//                                    @Override
//                                    public void onComplete(Object o, int code, Bundle data) {
//                                        if (code == JaneCallbackCode.SUCCESS
//                                                && AppConfiguration.getDefault().isPurchaseColorful()) {
//                                            mTextureLayout.unlock();
//                                            mColorLayout.unLock();
//                                        }
//                                    }
//                                });
//                        return;
                    }
                    if (mLastTextureIcon != null) {
                        mLastTextureIcon.setCheck(false);
                    }

                    textureImageButton.setCheck(true);

                    if (mClickTextureListener != null) {
                        mClickTextureListener.clickCallback(textureImageButton.getIndex());
                    }

                    mLastTextureIcon = (TextureImageButton) v;
                }
            }
        };

        public void setIndex(int index) {
            if (mIndex == -1) {
                return;
            }
            mIndex = index;
            mLastTextureIcon = mAllTextureIcons.get(mIndex);
            mLastTextureIcon.setCheck(true);
            final int scrollX = (mIndex - 1) *
                    (Utils.getRealPixel3(114) + Utils.getRealPixel3(20)) - Utils.getScreenW();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextureSrolll.scrollTo((int) mLastTextureIcon.getX() - Utils.getScreenW() / 2, 0);
                }
            }, 500);
        }

        public void unlock() {
            for (TextureImageButton button : mAllTextureIcons) {
                if (button.islock()) {
                    button.setLockVisible(View.INVISIBLE);
                }
            }
        }

        private ClickTextureListener mClickTextureListener;

        public void setClickTextureListener(ClickTextureListener clickTextureListener) {
            mClickTextureListener = clickTextureListener;
        }

    }

    public interface ClickTextureListener {
        void clickCallback(int index);
    }

    private void initUI() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout root = new RelativeLayout(getContext());
        this.addView(root, params);

        LayoutParams rParams;
        rParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                Utils.getRealPixel3(862));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mBlank = new View(getContext());
        root.addView(mBlank, rParams);
        mBlank.setBackgroundColor(Color.TRANSPARENT);
        mBlank.setOnClickListener(mOnClickListener);

        mChangeBgTextureLayout = new LinearLayout(getContext());
        rParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                Utils.getRealPixel3(438));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mChangeBgTextureLayout.setOrientation(LinearLayout.VERTICAL);
        // 设置截图高斯模糊背景
//        Drawable cutDrawable = JaneStyleBlur.getBlurBackgroundPartDrawable(getContext(),
//                new Rect(0, 0, Utils.getScreenW(), Utils.getScreenH()),
//                new Rect(0, Utils.getScreenH() - rParams.height,
//                        Utils.getScreenW(), Utils.getScreenH()));
        mChangeBgTextureLayout.setBackgroundResource(R.drawable.bg_toolbar);
        root.addView(mChangeBgTextureLayout, rParams);
        {
            mBackbtn = new RelativeLayout(getContext());
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(50));
            mBackbtn.setOnClickListener(mOnClickListener);
            mChangeBgTextureLayout.addView(mBackbtn, lParams);
            {

                RelativeLayout bottomRl = new RelativeLayout(getContext());
                rParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.getRealPixel3(438));
                rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bottomRl.setBackgroundColor(Color.TRANSPARENT);
                root.addView(bottomRl, rParams);

                rParams = new LayoutParams(
                        Utils.getRealPixel3(200), Utils.getRealPixel3(100));
                rParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mColorBackRl = new RelativeLayout(getContext());
                bottomRl.addView(mColorBackRl, rParams);
                mColorBackRl.setOnClickListener(mOnClickListener);

                // 向下推出颜色页面
                ImageView colorBackIv = new ImageView(getContext());
                rParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rParams.topMargin = Utils.getRealPixel3(28);
                colorBackIv.setImageResource(R.drawable.ic_exit_color_back_down);
                mColorBackRl.addView(colorBackIv, rParams);
            }
        }
    }

    private void exit() {
        AnimUtils.setTransAnim(EditBgTextureLayout.this,
                0, 0, 0, 1, 500, new AnimUtils.AnimEndCallBack() {
                    @Override
                    public void endCallBack(Animation animation) {
                        if (mViewCloseCallback != null) {
                            mViewCloseCallback.close();
                        }
                    }
                });
    }

    public void onBack() {
        exit();
    }

    public void setListener(TextureOrColorListener listener) {
        mListener = listener;
    }

    public interface TextureOrColorListener {
        void onChange(PuzzleBackgroundBean backgroundBean);
    }
}
