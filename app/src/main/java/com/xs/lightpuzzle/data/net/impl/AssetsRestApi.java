package com.xs.lightpuzzle.data.net.impl;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.xs.lightpuzzle.constant.DirConstant;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.data.entity.adapter.FontAdapter;
import com.xs.lightpuzzle.data.entity.adapter.TemplateSetAdapter;
import com.xs.lightpuzzle.data.mapper.FontAdapterMapper;
import com.xs.lightpuzzle.data.mapper.TemplateSetAdapterMapper;
import com.xs.lightpuzzle.data.net.RestApi;
import com.xs.lightpuzzle.data.serializer.Serializer;
import com.xs.lightpuzzle.yszx.AssetManagerHelper;

import java.util.List;

/**
 * Created by xs on 2018/11/5.
 */

public class AssetsRestApi implements RestApi {
    private final Context mContext;
    private final Serializer mSerializer;

    public AssetsRestApi(Context context, Serializer serializer) {
        mContext = context;
        mSerializer = serializer;
    }

    @Override
    public List<Font> getFonts() {
        String gsonString = AssetManagerHelper.convertInputString(
                mContext, DirConstant.ASSETS_DATA.FONT);
        if (!TextUtils.isEmpty(gsonString)) {
            try {
                List<FontAdapter> adapters = mSerializer
                        .deserialize(gsonString,
                                new TypeToken<List<FontAdapter>>() {
                                    // no-op by default
                                }.getType());

                if (adapters != null && !adapters.isEmpty()) {
                    return FontAdapterMapper.transform(adapters);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<TemplateSet> getTemplates() {
        String gsonString = AssetManagerHelper.convertInputString(
                mContext, DirConstant.ASSETS_DATA.TEMPLATE);

        if (!TextUtils.isEmpty(gsonString)) {
            try {
                List<TemplateSetAdapter> adapters = mSerializer
                        .deserialize(gsonString,
                                new TypeToken<List<TemplateSetAdapter>>() {
                                    // no-op by default
                                }.getType());

                if (adapters != null && !adapters.isEmpty()) {
                    return TemplateSetAdapterMapper.transform(adapters);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
