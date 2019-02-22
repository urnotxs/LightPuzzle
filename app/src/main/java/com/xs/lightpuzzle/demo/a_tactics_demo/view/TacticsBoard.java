package com.xs.lightpuzzle.demo.a_tactics_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_tactics_demo.data.TacticsModelVO;
import com.xs.lightpuzzle.demo.a_tactics_demo.data.TeamUserPositionVO;
import com.xs.lightpuzzle.demo.a_tactics_demo.data.UserBriefVO;

import java.util.List;

/**
 * @author xs
 * @description 足球战术板自定义控件，排兵布阵
 * @since 2019/2/21
 */

public class TacticsBoard extends FrameLayout {
    private String TACTICS = "3,3,4";
    public int width;
    public int height;
    private Handler handler;
    private boolean hasWH = false;
    public boolean isEditable = true;

    private List<TeamUserPositionVO> items;
    public TacticsItem selectedItem;
    public TacticsModelVO posItem;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public TacticsBoard(@NonNull Context context) {
        super(context);
    }

    public TacticsBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TacticsBoard);
        TACTICS = mTypedArray.getString(R.styleable.TacticsBoard_tactics);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        width = sizeWidth;
        height = width / 2;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth / 2, MeasureSpec.EXACTLY);
        setMeasuredDimension(sizeWidth, sizeWidth / 2);
        if (handler != null && !hasWH) {
            hasWH = true;
            handler.sendEmptyMessage(0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setItems(List<TeamUserPositionVO> items) {
        this.items = items;
    }

    public void showLocationItems() {
        for (int i = 0; i < items.size(); i++) {

            TacticsLocationItem itemView = new TacticsLocationItem(
                    this, items.get(i), isEditable);
            itemView.setHandler(handler);
            itemView.addViewToScreen();
        }
    }

    public void showItems() {
        for (int i = 0; i < items.size(); i++) {
            UserBriefVO userBriefVO = new UserBriefVO();
            userBriefVO.NickName = "nick";
            items.get(i).SetUser(userBriefVO);

            TacticsItem itemView = new TacticsItem(this, items.get(i), isEditable);
            itemView.addViewToScreen();
        }
    }
}
