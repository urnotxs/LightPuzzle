package com.xs.lightpuzzle.puzzle.config.impl;

import com.blankj.utilcode.util.SPUtils;
import com.xs.lightpuzzle.puzzle.config.AppOtherConfiguration;
import com.xs.lightpuzzle.puzzle.config.CONFIG_FILED;

/**
 * Created by xs on 2018/12/11.
 */

public class AppOtherConfigurationImpl implements AppOtherConfiguration {

    private static class SpHelper {

        public static synchronized SPUtils get() {
            return SPUtils.getInstance(CONFIG_FILED.OTHER.OTHER_PREFS_NAME);
        }
    }

    @Override
    public void setKeyboardHeight(int height) {
        SpHelper.get().put(CONFIG_FILED.OTHER.KEYBOARD_HEIGHT, height);
    }

    @Override
    public int getKeyboardHeight() {
        return SpHelper.get().getInt(CONFIG_FILED.OTHER.KEYBOARD_HEIGHT, 0);
    }
}
