package com.xs.lightpuzzle.puzzle.save;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.xs.lightpuzzle.puzzle.callback.JaneCallback;
import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * Created by xs on 2019/1/4.
 */

public class SaveBitmapHelper {
    public final static String TAG = "SaveBitmapHelper";
    public final static String CLIENT_STUB = "clientStub";

    private Context mContext;
    private JaneCallback<String> mSimpleJaneCallback;

    private ServiceConnection mServiceConnection;
    private IBinder.DeathRecipient mDeathRecipient;

    public SaveBitmapHelper(Context context, JaneCallback<String> mSimpleJaneCallback) {
        this.mContext = context;
        this.mSimpleJaneCallback = mSimpleJaneCallback;
    }

    public void startSaveBitmapService(final String jsonPath) {

        // 死亡回调
        mDeathRecipient = new IBinder.DeathRecipient() {

            @Override
            public void binderDied() {
                saveFail();
            }
        };

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                try {
                    service.linkToDeath(mDeathRecipient, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    saveFail();
                }

                ISaveBitmapService saveBitmapServiceAIDL = ISaveBitmapService.Stub.asInterface(service);

                try {
                    saveBitmapServiceAIDL.saveBitmap(jsonPath);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    saveFail();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                saveFail();
            }
        };

        ISaveBitmapClient.Stub mClient = new ISaveBitmapClient.Stub() {
            @Override
            public void call(String savePath) throws RemoteException {
                saveSuccess(savePath);
            }
        };
        Intent intent = new Intent(mContext, SaveBitmapService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CLIENT_STUB, new Messenger(mClient));
        intent.putExtras(bundle);
        mContext.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private void saveSuccess(final String savePath) {
        if (mServiceConnection != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSimpleJaneCallback.onSuccess(savePath, null);
                    Log.e(TAG, "保存成功 : " + savePath);

                    Utils.fileScan(mContext, savePath);
                }
            }, 100);
        }
        unBindService();
    }

    private void saveFail() {
        if (mServiceConnection != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSimpleJaneCallback.onFailure(null, -1, null);
                    Log.e(TAG, "保存失败");
                }
            }, 200);
        }
        unBindService();
    }

    private void unBindService() {
        if (mServiceConnection != null) {
            mContext.unbindService(mServiceConnection);
            mServiceConnection = null;
        }
    }
}
