package com.xs.lightpuzzle.puzzle.adapter;

import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;

import com.xs.lightpuzzle.data.FontManager;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.data.entity.Template;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.puzzle.data.BgTextureData;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.TemplateData;
import com.xs.lightpuzzle.puzzle.data.lowdata.FgData;
import com.xs.lightpuzzle.puzzle.data.lowdata.ImgPointData;
import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;
import com.xs.lightpuzzle.puzzle.data.lowdata.VariableFgData;
import com.xs.lightpuzzle.puzzle.data.lowdata.WaterMarkData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by xs on 2018/11/21.
 */

public class PuzzleDataAdapter {


    public static TemplateData getTemplateData(TemplateSet templateSet, int photoNum) {

        Template template = templateSet.getTemplateMap().get(photoNum);
        String dirPath = template.getDirPath();
        int[] rectSize = {template.getWidth(), template.getHeight()};
        TemplateData templateData = new TemplateData();

        templateData.setJsonId(templateSet.getId());

        //设置宽高比
        templateData.setSizeRatio(templateSet.getUiRatio());

        //设置输出尺寸
        templateData.setOutPutWidth(rectSize[0]);
        templateData.setOutPutHeight(rectSize[1]);

        //设置图片坐标
        templateData.setImgPointDatas(getImagePointData(template.getPhotos()));

        //设置前景图
        if (!TextUtils.isEmpty(template.getForegroundFileName())) {
            FgData fgData = new FgData();
            String foregroundFileName = template.getForegroundFileName();
            String foregroundFilePath = dirPath + File.separator + foregroundFileName;
            fgData.setFgPic(foregroundFilePath);
            templateData.setFgData(fgData);
        }

        //设置遮罩
        if (!TextUtils.isEmpty(template.getForegroundMaskFileName())) {

            String maskFileName = template.getForegroundMaskFileName();
            String maskFilePath = dirPath + File.separator + maskFileName;

            templateData.setMaskPic(maskFilePath);
        }

        //设置水印
        WaterMarkData waterMarkData = getWaterMarkData(template.getWatermark());
        if (waterMarkData != null) {
            templateData.setWaterMarkData(waterMarkData);
        }

        //设置可变色前景
        List<VariableFgData> variableFgData = getVariableFgData(template.getOrnaments(), dirPath);
        if (variableFgData != null) {
            templateData.setVarFgDatas(variableFgData);
        }

        //设置二维码

        //设置文字信息
        List<TextData> textDataList = getTextData(templateSet.getAttachedFontIdSet(), template.getTexts());
        if (textDataList != null) {
            templateData.setTextData(textDataList);
        }

        //开启自动美化功能
        templateData.setAutoBeautify(true);

        return templateData;
    }

    private static List<TextData> getTextData(Set<String> attachedFontIdSet, List<Template.Text> texts) {

        List<TextData> textDatas = new ArrayList<>();
        ArrayList<String> fontIdArr = new ArrayList<>();
        for (String fontId : attachedFontIdSet) {
            fontIdArr.add(fontId);
        }
        int index = 0;
        for (Template.Text text : texts) {
            TextData textData = new TextData();

            textData.setLineSpace((int) text.getLineSpacing());
            textData.setTopLineDistance((int) text.getPaddingTop());
//            textData.setMaxHeight((int) text.g);
            textData.setFontColor(text.getTextColor());
//            textData.setMaxCount(text.get);
            textData.setMinFontSize((int) text.getMinTextSize());
            textData.setMaxFontSize((int) text.getMaxTextSize());
            textData.setLayoutHeight((int) text.getRegion().height());
            textData.setLayoutWidth((int) text.getRegion().width());
            textData.setAlignment(text.getAlignment());
            textData.setAutoStr(text.getText());

            //font
            Font font = FontManager.get(text.getTypefaceId());
            if (font != null) {
                textData.setFont(font.getFileName());
                textData.setDownload(true);
            }
            String typefaceId = text.getTypefaceId();
            String typefaceUri = FontManager.getTypefaceUri(typefaceId);
            textData.setTypeface(typefaceId, typefaceUri);
            PointF[] textPoint = new PointF[4];
            if (text.getRegion() != null) {
                textPoint[0] = new PointF(text.getRegion().left, text.getRegion().top);
                textPoint[1] = new PointF(text.getRegion().right, text.getRegion().top);
                textPoint[2] = new PointF(text.getRegion().right, text.getRegion().bottom);
                textPoint[3] = new PointF(text.getRegion().left, text.getRegion().bottom);
            }
            textData.setPolygons(textPoint);
            textDatas.add(textData);

            index++;
        }
        return textDatas;
    }

    private static List<VariableFgData> getVariableFgData(List<Template.Ornament> ornaments, String dirPath) {

        List<VariableFgData> variableFgDatas = new ArrayList<>();
        if (ornaments == null) {
            return variableFgDatas;
        }
        for (Template.Ornament ornament : ornaments) {
            VariableFgData fgData = new VariableFgData();

            RectF rect = ornament.getRegion();
            String fileName = ornament.getFileName();

            if (!TextUtils.isEmpty(fileName) && !("none").equals(fileName)) {
                String path = dirPath + File.separator + fileName;
                fgData.setVarFgPic(path);
            }

            PointF[] pointFS = new PointF[4];
            pointFS[0] = new PointF(rect.left, rect.top);
            pointFS[1] = new PointF(rect.right, rect.top);
            pointFS[2] = new PointF(rect.right, rect.bottom);
            pointFS[3] = new PointF(rect.left, rect.bottom);
            fgData.setVarFgPoint(pointFS);

            variableFgDatas.add(fgData);
        }
        return variableFgDatas;
    }

    private static WaterMarkData getWaterMarkData(Template.Watermark watermark) {

        WaterMarkData waterMarkData = new WaterMarkData();

        RectF rect = watermark.getRegion();
        String path = watermark.getFileName();

        if (!TextUtils.isEmpty(path) && !("none").equals(path)) {
            waterMarkData.setWaterPic(path);
        }

        PointF[] waterPoint = new PointF[4];
        waterPoint[0] = new PointF(rect.left, rect.top);
        waterPoint[1] = new PointF(rect.right, rect.top);
        waterPoint[2] = new PointF(rect.right, rect.bottom);
        waterPoint[3] = new PointF(rect.left, rect.bottom);
        waterMarkData.setWaterPoint(waterPoint);

        return waterMarkData;
    }

    private static List<ImgPointData> getImagePointData(
            List<Template.Photo> photos) {
        List<ImgPointData> imgPointDataList = new ArrayList<>();

        PointF[] imgPoint;
        for (Template.Photo photo : photos) {
            ImgPointData imgPointData = new ImgPointData();
            if (photo.getRegionPathPointArr() != null) {

//                PointF[] pathPointArr = photo.getRegionPathPointArr();
//                imgPoint = new PointF[pathPointArr.length];
//                for (int j = 0; j < pathPointArr.length; j++) {
//                    imgPoint[j] = new PointF(pathPointArr[j].x / (float) rectSize[0],
//                            pathPointArr[j].y / (float) rectSize[1]);
//                }

                imgPointData.setPicPointF(photo.getRegionPathPointArr());
            } else {
                imgPoint = new PointF[4];
                RectF rect = photo.getRegion();

                imgPoint[0] = new PointF(rect.left, rect.top);
                imgPoint[1] = new PointF(rect.right, rect.top);
                imgPoint[2] = new PointF(rect.right, rect.bottom);
                imgPoint[3] = new PointF(rect.left, rect.bottom);

                imgPointData.setPicPointF(imgPoint);
            }

            imgPointDataList.add(imgPointData);
        }
        return imgPointDataList;
    }

    public static BgTextureData getBgTextureData(Template.Background background) {
        BgTextureData bgTextureData = new BgTextureData();

        bgTextureData.setBgColor(background.getColor());
//        bgTextureData.setTexture(background.getTextureId());

        return bgTextureData;
    }

    public static RotationImg[] toRotationImgs(ArrayList<String> photoPaths) {
        RotationImg[] rotationImgArr = new RotationImg[photoPaths.size()];
        for (int i = 0; i < photoPaths.size(); i++) {
            RotationImg rotationImg = new RotationImg();
            rotationImg.setPicPath(photoPaths.get(i));
            //旋转角度
            rotationImgArr[i] = rotationImg;
        }
        return rotationImgArr;
    }
}
