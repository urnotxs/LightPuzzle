package com.xs.lightpuzzle.puzzle.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xs.lightpuzzle.R;

import java.util.ArrayList;
import java.util.List;

public class UIAlertViewDialog {

    public enum Style {
        Alert,
        ActionSheet
    }

    public static final int HORIZONTAL_BUTTONS_MAX_COUNT = 2;
    public static final int CANCEL_POSITION = -1; // 点击取消按钮返回 －1, 其他按钮从0开始算

    private Dialog mDialog;
    private Style mAlertStyle = Style.Alert;
    private Context mContext;
    private String mTitle;
    private String mMessage;
    private String mCancel;
    private String mConfirm;
    private String mNeutral;
    private int mTitleGravity = Gravity.CENTER;
    private int mMessageGravity = Gravity.CENTER;

    private List<Integer> mListAlterItem = new ArrayList<>();
    private List<SheetItem> mSheetItems;
    private Display mDisplay;
    private boolean mCancelable = true;
    private DialogInterface.OnClickListener mConfirmBtnClickListener;
    private int confirmColor = Color.BLUE;
    private DialogInterface.OnClickListener mCancelBtnClickListener;
    private int mCancelColor = Color.BLUE;
    private DialogInterface.OnClickListener mNeutralBtnClickListener;
    private int mNeutralColor = Color.BLUE;

    private LinearLayout mRootView;

    private int mLineHeight;
    private int mButtonHeight;

    public UIAlertViewDialog(Context context) {
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();
        confirmColor = context.getResources().getColor(R.color.textColor_alert_button_others);
        mCancelColor = context.getResources().getColor(R.color.textColor_alert_button_others);
        mNeutralColor = context.getResources().getColor(R.color.textColor_alert_button_others);
        mLineHeight = context.getResources().getDimensionPixelSize(R.dimen.alert_dialog_devide_height);
        mButtonHeight = context.getResources().getDimensionPixelSize(R.dimen.alert_dialog_button_height);
    }

    public UIAlertViewDialog build() {
        if (mAlertStyle == Style.Alert) {
            mDialog = new Dialog(mContext, R.style.AlertDialogStyle);
        } else {
            mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
            // 初始化开始在底部
            Window window = mDialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
        }
        initView(mAlertStyle);
        mDialog.setContentView(mRootView);
        mDialog.setCancelable(mCancelable);
        return this;
    }

    /** 设置触屏是否消失 */
    public UIAlertViewDialog setDialogCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 初始化对话框的UI
     */
    private void initView(Style dialogStyle) {
        LinearLayout.LayoutParams lParams;

        mRootView = new LinearLayout(mContext);
        mRootView.setOrientation(LinearLayout.VERTICAL);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER;
        lParams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_titile_padding);
        lParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_titile_padding);
        lParams.rightMargin = mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_titile_padding);
        TextView titleTextView = new TextView(mContext);
        int titleColor = Color.BLACK;
        titleTextView.setTextColor(titleColor);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        titleTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        titleTextView.setGravity(mTitleGravity);
        if (mTitle != null) {
            titleTextView.setText(mTitle);
        }
        if (mTitleLeftDrawable != null) {
            titleTextView.setCompoundDrawables(mTitleLeftDrawable, null, null, null);
        }
        mRootView.addView(titleTextView, lParams);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout contentCon = new LinearLayout(mContext);
        contentCon.setOrientation(LinearLayout.VERTICAL);
        mRootView.addView(contentCon, lParams);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.CENTER;
        lParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_titile_padding);
        lParams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_titile_padding);
        lParams.rightMargin = mContext.getResources().getDimensionPixelSize(R.dimen.alert_dialog_titile_padding);
        TextView messageTextView = new TextView(mContext);
        int messageColor = Color.BLACK;
        messageTextView.setTextColor(messageColor);
        messageTextView.setGravity(mMessageGravity);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        if (mMessage != null) {
            messageTextView.setText(mMessage);
        }
        if (mMessageClickListener != null) {
            messageTextView.setOnClickListener(mMessageClickListener);
        }
        contentCon.addView(messageTextView, lParams);

        lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mLineHeight);
        lParams.topMargin = mContext.getResources()
                .getDimensionPixelSize(R.dimen.alert_dialog_titile_padding);
        ImageView titleLineImg = new ImageView(mContext);
        titleLineImg.setBackgroundColor(mContext.getResources().getColor(R.color.alertdialog_line));
        mRootView.addView(titleLineImg, lParams);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout bottomCon = new LinearLayout(mContext);
        mRootView.addView(bottomCon, lParams);

        if (Style.Alert == dialogStyle) {
            mRootView.setBackgroundDrawable(getShapeDrawable(true, true, true, true, Color.WHITE));
            Button button = null;
            ImageView lineImg = null;
            if (mTitle == null) {
                titleTextView.setVisibility(View.GONE);
            }
            if (mMessage == null) {
                messageTextView.setVisibility(View.GONE);
            }
            if (mListAlterItem.size() > 0) {
                if (mListAlterItem.size() == 1) {
                    int btnType = mListAlterItem.get(0);
                    lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            mButtonHeight);
                    button = new Button(mContext);
                    button.setId(btnType);
                    if (btnType == -1) {
                        button.setText(mCancel);
                        button.setTextColor(mCancelColor);
                    } else if (btnType == -2) {
                        button.setText(mNeutral);
                        button.setTextColor(mNeutralColor);
                    } else {
                        button.setText(mConfirm);
                        button.setTextColor(confirmColor);
                    }
                    button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    button.setOnClickListener(btnOnClickListener);
                    button.setBackgroundDrawable(
                            getShapePressedDrawable(false, false, true, true, Color.TRANSPARENT,
                                    mContext.getResources()
                                            .getColor(R.color.bgColor_alert_button_press)));
                    bottomCon.addView(button, lParams);
                } else {
                    for (int i = 0; i < mListAlterItem.size(); i++) {
                        int btnType = mListAlterItem.get(i);
                        if (i != 0) {
                            lParams = new LinearLayout.LayoutParams(mLineHeight,
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0);
                            lineImg = new ImageView(mContext);
                            lineImg.setBackgroundColor(
                                    mContext.getResources().getColor(R.color.alertdialog_line));
                            bottomCon.addView(lineImg, lParams);
                        }

                        lParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, mButtonHeight, 1);
                        lParams.gravity = Gravity.CENTER;
                        button = new Button(mContext);
                        button.setId(btnType);
                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        button.setOnClickListener(btnOnClickListener);
                        bottomCon.addView(button, lParams);

                        if (btnType == -1) {
                            button.setText(mCancel);
                            button.setTextColor(mCancelColor);
                        } else if (btnType == -2) {
                            button.setText(mNeutral);
                            button.setTextColor(mNeutralColor);
                        } else {
                            button.setText(mConfirm);
                            button.setTextColor(confirmColor);
                        }
                        if (i == 0) {
                            button.setBackgroundDrawable(
                                    getShapePressedDrawable(false, false, true, false,
                                            Color.TRANSPARENT, mContext.getResources()
                                                    .getColor(R.color.bgColor_alert_button_press)));
                        } else if (i == mListAlterItem.size() - 1) {
                            button.setBackgroundDrawable(
                                    getShapePressedDrawable(false, false, false, true,
                                            Color.TRANSPARENT, mContext.getResources()
                                                    .getColor(R.color.bgColor_alert_button_press)));
                        } else {
                            button.setBackgroundDrawable(
                                    getShapePressedDrawable(false, false, false, false,
                                            Color.TRANSPARENT, mContext.getResources()
                                                    .getColor(R.color.bgColor_alert_button_press)));
                        }
                    }
                }
            } else {
                titleLineImg.setVisibility(View.GONE);
            }

            mRootView.setMinimumWidth((int) (mDisplay.getWidth() * 0.75));
        } else {

            if (mMessage == null) {
                messageTextView.setVisibility(View.GONE);
            }

            lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ListView alertButtonListView = new ListView(mContext);
            alertButtonListView.setDivider(null);
            alertButtonListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            alertButtonListView.setSelector(new ColorDrawable(0x00000000));
            AlertViewAdapter alertViewAdapter = new AlertViewAdapter();
            alertButtonListView.setAdapter(alertViewAdapter);
            bottomCon.addView(alertButtonListView, lParams);

            mRootView.setMinimumWidth((int) (mDisplay.getWidth() * 0.75));
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * 四个角可以设为圆角 并有按压效果
     */
    private StateListDrawable getShapePressedDrawable(boolean tl, boolean tr, boolean bl,
                                                      boolean br, int normal, int pressed) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, getShapeDrawable(tl, tr, bl, br, pressed));
        selector.addState(new int[]{android.R.attr.state_enabled}, getShapeDrawable(tl, tr, bl, br, normal));
        return selector;
    }

    private OnClickListener btnOnClickListener = new OnClickListener() {

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View v) {
            if (-1 == v.getId()) {
                if (mCancelBtnClickListener != null) {
                    mCancelBtnClickListener.onClick(mDialog, -1);
                }
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            } else if (-2 == v.getId()) {
                if (mNeutralBtnClickListener != null) {
                    mNeutralBtnClickListener.onClick(mDialog, -2);
                }
            } else if (-3 == v.getId()) {
                if (mConfirmBtnClickListener != null) {
                    mConfirmBtnClickListener.onClick(mDialog, -3);
                }
            }
        }
    };

    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public UIAlertViewDialog setMessageGravity(int messageGravity) {
        this.mMessageGravity = messageGravity;
        return this;
    }

    public UIAlertViewDialog setTitleGravity(int titleGravity) {
        this.mTitleGravity = titleGravity;
        return this;
    }

    /**
     * 获取风格
     */
    public Style getAlertStyle() {
        return mAlertStyle;
    }

    /**
     * 设置风格
     */
    public UIAlertViewDialog setAlertStyle(Style alertStyle) {
        this.mAlertStyle = alertStyle;
        return this;
    }

    /**
     * 设置标题提示
     */
    public UIAlertViewDialog setTitle(String title) {
        if (title != null) {
            if ("".equals(title)) {
                this.mTitle = "提示";
            } else {
                this.mTitle = title;
            }
        }
        return this;
    }

    /**
     * 设置中间的提示内容
     */
    public UIAlertViewDialog setMessage(String msg) {
        if (msg != null) {
            if ("".equals(msg)) {
                this.mMessage = "内容";
            } else {
                this.mMessage = msg;
            }
        }
        return this;
    }

    private OnClickListener mMessageClickListener;

    public UIAlertViewDialog setMessageClickListener(OnClickListener listener) {
        mMessageClickListener = listener;
        return this;
    }

    private Drawable mTitleLeftDrawable;

    public UIAlertViewDialog setTitleLeftDrawable(Drawable drawable) {
        mTitleLeftDrawable = drawable;
        return this;
    }

    /**
     * 设置返回键是否可以取消
     */
    public UIAlertViewDialog setCancelable(boolean cancel) {
        this.mCancelable = cancel;
        if (mDialog != null) {
            mDialog.setCancelable(mCancelable);
        }
        return this;
    }

    /**
     * 确定按钮
     */
    public UIAlertViewDialog setPositiveButton(String text,
                                               final DialogInterface.OnClickListener listener) {
        if (text != null) {
            if ("".equals(text)) {
                mConfirm = "确定";
            } else {
                mConfirm = text;
            }
            if (mListAlterItem.contains(-3)) {
                mListAlterItem.remove(-3);
            }
            mListAlterItem.add(-3);
            mConfirmBtnClickListener = listener;
        }
        return this;
    }

    /**
     * 设置隐藏按钮
     */
    public UIAlertViewDialog setNeutralButton(String text, DialogInterface.OnClickListener listener) {
        if (text != null) {
            if ("".equals(text)) {
                mNeutral = "隐藏";
            } else {
                mNeutral = text;
            }
            if (mListAlterItem.contains(-2)) {
                mListAlterItem.remove(-2);
            }
            mListAlterItem.add(-2);
            mNeutralBtnClickListener = listener;
        }
        return this;
    }

    /**
     * 设置取消按钮
     */
    public UIAlertViewDialog setNegativeButton(String text, DialogInterface.OnClickListener listener) {
        if (text != null) {
            if ("".equals(text)) {
                mCancel = "取消";
            } else {
                mCancel = text;
            }
            if (mListAlterItem.contains(-1)) {
                mListAlterItem.remove(-1);
            }
            mListAlterItem.add(-1);
            mCancelBtnClickListener = listener;
        }
        return this;
    }

    /**
     * 添加单个条目名称
     */
    public UIAlertViewDialog addSheetItem(String strItem, int color, OnSheetItemClickListener listener) {
        if (mSheetItems == null) {
            mSheetItems = new ArrayList<>();
        }
        mSheetItems.add(new SheetItem(strItem, color, listener));
        return this;
    }

    /**
     *
     * @param strItems
     * @param color
     * @param listener
     * @return
     */
    public UIAlertViewDialog addSheetItems(String[] strItems, int color, OnSheetItemClickListener listener) {
        if (strItems != null) {
            for (int i = 0; i < strItems.length; i++) {
                String strItem = strItems[i];
                addSheetItem(strItem, color, listener);
            }
        }
        return this;
    }

    /**
     * 设置普通的多个按钮条目, 默认蓝色
     */
    public UIAlertViewDialog addSheetItems(String[] strItems, OnSheetItemClickListener listener) {
        if (strItems != null) {
            for (int i = 0; i < strItems.length; i++) {
                String strItem = strItems[i];
                addSheetItem(strItem, SheetItemColor.Blue.getColor(), listener);
            }
        }
        return this;
    }

    /**
     * 设置多个高亮的按钮条目, 默认红色
     */
    public UIAlertViewDialog addDestructiveSheetItems(String[] strItems, OnSheetItemClickListener listener) {
        if (strItems != null) {
            for (int i = 0; i < strItems.length; i++) {
                String strItem = strItems[i];
                addSheetItem(strItem, SheetItemColor.Red.getColor(), listener);
            }
        }
        return this;
    }

    /**
     * 添加普通单个条目名称,默认颜色是蓝色
     */
    public UIAlertViewDialog addSheetItem(String strItem, OnSheetItemClickListener listener) {
        return addSheetItem(strItem, SheetItemColor.Blue.getColor(), listener);
    }

    /**
     * 添加高亮当个条目名称, 默认颜色是红色
     */
    public UIAlertViewDialog addDestructiveSheetItem(String strItem, OnSheetItemClickListener listener) {
        return addSheetItem(strItem, SheetItemColor.Red.getColor(), listener);
    }

    private ShapeDrawable getShapeDrawable(boolean tl, boolean tr, boolean bl, boolean br, int color) {
        return getShapeDrawable(tl, tr, bl, br, color, 15);
    }

    /**
     * 圆角边框
     *
     * @param tl 左上
     * @param tr 右上
     * @param br 右下
     * @param bl 左下
     * @param color 颜色
     */
    private ShapeDrawable getShapeDrawable(boolean tl, boolean tr, boolean bl, boolean br, int color, int radius) {
        float[] outerRadii = new float[8];
        if (tl) {
            outerRadii[0] = radius;
            outerRadii[1] = radius;
        }
        if (tr) {
            outerRadii[2] = radius;
            outerRadii[3] = radius;
        }
        if (br) {
            outerRadii[4] = radius;
            outerRadii[5] = radius;
        }
        if (bl) {
            outerRadii[6] = radius;
            outerRadii[7] = radius;
        }
        RoundRectShape round = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shape = new ShapeDrawable(round);
        shape.getPaint().setColor(color);
        shape.getPaint().setStyle(Paint.Style.FILL);
        return shape;
    }

    public interface OnSheetItemClickListener {

        void onClick(int which, View v);
    }

    /**
     * 每一项里面的item数据
     */
    public class SheetItem {

        public String name;
        public OnSheetItemClickListener itemClickListener;
        int color;

        public SheetItem(String name, int color, OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public enum SheetItemColor {

        Blue(0xff037BFF), Red(0xffFD4A2E);

        private int color;

        SheetItemColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    public class AlertViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mSheetItems != null) {
                return mSheetItems.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mSheetItems != null) {
                return mSheetItems.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final SheetItem sheetItem = mSheetItems.get(position);
            HolderView holderView = null;
            if (convertView == null) {
                holderView = new HolderView(mContext);
            } else {
                holderView = (HolderView) convertView;
            }

            holderView.button.setText(sheetItem.name);
            holderView.button.setTextColor(sheetItem.color);
            holderView.button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (sheetItem.itemClickListener != null) {
                        sheetItem.itemClickListener.onClick(position, convertView);
                    }
                }
            });
            return holderView;
        }
    }

    class HolderView extends LinearLayout {

        public ImageView imageView;
        public Button button;

        public HolderView(Context context) {
            super(context);
            init();
        }

        public HolderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            this.setOrientation(LinearLayout.VERTICAL);
            LayoutParams lParams;
            lParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mLineHeight,
                    0);
            imageView = new ImageView(mContext);
            imageView.setBackgroundColor(mContext.getResources().getColor(R.color.alertdialog_line));
            this.addView(imageView, lParams);

            lParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    mButtonHeight, 1);
            lParams.gravity = Gravity.CENTER;
            button = new Button(mContext);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            this.addView(button, lParams);
        }
    }
}
