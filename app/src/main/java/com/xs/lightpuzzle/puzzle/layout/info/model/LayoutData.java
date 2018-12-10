package com.xs.lightpuzzle.puzzle.layout.info.model;

import android.graphics.PointF;
import android.graphics.RectF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by urnot_XS on 2017/12/12.
 * 编辑页布局相关
 */

public class LayoutData {

    /**
     * Created by admin on 2017/12/18.
     */

    /**
     * w : 2436
     * h : 2436
     * rect : [[0,0,812,812],[812,0,812,812],[1624,0,812,812],[0,812,812,1624],[812,812,812,812],[1624,812,812,812],[812,1624,812,812],[1624,1624,812,812]]
     */

    private float width;
    private float height;
    private float insidePaddingRatio;
    private float outsidePaddingRatio;
    private float radian;
    private ArrayList<DrawableVO> drawableVOS;
    private ArrayList<RectVO> rectList;



    public float getInsidePaddingRatio() {
        return insidePaddingRatio;
    }

    public void setInsidePaddingRatio(float insidePaddingRatio) {
        this.insidePaddingRatio = insidePaddingRatio;
    }

    public float getOutsidePaddingRatio() {
        return outsidePaddingRatio;
    }

    public void setOutsidePaddingRatio(float outsidePaddingRatio) {
        this.outsidePaddingRatio = outsidePaddingRatio;
    }

    public float getRadian() {
        return radian;
    }

    public void setRadianRatio(float radian) {
        this.radian = radian;
    }


    public float getW() {
        return width;
    }

    public void setW(float width) {
        this.width = width;
    }

    public float getH() {
        return height;
    }

    public void setH(float height) {
        this.height = height;
    }

    public ArrayList<DrawableVO> getDrawableVOS() {
        return drawableVOS;
    }

    public void setDrawableVOS(ArrayList<DrawableVO> drawableVOS) {
        this.drawableVOS = drawableVOS;
    }

    public ArrayList<RectVO> getRect() {
        return rectList;
    }

    public void setRect(ArrayList<RectVO> rectList) {
        this.rectList = rectList;
    }

    public static class DrawableVO{
        private RectF drawableRect;
        private float degree;
        private float transX;
        private float transY;
        private float scale;
        public DrawableVO(){

        }

        public DrawableVO(RectF rectF , float degree , float transX , float transY , float scale){
            this.drawableRect =  rectF;
            this.degree = degree;
            this.transX = transX;
            this.transY = transY;
            this.scale = scale;

        }

        public RectF getDrawableRect() {
            return drawableRect;
        }

        public void setDrawableRect(RectF drawableRect) {
            this.drawableRect = drawableRect;
        }

        public float getDegree() {
            return degree;
        }

        public void setDegree(float degree) {
            this.degree = degree;
        }

        public float getTransX() {
            return transX;
        }

        public void setTransX(float transX) {
            this.transX = transX;
        }

        public float getTransY() {
            return transY;
        }

        public void setTransY(float transY) {
            this.transY = transY;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }
    }

    public static class RectVO{
        //左上角 以及 宽高
        private float xPosition;
        private float yPosition;
        private float width;
        private float height;
        public RectVO(){

        }
        public RectVO(float xPosition, float yPosition, float width, float height) {
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            this.width = width;
            this.height = height;
        }

        public float getxPosition() {
            return xPosition;
        }

        public void setxPosition(float xPosition) {
            this.xPosition = xPosition;
        }

        public float getyPosition() {
            return yPosition;
        }

        public void setyPosition(float yPosition) {
            this.yPosition = yPosition;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }
    }

    public LayoutData generateLayoutData(float standSize, List<PointF[]> imgPoints) {

        LayoutData layoutData = new LayoutData();
        layoutData.setRadianRatio(0);
        layoutData.setInsidePaddingRatio(0.5f);
        layoutData.setOutsidePaddingRatio(0.5f);
        layoutData.setW(standSize);
        layoutData.setH(standSize);
        ArrayList<RectVO> rectArrayList = new ArrayList<>();
        for (int i = 0; i < imgPoints.size(); i++) {
            float l = imgPoints.get(i)[0].x;
            float t = imgPoints.get(i)[0].y;
            float w = imgPoints.get(i)[1].x;
            float h = imgPoints.get(i)[1].y;
            rectArrayList.add(new LayoutData.RectVO(l, t, w, h));
        }
        layoutData.setRect(rectArrayList);

        return layoutData;
    }

    public LayoutData jsonToLayoutData(String json){
        LayoutData layoutData = new LayoutData();
        try {
            JSONObject jsonObject = new JSONObject(json);
            ArrayList<RectVO> rectVOList = new ArrayList<>();
            layoutData.setW(jsonObject.getInt("w"));
            layoutData.setH(jsonObject.getInt("h"));
            layoutData.setInsidePaddingRatio(0);
            layoutData.setOutsidePaddingRatio(0);
            layoutData.setRadianRatio(0);
            JSONArray jsonArray = jsonObject.getJSONArray("rect");
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONArray jArray = (JSONArray) jsonArray.get(i);
                LayoutData.RectVO rectVO = new LayoutData.RectVO();
                rectVO.setxPosition((Integer) jArray.get(0));
                rectVO.setyPosition((Integer) jArray.get(1));
                rectVO.setWidth((Integer) jArray.get(2));
                rectVO.setHeight((Integer) jArray.get(3));
                rectVOList.add(rectVO);
            }
            layoutData.setRect(rectVOList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return layoutData;
    }

    public void LayoutDataToJson(LayoutData layoutData){

    }
}
/**        LayoutData layoutData = mPuzzleInfo.getPuzzleDatas().get(0).getTemplateInfo().mPolygonLayoutInfo.mLayoutParameter.switchAreaToJson();
 layoutData.setRadian(50);
 layoutData.setOutsidePadding(50);
 mPuzzleInfo.getPuzzleDatas().get(0).getTemplateInfo().mPolygonLayoutInfo.setLayoutData(layoutData);
 mCallBack.onInvalidate(null , null);*/
