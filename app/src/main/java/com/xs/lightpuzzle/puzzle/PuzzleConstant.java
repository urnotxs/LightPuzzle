package com.xs.lightpuzzle.puzzle;

import android.graphics.Color;

import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.yszx.Scheme;

/**
 * Created by xs on 2018/11/13.
 */

public interface PuzzleConstant {

    interface ASSETS_PLACEHOLDER_PATH {

        String PHOTO = "placeholder/placeholder_photo.png";
        String AVATAR = "placeholder/placeholder_avatar.png";
        String QR_CODE = "placeholder/placeholder_qr_code.png";
    }

    interface ASSETS_PLACEHOLDER_URI {

        String PHOTO = Scheme.ASSETS.wrap(ASSETS_PLACEHOLDER_PATH.PHOTO);
        String AVATAR = Scheme.ASSETS.wrap(ASSETS_PLACEHOLDER_PATH.AVATAR);
        String QR_CODE = Scheme.ASSETS.wrap(ASSETS_PLACEHOLDER_PATH.QR_CODE);
    }

    int DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    interface ASSET_DATA_PATH {

        // assets start //
        String TEXTURE_COLOR_DATA_PATH = "data/textureandcolor/";
        String TEXTURE_INFO_PATH = TEXTURE_COLOR_DATA_PATH + "new_JNETextureInfo.json";
        String BACKGROUND_COLOR_PATH = TEXTURE_COLOR_DATA_PATH + "new_JNEColorInfo.json";
        String TEXTURE_COLOR_ALPHA_PATH = TEXTURE_COLOR_DATA_PATH + "color_texture_alpha.json";
        String FONT_COLOR_PATH = TEXTURE_COLOR_DATA_PATH + "new_JNEFontColorInfo.json";
        String FONT = "data/font.json";
    }

    // --- 签名
    String USER_SUB = DataConstant.DIR_PATH.APP + "/appdata/";
    String SD_SIGNATURE_PATH = USER_SUB + "signature/";
    String SD_SIGNATURE_HISTORY_LIST_PATH = SD_SIGNATURE_PATH + "history/"; // 历史
    String SD_SIGNATURE_EDITING_PATH = SD_SIGNATURE_PATH + "editing/";      // 正在编辑

    // 标签图片
    String PUZZLE_SAVE_LABEL_IMG = USER_SUB + "/label/";
}
