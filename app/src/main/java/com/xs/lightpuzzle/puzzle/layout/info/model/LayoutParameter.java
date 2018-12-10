package com.xs.lightpuzzle.puzzle.layout.info.model;

import android.graphics.RectF;

import com.xs.lightpuzzle.puzzle.piece.LayoutPiece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局相关
 */
public class LayoutParameter {

    private float mJsonWidth;//json提供的宽
    private float mJsonHeight;//json提供的高
    private float mWidth;
    private float mHeight;
    private float mOuterPadding;//外部边距
    private float mOuterPaddingRatio;//外部边距
    private float mPiecePadding;//内部间隔
    private float mPiecePaddingRatio;//内部间隔
    private float mPieceRadianRatio;//边框半径

    private LayoutArea mOuterArea;//外部矩形区域
    private HashMap<Integer, LayoutArea> mAreaHashMap = new HashMap<>();
    private List<LayoutArea> mLayoutAreaList = new ArrayList<>();//所有矩形区域
    private List<LayoutLine> mAllVerLineList = new ArrayList<>();//所有矩形的所有垂直方向的边
    private List<LayoutLine> mAllHorLineList = new ArrayList<>();//所有矩形的所有水平方向的边
    private List<LayoutLine> mLineList = new ArrayList<>();//所有矩形的所有边
    private List<LayoutLine> mOneLineList = new ArrayList<>();
    private ArrayList<Float> mAxisList = new ArrayList<>();//根据当前move的线段的方向，去检索标准的坐标刻度，用来释放线段时做一个位置适配

    public List<LayoutLine> generateOneLineList(LayoutLine mHandlingLine){
        ArrayList<LayoutLine> mLines = new ArrayList<>();
        mOneLineList = new ArrayList<>();
        //用于以手持line为基准，初始一个高为10的矩形框，方向相同的line在其内则表示在一条直线上
        RectF totalLineRectF = mHandlingLine.getTotalLineRectF(mOuterArea);

        if (mHandlingLine.getDirection() == LayoutLine.Direction.HORIZONTAL) {
            mLines.addAll(mAllHorLineList);
        } else {
            mLines.addAll(mAllVerLineList);
        }

        for (LayoutLine line : mLines) {
            RectF lineRectF = line.getLineRectF();

            if (totalLineRectF != null && lineRectF != null && totalLineRectF.contains(lineRectF)) {
                mOneLineList.add(line);
            }
        }
        return mOneLineList;
    }
    public void generateXYAxisList(LayoutLine mHandlingLine) {
        ArrayList<LayoutLine> mLines = new ArrayList<>();
        mAxisList = new ArrayList<>();

        if (mHandlingLine.getDirection() == LayoutLine.Direction.HORIZONTAL) {
            mLines.addAll(mAllHorLineList);
        } else {
            mLines.addAll(mAllVerLineList);
        }

        for (LayoutLine line : mLines) {
            float position;

            if (mHandlingLine.getDirection() == LayoutLine.Direction.HORIZONTAL) {
                position = line.getStartPoint().y;
            } else {
                position = line.getStartPoint().x;
            }

            if (mAxisList.size() > 0 && mAxisList.contains(position)) {
                continue;
            } else {
                mAxisList.add(position);
            }
        }
    }

    public int getAreaCount() {
        return mLayoutAreaList.size();
    }

    RectF mBounds;
    public void setOuterBounds(int id, RectF bounds) {
        mOuterArea = new LayoutArea(id, bounds);
        mBounds = bounds;
        if (bounds.width()/bounds.height()<1){
            mTotalPadding = ((250.0f/2048)*bounds.width());
        }else{
            mTotalPadding = ((250.0f/2048)*bounds.height());
        }
    }
    public float getTotalPadding(){
        return mTotalPadding;
    }
    private float mTotalPadding = 0;//外部矩形框的宽高比
    public void setOuterPaddingRatio(float outerPaddingRatio) {
        mOuterPaddingRatio = outerPaddingRatio;
        mOuterPadding = outerPaddingRatio*mTotalPadding;
        initPiecePadding();
        if (mOuterArea != null)
            mOuterArea.setPadding(mOuterPadding);
    }

    public void setPaddingRatio(float piecePaddingRatio) {
        mPiecePaddingRatio = piecePaddingRatio;
        float total  ;
        if (mBounds.width()/mBounds.height()>1){
            total = ((80.0f/2048)*mBounds.width());
        }else{
            total = ((80.0f/2048)*mBounds.height());
        }
        mPiecePadding = (piecePaddingRatio*total)/2;
        initPiecePadding();
    }

    public void setRadianRatio(float pieceRadianRatio) {
        mPieceRadianRatio = pieceRadianRatio;
        initPieceRadian();
    }

    public void initPiecePadding() {
        for (int i = 0; i < mLayoutAreaList.size(); i++) {
            float paddingLeft = mPiecePadding;
            float paddingTop = mPiecePadding;
            float paddingRight = mPiecePadding;
            float paddingBottom = mPiecePadding;
            LayoutArea area = mLayoutAreaList.get(i);
            if (area.left() == mOuterArea.left()) {
                //left
                paddingLeft = mOuterPadding;
            }
            if (area.right() == mOuterArea.right()) {
                //right
                paddingRight = mOuterPadding;
            }
            if (area.top() == mOuterArea.top()) {
                //top
                paddingTop = mOuterPadding;
            }
            if (area.bottom() == mOuterArea.bottom()) {
                //bottom
                paddingBottom = mOuterPadding;
            }
            mLayoutAreaList.get(i).setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    public void initPieceRadian() {
        for (int i = 0; i < mLayoutAreaList.size(); i++) {
            mLayoutAreaList.get(i).setRadianRatio(mPieceRadianRatio);
        }
    }

    public void layout(LayoutData layoutData, float mScalWidthRatio, float mScalHeightRatio) {

        for (int i = 0; i < layoutData.getRect().size(); i++) {

            LayoutData.RectVO rectVO = layoutData.getRect().get(i);

            float left = (int) (rectVO.getxPosition() * mScalWidthRatio);
            float top = (int) (rectVO.getyPosition() * mScalHeightRatio);
            float right = (int) ((rectVO.getxPosition() + rectVO.getWidth()) * mScalWidthRatio);
            float bottom = (int) ((rectVO.getyPosition() + rectVO.getHeight()) * mScalHeightRatio);

            //调整外边框齐边
            if (Math.abs(mOuterArea.left() - left)<2){
                left = mOuterArea.left();
            }
            if (Math.abs(mOuterArea.top() - top)<2){
                top = mOuterArea.top();
            }
            if (Math.abs(mOuterArea.right() - right)<2){
                right = mOuterArea.right();
            }
            if (Math.abs(mOuterArea.bottom() - bottom)<2){
                bottom = mOuterArea.bottom();
            }

            LayoutArea area = new LayoutArea(i, new RectF(left, top, right, bottom));

            mLayoutAreaList.add(area);
            mAreaHashMap.put(area.getmId(), area);

            if (area.left() != mOuterArea.left()) {
                mAllVerLineList.add(area.getLines().get(0));
            }
            if (area.right() != mOuterArea.right()) {
                mAllVerLineList.add(area.getLines().get(2));
            }
            if (area.top() != mOuterArea.top()) {
                mAllHorLineList.add(area.getLines().get(1));
            }
            if (area.bottom() != mOuterArea.bottom()) {
                mAllHorLineList.add(area.getLines().get(3));
            }
        }
        mLineList.addAll(mAllHorLineList);
        mLineList.addAll(mAllVerLineList);

        //填补内部线的误差
        if (mAllHorLineList.size()>0) {
            generateXYAxisList(mAllHorLineList.get(0));
            for (int i = 0; i < mAxisList.size(); i++) {
                float standard = mAxisList.get(i);
                for (int j = 0; j < mAxisList.size(); j++) {
                    float offset = mAxisList.get(j) - standard;
                    if (offset != 0 && Math.abs(offset) < matchSize) {
                        mAxisList.set(j, standard);
                    }
                }
            }
            for (int i = 0; i < mAllHorLineList.size(); i++) {
                mAllHorLineList.get(i).releaseMove(mAxisList, matchSize);
            }
        }

        if (mAllVerLineList.size() > 0) {
            generateXYAxisList(mAllVerLineList.get(0));
            for (int i = 0; i < mAxisList.size(); i++) {
                float standard = mAxisList.get(i);
                for (int j = 0; j < mAxisList.size(); j++) {
                    float offset = mAxisList.get(j) - standard;
                    if (offset != 0 && Math.abs(offset) < matchSize) {
                        mAxisList.set(j, standard);
                    }
                }
            }
            for (int i = 0; i < mAllVerLineList.size(); i++) {
                mAllVerLineList.get(i).releaseMove(mAxisList, matchSize);
            }
        }

    }
    private float matchSize = 2;
    public void setJsonWidth(float width) {
        this.mJsonWidth = width;
    }

    public void setJsonHeight(float height) {
        this.mJsonHeight = height;
    }

    public void setWidth(float width) {
        this.mWidth = width;
    }

    public void setHeight(float height) {
        this.mHeight = height;
    }

    public LayoutArea getOuterArea() {
        return mOuterArea;
    }

    public ArrayList<Float> getAxisList() {
        return mAxisList;
    }

    public List<LayoutLine> getOneLineList() {
        return mOneLineList;
    }

    public List<LayoutLine> getLineList() {
        return mLineList;
    }

    public HashMap<Integer, LayoutArea> getAreaHashMap() {
        return mAreaHashMap;
    }

    public List<LayoutArea> getLayoutAreaList() {
        return mLayoutAreaList;
    }

    public LayoutData switchAreaToJson(List<LayoutPiece> layoutPieces) {
        float ratioH = mJsonHeight / mHeight;
        float ratioW = mJsonWidth / mWidth;
        LayoutData layoutData = new LayoutData();
        ArrayList<LayoutData.RectVO> rectVOS = new ArrayList<>();
        ArrayList<LayoutData.DrawableVO> drawableRectFs = new ArrayList<>();
        layoutData.setW(mJsonWidth);
        layoutData.setH(mJsonHeight);
        layoutData.setRadianRatio(mPieceRadianRatio);
        layoutData.setInsidePaddingRatio(mPiecePaddingRatio);
        layoutData.setOutsidePaddingRatio(mOuterPaddingRatio);
        for (int i = 0; i < mLayoutAreaList.size(); i++) {
            LayoutArea area = mLayoutAreaList.get(i);
            rectVOS.add(area.getRectVO(ratioW, ratioH));
            if (i<layoutPieces.size()){
                drawableRectFs.add(new LayoutData.DrawableVO(layoutPieces.get(i).getDrawableRectF(ratioW, ratioH) ,
                        layoutPieces.get(i).getMatrixAngle() ,layoutPieces.get(i).getMatrixTransX(),
                        layoutPieces.get(i).getMatrixTransY(),layoutPieces.get(i).getMatrixScale()));
            }
        }
        layoutData.setDrawableVOS(drawableRectFs);
        layoutData.setRect(rectVOS);
        return layoutData;
    }



    public void reset() {
        mAreaHashMap.clear();
        mLayoutAreaList.clear();
        mAllHorLineList.clear();
        mAllVerLineList.clear();
        mLineList.clear();
        mOneLineList.clear();
        mAxisList.clear();
    }
}
