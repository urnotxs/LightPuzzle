package com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.AdjustmentVideoLayout;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.puzzle.piece.AjustmentPiece;
import com.xs.lightpuzzle.puzzle.piece.LayoutJointPiece;
import com.xs.lightpuzzle.puzzle.piece.callback.PieceAnimationCallback;
import com.xs.lightpuzzle.puzzle.piece.util.MatrixUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;


/**
 * Created by xs on 2018/9/5.
 */

public class ContentView extends View {
    private final int CLICK_RESP_SIZE = Utils.getRealPixel3(5);// 点击响应
    private final int DURATION = 300;//动画时长
    private Context mContext;
    private RectF mRect;
    private RectF mClipRect;
    private float mViewWidth;
    private float mViewHeight;
    private AjustmentPiece mPiece;

    // Touch相关变量
    private float mDownX;
    private float mDownY;
    private float mPreviousDistance;// 做缩放时，前后两次距离
    private PointF mMidPoint = new PointF();;

    private enum ActionMode {
        NONE, DRAG, ZOOM, MOVE, SWAP;
    }//无，拖拽，缩放，移动线，切换
    private ActionMode mCurrentMode = ActionMode.NONE;
    private AjustmentPiece mHandlingPiece;// 当前把持的piece

    private RectF mDrawableRect;

    private PieceAnimationCallback mCallback = new PieceAnimationCallback() {
        @Override
        public void onInvalidate() {
            invalidate();
        }
    };

    private AdjustmentVideoLayout.VertexPotsChangedListener mListener;
    public void setVertexPotsChangedListener(AdjustmentVideoLayout.VertexPotsChangedListener listener) {
        mListener = listener;
    }

    public ContentView(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRect != null && mPiece != null){
            //mPiece.draw(canvas);
            mDrawableRect = mPiece.getCurrentDrawableBounds();
            calculateVertexPots(mRect , mDrawableRect);
        }
    }

    public void onSave(){
        if (mCurrentMode == ActionMode.NONE){
            calculateTexturPots(mClipRect , mDrawableRect);
        }
    }
    /**
     * 计算视频的纹理坐标
     *
     * @param viewRect OpenGL视口大小
     * @param drawableRect
     */
    private void calculateTexturPots(RectF viewRect , RectF drawableRect) {
        float leftOff = Math.abs(drawableRect.left - viewRect.left);
        float topOff = Math.abs(drawableRect.top - viewRect.top);
        float rightOff = Math.abs(drawableRect.right - viewRect.right);
        float bottomOff = Math.abs(drawableRect.bottom - viewRect.bottom);

        // 左下角纹理坐标 ( 0 , 1-0 )
        float x1 = leftOff/drawableRect.width();
        float y1 = 1 - ( bottomOff/drawableRect.height());

        // 左上角纹理坐标 ( 0 , 1-1 )
        float x2 = leftOff/drawableRect.width();
        float y2 = 1 - (1 - topOff/drawableRect.height());

        // 右下角纹理坐标 ( 1 , 1-0 )
        float x3 = 1 - rightOff/drawableRect.width();
        float y3 = 1 - (bottomOff/drawableRect.height());

        // 右上角纹理坐标 ( 1 , 1-1 )
        float x4 = 1 - rightOff/drawableRect.width();
        float y4 = 1 - (1 - topOff/drawableRect.height());

        float[] mTexturePots = {x1,y1,x2,y2,x3,y3,x4,y4};

        if (mListener != null){
            mListener.onSave(mTexturePots);
        }
    }

    /**
     *  计算视频的顶点坐标
     *
     * @param viewRect OpenGL视口大小
     * @param drawableRect
     */
    private void calculateVertexPots(RectF viewRect , RectF drawableRect) {
        float leftOff = drawableRect.left - viewRect.left;
        float topOff = drawableRect.top - viewRect.top;

        viewRect = new RectF(0 , 0 ,
                viewRect.width() , viewRect.height());

        drawableRect = new RectF(leftOff , topOff ,
                leftOff + drawableRect.width() ,
                topOff + drawableRect.height());

        float rightOff = drawableRect.right - viewRect.right;
        float bottomOff = drawableRect.bottom - viewRect.bottom;

        // 左下角顶点坐标
        float x1 = leftOff/viewRect.width() * 2 - 1 ;
        float y1 = -bottomOff/viewRect.height() * 2 - 1 ;

        // 左上角顶点坐标
        float x2 = leftOff/viewRect.width() * 2 - 1 ;
        float y2 = -topOff/viewRect.height() * 2 + 1 ;

        //右下角顶点坐标
        float x3 = rightOff/viewRect.width() * 2 + 1 ;
        float y3 = -bottomOff/viewRect.height() * 2 - 1 ;

        //右上角顶点坐标
        float x4 = rightOff/viewRect.width() * 2 + 1 ;
        float y4 = -topOff/viewRect.height() * 2 + 1 ;



        float [] mVerTex = new float[]{
                x1,y1,x2,y2,x3,y3,x4,y4
        };
/*        Log.e("VerTex" , "width :" + viewRect.width() +
                " height :" + viewRect.height() +
                " leftOff : "+ leftOff +
                " topOff : "+ topOff +
                " rightOff : "+ rightOff +
                " bottomOff : "+ bottomOff +
                " 左下 ： x = " + x1 + " y = " + y1 +
                " 左上 ： x = " + x4 + " y = " + y4 +
                " 右下 ： x = " + x2 + " y = " + y2 +
                " 右上 ： x = " + x3 + " y = " + y3 );*/
        if (mListener != null){
            mListener.onChanged(mVerTex);
        }

    }

    public void setParams(float width, float height, RectF clipRect){
        mRect = new RectF(0, 0, width, height);
        mViewWidth = mRect.width();
        mViewHeight = mRect.height();
        mClipRect = clipRect;

        Bitmap bitmap = Bitmap.createBitmap((int)mViewWidth,(int)mViewHeight , Bitmap.Config.ARGB_8888);
        BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setFilterBitmap(true);

        mPiece = new AjustmentPiece(drawable, mClipRect, new Matrix(), 0);
        Matrix matrix = MatrixUtils.generateMatrix(mPiece, mClipRect, 0f);

        mPiece.set(matrix);
        mPiece.setAnimateDuration(DURATION);
    }

    private RectF calculateMinRect(RectF standardRect , RectF adapterRect){
        float standardRatio = standardRect.width() / standardRect.height();
        float adapterRatio = adapterRect.width() / adapterRect.height();
        float outputWidth;
        float outputHeight;
        if (adapterRatio > standardRatio) {
            //齐宽
            outputWidth = standardRect.width();
            outputHeight = outputWidth / adapterRatio;
        } else {
            //齐高
            outputHeight = standardRect.height();
            outputWidth = outputHeight * adapterRatio;
        }
        float left = standardRect.left + (standardRect.width() - outputWidth) / 2;
        float top = standardRect.top + (standardRect.height() - outputHeight) / 2;
        return new RectF(left, top, left + outputWidth, top + outputHeight);
    }

    public void setImagePath(String imagePath) {

        Bitmap bitmap = JaneBitmapFactory.decodefile(mContext, imagePath);
        BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setFilterBitmap(true);

        mPiece = new AjustmentPiece(drawable, mClipRect, new Matrix(), 0);
        Matrix matrix = MatrixUtils.generateMatrix(mPiece, mClipRect, 0f);

        mPiece.set(matrix);
        mPiece.setAnimateDuration(DURATION);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();

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
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                boolean result = finishAction(event);
                mCurrentMode = ActionMode.NONE;
                invalidate();
                if (result) {
                    return true;
                }
                break;
        }

        if (mCurrentMode != ActionMode.NONE) {
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 决定当前event会触发哪种事件，none、drag、zoom、moveline、swap
     * */
    private void decideActionMode(MotionEvent event) {
        if (mPiece.isAnimateRunning()) {
            mCurrentMode = ActionMode.NONE;
            return;
        }
        if (event.getPointerCount() == 1) {
            //落点在区域外
            if (mDownX < mRect.left || mDownX > mRect.right || mDownY < mRect.top || mDownY > mRect.bottom) {
                return;
            }
            //当前触发的是piece的移动
            mHandlingPiece = mPiece;

            if (mHandlingPiece != null) {
                mCurrentMode = ActionMode.DRAG;
                //计时器触发长摁事件
                //mHandler.postDelayed(mSwitchToSwapAction, 500);
            }
        } else if (event.getPointerCount() > 1) {
            //两个手指，触发缩放的条件
            if (mHandlingPiece != null
                    && mRect.contains(event.getX(1), event.getY(1))
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
                break;
            case ZOOM:
                mHandlingPiece.record();
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
                break;
            case ZOOM:
                zoomPiece(mHandlingPiece, event);
                break;
        }
    }

    /**
     * 结束Action
     */
    private boolean finishAction(MotionEvent event) {
        boolean isChangeFilterBarIndex = false;
        boolean isClick = false;
        switch (mCurrentMode) {
            case NONE:
                break;
            case DRAG:
                if (mHandlingPiece != null && !mHandlingPiece.isFilledArea()) {
                    mHandlingPiece.moveToFillArea(mCallback);
                }

                if (Math.abs(mDownX - event.getX()) < CLICK_RESP_SIZE
                        && Math.abs(mDownY - event.getY()) < CLICK_RESP_SIZE) {
                    isClick = true;
                }
                //暂停
                if (isClick && mListener != null){
                    mListener.onStop();
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
                break;
        }

        //暂停
        if (!isClick && mListener != null){
            mListener.onPlay();
        }

        return isChangeFilterBarIndex;
    }

    /**
     *  拖拽piece
     * */
    private void dragPiece(LayoutJointPiece piece, MotionEvent event) {
        if (piece == null || event == null) return;
        piece.translate(event.getX() - mDownX, event.getY() - mDownY);
    }

    /**
     *  缩放piece
     * */
    private void zoomPiece(LayoutJointPiece piece, MotionEvent event) {
        if (piece == null || event == null || event.getPointerCount() < 2) return;
        float scale = calculateDistance(event) / mPreviousDistance;
        piece.zoomAndTranslate(scale, scale, mMidPoint, event.getX() - mDownX, event.getY() - mDownY);
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
}
