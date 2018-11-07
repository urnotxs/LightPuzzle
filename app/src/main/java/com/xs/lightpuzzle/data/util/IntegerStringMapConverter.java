package com.xs.lightpuzzle.data.util;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.xs.lightpuzzle.data.DataManager;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Map;

/**
 * Created by xs on 2018/11/6.
 */

public class IntegerStringMapConverter implements
        PropertyConverter<Map<Integer, String>, String> {
    @Override
    public Map<Integer, String> convertToEntityProperty(String databaseValue) {
        if (TextUtils.isEmpty(databaseValue)) {
            return null;
        }

        return DataManager.getDefault().getSerializer()
                .deserialize(databaseValue, new TypeToken<Map<Integer, String>>() {
                    // no-op by default
                }.getType());
    }

    @Override
    public String convertToDatabaseValue(Map<Integer, String> entityProperty) {
        if (entityProperty == null || entityProperty.isEmpty()) {
            return null;
        }

        return DataManager.getDefault().getSerializer()
                .serialize(entityProperty, new TypeToken<Map<Integer, String>>() {
                    // no-op by default
                }.getType());
    }
}
