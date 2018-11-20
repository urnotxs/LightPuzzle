package com.xs.lightpuzzle.minnie;

import android.graphics.Color;

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

}
