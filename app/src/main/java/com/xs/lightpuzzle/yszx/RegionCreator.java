package com.xs.lightpuzzle.yszx;

import android.graphics.Rect;
import android.graphics.RectF;

public class RegionCreator {

    public static float getCenterX(Rect rect) {
        return rect == null ? 0 : rect.centerX();
    }

    public static float getCenterX(RectF rectF) {
        return rectF == null ? 0 : rectF.centerX();
    }

    public static float getCenterY(Rect rect) {
        return rect == null ? 0 : rect.centerY();
    }

    public static float getCenterY(RectF rectF) {
        return rectF == null ? 0 : rectF.centerY();
    }

    public static int getWidth(Rect rect) {
        return rect == null ? 0 : rect.width();
    }

    public static float getWidth(RectF rectF) {
        return rectF == null ? 0 : rectF.width();
    }

    public static int getHeight(Rect rect) {
        return rect == null ? 0 : rect.height();
    }

    public static float getHeight(RectF rectF) {
        return rectF == null ? 0 : rectF.height();
    }

    // ---

    public static void scale(Rect rect, float scaleX, float scaleY) {
        if (rect == null) {
            return;
        }
        if (scaleX != 1.0f) {
            rect.left *= scaleX;
            rect.right *= scaleX;
        }
        if (scaleY != 1.0f) {
            rect.top *= scaleY;
            rect.bottom *= scaleY;
        }
    }

    public static void scale(RectF rect, float scaleX, float scaleY) {
        if (rect == null) {
            return;
        }
        if (scaleX != 1.0f) {
            rect.left *= scaleX;
            rect.right *= scaleX;
        }
        if (scaleY != 1.0f) {
            rect.top *= scaleY;
            rect.bottom *= scaleY;
        }
    }

    public static void scaleRoundly(Rect rect, float scaleX, float scaleY) {
        if (rect == null) {
            return;
        }
        if (scaleX != 1.0f) {
            rect.left = Math.round(rect.left * scaleX);
            rect.right = Math.round(rect.right * scaleX);
        }
        if (scaleY != 1.0f) {
            rect.top = Math.round(rect.top * scaleY);
            rect.bottom = Math.round(rect.bottom * scaleY);
        }
    }

    public static void scaleRoundly(RectF rect, float scaleX, float scaleY) {
        if (rect == null) {
            return;
        }
        if (scaleX != 1.0f) {
            rect.left = Math.round(rect.left * scaleX);
            rect.right = Math.round(rect.right * scaleX);
        }
        if (scaleY != 1.0f) {
            rect.top = Math.round(rect.top * scaleY);
            rect.bottom = Math.round(rect.bottom * scaleY);
        }
    }

    public static Rect duplicateAndScale(Rect rect, float scaleX, float scaleY) {
        if (rect == null) {
            return null;
        }
        Rect duplicate = new Rect(rect);
        scale(duplicate, scaleX, scaleY);
        return duplicate;
    }

    public static RectF duplicateAndScale(RectF rect, float scaleX, float scaleY) {
        if (rect == null) {
            return null;
        }
        RectF duplicate = new RectF(rect);
        scale(duplicate, scaleX, scaleY);
        return duplicate;
    }

    public static Rect duplicateAndScaleRoundly(Rect rect, float scaleX, float scaleY) {
        if (rect == null) {
            return null;
        }
        Rect duplicate = new Rect(rect);
        scaleRoundly(duplicate, scaleX, scaleY);
        return duplicate;
    }

    public static RectF duplicateAndScaleRoundly(RectF rect, float scaleX, float scaleY) {
        if (rect == null) {
            return null;
        }
        RectF duplicate = new RectF(rect);
        scaleRoundly(duplicate, scaleX, scaleY);
        return duplicate;
    }

    public static Rect toRect(RectF rect) {
        if (rect == null) {
            return null;
        }
        return new Rect((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom);
    }

    public static Rect createByLTWH(int left, int top, int width, int height) {
        return new Rect(left, top, left + width, top + height);
    }

    public static RectF createFByLTWH(int left, int top, int width, int height) {
        return new RectF(createByLTWH(left, top, width, height));
    }

    public static RectF createFByLTWH(float left, float top, float width, float height) {
        return new RectF(left, top, left + width, top + height);
    }

    public static Rect create(int left, int top, int right, int bottom) {
        return new Rect(left, top, right, bottom);
    }

    public static RectF createF(int left, int top, int right, int bottom) {
        return new RectF(create(left, top, right, bottom));
    }

    public static RectF createF(float left, float top, float right, float bottom) {
        return new RectF(left, top, right, bottom);
    }

    public static void increases(RectF r, float tolerance) {
        if (r == null || tolerance == 0.0F) {
            return;
        }
        r.left -= tolerance;
        r.top -= tolerance;
        r.right += tolerance;
        r.bottom += tolerance;
    }

    public static RectF duplicateAndIncreases(RectF rectF, float tolerance) {
        if (rectF == null) {
            return null;
        }
        RectF r = new RectF(rectF);
        increases(r, tolerance);
        return r;
    }

    public static void offsetX(RectF rect, float x) {
        if (rect == null) {
            return;
        }
        rect.left += x;
        rect.right += x;
    }

    public static void offsetY(RectF rect, float y) {
        if (rect == null) {
            return;
        }
        rect.top += y;
        rect.bottom += y;
    }

    public static void offset(RectF rect, float x, float y) {
        if (rect == null) {
            return;
        }
        offsetX(rect, x);
        offsetY(rect, y);
    }

    public static RectF duplicateAndOffsetX(RectF rect, float x) {
        if (rect == null) {
            return null;
        }
        RectF r = new RectF(rect);
        offsetX(r, x);
        return r;
    }

    public static RectF duplicateAndOffsetY(RectF rect, float y) {
        if (rect == null) {
            return null;
        }
        RectF r = new RectF(rect);
        offsetY(r, y);
        return r;
    }

    public static RectF duplicateAndOffset(RectF rect, float x, float y) {
        if (rect == null) {
            return null;
        }
        RectF r = new RectF(rect);
        offset(r, x, y);
        return r;
    }
}
