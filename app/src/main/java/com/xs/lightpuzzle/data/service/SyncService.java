package com.xs.lightpuzzle.data.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xs.lightpuzzle.data.DataManager;

/**
 * Created by xs on 2018/11/5.
 */

public class SyncService extends Service {

    public static void start(Context context) {
        Intent intent = new Intent(context, SyncService.class);
        context.startService(intent);
    }

    public SyncService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DataManager.getDefault().syncData();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
