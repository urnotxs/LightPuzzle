package com.xs.lightpuzzle.puzzle.info.low;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.LightPuzzleConstant;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.info.DrawView;


/**
 * Created by xs on 2018/4/12.
 * 遮罩图层
 */

public class PuzzlesMaskInfo implements DrawView {

    private String maskPic;

    private transient Rect rect;
    //保存时用到的rect
    private Rect outPutRect;

    private int bgColor = LightPuzzleConstant.INVALID_COLOR;

    private transient boolean save;

    private transient Bitmap maskBitmap, colorMaskBitmap;

    private Rect finalRect;
    private Paint mPaint;
    private Drawable mDrawable;
    private Bitmap mTextureBitmap;

    public void setMaskPic(String maskPic) {
        this.maskPic = maskPic;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    @Override
    public void init() {
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
        if (finalRect == null) {
            return;
        }

        mPaint = new Paint();
    }

    @Override
    public void initBitmap(Context context) {
        if (context == null) {
            return;
        }
        if (finalRect == null) {
            return;
        }
        if (TextUtils.isEmpty(maskPic)) {
            return;
        }

        synchronized (this) {
            maskBitmap = JaneBitmapFactory.decode(context, maskPic, new ImageSize(finalRect.width(), finalRect.height()));
        }

        if (mDrawable == null) {
            mDrawable = new BitmapDrawable(maskBitmap);
            mDrawable.setBounds(new Rect(0, 0, finalRect.width(), finalRect.height()));
        }
        changeBgTexture(null, bgColor);
    }

    public void changeBgTexture(Bitmap colorTextureBitmap, int bgColor) {
        if (BitmapHelper.isInvalid(maskBitmap)) {
            return;
        }
        if (BitmapHelper.isValid(colorTextureBitmap)) {
            mTextureBitmap = colorTextureBitmap;
        }
        if (bgColor != LightPuzzleConstant.INVALID_COLOR) {
            this.bgColor = bgColor;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (finalRect == null) {
            return;
        }

        if (BitmapHelper.isValid(mTextureBitmap)) {
            drawTexture(canvas);
        } else {
            if (bgColor != LightPuzzleConstant.INVALID_COLOR) {
                DrawableCompat.setTint(mDrawable, bgColor);
            }
            canvas.save();
            if (finalRect.left != 0 && finalRect.top != 0) {
                canvas.translate(finalRect.left, finalRect.top);
            }
            mDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private void drawTexture(Canvas canvas) {
        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        {
            canvas.drawBitmap(maskBitmap, null, finalRect, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            Shader shader = new BitmapShader(mTextureBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mPaint.setShader(shader);
            canvas.drawRect(finalRect, mPaint);
            mPaint.setXfermode(null);
        }
        canvas.restoreToCount(saved);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void recycle() {
        maskBitmap = null;
    }
}
