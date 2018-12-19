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
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.ImageScaleType;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.data.editdata.TemporaryTextData;
import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.util.PuzzleTextUtils;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.SingleTouchUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;

import static com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName.PUZZLES_ADD_TEXT;

/**
 * Created by xs on 2018/4/12.
 */

public class PuzzlesAddTextInfo implements DrawView {

    // 基本数据
    private Rect rect;
    private Rect outPutRect;
    private int id;
    private String autoStr; // 输入文本
    private String font; // 当前字体
    private String alignment = "Center"; // 对齐方式
    private int fontSize; // 当前字体大小
    private int fontColor; // 当前字体颜色
    private int scrollY;
    private boolean downloadFont = true; // 是否是下载的字体 // 全部都是下载的文字，没有内置
    private int puzzleModel;


    // touch交互参数
    private float degree;
    private boolean isDown;
    private boolean isShowFrame;
    private boolean isSelectDelete;
    private boolean isSelectControl;
    private boolean isMove;
    private boolean isSelectAddText;


    // 绘图参数
    private Rect imageRect; // 绘制的最大区域
    private transient Path path;
    private int lineSpace; // 当前的行间距
    private boolean canDraw = true;
    private transient Paint paint;
    private transient Paint linePaint;
    private PointF startPoint = new PointF();
    private PointF centerPoint = new PointF();
    private transient Bitmap deleteIconBmp;
    private transient Bitmap controlIconBmp;
    private int controlWidth;
    private int controlHeight;
    private int deleteWidth;
    private int deleteHeight;
    private PointF[] textPoint = new PointF[4];
    private PointF[] deletePoint = new PointF[4];
    private PointF[] controlPoint = new PointF[4];
    private transient Matrix mMatrix = new Matrix();
    private transient Matrix mDeleteMatrix = new Matrix();
    private transient Matrix mControlMatrix = new Matrix();
    private float[] matrixValue; // 只有走进保存 这里才会bu'wei'kon

    // 重设参数
    private transient boolean reloadFont = true;
    private transient boolean reloadFontSize = true;
    private transient boolean isFirstInit = true;
    private transient int mLastHeight;

    private transient boolean save;

    public void setImageRect(Rect rect) {
        this.imageRect = rect;
    }

    public Rect getImageRect() {
        return imageRect;
    }

    public PointF getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(PointF centerPoint) {
        this.centerPoint = centerPoint;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public PointF[] getTextPoint() {
        return textPoint;
    }

    private boolean isBack = false;

    public void setTextPoint(PointF[] textPoint) {
        this.textPoint = textPoint;
        isBack = true;
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
            //切换布局比例时做适配
            Rect lastRect = this.rect;
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);

            if (puzzleModel == PuzzleMode.MODE_LONG) {
                if (textPoint != null && textPoint.length > 2 && (int) textPoint[2].y > rect.height()) {
                    reloadRect(rect, lastRect);
                }
            } else {
                reloadRect(rect, lastRect);
            }
        } else {
            this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    private void reloadRect(Rect rect, Rect lastRect) {
        PointF[] lastTextPoint = textPoint;
        PointF lastCenterPoint = centerPoint;
        Matrix matrix = mMatrix;
        Rect lastImageRect = imageRect;
        if (lastTextPoint != null && lastCenterPoint != null && matrix != null && lastImageRect != null) {
            for (int j = 0; j < lastTextPoint.length; j++) {
                lastTextPoint[j] = PuzzlesUtils.rotate(lastTextPoint[j], lastCenterPoint, (float)
                        SingleTouchUtils.degreeToRadian(degree));
            }
            float textWidth = lastTextPoint[1].x - lastTextPoint[0].x;
            float textHeight = lastTextPoint[2].y - lastTextPoint[1].y;
            float left = (lastTextPoint[0].x / lastRect.width()) * rect.width();
            float top = (lastTextPoint[0].y / lastRect.height()) * rect.height();

            // 判断切换比例后文字是否超出范围
            if (left + textWidth > rect.width()) {
                left = rect.width() - textWidth;
            }
            if (top + textHeight > rect.height()) {
                top = rect.height() - textHeight;
            }

            lastTextPoint[0].set(left, top);
            lastTextPoint[1].set(left + textWidth, top);
            lastTextPoint[2].set(left + textWidth, top + textHeight);
            lastTextPoint[3].set(left, top + textHeight);

            lastCenterPoint.set(lastTextPoint[0].x + textWidth / 2, lastTextPoint[0].y + textHeight / 2);

            Matrix resultMatrix = new Matrix();
            resultMatrix.postTranslate(left, top);
            resultMatrix.postRotate(degree, lastCenterPoint.x, lastCenterPoint.y);
            lastImageRect.set((int) ((textWidth - lastImageRect.width()) / 2 + left), (int) (top),
                    (int) ((textWidth - lastImageRect.width()) / 2 + left + lastImageRect.width()), (int) (top + lastImageRect.height()));
            for (int j = 0; j < lastTextPoint.length; j++) {
                lastTextPoint[j] = PuzzlesUtils.rotate(lastTextPoint[j], lastCenterPoint, (float)
                        SingleTouchUtils.degreeToRadian(-degree));
            }
            textPoint = lastTextPoint;
            mMatrix = resultMatrix;
            centerPoint = lastCenterPoint;
            imageRect = lastImageRect;
        }
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    @Override
    public void init() {
        // 内部生成画笔
        if (paint == null) {
            paint = new Paint();
        }

        if (linePaint == null) {
            linePaint = new Paint();
        }
        linePaint.setFilterBitmap(true);
        linePaint.setAntiAlias(true);
        linePaint.setColor(0xffffffff);
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.STROKE);

        if (save) {
            if (outPutRect == null) {
                return;
            }
            if (rect != null) {
                isShowFrame = false;
                float scale = (float) outPutRect.width() / (float) rect.width();
                fontSize = (int) (fontSize * scale);
                lineSpace = (int) (lineSpace * scale);
                imageRect.set((int) (imageRect.left * scale), (int) (imageRect.top * scale), (int) (imageRect.right * scale), (int) (imageRect.bottom * scale));
            }
        } else {
            if (imageRect == null) {
                // 确定最大绘图区域, 一旦确定则大小不再更改
                imageRect = new Rect();
                if (rect != null) {
                    float imgRectWidth = rect.width() * 0.8f;
                    float imgRectHeight = rect.width() * 0.8f;
                    int left = (int) (rect.centerX() - imgRectWidth / 2);
                    int top = rect.centerY();
                    int right = (int) (rect.centerX() + imgRectWidth / 2);
                    int bottom = (int) (rect.centerY() + imgRectHeight);

                    if (puzzleModel == PuzzleMode.MODE_LONG
                            || puzzleModel == PuzzleMode.MODE_JOIN
                            || puzzleModel == PuzzleMode.MODE_LAYOUT_JOIN) {

                        if (rect.height() > Utils.sScreenH * 0.7) {
                            int marginTop = PuzzlesUtils.getViewTop();
                            int marginTmpTop = PuzzlesUtils.getTopBarHeight() + marginTop;
                            top = Utils.sScreenH / 2 - marginTmpTop + scrollY;
                            bottom = top + (int) imgRectHeight;
                        }
                    }
                    imageRect.set(left, top, right, bottom);
                }
            }
        }


    }

    @Override
    public void initBitmap(Context context) {
        if (paint == null) {
            paint = new Paint();
        }
        paint.reset();
        setFontSize(fontSize);
        setFontColor(fontColor);
        setFont(context, font);

        // 初始化的时候测量起始位置
        if (isFirstInit) {
            isFirstInit = false;
            LinkedList<String> lineStrs = getLineStrs();
            if (lineStrs.size() > 0) {
                float maxWidth = 0;
                float maxHeight = lineStrs.size() * (paint.descent() * 2 - paint.ascent()) + (lineStrs.size() - 1) * lineSpace;
                for (int i = 0; i < lineStrs.size(); i++) {
                    String line = lineStrs.get(i);
                    float lineWidth = paint.measureText(line);
                    if (lineWidth > maxWidth) {
                        maxWidth = lineWidth;
                    }
                }

                float scaleWidth = (float) imageRect.width() / maxWidth;
                float scaleHeight = (float) imageRect.height() / maxHeight;
                mMatrix.reset();
                mMatrix.postScale(scaleWidth, scaleHeight);
                mMatrix.postTranslate((imageRect.width() - maxWidth) / 2 + imageRect.left, imageRect.top);

                if (!isBack) {
                    textPoint = new PointF[4];
                    textPoint[0] = new PointF(
                            (imageRect.width() - maxWidth) / 2 + imageRect.left, imageRect.top); //左上
                    textPoint[1] = new PointF(
                            imageRect.right - (imageRect.width() - maxWidth) / 2, imageRect.top); //右上
                    textPoint[2] = new PointF(
                            imageRect.right - (imageRect.width() - maxWidth) / 2, imageRect.top + maxHeight);  //右下
                    textPoint[3] = new PointF(
                            (imageRect.width() - maxWidth) / 2 + imageRect.left, imageRect.top + maxHeight);// 左下

                    centerPoint.set((textPoint[1].x - textPoint[0].x) / 2 + textPoint[0].x,
                            (textPoint[2].y - textPoint[1].y) / 2 + textPoint[1].y);
                } else {
                    isBack = false;
                }

                initDeleteIcon(context);
                initControlIcon(context);
                initPath();
            }
        }
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
        if (canDraw) {
            canvas.save();
            //debug模式可以开启
//        paint.setColor(PuzzleUtils.strColor2Int("00ff00"));
//        canvas.drawRect(imageRect, paint);
//        paint.setColor(fontColor);
            canvas.translate(0, -mLastHeight);
            canvas.rotate(degree, centerPoint.x, centerPoint.y);
            if (imageRect != null && paint != null) {
                if (rect != null && autoStr != null) {
                    //每一行所绘制的行数
                    LinkedList<String> lineStrs = getLineStrs();
                    if (lineStrs.size() > 0) {
                        float maxWidth = 0;
                        float maxHeight = lineStrs.size() * (paint.descent() * 2 - paint.ascent()) + (lineStrs.size() - 1) * lineSpace;
                        for (int i = 0; i < lineStrs.size(); i++) {
                            String line = lineStrs.get(i);
                            float start_x = imageRect.left;
                            float start_y = imageRect.top + (lineSpace + paint.descent()) * i - paint.ascent() * (i + 1);
                            float lineWidth = paint.measureText(line);
                            if (lineWidth > maxWidth) {
                                maxWidth = lineWidth;
                            }
                            if (alignment.equals("Right")) {
                                start_x += imageRect.width() - lineWidth;
                            } else if (alignment.equals("Center")) {
                                start_x += (imageRect.width() - lineWidth) / 2;
                            }
                            canvas.drawText(line, start_x, start_y, paint);
                        }
                        if (textPoint != null) {
                            for (int i = 0; i < textPoint.length; i++) {
                                textPoint[i] = PuzzlesUtils.rotate(textPoint[i], centerPoint, (float)
                                        degreeToRadian(degree));
                            }
                            Rect textRect = PuzzlesUtils.getMaxWidthHeight(textPoint);
                            centerPoint.set(textRect.centerX(), textRect.centerY());
                            float resutl_x = ((int) maxWidth - textRect.width()) / 2;
                            float resutl_y = ((int) maxHeight - textRect.height());

                            textPoint[0].x -= resutl_x;
                            textPoint[1].x += resutl_x;
                            textPoint[2].x += resutl_x;
                            textPoint[3].x -= resutl_x;

                            textPoint[2].y += resutl_y;
                            textPoint[3].y += resutl_y;

                            mDeleteMatrix.reset();
                            mDeleteMatrix.postTranslate(textPoint[0].x - deleteWidth / 2, textPoint[0].y - deleteHeight / 2);
                            if (deletePoint == null) {
                                deletePoint = new PointF[4];
                            }
                            deletePoint[0] = new PointF(textPoint[0].x - deleteWidth / 2, textPoint[0].y - deleteHeight / 2);
                            deletePoint[1] = new PointF(textPoint[0].x + deleteWidth / 2, textPoint[0].y - deleteHeight / 2);
                            deletePoint[2] = new PointF(textPoint[0].x + deleteWidth / 2, textPoint[0].y + deleteHeight / 2);
                            deletePoint[3] = new PointF(textPoint[0].x - deleteWidth / 2, textPoint[0].y + deleteHeight / 2);

                            mControlMatrix.reset();
                            mControlMatrix.postTranslate(textPoint[2].x - controlWidth / 2, textPoint[2].y - controlHeight / 2);
                            if (controlPoint == null) {
                                controlPoint = new PointF[4];
                            }
                            controlPoint[0] = new PointF(textPoint[2].x - controlWidth / 2, textPoint[2].y - controlHeight / 2);
                            controlPoint[1] = new PointF(textPoint[2].x + controlWidth / 2, textPoint[2].y - controlHeight / 2);
                            controlPoint[2] = new PointF(textPoint[2].x + controlWidth / 2, textPoint[2].y + controlHeight / 2);
                            controlPoint[3] = new PointF(textPoint[2].x - controlWidth / 2, textPoint[2].y + controlHeight / 2);

                            if (mMatrix != null) {
                                mMatrix.postRotate(degree, centerPoint.x, centerPoint.y);
                            }
                            if (textPoint != null) {
                                for (int i = 0; i < textPoint.length; i++) {
                                    textPoint[i] = PuzzlesUtils.rotate(textPoint[i], centerPoint, (float)
                                            degreeToRadian(-degree));
                                }
                            }
                            if (mDeleteMatrix != null) {
                                mDeleteMatrix.postRotate(degree, centerPoint.x, centerPoint.y);
                            }

                            if (deletePoint != null) {
                                for (int i = 0; i < deletePoint.length; i++) {
                                    deletePoint[i] = PuzzlesUtils.rotate(deletePoint[i], centerPoint, (float)
                                            degreeToRadian(-degree));
                                }
                            }
                            if (mControlMatrix != null) {
                                mControlMatrix.postRotate(degree, centerPoint.x, centerPoint.y);
                            }
                            if (controlPoint != null) {
                                for (int i = 0; i < controlPoint.length; i++) {
                                    controlPoint[i] = PuzzlesUtils.rotate(controlPoint[i], centerPoint, (float)
                                            degreeToRadian(-degree));
                                }
                            }
                        }
                    }
                }
                canvas.restore();
                if (isShowFrame) {
                    initPath();
                    //画路径
                    if (path != null) {
                        canvas.drawPath(path, linePaint);
                    }
                    //画delete按钮和control按钮
                    if (deleteIconBmp != null && deleteIconBmp != null
                            && !deleteIconBmp.isRecycled()) {
                        canvas.drawBitmap(deleteIconBmp, mDeleteMatrix, null);
                    }
                    if (controlIconBmp != null && controlIconBmp != null
                            && !controlIconBmp.isRecycled()) {
                        canvas.drawBitmap(controlIconBmp, mControlMatrix, null);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        Rect bitmapRect = PuzzlesUtils.getMaxWidthHeight(textPoint);
        Rect controlRect = PuzzlesUtils.getMaxWidthHeight(controlPoint);
        Rect deleteRect = PuzzlesUtils.getMaxWidthHeight(deletePoint);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (controlRect != null && x > controlRect.left && x < controlRect.right && y > controlRect.top &&
                        y < controlRect.bottom) {
                    isSelectControl = true;
                    isSelectDelete = false;
                    isSelectAddText = false;
                    centerPoint.set(bitmapRect.centerX(), bitmapRect.centerY());
                    startPoint.set(x, y);
                    setShowFrame(true);
                    Log.d("PuzzleAddTextInfo", "选中了control");
                    return true;
                }
                if (deleteRect != null && x > deleteRect.left && x < deleteRect.right && y > deleteRect.top && y < deleteRect.bottom) {
                    isSelectControl = false;
                    isSelectDelete = true;
                    isSelectAddText = false;
                    Log.d("PuzzleAddTextInfo", "选中了delete");
                    setShowFrame(true);
                    return true;
                }
                if (bitmapRect != null && x > bitmapRect.left && x < bitmapRect.right && y > bitmapRect.top && y < bitmapRect.bottom) {
                    isSelectControl = false;
                    isSelectDelete = false;
                    isSelectAddText = true;
                    isDown = true;
                    isMove = false;
                    startPoint.set(x, y);
                    Log.d("PuzzleAddTextInfo", "选中了文字");
                    setShowFrame(true);
                    return true;
                }

                isSelectControl = false;
                isSelectDelete = false;
                isSelectAddText = false;
                isMove = false;
                isDown = false;
                //若选中的不是输入文本的区域, 如果显示了边框和删除, 则去掉
                if (isShowFrame) {
                    isShowFrame = false;
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PUZZLES_ADD_TEXT, 0));
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isShowFrame) {
                    if (isSelectControl) {
                        isMove = true;
                        PointF movePoint = new PointF(x, y);

                        // 角度
                        double a = distance4PointF(centerPoint, startPoint);
                        double b = distance4PointF(startPoint, movePoint);
                        double c = distance4PointF(centerPoint, movePoint);
                        double cosb = (a * a + c * c - b * b) / (2 * a * c);
                        if (cosb >= 1) {
                            cosb = 1f;
                        }
                        double radian = Math.acos(cosb);
                        float nowDegree = (float) radianToDegree(radian);
                        PointF centerToProMove = new PointF((startPoint.x - centerPoint.x), (startPoint.y - centerPoint.y));
                        PointF centerToCurMove = new PointF((movePoint.x - centerPoint.x), (movePoint.y - centerPoint.y));
                        float result = centerToProMove.x * centerToCurMove.y - centerToProMove.y * centerToCurMove.x;
                        if (result < 0) {
                            nowDegree = -nowDegree;
                        }

                        if (mMatrix != null) {
                            mMatrix.postRotate(nowDegree, centerPoint.x, centerPoint.y);
                        }
                        if (textPoint != null) {
                            for (int i = 0; i < textPoint.length; i++) {
                                textPoint[i] = PuzzlesUtils.rotate(textPoint[i], centerPoint, (float)
                                        degreeToRadian(-nowDegree));
                            }
                        }
                        if (mDeleteMatrix != null) {
                            mDeleteMatrix.postRotate(nowDegree, centerPoint.x, centerPoint.y);
                        }
                        if (deletePoint != null) {
                            for (int i = 0; i < deletePoint.length; i++) {
                                deletePoint[i] = PuzzlesUtils.rotate(deletePoint[i], centerPoint, (float)
                                        degreeToRadian(-nowDegree));
                            }
                        }
                        if (mControlMatrix != null) {
                            mControlMatrix.postRotate(nowDegree, centerPoint.x, centerPoint.y);
                        }
                        if (controlPoint != null) {
                            for (int i = 0; i < controlPoint.length; i++) {
                                controlPoint[i] = PuzzlesUtils.rotate(controlPoint[i], centerPoint, (float)
                                        degreeToRadian(-nowDegree));
                            }
                        }

                        degree += nowDegree;
                        startPoint.set(x, y);
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PUZZLES_ADD_TEXT, 0));
                        return true;
                    }
                    if (isSelectAddText) {
                        if (Math.abs(x - startPoint.x) > 8 || Math.abs(y - startPoint.y) > 8) {
                            isMove = true;
                        }
                        float result_x = x - startPoint.x;
                        float result_y = y - startPoint.y;
                        if (result_x > 0) {
                            if (bitmapRect.right + result_x > rect.right) {
                                result_x = 0;
                            }
                        } else {
                            if (bitmapRect.left + result_x < 0) {
                                result_x = 0;
                            }
                        }
                        if (result_y > 0) {
                            if (bitmapRect.bottom + result_y > rect.bottom) {
                                result_y = 0;
                            }
                        } else {
                            if (bitmapRect.top + result_y < 0) {
                                result_y = 0;
                            }
                        }
                        centerPoint.set(centerPoint.x + result_x, centerPoint.y + result_y);
                        //移动的时候, 最大显示区域也需要移动
                        if (imageRect != null) {
                            imageRect.set((int) (imageRect.left + result_x), (int) (imageRect.top + result_y), (int) (imageRect
                                    .right + result_x), (int) (imageRect.bottom + result_y));
                        }

                        if (mMatrix != null) {
                            mMatrix.postTranslate(result_x, result_y);
                        }
                        if (textPoint != null) {
                            for (int i = 0; i < textPoint.length; i++) {
                                PointF pointF = textPoint[i];
                                if (pointF != null) {
                                    pointF.x += result_x;
                                    pointF.y += result_y;
                                }
                            }
                        }
                        if (mDeleteMatrix != null) {
                            mDeleteMatrix.postTranslate(result_x, result_y);
                        }
                        if (deletePoint != null) {
                            for (int i = 0; i < deletePoint.length; i++) {
                                PointF pointF = deletePoint[i];
                                if (pointF != null) {
                                    pointF.x += result_x;
                                    pointF.y += result_y;
                                }
                            }
                        }
                        if (mControlMatrix != null) {
                            mControlMatrix.postTranslate(result_x, result_y);
                        }
                        if (controlPoint != null) {
                            for (int i = 0; i < controlPoint.length; i++) {
                                PointF pointF = controlPoint[i];
                                if (pointF != null) {
                                    pointF.x += result_x;
                                    pointF.y += result_y;
                                }
                            }
                        }
                        startPoint.set(x, y);
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PUZZLES_ADD_TEXT, 0));
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isSelectDelete) {
                    Log.d("PuzzleAddTextInfo", "选中了删除");
                    TemporaryTextData temporaryTextData = getTemporaryTextData();
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PUZZLES_ADD_TEXT, 0, temporaryTextData.getPoints()));
                    return true;
                }
                if (isSelectControl) {
                    return true;
                }
                //手指离开的时候还是在签名处, 则显示边框
                if (isDown && x > bitmapRect.left && x < bitmapRect.right && y > bitmapRect.top && y < bitmapRect.bottom) {
                    if (!isShowFrame) {
                        isShowFrame = true;
                        initPath();
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PUZZLES_ADD_TEXT, 0));
                    }
                    if (isSelectAddText && !isMove) {
                        isShowFrame = true;
                        TemporaryTextData temporaryTextData = getTemporaryTextData();
                        EventBus.getDefault().post(new PuzzlesRequestMsg(
                                PUZZLES_ADD_TEXT, action, temporaryTextData));
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    private Point[] drawPoint = new Point[4];

    public Point[] getDrawPoint() {
        if (textPoint != null) {
            for (int i = 0; i < textPoint.length; i++) {
                drawPoint[i] = new Point((int) textPoint[i].x, (int) textPoint[i].y);
            }
        }
        return drawPoint;
    }

    public TemporaryTextData getTemporaryTextData() {
        TextData textData = new TextData();
        textData.setFontSize(fontSize);
        textData.setAutoStr(autoStr);
        textData.setFontColor(fontColor);
        textData.setFont(font);
        textData.setDownload(downloadFont);

        TemporaryTextData temporaryTextData = new TemporaryTextData(textData, getDrawPoint());
        return temporaryTextData;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void recycle() {

    }

    public void initPath() {
        if (path == null) {
            path = new Path();
        }
        path.reset();
        if (textPoint != null) {
            path.moveTo(textPoint[0].x, textPoint[0].y);
            for (int i = 1; i < textPoint.length; i++) {
                path.lineTo(textPoint[i].x, textPoint[i].y);
            }
            path.close();
        }
    }

    private void initDeleteIcon(Context context) {
        //获取控制按钮
        if (deleteIconBmp == null) {
            deleteIconBmp = JaneBitmapFactory.decodeResource(context, R.drawable.red_delete_btn, ImageScaleType.NONE);
        }
        //获取控制按钮的宽高
        deleteWidth = deleteIconBmp.getWidth();
        deleteHeight = deleteIconBmp.getHeight();
        initDeletePoint();
    }

    private void initDeletePoint() {
        mDeleteMatrix.reset();
        if (textPoint != null && textPoint.length > 2) {
            mDeleteMatrix.postTranslate(textPoint[0].x - deleteWidth / 2, textPoint[0].y - deleteHeight / 2);
            deletePoint[0] = new PointF(textPoint[0].x - deleteWidth / 2, textPoint[0].y - deleteHeight / 2);
            deletePoint[1] = new PointF(textPoint[0].x + deleteWidth / 2, textPoint[0].y - deleteHeight / 2);
            deletePoint[2] = new PointF(textPoint[0].x + deleteWidth / 2, textPoint[0].y + deleteHeight / 2);
            deletePoint[3] = new PointF(textPoint[0].x - deleteWidth / 2, textPoint[0].y + deleteHeight / 2);
        }

    }

    private void initControlIcon(Context context) {
        //获取控制按钮
        if (controlIconBmp == null) {
            controlIconBmp = JaneBitmapFactory.decodeResource(context, R.drawable.rotatezoom, ImageScaleType.NONE);
        }
        //获取控制按钮的宽高
        controlWidth = controlIconBmp.getWidth();
        controlHeight = controlIconBmp.getHeight();
        initControlPoint();
    }

    private void initControlPoint() {
        mControlMatrix.reset();
        if (textPoint != null && textPoint.length > 0) {
            mControlMatrix.postTranslate(textPoint[2].x - controlWidth / 2, textPoint[2].y - controlHeight / 2);
            controlPoint[0] = new PointF(textPoint[2].x - controlWidth / 2, textPoint[2].y - controlHeight / 2);
            controlPoint[1] = new PointF(textPoint[2].x + controlWidth / 2, textPoint[2].y - controlHeight / 2);
            controlPoint[2] = new PointF(textPoint[2].x + controlWidth / 2, textPoint[2].y + controlHeight / 2);
            controlPoint[3] = new PointF(textPoint[2].x - controlWidth / 2, textPoint[2].y + controlHeight / 2);
        }
    }

    /**
     * 获取所需要的行数
     *
     * @return
     */
    public LinkedList<String> getLineStrs() {
        if (autoStr != null) {
            LinkedList<String> lineStrs = new LinkedList<>();
            //该字体大小下一行字的高度
            float lineHeight = paint.descent() - paint.ascent();
            String editStr = autoStr;
            float strWidth = 0;
            String lineStr = "";
            for (int i = 0; i < editStr.length(); i++) {
                char str = editStr.charAt(i);
                String charStr = String.valueOf(str);
                float charWidth = paint.measureText(charStr);
                if (!charStr.equals("\n")) {
                    if ((strWidth + charWidth) > imageRect.width()) {
                        lineStrs.add(lineStr);
                        //如果宽度超过了, 判断高度是否超过最大高度
                        int lineIndex = lineStrs.size();
                        if (lineIndex != 0) {
                            int resultHeight = (int) (lineIndex * lineHeight + (lineIndex - 1) * lineSpace);
                            //如果超过了宽度
                            if (resultHeight > imageRect.height()) {
                                lineStrs.remove(lineStr);
                                return lineStrs;
                            }
                        }
                        strWidth = charWidth;
                        lineStr = charStr;
                    } else {
                        strWidth += charWidth;
                        lineStr += charStr;
                    }
                    if (i == editStr.length() - 1) {
                        lineStrs.add(lineStr);
                        //如果宽度超过了, 判断高度是否超过最大高度
                        int lineIndex = lineStrs.size();
                        if (lineIndex != 0) {
                            int resultHeight = (int) (lineIndex * lineHeight + (lineIndex - 1) * lineSpace);
                            //如果超过了宽度
                            if (resultHeight > imageRect.height()) {
                                lineStrs.remove(lineStr);
                                return lineStrs;
                            }
                        }
                        strWidth = 0;
                        lineStr = "";
                    }
                } else {
                    lineStrs.add(lineStr);
                    //如果宽度超过了, 判断高度是否超过最大高度
                    int lineIndex = lineStrs.size();
                    if (lineIndex != 0) {
                        int resultHeight = (int) (lineIndex * lineHeight + (lineIndex - 1) * lineSpace);
                        //如果超过了宽度
                        if (resultHeight > imageRect.height()) {
                            lineStrs.remove(lineStr);
                            return lineStrs;
                        }
                    }
                    strWidth = 0;
                    lineStr = "";
                }
            }
            return lineStrs;
        }
        return null;
    }

    /**
     * 角度换算成弧度
     *
     * @param degree
     * @return
     */
    public static double degreeToRadian(double degree) {
        return degree * Math.PI / 180;
    }

    /**
     * 弧度换算成角度
     *
     * @return
     */
    public static double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }

    /**
     * 两个点之间的距离
     *
     * @return
     */
    public static float distance4PointF(PointF pf1, PointF pf2) {
        float disX = pf2.x - pf1.x;
        float disY = pf2.y - pf1.y;
        return (float) Math.sqrt(disX * disX + disY * disY);
    }


    public String getAutoStr() {
        return autoStr;
    }

    public void setAutoStr(String autoStr) {
        this.autoStr = autoStr;
    }

    public String getFont() {
        return font;
    }

    public void setFont(Context context, String font) {
        if (TextUtils.isEmpty(font)) {
            return;
        }
        this.font = font;
        if (!TextUtils.isEmpty(font)) {
            Typeface typeface = PuzzleTextUtils.readFont(context, font, true);
            if (paint != null) {
                paint.setTypeface(typeface);
            }
        }
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        if (paint != null) {
            paint.setTextSize(fontSize);
        }
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
        if (paint != null) {
            paint.setColor(fontColor);
        }
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public boolean isShowFrame() {
        return isShowFrame;
    }

    public void setShowFrame(boolean showFrame) {
        isShowFrame = showFrame;
    }

    public boolean isDownloadFont() {
        return downloadFont;
    }

    public void setDownloadFont(boolean downloadFont) {
        this.downloadFont = downloadFont;
    }

    public int getPuzzleModel() {
        return puzzleModel;
    }

    public void setPuzzleModel(int puzzleModel) {
        this.puzzleModel = puzzleModel;
    }
}
