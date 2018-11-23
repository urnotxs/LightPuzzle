package com.xs.lightpuzzle.puzzle.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.DecodeImageOptions;
import com.xs.lightpuzzle.imagedecode.core.ImageScaleType;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.param.SignatureSaveVO;
import com.xs.lightpuzzle.puzzle.piece.MatrixUtils;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.ShapeUtils;
import com.xs.lightpuzzle.puzzle.util.SingleTouchUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.signature.SignaturePadHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xs on 2018/4/12.
 * 签名
 */

public class PuzzlesSignInfo implements DrawView {

    private Rect rect;
    //保存时用到的rect
    private Rect outPutRect;

    private PointF[] signPoint;//模板自带资源签名的时候需要用这个计算

    private transient Point[] startPoint;

    private transient Rect startRect;

    private transient PointF[] drawPoint, controlPoint, deletePoint, exChangeDrawPoint;

    private String signPic;

    private transient boolean save;

    private transient Matrix drawMatrix, controlMatrix, deleteMatrix, exChangeMatrix;

    private transient Bitmap drawBitmap, controlBitmap, deleteBitmap;

    private transient Paint paint;

    private transient boolean showFrame, actionDown, actionMove, selectSign, selectControl, selectDelete;

    private transient final float MAX_SCALE = 2.0f;

    private transient PointF centerP = new PointF(), startP = new PointF();
    private transient int mLastHeight;
    private boolean isSignModel;//
    private boolean canDraw = true;
    private int puzzleModel;
    private int mScrollY;
    private float[] matrixValues;
    private SignatureSaveVO mSignatureSaveVO;
    private Bitmap mOriginalBitmap;
    private RectF imageRect;
    private transient float degree = 0;
    private transient boolean firstInit = true;

    private boolean isNeedReload;

    public float[] getMatrixValues() {
        return matrixValues;
    }

    public void matrixToValues() {
        if (drawMatrix != null) {
            if (matrixValues == null) {
                matrixValues = new float[9];
            }
            drawMatrix.getValues(matrixValues);
        }
    }

    public Matrix getDrawMatrix() {
        return drawMatrix;
    }

    /**
     * 如已有默认恢复数据，则初始化的时候要这些数据
     * 用完reset
     */
    public void setDrawInitData(Matrix matrix, PointF[] drawPoint) {
        this.exChangeMatrix = matrix;
        this.exChangeDrawPoint = drawPoint;
    }

    public PointF[] getDrawPoint() {
        return drawPoint;
    }

    public void setDrawPoint(PointF[] drawPoint) {
        this.drawPoint = drawPoint;
    }

    public int getScrollY() {
        return mScrollY;
    }

    public void setScrollY(int scrollY) {
        this.mScrollY = scrollY;
    }

    public void setPuzzleModel(int puzzleModel) {
        this.puzzleModel = puzzleModel;
    }

    public void setSignModel(boolean signModel) {
        isSignModel = signModel;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {

        if (rect == null) {
            return;
        }
        if (this.rect == null) {
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
        }
        if (this.rect.height() != rect.height()) {
            Rect lastRect = this.rect;
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
            if (puzzleModel == PuzzleMode.MODE_LONG) {
                if (drawPoint != null) {
                    int h = (int) drawPoint[2].y;

                    if (h > rect.height()) {
                        reload(rect, lastRect);
                    }
                }

            } else {
                reload(rect, lastRect);
            }
        } else {
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    private void reload(Rect rect, Rect lastRect) {
        if (drawPoint == null || centerP == null || drawMatrix == null) {
            return;
        }
        PointF[] signPoints = drawPoint;
        PointF centerPoint = centerP;
        Matrix matrix = drawMatrix;
        //获取旋转角度
        float degree = PuzzlesUtils.getMatrixAngle(matrix);

        for (int i = 0; i < signPoints.length; i++) {
            signPoints[i] = PuzzlesUtils.rotate(signPoints[i], centerPoint, (float)
                    SingleTouchUtils.degreeToRadian(degree));
        }
        float textWidth = signPoints[1].x - signPoints[0].x;
        float textHeight = signPoints[2].y - signPoints[1].y;
        float left = (signPoints[0].x / lastRect.width()) * rect.width();
        float top = (signPoints[0].y / lastRect.height()) * rect.height();

        // 判断切换比例后文字是否超出范围
        if (left + textWidth > rect.width()) {
            left = rect.width() - textWidth;
        }
        if (top + textHeight > rect.height()) {
            top = rect.height() - textHeight;
        }
        signPoints[0].set(left, top);
        signPoints[1].set(left + textWidth, top);
        signPoints[2].set(left + textWidth, top + textHeight);
        signPoints[3].set(left, top + textHeight);

        matrix.postRotate(-degree, centerPoint.x, centerPoint.y);
        float[] values = new float[9];
        matrix.getValues(values);
        values[Matrix.MTRANS_X] = left;
        values[Matrix.MTRANS_Y] = top;
        centerPoint.set(signPoints[0].x + textWidth / 2, signPoints[0].y + textHeight / 2);
        matrix.setValues(values);
        matrix.postRotate(degree, centerPoint.x, centerPoint.y);
        for (int j = 0; j < signPoints.length; j++) {
            signPoints[j] = PuzzlesUtils.rotate(signPoints[j], centerPoint, (float)
                    SingleTouchUtils.degreeToRadian(-degree));
        }
        drawPoint = signPoints;
        centerP = centerPoint;
        drawMatrix = matrix;
        resetFrameData();
    }

    public void setSignPoint(PointF[] signPoint) {
        this.signPoint = signPoint;
    }

    public void setSignPic(String signPic) {
        this.signPic = signPic;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    public void setShowFrame(boolean showFrame) {
        this.showFrame = showFrame;
    }

    public String getSignPic() {
        return signPic;
    }

    @Override
    public void init() {
        Rect finalRect;
        if (save) {
            if (outPutRect == null) {
                return;
            }
            finalRect = outPutRect;
        } else {
            if (rect == null) {
                return;
            }
            finalRect = rect;
        }
        degree = 0;
        if (signPoint != null) {
            // 模板自带签名
            startPoint = ShapeUtils.makePts(signPoint, finalRect);
            startRect = ShapeUtils.makeRect(startPoint);
        }

        // 创建一个画笔
        if (paint == null) {
            paint = new Paint();
            paint.reset();
            paint.setFilterBitmap(true);//
            paint.setAntiAlias(true);
            paint.setColor(0xfff5808e);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public void initBitmap(Context context) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(signPic)) {
            return;
        }
        if (rect == null) {
            return;
        }
        drawBitmap = null;

        if (isSignModel) {
            // 模板带初始签名位置
            isSignModel = false;
            if (startRect == null) {
                return;
            }

            drawPoint = new PointF[4];
            if (isResourceSign(signPic)) {

                // 自带签名来自资源文件 .png
                DecodeImageOptions decodeImageOptions =
                        new DecodeImageOptions.Builder()
                                .setImageScaleType(ImageScaleType.FIT_XY)
                                .build();
                synchronized (this) {
                    drawBitmap = JaneBitmapFactory.decode(context, signPic, new ImageSize(startRect.width(), startRect
                            .height()), decodeImageOptions);
                }
                if (BitmapHelper.isInvalid(drawBitmap)) {
                    return;
                }
                for (int i = 0; i < drawPoint.length; i++) {
                    drawPoint[i] = new PointF(startPoint[i].x, startPoint[i].y);
                }

            } else {

                // 自带签名来自历史签名 .json
                int rotation = -90;
                drawBitmap = readSignPath(signPic, rect.width() / 4);
                if (BitmapHelper.isInvalid(drawBitmap)) {
                    return;
                }
                // 防止签名出界
                if (startRect.left + drawBitmap.getWidth() > rect.right) {
                    startRect.left = rect.right - drawBitmap.getWidth();
                }
                if (startRect.top + drawBitmap.getHeight() > rect.bottom) {
                    startRect.top = rect.bottom - drawBitmap.getHeight();
                }
                drawPoint = ShapeUtils.makePts(startRect.left, startRect.top, drawBitmap.getWidth(), drawBitmap.getHeight());
            }
        } else {

            int rotation = 0;
            DecodeImageOptions decodeImageOptions;
            if (!isResourceSign(signPic)) {
                rotation = -90;
                decodeImageOptions = new DecodeImageOptions.Builder()
                        .setImageScaleType(ImageScaleType.EXACTLY_WIDTH)
                        .setRotation(rotation)
                        .build();
            } else {
                decodeImageOptions = new DecodeImageOptions.Builder()
                        .setImageScaleType(ImageScaleType.FIT_XY)
                        .setRotation(rotation)
                        .build();
            }


            synchronized (this) {
                int resourceW = 0;
                int resourceH = 0;
                if (startRect != null) {
                    resourceW = startRect.width();
                    resourceH = startRect.height();
                }

                if (save) {
                    if (resourceW <= 0) {
                        resourceW = outPutRect.width() / 3;
                        resourceH = outPutRect.width() / 4;
                    }
                    //资源文件签名会变大
                    int width = isResourceSign(signPic) ? resourceW : outPutRect.width() / 4;
                    int height = isResourceSign(signPic) ? resourceH : outPutRect.width() / 4;
                    // 保存时签名的尺寸
                    if (isResourceSign(signPic)) {
                        drawBitmap = JaneBitmapFactory.decode(context, signPic,
                                new ImageSize(width, height), decodeImageOptions);
                    } else {
                        drawBitmap = readSignPath(signPic, width);
                        isNeedReload = true; // 保存时，需要根据matrix的缩放重新decode签名尺寸
                    }

                } else {
                    if (resourceW <= 0) {
                        resourceW = rect.width() / 3;
                        resourceH = rect.width() / 4;
                    }
                    int width = isResourceSign(signPic) ? resourceW : rect.width() / 4;
                    int height = isResourceSign(signPic) ? resourceH : rect.width() / 4;

                    if (isResourceSign(signPic)) {
                        drawBitmap = JaneBitmapFactory.decode(context, signPic,
                                new ImageSize(width, height), decodeImageOptions);
                    } else {
                        drawBitmap = readSignPath(signPic, width);
                        isNeedReload = true;
                    }
                }
            }
            if (BitmapHelper.isInvalid(drawBitmap)) {
                return;
            }

            if (save) {
                drawPoint = getDrawPoint(outPutRect, drawBitmap);
            } else {
                drawPoint = getDrawPoint(rect, drawBitmap);
            }
        }
        if (!isResourceSign(signPic)) {
            mOriginalBitmap = Bitmap.createBitmap(
                    drawBitmap, 0, 0, drawBitmap.getWidth(), drawBitmap.getHeight(), new Matrix(), true);
        }
        if (drawPoint != null) {
            imageRect = new RectF(drawPoint[0].x, drawPoint[0].y, drawPoint[2].x, drawPoint[2].y);
        }

        if (drawMatrix == null) {
            drawMatrix = new Matrix();
        }

        if (centerP == null) {
            centerP = new PointF();
        }

        if (save && matrixValues != null) {
            drawMatrix.reset();
            float scale;

            scale = (float) outPutRect.width() / (float) rect.width();
            matrixValues[Matrix.MTRANS_X] = (float) Math.ceil(scale * matrixValues[Matrix.MTRANS_X]);
            matrixValues[Matrix.MTRANS_Y] = (float) Math.ceil(scale * matrixValues[Matrix.MTRANS_Y]);
            drawMatrix.setValues(matrixValues);

        } else if (exChangeDrawPoint != null && exChangeMatrix != null) {


            drawMatrix = new Matrix(exChangeMatrix);
            drawPoint = new PointF[exChangeDrawPoint.length];
            for (int i = 0; i < drawPoint.length; i++) {
                PointF pointF = exChangeDrawPoint[i];
                if (pointF != null) {
                    drawPoint[i] = new PointF(pointF.x, pointF.y);
                }
            }


            exChangeMatrix = null;
            exChangeDrawPoint = null;

            initControlBitmap(context);
            initDeleteBitmap(context);
        } else {
            int bitmapWidth = drawBitmap.getWidth();
            int bitmapHeight = drawBitmap.getHeight();
            float scaleWidth = imageRect.width() / (float) bitmapWidth;
            float scaleHeight = imageRect.height() / (float) bitmapHeight;
            if (firstInit) {
                firstInit = false;
                drawMatrix.reset();
                drawMatrix.postScale(scaleWidth, scaleHeight);
                drawMatrix.postTranslate(drawPoint[0].x, drawPoint[0].y);
                initControlBitmap(context);
                initDeleteBitmap(context);

                //设置图片的中心点（）
                if (imageRect != null) {
                    centerP.x = imageRect.centerX();
                    centerP.y = imageRect.centerY();
                } else {
                    centerP.x = 0;
                    centerP.y = 0;
                }
            } else {
                resetMatrixPoints();
            }
        }
    }

    private Bitmap readSignPath(String signPic, int size) {
        mSignatureSaveVO = SignaturePadHelper.getSignatureSaveVO(signPic);
        return decodeSignBitmap(size);
    }

    private Bitmap decodeSignBitmap(int size) {
        return SignaturePadHelper.getSignatureBitmap(mSignatureSaveVO, size, -90,
                size / 100.0f, 0.5f, 30, true);
    }

    private PointF[] getDrawPoint(Rect rect, Bitmap drawBitmap) {
        PointF[] pointFArr = new PointF[4];
        if (rect != null && drawBitmap != null) {
            int bmpWidth = drawBitmap.getWidth();
            int bmpHeight = drawBitmap.getHeight();

            float left = (rect.centerX() - bmpWidth / 2) / (rect.width() * 1.0f);
            float top = rect.centerY() / (rect.height() * 1.0f);
            float right = (rect.centerX() + bmpWidth / 2) / (rect.width() * 1.0f);
            float bottom = (rect.centerY() + bmpHeight) / (rect.height() * 1.0f);

            if (puzzleModel == PuzzleMode.MODE_LONG
                    || puzzleModel == PuzzleMode.MODE_JOIN
                    || puzzleModel == PuzzleMode.MODE_LAYOUT_JOIN) {

                if (rect.height() > Utils.sScreenH * 0.7) {
                    int marginTop = PuzzlesUtils.getViewTop();
                    int marginTmpTop = Utils.getRealPixel3(90) + marginTop;
                    top = (Utils.sScreenH / 2 - marginTmpTop + mScrollY) / (rect.height() * 1.0f);
                    bottom = (Utils.sScreenH / 2 - marginTmpTop + mScrollY + bmpHeight) / (rect.height() * 1.0f);
                }
            }

            pointFArr[0] = new PointF(left, top);
            pointFArr[1] = new PointF(right, top);
            pointFArr[2] = new PointF(right, bottom);
            pointFArr[3] = new PointF(left, bottom);
            return ShapeUtils.makePtsF(pointFArr, rect);
        }
        return null;
    }

    private boolean isResourceSign(String signPic) {
        return false;
//        return !TextUtils.isEmpty(signPic) && signPic.contains(DirConstant.SIGNATURE_RESOURCE_PATH);
    }

    private void resetMatrixPoints() {
        if (drawMatrix == null || imageRect == null || drawBitmap == null) {
            return;
        }

        if (drawPoint != null) {
            imageRect = new RectF(drawPoint[0].x, drawPoint[0].y, drawPoint[2].x, drawPoint[2].y);
        }

        int bitmapWidth = drawBitmap.getWidth();
        int bitmapHeight = drawBitmap.getHeight();
        float scaleWidth = imageRect.width() / (float) bitmapWidth;
        float scaleHeight = imageRect.height() / (float) bitmapHeight;

        degree = getMatrixAngle(drawMatrix);
        // 选择新的签名，根据新签名宽高和之前的中心点计算出来的矩形是否超出边界
        judgeBeyondBorder(bitmapWidth, bitmapHeight);
        // 重新设置点坐标
        drawPoint[0] = new PointF(centerP.x - imageRect.width() / 2, centerP.y - imageRect
                .height() / 2);
        drawPoint[1] = new PointF(centerP.x + imageRect.width() / 2, centerP.y - imageRect
                .height() / 2);
        drawPoint[2] = new PointF(centerP.x + imageRect.width() / 2, centerP.y + imageRect
                .height() / 2);
        drawPoint[3] = new PointF(centerP.x - imageRect.width() / 2, centerP.y + imageRect
                .height() / 2);
        drawMatrix.reset();
        drawMatrix.postScale(scaleWidth, scaleHeight);
        drawMatrix.postTranslate(drawPoint[0].x, drawPoint[0].y);
        drawMatrix.postRotate(degree, centerP.x, centerP.y);
        resetFrameData();
        for (int i = 0; i < drawPoint.length; i++) {
            drawPoint[i] = PuzzlesUtils.rotate(drawPoint[i], centerP, (float)
                    SingleTouchUtils.degreeToRadian(-degree));
        }
    }

    private void judgeBeyondBorder(int bitmapWidth, int bitmapHeight) {
        float left = (centerP.x - bitmapWidth / 2.0f);
        float top = (centerP.y - bitmapHeight / 2.0f);
        float right = centerP.x + bitmapWidth / 2.0f;
        float bottom = centerP.y + bitmapHeight / 2.0f;
        if (left < rect.left) {
            //左边出界
            float offset = rect.left - left;
            centerP.set(centerP.x + offset, centerP.y);
        }
        if (top < rect.top) {
            //上边出界
            float offset = rect.top - top;
            centerP.set(centerP.x, centerP.y + offset);
        }
        if (right > rect.right) {
            //右边出界
            float offset = rect.right - right;
            centerP.set(centerP.x + offset, centerP.y);
        }
        if (bottom > rect.bottom) {
            //下边出界
            float offset = rect.bottom - bottom;
            centerP.set(centerP.x, centerP.y + offset);
        }
    }

    /**
     * 获取旋转角度
     *
     * @param matrix matrix
     * @return float
     */
    private float getMatrixAngle(Matrix matrix) {
        return (float) -(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
                getMatrixValue(matrix, Matrix.MSCALE_X)) * (180 / Math.PI));
    }

    private float getMatrixValue(Matrix matrix, int valueIndex) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[valueIndex];
    }

    private void initControlBitmap(Context context) {
        DecodeImageOptions decodeImageOptions = new DecodeImageOptions.Builder()
                .setImageScaleType(ImageScaleType.NONE)
                .build();
        controlBitmap = JaneBitmapFactory.decode(context, R.drawable.rotatezoom, null, decodeImageOptions);
        if (BitmapHelper.isInvalid(controlBitmap)) {
            return;
        }
        int controlWidth = controlBitmap.getWidth();
        int controlHeight = controlBitmap.getHeight();
        controlPoint = new PointF[4];
        controlPoint[0] = new PointF(drawPoint[2].x - controlWidth / 2, drawPoint[2].y - controlHeight / 2);
        controlPoint[1] = new PointF(drawPoint[2].x + controlWidth / 2, drawPoint[2].y - controlHeight / 2);
        controlPoint[2] = new PointF(drawPoint[2].x + controlWidth / 2, drawPoint[2].y + controlHeight / 2);
        controlPoint[3] = new PointF(drawPoint[2].x - controlWidth / 2, drawPoint[2].y + controlHeight / 2);
        if (controlMatrix == null) {
            controlMatrix = new Matrix();
        }
        controlMatrix.reset();
        controlMatrix.postTranslate(controlPoint[0].x, controlPoint[0].y);
    }

    private void initDeleteBitmap(Context context) {
        DecodeImageOptions decodeImageOptions = new DecodeImageOptions.Builder()
                .setImageScaleType(ImageScaleType.NONE)
                .build();
        deleteBitmap = JaneBitmapFactory.decode(context, R.drawable.red_delete_btn, null, decodeImageOptions);
        if (BitmapHelper.isInvalid(deleteBitmap)) {
            return;
        }
        //获取控制按钮的宽高
        int deleteWidth = deleteBitmap.getWidth();
        int deleteHeight = deleteBitmap.getHeight();
        deletePoint = new PointF[4];
        deletePoint[0] = new PointF(drawPoint[0].x - deleteWidth / 2, drawPoint[0].y - deleteHeight / 2);
        deletePoint[1] = new PointF(drawPoint[0].x + deleteWidth / 2, drawPoint[0].y - deleteHeight / 2);
        deletePoint[2] = new PointF(drawPoint[0].x + deleteWidth / 2, drawPoint[0].y + deleteHeight / 2);
        deletePoint[3] = new PointF(drawPoint[0].x - deleteWidth / 2, drawPoint[0].y + deleteHeight / 2);
        if (deleteMatrix == null) {
            deleteMatrix = new Matrix();
        }
        deleteMatrix.reset();
        deleteMatrix.postTranslate(deletePoint[0].x, deletePoint[0].y);
    }

    private float getDis(PointF startPoint, PointF endPoint) {
        float dx = endPoint.x - startPoint.x;
        float dy = endPoint.y - startPoint.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 长图含多个子模板时
     * 保存绘制
     */
    public void draw(Canvas canvas, int lastHeight) {
        mLastHeight = lastHeight;
        draw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (drawBitmap == null) {
            return;
        }
        if (drawMatrix == null) {
            return;
        }
        if (canDraw) {
            canvas.save();
            canvas.translate(0, -mLastHeight);

            if (isResourceSign(signPic) || (drawBitmap == mOriginalBitmap && !isNeedReload)) {
                // 图片来自于内置签名或者当前处于move状态
                canvas.translate(0, -mLastHeight);
                canvas.drawBitmap(drawBitmap, drawMatrix, null);
            } else {
                float scale = MatrixUtils.getMatrixScale(drawMatrix);
                float width = imageRect.width() * scale;

                if (isNeedReload) {
                    drawBitmap = decodeSignBitmap((int) width);
                    isNeedReload = false;
                }
                if (drawBitmap == null) {
                    return;
                }

                float translateX = MatrixUtils.getMatrixTransX(drawMatrix);
                float translateY = MatrixUtils.getMatrixTransY(drawMatrix);

                float angle = MatrixUtils.getMatrixAngle(drawMatrix);

                Matrix matrix = new Matrix();
                matrix.setRotate(angle);
                matrix.postTranslate(translateX, translateY);

                canvas.drawBitmap(drawBitmap, matrix, paint);
            }

            canvas.restore();

            if (showFrame && !save) {
                if (drawPoint != null) {
                    Path path = ShapeUtils.ptsFToPath(drawPoint);
                    canvas.drawPath(path, paint);
                }
                if (BitmapHelper.isValid(deleteBitmap)) {
                    canvas.drawBitmap(deleteBitmap, deleteMatrix, null);
                }
                if (BitmapHelper.isValid(controlBitmap)) {
                    canvas.drawBitmap(controlBitmap, controlMatrix, null);
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (showFrame) {
            //确认是否再次点中，否则取消选中状态
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (touchInside(event)) {
                    return touch(event);
                } else {
                    showFrame = false;
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_INVALIDATE_VIEW, event.getAction()));
                    return false;
                }
            } else {
                return touch(event);
            }
        } else {
            return showFrameTouch(event);
        }
    }


    /**
     * 拖动签名其他，移动事件，签名移动处理
     *
     * @param event   MotionEvent just move action
     * @param actCode MotionEvent action code
     */
    private boolean selectSignMove(MotionEvent event, int actCode) {
        float result_x = event.getX() - startP.x;
        float result_y = event.getY() - startP.y;
        Rect bitmapRect = PuzzlesUtils.getMaxWidthHeight(drawPoint);
        if (Math.abs(result_x) > 2 || Math.abs(result_y) > 2) {
            actionMove = true;
        }

        if (result_x > 0) {//向右
            if (bitmapRect.right + result_x > rect.right) {
                result_x = 0;
            }
        } else {
            if (bitmapRect.left + result_x < 0) {
                result_x = 0;
            }
        }

        if (result_y > 0) {//向下
            if (bitmapRect.bottom + result_y > rect.bottom) {
                result_y = 0;
            }
        } else {
            if (bitmapRect.top + result_y < 0) {
                result_y = 0;
            }
        }

        if (drawMatrix != null) {
            drawMatrix.postTranslate(result_x, result_y);
        }
        if (drawPoint != null) {
            for (PointF aDrawPoint : drawPoint) {
                aDrawPoint.x += result_x;
                aDrawPoint.y += result_y;
            }
        }
        if (deleteMatrix != null) {
            deleteMatrix.postTranslate(result_x, result_y);
        }
        if (deletePoint != null) {
            for (PointF aDeletePoint : deletePoint) {
                aDeletePoint.x += result_x;
                aDeletePoint.y += result_y;
            }
        }
        if (controlMatrix != null) {
            controlMatrix.postTranslate(result_x, result_y);
        }
        if (controlPoint != null) {
            for (PointF aControlPoint : controlPoint) {
                aControlPoint.x += result_x;
                aControlPoint.y += result_y;
            }
        }
        centerP.set(bitmapRect.centerX(), bitmapRect.centerY());
        startP.set(event.getX(), event.getY());
        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_SIGN, actCode));
        return true;
    }

    private static float getMatrixScale(float[] values) {
        return (float) Math.sqrt(Math.pow(values[Matrix.MSCALE_X], 2) + Math.pow(
                values[Matrix.MSKEW_Y], 2));
    }

    /**
     * 拖动控制按钮，移动事件，进行旋转，放大缩小等处理
     *
     * @param event   MotionEvent just move action
     * @param actCode MotionEvent action code
     */
    private boolean selectControlMoveEvent(MotionEvent event, int actCode) {
        PointF movePoint = new PointF(event.getX(), event.getY());
        float startDis = getDis(centerP, startP);
        float endDis = getDis(centerP, movePoint);
        float scale = endDis / startDis;
        float[] values = new float[9];
        drawMatrix.getValues(values);
        if (scale * getMatrixScale(values) > MAX_SCALE || scale * getMatrixScale(values) < -MAX_SCALE) {
            scale = 1;
        }

        for (PointF aDrawPoint1 : drawPoint) {
            float result_x = (aDrawPoint1.x - centerP.x) * scale + centerP.x;
            float result_y = (aDrawPoint1.y - centerP.y) * scale + centerP.y;
            if (result_x < rect.left || result_x > rect.right || result_y < rect.top || result_y > rect.bottom) {
                scale = 1;
                break;
            }
        }
        if (drawMatrix != null) {
            drawMatrix.postScale(scale, scale, centerP.x, centerP.y);
        }
        float trans_x = 0;
        float trans_y = 0;
        float trans_x1 = 0;
        float trans_y1 = 0;
        if (drawPoint != null) {
            trans_x = drawPoint[0].x;
            trans_y = drawPoint[0].y;
            trans_x1 = drawPoint[2].x;
            trans_y1 = drawPoint[2].y;
            for (PointF aDrawPoint : drawPoint) {
                aDrawPoint.x = (aDrawPoint.x - centerP.x) * scale + centerP.x;
                aDrawPoint.y = (aDrawPoint.y - centerP.y) * scale + centerP.y;
            }
            trans_x = drawPoint[0].x - trans_x;
            trans_y = drawPoint[0].y - trans_y;
            trans_x1 = drawPoint[2].x - trans_x1;
            trans_y1 = drawPoint[2].y - trans_y1;
        }

        if (deletePoint != null) {
            for (PointF aDeletePoint : deletePoint) {
                aDeletePoint.x += trans_x;
                aDeletePoint.y += trans_y;
            }
            if (deleteMatrix != null) {
                deleteMatrix.postTranslate(trans_x, trans_y);
            }
        }
        if (controlPoint != null) {
            for (PointF aControlPoint : controlPoint) {
                aControlPoint.x += trans_x1;
                aControlPoint.y += trans_y1;
            }
            if (controlMatrix != null) {
                controlMatrix.postTranslate(trans_x1, trans_y1);
            }
        }
        // 角度
        double a = SingleTouchUtils.distance4PointF(centerP, startP);
        double b = SingleTouchUtils.distance4PointF(startP, movePoint);
        double c = SingleTouchUtils.distance4PointF(centerP, movePoint);
        double cosb = (a * a + c * c - b * b) / (2 * a * c);
        if (cosb >= 1) {
            cosb = 1f;
        }

        double radian = Math.acos(cosb);
        float angle = (float) SingleTouchUtils.radianToDegree(radian);
        PointF centerToProMove = new PointF((startP.x - centerP.x), (startP.y - centerP.y));
        PointF centerToCurMove = new PointF((movePoint.x - centerP.x), (movePoint.y - centerP.y));
        float result = centerToProMove.x * centerToCurMove.y - centerToProMove.y * centerToCurMove.x;
        if (result < 0) {
            angle = -angle;
        }
        if (drawMatrix != null) {
            drawMatrix.postRotate(angle, centerP.x, centerP.y);
        }
        if (drawPoint != null) {
            for (int i = 0; i < drawPoint.length; i++) {
                drawPoint[i] = PuzzlesUtils.rotate(drawPoint[i], centerP, (float)
                        SingleTouchUtils.degreeToRadian(-angle));
            }
        }
        if (deleteMatrix != null) {
            deleteMatrix.postRotate(angle, centerP.x, centerP.y);
        }
        if (deletePoint != null) {
            for (int i = 0; i < deletePoint.length; i++) {
                deletePoint[i] = PuzzlesUtils.rotate(deletePoint[i], centerP, (float)
                        SingleTouchUtils.degreeToRadian(-angle));
            }
        }
        if (controlMatrix != null) {
            controlMatrix.postRotate(angle, centerP.x, centerP.y);
        }
        if (controlPoint != null) {
            for (int i = 0; i < controlPoint.length; i++) {
                controlPoint[i] = PuzzlesUtils.rotate(controlPoint[i], centerP, (float)
                        SingleTouchUtils.degreeToRadian(-angle));
            }
        }
        startP.set(event.getX(), event.getY());
        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_SIGN, actCode));
        return true;
    }

    /**
     * touch 事件分发处理
     */
    private boolean touch(MotionEvent event) {
        int actCode = event.getAction();
        if (drawPoint == null || controlPoint == null || deletePoint == null) {
            return false;
        }
        List<PointF> sign = Arrays.asList(drawPoint);
        List<PointF> control = Arrays.asList(controlPoint);
        List<PointF> delete = Arrays.asList(deletePoint);
        if (showFrame) {
            switch (actCode) {
                case MotionEvent.ACTION_DOWN:
                    if (PuzzlesUtils.pointFInRegion(control, new Point((int) event.getX(), (int) event.getY()))) {
                        if (!isResourceSign(signPic) && mOriginalBitmap != null) {
                            drawBitmap = mOriginalBitmap;
                        }
                        selectSign = false;
                        selectControl = true;
                        selectDelete = false;
                        actionDown = true;
                        actionMove = false;
                        Rect bitmapRect = PuzzlesUtils.getMaxWidthHeight(drawPoint);
                        centerP.set(bitmapRect.centerX(), bitmapRect.centerY());
                        startP.set(event.getX(), event.getY());
                        return true;
                    }
                    if (PuzzlesUtils.pointFInRegion(delete, new Point((int) event.getX(), (int) event.getY()))) {
                        if (!isResourceSign(signPic) && mOriginalBitmap != null) {
                            drawBitmap = mOriginalBitmap;
                        }
                        selectSign = false;
                        selectControl = false;
                        selectDelete = true;
                        actionDown = true;
                        actionMove = false;
                        return true;
                    }
                    if (PuzzlesUtils.pointFInRegion(sign, new Point((int) event.getX(), (int) event.getY()))) {
                        if (!isResourceSign(signPic) && mOriginalBitmap != null) {
                            drawBitmap = mOriginalBitmap;
                        }
                        selectSign = true;
                        selectControl = false;
                        selectDelete = false;
                        actionDown = true;
                        actionMove = false;
                        Rect bitmapRect = PuzzlesUtils.getMaxWidthHeight(drawPoint);
                        centerP.set(bitmapRect.centerX(), bitmapRect.centerY());
                        startP.set(event.getX(), event.getY());
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (actionDown) {
                        if (selectDelete) {
                            return true;
                        }
                        if (selectControl) {
                            return selectControlMoveEvent(event, actCode);
                        }
                        if (selectSign) {
                            return selectSignMove(event, actCode);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    actionDown = false;
                    if (selectDelete) {
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_SIGN_DELETE, actCode));
                        return true;
                    }

                    if (!isResourceSign(signPic)) {
                        // 重新decode
                        isNeedReload = true;
                    }

                    // 手指离开的时候还是在签名处, 则显示边框
                    if (selectSign && !actionMove) {
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_SIGN_EDIT, MotionEvent.ACTION_UP, signPic));
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * 判断是否点击当前签名
     *
     * @param event current MotionEvent
     * @return if is touch Inside,return true;
     */
    private boolean touchInside(MotionEvent event) {
        if (drawPoint == null || controlPoint == null || deletePoint == null) {
            return false;
        }

        List<PointF> sign = Arrays.asList(drawPoint);
        List<PointF> control = Arrays.asList(controlPoint);
        List<PointF> delete = Arrays.asList(deletePoint);

        if (PuzzlesUtils.pointFInRegion(sign, new Point((int) event.getX(), (int) event.getY()))) {
            return true;
        } else if (PuzzlesUtils.pointFInRegion(control, new Point((int) event.getX(), (int) event.getY()))) {
            return true;
        } else if (PuzzlesUtils.pointFInRegion(delete, new Point((int) event.getX(), (int) event.getY()))) {
            return true;
        }
        return false;
    }

    private boolean showFrameTouch(MotionEvent event) {
        int actCode = event.getAction();
        if (drawPoint == null) {
            return false;
        }
        switch (actCode) {
            case MotionEvent.ACTION_DOWN:
                if (!showFrame && touchInside(event)) {
                    actionDown = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (actionDown) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (actionDown) {
                    actionDown = false;
                    if (!showFrame) {
                        showFrame = true;
                    }
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_SIGN_SHOW_FRAME, actCode));
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void recycle() {
        drawBitmap = null;
        deleteBitmap = null;
        controlBitmap = null;
    }

    public void resetFrameData() {
        if (deleteBitmap == null || controlBitmap == null) {
            return;
        }

        int deleteWidth = deleteBitmap.getWidth();
        int deleteHeight = deleteBitmap.getHeight();
        int controlWidth = controlBitmap.getWidth();
        int controlHeight = controlBitmap.getHeight();

        deleteMatrix.reset();
        deleteMatrix.postTranslate(drawPoint[0].x - deleteWidth / 2, drawPoint[0].y - deleteHeight / 2);
        deletePoint = new PointF[4];
        deletePoint[0] = new PointF(drawPoint[0].x - deleteWidth / 2, drawPoint[0].y - deleteHeight / 2);
        deletePoint[1] = new PointF(drawPoint[0].x + deleteWidth / 2, drawPoint[0].y - deleteHeight / 2);
        deletePoint[2] = new PointF(drawPoint[0].x + deleteWidth / 2, drawPoint[0].y + deleteHeight / 2);
        deletePoint[3] = new PointF(drawPoint[0].x - deleteWidth / 2, drawPoint[0].y + deleteHeight / 2);
        controlMatrix.reset();
        controlMatrix.postTranslate(drawPoint[2].x - controlWidth / 2, drawPoint[2].y - controlHeight / 2);
        controlPoint = new PointF[4];
        controlPoint[0] = new PointF(drawPoint[2].x - controlWidth / 2, drawPoint[2].y - controlHeight / 2);
        controlPoint[1] = new PointF(drawPoint[2].x + controlWidth / 2, drawPoint[2].y - controlHeight / 2);
        controlPoint[2] = new PointF(drawPoint[2].x + controlWidth / 2, drawPoint[2].y + controlHeight / 2);
        controlPoint[3] = new PointF(drawPoint[2].x - controlWidth / 2, drawPoint[2].y + controlHeight / 2);

        if (degree != 0) {
            if (deleteMatrix != null) {
                deleteMatrix.postRotate(degree, centerP.x, centerP.y);
            }
            if (deletePoint != null) {
                for (int i = 0; i < deletePoint.length; i++) {
                    deletePoint[i] = PuzzlesUtils.rotate(deletePoint[i], centerP, (float)
                            SingleTouchUtils.degreeToRadian(-degree));
                }
            }
            if (controlMatrix != null) {
                controlMatrix.postRotate(degree, centerP.x, centerP.y);
            }
            if (controlPoint != null) {
                for (int i = 0; i < controlPoint.length; i++) {
                    controlPoint[i] = PuzzlesUtils.rotate(controlPoint[i], centerP, (float)
                            SingleTouchUtils.degreeToRadian(-degree));
                }
            }
        }
    }
}
