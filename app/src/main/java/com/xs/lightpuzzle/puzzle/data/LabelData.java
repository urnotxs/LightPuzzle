package com.xs.lightpuzzle.puzzle.data;

import com.xs.lightpuzzle.puzzle.view.label.view.EditLabelView;

import java.io.Serializable;

/**
 * Created by xs on 2018/5/21.
 */

public class LabelData implements Serializable, Cloneable {
    private String text;
    private String labelPic;
    private boolean isInvert;
    private boolean isUpdate;
    private EditLabelView.ICON_TYPE iconType;
    private EditLabelView.LABEL_TYPE labelType;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabelPic() {
        return labelPic;
    }

    public void setLabelPic(String labelPic) {
        this.labelPic = labelPic;
    }

    public boolean isInvert() {
        return isInvert;
    }

    public void setInvert(boolean invert) {
        isInvert = invert;
    }

    public EditLabelView.ICON_TYPE getIconType() {
        return iconType;
    }

    public void setIconType(EditLabelView.ICON_TYPE iconType) {
        this.iconType = iconType;
    }

    public EditLabelView.LABEL_TYPE getLabelType() {
        return labelType;
    }

    public void setLabelType(EditLabelView.LABEL_TYPE labelType) {
        this.labelType = labelType;
    }
}
