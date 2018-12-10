package com.xs.lightpuzzle.puzzle.layout.info.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;

import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.DecodeImageOptions;
import com.xs.lightpuzzle.imagedecode.core.ImageScaleType;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.data.RotationImg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by urnot_XS on 2017/12/12.
 * 布局保存对象，初始+绘制
 */

public class SavePolygonLayoutInfo implements Serializable {

    private transient static Xfermode SRC_IN = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    private transient Context mContext;

    // 传入的参数
    private Rect mRect;
    private int mPuzzleMode;
    private float mJsonWidth;
    private float mJsonHeight;
    private float mRadianRatio;
    private float mInsidePaddingRatio;
    private float mOutsidePaddingRatio;
    private RotationImg[] mRotationImgs;
    private ArrayList<RectF> mRectList;
    private ArrayList<SavePieceVO> mPieceVOList;
    private transient LayoutData mLayoutData;// 用于布局的放大缩小
    private transient ArrayList<Boolean> mZoomArray = new ArrayList<>();// 用于布局的放大缩小

    // 生成的参数
    private float mTotalOuterPadding;
    private float mTotalInsidePadding;
    private ArrayList<RectF> mShowRectList;
    private ArrayList<RectF> mShowPieceList;

    public void init() {
        //将传进来的矩形列表和piece列表进行转换

        float ratioW = mRect.width() / mJsonWidth;
        float ratioH = mRect.height() / mJsonHeight;

        if (mRect.width() / mRect.height() < 1) {
            mTotalOuterPadding = ((250.0f / 2048) * mRect.width());
        } else {
            mTotalOuterPadding = ((250.0f / 2048) * mRect.height());
        }
        if ((mRect.width() * 1.0f) / mRect.height() > 1) {
            mTotalInsidePadding = ((80.0f / 2048) * mRect.width());
        } else {
            mTotalInsidePadding = ((80.0f / 2048) * mRect.height());
        }

        layout(ratioW, ratioH);
    }

    private void layout(float mScalWidthRatio, float mScalHeightRatio) {

        mShowRectList = new ArrayList<>();
        mShowPieceList = new ArrayList<>();

        if (mPuzzleMode == PuzzleMode.MODE_LAYOUT) {
            loadLayoutRectData(mScalWidthRatio, mScalHeightRatio);
        } else if (mPuzzleMode == PuzzleMode.MODE_LAYOUT_JOIN) {
            mShowRectList = mRectList;
        }

        loadPieceRectData(mScalWidthRatio, mScalHeightRatio);

    }

    private void loadPieceRectData(float mScalWidthRatio, float mScalHeightRatio) {

        for (int i = 0; i < mPieceVOList.size(); i++) {

            RectF rectF = new RectF();
            rectF.left = mScalWidthRatio * mPieceVOList.get(i).getPieceRect().left;
            rectF.right = mScalWidthRatio * mPieceVOList.get(i).getPieceRect().right;
            rectF.top = mScalHeightRatio * mPieceVOList.get(i).getPieceRect().top;
            rectF.bottom = mScalHeightRatio * mPieceVOList.get(i).getPieceRect().bottom;
            mShowPieceList.add(rectF);
        }
    }

    private void loadLayoutRectData(float mScalWidthRatio, float mScalHeightRatio) {

        float insidePadding = (mInsidePaddingRatio * mTotalInsidePadding) / 2.0f;
        float outsidePadding = (mOutsidePaddingRatio * mTotalOuterPadding);

        for (int i = 0; i < mRectList.size(); i++) {
            float left = (mRectList.get(i).left * mScalWidthRatio);
            float top = (mRectList.get(i).top * mScalHeightRatio);
            float right = (mRectList.get(i).right * mScalWidthRatio);
            float bottom = (mRectList.get(i).bottom * mScalHeightRatio);

            float paddingLeft = insidePadding;
            float paddingTop = insidePadding;
            float paddingRight = insidePadding;
            float paddingBottom = insidePadding;

            // 误差计算
            if (Math.abs(left - mRect.left) < 2) {
                paddingLeft = outsidePadding;
            }
            if (Math.abs(top - mRect.top) < 2) {
                paddingTop = outsidePadding;
            }
            if (Math.abs(right - mRect.right) < 2) {
                paddingRight = outsidePadding;
            }
            if (Math.abs(bottom - mRect.bottom) < 2) {
                paddingBottom = outsidePadding;
            }

            left = left + paddingLeft;
            top = top + paddingTop;
            right = right - paddingRight;
            bottom = bottom - paddingBottom;
            mShowRectList.add(new RectF(left, top, right, bottom));
        }
    }

    public void initBitmap(Context context) {
        mContext = context;
        //将图片路径转化成XqBitmap ，以及加载对应的滤镜
    }

    public void draw(Canvas canvas) {

        Matrix matrix = new Matrix();

        for (int i = 0; i < mShowRectList.size(); i++) {
            BitmapDrawable drawable = generateFinalDrawable(i);
            //绘制布局
            if (drawable != null && i < mPieceVOList.size()) {
                //调整误差
                mShowRectList.get(i).right = mShowRectList.get(i).right + 1;
                mShowRectList.get(i).bottom = mShowRectList.get(i).bottom + 1;

                mShowPieceList.get(i).left = mShowPieceList.get(i).left - 1;
                mShowPieceList.get(i).top = mShowPieceList.get(i).top - 1;
                mShowPieceList.get(i).right = mShowPieceList.get(i).right + 1;
                mShowPieceList.get(i).bottom = mShowPieceList.get(i).bottom + 2;

                Bitmap bitmap = (drawable).getBitmap();
                Paint paint = (drawable).getPaint();

                int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
                paint.setColor(Color.RED);

                canvas.drawRoundRect(mShowRectList.get(i), getRadian(mShowRectList.get(i)), getRadian(mShowRectList.get(i)), paint);
                paint.setXfermode(SRC_IN);

                matrix.reset();
                matrix.postRotate(mPieceVOList.get(i).getDegree(), bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                canvas.drawBitmap(bitmap, null, mShowPieceList.get(i), paint);
                paint.setXfermode(null);
                canvas.restoreToCount(saved);
            }
        }
    }

    private BitmapDrawable generateFinalDrawable(int pos) {
        DecodeImageOptions decodeImageOptions = new DecodeImageOptions.Builder()
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .setImageScaleType(ImageScaleType.EXACTLY)
                .setDecodeingOptions(new BitmapFactory.Options())
                .setNativeRetreatment(true)
                .build();

        Bitmap sourceBmp = JaneBitmapFactory.decodefile(
                mContext, mRotationImgs[pos].getPicPath(),
                new ImageSize((int) (mShowPieceList.get(pos).width()), (int) mShowPieceList.get(pos).height()), decodeImageOptions);

        Bitmap beautyBmp = generateBeautyBmp(sourceBmp, mRotationImgs[pos]);
        Bitmap filterBmp = generateFilterBmp(beautyBmp, mRotationImgs[pos]);
        Bitmap filterAlphaBmp = generateFilterAlphaBmp(beautyBmp, filterBmp, mRotationImgs[pos]);

        Bitmap finalBitmap = null;
        if (BitmapHelper.isValid(sourceBmp)) {
            finalBitmap = sourceBmp;
        }
        if (BitmapHelper.isValid(beautyBmp)) {
            finalBitmap = beautyBmp;
        }
        if (BitmapHelper.isValid(filterBmp)) {
            finalBitmap = filterBmp;
        }
        if (BitmapHelper.isValid(filterAlphaBmp)) {
            finalBitmap = filterAlphaBmp;
        }

        BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), finalBitmap);
        drawable.setAntiAlias(true);
        drawable.setFilterBitmap(true);

        return drawable;
    }

    /**
     * 生成美颜效果图
     * 至少与原图一致
     */
    public Bitmap generateBeautyBmp(Bitmap bitmap, RotationImg rotationImg) {
        Bitmap beautyBitmap = null;

        if (rotationImg == null) {
            return null;
        }

        if (BitmapHelper.isInvalid(bitmap)) {
            return null;
        }

        if (rotationImg.getSkinSmoothAlpha() != 0 || rotationImg.getSkinColorAlpha() != 0) {
            // 有美肤参数
//            beautyBitmap = PocoBeautyFilter.CameraSmoothBeauty
//                    (bitmap.copy(Bitmap.Config.ARGB_8888, true), rotationImg.getSkinSmoothAlpha());// 磨皮
//
//            byte[] skinDatas = FileUtils.assetsToByte(JaneApplication.getContext(), DirConstant.SKIN_STYLE_PATH);
//            beautyBitmap = PocoBeautyFilter.CameraSkinBeauty
//                    (beautyBitmap, skinDatas, rotationImg.getSkinColorAlpha());// 肤色
        } else {
            beautyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 原图
        }
        return beautyBitmap;
    }

    /**
     * 生成滤镜效果图
     */
    private Bitmap generateFilterBmp(Bitmap bitmap, RotationImg rotationImg) {
        Bitmap filterBitmap = null;

//        if (rotationImg == null || rotationImg.getTepFilterInfo() == null) {
//            return null;
//        }
//
//        if (BitmapHelper.isInvalid(bitmap)) {
//            return null;
//        }
//
//        TepFilterInfo tepFilterInfo = rotationImg.getTepFilterInfo();
//
//        if (!TextUtils.isEmpty(tepFilterInfo.getName()) && !("ORIGINAL").equals(tepFilterInfo.getName())) {
//
//            Bitmap lookupBitmap = tepFilterInfo.getLookupBitmap(JaneApplication.getContext());
//            Bitmap[] maskBitmaps = tepFilterInfo.getMaskBitmaps(JaneApplication.getContext(),
//                    bitmap.getWidth(), bitmap.getHeight());
//
//            Bitmap tmpFilterBmp = RsFilter.loadFilterV2_rs(JaneApplication.getContext(), bitmap.copy(Bitmap.Config.ARGB_8888, true),
//                    lookupBitmap, maskBitmaps, tepFilterInfo.getComOps(), tepFilterInfo.getSrcAlphas());
//
//            if (BitmapHelper.isValid(tmpFilterBmp)) {
//                synchronized (this) {
//                    filterBitmap = tmpFilterBmp;
//                }
//            }
//
//        }
        return filterBitmap;
    }

    /**
     * 生成带透明度的滤镜效果图
     */
    public Bitmap generateFilterAlphaBmp(Bitmap bitmap, Bitmap filterBitmap, RotationImg rotationImg) {
        Bitmap alphaBitmap = null;

//        if (rotationImg == null || rotationImg.getTepFilterInfo() == null) {
//            return null;
//        }
//
//        if (BitmapHelper.isInvalid(bitmap) || BitmapHelper.isInvalid(filterBitmap)) {
//            return null;
//        }
//
//        TepFilterInfo tepFilterInfo = rotationImg.getTepFilterInfo();
//
//        float alpha = tepFilterInfo != null ? tepFilterInfo.getAlpha() : 0;
//
//        synchronized (this) {
//            alphaBitmap = RsFilter.overlayOpacity(bitmap, filterBitmap, (int) (alpha * 255));
//        }

        return alphaBitmap;
    }

    private float getRadian(RectF rectF) {
        float length = rectF.width() < rectF.height() ? rectF.width() : rectF.height();
        return (length / 2.0f) * mRadianRatio;
    }

    public ArrayList<SavePieceVO> getPieceVOList() {
        return mPieceVOList;
    }

    public void setPieceVOList(ArrayList<SavePieceVO> mPieceVOList) {
        this.mPieceVOList = mPieceVOList;
    }

    public ArrayList<Boolean> getZoomArray() {
        return mZoomArray;
    }

    public void setZoomArray(ArrayList<Boolean> zoomArray) {
        this.mZoomArray = zoomArray;
    }

    public RotationImg[] getRotationImgs() {
        return mRotationImgs;
    }

    public void setRotationImgs(RotationImg[] rotationImgs) {
        mRotationImgs = rotationImgs;
    }

    public ArrayList<RectF> getRectList() {
        return mRectList;
    }

    public void setRectList(ArrayList<RectF> mRectList) {
        this.mRectList = mRectList;
    }

    public float getJsonWidth() {
        return mJsonWidth;
    }

    public void setJsonWidth(float jsonWidth) {
        this.mJsonWidth = jsonWidth;
    }

    public LayoutData getLayoutData() {
        return mLayoutData;
    }

    public void setLayoutData(LayoutData layoutData) {
        this.mLayoutData = layoutData;
    }

    public float getJsonHeight() {
        return mJsonHeight;
    }

    public void setJsonHeight(float jsonHeight) {
        this.mJsonHeight = jsonHeight;
    }

    public float getInsidePaddingRatio() {
        return mInsidePaddingRatio;
    }

    public void setInsidePaddingRatio(float mInsidePaddingRatio) {
        this.mInsidePaddingRatio = mInsidePaddingRatio;
    }

    public float getOutsidePaddingRatio() {
        return mOutsidePaddingRatio;
    }

    public void setOutsidePaddingRatio(float mOutsidePaddingRatio) {
        this.mOutsidePaddingRatio = mOutsidePaddingRatio;
    }

    public float getRadianRatio() {
        return mRadianRatio;
    }

    public void setRadianRatio(float mRadianRatio) {
        this.mRadianRatio = mRadianRatio;
    }

    public int getPuzzleMode() {
        return mPuzzleMode;
    }

    public void setPuzzleMode(int puzzleMode) {
        this.mPuzzleMode = puzzleMode;
    }

    public void setRect(Rect mRect) {
        this.mRect = mRect;
    }

}
