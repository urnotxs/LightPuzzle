package com.xs.lightpuzzle.data.util;

import android.util.SparseArray;

import com.xs.lightpuzzle.constant.DataConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xs on 2018/11/6.
 */

public final class TemplateCategoryHelper {
    private static final Set<Integer> SET = new HashSet<>();

    private static final SparseArray<String> NAME_MAP = new SparseArray<>();

    static {
        SET.add(DataConstant.TEMPLATE_CATEGORY.SIMPLE);
        SET.add(DataConstant.TEMPLATE_CATEGORY.MEMO);
        SET.add(DataConstant.TEMPLATE_CATEGORY.SEAMLESS);
        SET.add(DataConstant.TEMPLATE_CATEGORY.WALLPAPER);
        SET.add(DataConstant.TEMPLATE_CATEGORY.COLLAGE);
        SET.add(DataConstant.TEMPLATE_CATEGORY.COVER);
        SET.add(DataConstant.TEMPLATE_CATEGORY.POSTCARD);
        SET.add(DataConstant.TEMPLATE_CATEGORY.BUSINESS_CARD);
        SET.add(DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP);
        SET.add(DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_SUB);
        SET.add(DataConstant.TEMPLATE_CATEGORY.LAYOUT);

        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.SIMPLE, DataConstant.TEMPLATE_CATEGORY_NAME.SIMPLE);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.MEMO, DataConstant.TEMPLATE_CATEGORY_NAME.MEMO);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.SEAMLESS, DataConstant.TEMPLATE_CATEGORY_NAME.SEAMLESS);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.WALLPAPER, DataConstant.TEMPLATE_CATEGORY_NAME.WALLPAPER);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.COLLAGE, DataConstant.TEMPLATE_CATEGORY_NAME.COLLAGE);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.COVER, DataConstant.TEMPLATE_CATEGORY_NAME.COVER);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.POSTCARD, DataConstant.TEMPLATE_CATEGORY_NAME.POSTCARD);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.BUSINESS_CARD, DataConstant.TEMPLATE_CATEGORY_NAME.BUSINESS_CARD);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP, DataConstant.TEMPLATE_CATEGORY_NAME.LONG_COLLAGE_GROUP);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_SUB, DataConstant.TEMPLATE_CATEGORY_NAME.LONG_COLLAGE_SUB);
        NAME_MAP.append(DataConstant.TEMPLATE_CATEGORY.LAYOUT, DataConstant.TEMPLATE_CATEGORY_NAME.LAYOUT);
    }

    public static boolean isLegal(int category) {
        return SET.contains(category);
    }

    public static String getName(int category) {
        if (!isLegal(category)) {
            throw new RuntimeException("Category is illegal!");
        }
        return NAME_MAP.get(category);

    }
}
