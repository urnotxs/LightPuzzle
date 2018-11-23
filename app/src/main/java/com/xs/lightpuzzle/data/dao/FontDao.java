package com.xs.lightpuzzle.data.dao;

import com.xs.lightpuzzle.data.entity.Font;

import java.util.Collection;
import java.util.List;

/**
 * Created by xs on 2018/11/2.
 * 接口
 * 用于对本地数据库的字体表进行增删改查
 */

public interface FontDao {

    long insert(Font font);

    boolean update(Font font);

    boolean save(Font font);

    boolean delete(Font font);

    boolean save(Collection<Font> fonts);

    boolean delete(Collection<Font> fonts);

    Font query(String id);

    List<Font> query(FontQuery query);
}
