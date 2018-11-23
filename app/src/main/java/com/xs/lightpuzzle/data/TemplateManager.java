package com.xs.lightpuzzle.data;

import com.blankj.utilcode.util.FileIOUtils;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.data.entity.adapter.TemplateSetAdapter;
import com.xs.lightpuzzle.data.mapper.TemplateSetAdapterMapper;
import com.xs.lightpuzzle.data.serializer.Serializer;

import java.io.File;
import java.util.List;

/**
 * Created by xs on 2018/11/7.
 */

public class TemplateManager {

    public static boolean save(TemplateSet templateSet) {
        return DataManager.getDefault().saveTemplateSet(templateSet);
    }

    public static TemplateSet get(int category, String id) {
        TemplateSet templateSet = DataManager.getDefault().getTemplateSet(category, id);

        String filePath = templateSet.getDirPath() + File.separator + id + ".json";
        String data = FileIOUtils.readFile2String(filePath);
        Serializer serializer = DataManager.getDefault().getSerializer();
        TemplateSetAdapter adapter = serializer.deserialize(data, TemplateSetAdapter.class);
        return TemplateSetAdapterMapper.transform(adapter, false);
    }

    public static TemplateSet list(int category, String id) {
        return DataManager.getDefault().getTemplateSet(category, id);
    }

    public static List<TemplateSet> list(TemplateSetQuery query) {
        return DataManager.getDefault().getTemplateSets(query);
    }
}
