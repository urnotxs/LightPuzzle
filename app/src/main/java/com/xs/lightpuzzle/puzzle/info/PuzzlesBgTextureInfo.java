package com.xs.lightpuzzle.puzzle.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.LightPuzzleConstant;
import com.xs.lightpuzzle.puzzle.data.BgTextureData;

/**
 * Created by Lin on 2018/4/12.
 */

public class PuzzlesBgTextureInfo implements DrawView {

    private transient Rect rect;
    //保存时用到的rect
    private Rect outPutRect;

    private transient int mLastHeight;

    private int bgColor;

    private String textureStr;
    //texture alpha
    private float alpha;
    //background effect
    private String effect;

    private int waterColor = LightPuzzleConstant.INVALID_COLOR;

    private transient boolean save;

    private transient Bitmap colorTextureBitmap;

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setTextureStr(String textureStr) {
        this.textureStr = textureStr;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getBgColor() {
        return bgColor;
    }

    public String getTextureStr() {
        return textureStr;
    }

    public float getAlpha() {
        return alpha;
    }

    public String getEffect() {
        return effect;
    }

    public Bitmap getColorTextureBitmap() {
        return colorTextureBitmap;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public int getWaterColor() {
        return waterColor;
    }

    public void setWaterColor(int waterColor) {
        this.waterColor = waterColor;
    }

    @Override
    public void init() {

    }

    @Override
    public void initBitmap(Context context) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(textureStr) || ("none").equals(textureStr)) {
            colorTextureBitmap = null;
            return;
        }
//        DecodeImageOptions options = new DecodeImageOptions.Builder()
//                .setImageScaleType(ImageScaleType.NONE)
//                .build();
//        Bitmap textureBitmap = JaneBitmapFactory.decodeAssets(context, DirConstant.TEXTURE_RESOURCE_PATH + File
//                .separator + textureStr, null, options);
//        if (BitmapHelper.isValid(textureBitmap)) {
//            Bitmap colorBitmap = Bitmap.createBitmap(textureBitmap.getWidth(), textureBitmap.getHeight(),
//                    Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(colorBitmap);
//            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
//            if (bgColor == 0) {
//                bgColor = -1;
//            }
//            canvas.drawColor(bgColor);
//            colorTextureBitmap = MakeMixAndEffect.DoubleExposeMix(colorBitmap, textureBitmap, 0, effect, alpha);
//        }
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
        Rect finalRect = null;
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
        if (outPutRect == null) {
            return;
        }
        canvas.save();
        canvas.translate(0, -mLastHeight);
//        if (BitmapHelper.isValid(colorTextureBitmap)) {
//            float scal = (float) finalRect.width() / (float) outPutRect.width();
//            int drawWidth = (int) (colorTextureBitmap.getWidth() * scal);
//            int drawHeight = (int) (colorTextureBitmap.getHeight() * scal);
//            //横轴循环次数
//            int count_x = (finalRect.width() + drawWidth - 1) / drawWidth;
//            //纵轴循环次数
//            int count_y = (finalRect.height() + drawHeight - 1) / drawHeight;
//            Matrix m = new Matrix();
//            for (int idy = 0; idy < count_y; idy++) {
//                for (int idx = 0; idx < count_x; idx++) {
//                    m.reset();
//                    m.setScale(scal, scal);
//                    m.postTranslate(idx * drawWidth, idy * drawHeight);
//                    canvas.drawBitmap(colorTextureBitmap, m, null);
//                }
//            }
//        } else {
//
//        }
        Paint paint = new Paint();
        paint.setColor(bgColor);
        canvas.drawRect(finalRect, paint);
        canvas.restore();
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
        colorTextureBitmap = null;
    }

    public BgTextureData getBgTextureData() {
        BgTextureData bgTextureData = new BgTextureData();
        bgTextureData.setAlpha(alpha);
        bgTextureData.setBgColor(bgColor);
        bgTextureData.setEffect(effect);
        bgTextureData.setTexture(textureStr);
        bgTextureData.setWaterColor(waterColor);
        return bgTextureData;
    }
}
