package com.xs.lightpuzzle.materials.layout;

import com.google.gson.annotations.SerializedName;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @version 1.0
 * @function 用于retrofit2接受数据
 * @since 17/8/1
 */
public class PuzzleResourceAdapter {

    @SerializedName("info")
    private String info;
    @SerializedName("proportion")
    private String proportion;
    @SerializedName("minPicNum")
    private String minPicNum;
    @SerializedName("maxPicNum")
    private String maxPicNum;

    public PuzzleResourceAdapter() {
    }

    public PuzzleResourceAdapter(String info, String proportion, String minPicNum,
                                 String maxPicNum) {
        this.info = info;
        this.proportion = proportion;
        this.minPicNum = minPicNum;
        this.maxPicNum = maxPicNum;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String DataFileName) {
        this.info = DataFileName;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getMinPicNum() {
        return minPicNum;
    }

    public void setMinPicNum(String minPicNum) {
        this.minPicNum = minPicNum;
    }

    public String getMaxPicNum() {
        return maxPicNum;
    }

    public void setMaxPicNum(String maxPicNum) {
        this.maxPicNum = maxPicNum;
    }

}
