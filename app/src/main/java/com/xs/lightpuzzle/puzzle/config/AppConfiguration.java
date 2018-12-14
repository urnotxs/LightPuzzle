package com.xs.lightpuzzle.puzzle.config;

import com.xs.lightpuzzle.puzzle.config.impl.AppOtherConfigurationImpl;

/**
 * Created by xs on 2018/12/11.
 */

public class AppConfiguration implements AppOtherConfiguration {

    private static volatile AppConfiguration INSTANCE;

    public static AppConfiguration getDefault() {
        if (INSTANCE == null) {
            synchronized (AppConfiguration.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppConfiguration();
                }
            }
        }
        return INSTANCE;
    }

    private final AppOtherConfiguration mAppOther;

    private AppConfiguration() {
        mAppOther = new AppOtherConfigurationImpl();
    }

    @Override
    public void setKeyboardHeight(int height) {
        if (height > mAppOther.getKeyboardHeight()) {
            mAppOther.setKeyboardHeight(height);
        }
    }

    @Override
    public int getKeyboardHeight() {
        return mAppOther.getKeyboardHeight();
    }
}
