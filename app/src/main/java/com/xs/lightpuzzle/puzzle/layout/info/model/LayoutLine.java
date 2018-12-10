package com.xs.lightpuzzle.puzzle.layout.info.model;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局相关
 */

public class LayoutLine {
    public enum Direction {
        HORIZONTAL, VERTICAL
    }
    public enum Towards {
        LEFT, TOP , RIGHT , BOTTOM
    }
    private LayoutLine.Towards mTowards = Towards.LEFT;
    private LayoutLine.Direction mDirection = LayoutLine.Direction.HORIZONTAL;
    private int mParentId;
    private PointF mStartPoint;
    private PointF mEndPoint;
    private float mPadding;//间距
    private float mLength;
    private PointF mPreviousStart = new PointF();//line前一次触发所在位置
    private PointF mPreviousEnd = new PointF();
    private RectF bounds = new RectF();//line接受一定矩形范围内的move触发
    private RectF mRectF = new RectF();//判断在同一条直线上，和padding有关，忽略间隔偏差

    private float matchSize = 20;
    private float tempRectfHeight = 5;

    public void resetLayoutLine(PointF start, PointF end){
        mStartPoint.set(start.x , start.y);
        mEndPoint.set(end.x , end.y);
    }
    LayoutLine(int mParentId , Towards mTowards , PointF start, PointF end) {
        this.mTowards = mTowards;
        this.mParentId = mParentId;
        mStartPoint = start;
        mEndPoint = end;
        if (start.x == end.x) {
            mDirection = Direction.VERTICAL;
        } else if (start.y == end.y) {
            mDirection = Direction.HORIZONTAL;
        } else {
            Log.d("StraightLine", "StraightLine: current only support two mDirection");
        }
    }

    public RectF getLineRectF() {
        if (mDirection== Direction.VERTICAL) {
            //垂直
            return new RectF(
                    mStartPoint.x , mStartPoint.y+mPadding ,
                    mStartPoint.x , mEndPoint.y-mPadding);
        } else if (mDirection== Direction.HORIZONTAL) {
            //水平
            return new RectF(
                    mStartPoint.x+mPadding , mStartPoint.y ,
                    mEndPoint.x-mPadding , mStartPoint.y);
        }
        return null;
    }

    public RectF getTotalLineRectF(LayoutArea outerArea) {
        if (mDirection== Direction.VERTICAL) {
            //垂直
            return new RectF(
                    mStartPoint.x-tempRectfHeight , outerArea.top()-outerArea.getPaddingTop() ,
                    mStartPoint.x+tempRectfHeight , outerArea.bottom()+outerArea.getPaddingBottom());
        } else if (mDirection== Direction.HORIZONTAL) {
            //水平
            return new RectF(
                    outerArea.left()-outerArea.getPaddingLeft() , mStartPoint.y-tempRectfHeight ,
                    outerArea.right()+outerArea.getPaddingRight(), mStartPoint.y+tempRectfHeight );
        }
        return null;
    }

    public boolean contains(float x, float y, float extra) {
        if (mDirection == Direction.HORIZONTAL) {
            bounds.left = mStartPoint.x;
            bounds.right = mEndPoint.x;
            bounds.top = mStartPoint.y - extra / 2;
            bounds.bottom = mStartPoint.y + extra / 2;
        } else if (mDirection == Direction.VERTICAL) {
            bounds.top = mStartPoint.y;
            bounds.bottom = mEndPoint.y;
            bounds.left = mStartPoint.x - extra / 2;
            bounds.right = mStartPoint.x + extra / 2;
        }

        return bounds.contains(x, y);//在line的一定范围内接受触发
    }

    public void prepareMove() {
        mPreviousStart.set(mStartPoint);
        mPreviousEnd.set(mEndPoint);
    }

    //释放line的时候调用，用于适配邻近的坐标值
    public float releaseMove(ArrayList<Float> xyAxisList,float matchSize){
        if (mDirection == LayoutLine.Direction.HORIZONTAL) {
            for (int i = 0; i < xyAxisList.size(); i++) {
                float offset = xyAxisList.get(i) - mStartPoint.y;
                if (offset != 0 && Math.abs(offset) < matchSize) {
                    mStartPoint.y = xyAxisList.get(i);
                    mEndPoint.y = xyAxisList.get(i);
                    return offset;
                }
            }
        }else{
            for (int i = 0; i < xyAxisList.size(); i++) {
                float offset = xyAxisList.get(i) - mStartPoint.x;
                if (offset != 0 && Math.abs(offset) < matchSize) {
                    mStartPoint.x = xyAxisList.get(i);
                    mEndPoint.x = xyAxisList.get(i);
                    return offset;
                }
            }
        }
        return 0;

    }

    //根据offset平移线段
    public void move(float offset) {
        if (mDirection == LayoutLine.Direction.HORIZONTAL) {
            mStartPoint.y = mPreviousStart.y + offset;
            mEndPoint.y = mPreviousEnd.y + offset;
            mPreviousStart.set(mStartPoint);
            mPreviousEnd.set(mEndPoint);
        } else {
            mStartPoint.x = mPreviousStart.x + offset;
            mEndPoint.x = mPreviousEnd.x + offset;
            mPreviousStart.set(mStartPoint);
            mPreviousEnd.set(mEndPoint);
        }
    }

    public float getPadding() {
        return mPadding;
    }

    public void setPadding(float padding) {
        this.mPadding = padding;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int mParentId) {
        this.mParentId = mParentId;
    }

    public Towards getTowards() {
        return mTowards;
    }

    public void setTowards(Towards mTowards) {
        this.mTowards = mTowards;
    }

    public float length() {
        return (float) Math.sqrt(Math.pow(mEndPoint.x - mStartPoint.x, 2) + Math.pow(mEndPoint.y - mStartPoint.y, 2));
    }

    public PointF getStartPoint() {
        return mStartPoint;
    }

    public PointF getEndPoint() {
        return mEndPoint;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction mDirection) {
        this.mDirection = mDirection;
    }

    public float getLength() {
        return (float) Math.sqrt(Math.pow(mEndPoint.x - mStartPoint.x, 2) + Math.pow(mEndPoint.y - mStartPoint.y, 2));
    }
}
