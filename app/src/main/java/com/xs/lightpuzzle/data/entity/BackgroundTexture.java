package com.xs.lightpuzzle.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @since 1.0.0
 */
public class BackgroundTexture {

    @SerializedName("id")
    private long id;
    @SerializedName("order")
    private long order;
    @SerializedName("url")
    private String url;
    @SerializedName("icon_normal_url")
    private String iconNormalUrl;
    @SerializedName("icon_checked_url")
    private String iconCheckedUrl;

    public BackgroundTexture() {
    }

    public BackgroundTexture(long id, long order, String url, String iconNormalUrl,
                             String iconCheckedUrl) {
        this.id = id;
        this.order = order;
        this.url = url;
        this.iconNormalUrl = iconNormalUrl;
        this.iconCheckedUrl = iconCheckedUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconNormalUrl() {
        return iconNormalUrl;
    }

    public void setIconNormalUrl(String iconNormalUrl) {
        this.iconNormalUrl = iconNormalUrl;
    }

    public String getIconCheckedUrl() {
        return iconCheckedUrl;
    }

    public void setIconCheckedUrl(String iconCheckedUrl) {
        this.iconCheckedUrl = iconCheckedUrl;
    }
}
