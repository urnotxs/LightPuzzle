package com.xs.lightpuzzle.puzzle.info.low;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.xs.lightpuzzle.LightPuzzleApplication;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.DecodeImageOptions;
import com.xs.lightpuzzle.imagedecode.core.ImageScaleType;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.PuzzleMode;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.image.ImageShowBarData;
import com.xs.lightpuzzle.puzzle.info.DrawView;
import com.xs.lightpuzzle.puzzle.layout.info.model.SavePieceVO;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzleImageMsgEvent;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesImageMsgName;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.piece.LayoutJointPiece;
import com.xs.lightpuzzle.puzzle.piece.callback.PieceAnimationCallback;
import com.xs.lightpuzzle.puzzle.piece.util.MatrixUtils;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.ShapeUtils;
import com.xs.lightpuzzle.puzzle.util.ShareData;
import com.xs.lightpuzzle.puzzle.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;


/**
 * Created by xs on 2018/11/29.
 * <p>
 * 普通模板拼图 之 ImageInfo
 */

public class PuzzleImagePieceInfo implements DrawView {

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

    @Override
    public void init() {
        if (imgPoint == null) {
            return;
        }
        Rect finalRect;
        if (save) {
            finalRect = outPutRect;
        } else {
            finalRect = rect;
        }
        if (finalRect == null) {
            return;
        }

        int border = 1; // 防白边,误差容错
        drawPoint = ShapeUtils.makePts(imgPoint, new Rect(finalRect.left - border, finalRect.top - border,
                finalRect.right + border, finalRect.bottom + border));
        drawRect = ShapeUtils.makeRect(drawPoint);

        path = ShapeUtils.ptsToPath(drawPoint);

        RectF rectF = new RectF();
        if (path.isRect(rectF)) {
            polygon = false;
        } else {
            polygon = true;
        }
    }

    @Override
    public void initBitmap(Context context) {
        if (context == null) {
            return;
        }
        if (rotationImg == null) {
            return;
        }
        if (drawPoint == null || drawRect == null) {
            return;
        }

        recycle();

        if (TextUtils.isEmpty(rotationImg.getPicPath())) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            DecodeImageOptions decodeImageOptions = new DecodeImageOptions.Builder()
                    .setBitmapConfig(Bitmap.Config.ARGB_8888)
                    .setImageScaleType(ImageScaleType.NONE)
                    .setDecodeingOptions(options)
                    .build();
            synchronized (this) {
                mEmptyBitmap = JaneBitmapFactory.decodeResource(context,
                        R.drawable.template_add_photo, null, decodeImageOptions);
            }
            int x = (drawRect.width() - mEmptyBitmap.getWidth()) / 2;
            int y = (drawRect.height() - mEmptyBitmap.getHeight()) / 2;
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
            generateFinalBmp();
            noneRect = new Rect(
                    drawRect.left + x,
                    drawRect.top + y,
                    drawRect.right - x,
                    drawRect.bottom - y);
            return;
        }

        int decodeWidth = (int) ((1.0f / 2) * Utils.getScreenW());

        if (save) {
            DecodeImageOptions decodeImageOptions = new DecodeImageOptions.Builder()
                    .setBitmapConfig(Bitmap.Config.ARGB_8888)
                    .setImageScaleType(ImageScaleType.EXACTLY)
                    .setDecodeingOptions(new BitmapFactory.Options())
                    .setNativeRetreatment(true)
                    .build();

            mSourceBitmap = JaneBitmapFactory.decodefile(
                    context, rotationImg.getPicPath(),
                    new ImageSize((int) (mSavePieceVO.getPieceRect().width()),
                            (int) mSavePieceVO.getPieceRect().height()), decodeImageOptions);

        } else {
            mSourceBitmap = JaneBitmapFactory.decodefile(context,
                    rotationImg.getPicPath(), new ImageSize(decodeWidth, decodeWidth));
        }

        mBeautyBitmap = generateBeautyBmp(mSourceBitmap);
        mFilterBitmap = generateFilterBmp(mBeautyBitmap);
        mFilterAlphaBitmap = generateFilterAlphaBmp(mBeautyBitmap, mFilterBitmap);
        generateFinalBmp();
        clearCacheBitmap();

        if (BitmapHelper.isValid(mFinalBitmap) && !save) {
            initPiece(context, mFinalBitmap);
        }

        if (mDraftData != null) {

            //草稿箱进入
            float scale = rect.width() * 1.0f / outPutRect.width();
            RectF srcRect = mDraftData.getPieceRect();
            RectF dstRect = new RectF(
                    srcRect.left * scale,
                    srcRect.top * scale,
                    srcRect.right * scale,
                    srcRect.bottom * scale
            );
            imagePiece.setCurrentDrawableBounds(dstRect, mDraftData.getDegree());
//            imagePiece.setZoom(mDraftData.getZoomArray().get(i));

            mDraftData = null;
        }
    }

    private SavePieceVO mDraftData;

    private void initPiece(Context context, Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setFilterBitmap(true);

        // TODO: 2018/11/29
        // 换图片？

        RectF rectF = new RectF(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom);
        imagePiece = new LayoutJointPiece(drawable, rectF, new Matrix(), id);
        final Matrix matrix = MatrixUtils.generateMatrix(imagePiece, rectF, 0f);

        imagePiece.set(matrix);
        imagePiece.setAnimateDuration(DURATION);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null || drawRect == null) {
            return;
        }

        if (mFinalBitmap == mEmptyBitmap) {
            if (noneRect == null) {
                return;
            } else if (!save) {
                canvas.save();
                paint = PuzzlesUtils.getPuzzleSettingPaint(paint);
                paint.setStyle(Paint.Style.FILL);//设置画笔为填充模式
                paint.setColor(0x407d7d7d);//不透明度25%
                canvas.drawPath(path, paint);
                canvas.drawBitmap(mFinalBitmap, null, noneRect, null);
                canvas.restore();
            }
        } else {
            if (save) {
                Matrix matrix = new Matrix();
                int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
                paint = PuzzlesUtils.getPuzzleSettingPaint(paint);

                paint.setColor(Color.WHITE);
                paint.setAlpha(255);
                canvas.drawPath(path, paint);
                paint.setXfermode(SRC_IN);

                matrix.reset();
                matrix.postRotate(mSavePieceVO.getDegree(),
                        mFinalBitmap.getWidth() / 2.0f, mFinalBitmap.getHeight() / 2.0f);
                Bitmap bitmap = Bitmap.createBitmap(mFinalBitmap, 0, 0, mFinalBitmap.getWidth(), mFinalBitmap.getHeight(), matrix, true);

                int top = (int) (rect.top * (outPutRect.width() * 1.0f / rect.width()));
                RectF rect = mSavePieceVO.getPieceRect();
                RectF saveDrawRect = new RectF(rect.left, rect.top - top, rect.right, rect.bottom - top);
                canvas.drawBitmap(bitmap, null, saveDrawRect, paint);
                paint.setXfermode(null);
                canvas.restoreToCount(saved);
            } else {
                imagePiece.setShowFrame(showFrame);
                if (polygon) {
                    imagePiece.drawPolygon(canvas, path);
                } else {
                    imagePiece.draw(canvas);
                }
            }
        }

        if (isSelectedForLong) {
            canvas.save();
            canvas.clipRect(drawRect);
            if (puzzleMode == PuzzleMode.MODE_JOIN || puzzleMode == PuzzleMode.MODE_LONG) {
                paint = PuzzlesUtils.getPuzzleSettingPaint(paint);
                paint.setColor(0xfff5808e);
                paint.setStrokeWidth(SELECT_RESP_SIZE);
                paint.setStyle(Paint.Style.STROKE);

                //4.4以下的机器drawPath有很大概率会出现把整个Path填满的情况
                if (Build.VERSION.SDK_INT > 20) {
                    canvas.drawPath(path, paint);
                } else {
                    drawBorder(canvas, paint, drawPoint);
                }
            }
            canvas.restore();
        }
    }

    private boolean isSelectedForLong;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && !touchDrawInside(event)) {
            isSelectedForLong = false;
            return false;
        }
        if ((puzzleMode == PuzzleMode.MODE_JOIN
                || puzzleMode == PuzzleMode.MODE_LONG)
                && !isSelectedForLong) {
            // 长图和拼接：
            // 没选中状态，只接收点击触发
            return onTouchForClick(event);
        } else {
            // 已选中状态，可接收拖拽触发
            if (event.getAction() != MotionEvent.ACTION_DOWN
                    && mCurrentMode == ActionMode.NONE) {
                return false;
            }
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getX();
                    mDownY = event.getY();

                    decideActionMode(event);
                    prepareAction();
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
            return false;
        }
    }

    /**
     * mHandlingPiece == null
     * 每个item需要先选中，才可以做托拉拽效果，
     * 否则不接受事件触发，只可scroll view
     */
    private boolean onTouchForClick(MotionEvent event) {
        // todo 可能需要弹出选图框（长图）
        // todo 可能需要直接改变滤镜条和美颜条的指向
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                boolean isClick = false;
                if (Math.abs(mDownX - event.getX()) < CLICK_RESP_SIZE
                        && Math.abs(mDownY - event.getY()) < CLICK_RESP_SIZE) {
                    isClick = true;
                }
                if (isClick) {
                    if (TextUtils.isEmpty(rotationImg.getPicPath())) {
                        // 选图
                        //添加新图片
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                                .PUZZLES_IMAGE_POP_PHOTOPICKER, event.getAction(), drawPoint));
                    } else {
                        isSelectedForLong = true;
                        showToolBar(event.getAction());
                        invalidate();
                    }
                    return true;
                }
                break;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return false;
    }


    private void drawBorder(Canvas canvas, Paint paint, Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (i + 1 < points.length) {
                Point pt1 = points[i];
                Point pt2 = points[i + 1];
                canvas.drawLine(pt1.x, pt1.y, pt2.x, pt2.y, paint);
            } else {
                Point pt1 = points[i];
                Point pt2 = points[0];
                canvas.drawLine(pt1.x, pt1.y, pt2.x, pt2.y, paint);
            }
        }
    }


    /**
     * 执行Action
     */
    private void performAction(MotionEvent event) {
        switch (mCurrentMode) {
            case NONE:
                break;
            case DRAG:
                dragPiece(imagePiece, event);

                EventBus.getDefault().post(new PuzzleImageMsgEvent(
                        PuzzlesImageMsgName.PUZZLES_IMAGE_TOUCH,
                        event.getAction(), new Point((int) event.getX(), (int) event.getY())));
                break;
            case ZOOM:
                zoomPiece(imagePiece, event);
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
                if (imagePiece != null && !imagePiece.isFilledArea()) {
                    imagePiece.moveToFillArea(mCallback);
                }

                boolean isClick = false;
                if (Math.abs(mDownX - event.getX()) < CLICK_RESP_SIZE
                        && Math.abs(mDownY - event.getY()) < CLICK_RESP_SIZE) {
                    isClick = true;
                }

                if (isClick) {
//                    if (PuzzlesInfoHelper.getInstance().isPicFilterEdit()) {
//                        isChangeFilterBarIndex = true;
//                    }
                    isSelectedForLong = false;
                    showToolBar(event.getAction());
                } else {
                    EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                            .PUZZLES_IMAGE_PIECE_SHOW_BAR, event.getAction(), null));
                }

                EventBus.getDefault().post(new PuzzleImageMsgEvent(
                        PuzzlesImageMsgName.PUZZLES_IMAGE_TOUCH,
                        event.getAction(), new Point((int) event.getX(), (int) event.getY())));
                break;
            case ZOOM:
                if (imagePiece != null && !imagePiece.isFilledArea()) {
                    if (imagePiece.canFilledArea()) {
                        imagePiece.moveToFillArea(mCallback);
                    } else {
                        imagePiece.fillArea(mCallback, false);
                    }
                }
                break;
        }

        return isChangeFilterBarIndex;
    }

    /**
     * 决定当前event会触发哪种事件，none、drag、zoom、moveline、swap
     */
    private void decideActionMode(MotionEvent event) {
        if (imagePiece.isAnimateRunning()) {
            mCurrentMode = ActionMode.NONE;
            return;
        }
        if (event.getPointerCount() == 1) {
            //落点在区域外
            if (touchDrawInside(event)) {
                //当前触发的是piece的移动
                mCurrentMode = ActionMode.DRAG;
            }
        } else if (event.getPointerCount() > 1) {
            //两个手指，触发缩放的条件
            if (touchDrawInside(event) && mCurrentMode == ActionMode.DRAG) {
                mCurrentMode = ActionMode.ZOOM;
            }
        }
    }

    /**
     * 执行Action前的准备工作
     */
    private void prepareAction() {
        switch (mCurrentMode) {
            case NONE:
                break;
            case DRAG:
                imagePiece.record();
                break;
            case ZOOM:
                imagePiece.record();
                break;
        }
    }

    /**
     * 缩放piece
     */
    private void zoomPiece(LayoutJointPiece piece, MotionEvent event) {
        if (piece == null || event == null || event.getPointerCount() < 2) return;
        float scale = calculateDistance(event) / mPreviousDistance;
        piece.zoomAndTranslate(scale, scale, mMidPoint, event.getX() - mDownX, event.getY() - mDownY);
    }

    /**
     * 拖拽piece
     */
    private void dragPiece(LayoutJointPiece piece, MotionEvent event) {
        if (piece == null || event == null) return;
        piece.translate(event.getX() - mDownX, event.getY() - mDownY);
    }

    public void swapFillArea() {
        imagePiece.swapFillArea(mCallback, true);
    }

    /**
     * 计算事件的两个手指的中心坐标
     */
    private void calculateMidPoint(MotionEvent event, PointF point) {
        point.x = (event.getX(0) + event.getX(1)) / 2;
        point.y = (event.getY(0) + event.getY(1)) / 2;
    }

    /**
     * 计算事件的两个手指之间的距离
     */
    private float calculateDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * 点击是否在当前draw图片区域
     * *
     */
    private boolean touchDrawInside(MotionEvent event) {
        if (event != null && drawPoint != null) {
            return touchInside(drawPoint, event);
        }
        return false;
    }

    /**
     * 点击是否在pointsArr区域
     * *
     */
    private boolean touchInside(Point[] pointsArr, MotionEvent event) {
        if (event != null && pointsArr != null) {
            return touchInside(pointsArr, new Point((int) event.getX(), (int) event.getY()));
        }
        return false;
    }

    /**
     * point当前Point[]区域
     *
     * @param pointsArr point数组
     * @param point     检测点
     */
    private boolean touchInside(Point[] pointsArr, Point point) {
        if (pointsArr != null && point != null) {
            List<Point> points = Arrays.asList(pointsArr);
            return PuzzlesUtils.pointInRegion(points, point);
        }
        return false;
    }


    public boolean contains(Point eventPoint) {
        return touchInside(drawPoint, eventPoint);
    }

    /**
     * 展示工具条
     *
     * @param actCode MotionEvent action code
     */
    public void showToolBar(int actCode) {
        if (rotationImg != null && !TextUtils.isEmpty(rotationImg.getPicPath())) {
            ImageShowBarData imageShowBarData = new ImageShowBarData();
            imageShowBarData.setPoints(drawPoint);
//            if (rotationImg instanceof UserVideoInfo) {
//                UserVideoInfo userVideoInfo = (UserVideoInfo) rotationImg;
//                imageShowBarData.setVideo(!userVideoInfo.isPicMixVideo());
//                imageShowBarData.setSoundOpen(userVideoInfo.isSoundtrackingOpen());
//            }
            EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName
                    .PUZZLES_IMAGE_PIECE_SHOW_BAR, actCode, imageShowBarData));
        }
    }

    public void postSelectForLong(int actCode) {
        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_IMGAE_SELECT_FOR_LONG,
                actCode, drawPoint));
    }

    @Override
    public void recycle() {
        mSourceBitmap = null;
        mEmptyBitmap = null;
        mBeautyBitmap = null;
        mFinalBitmap = null;
        mFilterAlphaBitmap = null;
        mFinalBitmap = null;
    }

    public int getSkinSmoothAlpha() {
        if (rotationImg != null) {
            if (!rotationImg.isChangedBeauty()) {
                return 35;
            }
            return rotationImg.getSkinSmoothAlpha();
        }
        return 0;
    }

    public int getSkinColorAlpha() {
        if (rotationImg != null) {
            if (!rotationImg.isChangedBeauty()) {
                return 35;
            }
            return rotationImg.getSkinColorAlpha();
        }
        return 0;
    }

    /**
     * 美颜参数赋值
     */
    public void setBeautyParam(int skinSmoothAlpha, int skinColorAlpha) {
        if (rotationImg != null) {
            if (!rotationImg.isChangedBeauty())
                rotationImg.setChangedBeauty(true);
            rotationImg.setSkinSmoothAlpha(skinSmoothAlpha);
            rotationImg.setSkinColorAlpha(skinColorAlpha);
        }
    }

    /**
     * 编辑页美颜功能，调节bar，实时改动参数
     */
    public void changeBeautyParam(int skinSmoothAlpha, int skinColorAlpha) {

//        if (rotationImg != null) {
//
//            if (!rotationImg.isChangedBeauty())
//                rotationImg.setChangedBeauty(true);
//
//            rotationImg.setSkinSmoothAlpha(skinSmoothAlpha);
//            rotationImg.setSkinColorAlpha(skinColorAlpha);
//
//            SkinAlphaVO skinAlphaVO = new SkinAlphaVO();
//            skinAlphaVO.skinSmoothAlpha = skinSmoothAlpha;
//            skinAlphaVO.skinColorAlpha = skinColorAlpha;
//
//            if (mSkinRunnable != null) {
//                mSkinRunnable.addSkin(skinAlphaVO);
//            }
//        }
    }

    /**
     * 弹出美颜条时，开启美颜线程
     */
    public void startSkinThread() {

        Log.e("Analysis", "启动线程");

        if (mSkinHandler == null) {
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

//        if (mSkinRunnable == null) {
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
//                        initBeautyAndFilterBitmap(skinAlphaVO.skinSmoothAlpha, skinAlphaVO.skinColorAlpha);
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

//    public Runnable getSkinRunnable() {
//        return mSkinRunnable;
//    }

    /**
     * 关闭美颜条时，关闭美颜线程
     */
    public void stopSkinThread() {
//        if (mSkinRunnable != null) {
//            mSkinRunnable.stop();
//            mSkinRunnable = null;
//            clearCacheBitmap();
//
//            Log.e("Analysis", "关闭线程");
//        }
    }

    private void initBeautyAndFilterBitmap(int skinSmoothAlpha, int skinColorAlpha) {
        // 调节美颜进度条时调用
        rotationImg.setSkinSmoothAlpha(skinSmoothAlpha);
        rotationImg.setSkinColorAlpha(skinColorAlpha);
        mBeautyBitmap = generateBeautyBmp(mSourceBitmap);
        mFilterBitmap = generateFilterBmp(mBeautyBitmap);
        mFilterAlphaBitmap = generateFilterAlphaBmp(mBeautyBitmap, mFilterBitmap);

        if (mFinalBitmap != null) {
            synchronized (mFinalBitmap) {
                generateFinal();
            }
        }
    }

    /**
     * 弹出底部滤镜栏时，为当前imageInfo创建美颜和滤镜效果图，提升改变alpha时的效率
     */
    public void openFilterBar() {
        mBeautyBitmap = generateBeautyBmp(mSourceBitmap);
        mFilterBitmap = generateFilterBmp(mBeautyBitmap);
    }

    public void changeBeautyEffect() {
        mBeautyBitmap = generateBeautyBmp(mSourceBitmap);
        mFilterBitmap = generateFilterBmp(mBeautyBitmap);
        mFilterAlphaBitmap = generateFilterAlphaBmp(mBeautyBitmap, mFilterBitmap);
    }

    /**
     * 滤镜条关闭时，清空美颜bmp和滤镜bmp和透明度bmp
     */
    public void closeFilterBar() {
        clearCacheBitmap();
    }

    private void clearCacheBitmap() {
        mBeautyBitmap = null;
        mFilterBitmap = null;
        mFilterAlphaBitmap = null;
    }

    /**
     * 图片加滤镜
     */
    public void changeFilterEffect() {
        if (mBeautyBitmap == null) {
            mBeautyBitmap = generateBeautyBmp(mSourceBitmap);
        }
        mFilterBitmap = generateFilterBmp(mBeautyBitmap);
        mFilterAlphaBitmap = generateFilterAlphaBmp(mBeautyBitmap, mFilterBitmap);
    }

    /**
     * 修改滤镜透明度
     */
    public void changeFilterAlphaEffect() {
        if (puzzleMode == PuzzleMode.MODE_VIDEO) {
            openFilterBar();
        }
        mFilterAlphaBitmap = generateFilterAlphaBmp(mBeautyBitmap, mFilterBitmap);
    }

    /**
     * 生成美颜效果图
     * 至少与原图一致
     */
    public Bitmap generateBeautyBmp(Bitmap bitmap) {
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
    public Bitmap generateFilterBmp(Bitmap bitmap) {
        Bitmap filterBitmap = null;

//        if (rotationImg == null || rotationImg.getTepFilterInfo() == null) {
//            return null;
//        }
//
//        if (BitmapHelper.isInvalid(bitmap)) {
//            return null;
//        }

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
     * 生成带透明度的滤镜效果图
     */
    public Bitmap generateFilterAlphaBmp(Bitmap bitmap, Bitmap filterBitmap) {
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

    /**
     * 生成最终效果图
     */
    public void generateFinalBmp() {
        if (BitmapHelper.isValid(mEmptyBitmap)) {
            mFinalBitmap = mEmptyBitmap;
        }
        if (BitmapHelper.isValid(mSourceBitmap)) {
            mFinalBitmap = mSourceBitmap;
        }
//        if (BitmapHelper.isValid(mBeautyBitmap)) {
//            mFinalBitmap = mBeautyBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        }
//        if (BitmapHelper.isValid(mFilterBitmap)) {
//            mFinalBitmap = mFilterBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        }
//        if (BitmapHelper.isValid(mFilterAlphaBitmap)) {
//            mFinalBitmap = mFilterAlphaBitmap.copy(Bitmap.Config.ARGB_8888, true);
//            mFilterAlphaBitmap = null;
//        }

    }

    /**
     * 生成最终效果图,以及将bitmap转化为Drawable,并赋值给piece
     */
    public void generateFinal() {
        generateFinalBmp();
        if (mFinalBitmap != null) {
            BitmapDrawable drawable = new BitmapDrawable(
                    LightPuzzleApplication.getContext().getResources(), mFinalBitmap);
            drawable.setAntiAlias(true);
            drawable.setFilterBitmap(true);
            imagePiece.setDrawable(drawable);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setImgPoint(PointF[] imgPoint) {
        this.imgPoint = imgPoint;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setRotationImg(RotationImg rotationImg) {
        if (rotationImg == null) {
            return;
        }
        this.rotationImg = rotationImg;
    }

    public void setPuzzleMode(int puzzleMode) {
        this.puzzleMode = puzzleMode;
    }

    public RotationImg getRotationImg() {
        return rotationImg;
    }

    public void setShowFrame(boolean showFrame) {
        this.showFrame = showFrame;
    }

    public Point[] getDrawPoint() {
        return drawPoint;
    }

    public void setSelectForLong(boolean selectForLong) {
        this.selectForLong = selectForLong;
        isSelectedForLong = selectForLong;
    }

    public boolean isSelectForLong() {
        return selectForLong;
    }

    public Rect getDrawRect() {
        return drawPoint == null ? null : ShapeUtils.makeRect(drawPoint);
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    /**
     * 在当前旋转角度基础上旋转rotation
     */
    public void setRotation() {
        imagePiece.postRotate(90);
    }

    public PointF[] getImgPoint() {
        return imgPoint;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public LayoutJointPiece getImagePiece() {
        return imagePiece;
    }

    public Bitmap getSourceBitmap() {
        return mSourceBitmap;
    }

    public void setSourceBitmap(Bitmap bitmap) {
        this.mSourceBitmap = bitmap;
    }

    private transient static Xfermode SRC_IN = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private transient final int CLICK_RESP_SIZE = ShareData.PxToDpi_xhdpi(5);//点击响应
    private transient final int TOUCH_RESP_FRAME_SIZE = ShareData.PxToDpi_xhdpi(20);//移动切换效果边框
    private transient final int SELECT_RESP_SIZE = ShareData.PxToDpi_xhdpi(10);//长图，拼接，选中边框
    private final int DURATION = 300;//动画时长


    private int id = -1;

    private Rect rect;
    //保存时用到的rect
    private Rect outPutRect;

    private RotationImg rotationImg;

    private transient LayoutJointPiece imagePiece;

    private PointF[] imgPoint;

    private transient Point[] drawPoint;

    private transient Rect drawRect, noneRect;

    private transient Path path;

    private transient int bitmapWidth, bitmapHeight;
    private transient Bitmap mSourceBitmap;
    private transient Bitmap mEmptyBitmap;
    private transient Bitmap mBeautyBitmap;
    private transient Bitmap mFilterBitmap;
    private transient Bitmap mFilterAlphaBitmap;

    private transient Bitmap mFinalBitmap;

    private transient boolean showFrame;

    private transient Paint paint = new Paint();
    //判断是否异形
    private transient boolean polygon = false;

    private transient boolean save = false;

    private int puzzleMode;

    private transient boolean selectForLong;

    private transient Handler mSkinHandler;
//    private transient SkinRunnable mSkinRunnable;

    // --- touch
    private transient float mDownX;
    private transient float mDownY;
    private transient PointF mMidPoint = new PointF();// down时的中心点坐标
    private transient float mPreviousDistance;// 做缩放时，前后两次距离
    private transient ActionMode mCurrentMode = ActionMode.NONE;
    private SavePieceVO mSavePieceVO;

    public SavePieceVO savePieceVO() {
        float widthRatio = outPutRect.width() * 1.0f / rect.width();
        float heightRatio = outPutRect.height() * 1.0f / rect.height();
        mSavePieceVO = new SavePieceVO(imagePiece.getDrawableRectF(widthRatio, heightRatio),
                imagePiece.getMatrixAngle(), imagePiece.getMatrixTransX(),
                imagePiece.getMatrixTransY(), imagePiece.getMatrixScale());

        return mSavePieceVO;
    }

    public void setSavePieceVO(SavePieceVO savePieceVO) {
        // 恢复数据
        // TODO: 2018/11/30
        mDraftData = savePieceVO;
    }

    public void zoomOut() {
        // 图片缩小
        imagePiece.postZoom(0.9f);
    }

    public void zoomIn() {
        // 图片放大
        imagePiece.postZoom(1.1f);
    }

    private enum ActionMode {
        NONE, DRAG, ZOOM, MOVE, SWAP;
    }//无，拖拽，缩放，移动线，切换

    /**
     * 上下移动替换模板调整位置
     *
     * @param scrollYOffset 与原位置的偏移量
     */
    public void translateOffset(int scrollYOffset) {
        if (imagePiece != null) {
            imagePiece.setRectF(new RectF(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom));
            imagePiece.postTranslate(0, scrollYOffset);
        }
    }
}
