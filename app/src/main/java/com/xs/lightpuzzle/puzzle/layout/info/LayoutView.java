package com.xs.lightpuzzle.puzzle.layout.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.info.low.PuzzlesLayoutInfo;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutData;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/4/18.
 */

public class LayoutView extends View {
    private int mViewWidth;
    private int mViewHeight;
    private ArrayList<Bitmap> mBitmaps;
    private RotationImg[] mRotationImg;
    private List<PointF[]> mImgPoints;
    private PuzzlesLayoutInfo mPolygonLayoutInfo;
    private float ratio = 1f;
    private int mStandSize = 2048;
    private String[] mPointStringArr = {"0,0,683,512",
            "683,0,683,512",
            "1366,0,682,512",
            "0,512,683,512",
            "0,1024,683,512",
            "683,512,1365,1024",
            "0,1536,683,512",
            "683,1536,683,512",
            "1366,1536,682,512"};


    public interface PuzzleInvalidateCallBack {
        //需要重绘部分的rect
        void onInvalidate(String msg, Rect rect);
    }

    public LayoutView(Context context) {
        super(context);
        loadFackData();
    }

    private void loadFackData() {

        mRotationImg = new RotationImg[9];
        mBitmaps = new ArrayList<>();
        String path = Utils.getSdcardPath() + "/Tencent/QQ_Images/3b83527eb1af24d0.png";
        Bitmap bitmap = JaneBitmapFactory.decodefile(getContext() , path );

        for (int i = 0 ; i < 9 ; i++){
            RotationImg img = new RotationImg();
            img.setPicPath(path);
            mRotationImg[i] = img;
            mBitmaps.add(bitmap);
        }

        mImgPoints = new ArrayList<>();

        for (int i = 0; i < mPointStringArr.length ; i++){
            String [] list = mPointStringArr[i].split(",");
            PointF[] pointFArr = new PointF[2];
            for (int j = 0 ; j < 2 ; j++){
                PointF pointF = new PointF(
                        Float.parseFloat(list[2*j]) ,
                        Float.parseFloat(list[2*j+1]));
                pointFArr[j] = pointF;
            }
            mImgPoints.add(pointFArr);
        }

        mPolygonLayoutInfo = new PuzzlesLayoutInfo();
        mPolygonLayoutInfo.setPicPathList(mRotationImg);
        mPolygonLayoutInfo.setRect(new Rect(0, 0, Utils.getScreenW(), Utils.getScreenW()));

        LayoutData layoutData = new LayoutData().generateLayoutData(mStandSize , mImgPoints);
        layoutData.setOutsidePaddingRatio(0.2f);
        layoutData.setInsidePaddingRatio(0.5f);
        mPolygonLayoutInfo.init(layoutData);
        mPolygonLayoutInfo.addPieces(getContext(),mBitmaps);

/*        mPolygonLayoutInfo.setPuzzleInitBitmapCallback(new PuzzleInvalidateCallBack() {
            @Override
            public void onInvalidate(String msg, Rect rect) {
                invalidate();
            }
        });*/
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPolygonLayoutInfo != null && mPolygonLayoutInfo.dispatchTouchEvent(event)){
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int length = width > height ? height : width;

        mViewWidth = (int) (length*ratio);
        mViewHeight = length ;
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(
                0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        canvas.save();

        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setAntiAlias(true);
        canvas.drawColor(Color.WHITE);

        if (mPolygonLayoutInfo != null){
            mPolygonLayoutInfo.draw(canvas);
        }
        canvas.restore();
    }
}
