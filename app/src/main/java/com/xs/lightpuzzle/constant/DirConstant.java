package com.xs.lightpuzzle.constant;

import com.blankj.utilcode.util.SDCardUtils;
import com.xs.lightpuzzle.yszx.Scheme;

import java.io.File;

/**
 * Created by xs on 2019/1/9.
 */

public interface DirConstant {

    // 数据库名
    String DB_NAME = "puzzle_material";

    // --- 内置文件路径
    interface ASSETS_DATA {
        String TEMPLATE = "data/template.json"; // 模板
        String FONT = "data/font.json"; // 字体
        String LAYOUT = "template/layout/BasePuzzle"; // 布局
    }

    interface ASSETS_FONT_PATH {
        String FONT = "font";
    }

    interface ASSET_TEXTURE_COLOR {
        // 内置背景和字体的纹理和颜色关联json
        // assets start //
        String TEXTURE_COLOR_DATA_PATH = "data/textureandcolor/";
        String TEXTURE_INFO_PATH = TEXTURE_COLOR_DATA_PATH + "new_JNETextureInfo.json";
        String BACKGROUND_COLOR_PATH = TEXTURE_COLOR_DATA_PATH + "new_JNEColorInfo.json";
        String TEXTURE_COLOR_ALPHA_PATH = TEXTURE_COLOR_DATA_PATH + "color_texture_alpha.json";
        String FONT_COLOR_PATH = TEXTURE_COLOR_DATA_PATH + "new_JNEFontColorInfo.json";
    }

    interface ASSETS_PLACEHOLDER_PATH {
        // 内置默认图
        String PHOTO = "placeholder/placeholder_photo.png";
        String AVATAR = "placeholder/placeholder_avatar.png";
        String QR_CODE = "placeholder/placeholder_qr_code.png";
    }

    interface ASSETS_PLACEHOLDER_URI {
        String PHOTO = Scheme.ASSETS.wrap(ASSETS_PLACEHOLDER_PATH.PHOTO);
        String AVATAR = Scheme.ASSETS.wrap(ASSETS_PLACEHOLDER_PATH.AVATAR);
        String QR_CODE = Scheme.ASSETS.wrap(ASSETS_PLACEHOLDER_PATH.QR_CODE);
    }

    // --- 本地文件路径

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


    // 用户数据子文件夹路径
    String USER_SUB = DIR_PATH.APP + "/appdata/";

    // --- 签名
    String SD_SIGNATURE_PATH = USER_SUB + "signature/";
    String SD_SIGNATURE_HISTORY_LIST_PATH = SD_SIGNATURE_PATH + "history/"; // 历史
    String SD_SIGNATURE_EDITING_PATH = SD_SIGNATURE_PATH + "editing/";      // 正在编辑

    // 标签图片
    String PUZZLE_SAVE_LABEL_IMG = USER_SUB + "/label/";

    // 保存
    String PUZZLE_SAVE = USER_SUB + "save/";
    String PUZZLE_SAVE_JSON = PUZZLE_SAVE + "puzzle_save_info.json";
    String PUZZLE_SAVE_START_NAME = "JanePhoto_";
}
