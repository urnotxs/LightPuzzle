package com.xs.lightpuzzle.puzzle.util;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.data.TemplateData;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lin on 2018/4/12.
 */

public class PuzzlesUtils {

    public static Rect getRect(int puzzleMode, TemplateData templateData) {
        if (templateData == null || puzzleMode == -1 || templateData.getSizeRatio() == 0) {
            return null;
        }
        switch (puzzleMode) {
            case PuzzleMode.MODE_WAG:
            case PuzzleMode.MODE_LAYOUT:
            case PuzzleMode.MODE_VIDEO:
                return getPrivateRect(templateData.getSizeRatio());
            case PuzzleMode.MODE_LONG:
            case PuzzleMode.MODE_JOIN:
            case PuzzleMode.MODE_LAYOUT_JOIN:
                return getLongRect(templateData.getSizeRatio());
        }
        return null;
    }

    public static Rect getPrivateRect(float ratio) {
        int width = 0;
        int height = 0;
        if (ratio != 0) {
            if (ratio < 1f) {
                int maxWidth = Utils.getScreenW() - 2 * getViewMarginLeftRight();
                int maxHeight = Utils.getScreenH() - 2 * getViewTop() - getTopBarHeight() - getBottomViewHeight();
                float viewRatio = (float) maxWidth / (float) maxHeight;
                if (ratio < viewRatio) {
                    height = maxHeight;
                    width = (int) (height * ratio);
                } else {
                    width = maxWidth;
                    height = (int) (width / ratio);
                }
            } else {
                width = Utils.getScreenW() - 2 * getViewMarginLeftRight();
                height = (int) (width / ratio);
            }
        }
        return new Rect(0, 0, width, height);
    }

    public static Rect getLongRect(float ratio) {
        int width = Utils.getScreenW() - 2 * getViewMarginLeftRight();
        int height = (int) (width / ratio);
        return new Rect(0, 0, width, height);
    }

    public static int getTopBarHeight() {
        return Utils.getRealPixel3(90);
    }

    /**
     * 顶部距离
     *
     * @return int
     */

    public static int getViewTop() {
        float border = 0;
        int viewTop = 0;
        if (Utils.getScreenW() >= 1080) {//大于1080的边距比更多
            border = 0.05f * Utils.getScreenW() / (float) 1080;
            viewTop = (int) (border * 1080);
        } else {
            viewTop = (int) (border * Utils.getScreenW());
        }
        return viewTop;
    }

    /**
     * 获取左右边距
     *
     * @return int
     */

    public static int getViewMarginLeftRight() {
        return Utils.getRealPixel3(22);
    }

    /**
     * 获取底部按钮控件的高度
     * 通常传 0 ，只有改变底部高度时才传值，如布局缩放
     *
     * @return int
     */
    public static int getBottomViewHeight() {
        if (mBottomHeight == 0) {
            mBottomHeight = Utils.getRealPixel3(111);
        }
        return mBottomHeight;
    }

    public static void setBottomViewHeight(int bottomHeight) {
        mBottomHeight = bottomHeight;
    }

    private static int mBottomHeight;


    /**
     * OutOfMemory 限制
     * size限制 15
     * 内存限制 82% 并且 size >4
     * 高度限制 11500
     */
    public static boolean isOutOfMemory(int viewHeight, int size) {
        System.gc();
        System.runFinalization();
        System.gc();
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
        long totalMemory = Runtime.getRuntime().totalMemory() / 1024;
        if (viewHeight >= 11500 || size >= 15 || (totalMemory > maxMemory * 8.2f / 10.0f && size > 4)) {
            return true;
        }
        return false;
    }

    /**
     * 获取长图独有的增加或者删除模板的按钮
     *
     * @return int
     */

    public static int getAddOrDelTempHeight() {
        return Utils.getRealPixel3(70);
    }

    public static int strColor2Int(String colorStr) {
        int colorBg = 0;
        if (colorStr != null && !colorStr.isEmpty()) {
            if (colorStr.length() == 8) {
                colorBg = Integer.parseInt(colorStr, 16);
            } else {
                colorBg = 0xff000000 | Integer.parseInt(colorStr, 16);
            }
        }
        return colorBg;
    }

    public static boolean pointInRegion(List<Point> plist, Point pt) {
        int nCross = 0;    // 定义变量，统计目标点向右画射线与多边形相交次数

        for (int i = 0; i < plist.size(); i++) {   //遍历多边形每一个节点

            Point p1;
            Point p2;

            p1 = plist.get(i);
            p2 = plist.get((i + 1) % plist.size());  // p1是这个节点，p2是下一个节点，两点连线是多边形的一条边
            // 以下算法是用是先以y轴坐标来判断的

            if (p1.y == p2.y)
                continue;   //如果这条边是水平的，跳过

            if (pt.y < Math.min(p1.y, p2.y)) //如果目标点低于这个线段，跳过
                continue;

            if (pt.y >= Math.max(p1.y, p2.y)) //如果目标点高于这个线段，跳过
                continue;
            //那么下面的情况就是：如果过p1画水平线，过p2画水平线，目标点在这两条线中间
            double x = (double) (pt.y - p1.y) * (double) (p2.x - p1.x) / (double) (p2.y - p1.y) + p1.x;
            // 这段的几何意义是 过目标点，画一条水平线，x是这条线与多边形当前边的交点x坐标
            if (x > pt.x)
                nCross++; //如果交点在右边，统计加一。这等于从目标点向右发一条射线（ray），与多边形各边的相交（crossing）次数
        }

        if (nCross % 2 == 1) {
            return true; //如果是奇数，说明在多边形里
        } else {
            return false; //否则在多边形外 或 边上
        }

    }

    public static boolean pointFInRegion(List<PointF> plist, Point pt) {
        int nCross = 0;    // 定义变量，统计目标点向右画射线与多边形相交次数

        if(plist ==null){
            return false;
        }

        for (int i = 0; i < plist.size(); i++) {   //遍历多边形每一个节点

            PointF p1;
            PointF p2;

            p1 = plist.get(i);
            p2 = plist.get((i + 1) % plist.size());  // p1是这个节点，p2是下一个节点，两点连线是多边形的一条边
            // 以下算法是用是先以y轴坐标来判断的

            if (p1.y == p2.y)
                continue;   //如果这条边是水平的，跳过

            if (pt.y < Math.min(p1.y, p2.y)) //如果目标点低于这个线段，跳过
                continue;

            if (pt.y >= Math.max(p1.y, p2.y)) //如果目标点高于这个线段，跳过
                continue;
            //那么下面的情况就是：如果过p1画水平线，过p2画水平线，目标点在这两条线中间
            double x = (double) (pt.y - p1.y) * (double) (p2.x - p1.x) / (double) (p2.y - p1.y) + p1.x;
            // 这段的几何意义是 过目标点，画一条水平线，x是这条线与多边形当前边的交点x坐标
            if (x > pt.x)
                nCross++; //如果交点在右边，统计加一。这等于从目标点向右发一条射线（ray），与多边形各边的相交（crossing）次数
        }

        if (nCross % 2 == 1) {
            return true; //如果是奇数，说明在多边形里
        } else {
            return false; //否则在多边形外 或 边上
        }

    }

    public static Rect getMaxWidthHeight(PointF[] pointFS) {
        if (pointFS == null) {
            return null;
        }
        for (int i = 0; i < pointFS.length; i++) {
            if (pointFS[i] == null) {
                return null;
            }
        }
        if (pointFS.length == 4) {
            float min_x = 0;
            float min_y = 0;
            float max_x = 0;
            float max_y = 0;
            max_x = min_x = pointFS[0].x;
            max_y = min_y = pointFS[0].y;
            for (int i = 1; i < pointFS.length; i++) {
                if (pointFS[i].x < min_x) {
                    min_x = pointFS[i].x;
                }
                if (pointFS[i].x > max_x) {
                    max_x = pointFS[i].x;
                }
                if (pointFS[i].y < min_y) {
                    min_y = pointFS[i].y;
                }
                if (pointFS[i].y > max_y) {
                    max_y = pointFS[i].y;
                }
            }
            return new Rect((int) min_x, (int) min_y, (int) max_x, (int) max_y);
        }
        return null;
    }

    /**
     * 根据旋转中心点和旋转弧度，计算旋转后的点
     *
     * @param pointF pointF
     * @param center  旋转中心点
     * @param radians 弧度
     * @return PointF
     */
    public static PointF rotate(PointF pointF, PointF center, float radians) {
        /*最后如果旋转中心为(a,b),在利用下面的公式时,需要把(a,b)沿向量(-a,-b)移动到原点,此时(x,y)变成(x-a,y-b),(x',y')变成(x'-a,y'-b),整理得
        x'=(x-a)cosα+(y-b)sinα+a
        y'=-(x-a)sinα+(y-b)cosα+b*/
        float x = (float) ((pointF.x - center.x) * Math.cos(radians) + (pointF.y - center.y) * Math.sin(radians) + center.x);
        float y = (float) (-(pointF.x - center.x) * Math.sin(radians) + (pointF.y - center.y) * Math.cos(radians) + center.y);
        return new PointF(x, y);
    }

    public static float getPic_size(int aspect) {
        switch (aspect) {
            case 1:
                return (float) 1;
            case 2:
                return (float) 0.5625;
            case 3:
                return (float) 0.75;
            case 4:
                return (float) 1.78;
            case 5:
                return (float) 1.33;
            case 6:
                return (float) 1.5;
            case 7:
                return (float) 2;
            case 8:
                return (float) 0.67;
            case 9:
                return (float) 0.5;
        }
        return 0;
    }

    public static int getPic_sizeIndex(float ratio) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (df.format(ratio).equals(df.format(1))) {
            return 1;
        } else if (df.format(ratio).equals(df.format(0.5625))) {
            return 2;
        } else if (df.format(ratio).equals(df.format(0.75))) {
            return 3;
        } else if (df.format(ratio).equals(df.format(1.78))) {
            return 4;
        } else if (df.format(ratio).equals(df.format(1.33))) {
            return 5;
        } else if (df.format(ratio).equals(df.format(1.5))) {
            return 6;
        } else if (df.format(ratio).equals(df.format(2))) {
            return 7;
        } else if (df.format(ratio).equals(df.format(0.67))) {
            return 8;
        } else if (df.format(ratio).equals(df.format(0.5))) {
            return 9;
        }
        return 0;
    }

    /**
     * 获取旋转角度
     *
     * @param matrix matrix
     * @return float
     */

    public static float getMatrixAngle(Matrix matrix) {
        return (float) -(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
                getMatrixValue(matrix, Matrix.MSCALE_X)) * (180 / Math.PI));
    }

    public static float getMatrixValue(Matrix matrix, int valueIndex) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[valueIndex];
    }

    public static Paint getPuzzleSettingPaint(Paint paint){
        paint.reset();
        paint.setAntiAlias(true); // 防止边缘的锯齿
        paint.setFilterBitmap(true); // 对位图进行滤波处理
//        paint.setDither(true); // 设置是否抖动，不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些

        return paint;
    }

}
