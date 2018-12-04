package com.xs.lightpuzzle.data;

import com.blankj.utilcode.util.SDCardUtils;

import java.io.File;

/**
 * Created by xs on 2018/11/2.
 */

public interface DataConstant {

    String DB_NAME = "puzzle_material";

    interface ASSETS_DATA {
        String TEMPLATE = "data/template.json";
        String FONT = "data/font.json";
        String LAYOUT = "data/layout.json";
    }

    interface ASSETS_DATA_LAYOUT {
        String LAYOUT = "template/layout/BasePuzzle";
    }

    interface DIR_PATH {

        String APP = SDCardUtils.getSDCardPaths().get(0) + File.separator + FOLDER.APP;
        String TEMPLATE = APP + File.separator + FOLDER.TEMPLATE;
        String FONT = APP + File.separator + FOLDER.FONT;
    }

    interface FOLDER {

        String APP = "LightPuzzle";
        String TEMPLATE = "template";
        String FONT = "font";
    }

    interface TEMPLATE_CATEGORY {

        int SIMPLE = 0x00000100;
        int MEMO = 0x00000200;
        int SEAMLESS = 0x00000300;
        int WALLPAPER = 0x00000400;
        int COLLAGE = 0x00000500;
        int COVER = 0x00000600;
        int POSTCARD = 0x00000700;
        int BUSINESS_CARD = 0x00000800;
        int LONG_COLLAGE_GROUP = 0x00000900;
        int LONG_COLLAGE_SUB = 0x00000a00;
        int LAYOUT = 0x00000b00;
    }

    interface TEMPLATE_ADDITIONAL_CATEGORY {

        int LIKE = 0x01000100;
        int HISTORY = 0x01000200;
    }

    interface TEMPLATE_CATEGORY_NAME {

        String SIMPLE = "simple"; // 简约
        String MEMO = "memo"; // 便签
        String SEAMLESS = "seamless"; // 无缝
        String WALLPAPER = "wallpaper"; // 锁屏
        String COLLAGE = "collage"; // 拼接
        String COVER = "cover"; // 封面
        String POSTCARD = "postcard"; // 明信片
        String BUSINESS_CARD = "business_card"; // 名片
        String LONG_COLLAGE_GROUP = "long_collage_group"; // 长图拼接
        String LONG_COLLAGE_SUB = "long_collage_sub"; // 长图子模板
        String LAYOUT = "layout"; // 长图子模板
    }
}
