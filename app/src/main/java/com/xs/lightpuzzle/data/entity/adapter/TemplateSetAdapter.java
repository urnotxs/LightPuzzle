package com.xs.lightpuzzle.data.entity.adapter;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Set;

/**
 * Created by xs on 2018/11/6.
 */

public class TemplateSetAdapter {
    public static class NodeAdapter {

        public static NodeAdapter create(int category, String id) {
            return new NodeAdapter(category, id);
        }

        @SerializedName("category")
        private final int category;
        @SerializedName("id")
        private final String id;

        public NodeAdapter(int category, String id) {
            this.category = category;
            this.id = id;
        }

        public int getCategory() {
            return category;
        }

        public String getId() {
            return id;
        }
    }

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private int category;

    @SerializedName("proportion")
    private int proportion;

    @SerializedName("url")
    private String url;

    @SerializedName("thumb_url")
    private String thumbUrl;
    @SerializedName("thumb_file_name")
    private String thumbFileName;

    @SerializedName("min_photo_num")
    private int minPhotoNum;
    @SerializedName("max_photo_num")
    private int maxPhotoNum;

    @SerializedName("thumb_file_name_list")
    private Map<Integer, String> thumbFileNameMap;

    @SerializedName("template_list")
    private Map<Integer, TemplateAdapter> templateAdapterMap;

    @SerializedName("attached_font_id_list")
    private Set<String> attachedFontIdSet;

    @SerializedName("order")
    private long order;
    @SerializedName("ui_ratio")
    private float uiRatio;

    @SerializedName("attached_node_list")
    private Map<Integer, NodeAdapter> attachedNodeMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbFileName() {
        return thumbFileName;
    }

    public void setThumbFileName(String thumbFileName) {
        this.thumbFileName = thumbFileName;
    }

    public int getMinPhotoNum() {
        return minPhotoNum;
    }

    public void setMinPhotoNum(int minPhotoNum) {
        this.minPhotoNum = minPhotoNum;
    }

    public int getMaxPhotoNum() {
        return maxPhotoNum;
    }

    public void setMaxPhotoNum(int maxPhotoNum) {
        this.maxPhotoNum = maxPhotoNum;
    }

    public Map<Integer, String> getThumbFileNameMap() {
        return thumbFileNameMap;
    }

    public void setThumbFileNameMap(Map<Integer, String> thumbFileNameMap) {
        this.thumbFileNameMap = thumbFileNameMap;
    }

    public Map<Integer, TemplateAdapter> getTemplateAdapterMap() {
        return templateAdapterMap;
    }

    public void setTemplateAdapterMap(Map<Integer, TemplateAdapter> templateAdapterMap) {
        this.templateAdapterMap = templateAdapterMap;
    }

    public Set<String> getAttachedFontIdSet() {
        return attachedFontIdSet;
    }

    public void setAttachedFontIdSet(Set<String> attachedFontIdSet) {
        this.attachedFontIdSet = attachedFontIdSet;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public float getUiRatio() {
        return uiRatio;
    }

    public void setUiRatio(float uiRatio) {
        this.uiRatio = uiRatio;
    }

    public Map<Integer, NodeAdapter> getAttachedNodeMap() {
        return attachedNodeMap;
    }

    public void setAttachedNodeMap(Map<Integer, NodeAdapter> attachedNodeMap) {
        this.attachedNodeMap = attachedNodeMap;
    }
}
