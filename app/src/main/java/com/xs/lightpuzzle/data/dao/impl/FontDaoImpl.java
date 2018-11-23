package com.xs.lightpuzzle.data.dao.impl;


import android.text.TextUtils;

import com.xs.lightpuzzle.data.dao.FontDao;
import com.xs.lightpuzzle.data.dao.FontQuery;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.data.entity.FontDao.Properties;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xs on 2018/11/5.
 * 实现类
 * 用于对本地数据库的字体表进行增删改查
 */

public class FontDaoImpl implements FontDao {

    private final com.xs.lightpuzzle.data.entity.FontDao mDao;

    public FontDaoImpl(com.xs.lightpuzzle.data.entity.FontDao dao) {
        mDao = dao;
    }

    @Override
    public long insert(Font font) {
        if (font != null) {
            try {
                return mDao.insert(font);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean update(Font font) {
        if (font != null) {
            try {
                mDao.update(font);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean save(Font font) {
        if (font != null) {
            Long localId = font.getLocalId();
            if (localId == null || localId == 0) {
                Font local = query(font.getId());
                if (local == null) {
                    return insert(font) == 0;
                } else {
                    font.setLocalId(local.getLocalId());
                    return update(font);
                }
            } else {
                return update(font);
            }
        }
        return false;
    }

    @Override
    public boolean delete(Font font) {
        if (font != null) {
            Long localId = font.getLocalId();
            if (localId == null || localId == 0) {
                String id = font.getId();
                Font local = query(id);
                if (local != null) {
                    localId = local.getLocalId();
                }
            }
            if (localId != null && localId != 0) {
                try {
                    mDao.deleteByKey(localId);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean save(Collection<Font> fonts) {
        if (fonts != null && !fonts.isEmpty()) {
            List<Font> insertList = new ArrayList<>();
            List<Font> updateList = new ArrayList<>();
            for (Font font : fonts) {
                Long localId = font.getLocalId();
                if (localId == null || localId == 0) {
                    Font local = query(font.getId());
                    if (local == null) {
                        insertList.add(font);
                    } else {
                        updateList.add(font);
                    }
                } else {
                    updateList.add(font);
                }
            }
            try {
                if (!insertList.isEmpty()) {
                    mDao.insertInTx(insertList);
                }
                if (!updateList.isEmpty()) {
                    mDao.updateInTx(updateList);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean delete(Collection<Font> fonts) {
        if (fonts != null && !fonts.isEmpty()) {
            List<Font> deleteList = new ArrayList<>();
            for (Font font : fonts) {
                Long localId = font.getLocalId();
                if (localId == null || localId == 0) {
                    Font local = query(font.getId());
                    if (local != null) {
                        deleteList.add(font);
                    }
                } else {
                    deleteList.add(font);
                }
            }
            try {
                if (!deleteList.isEmpty()) {
                    mDao.deleteInTx(deleteList);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Font query(String id) {
        if (!TextUtils.isEmpty(id)) {
            QueryBuilder<Font> qb = mDao.queryBuilder();
            qb.where(Properties.Id.eq(id));
            try {
                return qb.uniqueOrThrow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<Font> query(FontQuery query) {
        if (query != null) {
            QueryBuilder<Font> qb = mDao.queryBuilder();
            Boolean isDownloaded = query.getDownloaded();
            if (isDownloaded != null){
                qb.where(Properties.IsDownloaded.eq(isDownloaded));
            }
            Integer type = query.getType();
            if (type != null){
                qb.where(Properties.Type.eq(type));
            }
            Boolean isOrderTypeAsc = query.isOrderTypeAsc();
            if (isOrderTypeAsc!=null){
                qb.orderAsc(Properties.Type);
            }
            qb.orderAsc(Properties.Order);

            try {
                return qb.list();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
