package com.xs.lightpuzzle.data.entity.adapter;

import android.graphics.PointF;
import android.graphics.RectF;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xs on 2018/11/6.
 */

public class TemplateAdapter {

    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    @SerializedName("background")
    private BackgroundAdapter backgroundAdapter;

    @SerializedName("photo_num")
    private int photoNum;

    @SerializedName("thumb_file_name")
    private String thumbFileName;

    @SerializedName("photo_list")
    private List<PhotoAdapter> photoAdapters;

    @SerializedName("text_list")
    private List<TextAdapter> textAdapters;

    @SerializedName("foreground_file_name")
    private String foregroundFileName;

    @SerializedName("foreground_mask_file_name")
    private String foregroundMaskFileName;

    @SerializedName("ornament_list")
    private List<OrnamentAdapter> ornamentAdapters; // 可变前景，装饰前景

    @SerializedName("watermark")
    private WatermarkAdapter watermarkAdapter;

    @SerializedName("avatar")
    private AvatarAdapter avatarAdapter; // 头像

    @SerializedName("qr_code")
    private QrCodeAdapter qrCodeAdapter;

    @SerializedName("business_card")
    private BusinessCardAdapter businessCardAdapter;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BackgroundAdapter getBackgroundAdapter() {
        return backgroundAdapter;
    }

    public void setBackgroundAdapter(BackgroundAdapter backgroundAdapter) {
        this.backgroundAdapter = backgroundAdapter;
    }

    public int getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(int photoNum) {
        this.photoNum = photoNum;
    }

    public String getThumbFileName() {
        return thumbFileName;
    }

    public void setThumbFileName(String thumbFileName) {
        this.thumbFileName = thumbFileName;
    }

    public List<PhotoAdapter> getPhotoAdapters() {
        return photoAdapters;
    }

    public void setPhotoAdapters(List<PhotoAdapter> photoAdapters) {
        this.photoAdapters = photoAdapters;
    }

    public List<TextAdapter> getTextAdapters() {
        return textAdapters;
    }

    public void setTextAdapters(List<TextAdapter> textAdapters) {
        this.textAdapters = textAdapters;
    }

    public String getForegroundFileName() {
        return foregroundFileName;
    }

    public void setForegroundFileName(String foregroundFileName) {
        this.foregroundFileName = foregroundFileName;
    }

    public String getForegroundMaskFileName() {
        return foregroundMaskFileName;
    }

    public void setForegroundMaskFileName(String foregroundMaskFileName) {
        this.foregroundMaskFileName = foregroundMaskFileName;
    }

    public List<OrnamentAdapter> getOrnamentAdapters() {
        return ornamentAdapters;
    }

    public void setOrnamentAdapters(List<OrnamentAdapter> ornamentAdapters) {
        this.ornamentAdapters = ornamentAdapters;
    }

    public WatermarkAdapter getWatermarkAdapter() {
        return watermarkAdapter;
    }

    public void setWatermarkAdapter(WatermarkAdapter watermarkAdapter) {
        this.watermarkAdapter = watermarkAdapter;
    }

    public AvatarAdapter getAvatarAdapter() {
        return avatarAdapter;
    }

    public void setAvatarAdapter(AvatarAdapter avatarAdapter) {
        this.avatarAdapter = avatarAdapter;
    }

    public QrCodeAdapter getQrCodeAdapter() {
        return qrCodeAdapter;
    }

    public void setQrCodeAdapter(QrCodeAdapter qrCodeAdapter) {
        this.qrCodeAdapter = qrCodeAdapter;
    }

    public BusinessCardAdapter getBusinessCardAdapter() {
        return businessCardAdapter;
    }

    public void setBusinessCardAdapter(BusinessCardAdapter businessCardAdapter) {
        this.businessCardAdapter = businessCardAdapter;
    }

    public static class BackgroundAdapter {

        @SerializedName("color")
        private int color;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    public static class PhotoAdapter {

        @SerializedName("region")
        private RectF region;
        @SerializedName("region_path_point_list")
        private PointF[] regionPathPointArr;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }

        public PointF[] getRegionPathPointArr() {
            return regionPathPointArr;
        }

        public void setRegionPathPointArr(PointF[] regionPathPointArr) {
            this.regionPathPointArr = regionPathPointArr;
        }
    }

    public static class OrnamentAdapter {

        @SerializedName("region")
        private RectF region;
        @SerializedName("file_name")
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }
    }

    public static class TextAdapter {

        @SerializedName("region")
        private RectF region;

        @SerializedName("text")
        private String text;
        @SerializedName("text_color")
        private int textColor;

        @SerializedName("typeface_id")
        private String typefaceId;

        @SerializedName("min_text_size")
        private float minTextSize;
        @SerializedName("max_text_size")
        private float maxTextSize;

        @SerializedName("alignment")
        private String alignment;

        @SerializedName("padding_top")
        private float paddingTop;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public String getTypefaceId() {
            return typefaceId;
        }

        public void setTypefaceId(String typefaceId) {
            this.typefaceId = typefaceId;
        }

        public float getMinTextSize() {
            return minTextSize;
        }

        public void setMinTextSize(float minTextSize) {
            this.minTextSize = minTextSize;
        }

        public float getMaxTextSize() {
            return maxTextSize;
        }

        public void setMaxTextSize(float maxTextSize) {
            this.maxTextSize = maxTextSize;
        }

        public String getAlignment() {
            return alignment;
        }

        public void setAlignment(String alignment) {
            this.alignment = alignment;
        }

        public float getPaddingTop() {
            return paddingTop;
        }

        public void setPaddingTop(float paddingTop) {
            this.paddingTop = paddingTop;
        }

    }

    public static class WatermarkAdapter {

        @SerializedName("region")
        private RectF region;
        @SerializedName("file_name")
        private String fileName;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    public static class AvatarAdapter {
        @SerializedName("region")
        private RectF region;
        @SerializedName("text")
        private TextAdapter textAdapter;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }

        public TextAdapter getTextAdapter() {
            return textAdapter;
        }

        public void setTextAdapter(TextAdapter textAdapter) {
            this.textAdapter = textAdapter;
        }
    }

    public static class QrCodeAdapter {

        @SerializedName("region")
        private RectF region;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }
    }

    public static class BusinessCardAdapter {

        @SerializedName("region")
        private RectF region;
        @SerializedName("text_color")
        private int textColor;
        @SerializedName("item_tint_color")
        private int itemTintColor;
        @SerializedName("max_item_count")
        private int maxItemCount;
        @SerializedName("item_type")
        private int itemType;
        @SerializedName("item_file_name_list")
        private List<String> itemFileNames;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public int getItemTintColor() {
            return itemTintColor;
        }

        public void setItemTintColor(int itemTintColor) {
            this.itemTintColor = itemTintColor;
        }

        public int getMaxItemCount() {
            return maxItemCount;
        }

        public void setMaxItemCount(int maxItemCount) {
            this.maxItemCount = maxItemCount;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public List<String> getItemFileNames() {
            return itemFileNames;
        }

        public void setItemFileNames(List<String> itemFileNames) {
            this.itemFileNames = itemFileNames;
        }
    }
}
