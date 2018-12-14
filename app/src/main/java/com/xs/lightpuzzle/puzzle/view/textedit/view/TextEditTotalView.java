package com.xs.lightpuzzle.puzzle.view.textedit.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.config.AppConfiguration;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.textedit.textinterface.OnInputListener;

/**
 * Created by urnotXS on 2018/4/11.
 */

public class TextEditTotalView extends LinearLayout implements View.OnClickListener {

    public static final int EDIT_MIDDLE_HEIGHT = (int) (Utils.getRealPixel3(90));
    public static int EDIT_BOTTOM_HEIGHT = Utils.getScreenH() / 2;

    private Context mContext;
    private TextEditTopView mTopView;
    private TextEditBottomView mBottomView;

    private int mBottomHeight = EDIT_BOTTOM_HEIGHT;

    public void setEditModel(int editModel) {
        mTopView.setEditModel(editModel);
        mBottomView.setEditModel(editModel);
    }

    public void setOnInputListener(OnInputListener listener) {
        mTopView.setOnInputListener(listener);
        mBottomView.setOnInputListener(listener);
    }

    public TextEditTopView getTopView() {
        return mTopView;
    }

    public TextEditBottomView getBottomView() {
        return mBottomView;
    }

    public TextEditTotalView(Context context) {
        super(context);
        mContext = context;
        if (AppConfiguration.getDefault().getKeyboardHeight() > 0) {
            mBottomHeight = AppConfiguration.getDefault().getKeyboardHeight();
        }
        initView();
    }

    private void initView() {
        LayoutParams llParams;
        this.setOrientation(LinearLayout.VERTICAL);

        //顶部line
        llParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        ImageView line = new ImageView(mContext);
        line.setImageResource(R.drawable.black_line);
        line.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(line, llParams);

        //包括翻译，输入框，软键盘和字体编辑控制按钮
        llParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mTopView = new TextEditTopView(mContext);
        mTopView.setBackgroundResource(R.drawable.uitextviewbaraddtexshowbarfenge);
        mTopView.setOnClickListener(this);
        addView(mTopView, llParams);

        //底部的ListView分割线
        llParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        line = new ImageView(mContext);
        line.setImageResource(R.drawable.black_line);
        line.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(line, llParams);

        //包括字体类型，颜色，大小
        llParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                mBottomHeight);
        mBottomView = new TextEditBottomView(getContext());
        Rect rect = new Rect(0, 0, Utils.getScreenW(), Utils.getScreenH());
        Rect dstRect = new Rect(0, Utils.getScreenH() - mBottomHeight, Utils.getScreenW(), Utils.getScreenH());
        mBottomView.setBackgroundResource(R.drawable.bg_toolbar);
        mBottomView.setOrientation(LinearLayout.VERTICAL);
        addView(mBottomView, llParams);

    }

    public void setContentHeight(int height) {
        // 底部View高度 ， 当键盘出现时做调整
        ViewGroup.LayoutParams params = mBottomView.getLayoutParams();
        if (params.height != height){
            params.height = height ;
            AppConfiguration.getDefault().setKeyboardHeight(height);
            mBottomView.setLayoutParams(params);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
