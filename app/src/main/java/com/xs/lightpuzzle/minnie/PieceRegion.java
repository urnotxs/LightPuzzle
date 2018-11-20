package com.xs.lightpuzzle.minnie;

import android.graphics.Rect;
import android.graphics.RectF;

import com.xs.lightpuzzle.yszx.RegionCreator;

/**
 * Created by xs on 2018/11/13.
 */
public class PieceRegion {

    // --- Clone and Scale

    public static PieceRegion cloneAndScale(PieceRegion src, float scale) {
        return cloneAndScale(src, scale, scale);
    }

    public static PieceRegion cloneAndScale(PieceRegion src, float scaleX, float scaleY) {
        if (src == null) {
            return null;
        }
        PieceRegion dest = clone(src);
        scale(dest, scaleX, scaleY);
        return dest;
    }

    // --- Scale

    public static void scale(PieceRegion region, float scale) {
        scale(region, scale, scale);
    }

    public static void scale(PieceRegion region, float scaleX, float scaleY) {
        if (region == null || (scaleX == 1.0F && scaleY == 1.0F)) {
            return;
        }
        RegionCreator.scale(region.singleRegion, scaleX, scaleY);
        RegionCreator.scale(region.pieceRegion, scaleX, scaleY);
        RegionCreator.scale(region.contentRegion, scaleX, scaleY);
    }

    // --- Clone

    public static PieceRegion clone(PieceRegion src) {
        if (src == null) {
            return null;
        }
        return new PieceRegion(new Rect(src.singleRegion),
                new Rect(src.pieceRegion), new Rect(src.contentRegion));
    }

    // --- Dest

    public static PieceRegion createDest(PieceRegion src, float scale) {
        return createDest(src, scale, scale);
    }

    /**
     * Piece is all fulled by Content & Only Single Platter (God! so pool)
     */
    public static PieceRegion createDest(PieceRegion src, float scaleX, float scaleY) {
        Rect destSingleRegion = RegionCreator
                .duplicateAndScaleRoundly(src.singleRegion, 1 / scaleX, 1 / scaleY);
        Rect destPieceRegion = RegionCreator
                .duplicateAndScaleRoundly(src.pieceRegion, 1 / scaleX, 1 / scaleY);
        Rect destContentRegion = RegionCreator
                .duplicateAndScaleRoundly(src.contentRegion, 1 / scaleX, 1 / scaleY);
        return create(destSingleRegion, destPieceRegion, destContentRegion);
    }

    // --- Src & Dest

    /**
     * Piece is all fulled by Content
     */
    public static PieceRegion create(Rect singleRegion, Rect contentRegion) {
        return create(singleRegion, contentRegion, contentRegion);
    }

    /**
     * Single is all fulled by Content
     */
    public static PieceRegion create(Rect contentRegion) {
        return create(contentRegion, contentRegion, contentRegion);
    }

    public static PieceRegion create(Rect singleRegion, Rect pieceRegion, Rect contentRegion) {
        return new PieceRegion(singleRegion, pieceRegion, contentRegion);
    }

    // Single -> Platter , view的总区域
    protected final Rect singleRegion;

    // Piece -> Single  单个piece的区域
    protected final Rect pieceRegion;

    // Content -> Single  单个piece的区域
    protected final Rect contentRegion; // location on single region

    public PieceRegion(Rect singleRegion, Rect pieceRegion, Rect contentRegion) {
        this.singleRegion = singleRegion;
        this.pieceRegion = pieceRegion;
        this.contentRegion = contentRegion;
    }

    // --- Getter

    public Rect getSingleRegion() {
        return singleRegion;
    }

    public Rect getPieceRegion() {
        return pieceRegion;
    }

    public Rect getContentRegion() {
        return contentRegion;
    }

    // --- Helper

    // -> Single

    public RectF getSingleRegionF() {
        return singleRegion == null ? null : new RectF(singleRegion);
    }

    public int getSingleLeft() {
        return singleRegion == null ? 0 : singleRegion.left;
    }

    public float getSingleLeftF() {
        return singleRegion == null ? 0 : singleRegion.left;
    }

    public int getSingleTop() {
        return singleRegion == null ? 0 : singleRegion.top;
    }

    public float getSingleTopF() {
        return singleRegion == null ? 0 : singleRegion.top;
    }

    public int getSingleRight() {
        return singleRegion == null ? 0 : singleRegion.right;
    }

    public float getSingleRightF() {
        return singleRegion == null ? 0 : singleRegion.right;
    }

    public int getSingleBottom() {
        return singleRegion == null ? 0 : singleRegion.bottom;
    }

    public float getSingleBottomF() {
        return singleRegion == null ? 0 : singleRegion.bottom;
    }

    public int getSingleWidth() {
        return getSingleRight() - getSingleLeft();
    }

    public float getSingleWidthF() {
        return getSingleWidth();
    }

    public int getSingleHeight() {
        return getSingleBottom() - getSingleTop();
    }

    public float getSingleHeightF() {
        return getSingleHeight();
    }

    public float getSingleAspectRatio() {
        return getSingleHeight() <= 0 ? 0 : getSingleWidth() * 1.0F / getSingleHeight();
    }

    // -> Piece

    public RectF getPieceRegionF() {
        return pieceRegion == null ? null : new RectF(pieceRegion);
    }

    public int getPieceLeft() {
        return pieceRegion == null ? 0 : pieceRegion.left;
    }

    public float getPieceLeftF() {
        return pieceRegion == null ? 0 : pieceRegion.left;
    }

    public int getPieceTop() {
        return pieceRegion == null ? 0 : pieceRegion.top;
    }

    public float getPieceTopF() {
        return pieceRegion == null ? 0 : pieceRegion.top;
    }

    public int getPieceRight() {
        return pieceRegion == null ? 0 : pieceRegion.right;
    }

    public float getPieceRightF() {
        return pieceRegion == null ? 0 : pieceRegion.right;
    }

    public int getPieceBottom() {
        return pieceRegion == null ? 0 : pieceRegion.bottom;
    }

    public float getPieceBottomF() {
        return pieceRegion == null ? 0 : pieceRegion.bottom;
    }

    public int getPieceWidth() {
        return getPieceRight() - getPieceLeft();
    }

    public float getPieceWidthF() {
        return getPieceWidth();
    }

    public int getPieceHeight() {
        return getPieceBottom() - getPieceTop();
    }

    public float getPieceHeightF() {
        return getPieceHeight();
    }

    public float getPieceAspectRatio() {
        return getPieceHeight() <= 0 ? 0 : getPieceWidth() * 1.0F / getPieceHeight();
    }

    // -> Content

    public RectF getContentRegionF() {
        return contentRegion == null ? null : new RectF(contentRegion);
    }

    public int getContentLeft() {
        return contentRegion == null ? 0 : contentRegion.left;
    }

    public float getContentLeftF() {
        return contentRegion == null ? 0 : contentRegion.left;
    }

    public int getContentTop() {
        return contentRegion == null ? 0 : contentRegion.top;
    }

    public float getContentTopF() {
        return contentRegion == null ? 0 : contentRegion.top;
    }

    public int getContentRight() {
        return contentRegion == null ? 0 : contentRegion.right;
    }

    public float getContentRightF() {
        return contentRegion == null ? 0 : contentRegion.right;
    }

    public int getContentBottom() {
        return contentRegion == null ? 0 : contentRegion.bottom;
    }

    public float getContentBottomF() {
        return contentRegion == null ? 0 : contentRegion.bottom;
    }

    public int getContentWidth() {
        return getContentRight() - getContentLeft();
    }

    public float getContentWidthF() {
        return getContentWidth();
    }

    public int getContentHeight() {
        return getContentBottom() - getContentTop();
    }

    public float getContentHeightF() {
        return getContentHeight();
    }

    public int getContentCenterX() {
        return getContentLeft() + getContentWidth() / 2;
    }

    public float getContentCenterXF() {
        return getContentCenterX();
    }

    public int getContentCenterY() {
        return getContentTop() + getContentHeight() / 2;
    }

    public float getContentCenterYF() {
        return getContentCenterY();
    }

    public boolean isContentContains(float x, float y) {
        return getContentRegionF().contains(x, y);
    }

    public float getContentAspectRatio() {
        return getContentHeight() <= 0 ? 0 : getContentWidth() * 1.0F / getContentHeight();
    }
}
