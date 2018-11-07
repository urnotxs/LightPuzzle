package com.xs.lightpuzzle.data.mapper;

import android.annotation.SuppressLint;

import com.xs.lightpuzzle.data.PuzzleFileExtension;
import com.xs.lightpuzzle.data.entity.Template;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.data.entity.adapter.TemplateAdapter;
import com.xs.lightpuzzle.data.entity.adapter.TemplateSetAdapter;
import com.xs.lightpuzzle.data.util.MaterialDirPathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xs on 2018/11/6.
 */

public class TemplateSetAdapterMapper {

    public static List<TemplateSet> transform(
            Collection<TemplateSetAdapter> adapterCollection) {

        if (adapterCollection == null || adapterCollection.isEmpty()) {
            return null;
        }
        List<TemplateSet> collections = new ArrayList<>();
        for (TemplateSetAdapter adapter : adapterCollection) {
            collections.add(transform(adapter, true));
        }
        return collections;
    }

    public static TemplateSet transform(TemplateSetAdapter adapter,
                                         boolean isTransientTemplateMap) {
        if (adapter == null) {
            return null;
        }

        TemplateSet collection = new TemplateSet();

        int category = adapter.getCategory();
        String id = adapter.getId();

        collection.setCategory(adapter.getCategory());
        collection.setId(adapter.getId());
        collection.setName(adapter.getName());
        collection.setProportion(adapter.getProportion());
        collection.setUrl(adapter.getUrl());
        collection.setThumbUrl(adapter.getThumbUrl());

        collection.setThumbFileName(PuzzleFileExtension.mapFile(adapter.getThumbFileName()));

        collection.setMinPhotoNum(adapter.getMinPhotoNum());
        collection.setMaxPhotoNum(adapter.getMaxPhotoNum());
        collection.setThumbFileNameMap(adapter.getThumbFileNameMap());

        if (!isTransientTemplateMap) {
            collection.setTemplateMap(
                    transformTemplateMap(adapter.getTemplateAdapterMap(), category, id));
        }

        collection.setAttachedNodeMap(
                transformAttachedNodeMap(adapter.getAttachedNodeMap()));

        collection.setAttachedFontIdSet(adapter.getAttachedFontIdSet());

        collection.setOrder(adapter.getOrder());
        collection.setUiRatio(adapter.getUiRatio());

        collection.setDirPath(MaterialDirPathHelper.template(category, id));

        return collection;
    }



    private static Map<Integer, Template> transformTemplateMap(
            Map<Integer, TemplateAdapter> templateAdapterMap, int category, String id) {

        if (templateAdapterMap == null || templateAdapterMap.isEmpty()) {
            return null;
        }

        @SuppressLint("UseSparseArrays")
        Map<Integer, Template> map = new HashMap<>();

        for (Map.Entry<Integer, TemplateAdapter> entry : templateAdapterMap.entrySet()) {
            TemplateAdapter adapter = entry.getValue();
            if (adapter != null) {
                Template template = TemplateAdapterMapper.transformOrnament(adapter);
                template.setDirPath(MaterialDirPathHelper
                        .template(category, id, entry.getKey()));
                map.put(entry.getKey(), template);
            }
        }
        return map;
    }

    private static Map<Integer, TemplateSet.Node> transformAttachedNodeMap(
            Map<Integer, TemplateSetAdapter.NodeAdapter> attachedNodeMap) {

        if (attachedNodeMap == null || attachedNodeMap.isEmpty()) {
            return null;
        }

        @SuppressLint("UseSparseArrays")
        Map<Integer, TemplateSet.Node> map = new HashMap<>();

        for (Map.Entry<Integer, TemplateSetAdapter.NodeAdapter> entry
                : attachedNodeMap.entrySet()) {

            TemplateSetAdapter.NodeAdapter adapter = entry.getValue();

            if (adapter != null) {
                TemplateSet.Node node = transformTemplateNode(adapter);
                map.put(entry.getKey(), node);
            }
        }
        return map;
    }

    private static TemplateSet.Node transformTemplateNode(
            TemplateSetAdapter.NodeAdapter adapter) {

        return adapter == null ? null : new TemplateSet.Node(adapter.getCategory(), adapter.getId());
    }

}
