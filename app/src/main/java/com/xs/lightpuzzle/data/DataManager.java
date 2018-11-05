package com.xs.lightpuzzle.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xs.lightpuzzle.data.dao.FontDao;
import com.xs.lightpuzzle.data.dao.impl.FontDaoImpl;
import com.xs.lightpuzzle.data.entity.DaoMaster;
import com.xs.lightpuzzle.data.entity.DaoSession;
import com.xs.lightpuzzle.data.net.RestApi;
import com.xs.lightpuzzle.data.net.impl.AssetsRestApi;
import com.xs.lightpuzzle.data.serializer.Serializer;
import com.xs.lightpuzzle.data.service.SyncService;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
                    // 开个服务用来构建本地数据库
                    SyncService.start(applicationContext);
                }
            }
        }
    }

    private static DataManager INSTANCE;

    public static DataManager getDefault(){
        return INSTANCE;
    }

    private final Context mContext;

    private final ExecutorService mThreadPoolExcutor;

    private final DaoMaster.OpenHelper mOpenHelper;
    private final Database mDatabase;
    private final DaoSession mDaoSession;
    private final FontDao mFontDao;

    private final Serializer mSerializer;
    private final RestApi mRestApi;

    private DataManager(Context context){
        mContext = context;

        mThreadPoolExcutor = new ThreadPoolExecutor(3,
                5,10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new JobThreadFactory());

        mSerializer = new Serializer();

        // 初始数据库GreenDao
        mOpenHelper = new DaoMaster.DevOpenHelper(context, DataConstant.DB_NAME);

        mDatabase = mOpenHelper.getWritableDb();
        mDaoSession = new DaoMaster(mDatabase).newSession();

        mFontDao = new FontDaoImpl(mDaoSession.getFontDao());

        mRestApi = new AssetsRestApi(context, mSerializer);

    }

    public void syncData() {
        syncFonts();
    }

    private void syncFonts() {
        mThreadPoolExcutor.execute(new Runnable() {
            @Override
            public void run() {
                mFontDao.save(mRestApi.getFonts());
            }
        });
    }

    private static class JobThreadFactory implements ThreadFactory {

        private int counter = 0;

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "pizza_material_" + counter++);
        }
    }
}
