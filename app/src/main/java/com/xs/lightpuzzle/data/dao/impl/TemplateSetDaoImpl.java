package com.xs.lightpuzzle.data.dao.impl;

import android.text.TextUtils;

import com.xs.lightpuzzle.data.dao.TemplateSetDao;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.data.util.TemplateCategoryHelper;
import com.xs.lightpuzzle.data.entity.TemplateSetDao.Properties;
import com.xs.lightpuzzle.yszx.FlagHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xs on 2018/11/6.
 * 实现类
 * 用于对本地数据库的模板表 进行增删改查
 */

public class TemplateSetDaoImpl implements TemplateSetDao {

    private final com.xs.lightpuzzle.data.entity.TemplateSetDao mDao;

    public TemplateSetDaoImpl(com.xs.lightpuzzle.data.entity.TemplateSetDao templateSetDao) {
        mDao = templateSetDao;
    }

    @Override
    public long insert(TemplateSet templateSet) {
        if (templateSet != null) {
            try {
                return mDao.insert(templateSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public boolean update(TemplateSet templateSet) {
        if (templateSet != null) {
            try {
                mDao.update(templateSet);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean save(TemplateSet templateSet) {
        if (templateSet != null) {
            Long localId = templateSet.getLocalId();
            if (localId == null || localId == 0) {
                TemplateSet local = query(templateSet.getCategory(), templateSet.getId());
                if (local == null) {
                    return insert(templateSet) == 0;
                } else {
                    templateSet.setLocalId(templateSet.getLocalId());
                    return update(templateSet);
                }
            } else {
                return update(templateSet);
            }
        }
        return false;
    }

    @Override
    public boolean delete(TemplateSet templateSet) {
        if (templateSet != null) {
            Long localId = templateSet.getLocalId();
            if (localId == null || localId == 0) {
                int category = templateSet.getCategory();
                String id = templateSet.getId();
                TemplateSet local = query(category, id);
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
    public boolean save(Collection<TemplateSet> templateSets) {
        if (templateSets != null && !templateSets.isEmpty()) {
            List<TemplateSet> inserts = new ArrayList<>();
            List<TemplateSet> updates = new ArrayList<>();
            for (TemplateSet collection : templateSets) {
                Long localId = collection.getLocalId();
                if (localId == null || localId == 0) {
                    TemplateSet local = query(collection.getCategory(), collection.getId());
                    if (local == null) {
                        inserts.add(collection);
                    } else {
                        updates.add(local);
                    }
                } else {
                    updates.add(collection);
                }
            }
            try {
                if (!inserts.isEmpty()) {
                    mDao.insertInTx(inserts);
                }
                if (!updates.isEmpty()) {
                    mDao.updateInTx(updates);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean delete(Collection<TemplateSet> templateSets) {
        if (templateSets != null && !templateSets.isEmpty()) {
            List<TemplateSet> deletes = new ArrayList<>();
            for (TemplateSet collection : templateSets) {
                Long localId = collection.getLocalId();
                if (localId == null || localId == 0) {
                    TemplateSet local = query(collection.getCategory(), collection.getId());
                    if (local != null) {
                        deletes.add(local);
                    }
                } else {
                    deletes.add(collection);
                }
            }
            try {
                mDao.deleteInTx(deletes);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<TemplateSet> loadAll() {
        return mDao.loadAll();
    }

    @Override
    public TemplateSet query(int category, String id) {
        if (TemplateCategoryHelper.isLegal(category) && !TextUtils.isEmpty(id)) {
            QueryBuilder<TemplateSet> qb = mDao.queryBuilder();
            qb.where(com.xs.lightpuzzle.data.entity.TemplateSetDao.Properties.Category.eq(category),
                    com.xs.lightpuzzle.data.entity.TemplateSetDao.Properties.Id.eq(id));
            try {
                return qb.uniqueOrThrow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<TemplateSet> query(TemplateSetQuery query) {

        if (query != null) {
            QueryBuilder<TemplateSet> queryBuilder = mDao.queryBuilder();

            Integer category = query.getCategory();
            if (category != null) {
                queryBuilder.where(Properties.Category.eq(category));
            }

            Integer photoNum = query.getPhotoNum();
            if (photoNum != null) {
                queryBuilder.where(queryBuilder.and(
                        Properties.MinPhotoNum.le(photoNum),
                        Properties.MaxPhotoNum.ge(photoNum)));
            }

            Integer flag = query.getFlag();
            if (flag != null) {
                queryBuilder.where(Properties.Flag.like(generateFlagLike(flag)));

                if (TemplateSet.FLAG.DOWNLOADED == flag){
                    queryBuilder.orderDesc(Properties.DownloadedOrder);
                } else if (TemplateSet.FLAG.HISTORY == flag) {
                    queryBuilder.orderDesc(Properties.HistoryOrder);
                } else if (TemplateSet.FLAG.LIKE == flag) {
                    queryBuilder.orderDesc(Properties.LikeOrder);
                }
            }else{
                queryBuilder.orderAsc(Properties.Order);
            }

            Integer count = query.getCount();
            if (count != null && count > 0) {
                queryBuilder.limit(count);
            }

            try {
                return queryBuilder.list();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    // HELPER

    private String generateFlagLike(Integer flag) {
        boolean offAll = true;
        StringBuilder stringBuilder =
                new StringBuilder("________________________________");

        if (FlagHelper.isOn(TemplateSet.FLAG.DOWNLOADED, flag)) {
            generateFlagWildcardChar(stringBuilder, 32);
            offAll = false;
        }

        if (FlagHelper.isOn(TemplateSet.FLAG.UNUSED, flag)) {
            generateFlagWildcardChar(stringBuilder, 31);
            offAll = false;
        }

        if (FlagHelper.isOn(TemplateSet.FLAG.HISTORY, flag)) {
            generateFlagWildcardChar(stringBuilder, 30);
            offAll = false;
        }

        if (FlagHelper.isOn(TemplateSet.FLAG.LIKE, flag)) {
            generateFlagWildcardChar(stringBuilder, 29);
            offAll = false;
        }

        if (offAll) {
            return "%";
        } else {
            return stringBuilder.toString();
        }
    }

    private void generateFlagWildcardChar(StringBuilder wc, int position) {
        wc.replace(position - 1, position, 1 + "");
    }
}
