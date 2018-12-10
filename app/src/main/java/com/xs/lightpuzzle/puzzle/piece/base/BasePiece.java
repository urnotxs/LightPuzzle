package com.xs.lightpuzzle.puzzle.piece.base;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.xs.lightpuzzle.puzzle.piece.callback.PieceAnimationCallback;
import com.xs.lightpuzzle.puzzle.piece.util.MatrixUtils;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.ShareData;

/**
 * Created by xs on 2018/8/15.
 */

public class BasePiece {

    private static Xfermode SRC_IN = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private transient final int TOUCH_RESP_FRAME_SIZE = ShareData.PxToDpi_xhdpi(20);//移动切换效果边框
    private ValueAnimator mAnimator;
    private int mDuration = 300;
    private float mMaxScale = 4.0f;
    private boolean isZoom = false;

    private int mId;
    private Drawable mDrawable;
    private Matrix mMatrix;
    private Matrix mPreviousMatrix;
    private Rect mDrawableBounds;
    private float[] mDrawablePoints;
    private boolean isShowFrame;

    /**
     * 构造函数
     *
     * @param drawable
     * @param matrix
     * @param id
     */
    public BasePiece(Drawable drawable, Matrix matrix, int id) {
        this.mId = id;
        this.mMatrix = matrix;
        this.mDrawable = drawable;

        this.mPreviousMatrix = new Matrix();
        this.mDrawableBounds = new Rect(0, 0, getWidth(), getHeight());
        this.mDrawablePoints = new float[]{0f, 0f, getWidth(), 0f, getWidth(), getHeight(), 0f, getHeight()};

        this.mAnimator = ValueAnimator.ofFloat(0f, 1f);
        this.mAnimator.setInterpolator(new DecelerateInterpolator());
    }

    /**
     * 绘制
     *
     * @param canvas
     * @param rectF  经过scale以后的矩形
     * @param radian 半径
     */
    public void draw(Canvas canvas, RectF rectF, float radian) {
        Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
        Paint paint = ((BitmapDrawable) mDrawable).getPaint();
        if (bitmap == null) {
            return;
        }

        if (radian > 0) {
            int saved = canvas.saveLayer(rectF, null, Canvas.ALL_SAVE_FLAG);
            paint.setColor(Color.WHITE);
            paint.setAlpha(255);
            canvas.drawRoundRect(rectF, radian, radian, paint);
            paint.setXfermode(SRC_IN);
            canvas.drawBitmap(bitmap, mMatrix, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(saved);
        } else {
            canvas.save();
            paint.setAntiAlias(true);
            canvas.setDrawFilter(
                    new PaintFlagsDrawFilter(0,
                            Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            if (isShowFrame){
                canvas.clipRect(getFrameRect(rectF));
            }else{
                canvas.clipRect(rectF);
            }
            Log.e("draw", isShowFrame + "");
            paint = PuzzlesUtils.getPuzzleSettingPaint(paint);
            canvas.drawBitmap(bitmap, mMatrix, paint);
            canvas.restore();
        }
    }

    public void drawPolygon(Canvas canvas, RectF rectF, Path path){
        Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
        Paint paint = ((BitmapDrawable) mDrawable).getPaint();

        int saved = canvas.saveLayer(rectF, null, Canvas.ALL_SAVE_FLAG);
        if (isShowFrame){
            canvas.clipRect(getFrameRect(rectF));
        }else{
            canvas.clipRect(rectF);
        }
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        canvas.drawPath(path, paint);
        paint.setXfermode(SRC_IN);
        canvas.drawBitmap(bitmap, mMatrix, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saved);
    }

    private RectF getFrameRect(RectF drawRect){
        return new RectF(drawRect.left + TOUCH_RESP_FRAME_SIZE,
                drawRect.top + TOUCH_RESP_FRAME_SIZE,
                drawRect.right - TOUCH_RESP_FRAME_SIZE,
                drawRect.bottom - TOUCH_RESP_FRAME_SIZE);
    }

    public void setShowFrame(boolean isShowFrame){
        this.isShowFrame = isShowFrame;
    }

    public void setDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
        this.mDrawableBounds = new Rect(0, 0, getWidth(), getHeight());
        this.mDrawablePoints = new float[]{
                0f, 0f, getWidth(), 0f, getWidth(), getHeight(), 0f, getHeight()
        };
    }

    public void set(RectF rectF, Matrix mMatrix) {
        this.mMatrix.set(mMatrix);
        moveToFillArea(rectF, null);
    }

    public void record() {
        mPreviousMatrix.set(mMatrix);
    }

    public boolean isFilledArea(RectF rectF) {
        RectF bounds = getCurrentDrawableBounds();
        return !(bounds.left > rectF.left
                || bounds.top > rectF.top
                || bounds.right < rectF.right
                || bounds.bottom < rectF.bottom);
    }

    public void zoom(float scaleX, float scaleY, PointF midPoint) {
        mMatrix.set(mPreviousMatrix);
        postScale(scaleX, scaleY, midPoint);
    }

    public void translate(float offsetX, float offsetY) {
        mMatrix.set(mPreviousMatrix);
        postTranslate(offsetX, offsetY);
    }

    public void postZoom(RectF rectF, float scale) {
        isZoom = true;
        //+ - 按钮缩放
        float nextScale = getMatrixScale() * scale;

        float minMatrixScale = MatrixUtils.getMinMatrixScale(this, rectF);

        if (nextScale <= minMatrixScale) {
            scale = minMatrixScale / getMatrixScale();
            isZoom = false;// 已经是最小缩放比，即从未缩放过
        } else if (nextScale > mMaxScale * minMatrixScale) {
            scale = (mMaxScale * minMatrixScale) / getMatrixScale();
        }

        postScale(scale, scale, new PointF(rectF.centerX(), rectF.centerY()));
        moveToFillArea(rectF, null);
    }

    public void postScale(float scaleX, float scaleY, PointF midPoint) {
        this.mMatrix.postScale(scaleX, scaleY, midPoint.x, midPoint.y);
    }

    public void postRotate(RectF rectF, float degree) {
        this.mMatrix.postRotate(degree, rectF.centerX(), rectF.centerY());

        float minScale = MatrixUtils.getMinMatrixScale(this, rectF);
        if (!isZoom) {
            //没有放大过
            swapFillArea(rectF, null, false);
        } else {
            if (getMatrixScale() < minScale) {
                final PointF midPoint = new PointF();
                midPoint.set(getCurrentDrawableCenterPoint());

                postScale(minScale / getMatrixScale(), minScale / getMatrixScale(), midPoint);
            }

            if (!MatrixUtils.judgeIsImageContainsBorder(this, rectF, getMatrixAngle())) {
                final float[] imageIndents = MatrixUtils.calculateImageIndents(this, rectF);
                float deltaX = -(imageIndents[0] + imageIndents[2]);
                float deltaY = -(imageIndents[1] + imageIndents[3]);

                postTranslate(deltaX, deltaY);
            }
        }
    }

    public void postTranslate(float x, float y) {
        this.mMatrix.postTranslate(x, y);
    }

    public void zoomAndTranslate(RectF rectF, float scaleX, float scaleY, PointF midPoint, float offsetX, float offsetY) {
        //两指触发缩放
        mMatrix.set(mPreviousMatrix);

        float scale = getMatrixScale() * scaleX;
        float minMatrixScale = MatrixUtils.getMinMatrixScale(this, rectF);
        if (scale > mMaxScale * minMatrixScale) {
            scaleX = (mMaxScale * minMatrixScale) / getMatrixScale();
            scaleY = scaleX;
        }

        postScale(scaleX, scaleY, midPoint);
        postTranslate(offsetX * 1.0f / 2, offsetY * 1.0f / 2);
        isZoom = true;
    }

    public void swapFillArea(RectF rectF, final PieceAnimationCallback callback, boolean quick) {
        record();

        final float startScale = getMatrixScale();
        final float endScale = MatrixUtils.getMinMatrixScale(this, rectF);

        final PointF midPoint = new PointF();
        midPoint.set(getCurrentDrawableCenterPoint());

        Matrix tempMatrix = new Matrix();
        tempMatrix.set(mMatrix);
        tempMatrix.postScale(endScale / startScale, endScale / startScale, midPoint.x, midPoint.y);

        RectF tempDrawableBounds = new RectF(mDrawableBounds);
        tempMatrix.mapRect(tempDrawableBounds);

        float offsetX = 0f;
        float offsetY = 0f;

        if (tempDrawableBounds.left > rectF.left) {
            offsetX = rectF.left - tempDrawableBounds.left;
        }

        if (tempDrawableBounds.top > rectF.top) {
            offsetY = rectF.top - tempDrawableBounds.top;
        }

        if (tempDrawableBounds.right < rectF.right) {
            offsetX = rectF.right - tempDrawableBounds.right;
        }

        if (tempDrawableBounds.bottom < rectF.bottom) {
            offsetY = rectF.bottom - tempDrawableBounds.bottom;
        }

        final float translateX = offsetX;
        final float translateY = offsetY;
        if (callback == null) {
            mMatrix.postScale(endScale / startScale, endScale / startScale, midPoint.x, midPoint.y);
            postTranslate(offsetX, offsetY);
        } else {
            animateOfTransAndScale(callback, translateX, translateY, startScale, endScale, midPoint, quick);
        }
    }

    public void fillRect(RectF rectF) {
        float deltaScale = MatrixUtils.getMinMatrixScale(this, rectF) / getMatrixScale();
        postScale(deltaScale, deltaScale, new PointF(rectF.centerX(), rectF.centerY()));
    }

    public boolean canFilledArea(RectF rectF) {
        float scale = MatrixUtils.getMatrixScale(mMatrix);
        float minScale = MatrixUtils.getMinMatrixScale(this, rectF);
        if (scale < minScale) {
            isZoom = false;
        }
        return scale >= minScale;
    }

    public void moveToFillArea(RectF rectF, final PieceAnimationCallback callback) {
        if (isFilledArea(rectF)) return;
        record();

        RectF tempDrawableBounds = getCurrentDrawableBounds();
        float offsetX = 0f;
        float offsetY = 0f;

        if (tempDrawableBounds.left > rectF.left) {
            offsetX = rectF.left - tempDrawableBounds.left;
        }

        if (tempDrawableBounds.top > rectF.top) {
            offsetY = rectF.top - tempDrawableBounds.top;
        }

        if (tempDrawableBounds.right < rectF.right) {
            offsetX = rectF.right - tempDrawableBounds.right;
        }

        if (tempDrawableBounds.bottom < rectF.bottom) {
            offsetY = rectF.bottom - tempDrawableBounds.bottom;
        }

        if (callback == null) {
            postTranslate(offsetX, offsetY);
        } else {
            animateOfTranslate(callback, offsetX, offsetY);
        }
    }

    public void fillArea(RectF rectF, final PieceAnimationCallback callback, boolean quick) {
        if (isFilledArea(rectF)) return;
        record();

        final float startScale = getMatrixScale();
        final float endScale = MatrixUtils.getMinMatrixScale(this, rectF);

        final PointF midPoint = new PointF();
        midPoint.set(getCurrentDrawableCenterPoint());

        Matrix tempMatrix = new Matrix();
        tempMatrix.set(mMatrix);
        tempMatrix.postScale(endScale / startScale, endScale / startScale, midPoint.x, midPoint.y);

        RectF tempDrawableBounds = new RectF(mDrawableBounds);
        tempMatrix.mapRect(tempDrawableBounds);

        float offsetX = 0f;
        float offsetY = 0f;

        if (tempDrawableBounds.left > rectF.left) {
            offsetX = rectF.left - tempDrawableBounds.left;
        }

        if (tempDrawableBounds.top > rectF.top) {
            offsetY = rectF.top - tempDrawableBounds.top;
        }

        if (tempDrawableBounds.right < rectF.right) {
            offsetX = rectF.right - tempDrawableBounds.right;
        }

        if (tempDrawableBounds.bottom < rectF.bottom) {
            offsetY = rectF.bottom - tempDrawableBounds.bottom;
        }

        final float translateX = offsetX;
        final float translateY = offsetY;
        animateOfTransAndScale(callback, translateX, translateY, startScale, endScale, midPoint, quick);
    }

    private void animateOfTransAndScale(final PieceAnimationCallback callback, final float translateX, final float translateY, final float startScale, final float endScale, final PointF midPoint, boolean quick) {
        mAnimator.end();
        mAnimator.removeAllUpdateListeners();
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float scale = (startScale + (endScale - startScale) * value) / startScale;
                float x = translateX * value;
                float y = translateY * value;

                zoom(scale, scale, midPoint);
                postTranslate(x, y);
                callback.onInvalidate();
            }
        });

        if (quick) {
            mAnimator.setDuration(0);
        } else {
            mAnimator.setDuration(mDuration);
        }
        mAnimator.start();
    }

    private void animateOfTranslate(final PieceAnimationCallback callback, final float translateX, final float translateY) {

        mAnimator.end();
        mAnimator.removeAllUpdateListeners();
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = translateX * (float) animation.getAnimatedValue();
                float y = translateY * (float) animation.getAnimatedValue();

                translate(x, y);
                callback.onInvalidate();
            }
        });
        mAnimator.setDuration(mDuration);
        mAnimator.start();

    }

    private PointF getCurrentDrawableCenterPoint() {
        RectF mapBounds = getCurrentDrawableBounds();
        PointF mapCenterPoint = new PointF();
        mapCenterPoint.x = mapBounds.centerX();
        mapCenterPoint.y = mapBounds.centerY();
        return mapCenterPoint;
    }

    public float[] getCurrentDrawablePoints() {
        float[] mapPoints = new float[8];
        mMatrix.mapPoints(mapPoints, mDrawablePoints);
        return mapPoints;
    }

    public RectF getCurrentDrawableBounds() {
        //将drawable的原宽高通过平移缩放后所得的matrix进行转化为新的矩形
        RectF mapBounds = new RectF();
        mMatrix.mapRect(mapBounds, new RectF(mDrawableBounds));
        return mapBounds;
    }

    public float getMatrixScale() {
        return MatrixUtils.getMatrixScale(mMatrix);
    }

    public float getMatrixAngle() {
        return MatrixUtils.getMatrixAngle(mMatrix);
    }

    public float getMatrixTransX(){
        return MatrixUtils.getMatrixTransX(mMatrix);
    }

    public float getMatrixTransY(){
        return MatrixUtils.getMatrixTransY(mMatrix);
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void initFitMatrix(RectF rectF) {
        mMatrix = MatrixUtils.generateMatrix(this, rectF, 0f);
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public int getWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    public int getHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    public int getId() {
        return mId;
    }

    public void setZoom(boolean zoom) {
        isZoom = zoom;
    }

    public boolean isZoom() {
        return isZoom;
    }

    public int getDuration(){
        return mDuration;
    }

    public boolean isAnimateRunning() {
        return mAnimator.isRunning();
    }

    public void setAnimateDuration(int mDuration) {
        this.mDuration = mDuration;
    }


    // 保存
    public RectF getDrawableRectF(float ratioW, float ratioH) {
        RectF rectF = getCurrentDrawableBounds();
        rectF.left = rectF.left*ratioW;
        rectF.right = rectF.right*ratioW;
        rectF.top = rectF.top*ratioH;
        rectF.bottom = rectF.bottom*ratioH;
        return rectF;
    }


    // 恢复
    public void setCurrentDrawableBounds(RectF mappedBounds , float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(-degree , mappedBounds.centerX() , mappedBounds.centerY());
        matrix.mapRect(mappedBounds);
        mMatrix = generateCurMatrix(new RectF(mDrawableBounds.left,mDrawableBounds.top , mDrawableBounds.right , mDrawableBounds.bottom) , mappedBounds , degree);
    }

    private Matrix generateCurMatrix(RectF srcRect, RectF dstRect, float degree) {
        Matrix matrix = new Matrix();
        float offsetX = dstRect.centerX() - srcRect.width() / 2;
        float offsetY = dstRect.centerY() - srcRect.height() / 2;
        matrix.postTranslate(offsetX, offsetY);

        float scale;

        if (srcRect.width() * dstRect.height() > dstRect.width() * srcRect.height()) {
            scale = (dstRect.height()) / srcRect.height();
        } else {
            scale = (dstRect.width()) / srcRect.width();
        }

        matrix.postScale(scale, scale, dstRect.centerX(), dstRect.centerY());

        matrix.postRotate(degree, dstRect.centerX(), dstRect.centerY());
        return matrix;
    }

}
