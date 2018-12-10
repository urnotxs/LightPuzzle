package com.xs.lightpuzzle.puzzle.piece;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;

import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutArea;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutLine;
import com.xs.lightpuzzle.puzzle.piece.base.BasePiece;
import com.xs.lightpuzzle.puzzle.piece.callback.PieceAnimationCallback;
import com.xs.lightpuzzle.puzzle.piece.util.MatrixUtils;

/**
 * Created by xs on 2018/8/15.
 */

public class LayoutPiece extends BasePiece {
    private LayoutArea mArea;
    private float mScale = 1f;
    /**
     * 构造函数
     * @param drawable
     * @param area
     * @param matrix
     * @param id
     */
    public LayoutPiece(Drawable drawable, LayoutArea area, Matrix matrix, int id) {
        super(drawable, matrix, id);
        mArea = area;
    }

    public void draw(Canvas canvas) {
        draw(canvas, getArea().getAreaRect(mScale), getArea().getRadian());
    }

    public boolean isFilledArea() {
        return isFilledArea(getRectF());
    }

    public boolean canFilledArea() {
        return canFilledArea(getRectF());
    }

    public void set(Matrix matrix) {
        set(getRectF(), matrix);
    }

    public void postZoom(float scale) {
        postZoom(getRectF(), scale);
    }

    public void postRotate(float degree) {
        postRotate(getRectF(), degree);
    }

    public void zoomAndTranslate(float scaleX, float scaleY, PointF midPoint, float offsetX, float offsetY) {
        zoomAndTranslate(getRectF(), scaleX, scaleY, midPoint, offsetX, offsetY);
    }

    public void swapFillArea(final PieceAnimationCallback callback, boolean quick) {
        swapFillArea(getRectF(), callback, quick);
    }

    private void fillRect() {
        fillRect(getRectF());
    }

    public void moveToFillArea(final PieceAnimationCallback callback) {
        moveToFillArea(getRectF(), callback);
    }

    public void fillArea(final PieceAnimationCallback callback, boolean quick) {
        fillArea(getRectF(), callback, quick);
    }


    // 子类附加方法
    public LayoutArea getArea() {
        return mArea;
    }

    public RectF getRectF() {
        return mArea.getAreaRect();
    }

    public Point[] getPointArr() {
        RectF rectF = getRectF();
        Point[] points = new Point[4];
        points[0] = new Point((int) rectF.left, (int) rectF.top);
        points[1] = new Point((int) rectF.left, (int) rectF.bottom);
        points[2] = new Point((int) rectF.right, (int) rectF.bottom);
        points[3] = new Point((int) rectF.right, (int) rectF.top);
        return points;
    }

    public void totalZoom(float scale){
        this.mScale = scale;
    }

    public boolean contains(float x, float y) {
        return getArea().contains(x, y);
    }

    public boolean contains(LayoutLine line) {
        return getArea().contains(line);
    }

    public void animateOfTransArea(final PieceAnimationCallback callback, final Rect lastRect , final Rect nowRect) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float l = lastRect.left + (nowRect.left - lastRect.left) * value;
                float t = lastRect.top + (nowRect.top - lastRect.top) * value;
                float w = lastRect.width() + (nowRect.width() - lastRect.width()) * value;
                float h = lastRect.height() + (nowRect.height() - lastRect.height()) * value;
                resetArea(l, t, l + w, t + h);
                callback.onInvalidate();
            }
        });
        animator.setDuration(getDuration());
        animator.start();
    }

    private void resetArea(float l , float t , float r, float b){
        getArea().resetArea(l, t, r, b);
        float angle = getMatrixAngle();
        set(mArea.getAreaRect() , MatrixUtils.generateMatrix(this, mArea.getAreaRect(), 0f));
        postRotate(mArea.getAreaRect() , angle);
    }

    public void updateWithLine(final LayoutLine line, float offset) {
        LayoutArea mArea = getArea();
        RectF rectF = getCurrentDrawableBounds();
        if (isZoom()) {
            if (!canFilledArea(getArea().getAreaRect())) {
                fillRect(getArea().getAreaRect());
            } else {
                if (line.getDirection() == LayoutLine.Direction.HORIZONTAL) {
                    float drawableHeight = rectF.height();
                    float nowAreaHeight = mArea.height();
                    float lastAreaHeight = 0;
                    float nowAreaScale = mArea.width() / mArea.height();
                    float pieceScale = getWidth() / getHeight();
                    if (nowAreaScale < pieceScale) {
                        PointF lastCenter = new PointF(mArea.getCenterPoint().x, mArea.getCenterPoint().y - offset * 1.0f / 2);
                        if (line.getTowards() == LayoutLine.Towards.TOP) {
                            lastAreaHeight = mArea.height() + offset;
                            float scale = nowAreaHeight / lastAreaHeight;

                            postScale(scale, scale, lastCenter);
                        }
                        if (line.getTowards() == LayoutLine.Towards.BOTTOM) {
                            lastAreaHeight = mArea.height() - offset;
                            float scale = nowAreaHeight / lastAreaHeight;
                            postScale(scale, scale, lastCenter);
                        }
                    }
                } else {
                    float drawableWidth = rectF.width();
                    float nowAreaWidth = mArea.width();
                    float lastAreaWidth = 0;
                    float nowAreaScale = mArea.width() / mArea.height();
                    float pieceScale = getWidth() / getHeight();
                    if (nowAreaScale > pieceScale) {
                        PointF lastCenter = new PointF(mArea.getCenterPoint().x - offset * 1.0f / 2, mArea.getCenterPoint().y);
                        if (line.getTowards() == LayoutLine.Towards.LEFT) {
                            lastAreaWidth = mArea.width() + offset;
                            float scale = nowAreaWidth / lastAreaWidth;
                            postScale(scale, scale, lastCenter);
                        }
                        if (line.getTowards() == LayoutLine.Towards.RIGHT) {
                            lastAreaWidth = mArea.width() - offset;
                            float scale = nowAreaWidth / lastAreaWidth;
                            postScale(scale, scale, lastCenter);
                        }
                    }

                }
            }
        } else {
            fillRect(getArea().getAreaRect());
        }
        if (getMatrixScale() >= MatrixUtils.getMinMatrixScale(this , getArea().getAreaRect())) {
            if (line.getDirection() == LayoutLine.Direction.HORIZONTAL) {
                postTranslate(0, offset * 1.0f / 2);
            } else if (line.getDirection() == LayoutLine.Direction.VERTICAL) {
                postTranslate(offset * 1.0f / 2, 0);
            }
        }
        rectF = getCurrentDrawableBounds();
        mArea = getArea();
        float moveY = 0f;

        if (rectF.top > mArea.top()) {
            moveY = mArea.top() - rectF.top;
        }

        if (rectF.bottom < mArea.bottom()) {
            moveY = mArea.bottom() - rectF.bottom;
        }

        float moveX = 0f;

        if (rectF.left > mArea.left()) {
            moveX = mArea.left() - rectF.left;
        }

        if (rectF.right < mArea.right()) {
            moveX = mArea.right() - rectF.right;
        }

        if (moveX != 0 || moveY != 0) {
            postTranslate(moveX, moveY);
        }
    }

}
