package com.xs.lightpuzzle.puzzle.view.signature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.xs.lightpuzzle.puzzle.PuzzleConstant;
import com.xs.lightpuzzle.puzzle.param.SignatureSaveVO;
import com.xs.lightpuzzle.puzzle.param.TimedPoint;
import com.xs.lightpuzzle.puzzle.util.ShareData;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/8/23.
 */
public class SignaturePanel extends View {
    private String TAG = "SinaturePad";
    private final int PAINT_WIDTH_MIN = ShareData.PxToDpi_xhdpi(7);
    private final int PAINT_WIDTH_MAX = ShareData.PxToDpi_xhdpi(17);

    //View state
    private List<TimedPoint> mPoints;
    private ArrayList<ArrayList<TimedPoint>> mStoragePoints;
    private ArrayList<TimedPoint> mStorageItemPoints;

    private boolean mIsEmpty;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastVelocity;
    private float mLastWidth;

    //Configurable parameters
    private int mMinWidth;//最小笔宽
    private int mMaxWidth;//最大笔宽
    private float mVelocityFilterWeight;//笔宽速率控制，也小笔宽变化慢
    private boolean mIsSignatrue = true;

    //签名
    private Paint mPaint = new Paint();
    private Bitmap mSignatureBitmap = null;
    private int mWidth;
    private int mHeight;
    private Canvas mSignatureBitmapCanvas = null;
    private RectF mSignatureRect;//签名的范围

    private long mLastTime;//上一次触摸时间
    private boolean mIsChange;//签名是否更改标志
    private String mPicPath = null;//签名路径
    private boolean mMoveFirst = true;
    private final int NUM_SEGMENTS = 3;
    private final int LOA = 100;

    /**
     * @param context
     * @param picPath 不为null时初始化，根据路径名恢复签名
     */
    public SignaturePanel(Context context, String picPath) {
        super(context);
        mPicPath = picPath;
        mIsChange = false;
        mWidth = Utils.getScreenW();
        mHeight = Utils.getScreenH();

        //以前差8
        setMinWidth(mWidth / 100);
        setMaxWidth(mMinWidth * 2.5f);
        setVelocityFilterWeight(0.45f);
        initPaint();

        ensureSignatureBitmap();
        clear();

        resetSignatureData(picPath); // 恢复签名 , 存的是点的集合，重新绘制贝赛尔曲线

    }

    /**
     * 初始画笔属性
     */
    private void initPaint() {
        //Fixed parameters
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.BLACK);//默认颜色
    }

    /**
     * 初始化 Canvas 和 Bitmap
     */
    private void ensureSignatureBitmap() {
        if (mSignatureBitmap == null) {
            mSignatureBitmap = Bitmap.createBitmap(Utils.getScreenW(), Utils.getScreenH(),
                    Bitmap.Config.ARGB_8888);
            mSignatureBitmapCanvas = new Canvas(mSignatureBitmap);
            mSignatureBitmapCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            mSignatureBitmapCanvas.drawColor(Color.TRANSPARENT);
        }
    }

    /**
     * 通过签名路径，读取点集合，并将签名点集合绘制在画布上
     *
     * @param picPath 签名路径
     */
    private void resetSignatureData(String picPath) {
        if (!FileUtils.isFileExists(picPath)) {
            return;
        }

        SignatureSaveVO signatureSaveVO = SignaturePadHelper.getSignatureSaveVO(picPath);
        mPaint.setColor(signatureSaveVO.getColor());
        mSignatureRect = signatureSaveVO.getRectF();
        mStoragePoints = signatureSaveVO.getTimedPointsArray();
        //
        Bitmap bitmap = SignaturePadHelper.getSignatureBitmap(signatureSaveVO,
                (int) signatureSaveVO.getHeight(), 0, mMinWidth, mVelocityFilterWeight, LOA, false);
        if (bitmap != null && !bitmap.isRecycled()) {
            mSignatureBitmapCanvas.drawBitmap(bitmap, mSignatureRect.left, mSignatureRect.top, null);
            bitmap.recycle();
        }
        setIsEmpty(false);

        mStoragePoints = exchangeStoragePoint(signatureSaveVO.getTimedPointsArray());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);

                mLastTouchX = eventX;
                mLastTouchY = eventY;

                if (mIsSignatrue) {
                    mPoints.clear();
                    addPoint(new TimedPoint(eventX, eventY));
                    addPoint(new TimedPoint(eventX, eventY));
                    mLastTime = System.currentTimeMillis();
                    expandSignatureRange(eventX, eventY);
                    mIsChange = true;//签名改变了
                }

            case MotionEvent.ACTION_MOVE:
                //采点
                if (mIsSignatrue) {
                    long newTime = System.currentTimeMillis();
                    long useTime = newTime - mLastTime;
                    if (mMoveFirst)//优化
                    {
                        if (useTime < 80) {
                            return true;
                        } else {
                            mMoveFirst = false;
                        }
                    }

                    final float dx = Math.abs(eventX - mLastTouchX);
                    final float dy = Math.abs(eventY - mLastTouchY);
                    if (dx > 60 || dy > 60)//最大距离控制
                    {
                        addPoint(new TimedPoint(eventX, eventY));
                        mLastTouchX = eventX;
                        mLastTouchY = eventY;
                        mLastTime = newTime;
                    } else if (useTime > 20)//时间控制
                    {
                        if (dx > 10 || dy > 10)//最小距离控制
                        {
                            addPoint(new TimedPoint(eventX, eventY));
                            mLastTouchX = eventX;
                            mLastTouchY = eventY;
                            mLastTime = newTime;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                mMoveFirst = true;
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mIsSignatrue) {
                    addPoint(new TimedPoint(eventX, eventY));
                    if (mStorageItemPoints.size() > 0) {
                        mStoragePoints.add(mStorageItemPoints);
                    }
                    mStorageItemPoints = new ArrayList<>();
                    setIsEmpty(false);
                    invalidate();
                }
                break;

            default:
                return false;
        }
        if (mIsSignatrue)//刷新
        {
            invalidate();
        }
        return true;
    }

    private void addPoint(TimedPoint newPoint) {
        if (Float.isNaN(newPoint.x)) {
            newPoint.x = 0.0f;
        }
        if (Float.isNaN(newPoint.y)) {
            newPoint.y = 0.0f;
        }
        mPoints.add(newPoint);
        mStorageItemPoints.add(new TimedPoint(newPoint.x, newPoint.y, newPoint.timestamp));

        if (mPoints.size() > 2) {
            // To reduce the initial lag make it work with 3 mPoints
            // by copying the first point to the beginning.
            if (mPoints.size() == 3) mPoints.add(0, mPoints.get(0));

            TimedPoint startPoint = mPoints.get(1);
            TimedPoint endPoint = mPoints.get(2);

            float velocity = endPoint.velocityFrom(startPoint);
            velocity = Float.isNaN(velocity) ? 0.0f : velocity;

            velocity = mVelocityFilterWeight * velocity
                    + (1 - mVelocityFilterWeight) * mLastVelocity;


            // The new width is a function of the velocity. Higher velocities
            // correspond to thinner strokes.
            float newWidth = strokeWidth(velocity);

            Log.i("test:", "" + velocity + "\r\n" + "newWidth:" + newWidth);
            // gradually changes to the stroke width just calculated. The new
            // start and end mPoints.
            addBspline(mPoints.get(0), mPoints.get(1), mPoints.get(2), mPoints.get(3), mLastWidth, newWidth);

            mLastVelocity = velocity;
            mLastWidth = newWidth;

            // Remove the first element from the list,
            // so that we always have no more than 4 mPoints in mPoints array.
            mPoints.remove(0);
        }
    }

    //b样条曲线
    private void addBspline(TimedPoint pt0, TimedPoint pt1, TimedPoint pt2, TimedPoint pt3, float startWidth, float endWidth) {
        float originalWidth = mPaint.getStrokeWidth();
        float widthDelta = endWidth - startWidth;

        // for each section of curve, draw LOD number of divisions
        for (int i = 0; i != LOA; ++i) {
            // use the parametric time value 0 to 1 for this curve
            // segment.
            float t = (float) i / LOA;
            float tt = t * t;
            float ttt = tt * t;
            // the t value inverted
            float it = 1.0f - t;

            // calculate blending functions for cubic bspline
            float b0 = it * it * it / 6.0f;
            float b1 = (3 * ttt - 6 * tt + 4) / 6.0f;
            float b2 = (-3 * ttt + 3 * tt + 3 * t + 1) / 6.0f;
            float b3 = ttt / 6.0f;

            // calculate the x,y and z of the curve point
            float x = b0 * pt0.x + b1 * pt1.x + b2 * pt2.x + b3 * pt3.x;

            float y = b0 * pt0.y + b1 * pt1.y + b2 * pt2.y + b3 * pt3.y;
            // specify the point

            // Set the incremental stroke width and draw.
            mPaint.setStrokeWidth(startWidth + ttt * widthDelta);
            mSignatureBitmapCanvas.drawPoint(x, y, mPaint);

            expandSignatureRange(x, y);
        }

        mPaint.setStrokeWidth(originalWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSignatureBitmap != null) {
            canvas.drawBitmap(mSignatureBitmap, 0, 0, mPaint);
            float temp = mPaint.getStrokeWidth();
            mPaint.setStrokeWidth(3);
            mPaint.setStrokeWidth(temp);
        }
    }

    /**
     * 扩大用户的签名绘制区域
     *
     * @param historicalX
     * @param historicalY
     */
    private void expandSignatureRange(float historicalX, float historicalY) {
        if (mSignatureRect == null) {
            mSignatureRect = new RectF(historicalX, historicalY, historicalX, historicalY);//没签名
        }

        if (historicalX - mMaxWidth < mSignatureRect.left) {
            if (historicalX - mMaxWidth < 0) {
                mSignatureRect.left = 0.0f;
            } else {
                mSignatureRect.left = historicalX - mMaxWidth;
            }
        } else if (historicalX + mMaxWidth > mSignatureRect.right) {
            if (historicalX + mMaxWidth > mWidth) {
                mSignatureRect.right = mWidth;
            } else {
                mSignatureRect.right = historicalX + mMaxWidth;
            }
        }

        if (historicalY - mMaxWidth < mSignatureRect.top) {
            if (historicalY - mMaxWidth < 0) {
                mSignatureRect.top = 0.0f;
            } else {
                mSignatureRect.top = historicalY - mMaxWidth;
            }
        } else if (historicalY + mMaxWidth > mSignatureRect.bottom) {
            if (historicalY + mMaxWidth > mHeight) {
                mSignatureRect.bottom = mHeight;
            } else {
                mSignatureRect.bottom = historicalY + mMaxWidth;
            }
        }
    }

    /**
     * 调整保存矩形的大小
     */
    private void adjustSignatureRect() {
        int minWidth = 200;
        int minHeight = 200;
        if (mSignatureRect != null) {
            if (mSignatureRect.width() < minWidth) {
                mSignatureRect.left = mSignatureRect.centerX() - minWidth / 2;
                mSignatureRect.right = mSignatureRect.centerX() + minWidth / 2;
                if (mSignatureRect.left < 0) mSignatureRect.left = 0;
                if (mSignatureRect.right > mWidth) mSignatureRect.right = mWidth;
            }
            if (mSignatureRect.height() < minHeight) {
                mSignatureRect.top = mSignatureRect.centerY() - minHeight / 2;
                mSignatureRect.bottom = mSignatureRect.centerY() + minHeight / 2;
                if (mSignatureRect.top < 0) mSignatureRect.top = 0;
                if (mSignatureRect.bottom > mHeight) mSignatureRect.bottom = mHeight;
            }
        }

    }

    /**
     * 按照当前view的宽高计算对应位置的点集合
     *
     * @param timedPointsArray 保存时按照0-1的比例计算每个点的位置
     * @return
     */
    private ArrayList<ArrayList<TimedPoint>> exchangeStoragePoint(ArrayList<ArrayList<TimedPoint>> timedPointsArray) {
        float offsetX = mSignatureRect.left;
        float offsetY = mSignatureRect.top;

        for (int i = 0; i < timedPointsArray.size(); i++) {
            for (int j = 0; j < timedPointsArray.get(i).size(); j++) {
                timedPointsArray.get(i).get(j).x = timedPointsArray.get(i).get(j).x * mSignatureRect.width() + offsetX;
                timedPointsArray.get(i).get(j).y = timedPointsArray.get(i).get(j).y * mSignatureRect.height() + offsetY;
            }
        }

        return timedPointsArray;
    }

    /**
     * 改变签名的颜色
     *
     * @param color        颜色值
     * @param isInvalidate 是否调invalidate
     */
    public void changeSignatureColor(int color, boolean isInvalidate) {
        if (mSignatureBitmapCanvas != null) {
            Bitmap maskSrc = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);

            if (maskSrc != null && !maskSrc.isRecycled()) {
                maskSrc.eraseColor(color);
                PorterDuffXfermode maskColorMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
                mPaint.setXfermode(maskColorMode);
                mSignatureBitmapCanvas.drawBitmap(maskSrc, 0, 0, mPaint);
                mPaint.setXfermode(null);
                maskSrc.recycle();
                mIsChange = true;
            }
        }

        mPaint.setColor(color);
        if (isInvalidate)
            invalidate();
    }

    public boolean saveTransparentSignatureArray() {
        boolean isSave = false;
        //签名没改过返回
        if (!mIsChange) {
            if (mPicPath != null && FileUtils.isFileExists(mPicPath))
                isSave = true;
            return isSave;
        }

        String savePath = "";
        if (mSignatureBitmap != null && mSignatureRect != null) {
            String filePath = PuzzleConstant.SD_SIGNATURE_HISTORY_LIST_PATH;
            String fileName = String.valueOf(System.currentTimeMillis()) + ".json";
            // eg: storage/emulated/0/PocoJane/appdata/signature/history/1538213267233.json
            savePath = filePath + fileName; // 历史草稿文件夹

            adjustSignatureRect();//调整大小

            float offsetX = mSignatureRect.left;
            float offsetY = mSignatureRect.top;
            SignatureSaveVO signatureSaveVO = new SignatureSaveVO();
            signatureSaveVO.setColor(mPaint.getColor());
            signatureSaveVO.setRectF(mSignatureRect);
            signatureSaveVO.setWidth(mSignatureRect.width());
            signatureSaveVO.setHeight(mSignatureRect.height());

            for (int i = 0; i < mStoragePoints.size(); i++) {
                for (int j = 0; j < mStoragePoints.get(i).size(); j++) {
                    mStoragePoints.get(i).get(j).x = (mStoragePoints.get(i).get(j).x - offsetX) / mSignatureRect.width();
                    mStoragePoints.get(i).get(j).y = (mStoragePoints.get(i).get(j).y - offsetY) / mSignatureRect.height();
                }
            }
            signatureSaveVO.setTimedPointsArray(mStoragePoints);

            String templateStr = new Gson().toJson(signatureSaveVO);
            isSave = FileIOUtils.writeFileFromString(savePath, templateStr);
            mPicPath = savePath;
        }

        return isSave;
    }

    public void clear() {
        mPoints = new ArrayList<>();

        mStoragePoints = new ArrayList<>();
        mStorageItemPoints = new ArrayList<>();

        mLastVelocity = 0;
        mLastWidth = (mMinWidth + mMaxWidth) / 2;

        if (mSignatureBitmap != null) {
            mSignatureBitmap.eraseColor(Color.TRANSPARENT);
            mSignatureRect = null;
            mIsSignatrue = true;
            mIsChange = true;//签名更改了
            mPicPath = null;
        }

        setIsEmpty(true);
        invalidate();
    }

    public void clearBitmap() {
        if (mSignatureBitmap != null && !mSignatureBitmap.isRecycled()) {
            mSignatureBitmap.recycle();
            mSignatureBitmap = null;
        }
    }

    // 暂时没用
    public boolean isEmpty() {
        return mIsEmpty;
    }

    private void setIsEmpty(boolean newValue) {
        mIsEmpty = newValue;
    }

    /**
     * Set the minimum width of the stroke in pixel.
     *
     * @param minWidth the width in dp.
     */
    public void setMinWidth(float minWidth) {
        mMinWidth = (int) minWidth;
    }

    /**
     * Set the maximum width of the stroke in pixel.
     *
     * @param maxWidth the width in dp.
     */
    public void setMaxWidth(float maxWidth) {
        mMaxWidth = (int) maxWidth;
    }

    /**
     * Set the velocity filter weight.
     *
     * @param velocityFilterWeight the weight.
     */
    public void setVelocityFilterWeight(float velocityFilterWeight) {
        mVelocityFilterWeight = velocityFilterWeight;
    }

    public void setSignature(boolean isSignature) {
        mIsSignatrue = isSignature;
    }

    public boolean getSignature() {
        return mIsSignatrue;
    }

    /**
     * 是否更改过签名
     */
    public boolean hasChanged() {
        return mIsChange;
    }

    public void setChange(boolean change) {
        mIsChange = change;
    }

    public String getSavePicPath() {
        return mPicPath;
    }

    public int getPaintColor() {
        return mPaint.getColor();
    }

    private float strokeWidth(float velocity) {
        return Math.max(mMaxWidth / (velocity + 1), mMinWidth);
    }
}
