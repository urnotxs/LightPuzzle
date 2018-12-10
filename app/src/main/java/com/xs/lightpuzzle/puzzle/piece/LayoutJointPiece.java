package com.xs.lightpuzzle.puzzle.piece;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.xs.lightpuzzle.puzzle.piece.base.BasePiece;
import com.xs.lightpuzzle.puzzle.piece.callback.PieceAnimationCallback;

/**
 * Created by xs on 2018/8/15.
 */

public class LayoutJointPiece extends BasePiece {
    private RectF mRectF;
    private float mRadian; // 半径
    private float mScale = 1f;

    /**
     * 构造函数
     *
     * @param drawable
     * @param matrix
     * @param id
     */
    public LayoutJointPiece(Drawable drawable, RectF rectF, Matrix matrix, int id) {
        super(drawable, matrix, id);
        mRectF = rectF;
    }

    public void draw(Canvas canvas) {
        draw(canvas, getRectWithScale(mScale), mRadian);
    }

    public void drawPolygon(Canvas canvas, Path path){
        drawPolygon(canvas, getRectWithScale(mScale), path);
    }

    public boolean isFilledArea() {
        return isFilledArea(mRectF);
    }

    public boolean canFilledArea() {
        return canFilledArea(mRectF);
    }

    public void set(Matrix matrix) {
        set(mRectF, matrix);
    }

    public void setRectF(RectF rectF) {
        mRectF = rectF;
    }

    public void updateRect(RectF rectF) {
        RectF lastRect = new RectF(mRectF.left, mRectF.top, mRectF.right, mRectF.bottom);
        mRectF = rectF;
        if (!isZoom() && getMatrixAngle() == 0) {
            // 没有放大 且旋转角度为0
            initFitMatrix(mRectF);
        } else {
            float disX = mRectF.width() - lastRect.width();
            float disY = mRectF.height() - lastRect.height();
            float ratioX = getWidth() / lastRect.width();
            float ratioY = getHeight() / lastRect.height();
            float scaleW = (getWidth() + disX * ratioX) / getWidth();
            float scaleH = (getHeight() + disY * ratioY) / getHeight();
            PointF lastCenterPoint = new PointF(lastRect.centerX(), lastRect.centerY());
            PointF newCenterPoint = new PointF(mRectF.centerX(), mRectF.centerY());
            float offsetX = newCenterPoint.x - lastCenterPoint.x;
            float offsetY = newCenterPoint.y - lastCenterPoint.y;

            //先缩放后平移
            postScale(scaleW, scaleH, lastCenterPoint);
            postTranslate(offsetX, offsetY);
        }
    }

    public void postZoom(float scale) {
        postZoom(mRectF, scale);
    }

    public void postRotate(float degree) {
        postRotate(mRectF, degree);
    }

    public void zoomAndTranslate(float scaleX, float scaleY, PointF midPoint, float offsetX, float offsetY) {
        zoomAndTranslate(mRectF, scaleX, scaleY, midPoint, offsetX, offsetY);
    }

    public void swapFillArea(final PieceAnimationCallback callback, boolean quick) {
        swapFillArea(mRectF, callback, quick);
    }

    private void fillRect() {
        fillRect(mRectF);
    }

    public void moveToFillArea(final PieceAnimationCallback callback) {
        moveToFillArea(mRectF, callback);
    }

    public void fillArea(final PieceAnimationCallback callback, boolean quick) {
        fillArea(mRectF, callback, quick);
    }


    // 子类附加方法
    public RectF getRectF() {
        return mRectF;
    }

    public boolean contains(float x, float y) {
        return mRectF.contains(x, y);
    }

    public void setRadian(float mRadian) {
        this.mRadian = mRadian;
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

    public RectF getRectWithScale(float scale) {
        float widthOff = (mRectF.width() - mRectF.width() * scale) / 2;
        float heightOff = (mRectF.height() - mRectF.height() * scale) / 2;
        return new RectF(
                mRectF.left + widthOff,
                mRectF.top + heightOff,
                mRectF.right - widthOff,
                mRectF.bottom - heightOff);
    }

    public void totalZoom(float scale) {
        this.mScale = scale;
    }

}
