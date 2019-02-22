package com.xs.lightpuzzle.demo.a_recyclerview_demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.xs.lightpuzzle.LightPuzzleApplication;
import com.xs.lightpuzzle.R;

/**
 * Created by xs on 2018/12/14.
 */

public class TestItemDecoration extends RecyclerView.ItemDecoration {

    private int item_height;
    private int item_padding;
    private Paint paint;
    private Paint paint2;
    private RecyclerViewTestLayout.TextListener mListener;

    private Paint.FontMetrics fontMetrics;
    private TextPaint textPaint;

    public TestItemDecoration(RecyclerViewTestLayout.TextListener listener) {
        mListener = listener;
        item_height = ConvertUtils.dp2px(30);
//        item_padding = ConvertUtils.dp2px(10);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(LightPuzzleApplication.getContext().getResources().getColor(R.color.recyclerView_decoration));

        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint2.setColor(Color.parseColor("#52ff0000"));

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(ConvertUtils.dp2px(25));
        fontMetrics = new Paint.FontMetrics();
        textPaint.getFontMetrics(fontMetrics);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        //因为分割线是在item的上方,所以需要为每个item腾出一条分割线的高度
        if (!isFirstInGroup(position)) {
            outRect.top = item_height;
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        View child0 = parent.getChildAt(0);

        int position = parent.getChildAdapterPosition(child0);
        String content = mListener.getName(position);
        // 如果第一个 item 的 Bottom <= 分割线的高度
        if (child0.getBottom() <= item_height && !isFirstInGroup(position + 1)) {
            // 随着 RecyclerView 滑动 分割线的 top = 固定为 0 不动, bottom 则赋值为 child0 的 bottom 值.
            c.drawRect(0, 0, child0.getRight(), child0.getBottom(), paint);
            int textLeft = (int) (child0.getWidth() / 2.0f - textPaint.measureText(content) / 2.0f);
            c.drawText(content, textLeft, child0.getBottom() - fontMetrics.descent, textPaint);
        } else {
            // 固定不动
            c.drawRect(0, 0, child0.getRight(), item_height, paint);
            int textLeft = (int) (child0.getWidth() / 2.0f - textPaint.measureText(content) / 2.0f);
            c.drawText(content, textLeft, item_height - fontMetrics.descent, textPaint); // 左下角
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);

            int top = view.getTop() - item_height;
            int bottom = view.getTop();
            int left = view.getLeft() + item_padding;
            int right = view.getRight() - item_padding;

            int position = parent.getChildAdapterPosition(view);
            String name = mListener.getName(position);
            if (!isFirstInGroup(position)) {
                //这里把left和right的值分别增加item_padding,和减去item_padding.
                c.drawRect(left, top, right, bottom, paint);
                int textLeft = (int) (view.getWidth() / 2.0f - textPaint.measureText(name) / 2.0f);
                c.drawText(name, textLeft, bottom - fontMetrics.descent, textPaint);
            }
        }
    }

    //判断当前item和下一个item的第一个文字是否相同,如果相同说明是同一组,不需要画分割线
    private boolean isFirstInGroup(int pos) {
        //如果是adapter的第一个position直接return,因为第一个item必须有分割线
        if (pos == 0) {
            return false;
        } else {
            //否者判断前一个item的字符串 与 当前item字符串 是否相同
            String prevGroupId = mListener.getName(pos - 1);
            String groupId = mListener.getName(pos);
            if (prevGroupId.equals(groupId)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
