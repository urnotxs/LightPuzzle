package com.xs.lightpuzzle.data.util;

import com.xs.lightpuzzle.data.DataConstant;

import java.io.File;

/**
 * Created by xs on 2018/11/6.
 */

public class MaterialDirPathHelper {

    public static String font(String id) {
        return DataConstant.DIR_PATH.FONT + File.separator + id;
    }

    public static String template(int category, String id, int photoNum){
        return template(category, id) + File.separator + photoNum;
    }

    public static String template(int category, String id){
        return template(category) + File.separator + id;
    }

    public static String template(int category){
        return DataConstant.DIR_PATH.TEMPLATE + File.separator
                + TemplateCategoryHelper.getName(category);
    }
}
