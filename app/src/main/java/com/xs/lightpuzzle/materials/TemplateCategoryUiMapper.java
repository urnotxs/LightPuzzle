package com.xs.lightpuzzle.materials;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.xs.lightpuzzle.LightPuzzleApplication;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.DataConstant;

/**
 * Created by xs on 2018/11/8.
 */

public class TemplateCategoryUiMapper {

    private static final SparseIntArray MAPPER = new SparseIntArray();
    private static final SparseArray<String> NAME_MAPPER = new SparseArray<>();

    static {
        MAPPER.append(0, DataConstant.TEMPLATE_ADDITIONAL_CATEGORY.HISTORY);
        MAPPER.append(1, DataConstant.TEMPLATE_ADDITIONAL_CATEGORY.LIKE);
        MAPPER.append(2, DataConstant.TEMPLATE_CATEGORY.LAYOUT);
        MAPPER.append(3, DataConstant.TEMPLATE_CATEGORY.SIMPLE);
        MAPPER.append(4, DataConstant.TEMPLATE_CATEGORY.MEMO);
        MAPPER.append(5, DataConstant.TEMPLATE_CATEGORY.SEAMLESS);
        MAPPER.append(6, DataConstant.TEMPLATE_CATEGORY.WALLPAPER);
        MAPPER.append(7, DataConstant.TEMPLATE_CATEGORY.COLLAGE);
        MAPPER.append(8, DataConstant.TEMPLATE_CATEGORY.COVER);
        MAPPER.append(9, DataConstant.TEMPLATE_CATEGORY.POSTCARD);
        MAPPER.append(10, DataConstant.TEMPLATE_CATEGORY.BUSINESS_CARD);
        MAPPER.append(11, DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP);


        NAME_MAPPER.append(0, LightPuzzleApplication.getContext().getString(R.string.template_category_history));
        NAME_MAPPER.append(1, LightPuzzleApplication.getContext().getString(R.string.template_category_favorite));
        NAME_MAPPER.append(3, LightPuzzleApplication.getContext().getString(R.string.template_category_simple));
        NAME_MAPPER.append(4, LightPuzzleApplication.getContext().getString(R.string.template_category_memo));
        NAME_MAPPER.append(5, LightPuzzleApplication.getContext().getString(R.string.template_category_seamless));
        NAME_MAPPER.append(6, LightPuzzleApplication.getContext().getString(R.string.template_category_wallpaper));
        NAME_MAPPER.append(7, LightPuzzleApplication.getContext().getString(R.string.template_category_collage));
        NAME_MAPPER.append(8, LightPuzzleApplication.getContext().getString(R.string.template_category_cover));
        NAME_MAPPER.append(9, LightPuzzleApplication.getContext().getString(R.string.template_category_postcard));
        NAME_MAPPER.append(10, LightPuzzleApplication.getContext().getString(R.string.template_category_business_card));
        NAME_MAPPER.append(11, LightPuzzleApplication.getContext().getString(R.string.template_category_long_collage));
        NAME_MAPPER.append(2, LightPuzzleApplication.getContext().getString(R.string.template_category_layout));
    }

    public static SparseIntArray getMapper() {
        return MAPPER;
    }

    public static SparseArray<String> getNameMapper() {
        return NAME_MAPPER;
    }

    public static int getPosition(int category) {
        return MAPPER.keyAt(MAPPER.indexOfValue(category));
    }

    public static int getCategory(int position) {
        return MAPPER.get(position);
    }

    public static String getCategoryName(int position) {
        return NAME_MAPPER.get(position);
    }
}
