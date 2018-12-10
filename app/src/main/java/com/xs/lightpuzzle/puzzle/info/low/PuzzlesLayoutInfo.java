package com.xs.lightpuzzle.puzzle.info.low;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.DecodeImageOptions;
import com.xs.lightpuzzle.imagedecode.core.ImageScaleType;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.info.DrawView;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutArea;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutData;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutLine;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutParameter;
import com.xs.lightpuzzle.puzzle.layout.info.model.SavePieceVO;
import com.xs.lightpuzzle.puzzle.layout.info.model.SavePolygonLayoutInfo;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.piece.LayoutPiece;
import com.xs.lightpuzzle.puzzle.piece.callback.PieceAnimationCallback;
import com.xs.lightpuzzle.puzzle.piece.util.MatrixUtils;
import com.xs.lightpuzzle.puzzle.util.ShareData;
import com.xs.lightpuzzle.puzzle.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局相关
 */

public class PuzzlesLayoutInfo implements DrawView {
    String TAG = "LayoutPuzzleView";
    private final int FRAME_LINE_SIZE = Utils.getRealPixel3(6);//描边线的尺寸
    private final int HANDLEBAR_LINE_SIZE = Utils.getRealPixel3(18);//手柄线的尺寸
    private final int SELECTED_LINE_SIZE = Utils.getRealPixel3(6);//描边线的尺寸
    private final int LINE_SENSITIVITY_SIZE = Utils.getRealPixel3(30);//line的灵敏度取值
    private final int LINE_COLOR = Color.parseColor("#f5808e");//描边线的颜色
    private final int SELECTED_LINE_COLOR = Color.parseColor("#f5808e");//描边线选中时的颜色
    private final int HANDLEBAR_COLOR = Color.parseColor("#f5808e");//手柄颜色
    private final int DURATION = 300;//动画时长

    private enum ActionMode {
        NONE, DRAG, ZOOM, MOVE, SWAP;
    }//无，拖拽，缩放，移动线，切换

//    class MyHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
////            EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
////                    .PUZZLES_LAYOUT_SHOW_BAR, 0, mHandlingPiece));
//        }
//    }

    public interface PuzzleInvalidateCallBack {
        void onInvalidate();
    }

    private PieceAnimationCallback mCallback = new PieceAnimationCallback() {
        @Override
        public void onInvalidate() {
            EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_INVALIDATE_VIEW, 0));
        }
    };

    private void invalidate() {
        if (mCallback != null) {
            mCallback.onInvalidate();
        }
    }

    private boolean isAutomation = true;// 自动美化,添加的滤镜效果

    private ActionMode mCurrentMode = ActionMode.NONE;

    private SavePolygonLayoutInfo mSavePolygonLayoutInfo;// 保存Info

    private boolean isFirstTime = false;// 第一次创建时，弹出工具条

//    private MyHandler mHandler;

    // 内部操作数据
    private Context mContext;
    private Paint mFillPaint;
    private Paint mLinePaint;
    private Paint mSelectedAreaPaint;
    private Paint mHandleBarPaint;
    private boolean isTouchEnable = true;// 是否能够接收触摸事件

    private int mLastReplaceIndex;
    private LayoutPiece mHandlingLineOfPiece;// 当前把持的piece
    private LayoutPiece mHandlingPiece;// 当前把持的piece
    private LayoutPiece mReplacePiece;// 将要进行交换的Piece,长摁
    private LayoutPiece mPreviousHandlingPiece;// 第二次选中同一个piece时，取消选中边框

    private LayoutLine mHandlingLine;// 当前把持的线
    private LayoutLine mHandlingLineTwo;// 其实为同一根线
    private ArrayList<LayoutLine> mNeedChangedLines = new ArrayList<>();// 所有需要联动的线集合
    private List<LayoutPiece> mNeedChangePieces = new ArrayList<>();// 需要随着线move改动的piece集

    // 外部传入数据
    private RectF mBounds;
    private transient Rect mRect;// 外部传入的控件实际矩形框
    private RotationImg[] mRotationImgs;
    private LayoutData mLayoutData;// 数据对象
    private int mSelectedLayout = 0;
    private float mSelectedRatio = 0;
    private float mOuterPaddingRatio = 0;// 外部边距
    private float mPiecePaddingRatio = 0;// 内部间隔
    private float mPieceRadianRatio = 0;// 边框半径

    private float mWidth;// json提供的宽
    private float mHeight;// json提供的高
    private float mScalWidthRatio;// 屏幕宽与json给的宽的比例
    private float mScalHeightRatio;// 屏幕高与json给的高的比例

    private ArrayList<LayoutData.RectVO> mRectVOList = new ArrayList<>();// 给定的矩形区域

    private transient Handler mSkinHandler;
//    private transient SkinRunnable mSkinRunnable;
    public transient ArrayList<Bitmap> mSourceBitmapList = new ArrayList<>();// 在相册选中的图片数据
    private transient HashMap<Integer, Bitmap> mBeautyBitmapList = new HashMap<>();// 在相册选中的图片数据
    private transient HashMap<Integer, Bitmap> mFilterBitmapList = new HashMap<>();// 在相册选中的图片数据
    private transient HashMap<Integer, Bitmap> mFilterAlphaBitmapList = new HashMap<>();// 在相册选中的图片数据
    private transient HashMap<Integer, Bitmap> mFinalBitmapList = new HashMap<>();// 在相册选中的图片数据

    private LayoutParameter mLayoutParameter;
    private HashMap<Integer, LayoutPiece> mPiecesHashMap = new HashMap<>();
    private List<LayoutPiece> mPiecesList = new ArrayList<>();//所有area所对应的piece

    private boolean[] mReloadArr;// 记录图片是否已经decode，更换图片后重新回到编辑页时，是否需要重新在线程里面load图片

    // Touch相关变量
    private float mDownX;
    private float mDownY;
    private float mLastX;
    private float mLastY;
    private float mPreviousDistance;// 做缩放时，前后两次距离
    private PointF mMidPoint;// down时的中心点坐标
    private float mMinItemHeight;
    private float mLineMatchSize = 0;// 线段吸附取值
    private final int CLICK_RESP_SIZE = ShareData.PxToDpi_xhdpi(5);// 点击响应

    /**
     * 清理缓存
     */
    @Override
    public void recycle() {
        clearPieces();
        clearBitmaps();
        clearFilters();
    }

    /**
     * 清除Piece相关缓存
     */
    public void clearPieces() {
        mHandlingLine = null;
        mHandlingLineTwo = null;
        mHandlingPiece = null;
        mReplacePiece = null;
        mNeedChangePieces.clear();
        mNeedChangedLines.clear();
        mPiecesList.clear();
        mPiecesHashMap.clear();
    }

    /**
     * 清除图片资源
     */
    public void clearBitmaps() {
        mSourceBitmapList.clear();
    }

    /**
     * 清除滤镜图片资源
     */
    public void clearFilters() {
        mFilterAlphaBitmapList.clear();
        mFilterBitmapList.clear();

    }

    /**
     * 清除Piece的选中状态
     */
    public void clearSelectedPiece() {
        mPreviousHandlingPiece = null;
        mHandlingPiece = null;
        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                .PUZZLES_LAYOUT_SHOW_BAR, 0, null));
    }

    /**
     * 外边距交互
     */
    public void setOuterPadding(float outerPaddingRatio) {
        ArrayList<Float> lastPaddingLeftList = new ArrayList<>();
        ArrayList<Float> lastPaddingTopList = new ArrayList<>();
        ArrayList<Float> lastPaddingRightList = new ArrayList<>();
        ArrayList<Float> lastPaddingBottomList = new ArrayList<>();
        for (int i = 0; i < mLayoutParameter.getLayoutAreaList().size(); i++) {
            lastPaddingLeftList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingLeft());
            lastPaddingTopList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingTop());
            lastPaddingRightList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingRight());
            lastPaddingBottomList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingBottom());
        }
        mOuterPaddingRatio = outerPaddingRatio;
        mLayoutParameter.setOuterPaddingRatio(outerPaddingRatio);
        for (int i = 0; i < mPiecesList.size(); i++) {
            for (int j = 0; j < 5; j++) {
                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(0),
                        (mPiecesList.get(i).getArea().getPaddingLeft() - lastPaddingLeftList.get(i)) / 5.0f);

                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(1),
                        (mPiecesList.get(i).getArea().getPaddingTop() - lastPaddingTopList.get(i)) / 5.0f);

                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(2),
                        (mPiecesList.get(i).getArea().getPaddingRight() - lastPaddingRightList.get(i)) / 5.0f);

                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(3),
                        (mPiecesList.get(i).getArea().getPaddingBottom() - lastPaddingBottomList.get(i)) / 5.0f);
            }
        }
        invalidate();
    }

    /**
     * 内间距交互
     */
    public void setPiecePadding(float piecePaddingRatio) {
        ArrayList<Float> lastPaddingLeftList = new ArrayList<>();
        ArrayList<Float> lastPaddingTopList = new ArrayList<>();
        ArrayList<Float> lastPaddingRightList = new ArrayList<>();
        ArrayList<Float> lastPaddingBottomList = new ArrayList<>();
        for (int i = 0; i < mLayoutParameter.getLayoutAreaList().size(); i++) {
            lastPaddingLeftList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingLeft());
            lastPaddingTopList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingTop());
            lastPaddingRightList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingRight());
            lastPaddingBottomList.add(mLayoutParameter.getLayoutAreaList().get(i).getPaddingBottom());
        }
        mPiecePaddingRatio = piecePaddingRatio;
        mLayoutParameter.setPaddingRatio(piecePaddingRatio);
        for (int i = 0; i < mPiecesList.size(); i++) {
            for (int j = 0; j < 5; j++) {
                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(0),
                        (mPiecesList.get(i).getArea().getPaddingLeft() - lastPaddingLeftList.get(i)) / 5.0f);

                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(1),
                        (mPiecesList.get(i).getArea().getPaddingTop() - lastPaddingTopList.get(i)) / 5.0f);

                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(2),
                        (mPiecesList.get(i).getArea().getPaddingRight() - lastPaddingRightList.get(i)) / 5.0f);

                mPiecesList.get(i).updateWithLine(mPiecesList.get(i).getArea().getLines().get(3),
                        (mPiecesList.get(i).getArea().getPaddingBottom() - lastPaddingBottomList.get(i)) / 5.0f);
            }
        }
        invalidate();
    }

    /**
     * 圆角交互
     */
    public void setPieceRadianRatio(float pieceRadianRatio) {
        mPieceRadianRatio = pieceRadianRatio;
        mLayoutParameter.setRadianRatio(mPieceRadianRatio);
        for (int i = 0; i < mPiecesList.size(); i++) {
            mPiecesList.get(i).fillArea(mCallback, false);
        }
        invalidate();
    }

    /**
     * 工具条_缩小交互
     */
    public void setImageZoomOut(int id) {
        // 图片缩小
        if (mHandlingPiece != null) {
            mHandlingPiece.postZoom(0.9f);
            invalidate();
        }
    }

    /**
     * 工具条_放大交互
     */
    public void setImageZoomIn(int id) {
        // 图片放大
        if (mHandlingPiece != null) {
            mHandlingPiece.postZoom(1.1f);
            invalidate();
        }
    }

    /**
     * 工具条_旋转交互
     */
    public void setImageRotate(int id) {
        if (mHandlingPiece != null) {
            mHandlingPiece.postRotate(90);
            invalidate();
        }
    }

    public RotationImg[] getRotationImg() {
        return mRotationImgs;
    }

    public LayoutPiece getHandlingPiece() {
        return mHandlingPiece;
    }

    public RotationImg getHandlingRotationImg() {
        int handleIndex = getRotationImgIndex();
        if (mRotationImgs != null &&  handleIndex>= 0
                && mRotationImgs.length > handleIndex) {
            return mRotationImgs[handleIndex];
        }
        return null;
    }

    public int getRotationImgIndex() {
        if (mPiecesList != null && mHandlingPiece != null) {
            return mPiecesList.indexOf(mHandlingPiece);
        } else {
            return -1;
        }
    }

    public void setReloadImg(int index, boolean reloadImg) {
        mReloadArr[index] = reloadImg;
    }

    /**
     * init()时，传入用户所选图片的路径以及相关数据信息
     * initBitmap()时，将路径decode成bitmap
     */
    public void setPicPathList(RotationImg[] rotationImgs) {
        mRotationImgs = rotationImgs;
        mReloadArr = new boolean[rotationImgs.length];
    }

    /**
     * 传入画布的矩形大小
     */
    public void setRect(Rect mRect) {
        this.mRect = mRect;
        if (mRect.width() > mRect.height()) {
            mMinItemHeight = mRect.height() / 5.0f;
        } else {
            mMinItemHeight = mRect.width() / 5.0f;
        }
        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                .PUZZLES_LAYOUT_SHOW_BAR, 0, mHandlingPiece));
    }

    public Rect getRect() {
        return mRect;
    }

    public LayoutData getLayoutData() {
        return mLayoutData;
    }

    public void setData(LayoutData layoutData) {
        mLayoutData = layoutData;
    }

    @Override
    public void init() {
        init(mLayoutData);
    }

    /**
     * 调用方式：
     * 1、从模板页进入编辑页初始化调用
     * 2、从保存页进入编辑页初始化调用
     * 3、底部切换布局页不同比例切换布局后刷新编辑页时调用
     * 4、底部切换布局页调整边框弹出时，编辑页view需要缩小放大时调用
     * 5、底部切换布局页的子item的初始绘制调用
     */
    public void init(LayoutData layoutData) {
        //下面布局列表的初始化
        this.mLayoutData = layoutData;
//        mHandler = new MyHandler();
        initConfigData();
        initPaint();
    }

    /**
     * 底部切换布局页同比例切换布局后刷新编辑页时调用
     * 绘制动画效果
     */
    public void setLayoutData(LayoutData layoutData) {
        //同比例改变布局的动画初始
        LayoutData lastLayoutData = mLayoutData;
        ArrayList<LayoutPiece> lastPiecesList = new ArrayList<>();
        for (int i = 0; i < mPiecesList.size(); i++) {
            lastPiecesList.add(mPiecesList.get(i));
        }

        mLayoutData = layoutData;
        initConfigData();
        clearPieces();
        addPiecesWithFilter();

        //进行动画
        if (lastLayoutData.getRect().size() == mLayoutData.getRect().size()) {
            for (int i = 0; i < mLayoutData.getRect().size(); i++) {
                Rect lastRect = lastPiecesList.get(i).getArea().getOriginalRect();
                Rect nowRect = mPiecesList.get(i).getArea().getOriginalRect();
                mPiecesList.get(i).animateOfTransArea(mCallback, lastRect, nowRect);
            }
        }
    }

    /**
     * 初始化外部矩形框的边界
     * 初始化每个子矩形框的位置信息
     * 并且收集所有垂直或水平方向的线段对象
     */
    private void initConfigData() {
        if (mLayoutData != null) {
            mWidth = mLayoutData.getW();
            mHeight = mLayoutData.getH();
            mPiecePaddingRatio = mLayoutData.getInsidePaddingRatio();
            mOuterPaddingRatio = mLayoutData.getOutsidePaddingRatio();
            mPieceRadianRatio = mLayoutData.getRadian();
            mRectVOList = mLayoutData.getRect();
            mScalWidthRatio = mRect.width() * 1.0f / mLayoutData.getW();
            mScalHeightRatio = mRect.height() * 1.0f / mLayoutData.getH();
            mBounds = new RectF(mRect.left, mRect.top, mRect.right, mRect.bottom);

            mLayoutParameter = new LayoutParameter();
            mLayoutParameter.reset();
            mLayoutParameter.setOuterBounds(mRectVOList.size(), mBounds);
            mLayoutParameter.layout(mLayoutData, mScalWidthRatio, mScalHeightRatio);
            mLayoutParameter.setOuterPaddingRatio(mOuterPaddingRatio);
            mLayoutParameter.setPaddingRatio(mPiecePaddingRatio);
            mLayoutParameter.setRadianRatio(mPieceRadianRatio);
            mLayoutParameter.setJsonWidth(mWidth);
            mLayoutParameter.setJsonHeight(mHeight);
            mLayoutParameter.setWidth(mRect.width());
            mLayoutParameter.setHeight(mRect.height());

   /*         if (mSkinSmoothAlphaArr == null){
                mSkinSmoothAlphaArr = new int[mRectVOList.size()];
            }
            if (mSkinColorAlphaArr == null){
                mSkinColorAlphaArr = new int[mRectVOList.size()];
            }*/

            if (mBounds.width() > mBounds.height()) {
                mLineMatchSize = (26.0f / 2048) * mBounds.width();
            } else {
                mLineMatchSize = (26.0f / 2048) * mBounds.height();
            }
        }
    }

    /**
     * 初始化画笔属性
     */
    private void initPaint() {
        mBounds = new RectF();
        mMidPoint = new PointF();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(LINE_COLOR);
        mLinePaint.setStrokeWidth(FRAME_LINE_SIZE);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.SQUARE);

        mSelectedAreaPaint = new Paint();
        mSelectedAreaPaint.setAntiAlias(true);
        mSelectedAreaPaint.setStyle(Paint.Style.STROKE);
        mSelectedAreaPaint.setStrokeJoin(Paint.Join.ROUND);
        mSelectedAreaPaint.setStrokeCap(Paint.Cap.ROUND);
        mSelectedAreaPaint.setColor(SELECTED_LINE_COLOR);
        mSelectedAreaPaint.setStrokeWidth(SELECTED_LINE_SIZE);

        mHandleBarPaint = new Paint();
        mHandleBarPaint.setAntiAlias(true);
        mHandleBarPaint.setStyle(Paint.Style.FILL);
        mHandleBarPaint.setColor(HANDLEBAR_COLOR);
        mHandleBarPaint.setStrokeWidth(HANDLEBAR_LINE_SIZE);

        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(Color.RED);
    }

    /**
     * decode从相册页传入的picPath
     * 初始化布局图层的bitmap属性
     * 将选图页传入的图片路径转化成XqBitmap
     * 在线程中被调用
     */
    @Override
    public void initBitmap(Context context) {
        if (context == null) {
            return;
        }
        if (mRotationImgs == null) {
            return;
        }
        mContext = context;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        DecodeImageOptions decodeImageOptions = new DecodeImageOptions.Builder()
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .setImageScaleType(ImageScaleType.EXACTLY)
                .setDecodeingOptions(options)
                .setNativeDecode(false)
                .build();

        for (int i = 0; i < mRotationImgs.length; i++) {
            if (!mReloadArr[i]) {
                mReloadArr[i] = true;

                if (mSourceBitmapList.size() > i) {
                    mSourceBitmapList.set(i, JaneBitmapFactory.decodefile(mContext, mRotationImgs[i].getPicPath(),
                            new ImageSize(mRect.width() / 2, mRect.height() / 2), decodeImageOptions));
                } else {
                    mSourceBitmapList.add(JaneBitmapFactory.decodefile(mContext, mRotationImgs[i].getPicPath(),
                            new ImageSize(mRect.width() / 2, mRect.height() / 2), decodeImageOptions));
                }

                //旋转图片
                if (Utils.getJpgRotation(mRotationImgs[i].getPicPath()) != 0) {
                    Matrix matrix = new Matrix();
                    mSourceBitmapList.set(i, Bitmap.createBitmap(
                            mSourceBitmapList.get(i), 0, 0,
                            mSourceBitmapList.get(i).getWidth(),
                            mSourceBitmapList.get(i).getHeight(), matrix, true));
                }

                mBeautyBitmapList.put(i , generateBeautyBmp(mSourceBitmapList.get(i) , mRotationImgs[i]));
                mFilterBitmapList.put(i , generateFilterBmp(mBeautyBitmapList.get(i) , mRotationImgs[i]));
                mFilterAlphaBitmapList.put(i , generateFilterAlphaBmp(mBeautyBitmapList.get(i) , mFilterBitmapList.get(i) , mRotationImgs[i]));
                generateFinalBmp(i);
                clearCacheBitmap(i);
                //changeFilterEffect(context, i);

                if (mFinalBitmapList.containsKey(i) && BitmapHelper.isValid(mFinalBitmapList.get(i))) {
                    addPiece(i, mFinalBitmapList.get(i));
                }
            }
        }

        if (isFirstTime) {
            //弹出工具条
            isFirstTime = false;
            if (mPiecesList.size() > 0) {
                mHandlingPiece = mPiecesList.get(0);
                mPreviousHandlingPiece = mPiecesList.get(0);
            }
//            mHandler.sendEmptyMessage(0);
        }
    }

    public int getSkinSmoothAlpha(int pos) {
        if (mRotationImgs != null && mRotationImgs[pos] != null){
            if (!mRotationImgs[pos].isChangedBeauty()){
                return 35;
            }
            return mRotationImgs[pos].getSkinSmoothAlpha();
        }
        return 0;
    }

    public int getSkinColorAlpha(int pos) {
        if (mRotationImgs != null && mRotationImgs[pos] != null){
            if (!mRotationImgs[pos].isChangedBeauty()){
                return 35;
            }
            return mRotationImgs[pos].getSkinColorAlpha();
        }
        return 0;
    }

    /**
     * 美颜参数赋值
     */
    public void setBeautyParam(int pos , int skinSmoothAlpha, int skinColorAlpha) {
        if (mRotationImgs != null && mRotationImgs[pos] != null) {
            if (!mRotationImgs[pos].isChangedBeauty())
                mRotationImgs[pos].setChangedBeauty(true);
            mRotationImgs[pos].setSkinSmoothAlpha(skinSmoothAlpha);
            mRotationImgs[pos].setSkinColorAlpha(skinColorAlpha);
        }
    }

    /**
     * 编辑页美颜功能，调节bar，实时改动参数
     */
    public void changeBeautyParam(int pos , int skinSmoothAlpha, int skinColorAlpha) {

//        if (mRotationImgs != null && mRotationImgs[pos] != null) {
//
//            if (!mRotationImgs[pos].isChangedBeauty())
//                mRotationImgs[pos].setChangedBeauty(true);
//
//            mRotationImgs[pos].setSkinSmoothAlpha(skinSmoothAlpha);
//            mRotationImgs[pos].setSkinColorAlpha(skinColorAlpha);
//
//            SkinAlphaVO skinAlphaVO = new SkinAlphaVO();
//            skinAlphaVO.pos = pos;
//            skinAlphaVO.skinSmoothAlpha = skinSmoothAlpha;
//            skinAlphaVO.skinColorAlpha = skinColorAlpha;
//
//            if (mSkinRunnable != null){
//                mSkinRunnable.addSkin(skinAlphaVO);
//            }
//        }
    }

    /**
     * 弹出美颜条时，开启美颜线程
     */
    public void startSkinThread() {

        Log.e("Analysis", "启动线程");

        if (mSkinHandler == null){
            mSkinHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    switch (message.what) {      //判断标志位
                        case 1:
                            //获取数据，更新UI
                            EventBus.getDefault().post(new PuzzlesRequestMsg(
                                    PuzzlesRequestMsgName.PUZZLES_INVALIDATE_VIEW, 0));
                            break;
                    }
                    return false;
                }
            });
        }

//        if (mSkinRunnable == null){
//            mSkinRunnable = new SkinRunnable() {
//                @Override
//                public void run() {
//                    while (!isStop) {
//
//                        SkinAlphaVO skinAlphaVO = null;
//
//                        if (mSkinAlphaVOList.size() > 0) {
//                            synchronized (mSkinAlphaVOList) {
//                                skinAlphaVO = mSkinAlphaVOList.remove(0);
//                            }
//                        }
//
//                        if (skinAlphaVO == null) {
//                            continue;
//                        }
//
//                        long start = System.currentTimeMillis();
//
//                        initBeautyAndFilterBitmap(skinAlphaVO.pos , skinAlphaVO.skinSmoothAlpha, skinAlphaVO.skinColorAlpha);
//
//                        Log.e("Analysis", "时长 ： " + (System.currentTimeMillis() - start) +
//                                " ms ， SmoothAlpha : " + skinAlphaVO.skinSmoothAlpha +
//                                " , ColorAlpha : " + skinAlphaVO.skinColorAlpha);
//
//                        // 通知主线程更新UI
//                        if (mSkinHandler != null) {
//                            mSkinHandler.sendEmptyMessage(1);
//                        }
//
//                    }
//                }
//            };
//            Thread mSkinThread = new Thread(mSkinRunnable);
//            mSkinThread.start();
//        }
    }

    /**
     * 关闭美颜条时，关闭美颜线程
     */
    public void stopSkinThread() {
//        if (mSkinRunnable != null) {
//            mSkinRunnable.stop();
//            mSkinRunnable = null;
//            clearCacheBitmap(getRotationImgIndex());
//
//            Log.e("Analysis", "关闭线程");
//        }
    }

    private void initBeautyAndFilterBitmap(int pos, int skinSmoothAlpha, int skinColorAlpha) {

        mRotationImgs[pos].setSkinSmoothAlpha(skinSmoothAlpha);
        mRotationImgs[pos].setSkinColorAlpha(skinColorAlpha);

        // 调节美颜进度条时调用
        mBeautyBitmapList.put(pos , generateBeautyBmp(mSourceBitmapList.get(pos), mRotationImgs[pos]));
        mFilterBitmapList.put(pos , generateFilterBmp(mBeautyBitmapList.get(pos) , mRotationImgs[pos]));
        mFilterAlphaBitmapList.put(pos ,
                generateFilterAlphaBmp(mBeautyBitmapList.get(pos) ,
                        mFilterBitmapList.get(pos) , mRotationImgs[pos]));

        generateFinal(pos);
        /*if (mFinalBitmapList.get(pos) != null) {
            synchronized (mFinalBitmapList.get(pos)) {
            }
        }*/
    }

    /**
     * 生成美颜效果图
     * 至少与原图一致
     */
    public Bitmap generateBeautyBmp(Bitmap bitmap , RotationImg rotationImg) {
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
    private Bitmap generateFilterBmp(Bitmap bitmap , RotationImg rotationImg) {
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
//        if (isAutomation) {
//
//        }
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
//            lookupBitmap = null;
//            if (maskBitmaps != null) {
//                for (Bitmap maskBitmap : maskBitmaps) {
//                    if (maskBitmap != null && !maskBitmap.isRecycled()) {
//                        maskBitmap = null;
//                    }
//                }
//            }
//        }
        return filterBitmap;
    }
    /**
     *  修改滤镜
     * */
    public void changeFilterEffect(int pos) {
        if (mBeautyBitmapList.containsKey(pos)){
            mFilterBitmapList.put(pos , generateFilterBmp(mBeautyBitmapList.get(pos) , mRotationImgs[pos])) ;
            if (mFilterBitmapList.containsKey(pos)){
                mFilterAlphaBitmapList.put(pos , generateFilterAlphaBmp(mBeautyBitmapList.get(pos) , mFilterBitmapList.get(pos) , mRotationImgs[pos])) ;
            }
        }
    }

    public void changeBeautyEffect(int pos){
        mBeautyBitmapList.put(pos , generateBeautyBmp(mSourceBitmapList.get(pos), mRotationImgs[pos]));
        mFilterBitmapList.put(pos , generateFilterBmp(mBeautyBitmapList.get(pos) ,mRotationImgs[pos]));
        mFilterAlphaBitmapList.put(pos , generateFilterAlphaBmp(mBeautyBitmapList.get(pos) , mFilterBitmapList.get(pos) , mRotationImgs[pos]));
    }

    public void changeFilterAlphaEffect(int pos) {
        if (mBeautyBitmapList.containsKey(pos) && mFilterBitmapList.containsKey(pos)){
            mFilterAlphaBitmapList.put(pos , generateFilterAlphaBmp(mBeautyBitmapList.get(pos) , mFilterBitmapList.get(pos) , mRotationImgs[pos])) ;
        }
    }

    /**
     * 弹出底部滤镜栏时，为当前imageInfo创建美颜和滤镜效果图，提升改变alpha时的效率
     */
    public void openFilterBar(int pos) {
        mBeautyBitmapList.put(pos , generateBeautyBmp(mSourceBitmapList.get(pos), mRotationImgs[pos]));
        mFilterBitmapList.put(pos , generateFilterBmp(mBeautyBitmapList.get(pos) , mRotationImgs[pos]));
    }

    /**
     * 滤镜条关闭时，清空美颜bmp和滤镜bmp和透明度bmp
     */
    public void closeFilterBar() {
        for (int i = 0 ; i < mSourceBitmapList.size() ; i++){
            clearCacheBitmap(i);
        }
    }

    /**
     * 生成带透明度的滤镜效果图
     */
    public Bitmap generateFilterAlphaBmp(Bitmap bitmap, Bitmap filterBitmap , RotationImg rotationImg) {
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

    public void clearCacheBitmap(int pos) {
        mBeautyBitmapList.put(pos , null);
        mFilterBitmapList.put(pos , null);
        mFilterAlphaBitmapList.put(pos , null);
    }

    /**
     * 生成最终效果图
     */
    public void generateFinalBmp(int pos) {
        Bitmap finalBitmap = null ;
        if (pos < mSourceBitmapList.size() && BitmapHelper.isValid(mSourceBitmapList.get(pos))) {
            finalBitmap = mSourceBitmapList.get(pos);
        }
//        if (mBeautyBitmapList.containsKey(pos) && BitmapHelper.isValid(mBeautyBitmapList.get(pos))) {
//            finalBitmap = mBeautyBitmapList.get(pos).copy(Bitmap.Config.ARGB_8888, true);
//        }
//        if (mFilterBitmapList.containsKey(pos) && BitmapHelper.isValid(mFilterBitmapList.get(pos))) {
//            finalBitmap = mFilterBitmapList.get(pos).copy(Bitmap.Config.ARGB_8888, true);
//        }
//        if (mFilterAlphaBitmapList.containsKey(pos) && BitmapHelper.isValid(mFilterAlphaBitmapList.get(pos))) {
//            finalBitmap = mFilterAlphaBitmapList.get(pos).copy(Bitmap.Config.ARGB_8888, true);
//            mFilterAlphaBitmapList.put(pos , null);
//        }
        mFinalBitmapList.put(pos , finalBitmap);
    }

    public void generateFinal(int pos) {
        generateFinalBmp(pos);
        if (mFinalBitmapList.containsKey(pos) && mFinalBitmapList.get(pos) != null) {
            BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), mFinalBitmapList.get(pos));
            drawable.setAntiAlias(true);
            drawable.setFilterBitmap(true);
            mPiecesList.get(pos).setDrawable(drawable);
        }
    }

    public ArrayList<Bitmap> getFinalBmpArray() {

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < mFinalBitmapList.size(); i++) {
            //generateFinalBmp(i);
            bitmaps.add(mFinalBitmapList.get(i));
        }
        return bitmaps;
    }

    /**
     * 绘制布局图层
     */
    @Override
    public void draw(Canvas canvas) {
        if (mLayoutParameter.getOuterArea() == null) {
            return;
        }

        for (int i = 0; i < mLayoutParameter.getAreaCount(); i++) {
            if (i >= mPiecesList.size()) {
                break;
            }
            LayoutPiece piece = mPiecesList.get(i);
            if (piece == mHandlingPiece && mCurrentMode == ActionMode.SWAP) {
                continue;
            }
            if (mPiecesList.size() > i) {
                piece.draw(canvas);
            }
        }
        if (mHandlingPiece != null && mCurrentMode != ActionMode.SWAP) {
            drawSelectedArea(canvas, mHandlingPiece);
        }
    }

    /**
     *  绘制选中区域的边框
     */
    private void drawSelectedArea(Canvas canvas, LayoutPiece piece) {
        final LayoutArea area = piece.getArea();
        RectF rectF = area.getAreaRect();
        float offset;
        if (SELECTED_LINE_SIZE % 2 == 0) {
            offset = SELECTED_LINE_SIZE / 2.0f - 1;
        } else {
            offset = (SELECTED_LINE_SIZE - 1.0f) / 2.0f - 1;
        }
        rectF = new RectF(rectF.left + offset, rectF.top + offset,
                rectF.right - offset, rectF.bottom - offset);
        canvas.drawRoundRect(rectF, area.getRadian(), area.getRadian(), mSelectedAreaPaint);

        // draw handle bar
        for (LayoutLine line : area.getLines()) {
            if (mLayoutParameter.getLineList().contains(line)) {
                PointF[] handleBarPoints = area.getHandleBarPoints(line, offset);
                canvas.drawLine(handleBarPoints[0].x, handleBarPoints[0].y, handleBarPoints[1].x,
                        handleBarPoints[1].y, mHandleBarPaint);
                canvas.drawCircle(handleBarPoints[0].x, handleBarPoints[0].y, HANDLEBAR_LINE_SIZE / 2,
                        mHandleBarPaint);
                canvas.drawCircle(handleBarPoints[1].x, handleBarPoints[1].y, HANDLEBAR_LINE_SIZE / 2,
                        mHandleBarPaint);
            }
        }
    }

    /**
     * 事件处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchEnable) {
            return false;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mLastX = event.getX();
                mLastY = event.getY();

                decideActionMode(event);
                prepareAction(event);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mPreviousDistance = calculateDistance(event);
                calculateMidPoint(event, mMidPoint);

                decideActionMode(event);
                break;

            case MotionEvent.ACTION_MOVE:
                performAction(event);
                if ((Math.abs(event.getX() - mDownX) > 10 || Math.abs(event.getY() - mDownY) > 10)
                        && mCurrentMode != ActionMode.SWAP) {
                    //mHandler.removeCallbacks(mSwitchToSwapAction);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                boolean result = finishAction(event);
                mCurrentMode = ActionMode.NONE;
                //mHandler.removeCallbacks(mSwitchToSwapAction);
                invalidate();
                if (result){
                    return true;
                }
                break;
        }

        if (mCurrentMode != ActionMode.NONE) {
            invalidate();
            return true;
        }
        if (mCurrentMode != ActionMode.NONE) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * 决定当前event会触发哪种事件，none、drag、zoom、moveline、swap
     * */
    private void decideActionMode(MotionEvent event) {
        for (LayoutPiece piece : mPiecesList) {
            if (piece.isAnimateRunning()) {
                mCurrentMode = ActionMode.NONE;
                return;
            }
        }
        if (event.getPointerCount() == 1) {
            //落点在区域外
            if (mDownX < mLayoutParameter.getOuterArea().left() || mDownX > mLayoutParameter.getOuterArea().right() || mDownY < mLayoutParameter.getOuterArea().top() || mDownY > mLayoutParameter.getOuterArea().bottom()) {
                return;
            }

            mHandlingLine = findHandlingLine();
            if (mHandlingLine != null) {
                //当前触发的是line的移动
                mCurrentMode = ActionMode.MOVE;
                mHandlingLineOfPiece = findHandlingPiece();
            } else {
                //当前触发的是piece的移动
                mHandlingPiece = findHandlingPiece();

                if (mHandlingPiece != null) {
                    mCurrentMode = ActionMode.DRAG;
                    //计时器触发长摁事件
                    //mHandler.postDelayed(mSwitchToSwapAction, 500);
                }
            }
        } else if (event.getPointerCount() > 1) {
            //两个手指，触发缩放的条件
            if (mHandlingPiece != null
                    && mHandlingPiece.contains(event.getX(1), event.getY(1))
                    && mCurrentMode == ActionMode.DRAG) {
                mCurrentMode = ActionMode.ZOOM;
            }
        }
    }

    /**
     * 执行Action前的准备工作
     * */
    @SuppressWarnings("unused")
    private void prepareAction(MotionEvent event) {
        switch (mCurrentMode) {
            case NONE:
                break;
            case DRAG:
                mHandlingPiece.record();
                mLastReplaceIndex = -1;
                break;
            case ZOOM:
                mHandlingPiece.record();
                break;
            case MOVE:
                mNeedChangedLines.addAll(findNeedChangedLines(mHandlingLine, mHandlingLineTwo));
                mNeedChangePieces.clear();
                for (int i = 0; i < mNeedChangedLines.size(); i++) {
                    mNeedChangedLines.get(i).prepareMove();
                    mNeedChangePieces.add(mPiecesHashMap.get(mNeedChangedLines.get(i).getParentId()));
                }
                break;
        }
    }

    /**
     * 执行Action
     * */
    private void performAction(MotionEvent event) {
        switch (mCurrentMode) {
            case NONE:
                break;
            case DRAG:
                dragPiece(mHandlingPiece, event);
                mReplacePiece = findReplacePiece(event);
                //图片缩小
                if (mReplacePiece != null) {
                    if (mLastReplaceIndex >= 0) {
                        mPiecesList.get(mLastReplaceIndex).totalZoom(1f);
                    }
                    mReplacePiece.totalZoom(0.9f);
                    mLastReplaceIndex = mPiecesList.indexOf(mReplacePiece);
                } else {
                    if (mLastReplaceIndex >= 0) {
                        mPiecesList.get(mLastReplaceIndex).totalZoom(1f);
                        mLastReplaceIndex = -1;
                    }
                }

                break;
            case ZOOM:
                zoomPiece(mHandlingPiece, event);
                break;
            case SWAP:
                /*dragPiece(mHandlingPiece, event);
                mReplacePiece = findReplacePiece(event);*/
                break;
            case MOVE:
                moveLine(mHandlingLine, event);
                EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                        .PUZZLES_LAYOUT_SHOW_BAR, event.getAction(), null));
                break;
        }
    }


    /**
     * 结束Action
     */
    private boolean finishAction(MotionEvent event) {
        boolean isChangeFilterBarIndex = false;
        switch (mCurrentMode) {
            case NONE:
                break;
            case DRAG:
                if (mHandlingPiece != null && !mHandlingPiece.isFilledArea()) {
                    mHandlingPiece.moveToFillArea(mCallback);
                }

                boolean isClick = false;
                if (Math.abs(mDownX - event.getX()) < CLICK_RESP_SIZE
                        && Math.abs(mDownY - event.getY()) < CLICK_RESP_SIZE) {
                    isClick = true;
                }
                if (mPreviousHandlingPiece == mHandlingPiece && isClick) {
                    mHandlingPiece = null;
                }

                mPreviousHandlingPiece = mHandlingPiece;
                if (mHandlingPiece != null && mReplacePiece != null) {
                    swapPiece(mHandlingPiece, mReplacePiece);
                    mHandlingPiece.swapFillArea(mCallback, true);
                    mReplacePiece.swapFillArea(mCallback, true);

                    mReplacePiece.totalZoom(1f);
                    mLastReplaceIndex = -1;
                    mReplacePiece = null;
                }
                if (isClick) {
//                    if (PuzzlesInfoHelper.getInstance().isPicFilterEdit()
//                            || PuzzlesInfoHelper.getInstance().isPicBeautyEdit()){
//                        isChangeFilterBarIndex = true;
//                    }
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                            .PUZZLES_LAYOUT_SHOW_BAR, event.getAction(), mHandlingPiece));
                } else {
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                            .PUZZLES_LAYOUT_SHOW_BAR, event.getAction(), null));
                }
                break;
            case ZOOM:
                if (mHandlingPiece != null && !mHandlingPiece.isFilledArea()) {
                    if (mHandlingPiece.canFilledArea()) {
                        mHandlingPiece.moveToFillArea(mCallback);
                    } else {
                        mHandlingPiece.fillArea(mCallback, false);
                    }
                }
                mPreviousHandlingPiece = mHandlingPiece;
                break;
            case MOVE:
                if (mHandlingLineOfPiece != null && Math.abs(mDownX - event.getX()) < 3
                        && Math.abs(mDownY - event.getY()) < 3) {
                    mHandlingPiece = mHandlingLineOfPiece;
                    if (mHandlingPiece == mPreviousHandlingPiece) {
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                                .PUZZLES_LAYOUT_SHOW_BAR, event.getAction(), null));
                        mHandlingPiece = null;
                        mPreviousHandlingPiece = null;
                    } else {
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                                .PUZZLES_LAYOUT_SHOW_BAR, event.getAction(), mHandlingPiece));
                        mPreviousHandlingPiece = mHandlingPiece;
                    }
                } else {
                    mHandlingLineOfPiece = null;
                }
                if (mHandlingLine != null) {
                    releaseLine(mHandlingLine, event);
                }
                break;
            case SWAP:
                /*if (mHandlingPiece != null && mReplacePiece != null) {
                    Drawable temp = mHandlingPiece.getDrawable();

                    mHandlingPiece.setDrawable(mReplacePiece.getDrawable());
                    mReplacePiece.setDrawable(temp);

                    mHandlingPiece.swapFillArea(mCallback, true);
                    mReplacePiece.swapFillArea(mCallback, true);

                    mHandlingPiece = null;
                    mReplacePiece = null;
                    mPreviousHandlingPiece = null;
                }*/
                break;
        }

        mHandlingLine = null;
        mNeedChangedLines.clear();
        return isChangeFilterBarIndex;
    }

    /**
     *  置换piece
     * */
    private void swapPiece(LayoutPiece handlingPiece, LayoutPiece replacePiece) {
        int replacedIndex = mPiecesList.indexOf(replacePiece);
        int handleIndex = mPiecesList.indexOf(handlingPiece);

        //置换图片资源
        RotationImg rotationImg = mRotationImgs[replacedIndex];
        mRotationImgs[replacedIndex] = mRotationImgs[handleIndex];
        mRotationImgs[handleIndex] = rotationImg;

        //置换滤镜和图片
        Bitmap bitmap = mSourceBitmapList.get(replacedIndex);
        mSourceBitmapList.set(replacedIndex, mSourceBitmapList.get(handleIndex));
        mSourceBitmapList.set(handleIndex, bitmap);
        Bitmap finalBitmap = mFinalBitmapList.get(replacedIndex);
        mFinalBitmapList.put(replacedIndex, mFinalBitmapList.get(handleIndex));
        mFinalBitmapList.put(handleIndex, finalBitmap);

        // 置换美颜参数
/*        int smoothAlpha = mSkinSmoothAlphaArr[replacedIndex];
        mSkinSmoothAlphaArr[replacedIndex] = mSkinSmoothAlphaArr[handleIndex];
        mSkinSmoothAlphaArr[handleIndex] = smoothAlpha;

        int colorAlpha = mSkinColorAlphaArr[replacedIndex];
        mSkinColorAlphaArr[replacedIndex] = mSkinColorAlphaArr[handleIndex];
        mSkinColorAlphaArr[handleIndex] = colorAlpha;*/

        /*Bitmap effectBitmap = mFilterBitmapList.get(replacedIndex);
        mFilterBitmapList.put(replacedIndex, mFilterBitmapList.get(handleIndex));
        mFilterBitmapList.put(handleIndex, effectBitmap);
        Bitmap drawBitmap = mFilterAlphaBitmapList.get(replacedIndex);
        mFilterAlphaBitmapList.put(replacedIndex, mFilterAlphaBitmapList.get(handleIndex));
        mFilterAlphaBitmapList.put(handleIndex, drawBitmap);*/
 /*       TepFilterInfo tepFilterInfo = mTepFilterInfos.get(replacedIndex);
        mTepFilterInfos.set(replacedIndex , mTepFilterInfos.get(handleIndex));
        mTepFilterInfos.set(handleIndex , tepFilterInfo);*/


        Drawable temp = handlingPiece.getDrawable();
        handlingPiece.setDrawable(replacePiece.getDrawable());
        replacePiece.setDrawable(temp);

        //置换角度
        float degree = handlingPiece.getMatrixAngle();
        float replaceDegree = replacePiece.getMatrixAngle();
        handlingPiece.set(MatrixUtils.generateMatrix(handlingPiece,handlingPiece.getArea().getAreaRect(),  0f));
        handlingPiece.postRotate(replaceDegree);
        replacePiece.set(MatrixUtils.generateMatrix(replacePiece, replacePiece.getArea().getAreaRect(), 0f));
        replacePiece.postRotate(degree);

    }

    /**
     *  释放line
     * */
    private void releaseLine(LayoutLine line, MotionEvent event) {
        if (line.getDirection() == LayoutLine.Direction.HORIZONTAL) {
            mLayoutParameter.generateXYAxisList(mHandlingLine);
        } else {
            mLayoutParameter.generateXYAxisList(mHandlingLine);
        }
        if (mNeedChangedLines != null && mNeedChangePieces != null) {
            for (int i = 0; i < mNeedChangedLines.size(); i++) {
                float offset = mNeedChangedLines.get(i).releaseMove(mLayoutParameter.getAxisList(), mLineMatchSize);
                if (mNeedChangePieces.get(i) != null) {
                    mNeedChangePieces.get(i).updateWithLine(mNeedChangedLines.get(i), offset);
                }
            }
        }
    }

    /**
     *  缩放piece
     * */
    private void zoomPiece(LayoutPiece piece, MotionEvent event) {
        if (piece == null || event == null || event.getPointerCount() < 2) return;
        float scale = calculateDistance(event) / mPreviousDistance;
        piece.zoomAndTranslate(scale, scale, mMidPoint, event.getX() - mDownX, event.getY() - mDownY);
    }

    /**
     *  拖拽piece
     * */
    private void dragPiece(LayoutPiece piece, MotionEvent event) {
        if (piece == null || event == null) return;
        piece.translate(event.getX() - mDownX, event.getY() - mDownY);
    }

    /**
     *  移动line，对piece做缩放和平移
     * */
    private void moveLine(LayoutLine line, MotionEvent event) {
        if (line == null || event == null) return;
        float offset;
        if (line.getDirection() == LayoutLine.Direction.HORIZONTAL) {
            offset = event.getY() - mLastY;
        } else {
            offset = event.getX() - mLastX;
        }
        mLastX = event.getX();
        mLastY = event.getY();
        for (int i = 0; i < mNeedChangedLines.size(); i++) {
            LayoutArea area = mLayoutParameter.getAreaHashMap().get(mNeedChangedLines.get(i).getParentId());
            if (line.getDirection() == LayoutLine.Direction.HORIZONTAL) {
                if (mNeedChangedLines.get(i).getTowards() == LayoutLine.Towards.TOP && area.getOriginalRect().height() - offset < mMinItemHeight) {
                    offset = area.getOriginalRect().height() - mMinItemHeight;
                }
                if (mNeedChangedLines.get(i).getTowards() == LayoutLine.Towards.BOTTOM && area.getOriginalRect().height() + offset < mMinItemHeight) {
                    offset = mMinItemHeight - area.getOriginalRect().height();
                }
            } else {
                if (mNeedChangedLines.get(i).getTowards() == LayoutLine.Towards.LEFT && area.getOriginalRect().width() - offset < mMinItemHeight) {
                    offset = area.getOriginalRect().width() - mMinItemHeight;
                }
                if (mNeedChangedLines.get(i).getTowards() == LayoutLine.Towards.RIGHT && area.getOriginalRect().width() + offset < mMinItemHeight) {
                    offset = mMinItemHeight - area.getOriginalRect().width();
                }
            }
        }

        if (offset == 0) {
            return;
        }
        if (mNeedChangedLines != null && mNeedChangePieces != null) {
            for (int i = 0; i < mNeedChangedLines.size(); i++) {
                mNeedChangedLines.get(i).move(offset);
                if (mNeedChangePieces.get(i) != null) {
                    mNeedChangePieces.get(i).updateWithLine(mNeedChangedLines.get(i), offset);
                }
            }
        }
    }

    /**
     *  在所有线段中查找当前down位置坐标所在的line
     * */
    private LayoutLine findHandlingLine() {
        ArrayList<LayoutLine> lines = new ArrayList<>();
        ArrayList<LayoutLine> mHandlingLineList = new ArrayList<>();
        for (LayoutLine line : mLayoutParameter.getLineList()) {
            if (line.contains(mDownX, mDownY, mPiecePaddingRatio * mLayoutParameter.getTotalPadding() + LINE_SENSITIVITY_SIZE)) {
                lines.add(line);
            }
        }

        if (lines.size() >= 2) {
            //当down在公共区域时做区分处理
            //找到了四根线，两根水平两根垂直
            mHandlingLineList.add(lines.get(0));
            //保险一点，再做一次方向排查
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).getDirection().equals(lines.get(0).getDirection())) {
                    mHandlingLineList.add(lines.get(i));
                }
            }
            for (int i = 1; i < lines.size(); i++) {
                if (!mHandlingLineList.contains(lines.get(i))) {
                    mHandlingLineList.add(lines.get(i));
                }
            }
            mHandlingLineTwo = mHandlingLineList.get(1);
            return mHandlingLineList.get(0);

        } else {
            return null;
        }
    }

    /**
     *  寻找所有需要移动的line
     */
    private List<LayoutLine> findNeedChangedLines(LayoutLine handlingLine, LayoutLine handlingLineTwo) {
        if (handlingLine == null) return new ArrayList<>();

        mLayoutParameter.generateOneLineList(handlingLine);

        if (mHandlingPiece != null && (mHandlingPiece.contains(handlingLine) || mHandlingPiece.contains(handlingLineTwo))) {
            //有选中子Item，则只改变影响到的矩形框
            Find<LayoutLine> mClass = new Find<>();
            mClass.find(handlingLine);
            return mClass.saves;
        } else {
            //无选中Item，则改变整条直线上的矩形框
            return mLayoutParameter.getOneLineList();
        }
    }

    /**
     *  拖拽、长摁或者缩放piece时，需要找到down位置所在的piece
     */
    private LayoutPiece findHandlingPiece() {
        for (LayoutPiece piece : mPiecesList) {
            if (piece.contains(mDownX, mDownY)) {
                return piece;
            }
        }
        return null;
    }

    /**
     *  长摁时需置换piece，根据手指位置查找置换的piece
     */
    private LayoutPiece findReplacePiece(MotionEvent event) {
        for (LayoutPiece piece : mPiecesList) {
            if (piece != mHandlingPiece && piece.contains(event.getX(), event.getY())) {
                return piece;
            }
        }
        return null;
    }

    /**
     *  计算事件的两个手指之间的距离
     * */
    private float calculateDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     *  计算事件的两个手指的中心坐标
     * */
    private void calculateMidPoint(MotionEvent event, PointF point) {
        point.x = (event.getX(0) + event.getX(1)) / 2;
        point.y = (event.getY(0) + event.getY(1)) / 2;
    }

    public void addPieces(Context context, List<Bitmap> bitmaps, ArrayList<LayoutData.DrawableVO> drawableRectFs, ArrayList<Boolean> zoomArray) {
        mContext = context;
        for (int i = 0; i < bitmaps.size(); i++) {
            BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), bitmaps.get(i));
            drawable.setAntiAlias(true);
            drawable.setFilterBitmap(true);
            final LayoutArea area = mLayoutParameter.getLayoutAreaList().get(i);

            LayoutPiece piece = new LayoutPiece(drawable, area, new Matrix(), i);

            final Matrix matrix = MatrixUtils.generateMatrix( piece , area.getAreaRect(),0f);
            piece.set(matrix);
            piece.setAnimateDuration(DURATION);
            RectF rectF = new RectF();
            rectF.left = mScalWidthRatio * drawableRectFs.get(i).getDrawableRect().left;
            rectF.right = mScalWidthRatio * drawableRectFs.get(i).getDrawableRect().right;
            rectF.top = mScalHeightRatio * drawableRectFs.get(i).getDrawableRect().top;
            rectF.bottom = mScalHeightRatio * drawableRectFs.get(i).getDrawableRect().bottom;
            //填补误差
            if (rectF.left > area.left()) {
                rectF.left = area.left();
            }
            if (rectF.top > area.top()) {
                rectF.top = area.top();
            }
            if (rectF.right < area.right()) {
                rectF.right = area.right();
            }
            if (rectF.bottom < area.bottom()) {
                rectF.bottom = area.bottom();
            }
            piece.setCurrentDrawableBounds(rectF, drawableRectFs.get(i).getDegree());
            piece.setZoom(zoomArray.get(i));
            mPiecesHashMap.put(i, piece);
            mPiecesList.add(piece);
        }
    }

    public void addPieces(Context context, ArrayList<Bitmap> bitmaps) {
        mContext = context;
        mSourceBitmapList = bitmaps;
        for (int i = 0; i < bitmaps.size(); i++) {
            addPiece(i, bitmaps.get(i));
        }
    }

    public void addPiecesWithFilter() {
        for (int i = 0; i < mFinalBitmapList.size(); i++) {
            if (mFinalBitmapList.get(i) != null) {
                addPiece(i, mFinalBitmapList.get(i));
            }
        }
    }

    public void addPiece(int position, Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setFilterBitmap(true);

        if (position >= mLayoutParameter.getAreaCount()) {
            Log.e(TAG, "addPiece: can not add more. the current puzzle layout can contains "
                    + mLayoutParameter.getAreaCount()
                    + " puzzle piece.");
            return;
        }

        if (mPiecesList.size() > position) {
            //换图片
            mPiecesList.get(position).setDrawable(drawable);
            float degree = mPiecesList.get(position).getMatrixAngle();
            mPiecesList.get(position).set(MatrixUtils.generateMatrix(mPiecesList.get(position), mPiecesList.get(position).getArea().getAreaRect(), 0f));
            mPiecesList.get(position).postRotate(degree);
        } else {
            final LayoutArea area = mLayoutParameter.getLayoutAreaList().get(position);

            LayoutPiece piece = new LayoutPiece(drawable, area, new Matrix(), position);

            final Matrix matrix = MatrixUtils.generateMatrix(piece, area.getAreaRect(), 0f);
            piece.set(matrix);
            piece.setAnimateDuration(DURATION);

            mPiecesHashMap.put(position, piece);
            mPiecesList.add(piece);
        }
    }

    /**
     * 递归算法
     * 根据当前line来查找与之联动的所有line
     * 从在同一直线（==x or ==y）中筛选相互牵制的
     * 两条线段有交集(不包括共端点的情况)则表示相互牵制
     * 即较短线段的一个端点落在较长线段上得知
     */
    public class Find<T> {
        private List<T> saves = new ArrayList<T>();

        public void find(T o) {
            // 递归终止条件
            if (saves.contains(o)) {
                return;
            } else {
                // 找到
                saves.add(o);
            }

            List<T> finds = contain(o);
            if (finds != null) {
                for (T t : finds) {
                    find(t);
                }
            }
        }

        public List<T> contain(T o) {
            List<T> finds = new ArrayList<>();

            LayoutLine mLine = (LayoutLine) o;
            float mLength = mLine.length();
            for (int i = 0; i < mLayoutParameter.getOneLineList().size(); i++) {
                LayoutLine line = mLayoutParameter.getOneLineList().get(i);
                if (line == mLine || saves.contains(line)) {
                    //排除自身和已经包含在saves里的线段，减小循环量
                    continue;
                }
                if (line.getStartPoint().x == mLine.getStartPoint().x && line.getStartPoint().y == mLine.getStartPoint().y && line.getEndPoint().x == mLine.getEndPoint().x && line.getEndPoint().y == mLine.getEndPoint().y) {
                    //完全重合的两条线段，add
                    finds.add((T) line);
                    continue;
                }
                LayoutLine line1;
                LayoutLine line2;
                if (mLength >= line.length()) {
                    line1 = line;//短线段
                    line2 = mLine;//长线段
                } else {
                    line1 = mLine;
                    line2 = line;
                }

                if (mLine.getDirection() == LayoutLine.Direction.HORIZONTAL) {
                    //x不同
                    if (isOnLine(line1.getStartPoint().x, line1.getEndPoint().x, line2.getStartPoint().x, line2.getEndPoint().x)) {
                        finds.add((T) line);
                    }
                } else {
                    //y不同
                    if (isOnLine(line1.getStartPoint().y, line1.getEndPoint().y, line2.getStartPoint().y, line2.getEndPoint().y)) {
                        finds.add((T) line);
                    }
                }
            }
            return finds;
        }

        private boolean isOnLine(float shortStart, float shortEnd, float longStart, float longEnd) {
            if (shortStart > longStart && shortStart < longEnd || shortEnd > longStart && shortEnd < longEnd) {
                //若短线段起点或者末点落在长线段上，则表示两个线段有交集，会相互牵制引发联动
                return true;
            } else {
                return false;
            }
        }
    }

    public float getOuterPaddingRatio() {
        return mOuterPaddingRatio;
    }

    public float getPiecePaddingRatio() {
        return mPiecePaddingRatio;
    }

    public float getPieceRadianRatio() {
        return mPieceRadianRatio;
    }

    public void setSelectedRatio(float ratio) {
        mSelectedRatio = ratio;
    }

    public void setSelectedLayout(int layout) {
        mSelectedLayout = layout;
    }

    public float getSelectedRatio() {
        return mSelectedRatio;
    }

    public int getSelectedLayout() {
        return mSelectedLayout;
    }

    public boolean isAutomation() {
        return isAutomation;
    }

    public void setAutomation(boolean automation) {
        isAutomation = automation;
    }

    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public ArrayList<Bitmap> getBmpList() {
        return mSourceBitmapList;
    }

    /**
     * 当前View所处状态的保存相关
     */
    public void setPieceMatrix(LayoutData layoutData, ArrayList<Matrix> matrixArray, ArrayList<Boolean> zoomArray) {
        ArrayList<LayoutData.DrawableVO> drawableRectFs = layoutData.getDrawableVOS();

        for (int i = 0; i < mPiecesList.size(); i++) {
            LayoutArea area = mLayoutParameter.getLayoutAreaList().get(i);
            RectF rectF = new RectF();
            rectF.left = mScalWidthRatio * drawableRectFs.get(i).getDrawableRect().left;
            rectF.right = mScalWidthRatio * drawableRectFs.get(i).getDrawableRect().right;
            rectF.top = mScalHeightRatio * drawableRectFs.get(i).getDrawableRect().top;
            rectF.bottom = mScalHeightRatio * drawableRectFs.get(i).getDrawableRect().bottom;
            //填补误差
            if (rectF.left > area.left()) {
                rectF.left = area.left();
            }
            if (rectF.top > area.top()) {
                rectF.top = area.top();
            }
            if (rectF.right < area.right()) {
                rectF.right = area.right();
            }
            if (rectF.bottom < area.bottom()) {
                rectF.bottom = area.bottom();
            }
            mPiecesList.get(i).setCurrentDrawableBounds(rectF, drawableRectFs.get(i).getDegree());
            mPiecesList.get(i).setZoom(zoomArray.get(i));
        }
    }

    public ArrayList<Matrix> getCurMatrixArray() {
        ArrayList<Matrix> matrixArrayList = new ArrayList<>();
        for (LayoutPiece piece : mPiecesList) {
            matrixArrayList.add(piece.getMatrix());
        }
        return matrixArrayList;
    }

    public ArrayList<Boolean> getCurZoomArray() {
        ArrayList<Boolean> zoomStatusArr = new ArrayList<>();
        for (LayoutPiece piece : mPiecesList) {
            zoomStatusArr.add(piece.isZoom());
        }
        return zoomStatusArr;
    }

    public LayoutData getCurLayoutData() {
        if (mLayoutParameter == null) {
            return null;
        } else {
            return mLayoutParameter.switchAreaToJson(mPiecesList);
        }
    }

    public SavePolygonLayoutInfo getSavePolygonLayoutInfo() {
        return mSavePolygonLayoutInfo;
    }

    public void generateSavePolygonLayoutInfo() {
        ArrayList<RectF> rectList = new ArrayList<>();
        ArrayList<SavePieceVO> pieceList = new ArrayList<>();

        LayoutData layoutData = getCurLayoutData();
        for (int i = 0; i < layoutData.getRect().size(); i++) {

            LayoutData.RectVO rectVO = layoutData.getRect().get(i);
            rectList.add(new RectF(rectVO.getxPosition(), rectVO.getyPosition(),
                    rectVO.getxPosition() + rectVO.getWidth(), rectVO.getyPosition() + rectVO.getHeight()));

            if (i < layoutData.getDrawableVOS().size()) {
                LayoutData.DrawableVO drawableVO = layoutData.getDrawableVOS().get(i);
                pieceList.add(new SavePieceVO(drawableVO.getDrawableRect(),
                        drawableVO.getDegree(), drawableVO.getTransX(), drawableVO.getTransY(), drawableVO.getScale()));
            }
        }
        mSavePolygonLayoutInfo = new SavePolygonLayoutInfo();
        mSavePolygonLayoutInfo.setLayoutData(layoutData);
        mSavePolygonLayoutInfo.setPuzzleMode(PuzzleMode.MODE_LAYOUT);
        mSavePolygonLayoutInfo.setJsonWidth(layoutData.getW());
        mSavePolygonLayoutInfo.setJsonHeight(layoutData.getH());
        mSavePolygonLayoutInfo.setInsidePaddingRatio(layoutData.getInsidePaddingRatio());
        mSavePolygonLayoutInfo.setOutsidePaddingRatio(layoutData.getOutsidePaddingRatio());
        mSavePolygonLayoutInfo.setRadianRatio(layoutData.getRadian());
        mSavePolygonLayoutInfo.setRotationImgs(mRotationImgs);
        mSavePolygonLayoutInfo.setRectList(rectList);
        mSavePolygonLayoutInfo.setPieceVOList(pieceList);
        mSavePolygonLayoutInfo.setZoomArray(getCurZoomArray());
    }

}
