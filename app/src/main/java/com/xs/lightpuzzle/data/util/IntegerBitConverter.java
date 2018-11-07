package com.xs.lightpuzzle.data.util;

import com.xs.lightpuzzle.yszx.BitHelper;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by xs on 2018/11/6.
 */

public class IntegerBitConverter implements PropertyConverter<Integer, String> {

    @Override
    public Integer convertToEntityProperty(String databaseValue) {
        return BitHelper.toInteger(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(Integer entityProperty) {
        return BitHelper.fillingZero(entityProperty);
    }
}
