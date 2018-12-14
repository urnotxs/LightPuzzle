package com.xs.lightpuzzle.puzzle.view.textedit.model;


/**
 * Created by urnotXS on 2018/4/11.
 */
public class SizeItemInfo {

    private boolean check = false;//是否选中
    //模式1 
    private String text; //字号模式 文字
    private String textFont;//字号模式 文字字体
    private float textSize;//字号模式 文字大小
    private float showTextSize = -1;//ListView上显示的字体大小，不是真实字体的大小

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextFont() {
        return textFont;
    }

    public void setTextFont(String textFont) {
        this.textFont = textFont;
    }

    public float getShowTextSize() {
        return showTextSize;
    }

    public void setShowTextSize(float showTextSize) {
        this.showTextSize = showTextSize;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

}
