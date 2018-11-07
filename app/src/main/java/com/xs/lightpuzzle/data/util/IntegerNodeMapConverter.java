package com.xs.lightpuzzle.data.util;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.xs.lightpuzzle.data.DataManager;
import com.xs.lightpuzzle.data.entity.TemplateSet.Node;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Map;

/**
 * Created by xs on 2018/11/6.
 */

public class IntegerNodeMapConverter implements
        PropertyConverter<Map<Integer, Node>, String> {
    @Override
    public Map<Integer, Node> convertToEntityProperty(String databaseValue) {
        if (TextUtils.isEmpty(databaseValue)) {
            return null;
        }

        return DataManager.getDefault().getSerializer()
                .deserialize(databaseValue, new TypeToken<Map<Integer, Node>>() {
                    // no-op by default
                }.getType());
    }

    @Override
    public String convertToDatabaseValue(Map<Integer, Node> entityProperty) {
        if (entityProperty == null || entityProperty.isEmpty()) {
            return null;
        }

        return DataManager.getDefault().getSerializer()
                .serialize(entityProperty, new TypeToken<Map<Integer, Node>>() {
                    // no-op by default
                }.getType());
    }
}
