package com.xs.lightpuzzle.data.util;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.xs.lightpuzzle.data.DataManager;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Set;

/**
 * Created by xs on 2018/11/6.
 */

public class SetConverter<T> implements PropertyConverter<Set<T>, String> {

    public static class _String extends SetConverter<String> {
        // no-op by default
    }

    @Override
    public Set<T> convertToEntityProperty(String databaseValue) {
        if (!TextUtils.isEmpty(databaseValue)) {
            return DataManager.getDefault().getSerializer()
                    .deserialize(databaseValue, new TypeToken<Set<T>>() {
                        // no-op by default
                    }.getType());
        }
        return null;
    }

    @Override
    public String convertToDatabaseValue(Set<T> entityProperty) {
        if (entityProperty != null && !entityProperty.isEmpty()) {
            return DataManager.getDefault().getSerializer()
                    .serialize(entityProperty, new TypeToken<Set<T>>() {
                        // no-op by default
                    }.getType());
        }
        return null;
    }
}
