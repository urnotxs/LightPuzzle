package com.xs.lightpuzzle.puzzle.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import cn.poco.config.Constant;

/**
 * Created by Lin on 2017/12/28.
 */

public class PuzzleTextUtils {

    public static HashMap<String, Typeface> sTypefaces = new HashMap<>();

    public static Typeface readFont(Context context, String fontType, boolean downFont) {
        if (context == null || fontType == null || fontType.equals("lihei pro.ttf") || fontType.equals("Heiti SC") || fontType.equals("Default")) {
            return Typeface.DEFAULT;
        } else {
            Typeface typeface = null;
            String fileName = fontType;
            if (fileName.equals("Helvetica CE 35 Thin.ttf")) {
                fileName = "Helvetica Neue CE 35 Thin.ttf";
            }
            //下载好后，直接是全路径
            String path = fileName;
            //如果静态里面存在这个字体, 则直接返回不要重新读, 因为重新读会很耗时
            if (sTypefaces.containsKey(fileName)) {
                return sTypefaces.get(fileName);
            }
            if (downFont) {
                File file = new File(path);
                if (file.exists() && file.isFile() && file.length() > 0) {
                    try {
                        typeface = Typeface.createFromFile(path);
                    } catch (Exception e) {
                        return Typeface.DEFAULT;
                    }
                } else {
                    typeface = Typeface.DEFAULT;
                }
            } else {
                // TODO: 2018/11/21 内置
//                try {
//                    typeface = Typeface.createFromAsset(context.getAssets(), DirConstant.FONT_ASSEST_PATH + fileName);
//                } catch (Exception e) {
//                    File file = new File(path);
//                    if (file.exists() && file.isFile() && file.length() > 0) {
//                        typeface = Typeface.createFromFile(path);
//                    } else {
//                        return Typeface.DEFAULT;
//                    }
//                }
            }
            if (typeface == null) {
                typeface = Typeface.DEFAULT;
            }
            if (!sTypefaces.containsKey(fileName)) {
                sTypefaces.put(fileName, typeface);
            }
            return typeface;
        }
    }

    public static String china2En(String str) {
        return FontHelperUtils.convertZhcnToEnForAssetFontRes(str);
    }

    /**
     * 过滤某些字段
     *
     * @param autoStr
     * @return
     */
    public static String autoStringFilter(String autoStr) {
        if (autoStr.contains("第") && autoStr.contains("6") && autoStr.contains("回")) {
            autoStr = "\n\n\n\n第6回";
        }
        if (autoStr.contains("味") && autoStr.contains("之") && autoStr.contains("选")) {
            autoStr = "味之选";
        }
        return autoStr;
    }

    /**
     * 获取起点位置
     * @param alignment
     * @param drawWidth
     * @param textWidth
     * @return
     */

    public static float getStartX(String alignment, float drawWidth, float textWidth){
        if (TextUtils.isEmpty(alignment)) {
            return 0;
        } else {
            alignment = alignment.trim();
            if (alignment.equals("Left")) {
                return 0;
            } else if (alignment.equals("Center")) {
                return (drawWidth - textWidth) / 2;
            } else if (alignment.equals("Right")) {
                return (drawWidth - textWidth);
            }
        }
        return 0;
    }

    public static List<String> getStrList(Context context, String autoStr, int fontSize, int maxFontSize, int
            minFontSize, int lineSpace, String font, boolean downloadFont, int width, int height){
        //先传入字体，因为字体是会影响大小的
        Paint paint = new Paint();
        if (!TextUtils.isEmpty(font)) {
            Typeface typeface = readFont(context, font, downloadFont);
            paint.setTypeface(typeface);
        }
        return getStrList(autoStr, fontSize, maxFontSize, minFontSize, lineSpace, width, height, paint);
    }

    public static List<String> getStrList(String autoStr, int fontSize, int maxFontSize,
                                          int minFontSize, int lineSpace, int width, int height, Paint paint){
        if (TextUtils.isEmpty(autoStr)) {
            return null;
        }
        List<String> strList = new ArrayList<>();
        paint.setTextSize(fontSize);
        float lineHeight = paint.descent() - paint.ascent();
        String editStr = autoStr;
        float lineWidth = 0;
        String linStr = "";
        for (int i = 0; i < editStr.length(); i++) {
            char strChar = editStr.charAt(i);
            String str = String.valueOf(strChar);
            float strWidth = paint.measureText(str);
            if (!("\n").equals(str)) {
                //先判断是否大于最大宽度
                if (lineWidth + strWidth > width) {
                    int lineCount = strList.size() + 1;
                    if (lineHeight * lineCount + (lineCount - 1) * lineSpace > height) {
                        //减少5个px
                        if (fontSize - 5 > minFontSize){
                            fontSize = fontSize - 5;
                            return getStrList(autoStr, fontSize, maxFontSize, minFontSize, lineSpace, width, height, paint);
                        } else {
                            if (fontSize != minFontSize) {
                                fontSize = minFontSize;
                                return getStrList(autoStr, fontSize, maxFontSize, minFontSize, lineSpace, width, height, paint);
                            }
                        }
                        return strList;
                    } else {
                        strList.add(linStr);
                        lineWidth = strWidth;
                        linStr = str;
                        if (i == editStr.length() - 1) {
                            strList.add(linStr);
                        }
                    }
                } else {
                    int lineCount = strList.size() + 1;
                    if (lineHeight * lineCount + (lineCount - 1) * lineSpace > height) {
                        //减少5个px
                        if (fontSize - 5 > minFontSize){
                            fontSize = fontSize - 5;
                            return getStrList(autoStr, fontSize, maxFontSize, minFontSize, lineSpace, width, height, paint);
                        } else {
                            if (fontSize != minFontSize) {
                                fontSize = minFontSize;
                                return getStrList(autoStr, fontSize, maxFontSize, minFontSize, lineSpace, width, height, paint);
                            }
                        }
                        return strList;
                    } else {
                        lineWidth += strWidth;
                        linStr += str;
                        if (i == editStr.length() - 1) {
                            strList.add(linStr);
                        }
                    }
                }
            } else {
                strList.add(linStr);
                lineWidth = 0;
                linStr = "";
            }
        }
        return strList;
    }

}
