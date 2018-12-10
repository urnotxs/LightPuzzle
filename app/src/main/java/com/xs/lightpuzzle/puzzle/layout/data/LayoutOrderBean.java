package com.xs.lightpuzzle.puzzle.layout.data;

// FIXME generate failure  field _$_16955

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.ResourceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by urnot_XS on 2017/12/12.
 * 布局排序，需从json中获取每个比例下的子布局排序
 */

public class LayoutOrderBean {
    public final static String FILE_PATH = "data/puzzle_layout_order.json";

    /**
     * 获取对应图片张数和对应布局比例的
     * 布局ID排序
     */
    public List<String> getLayoutOrder(Context context, int picNum, float ratio) {
        List<String> result = null;
        String ratioString = getRatioString(ratio);
        List<HashMap<String, List<String>>> array = new LayoutOrderBean().fromJson(context);
        if (array != null && array.get(picNum - 1) != null) {
            result = array.get(picNum - 1).get(ratioString);
        }
        return result;
    }

    /**
     * 获取所有图片张数对应的所有布局比例的布局排序
     */
    public static List<HashMap<String, List<String>>> fromJson(Context context) {

        String jsonStr = ResourceUtils.readAssets2String(FILE_PATH, null);
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        List<HashMap<String, List<String>>> mLayoutOrder;
        mLayoutOrder = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int maxPicNum = 10;
            for (int i = 1; i < maxPicNum; i++) {
                String key = i + "";
                if (jsonObject.has(key)) {
                    JSONObject ratioJsonObject = jsonObject.getJSONObject(key);
                    Iterator<String> keys = ratioJsonObject.keys();
                    HashMap<String, List<String>> ratioOrderHashMap = new HashMap<>();
                    while (keys.hasNext()) {
                        key = keys.next();
                        JSONArray idArr = ratioJsonObject.getJSONArray(key);
                        List<String> idsList = new ArrayList<>();
                        for (int j = 0; j < idArr.length(); j++) {
                            JSONObject idJsonObj = idArr.getJSONObject(j);
                            idsList.add(idJsonObj.getString("id"));
                        }
                        ratioOrderHashMap.put(key, idsList);
                    }
                    mLayoutOrder.add(ratioOrderHashMap);
                } else {
                    mLayoutOrder.add(new HashMap<String, List<String>>());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mLayoutOrder;
    }

    private interface Ratio {
        String R_1_1 = "1:1";
        String R_9_16 = "9:16";
        String R_3_4 = "3:4";
        String R_16_9 = "16:9";
        String R_4_3 = "4:3";
        String R_3_2 = "3:2";
        String R_2_1 = "2:1";
        String R_2_3 = "2:3";
        String R_1_2 = "1:2";
    }

    private float[] mRatioArr = new float[]{
            1f, 9.0f / 16, 3.0f / 4, 16.0f / 9, 4.0f / 3,
            3.0f / 2, 2.0f / 1, 2.0f / 3, 1.0f / 2};

    private int getRatioIndex(float ratio) {
        DecimalFormat df = new DecimalFormat("######0.00");
        int ratioIndex = 0;
        for (int i = 0; i < mRatioArr.length; i++) {
            if (df.format(mRatioArr[i]).equals(df.format(ratio))) {
                ratioIndex = i;
            }
        }
        return ratioIndex;
    }

    public String getRatioString(float ratio) {
        int ratioIndex = getRatioIndex(ratio);
        String ratioString = Ratio.R_1_1;
        switch (ratioIndex) {
            case 0:
                ratioString = Ratio.R_1_1;
                break;
            case 1:
                ratioString = Ratio.R_9_16;
                break;
            case 2:
                ratioString = Ratio.R_3_4;
                break;
            case 3:
                ratioString = Ratio.R_16_9;
                break;
            case 4:
                ratioString = Ratio.R_4_3;
                break;
            case 5:
                ratioString = Ratio.R_3_2;
                break;
            case 6:
                ratioString = Ratio.R_2_1;
                break;
            case 7:
                ratioString = Ratio.R_2_3;
                break;
            case 8:
                ratioString = Ratio.R_1_2;
                break;
        }
        return ratioString;
    }
}
