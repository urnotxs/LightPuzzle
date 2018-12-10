package com.xs.lightpuzzle.puzzle.layout.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xs on 2018/12/4.
 */

public class BasePuzzleInfo {


    /**
     * coverThumb : BasePuzzle23646thumb.png
     * defaultThumb : BasePuzzle23646thumb.png
     * minPicNum : 2
     * maxPicNum : 2
     * thumbs : {"2":"BasePuzzle23646thumb.png"}
     * pic_h : 2048
     * pic_w : 2048
     * bgcolor : ffffff
     * fgpic : {}
     * bgpic : none
     * bgEffect : 叠加
     * textDic : {}
     * watermark : {}
     * sizeRatio : {"2":"1.0"}
     * variableFg : {}
     * point : {"2":["0,0,2048,1024","0,1024,2048,1024"]}
     * imageEffect : none
     */

    private String coverThumb;
    private String defaultThumb;
    private String minPicNum;
    private String maxPicNum;
    private ThumbsBean thumbs;
    private String pic_h;
    private String pic_w;
    private String bgcolor;
    private FgpicBean fgpic;
    private String bgpic;
    private String bgEffect;
    private TextDicBean textDic;
    private WatermarkBean watermark;
    private HashMap<String, String> sizeRatio;
    private VariableFgBean variableFg;
    private HashMap<String, List<String>> point;
    private String imageEffect;

    public HashMap<String, String> getSizeRatio() {
        return sizeRatio;
    }

    public void setSizeRatio(HashMap<String, String> sizeRatio) {
        this.sizeRatio = sizeRatio;
    }

    public HashMap<String, List<String>> getPoint() {
        return point;
    }

    public void setPoint(HashMap<String, List<String>> point) {
        this.point = point;
    }

    public String getCoverThumb() {
        return coverThumb;
    }

    public void setCoverThumb(String coverThumb) {
        this.coverThumb = coverThumb;
    }

    public String getDefaultThumb() {
        return defaultThumb;
    }

    public void setDefaultThumb(String defaultThumb) {
        this.defaultThumb = defaultThumb;
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

    public ThumbsBean getThumbs() {
        return thumbs;
    }

    public void setThumbs(ThumbsBean thumbs) {
        this.thumbs = thumbs;
    }

    public String getPic_h() {
        return pic_h;
    }

    public void setPic_h(String pic_h) {
        this.pic_h = pic_h;
    }

    public String getPic_w() {
        return pic_w;
    }

    public void setPic_w(String pic_w) {
        this.pic_w = pic_w;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public FgpicBean getFgpic() {
        return fgpic;
    }

    public void setFgpic(FgpicBean fgpic) {
        this.fgpic = fgpic;
    }

    public String getBgpic() {
        return bgpic;
    }

    public void setBgpic(String bgpic) {
        this.bgpic = bgpic;
    }

    public String getBgEffect() {
        return bgEffect;
    }

    public void setBgEffect(String bgEffect) {
        this.bgEffect = bgEffect;
    }

    public TextDicBean getTextDic() {
        return textDic;
    }

    public void setTextDic(TextDicBean textDic) {
        this.textDic = textDic;
    }

    public WatermarkBean getWatermark() {
        return watermark;
    }

    public void setWatermark(WatermarkBean watermark) {
        this.watermark = watermark;
    }

    public VariableFgBean getVariableFg() {
        return variableFg;
    }

    public void setVariableFg(VariableFgBean variableFg) {
        this.variableFg = variableFg;
    }

    public String getImageEffect() {
        return imageEffect;
    }

    public void setImageEffect(String imageEffect) {
        this.imageEffect = imageEffect;
    }

    public static BasePuzzleInfo JsonStringToInfo(String data) {
        BasePuzzleInfo info = new BasePuzzleInfo();
        /*"pic_h":"2048"
        "pic_w":"2048"
        "bgcolor": "ffffff"
        "sizeRatio": {
            "2": "1.0"
        }
        "point": {
            "2": ["0,0,2048,1024", "0,1024,2048,1024"]
        }*/
        JSONObject styleJsonObj = null;
        try {
            styleJsonObj = new JSONObject(data);
            if (styleJsonObj.has("pic_w")) {
                info.pic_w = styleJsonObj.getString("pic_w");
            }
            if (styleJsonObj.has("pic_h")) {
                info.pic_h = styleJsonObj.getString("pic_h");
            }

            if (styleJsonObj.has("bgcolor")) {
                info.bgcolor = styleJsonObj.getString("bgcolor");
            }

            if (styleJsonObj.has("minPicNum")) {
                info.minPicNum = styleJsonObj.getString("minPicNum");
            }

            if (styleJsonObj.has("maxPicNum")) {
                info.maxPicNum = styleJsonObj.getString("maxPicNum");
            }

            if (styleJsonObj.has("sizeRatio")) {
                JSONObject sizeRatioJO = styleJsonObj.getJSONObject("sizeRatio");
                if (sizeRatioJO != null) {
                    HashMap<String, String> sizeRatio = new HashMap<String, String>();
                    {
                        Iterator<String> iter = sizeRatioJO.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            String value = sizeRatioJO.getString(key);
                            sizeRatio.put(key, value);
                        }
                    }
                    info.sizeRatio = sizeRatio;
                }
            }

            if (styleJsonObj.has("point")) {
                HashMap<String, List<String>> point = new HashMap<String, List<String>>();
                if (styleJsonObj.has("point")) {
                    JSONObject pointJO = styleJsonObj.getJSONObject("point");
                    if (pointJO != null) {
                        Iterator<String> iter = pointJO.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            JSONArray pointJA = null;
                            pointJA = pointJO.getJSONArray(key);
                            {
                                List<String> pointList = new ArrayList<String>();
                                for (int i = 0; i < pointJA.length(); i++) {
                                    pointList.add(pointJA.getString(i));
                                }
                                point.put(key, pointList);
                            }
                        }
                        info.point = point;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;

    }

    public static class ThumbsBean {
        /**
         * 2 : BasePuzzle23646thumb.png
         */

/*        @SerializedName("2")
        private String _$2;

        public String get_$2() {
            return _$2;
        }

        public void set_$2(String _$2) {
            this._$2 = _$2;
        }*/
    }

    public static class FgpicBean {
    }

    public static class TextDicBean {
    }

    public static class WatermarkBean {
    }

    public static class SizeRatioBean {
        /**
         * 2 : 1.0
         */

/*        @SerializedName("2")
        private String _$2;

        public String get_$2() {
            return _$2;
        }

        public void set_$2(String _$2) {
            this._$2 = _$2;
        }*/
    }

    public static class VariableFgBean {
    }

    public static class PointBean {
/*        @SerializedName("2")
        private List<String> _$2;

        public List<String> get_$2() {
            return _$2;
        }

        public void set_$2(List<String> _$2) {
            this._$2 = _$2;
        }*/
    }
}
