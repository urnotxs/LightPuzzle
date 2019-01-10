package com.xs.lightpuzzle.data.entity;

import com.xs.lightpuzzle.constant.DirConstant;
import com.xs.lightpuzzle.data.TemplateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/13.
 */

public class TemplateMapper {

    private TemplateSet templateSet;
    private List<String> photoFilePaths; // can't be null but could be empty -> photo num is 0

    public TemplateMapper(TemplateSet templateSet, List<String> photoFilePaths) {
        this.templateSet = templateSet;
        if (photoFilePaths == null || photoFilePaths.isEmpty()) {
            photoFilePaths = new ArrayList<>();
            int minPhotoNum = templateSet.getMinPhotoNum();
            for (int i = 0; minPhotoNum > 0 && i < minPhotoNum; i++) {
                photoFilePaths.add(DirConstant.ASSETS_PLACEHOLDER_URI.PHOTO);
            }
        }
        this.photoFilePaths = photoFilePaths;
    }

    public static TemplateMapper get(int category, String id) {
        return get(category, id, null);
    }

    public static TemplateMapper get(int category, String id, List<String> photoFilePaths) {
        return get(TemplateManager.get(category, id), photoFilePaths);
    }

    public static TemplateMapper get(TemplateSet templateSet, List<String> photoFilePaths) {
        return new TemplateMapper(templateSet, photoFilePaths);
    }

    public TemplateSet getTemplateSet() {
        return templateSet;
    }

    public void setTemplateSet(TemplateSet templateSet) {
        this.templateSet = templateSet;
    }

    public List<String> getPhotoFilePaths() {
        return photoFilePaths;
    }

    public void setPhotoFilePaths(List<String> photoFilePaths) {
        this.photoFilePaths = photoFilePaths;
    }
}
