//package com.xs.lightpuzzle.minnie.piece;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.text.TextUtils;
//
//import com.xs.lightpuzzle.minnie.PuzzleConstant;
//import com.xs.lightpuzzle.minnie.abs.StaticLayerPiece;
//import com.xs.lightpuzzle.minnie.util.GlideBitmapFactoryHelper;
//import com.xs.lightpuzzle.minnie.util.PuzzleUtils;
//
///**
// * Created by xs on 2018/11/13.
// */
//
//public class BackgroundPiece extends StaticLayerPiece{
//
//    private int mColor = PuzzleConstant.DEFAULT_BACKGROUND_COLOR;
//
//    private String mTextureUri;
//    private Integer mTextureComposite;
//    private Integer mTextureAlpha;
//
//    private Bitmap mTextureBitmap;
//
//    public BackgroundPiece(Context context,
//                           int srcSingleWidth, int srcSingleHeight,
//                           int destSingleWidth, int destSingleHeight) {
//        super(context, srcSingleWidth, srcSingleHeight,
//                destSingleWidth, destSingleHeight);
//    }
//
//    public BackgroundPiece(Context context,
//                           int srcSingleWidth, int srcSingleHeight,
//                           int destSingleWidth, int destSingleHeight,
//                           int color) {
//        super(context, srcSingleWidth, srcSingleHeight,
//                destSingleWidth, destSingleHeight);
//        mColor = color;
//    }
//
//    public BackgroundPiece(Context context,
//                           int srcSingleWidth, int srcSingleHeight,
//                           int destSingleWidth, int destSingleHeight,
//                           int color, String textureUri,
//                           Integer textureComposite, Integer textureAlpha) {
//        super(context, srcSingleWidth, srcSingleHeight,
//                destSingleWidth, destSingleHeight);
//        mColor = color;
//        mTextureUri = textureUri;
//        mTextureComposite = textureComposite;
//        mTextureAlpha = textureAlpha;
//    }
//
//    @Override
//    public void drawDirectly(Canvas canvas) {
//
//        if (!drawTexture(canvas)){
//
//        }
//    }
//
//    private boolean drawTexture(Canvas canvas) {
//        if (mTextureBitmap == null){
//            mTextureBitmap = getCompositeTextureBitmap(getTextureBitmap(mTextureUri), mColor,
//                    mTextureComposite, mTextureAlpha);
//        }
//
//    }
//
//    private Bitmap getTextureBitmap(String textureUri) {
//        if (TextUtils.isEmpty(textureUri)) {
//            return null;
//        }
//
//        int[] textureSize;
//        try {
//            textureSize = PuzzleUtils.getBitmapSize(mContext, textureUri);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        int width = textureSize[0];
//        int height = textureSize[1];
//
//        int destSingleWidth = (int) (width * getSrcSingleAspectRatio() / mScaleX);
//        int destSingleHeight = (int) (height * getSrcSingleAspectRatio() / mScaleY);
//
//        return GlideBitmapFactoryHelper.decode(mContext, textureUri, destSingleWidth, Integer.MAX_VALUE);
//    }
//
//    @Override
//    public void throwAway() {
//        PuzzleUtils.tryToRecycle(mTextureBitmap);
//        mTextureBitmap = null;
//    }
//
//
//}
