package com.xs.lightpuzzle.puzzle.data;

import com.xs.lightpuzzle.puzzle.data.lowdata.CardData;
import com.xs.lightpuzzle.puzzle.data.lowdata.FgData;
import com.xs.lightpuzzle.puzzle.data.lowdata.HeadData;
import com.xs.lightpuzzle.puzzle.data.lowdata.ImgPointData;
import com.xs.lightpuzzle.puzzle.data.lowdata.QrCodeData;
import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;
import com.xs.lightpuzzle.puzzle.data.lowdata.VariableFgData;
import com.xs.lightpuzzle.puzzle.data.lowdata.WaterMarkData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xs on 2018/4/12.
 * <p>
 * 模板信息
 */
public class TemplateData implements Serializable, Cloneable {

    //Template JsonId
    private String jsonId;
    //ratio
    private float sizeRatio;
    //output width
    private int outPutWidth;
    //output height
    private int outPutHeight;
    //fg
    private FgData fgData;
    //background picture
    private String bgPic;
    //mask
    private String maskPic;
    //watermark
    private WaterMarkData waterMarkData;
    //qrcode
    private QrCodeData qrCodeData;
    //text
    private List<TextData> textData;
    //Variable fg
    private List<VariableFgData> varFgDatas;
    //head
    private HeadData headData;
    //filterIndex
    private int filterIndex = -1;
    //是否自动美化
    private boolean autoBeautify = true;
    //img point
    private List<ImgPointData> imgPointDatas;
    //card
    private CardData cardData;

    public String getJsonId() {
        return jsonId;
    }

    public void setJsonId(String jsonId) {
        this.jsonId = jsonId;
    }

    public float getSizeRatio() {
        return sizeRatio;
    }

    public void setSizeRatio(float sizeRatio) {
        this.sizeRatio = sizeRatio;
    }

    public int getOutPutWidth() {
        return outPutWidth;
    }

    public void setOutPutWidth(int outPutWidth) {
        this.outPutWidth = outPutWidth;
    }

    public int getOutPutHeight() {
        return outPutHeight;
    }

    public void setOutPutHeight(int outPutHeight) {
        this.outPutHeight = outPutHeight;
    }

    public FgData getFgData() {
        return fgData;
    }

    public void setFgData(FgData fgData) {
        this.fgData = fgData;
    }

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }

    public String getMaskPic() {
        return maskPic;
    }

    public void setMaskPic(String maskPic) {
        this.maskPic = maskPic;
    }

    public WaterMarkData getWaterMarkData() {
        return waterMarkData;
    }

    public void setWaterMarkData(WaterMarkData waterMarkData) {
        this.waterMarkData = waterMarkData;
    }

    public QrCodeData getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(QrCodeData qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public List<TextData> getTextData() {
        return textData;
    }

    public void setTextData(List<TextData> textData) {
        this.textData = textData;
    }

    public List<VariableFgData> getVarFgDatas() {
        return varFgDatas;
    }

    public void setVarFgDatas(List<VariableFgData> varFgDatas) {
        this.varFgDatas = varFgDatas;
    }

    public HeadData getHeadData() {
        return headData;
    }

    public void setHeadData(HeadData headData) {
        this.headData = headData;
    }

    public int getFilterIndex() {
        return filterIndex;
    }

    public void setFilterIndex(int filterIndex) {
        this.filterIndex = filterIndex;
    }

    public boolean isAutoBeautify() {
        return autoBeautify;
    }

    public void setAutoBeautify(boolean autoBeautify) {
        this.autoBeautify = autoBeautify;
    }

    public List<ImgPointData> getImgPointDatas() {
        return imgPointDatas;
    }

    public void setImgPointDatas(List<ImgPointData> imgPointDatas) {
        this.imgPointDatas = imgPointDatas;
    }

    public CardData getCardData() {
        return cardData;
    }

    public void setCardData(CardData cardData) {
        this.cardData = cardData;
    }

}