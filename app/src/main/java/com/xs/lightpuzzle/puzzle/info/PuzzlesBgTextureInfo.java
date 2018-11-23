package com.xs.lightpuzzle.puzzle.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.xs.lightpuzzle.LightPuzzleConstant;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.data.BgTextureData;
import com.xs.lightpuzzle.puzzle.util.PuzzleUtils;
import com.xs.lightpuzzle.yszx.Scheme;

import java.io.IOException;

import cn.poco.filter.POCOCompositor;

/**
 * Created by xs on 2018/4/12.
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
        String textureUri = "texture/" + textureStr;
        int[] textureSize = new int[2];
        try {
            textureSize = PuzzleUtils.getBitmapSize(context,
                    Scheme.ASSETS.wrap(textureUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        float scale = (float) rect.width() / (float) outPutRect.width();
        int drawWidth = (int) (textureSize[0] * scale);
        int drawHeight = (int) (textureSize[1] * scale);
        Bitmap textureBitmap = null;
        if (drawWidth > 0 && drawHeight > 0) {
            textureBitmap = JaneBitmapFactory.decodeAssets(context,
                    textureUri, new ImageSize(drawWidth, drawHeight));
        }
        if (BitmapHelper.isValid(textureBitmap)) {
            Bitmap colorBitmap = Bitmap.createBitmap(
                    textureBitmap.getWidth(), textureBitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(colorBitmap);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            if (bgColor == 0) {
                bgColor = -1;
            }
            canvas.drawColor(bgColor);
            int composition = effect.equals("正常") ? 1 : 41;
            int opacity = (int) (alpha * 250);
            colorTextureBitmap = POCOCompositor
                    .composite(colorBitmap, textureBitmap,
                            POCOCompositor.A_B_INVERSE.NO,
                            composition, opacity);
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

        if (BitmapHelper.isValid(colorTextureBitmap)) {
            drawTexture(canvas);
        } else {
            Paint paint = new Paint();
            paint.setColor(bgColor);
            canvas.drawRect(finalRect, paint);
            canvas.restore();
        }
    }

    private boolean drawTexture(Canvas canvas) {
        if (colorTextureBitmap == null) {
            return false;
        }
        Paint paint = new Paint();
        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        {
            Shader shader = new BitmapShader(colorTextureBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paint.setShader(shader);
            canvas.drawRect(rect, paint);
            paint.setXfermode(null);
        }
        canvas.restoreToCount(saved);
        return true;
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
