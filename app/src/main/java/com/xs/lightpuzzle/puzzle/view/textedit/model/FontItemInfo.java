package com.xs.lightpuzzle.puzzle.view.textedit.model;

import com.xs.lightpuzzle.data.entity.Font;

/**
 * Created by urnotXS on 2018/4/11.
 */
public class FontItemInfo {

    private boolean check = false;//是否选中
    private String imageUrl = null;
    //模式0
    private boolean BarShow = false; //是否显示下载进度
    private boolean downTextShow = false;//是否显示下载字体
    private boolean downLoadBmp = false; //是否需要下载Bmp
    private String DownText;//下载大小
    private boolean needDownFont = false;//是否需要下载字体,一开始下载后为FALSE
    private String font;//字体类型
    private boolean readyDown = false;//准备开始下载
    private boolean transBmp = false;//设置Bmp半透明
    private boolean Downing = false;//是否正在下载中
    private Font fontInfo;

    public Font getFontInfo() {
        return fontInfo;
    }

    public void setFontInfo(Font fontInfo) {
        this.fontInfo = fontInfo;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDowning() {
        return Downing;
    }

    public void setDowning(boolean downing) {
        Downing = downing;
    }

    public boolean isDownTextShow() {
        return downTextShow;
    }

    public void setDownTextShow(boolean downTextShow) {
        this.downTextShow = downTextShow;
    }

    public boolean isreadyDown() {
        return readyDown;
    }

    public void setReadyDown(boolean readyDown) {
        this.readyDown = readyDown;
    }

    public boolean isBarShow() {
        return BarShow;
    }

    public void setBarShow(boolean barShow) {
        BarShow = barShow;
    }

    public String getDownText() {
        return DownText;
    }

    public void setDownText(String downText) {
        DownText = downText;
    }

    public boolean isDownLoadBmp() {
        return downLoadBmp;
    }

    public void setDownLoadBmp(boolean downLoadBmp) {
        this.downLoadBmp = downLoadBmp;
    }

    public boolean isNeedDownFont() {
        return needDownFont;
    }

    public void setNeedDownFont(boolean needDownFont) {
        this.needDownFont = needDownFont;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

}
