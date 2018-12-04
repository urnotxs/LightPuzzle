package com.xs.lightpuzzle.materials.layout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @version 1.0
 * @function 用于retrofit2接受数据
 * @since 17/7/25
 */
public class PreviewAdapter {

    @SerializedName("file_tracking_id")
    private String previewId;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private int type;
    @SerializedName("restype")
    private String categoryName;
    @SerializedName("restype_id")
    private int category;
    @SerializedName("order")
    private int order;
    @SerializedName("tracking_code")
    private int statisticsCode;
    @SerializedName("tj_url")
    private String statisticsUrl;
    @SerializedName("tags")
    private String tags;
    @SerializedName("thumb_80")
    private String smallThumbUrl;
    @SerializedName("thumb_120")
    private String thumbUrl;
    @SerializedName("size")
    private int size;
    @SerializedName("measure")
    private String measure;

    @SerializedName("needFontId")
    private String requisiteFontIdsStr;
    @SerializedName("needMusicId")
    private String requisiteMusicIdsStr;

    @SerializedName("res_arr")
    private List<PuzzleResourceAdapter> puzzleResourceAdapters;

    @SerializedName("reddot_type")
    private String label;

    @SerializedName("status_name")
    private String statusName;

    @SerializedName("relatid")
    private String relateId;

    @SerializedName("start_second")
    private String musicStartSecond;

    public PreviewAdapter() {
        // no-op
    }

    public PreviewAdapter(String previewId, String name, int type, String categoryName, int category, int order,
                          int statisticsCode, String statisticsUrl, String tags, String smallThumbUrl,
                          String thumbUrl, int size, String measure, String requisiteFontIdsStr,
                          String requisiteMusicIdsStr,
                          List<PuzzleResourceAdapter> puzzleResourceAdapters, String label, String statusName, String relateId,
                          String musicStartSecond) {
        this.previewId = previewId;
        this.name = name;
        this.type = type;
        this.categoryName = categoryName;
        this.category = category;
        this.order = order;
        this.statisticsCode = statisticsCode;
        this.statisticsUrl = statisticsUrl;
        this.tags = tags;
        this.smallThumbUrl = smallThumbUrl;
        this.thumbUrl = thumbUrl;
        this.size = size;
        this.measure = measure;
        this.requisiteFontIdsStr = requisiteFontIdsStr;
        this.requisiteMusicIdsStr = requisiteMusicIdsStr;
        this.puzzleResourceAdapters = puzzleResourceAdapters;
        this.label = label;
        this.statusName = statusName;
        this.relateId = relateId;
        this.musicStartSecond = musicStartSecond;
    }

    public String getPreviewId() {
        return previewId;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(int statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public String getStatisticsUrl() {
        return statisticsUrl;
    }

    public void setStatisticsUrl(String statisticsUrl) {
        this.statisticsUrl = statisticsUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSmallThumbUrl() {
        return smallThumbUrl;
    }

    public void setSmallThumbUrl(String smallThumbUrl) {
        this.smallThumbUrl = smallThumbUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getRequisiteFontIdsStr() {
        return requisiteFontIdsStr;
    }

    public void setRequisiteFontIdsStr(String requisiteFontIdsStr) {
        this.requisiteFontIdsStr = requisiteFontIdsStr;
    }

    public String getRequisiteMusicIdsStr() {
        return requisiteMusicIdsStr;
    }

    public void setRequisiteMusicIdsStr(String requisiteMusicIdsStr) {
        this.requisiteMusicIdsStr = requisiteMusicIdsStr;
    }

    public List<PuzzleResourceAdapter> getPuzzleResourceAdapters() {
        return puzzleResourceAdapters;
    }

    public void setPuzzleResourceAdapters(
            List<PuzzleResourceAdapter> puzzleResourceAdapters) {
        this.puzzleResourceAdapters = puzzleResourceAdapters;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public String getMusicStartSecond() {
        return musicStartSecond;
    }

    public void setMusicStartSecond(String musicStartSecond) {
        this.musicStartSecond = musicStartSecond;
    }
}
