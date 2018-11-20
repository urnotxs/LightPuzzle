package com.xs.lightpuzzle.puzzle.util;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;

/**
 * Created by pocouser on 2015/11/23 0023.
 */
public class ShapeUtils {

    public static Point[] makePts(PointF[] ptfs, int w, int h) {
        if (ptfs != null) {
            Point[] pts = new Point[ptfs.length];
            for (int i = 0; i < ptfs.length; i++) {
                pts[i] = new Point();
                pts[i].x = Math.round(ptfs[i].x * (float) w);
                if (pts[i].x < 0) {
                    pts[i].x = 0;
                } else if (pts[i].x > (w - 1)) {
                    pts[i].x = w;
                }

                pts[i].y = Math.round(ptfs[i].y * (float) h);
                if (pts[i].y < 0) {
                    pts[i].y = 0;
                } else if (pts[i].y > h) {
                    pts[i].y = h;
                }
            }
            return pts;
        }
        return null;
    }

    /**
     * 带位置的rect转成点
     *
     * @param ptfs
     * @param rect
     * @return
     */

    public static Point[] makePts(PointF[] ptfs, Rect rect) {
        if (ptfs != null) {
            Point[] pts = new Point[ptfs.length];
            for (int i = 0; i < ptfs.length; i++) {
                pts[i] = new Point();
                pts[i].x = (int) (Math.round(ptfs[i].x * (float) rect.width()) + rect.left);
                if (pts[i].x < 0) {
                    pts[i].x = rect.left;
                } else if (pts[i].x > rect.right) {
                    pts[i].x = rect.right;
                }
                pts[i].y = (int) (Math.round(ptfs[i].y * (float) rect.height()) + rect.top);
                if (pts[i].y < 0) {
                    pts[i].y = rect.top;
                } else if (pts[i].y > rect.bottom) {
                    pts[i].y = rect.bottom;
                }
            }
            return pts;
        }
        return null;
    }

    public static PointF[] makePtsF(PointF[] ptfs, Rect rect) {
        if (ptfs != null) {
            PointF[] pts = new PointF[ptfs.length];
            for (int i = 0; i < ptfs.length; i++) {
                pts[i] = new PointF();
                pts[i].x = (float) (Math.ceil(ptfs[i].x * (float) rect.width()) + rect.left);
                if (pts[i].x < 0) {
                    pts[i].x = rect.left;
                } else if (pts[i].x > rect.right) {
                    pts[i].x = rect.right;
                }
                pts[i].y = (float) (Math.ceil(ptfs[i].y * (float) rect.height()) + rect.top);
                if (pts[i].y < 0) {
                    pts[i].y = rect.top;
                } else if (pts[i].y > rect.bottom) {
                    pts[i].y = rect.bottom;
                }
            }
            return pts;
        }
        return null;
    }

    public static Rect makeRect(Point[] points) {
        if (points != null) {
            Rect rect = new Rect(points[0].x, points[0].y, 0, 0);
            for (int i = 1; i < points.length; i++) {
                if (points[i].x < rect.left) {
                    rect.left = points[i].x;
                }
                if (points[i].x > rect.right) {
                    rect.right = points[i].x;
                }
                if (points[i].y < rect.top) {
                    rect.top = points[i].y;
                }
                if (points[i].y > rect.bottom) {
                    rect.bottom = points[i].y;
                }
            }
            return rect;
        }
        return null;
    }

    public static Point[] rect2points(Rect rect) {
        if (rect != null) {
            Point[] points = new Point[4];
            points[0] = new Point(rect.left, rect.top);
            points[1] = new Point(rect.right, rect.top);
            points[2] = new Point(rect.right, rect.bottom);
            points[3] = new Point(rect.left, rect.bottom);
            return points;
        }
        return null;
    }

    public static Path ptsToPath(Point[] pts) {
        Path path = new Path();
        path.moveTo(pts[0].x, pts[0].y);
        for (int i = 1; i < pts.length; i++) {
            path.lineTo(pts[i].x, pts[i].y);
        }
        path.lineTo(pts[0].x, pts[0].y);
        return path;
    }

    public static Path ptsFToPath(PointF[] pts) {
        Path path = new Path();
        path.moveTo(pts[0].x, pts[0].y);
        for (int i = 1; i < pts.length; i++) {
            path.lineTo(pts[i].x, pts[i].y);
        }
        path.lineTo(pts[0].x, pts[0].y);
        return path;
    }

    public static Rect ptsToRect(Point[] pts) {
        return ptsToRect(pts, false);
    }

    public static boolean isRect(Point[] pts) {
        if (pts != null && pts.length == 4) {

            if (pts[0].x == pts[3].x && pts[1].x == pts[2].x) {
                if (pts[0].y == pts[1].y && pts[2].y == pts[3].y) {
                    return true;
                }
            }
        }


        return false;
    }

    public static Rect ptsToRect(Point[] pts, boolean isContainEqual) {
        if (pts == null)
            return null;
        Rect rect = new Rect(0x07ffffff, 0x07ffffff, 0, 0);
        for (int i = 0; i < pts.length; i++) {
            if (pts[i].x < rect.left)
                rect.left = pts[i].x;
            if (pts[i].x > rect.right)
                rect.right = pts[i].x;
            if (pts[i].y < rect.top)
                rect.top = pts[i].y;
            if (pts[i].y > rect.bottom)
                rect.bottom = pts[i].y;
        }
        if (isContainEqual) {
            if (rect.left <= rect.right && rect.top <= rect.bottom)
                return rect;
        } else {
            if (rect.left < rect.right && rect.top < rect.bottom)
                return rect;
        }
        return null;
    }

    public static PointF[] makePts(int left, int top, int width, int height) {
        PointF[] drawPoint = new PointF[4];
        int right = left + width;
        int bottom = top + height;

        drawPoint[0] = new PointF(left, top);
        drawPoint[1] = new PointF(right, top);
        drawPoint[2] = new PointF(right, bottom);
        drawPoint[3] = new PointF(left, bottom);
        return drawPoint;
    }

    /**
     * 缩小图片画图区域
     *
     * @param rect
     * @return
     */
    public static Rect zoomOutRect(Rect rect) {
        if (rect != null) {
            Rect rrect = new Rect();
            rrect.left = rect.left + Utils.getRealPixel3(10);
            rrect.top = rect.top + Utils.getRealPixel3(10);
            rrect.right = rect.right - Utils.getRealPixel3(10);
            rrect.bottom = rect.bottom - Utils.getRealPixel3(10);
            if (rrect.left < rrect.right && rrect.top < rrect.bottom) {
                return rrect;
            }

        }
        return null;
    }

    public static float getAngle(int x1, int y1, int x2, int y2) {
        float radian = 0;
        int ox = Math.abs(x1 - x2);
        int oy = Math.abs(y1 - y2);
        int diagonal = (int) Math.sqrt((double) (ox * ox + oy * oy));
        int ly = 0, ry = 0;
        if (x1 < x2) {
            ly = y1;
            ry = y2;
        } else {
            ly = y2;
            ry = y1;
        }
        if (ly < ry) {
            radian = (float) (Math.PI / 2 + Math.acos((double) oy
                    / (double) diagonal));
        } else {
            radian = (float) Math.acos((double) ox / (double) diagonal);
        }
        if (y2 < y1) {
            radian += Math.PI;
        }
        return radian;
    }

    public static boolean hitText(Rect drawRect, PointF[] polygonPts, int x, int y) {

        if (drawRect != null && polygonPts != null) {

            Point[] pts = makePts(polygonPts, drawRect.width(), drawRect.height());
            Path path = ptsToPath(pts);
            Rect rect = ptsToRect(pts);
            if (rect != null) {
                Region reg = new Region();
                reg.set(rect);
                reg.setPath(path, reg);
                if (reg.contains(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hitText(Rect drawRect, Path path, int x, int y) {
        if (drawRect != null && path != null) {
            Region reg = new Region();
            reg.set(drawRect);
            reg.setPath(path, reg);
            if (reg.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hitText(Rect rect, int x, int y) {
        if (rect != null) {
            if (rect.contains(x, y)) {
                System.out.println("范围之内");
                return true;
            } else {
                System.out.println("范围之外");

            }

        }
        System.out.println("范围之外");
        return false;
    }

    public static Path rectToPath(Rect rect) {
        if (rect == null)
            return null;
        Path path = new Path();
        path.moveTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.lineTo(rect.right, rect.bottom);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.left, rect.top);
        return path;
    }

    public static boolean pointFEqual(PointF[] firstPointArr, PointF[] secondPointArr) {
        if (firstPointArr == null || secondPointArr == null
                || firstPointArr.length != secondPointArr.length) {
            return false;
        }

        boolean isEqual = true;
        for (int i = 0; i < firstPointArr.length; i++) {
            PointF firstPoint = firstPointArr[i];
            PointF secondPoint = secondPointArr[i];
            if(firstPoint != null && secondPoint != null){
                if (firstPoint.x == secondPoint.x && firstPoint.y == secondPoint.y) {
                    continue;
                } else {
                    return false;
                }
            }else{
                return false;
            }

        }
        return isEqual;
    }

    public static boolean pointEqual(Point[] firstPointArr, Point[] secondPointArr) {
        if (firstPointArr == null
                || secondPointArr == null
                || firstPointArr.length != secondPointArr.length) {
            return false;
        }

        boolean isEqual = true;
        for (int i = 0; i < firstPointArr.length; i++) {
            Point firstPoint = firstPointArr[i];
            Point secondPoint = secondPointArr[i];
            if (firstPoint != null && secondPoint != null) {
                if (firstPoint.x != secondPoint.x || firstPoint.y != secondPoint.y) {
                    isEqual = false;
                    break;
                }
            } else {
                isEqual = false;
                break;
            }
        }
        return isEqual;
    }
}
