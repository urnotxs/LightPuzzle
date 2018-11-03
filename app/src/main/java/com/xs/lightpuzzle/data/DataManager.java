package com.xs.lightpuzzle.data;

import android.content.Context;

import com.xs.lightpuzzle.data.entity.DaoMaster;
import com.xs.lightpuzzle.data.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by xs on 2018/11/2.
 */

public class DataManager {

    public static void handleApplicationContext(Context context){
        if (context == null){
            throw new IllegalArgumentException("Context can't null");
        }

        Context applicationContext = context.getApplicationContext();

        if (INSTANCE == null){
            synchronized (DataManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataManager(applicationContext);
                    // TODO: 2018/11/2  
                }
            }
        }
    }

    private static DataManager INSTANCE;

    private static DataManager getDefault(){
        return INSTANCE;
    }

    private final Context mContext;

    private final DaoMaster.OpenHelper mOpenHelper;
    private final Database mDatabase;
    private final DaoSession mDaoSession;
//    private final FontDao mFontDao;

    private DataManager(Context context){
        mContext = context;

        // 初始数据库GreenDao
        mOpenHelper = new DaoMaster.DevOpenHelper(context, DataConstant.DB_NAME);

        mDatabase = mOpenHelper.getWritableDb();
        mDaoSession = new DaoMaster(mDatabase).newSession();

        // TODO: 2018/11/2  
    }
}
