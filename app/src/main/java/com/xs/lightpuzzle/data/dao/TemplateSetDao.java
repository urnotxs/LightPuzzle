package com.xs.lightpuzzle.data.dao;

import com.xs.lightpuzzle.data.entity.TemplateSet;

import java.util.Collection;
import java.util.List;

/**
 * Created by xs on 2018/11/6.
 * 接口
 * 用于对本地数据库的模板表进行增删改查
 */

public interface TemplateSetDao {

    long insert(TemplateSet templateSet);

    boolean update(TemplateSet templateSet);

    boolean save(TemplateSet templateSet);

    boolean delete(TemplateSet templateSet);

    boolean save(Collection<TemplateSet> templateSets);

    boolean delete(Collection<TemplateSet> templateSets);

    List<TemplateSet> loadAll();

    TemplateSet query(int category, String id);

    List<TemplateSet> query(TemplateSetQuery query);
}
