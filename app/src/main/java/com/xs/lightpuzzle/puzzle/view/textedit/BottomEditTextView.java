package com.xs.lightpuzzle.puzzle.view.textedit;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.puzzle.config.AppConfiguration;
import com.xs.lightpuzzle.puzzle.data.editdata.TemporaryTextData;
import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;
import com.xs.lightpuzzle.puzzle.util.AnimUtils;
import com.xs.lightpuzzle.puzzle.util.NoDoubleClickListener;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.textedit.textinterface.OnInputListener;
import com.xs.lightpuzzle.puzzle.view.textedit.util.SoftKeyboardStateWatcher;
import com.xs.lightpuzzle.puzzle.view.textedit.view.TextEditTotalView;

import java.util.HashMap;

/**
 * Created by urnot_XS on 2018/4/20.
 * 文字编辑
 */

public class BottomEditTextView extends RelativeLayout {
    // 编辑模板文本
    public static final int EDIT_TEMPLATE_TEXT = 0x3366;
    //添加用户输入的文本
    public static final int ADD_TEXT = 0x3399;

    public static boolean IS_DEBUG = false;



    private RelativeLayout mPageView;
    private TextEditTotalView mTotalView;
    private SoftKeyboardStateWatcher mSoftKeyboardStateWatcher;

    private int mEditModel;
    private String mInputText;
    private TemporaryTextData mTemporaryTextData;

    public interface OnEditInteractionListener {
        void onTranslate(String originalText);

        void changeText(int textMode, TemporaryTextData temporaryTextData, String text);

        void changeSize(int textMode, TemporaryTextData temporaryTextData, float size);

        void changeFont(int textMode, TemporaryTextData temporaryTextData, String font, boolean down);

        void changeColor(int textMode, TemporaryTextData temporaryTextData, String color);

        void save(int textMode, TemporaryTextData temporaryTextData, String text);

        void onClose();
    }

    private OnEditInteractionListener mEditInteractionListener;

    public void setEditInteractionListener(OnEditInteractionListener listener) {
        mEditInteractionListener = listener;
    }

    public void unLockVipColor() {
        mTotalView.getBottomView().unLockVip();

    }

    public void open(int editModel, TemporaryTextData temporaryTextData) {
        mEditModel = editModel;
        mTemporaryTextData = temporaryTextData;// 包含：字体，颜色，大小，最大，最小，字体大小相对的宽高值计算缩放比
        initFackData();
        refreshView();
    }

    public void showView() {
        this.setVisibility(VISIBLE);
    }

    public BottomEditTextView(Context context) {
        super(context);
        initView(context);
        mSoftKeyboardStateWatcher = new SoftKeyboardStateWatcher(this, (Activity) getContext());
        mSoftKeyboardStateWatcher.addSoftKeyboardStateListener(mSoftKeyboardStateListener);
    }

    private void initView(Context context) {
        LayoutParams rlParams;
        mPageView = new RelativeLayout(context);
        rlParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onBack();
            }
        });
        this.addView(mPageView, rlParams);
        {
            //编辑文字控件
            rlParams = new LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mTotalView = new TextEditTotalView(context);
            //设置高斯模糊背景。。。
            mPageView.addView(mTotalView, rlParams);
        }
    }

    public void refreshTextFromTrans(HashMap<String, Object> params) {
        if (params != null) {
            mTotalView.getTopView().setText((String) params.get("translateText"));
        }
        mTotalView.getTopView().showSoft();
    }

    /**
     * 模板文字
     */
    public void refreshView() {

        mTotalView.setEditModel(mEditModel);
        mTotalView.setOnInputListener(mInputListener);

        //底部UI数据
        mTotalView.getBottomView().setTextInfo(mTemporaryTextData);

        //顶部UI
        mInputText = mTemporaryTextData.getTextData().getAutoStr();
        mTotalView.getTopView().setText(mInputText);
        // 设置输入框获取焦点
        mTotalView.getTopView().setIconInputVisible(View.GONE);
        mTotalView.getTopView().setIconOperateVisible(View.VISIBLE);
        mTotalView.getTopView().showSoft();

        if (mEditInteractionListener != null) {
            mTotalView.getTopView().setEditInteractionListener(mEditInteractionListener);
        }
    }

    private void initFackData() {
        TextData textData = new TextData();

        textData.setAutoStr("你好·时光");
        textData.setFont("zzgfshG0v1xt.otf");
        textData.setLayoutWidth(1152);
        textData.setFontColor(PuzzlesUtils.strColor2Int("000000"));
        float fontScale = ((float) 1014 / (float) textData.getLayoutWidth());
        textData.setMinFontSize(60);
        textData.setMaxFontSize(80);
        textData.setFontSize((int) ((textData.getMaxFontSize() / 2) * fontScale));
        textData.setMaxFontSize((int) ((textData.getMaxFontSize() / 2) * fontScale));
        textData.setMinFontSize((int) ((textData.getMinFontSize() / 2) * fontScale));
        if (mTemporaryTextData == null) {
            mTemporaryTextData = new TemporaryTextData(textData, null);
        }
    }

    private OnInputListener mInputListener = new OnInputListener() {

        @Override
        public void onSave(String text) {
            if (mEditInteractionListener != null) {
                mEditInteractionListener.save(mEditModel, mTemporaryTextData, text);
            }
            onBack();
        }

        @Override
        public void changeText(String text) {
            mEditInteractionListener.changeText(mEditModel, mTemporaryTextData, text);
        }

        @Override
        public void onopenText() {
            hideSoftInput();
        }

        @Override
        public void changeSize(float size) {
            mEditInteractionListener.changeSize(mEditModel, mTemporaryTextData, size);
        }

        @Override
        public void changeFont(String font, boolean down) {
            mEditInteractionListener.changeFont(mEditModel, mTemporaryTextData, font, down);
        }

        @Override
        public void changeColor(String color) {
            mEditInteractionListener.changeColor(mEditModel, mTemporaryTextData, color);
        }

        @Override
        public void openInputMethod() {
            showSoftInput();

        }

        @Override
        public void changeDownFont(boolean down) {

        }
    };

    public void onBack() {
        hideSoftInput();
        AnimUtils.setTransAnim(this, 0, 0, 0, 1, 500, new AnimUtils.AnimEndCallBack() {
            @Override
            public void endCallBack(Animation animation) {
                mTemporaryTextData = null;
                BottomEditTextView.this.setVisibility(GONE);
                if (mEditInteractionListener != null) {
                    mEditInteractionListener.onClose();
                }
            }
        });
    }


    private SoftKeyboardStateWatcher.SoftKeyboardStateListener mSoftKeyboardStateListener =
            new SoftKeyboardStateWatcher.SoftKeyboardStateListener() {

                @Override
                public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                    Log.d("EditTextPage", "onSoftKeyboardOpened");

                    int height = keyboardHeightInPx;
                    if (height > 0) {
                        mTotalView.setContentHeight(height);
                    }
                    mTotalView.getTopView().setIconOperateVisible(View.VISIBLE);
                    mTotalView.getTopView().setIconInputVisible(View.GONE);
                }

                @Override
                public void onSoftKeyboardChange(int keyboardHeightInPx) {
                    Log.d("EditTextPage", "onSoftKeyboardChange");
                    int height = keyboardHeightInPx;
                    if (mTotalView.getVisibility() == VISIBLE && height > 0) {
                        mTotalView.setContentHeight(height);
                    }
                }

                @Override
                public void onSoftKeyboardClosed() {

                    Log.d("EditTextPage", "onSoftKeyboardClosed");
                    mTotalView.setContentHeight(AppConfiguration.getDefault().getKeyboardHeight());

                    mTotalView.getTopView().setIconOperateVisible(View.GONE);
                    mTotalView.getTopView().setIconInputVisible(View.VISIBLE);
                }
            };

    private Handler mHandler = new Handler(Looper.getMainLooper());

    // 打开系统软键盘
    private void showSoftInput() {

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        }, 300);
    }

    // 收起软键盘
    private void hideSoftInput() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Utils.hideKeyboard(getContext(), getApplicationWindowToken());
            }
        }, 200);
    }

}
