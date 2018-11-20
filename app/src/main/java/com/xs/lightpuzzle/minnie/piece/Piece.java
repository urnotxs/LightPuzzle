package com.xs.lightpuzzle.minnie.piece;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.xs.lightpuzzle.minnie.PieceRegion;
import com.xs.lightpuzzle.yszx.RegionCreator;

/**
 * Created by xs on 2018/11/13.
 */

public abstract class Piece {

    protected Context mContext;

    protected PieceRegion mSrcRegion;
    // --- Middleware
    protected float mScaleX;
    protected float mScaleY;
    protected PieceRegion mDestRegion;

    public Piece(Context context,
                 /* srcSingleRegion */
                 int srcSingleWidth, int srcSingleHeight,
                 /* srcPieceRegion -> */ RectF srcContentRegion,
                 int destSingleWidth, int destSingleHeight) {
        this(context, 1, 0, 0, srcSingleWidth, srcSingleHeight, srcContentRegion, destSingleWidth,
                destSingleHeight);
    }

    public Piece(Context context,
                 /*platter*/
                 int singleIndex,
                 /*srcSingleRegion*/
                 int srcSingleLeft, int srcSingleTop, int srcSingleWidth, int srcSingleHeight,
                 /*srcPieceRegion -> */ RectF srcContentRegion,
                 int destSingleWidth, int destSingleHeight) {
        mContext = context;
        Rect srcSingleRegion = RegionCreator.createByLTWH(
                srcSingleLeft, srcSingleTop, srcSingleWidth, srcSingleHeight);
        Rect _srcContentRegion = RegionCreator.toRect(srcContentRegion);
        mSrcRegion = PieceRegion.create(srcSingleRegion, _srcContentRegion);
        mScaleX = srcSingleWidth * 1.0F / destSingleWidth;
        mScaleY = srcSingleHeight * 1.0F / destSingleHeight;
        mDestRegion = PieceRegion.createDest(mSrcRegion, mScaleX, mScaleY);
    }

    public Piece(Context context,
                 /*srcSingleRegion*/
                 int srcSingleLeft, int srcSingleTop, int srcSingleWidth, int srcSingleHeight,
                 /*srcPieceRegion -> */ RectF srcContentRegion,
                 int destSingleWidth) {
        mContext = context;
        Rect srcSingleRegion = RegionCreator
                .createByLTWH(srcSingleLeft, srcSingleTop, srcSingleWidth, srcSingleHeight);
        Rect _srcContentRegion = RegionCreator.toRect(srcContentRegion);
        mSrcRegion = PieceRegion.create(srcSingleRegion, _srcContentRegion);
        mScaleX = mScaleY = srcSingleWidth * 1.0F / destSingleWidth;
        mDestRegion = PieceRegion.createDest(mSrcRegion, mScaleX, mScaleY);
    }

    public void draw(Canvas canvas) {
        drawDirectly(canvas);
    }

    public abstract void drawDirectly(Canvas canvas);

    public void throwAway() {
        // no-op by default
    }

    public Context getContext() {
        return mContext;
    }

    // -> Src

    // --> Single

    public Rect getSrcSingleRegion() {
        return mSrcRegion == null ? null : mSrcRegion.getSingleRegion();
    }

    public int getSrcSingleLeft() {
        return getSrcSingleRegion() == null ? 0 : getSrcSingleRegion().left;
    }

    public int getSrcSingleTop() {
        return getSrcSingleRegion() == null ? 0 : getSrcSingleRegion().top;
    }

    public int getSrcSingleRight() {
        return getSrcSingleRegion() == null ? 0 : getSrcSingleRegion().right;
    }

    public int getSrcSingleBottom() {
        return getSrcSingleRegion() == null ? 0 : getSrcSingleRegion().bottom;
    }

    public int getSrcSingleWidth() {
        return getSrcSingleRight() - getSrcSingleLeft();
    }

    public int getSrcSingleHeight() {
        return getSrcSingleBottom() - getSrcSingleTop();
    }

    public float getSrcSingleAspectRatio() {
        return getSrcSingleHeight() <= 0 ? 0 : getSrcSingleWidth() * 1.0F / getSrcSingleHeight();
    }

    // --> Piece

    public Rect getSrcPieceRegion() {
        return mSrcRegion == null ? null : mSrcRegion.getPieceRegion();
    }

    public int getSrcPieceLeft() {
        return getSrcPieceRegion() == null ? 0 : getSrcPieceRegion().left;
    }

    public int getSrcPieceTop() {
        return getSrcPieceRegion() == null ? 0 : getSrcPieceRegion().top;
    }

    public int getSrcPieceRight() {
        return getSrcPieceRegion() == null ? 0 : getSrcPieceRegion().right;
    }

    public int getSrcPieceBottom() {
        return getSrcPieceRegion() == null ? 0 : getSrcPieceRegion().bottom;
    }

    public int getSrcPieceWidth() {
        return getSrcPieceRight() - getSrcPieceLeft();
    }

    public int getSrcPieceHeight() {
        return getSrcPieceBottom() - getSrcPieceTop();
    }

// --> Content

    public Rect getSrcContentRegion() {
        return mSrcRegion == null ? null : mSrcRegion.getContentRegion();
    }

    public RectF getSrcContentRegionF() {
        return getSrcContentRegion() == null ? null : new RectF(getSrcContentRegion());
    }

    public int getSrcContentLeft() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().left;
    }

    public float getSrcContentLeftF() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().left;
    }

    public int getSrcContentTop() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().top;
    }

    public float getSrcContentTopF() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().top;
    }

    public int getSrcContentRight() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().right;
    }

    public float getSrcContentRightF() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().right;
    }

    public int getSrcContentBottom() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().bottom;
    }

    public float getSrcContentBottomF() {
        return getSrcContentRegion() == null ? 0 : getSrcContentRegion().bottom;
    }

    public int getSrcContentWidth() {
        return getSrcContentRight() - getSrcContentLeft();
    }

    public float getSrcContentWidthF() {
        return getSrcContentWidth();
    }

    public int getSrcContentHeight() {
        return getSrcContentBottom() - getSrcContentTop();
    }

    public float getSrcContentHeightF() {
        return getSrcContentHeight();
    }

    public float getSrcContentAspectRatio() {
        return getSrcContentHeight() <= 0 ? 0 : getSrcContentWidth() * 1.0F / getSrcContentHeight();
    }

    // -> Dest

    // --> Single

    public Rect getDestSingleRegion() {
        return mDestRegion == null ? null : mDestRegion.getSingleRegion();
    }

    public RectF getDestSingleRegionF() {
        return getDestSingleRegion() == null ? null : new RectF(getDestSingleRegion());
    }

    public int getDestSingleLeft() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().left;
    }

    public float getDestSingleLeftF() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().left;
    }

    public int getDestSingleTop() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().top;
    }

    public float getDestSingleTopF() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().top;
    }

    public int getDestSingleRight() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().right;
    }

    public float getDestSingleRightF() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().right;
    }

    public int getDestSingleBottom() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().bottom;
    }

    public float getDestSingleBottomF() {
        return getDestSingleRegion() == null ? 0 : getDestSingleRegion().bottom;
    }

    public int getDestSingleWidth() {
        return getDestSingleRight() - getDestSingleLeft();
    }

    public float getDestSingleWidthF() {
        return getDestSingleWidth();
    }

    public int getDestSingleHeight() {
        return getDestSingleBottom() - getDestSingleTop();
    }

    public float getDestSingleHeightF() {
        return getDestSingleHeight();
    }

    public float getDestSingleAspectRatio() {
        return getDestSingleHeight() <= 0 ? 0 : getDestSingleWidth() * 1.0F / getDestSingleHeight();
    }

    // --> Piece

    public Rect getDestPieceRegion() {
        return mDestRegion == null ? null : mDestRegion.getPieceRegion();
    }

    public RectF getDestPieceRegionF() {
        return getDestPieceRegion() == null ? null : new RectF(getDestPieceRegion());
    }

    public int getDestPieceLeft() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().left;
    }

    public float getDestPieceLeftF() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().left;
    }

    public int getDestPieceTop() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().top;
    }

    public float getDestPieceTopF() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().top;
    }

    public int getDestPieceRight() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().right;
    }

    public float getDestPieceRightF() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().right;
    }

    public int getDestPieceBottom() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().bottom;
    }

    public float getDestPieceBottomF() {
        return getDestPieceRegion() == null ? 0 : getDestPieceRegion().bottom;
    }

    public int getDestPieceWidth() {
        return getDestPieceRight() - getDestPieceLeft();
    }

    public float getDestPieceWidthF() {
        return getDestPieceWidth();
    }

    public int getDestPieceHeight() {
        return getDestPieceBottom() - getDestPieceTop();
    }

    public float getDestPieceHeightF() {
        return getDestPieceHeight();
    }

    public float getDestPieceAspectRatio() {
        return getDestPieceHeight() <= 0 ? 0 : getDestPieceWidth() * 1.0F / getDestPieceHeight();
    }

    // --> Content

    public Rect getDestContentRegion() {
        return mDestRegion == null ? null : mDestRegion.getContentRegion();
    }

    public RectF getDestContentRegionF() {
        return getDestContentRegion() == null ? null : new RectF(getDestContentRegion());
    }

    public int getDestContentLeft() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().left;
    }

    public float getDestContentLeftF() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().left;
    }

    public int getDestContentTop() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().top;
    }

    public float getDestContentTopF() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().top;
    }

    public int getDestContentRight() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().right;
    }

    public float getDestContentRightF() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().right;
    }

    public int getDestContentBottom() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().bottom;
    }

    public float getDestContentBottomF() {
        return getDestContentRegion() == null ? 0 : getDestContentRegion().bottom;
    }

    public int getDestContentWidth() {
        return getDestContentRight() - getDestContentLeft();
    }

    public float getDestContentWidthF() {
        return getDestContentWidth();
    }

    public int getDestContentHeight() {
        return getDestContentBottom() - getDestContentTop();
    }

    public float getDestContentHeightF() {
        return getDestContentHeight();
    }

    public float getDestContentAspectRatio() {
        return getDestContentHeight() <= 0 ? 0
                : getDestContentWidth() * 1.0F / getDestContentHeight();
    }

    public float getDestContentCenterX() {
        return mDestRegion.getContentCenterXF();
    }

    public float getDestContentCenterY() {
        return mDestRegion.getContentCenterYF();
    }

    public boolean isDestContentContains(float x, float y) {
        return mDestRegion.isContentContains(x, y);
    }

    // -> Color

    protected int getRandomColor() {
        return getRandomColor(256);
    }

    protected int getRandomColor(int alpha) {
        return (alpha << 24) | (getRandom256() << 16) | (getRandom256() << 8) | getRandom256();
    }

    protected int getRandom256() {
        return (int) (Math.random() * 255);
    }
}
