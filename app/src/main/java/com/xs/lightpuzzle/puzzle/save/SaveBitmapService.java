package com.xs.lightpuzzle.puzzle.save;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.google.gson.Gson;
import com.xs.lightpuzzle.constant.DirConstant;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.io.File;

import static com.xs.lightpuzzle.puzzle.save.SaveBitmapHelper.CLIENT_STUB;

/**
 * Created by xs on 2019/1/4.
 */

public class SaveBitmapService extends Service {

    // 服务端的stub，返回给客户端调用
    private SaveBitmapBinder mSaveBitmapBinder;
    // 客户端的stub 由客户端绑定传过来
    private ISaveBitmapClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mSaveBitmapBinder = new SaveBitmapBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null && intent.getExtras() != null){
            Messenger messenger = intent.getExtras().getParcelable(CLIENT_STUB);
            if (messenger != null){
                mClient = ISaveBitmapClient.Stub.asInterface(messenger.getBinder());
            }
        }
        return mSaveBitmapBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mClient = null;
        System.exit(0);
        return super.onUnbind(intent);
    }

    private class SaveBitmapBinder extends ISaveBitmapService.Stub {

        @Override
        public void saveBitmap(final String jsonPath) throws RemoteException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 做耗时操作
                    String savePath = Utils.getDefaultSavePath() + File.separator +
                            DirConstant.PUZZLE_SAVE_START_NAME + System.currentTimeMillis() + ".jpg";
                    String jsonStr = FileIOUtils.readFile2String(jsonPath);
                    PuzzlesInfo puzzlesInfo = new Gson().fromJson(jsonStr, PuzzlesInfo.class);
                    if (puzzlesInfo != null){
                        puzzlesInfo.setSave();

                        puzzlesInfo.reInit();
                        puzzlesInfo.initBitmap(SaveBitmapService.this);
                        Bitmap bitmap = Bitmap.createBitmap(puzzlesInfo.getOutPutRect().width(), puzzlesInfo
                                .getOutPutRect().height(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                        Paint paint = new Paint();
                        paint.setFilterBitmap(true);
                        paint.setColor(0xff000000);
                        paint.setAntiAlias(true);
                        canvas.drawColor(Color.WHITE);
                        puzzlesInfo.draw(canvas);
                        puzzlesInfo.recycle();
                        final boolean isSuccess = ImageUtils.save(bitmap, savePath, Bitmap.CompressFormat.JPEG, true);

                        if (isSuccess){
                            // 通知客户端做下一步处理
                            sendMessageToClient(savePath);
                        }
                    }

                }
            }).start();
        }
    }

    private void sendMessageToClient(String savePath) {
        // 通知客户端做下一步处理
        try {
            mClient.call(savePath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
