package com.xs.lightpuzzle.puzzle.adapter;

import android.graphics.Color;
import android.graphics.PointF;

import com.xs.lightpuzzle.LightPuzzleApplication;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.data.entity.Template;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.puzzle.layout.data.BasePuzzleInfo;
import com.xs.lightpuzzle.yszx.AssetManagerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xs on 2018/12/11.
 */

public class LayoutDataAdapter {

    public static TemplateSet get(String templateId, float templateRatio){

        String name = "/BasePuzzle" + templateId + ".json";
        String data = AssetManagerHelper.convertInputString(LightPuzzleApplication.getContext(),
                DataConstant.ASSETS_DATA_LAYOUT.LAYOUT + templateId + name);

        BasePuzzleInfo info = BasePuzzleInfo.JsonStringToInfo(data);

        Template template = new Template();
        TemplateSet templateSet = new TemplateSet();

        if (info == null){
            return null;
        }
        template.setWidth(Integer.parseInt(info.getPic_w()));
        template.setHeight(Integer.parseInt(info.getPic_h()));
        template.setBackground(new Template.Background(Color.parseColor("#" + info.getBgcolor())));
        template.setPhotoNum(Integer.parseInt(info.getMinPicNum()));
        ArrayList<Template.Photo> photos = new ArrayList<>();
        List<String> points = info.getPoint().get(String.valueOf(info.getMaxPicNum()));
        for (int index = 0; index < points.size(); index++) {

            Template.Photo photo = new Template.Photo();
            String[] stringArr = points.get(index).split(",");
            photo.setRegionPathPointArr(new PointF[]{
                    new PointF(Float.parseFloat(stringArr[0]), Float.parseFloat(stringArr[1])),
                    new PointF(Float.parseFloat(stringArr[2]), Float.parseFloat(stringArr[3]))
            });
            photos.add(photo);
        }
        template.setPhotos(photos);

        templateSet.setId(templateId);
        templateSet.setMinPhotoNum(Integer.parseInt(info.getMinPicNum()));
        templateSet.setMaxPhotoNum(Integer.parseInt(info.getMaxPicNum()));
        templateSet.setUiRatio(templateRatio);
        templateSet.setCategory(DataConstant.TEMPLATE_CATEGORY.LAYOUT);
        Map<Integer, Template> map = new HashMap<>();
        map.put(Integer.parseInt(info.getMinPicNum()), template);
        templateSet.setTemplateMap(map);

        return templateSet;
    }
}
