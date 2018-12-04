package com.xs.lightpuzzle.data.entity;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xs on 2018/11/6.
 */

public class Template {

    private int width;
    private int height;

    private Background background;

    private int photoNum;
    private String thumbFileName;
    private List<Photo> photos;
    private List<Text> texts;
    private String foregroundFileName;
    private String foregroundMaskFileName;
    private List<Ornament> ornaments; // 可变前景，装饰前景

    private Watermark watermark;
    private Avatar avatar;
    private QrCode qrCode;
    private BusinessCard businessCard;

    // --- Local

    private String dirPath;

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

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
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

    public List<Ornament> getOrnaments() {
        return ornaments;
    }

    public void setOrnaments(List<Ornament> ornaments) {
        this.ornaments = ornaments;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void setWatermark(Watermark watermark) {
        this.watermark = watermark;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public BusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    // --- Local

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    // ---

    public static class Background {
        public static final int INVALID_TEXTURE_ID = -1;

        private static final int DEFAULT_COLOR = 0xFFFFFFFF;
        private static final int DEFAULT_TEXTURE_ID = INVALID_TEXTURE_ID;

        @SerializedName("color")
        private int color = DEFAULT_COLOR;
        @SerializedName("texture_id")
        private int textureId = DEFAULT_TEXTURE_ID;

        public Background() {
        }

        public Background(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getTextureId() {
            return textureId;
        }

        public void setTextureId(int textureId) {
            this.textureId = textureId;
        }
    }

    public static class Photo {
        private static final int DEFAULT_ROTATION = 1;
        private static final float DEFAULT_SCALE = 1.0F;

        private RectF region;
        private PointF[] regionPathPointArr;

        public Photo() {
            // no-op by default
        }

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

    public static class Text {
        private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
        private static final String DEFAULT_ALIGNMENT = ALIGNMENT.LEFT;
        private static final float DEFAULT_LINE_SPACING = 0.0F;
        private static final float DEFAULT_PADDING_TOP = 0.0F;

        public interface ALIGNMENT {
            String LEFT = "LEFT";
            String CENTER = "CENTER";
            String RIGHT = "RIGHT";
        }

        private RectF region;
        private String text;
        private int textColor = DEFAULT_TEXT_COLOR;

        private float textSize; // relationship with current text

        private float minTextSize;
        private float maxTextSize;

        private String alignment = DEFAULT_ALIGNMENT;

        private float paddingTop = DEFAULT_PADDING_TOP;
        private float lineSpacing = DEFAULT_LINE_SPACING;

        private String typefaceId;// 文本类型，eg:标题，正文...

        public Text() {
            // no-op by default
        }

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

        public float getTextSize() {
            return textSize;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
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

        public float getLineSpacing() {
            return lineSpacing;
        }

        public void setLineSpacing(float lineSpacing) {
            this.lineSpacing = lineSpacing;
        }

        public String getTypefaceId() {
            return typefaceId;
        }

        public void setTypefaceId(String typefaceId) {
            this.typefaceId = typefaceId;
        }
    }

    public static class Ornament {

        private RectF region;
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

    public static class Watermark {
        private RectF region;
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

    public static class Avatar {

        private RectF region;
        private Text text;
        private String filePath;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    public static class BusinessCard {

        private RectF region;
        private int textColor;
        private int itemTintColor;
        private int maxItemCount;
        private int itemType;
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

    public static class QrCode {

        private RectF region;
        private String filePath;

        public RectF getRegion() {
            return region;
        }

        public void setRegion(RectF region) {
            this.region = region;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
