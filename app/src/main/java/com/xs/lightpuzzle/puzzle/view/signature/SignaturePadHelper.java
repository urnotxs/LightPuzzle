package com.xs.lightpuzzle.puzzle.view.signature;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xs.lightpuzzle.puzzle.param.SignatureSaveVO;
import com.xs.lightpuzzle.puzzle.param.TimedPoint;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by xs on 2018/8/24.
 */

public class SignaturePadHelper {
    private static int LOA = 30;
    private static int mMinWidth = 4;//最小笔宽
    private static int mMaxWidth = 10;//最大笔宽
    private static float mVelocityFilterWeight = 0.65f;//笔宽速率控制，也小笔宽变化慢
    private static float mLastWidth;
    private static float mLastVelocity;

    /**
     * 读取签名路径存放的点集合
     *
     * @param signPic 签名路径
     * @return 签名数据，点集合
     */
    public static SignatureSaveVO getSignatureSaveVO(String signPic) {
        SignatureSaveVO signatureSaveVO = null;
        if (FileUtils.isFileExists(signPic)) {
            signatureSaveVO = new Gson().fromJson(
                    FileIOUtils.readFile2String(signPic),
                    new TypeToken<SignatureSaveVO>() {}.getType());
        }
        return signatureSaveVO;
    }

    /**
     * 获取签名bitmap
     *
     * @param signatureSaveVO
     * @param height             签名高
     * @param rotation         旋转角度，拼图页需要旋转-90度
     * @param minPaintWidth    最小画笔宽度
     * @param velocityFilterWeight
     * @param loa              采集点
     * @param isBlackToGray    是否将黑色绘制成灰色
     * @return
     */
    public static Bitmap getSignatureBitmap(SignatureSaveVO signatureSaveVO,
                                            int height, int rotation, float minPaintWidth ,
                                            float velocityFilterWeight ,  int loa, boolean isBlackToGray) {
        if (signatureSaveVO != null){
            List<TimedPoint>  points = new ArrayList<>();
            int paintColor = signatureSaveVO.getColor();
            if (signatureSaveVO.getColor() == 0xff000000 && isBlackToGray){
                //如果是黑色，变为灰色绘制
                paintColor = 0xff666666;
            }
            float mStorageRatioWH = signatureSaveVO.getWidth()/signatureSaveVO.getHeight();
            ArrayList<ArrayList<TimedPoint>> mStoragePoints = signatureSaveVO.getTimedPointsArray();

            int width = (int) (height * mStorageRatioWH);
            LOA = loa;
            mVelocityFilterWeight = velocityFilterWeight;
            mMinWidth = (int) minPaintWidth;
            mMaxWidth = (int) (minPaintWidth * 2.5);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setColor(paintColor);//默认颜色

            mLastWidth = (mMinWidth + mMaxWidth) / 2;
            mLastVelocity = 0 ;

            if (mStoragePoints != null && mStoragePoints.size() > 0 && mStoragePoints.get(0).size() > 0){

                Bitmap bitmap = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.setDrawFilter(
                        new PaintFlagsDrawFilter(0,
                                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                canvas.drawColor(Color.TRANSPARENT);

                for (int i = 0 ; i < mStoragePoints.size() ; i++){
                    points.clear();
                    for (int j = 0 ; j < mStoragePoints.get(i).size() ; j++){
                        TimedPoint newPoint = new TimedPoint(
                                mStoragePoints.get(i).get(j).x * width,
                                mStoragePoints.get(i).get(j).y * height);
                        newPoint.timestamp = mStoragePoints.get(i).get(j).timestamp;
                        addPoint(canvas , paint , points , newPoint);
                    }
                }

                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);

                return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            }
        }

        return null;
    }

    public static void addPoint(Canvas canvas, Paint paint, List<TimedPoint> points, TimedPoint newPoint) {
        points.add(newPoint);
        if (points.size() > 2) {
            // To reduce the initial lag make it work with 3 mPoints
            // by copying the first point to the beginning.
            if (points.size() == 3) points.add(0, points.get(0));

            TimedPoint startPoint = points.get(1);
            TimedPoint endPoint = points.get(2);

            float velocity = endPoint.velocityFrom(startPoint);
            velocity = Float.isNaN(velocity) ? 0.0f : velocity;

            velocity = mVelocityFilterWeight * velocity
                    + (1 - mVelocityFilterWeight) * mLastVelocity;

            // The new width is a function of the velocity. Higher velocities
            // correspond to thinner strokes.
            float newWidth = Math.max(mMaxWidth / (velocity + 1), mMinWidth);;

            Log.i("test:", "" + velocity + "\r\n" + "newWidth:" + newWidth);
            // gradually changes to the stroke width just calculated. The new
            // start and end mPoints.
            addBspline(canvas ,paint ,points.get(0), points.get(1), points.get(2), points.get(3), mLastWidth, newWidth);

            mLastVelocity = velocity;
            mLastWidth = newWidth;

            // Remove the first element from the list,
            // so that we always have no more than 4 mPoints in mPoints array.
            points.remove(0);
        }
    }

    //b样条曲线
    public static void addBspline(Canvas canvas, Paint paint ,TimedPoint pt0, TimedPoint pt1, TimedPoint pt2, TimedPoint pt3, float startWidth, float endWidth) {
        float originalWidth = paint.getStrokeWidth();
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
            paint.setStrokeWidth(startWidth + ttt * widthDelta);
            canvas.drawPoint(x, y, paint);

        }

        paint.setStrokeWidth(originalWidth);
    }

}
