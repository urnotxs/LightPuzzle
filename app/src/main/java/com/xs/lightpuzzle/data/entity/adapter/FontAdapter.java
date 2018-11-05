package com.xs.lightpuzzle.data.entity.adapter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xs on 2018/11/5.
 */

public class FontAdapter {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private int type;
    @SerializedName("size")
    private int size;
    @SerializedName("url")
    private String url;
    @SerializedName("thumb_url")
    private String thumbUrl;

    @SerializedName("order")
    private long order;

    @SerializedName("file_name")
    private String fileName;
    @SerializedName("thumb_file_name")
    private String thumbFileName;

    public FontAdapter() {
        // no-op
    }

    public FontAdapter(String id, String name, int type, int size, String url, String thumbUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.url = url;
        this.thumbUrl = thumbUrl;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbFileName() {
        return thumbFileName;
    }

    public void setThumbFileName(String thumbFileName) {
        this.thumbFileName = thumbFileName;
    }
}
