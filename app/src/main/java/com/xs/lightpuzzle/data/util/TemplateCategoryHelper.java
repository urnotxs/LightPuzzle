package com.xs.lightpuzzle.data.util;

import android.util.SparseArray;

import com.xs.lightpuzzle.data.DataConstant.TEMPLATE_CATEGORY;
import com.xs.lightpuzzle.data.DataConstant.TEMPLATE_CATEGORY_NAME;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xs on 2018/11/6.
 */

public final class TemplateCategoryHelper {
    private static final Set<Integer> SET = new HashSet<>();

    private static final SparseArray<String> NAME_MAP = new SparseArray<>();

    static {
        SET.add(TEMPLATE_CATEGORY.SIMPLE);
        SET.add(TEMPLATE_CATEGORY.MEMO);
        SET.add(TEMPLATE_CATEGORY.SEAMLESS);
        SET.add(TEMPLATE_CATEGORY.WALLPAPER);
        SET.add(TEMPLATE_CATEGORY.COLLAGE);
        SET.add(TEMPLATE_CATEGORY.COVER);
        SET.add(TEMPLATE_CATEGORY.POSTCARD);
        SET.add(TEMPLATE_CATEGORY.BUSINESS_CARD);
        SET.add(TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP);
        SET.add(TEMPLATE_CATEGORY.LONG_COLLAGE_SUB);
        SET.add(TEMPLATE_CATEGORY.LAYOUT);

        NAME_MAP.append(TEMPLATE_CATEGORY.SIMPLE, TEMPLATE_CATEGORY_NAME.SIMPLE);
        NAME_MAP.append(TEMPLATE_CATEGORY.MEMO, TEMPLATE_CATEGORY_NAME.MEMO);
        NAME_MAP.append(TEMPLATE_CATEGORY.SEAMLESS, TEMPLATE_CATEGORY_NAME.SEAMLESS);
        NAME_MAP.append(TEMPLATE_CATEGORY.WALLPAPER, TEMPLATE_CATEGORY_NAME.WALLPAPER);
        NAME_MAP.append(TEMPLATE_CATEGORY.COLLAGE, TEMPLATE_CATEGORY_NAME.COLLAGE);
        NAME_MAP.append(TEMPLATE_CATEGORY.COVER, TEMPLATE_CATEGORY_NAME.COVER);
        NAME_MAP.append(TEMPLATE_CATEGORY.POSTCARD, TEMPLATE_CATEGORY_NAME.POSTCARD);
        NAME_MAP.append(TEMPLATE_CATEGORY.BUSINESS_CARD, TEMPLATE_CATEGORY_NAME.BUSINESS_CARD);
        NAME_MAP.append(TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP, TEMPLATE_CATEGORY_NAME.LONG_COLLAGE_GROUP);
        NAME_MAP.append(TEMPLATE_CATEGORY.LONG_COLLAGE_SUB, TEMPLATE_CATEGORY_NAME.LONG_COLLAGE_SUB);
        NAME_MAP.append(TEMPLATE_CATEGORY.LAYOUT, TEMPLATE_CATEGORY_NAME.LAYOUT);
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
