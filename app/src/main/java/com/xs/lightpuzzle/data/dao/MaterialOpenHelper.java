package com.xs.lightpuzzle.data.dao;

import android.content.Context;

import com.xs.lightpuzzle.data.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by xs on 2018/11/2.
 */

public class MaterialOpenHelper extends DaoMaster.OpenHelper {
    private final Context mContext;

    public MaterialOpenHelper(Context context, String name) {
        super(context, name);
        mContext = context;
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
