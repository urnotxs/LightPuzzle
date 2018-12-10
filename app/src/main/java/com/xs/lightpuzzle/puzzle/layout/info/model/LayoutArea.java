package com.xs.lightpuzzle.puzzle.layout.info.model;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.xs.lightpuzzle.puzzle.util.Utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局相关
 */
public class LayoutArea {
    private final int HANDLERBAR_PADDING = Utils.getRealPixel3(26);
    private final int HANDLERBAR_MAX_LENGTH = Utils.getRealPixel3(200);

    public LayoutLine mLineLeft;
    public LayoutLine mLineRight;
    public LayoutLine mLineTop;
    public LayoutLine mLineBottom;

    private Path mAreaPath = new Path();
    private RectF mAreaRect = new RectF();
    private PointF[] mHandleBarPoints = new PointF[2];

    private float mPaddingLeft;
    private float mPaddingTop;
    private float mPaddingRight;
    private float mPaddingBottom;
    private float mRadianRatio;
    private int mId;

    LayoutArea() {
        mHandleBarPoints[0] = new PointF();
        mHandleBarPoints[1] = new PointF();
    }

    LayoutArea(LayoutArea src) {
        this.mLineLeft = src.mLineLeft;
        this.mLineTop = src.mLineTop;
        this.mLineRight = src.mLineRight;
        this.mLineBottom = src.mLineBottom;

        mHandleBarPoints[0] = new PointF();
        mHandleBarPoints[1] = new PointF();
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }


    public LayoutArea(int id, RectF baseRect) {
        this();
        mId = id;
        setBaseRect(baseRect);
    }

    public void resetArea(float l, float t, float r, float b) {
        PointF one = new PointF(l, t);
        PointF two = new PointF(r, t);
        PointF three = new PointF(l, b);
        PointF four = new PointF(r, b);

        mLineLeft.resetLayoutLine(one,three);
        mLineTop.resetLayoutLine(one, two);
        mLineRight.resetLayoutLine(two, four);
        mLineBottom.resetLayoutLine(three, four);
    }

    private void setBaseRect(RectF baseRect) {
        PointF one = new PointF(baseRect.left, baseRect.top);
        PointF two = new PointF(baseRect.right, baseRect.top);
        PointF three = new PointF(baseRect.left, baseRect.bottom);
        PointF four = new PointF(baseRect.right, baseRect.bottom);

        mLineLeft = new LayoutLine(mId, LayoutLine.Towards.LEFT, one, three);
        mLineTop = new LayoutLine(mId, LayoutLine.Towards.TOP, one, two);
        mLineRight = new LayoutLine(mId, LayoutLine.Towards.RIGHT, two, four);
        mLineBottom = new LayoutLine(mId, LayoutLine.Towards.BOTTOM, three, four);
    }

    public LayoutData.RectVO getRectVO(float ratioW, float ratioH) {
        LayoutData.RectVO rectVO = new LayoutData.RectVO();
        rectVO.setxPosition(mLineLeft.getStartPoint().x * ratioW);
        rectVO.setyPosition(mLineLeft.getStartPoint().y * ratioH);
        rectVO.setWidth((mLineTop.getEndPoint().x - mLineTop.getStartPoint().x) * ratioW);
        rectVO.setHeight((mLineLeft.getEndPoint().y - mLineLeft.getStartPoint().y) * ratioH);
        return rectVO;
    }

    //left(),top(),right(),bottom()是piece的展示区域，除开padding的区域
    public float left() {
        return mLineLeft.getStartPoint().x + mPaddingLeft;
    }

    public float top() {
        return mLineTop.getStartPoint().y + mPaddingTop;
    }

    public float right() {
        return mLineRight.getStartPoint().x - mPaddingRight;
    }

    public float bottom() {
        return mLineBottom.getStartPoint().y - mPaddingBottom;
    }

    public float centerX() {
        return (left() + right()) / 2;
    }

    public float centerY() {
        return (top() + bottom()) / 2;
    }

    public float width() {
        return right() - left();
    }

    public float height() {
        return bottom() - top();
    }

    public PointF getCenterPoint() {
        return new PointF(centerX(), centerY());
    }

    public boolean contains(PointF point) {
        return contains(point.x, point.y);
    }

    public boolean contains(float x, float y) {
        return getAreaRect().contains(x, y);
    }

    public boolean contains(LayoutLine line) {
        return mLineLeft == line || mLineTop == line || mLineRight == line || mLineBottom == line;
    }

    public Path getAreaPath() {
        mAreaPath.reset();
        mAreaPath.addRoundRect(getAreaRect(), getRadian(), getRadian(), Path.Direction.CCW);
        return mAreaPath;
    }

    public RectF getAreaRect() {
        mAreaRect.set(left(), top(), right(), bottom());
        return mAreaRect;
    }

    public RectF getAreaRect(float scale) {
        float widthOff = (width() - width()*scale)/2;
        float heightOff = (height() - height()*scale)/2;
        mAreaRect.set(left()+widthOff, top()+heightOff, right()-widthOff, bottom()-heightOff);
        return mAreaRect;
    }

    public List<LayoutLine> getLines() {
        return Arrays.asList((LayoutLine) mLineLeft, mLineTop, mLineRight, mLineBottom);
    }

    public float getRadian() {
        float length = width() < height() ? width() : height();
        return ((length-1) / 2.0f) * mRadianRatio;
    }

    public void setRadianRatio(float radianRatio) {
        mRadianRatio = radianRatio;
    }

    public float getPaddingLeft() {
        return mPaddingLeft;
    }

    public float getPaddingTop() {
        return mPaddingTop;
    }

    public float getPaddingRight() {
        return mPaddingRight;
    }

    public float getPaddingBottom() {
        return mPaddingBottom;
    }

    public void setPadding(float padding) {
        setPadding(padding, padding, padding, padding);
    }

    public void setPadding(float mPaddingLeft, float mPaddingTop, float mPaddingRight,float mPaddingBottom) {
        this.mPaddingLeft = mPaddingLeft;
        this.mPaddingTop = mPaddingTop;
        this.mPaddingRight = mPaddingRight;
        this.mPaddingBottom = mPaddingBottom;
        mLineLeft.setPadding(mPaddingLeft);
        mLineRight.setPadding(mPaddingRight);
        mLineTop.setPadding(mPaddingTop);
        mLineBottom.setPadding(mPaddingBottom);
    }

    public PointF[] getHandleBarPoints(LayoutLine line, float offset) {
        float length = 0 ;
        float padding = HANDLERBAR_PADDING;
        if (line == mLineLeft) {
            if (height() - 2 * HANDLERBAR_PADDING < 0){
                padding = 9;
            }
            if (height() - 2 * HANDLERBAR_PADDING > HANDLERBAR_MAX_LENGTH) {
                padding = (height() - HANDLERBAR_MAX_LENGTH) / 2.0f;
            }

            mHandleBarPoints[0].x = left() + offset;
            mHandleBarPoints[0].y = top() + padding;
            mHandleBarPoints[1].x = left() + offset;
            mHandleBarPoints[1].y = bottom() - padding;

            length = mHandleBarPoints[1].y - mHandleBarPoints[0].y;
            length = (length - (1-getRadian()/height())*length)/2;
            mHandleBarPoints[0].y = mHandleBarPoints[0].y + length;
            mHandleBarPoints[1].y = mHandleBarPoints[1].y - length;

        } else if (line == mLineTop) {
            if (width() - 2 * HANDLERBAR_PADDING < 0){
                padding = 9;
            }
            if (width() - 2 * HANDLERBAR_PADDING > HANDLERBAR_MAX_LENGTH) {
                padding = (width() - HANDLERBAR_MAX_LENGTH) / 2.0f;
            }
            mHandleBarPoints[0].x = left() + padding;
            mHandleBarPoints[0].y = top() + offset;
            mHandleBarPoints[1].x = right() - padding;
            mHandleBarPoints[1].y = top() + offset;

            length = mHandleBarPoints[1].x - mHandleBarPoints[0].x;
            length = (length - (1-getRadian()/width())*length)/2;
            mHandleBarPoints[0].x = mHandleBarPoints[0].x + length;
            mHandleBarPoints[1].x = mHandleBarPoints[1].x - length;
        } else if (line == mLineRight) {
            if (height() - 2 * HANDLERBAR_PADDING < 0){
                padding = 9;
            }
            if (height() - 2 * HANDLERBAR_PADDING > HANDLERBAR_MAX_LENGTH) {
                padding = (height() - HANDLERBAR_MAX_LENGTH) / 2.0f;
            }

            mHandleBarPoints[0].x = right() - offset;
            mHandleBarPoints[0].y = top() + padding;
            mHandleBarPoints[1].x = right() - offset;
            mHandleBarPoints[1].y = bottom() - padding;

            length = mHandleBarPoints[1].y - mHandleBarPoints[0].y;
            length = (length - (1-getRadian()/height())*length)/2;
            mHandleBarPoints[0].y = mHandleBarPoints[0].y + length;
            mHandleBarPoints[1].y = mHandleBarPoints[1].y - length;
        } else if (line == mLineBottom) {
            if (width() - 2 * HANDLERBAR_PADDING < 0){
                padding = 9;
            }
            if (width() - 2 * HANDLERBAR_PADDING > HANDLERBAR_MAX_LENGTH) {
                padding = (width() - HANDLERBAR_MAX_LENGTH) / 2.0f;
            }
            mHandleBarPoints[0].x = left() + padding;
            mHandleBarPoints[0].y = bottom() - offset;
            mHandleBarPoints[1].x = right() - padding;
            mHandleBarPoints[1].y = bottom() - offset;

            length = mHandleBarPoints[1].x - mHandleBarPoints[0].x;
            length = (length - (1-getRadian()/width())*length)/2;
            mHandleBarPoints[0].x = mHandleBarPoints[0].x + length;
            mHandleBarPoints[1].x = mHandleBarPoints[1].x - length;
        }
        return mHandleBarPoints;
    }

    public Rect getOriginalRect(){
        int left = (int) Math.ceil(mLineLeft.getStartPoint().x);
        int top = (int) Math.ceil(mLineTop.getStartPoint().y);
        int right = (int) Math.ceil(mLineRight.getStartPoint().x);
        int bottom = (int) Math.ceil(mLineBottom.getStartPoint().y);

        return new Rect(left , top , right , bottom);
    }


    static class AreaComparator implements Comparator<LayoutArea> {
        @Override
        public int compare(LayoutArea lhs, LayoutArea rhs) {
            if (lhs.top() < rhs.top()) {
                return -1;
            } else if (lhs.top() == rhs.top()) {
                if (lhs.left() < rhs.left()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        }
    }
}
