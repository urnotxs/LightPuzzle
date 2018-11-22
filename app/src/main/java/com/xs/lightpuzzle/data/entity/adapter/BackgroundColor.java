package com.xs.lightpuzzle.data.entity.adapter;

import com.google.gson.annotations.SerializedName;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @since 1.0.0
 */
public class BackgroundColor {

    @SerializedName("color")
    private int color; // as id
    @SerializedName("order")
    private long order;
    @SerializedName("icon_normal_url")
    private String iconNormalUrl;
    @SerializedName("icon_checked_url")
    private String iconCheckedUrl;

    public BackgroundColor() {
    }

    public BackgroundColor(int color, long order, String iconNormalUrl, String iconCheckedUrl) {
        this.color = color;
        this.order = order;
        this.iconNormalUrl = iconNormalUrl;
        this.iconCheckedUrl = iconCheckedUrl;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
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
