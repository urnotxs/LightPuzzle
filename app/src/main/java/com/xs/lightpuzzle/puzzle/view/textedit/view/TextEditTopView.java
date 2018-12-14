package com.xs.lightpuzzle.puzzle.view.textedit.view;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.dialog.UIAlertViewDialog;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.EffectiveImageButton;
import com.xs.lightpuzzle.puzzle.view.textedit.BottomEditTextView;
import com.xs.lightpuzzle.puzzle.view.textedit.textinterface.OnInputListener;

/**
 * Created by urnotXS on 2018/4/11.
 */

public class TextEditTopView extends LinearLayout implements View.OnClickListener{

    private Context mContext;

    private EffectiveImageButton mTranslateBtn;
    private EffectiveImageButton mOkayBtn;
    private EffectiveImageButton mIconInput;
    private EffectiveImageButton mIconOperate;
    private FrameLayout mDeleteBtn;
    private EditText mInputEdt;

    private String mEdtString;
    private int mEditModel;
    private OnInputListener mOnInputListener;
    private BottomEditTextView.OnEditInteractionListener mListener;

    public void setEditInteractionListener(BottomEditTextView.OnEditInteractionListener listener){
        mListener = listener;
    }

    public void setOnInputListener(OnInputListener listener){
        mOnInputListener = listener;
    }

    public void setEditModel(int editModel){
        mEditModel = editModel;
    }

    public void setText(String text) {
        mEdtString = text;
        if (text.trim().equals("")) {
            mInputEdt.setText("");
        }else{
            mInputEdt.setText(mEdtString);
        }
        mInputEdt.selectAll(); //修复当text为空时的全选数组越界 anson 20180622
        /*if (Utils.checkChineseChar(mEdtString)) {
            mTranslateBtn.setTag("translate_zh_to_en");
        } else {
            mTranslateBtn.setTag("translate_en_to_zh");
        }*/
    }

    public void setIconInputVisible(int visibleStatus){
        mIconInput.setVisibility(visibleStatus);
    }

    public void setIconOperateVisible(int visibleStatus){
        mIconOperate.setVisibility(visibleStatus);
    }

    public TextEditTopView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutParams llParams;
        FrameLayout.LayoutParams flParams;
        {
            setGravity(Gravity.CENTER_VERTICAL);
            //中英文翻译
            FrameLayout translateLayout = new FrameLayout(getContext());
            llParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT, 0);
            addView(translateLayout, llParams);
            {
                flParams = new FrameLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                flParams.gravity = Gravity.BOTTOM;
                flParams.bottomMargin = Utils.getRealPixel3(12);
                mTranslateBtn = new EffectiveImageButton(mContext);
                mTranslateBtn.setOnClickListener(this);
                mTranslateBtn.setButtonImage(R.drawable.translate_en_to_zh, R.drawable.translate_en_to_zh_hover);
                mTranslateBtn.setTag("translate_zh_to_en");
                mTranslateBtn.setScaleType(ImageView.ScaleType.CENTER);
                // 开关控制是否显示翻译
                mTranslateBtn.setVisibility(VISIBLE);
                mTranslateBtn.setVisibility(VISIBLE);
                translateLayout.addView(mTranslateBtn, flParams);
            }

            //翻译按钮和编辑框中间的分割线（新加）
            llParams = new LayoutParams(
                    1, LayoutParams.MATCH_PARENT, 0);
            ImageView bar_line = new ImageView(getContext());
            bar_line.setImageResource(R.drawable.bar_splic);
            bar_line.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(bar_line, llParams);

            //输入框
            llParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, 1);
            llParams.setMargins(Utils.getRealPixel3(12),
                    Utils.getRealPixel3(12), 0 ,
                    Utils.getRealPixel3(12));
            LinearLayout inputPanel = new LinearLayout(mContext);//输入框
            inputPanel.setBackgroundResource(R.drawable.ft_input_backgound);
            addView(inputPanel, llParams);
            {
                llParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, 1);
                mInputEdt = new EditText(mContext);//输入文本框
                mInputEdt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                mInputEdt.setGravity(Gravity.CLIP_VERTICAL);
                mInputEdt.setHint(R.string.watermatkedittext_edit);
                mInputEdt.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_NORMAL
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                        | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                mInputEdt.addTextChangedListener(mWatcher);
                mInputEdt.setMaxLines(3);
                mInputEdt.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_edittext));
                mInputEdt.setCursorVisible(true);
                mInputEdt.setPadding(0, 0, 0, 0);
                mInputEdt.setOnClickListener(this);
                mInputEdt.setEnabled(true);
                //获取焦点并且弹出输入法
                //showSoft();
                inputPanel.addView(mInputEdt, llParams);

                llParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT, 0);
                mDeleteBtn = new FrameLayout(getContext());//删除框
                mDeleteBtn.setOnClickListener(this);
                inputPanel.addView(mDeleteBtn, llParams);
                {
                    flParams = new FrameLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    flParams.gravity = Gravity.BOTTOM;
                    flParams.setMargins(Utils.getRealPixel3(12),0,
                            Utils.getRealPixel3(2),Utils.getRealPixel3(4));
                    EffectiveImageButton deleteBtn = new EffectiveImageButton(mContext);//删除按钮
                    deleteBtn.setButtonImage(R.drawable.text_delect, R.drawable.text_delect_hover);
                    mDeleteBtn.addView(deleteBtn, flParams);
                }
            }

            //okay按钮
            FrameLayout okLayout = new FrameLayout(getContext());
            llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0);
            addView(okLayout, llParams);
            {
                flParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                flParams.gravity = Gravity.BOTTOM;
                flParams.bottomMargin = Utils.getRealPixel3(12);
                mOkayBtn = new EffectiveImageButton(mContext);
                mOkayBtn.setOnClickListener(this);
                mOkayBtn.setButtonImage(R.drawable.text_save, R.drawable.text_save_hover);
                okLayout.addView(mOkayBtn, flParams);
            }

            //字体按钮和确定按钮中间的分割线
            llParams = new LayoutParams(1, LayoutParams.MATCH_PARENT, 0);
            ImageView bar_line_font_ok = new ImageView(getContext());
            bar_line_font_ok.setImageResource(R.drawable.bar_splic);
            bar_line_font_ok.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(bar_line_font_ok, llParams);

            //选择字体按钮的外部容器
            llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0);
            FrameLayout flCon = new FrameLayout(getContext());
            addView(flCon, llParams);
            {
                flParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                flParams.gravity = Gravity.BOTTOM;
                flParams.bottomMargin = Utils.getRealPixel3(12);
                mIconInput = new EffectiveImageButton(mContext);
                mIconInput.setVisibility(GONE);
                mIconInput.setOnClickListener(this);
                mIconInput.setButtonImage(R.drawable.text_input,
                        R.drawable.text_input_hovet);
                flCon.addView(mIconInput, flParams);

                flParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                flParams.gravity = Gravity.BOTTOM;
                flParams.bottomMargin = Utils.getRealPixel3(12);
                mIconOperate = new EffectiveImageButton(mContext);
                mIconOperate.setOnClickListener(this);
                mIconOperate.setButtonImage(R.drawable.text_show, R.drawable.text_show_hover);
                flCon.addView(mIconOperate, flParams);
            }
        }
    }

    public void showSoft(){
        // 初始获取焦点
        mInputEdt.requestFocus();
        mInputEdt.selectAll();
        mOnInputListener.openInputMethod();
    }

    @Override
    public void onClick(View view) {
        if (view == mTranslateBtn){
            if (mListener != null){
                mOnInputListener.onopenText();
                mListener.onTranslate(mInputEdt.getText().toString());
            }
        }else if (view == mDeleteBtn){
            // 弹窗，清空输入框
            Utils.hideKeyboard(mContext , getApplicationWindowToken());
            UIAlertViewDialog uiAlertViewDialog = new UIAlertViewDialog(getContext());
            uiAlertViewDialog/*.setTitle("提示")*/
                    .setMessage(getResources().getString(R.string.watermatkedittext_cleartext_tips))
                    .setNegativeButton(getResources().getString(R.string.watermatkedittext_clear_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.watermatkedittext_clear_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mInputEdt.setText("");
                            mDeleteBtn.setVisibility(GONE);
                        }
                    })
                    .build()
                    .show();
        }else if (view == mOkayBtn){
            // 退出页面
            if (mOnInputListener != null) {
                mOnInputListener.onSave(mInputEdt.getText().toString());
            }
        }else if (view == mIconInput){
            // 弹输入法
            mIconInput.setVisibility(GONE);
            mIconOperate.setVisibility(VISIBLE);
            showSoft();

        }else if (view == mIconOperate){
            // 收输入法，显示bottomView
            mIconInput.setVisibility(VISIBLE);
            mIconOperate.setVisibility(GONE);
            mOnInputListener.onopenText();
        }
    }

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start,
                                      int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start,
                                  int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (mOnInputListener != null) {
                mOnInputListener.changeText(text);
            }

            if (!editable.toString().equals("")) {
                mDeleteBtn.setVisibility(VISIBLE);
            } else {
                mDeleteBtn.setVisibility(GONE);
            }
        }
    };
}
