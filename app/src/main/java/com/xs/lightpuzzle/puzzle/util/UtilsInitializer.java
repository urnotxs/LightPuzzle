package com.xs.lightpuzzle.puzzle.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UtilsInitializer {

    public static float sDensity;
    public static float sDensityDpi;
    public static int sScreenW;
    public static int sScreenH;
    private static int sCount;
    private static ActivityManager sActivityManager;

    public static void init(Activity activiy) {
        Display dis = activiy.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        dis.getMetrics(dm);
        int h = dis.getHeight();
        int w = dis.getWidth();
        sScreenW = w < h ? w : h;
        sScreenH = w < h ? h : w;
        sDensity = dm.density;
        sDensityDpi = dm.densityDpi;
        sActivityManager = (ActivityManager) activiy.getSystemService(Activity.ACTIVITY_SERVICE);
        // 前人挖坑，这么多地方用，用的时候才init
        ShareData.InitData(activiy);
    }

    public static boolean isNotInit() {
        return sScreenW == 0 || sScreenH == 0;
    }

    public static int getScreenW() {
        return sScreenW;
    }

    public static int getScreenH() {
        return sScreenH;
    }

    /**
     * 将dip转换成px
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * sDensity + 0.5f);
    }

    /**
     * 将px转换成dip
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / sDensity + 0.5f);
    }

    public static int getRealPixel(int pxSrc) {
        return (int) (pxSrc * sDensity / 1.5);
    }

    public static int getRealPixel2(int pxSrc) {
        return (int) (pxSrc * sScreenW / 480);
    }

    public static int getRealPixel3(int pxSrc) {
        int resultPx = (int) (pxSrc * sScreenW / 720);
        if (pxSrc != 0 && resultPx == 0) {
            resultPx = 1;
        }
        return resultPx;
    }

    public static int getRealPixel4(int pxSrc) {
        int resultPx = (int) (pxSrc * sScreenW / 1080);
        if (pxSrc != 0 && resultPx == 0) {
            resultPx = 1;
        }
        return resultPx;
    }

    public static int getRealPixel_w(int pxSrc) {
        return (int) (pxSrc * sScreenW / 480);
    }

    public static int getRealPixel_h(int pxSrc) {
        return (int) (pxSrc * sScreenH / 800);
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(
                        localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 设置输入框光标颜色
     */
    public static void setCursorDrawable(EditText editText, int colorRes) {
        if (editText == null) {
            return;
        }
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, colorRes);
        } catch (Exception ignored) {
        }
    }

    public static int computeSampleSize(int w, int h) {
        int bigOne = w > h ? w : h;
        return bigOne / 640;
    }

    /**
     * 获取手机的最大运行内存
     */
    public static int getMaxMemory() {
        return (int) (Runtime.getRuntime().maxMemory() / 1024.0f / 1024.0f);
    }

    public static String getSdcardPath() {
        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard == null) {
            return "";
        }
        return sdcard.getPath();
    }

    /**
     * 获取SD卡剩余内存大小
     */
    public static long getSdcardAvaiableSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File filePath = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(filePath.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取手机运行内存的剩余大小
     */
    public static double getAvailableMem() {
        if (sActivityManager != null) {
            MemoryInfo outInfo = new MemoryInfo();
            sActivityManager.getMemoryInfo(outInfo);
            return outInfo.availMem / 1024.0f / 1024.0f;
        }
        return 0;
    }

    public static int get_real_w(int w) {
        w = (int) ((float) w / 480.0f * UtilsInitializer.sScreenW);
        return w;
    }

    public static int get_real_h(int h) {
        h = (int) ((float) h / 800.0f * UtilsInitializer.sScreenH);
        return h;
    }

    public static long getRemainderMem(Context context) {//手机可用内存
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 获取该进程当前还可用内存
     *
     * @return 返回的是字节数
     */
    public static long getCurMemoryCapacityCanUse(Context context) {
        long capacity = 0;
        if (context != null) {
            long maxMem = Runtime.getRuntime().maxMemory();//最大配额
            long totalMemory = Runtime.getRuntime().totalMemory();//已消耗
            capacity = maxMem - totalMemory;    //产能
            long remainMemory = getRemainderMem(context);// 手机可用内存
            capacity = capacity < remainMemory ? capacity : remainMemory;
        }
        return capacity;
    }

    /**
     * 代码创建Selector
     */
    public static StateListDrawable newSelector(Context context, Drawable idNormal,
                                                Drawable idPressed) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal;
        Drawable pressed = idPressed;
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 代码创建Selector
     */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed},
                context.getResources().getDrawable(idPressed));
        selector.addState(new int[]{android.R.attr.state_enabled},
                context.getResources().getDrawable(idNormal));
        return selector;
    }

    /**
     * 紧急提示窗口盒子
     */
    public static void msgBox(final Context context, final String content) {
        if (Thread.currentThread().getId() == 1) {
            AlertDialog dlg = new AlertDialog.Builder(context).create();
            dlg.setTitle("提示");
            dlg.setMessage(content);
            dlg.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                    (DialogInterface.OnClickListener) null);
            dlg.show();
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    AlertDialog dlg = new AlertDialog.Builder(context).create();
                    dlg.setTitle("提示");
                    dlg.setMessage(content);
                    dlg.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                            (DialogInterface.OnClickListener) null);
                    dlg.show();
                }
            });
        }
    }

    public static String md5sum(String filename) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(filename.getBytes());
            return md5ToHexString(md5.digest());
        } catch (Exception e) {
            return "";
        }
    }

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = md5ToHexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
            return "";
        }
        return resultString;
    }

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5ToHexString(byte[] b) {  //String to  byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 获取对应的完整缓存路径下的随机生成文件名缓存文件的完整路径
     */
    public static String getTempImageFile(Context context, String tmpPath) {
        sCount = (sCount + 1) % 0x80000000;
        String filename = "temp" + sCount + ".img";
        String path = null;
        String state = Environment.getExternalStorageState();
        boolean bSaveToMemory = false;
        double avaliableMem = UtilsInitializer.getAvailableMem();
        if (Environment.MEDIA_MOUNTED.equals(state) == false || avaliableMem > 40) {
            bSaveToMemory = true;
        }

        if (bSaveToMemory == true) {
            //存内存
            if (context == null) {
                return null;
            }
            File file = context.getApplicationContext().getFilesDir();
            path = file.getAbsolutePath();
            path += "/" + filename;
            return path;
        } else {
            //存SD卡
            File sdcard = Environment.getExternalStorageDirectory();
            path = sdcard.getAbsolutePath() + tmpPath;
            File picdir = new File(path);
            if (picdir.exists() == false) {
                if (false == picdir.mkdirs()) {
                    return null;
                }
            }
            path += "/" + filename;
            return path;
        }
    }

    /**
     * 获取对应的完整缓存路径下的随机生成并包含时间的文件名缓存文件的完整路径
     */
    public static String getImageFile(Context context, String tmpPath) {
        sCount = (sCount + 1) % 0x80000000;
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String strDate = df.format(date);
        String filename = strDate + "-" + sCount + ".jpg";
        String path = null;
        String state = Environment.getExternalStorageState();
        boolean bSaveToMemory = false;
        double avaliableMem = UtilsInitializer.getAvailableMem();
        if (Environment.MEDIA_MOUNTED.equals(state) == false || avaliableMem > 40) {
            bSaveToMemory = true;
        }

        if (bSaveToMemory == true) {
            //存内存
            if (context == null) {
                return null;
            }
            File file = context.getApplicationContext().getFilesDir();
            path = file.getAbsolutePath();
            path += "/" + filename;
            return path;
        } else {
            //存SD卡
            File sdcard = Environment.getExternalStorageDirectory();
            path = sdcard.getAbsolutePath() + tmpPath;
            File picdir = new File(path);
            if (picdir.exists() == false) {
                if (false == picdir.mkdirs()) {
                    return null;
                }
            }
            path += "/" + filename;
            return path;
        }
    }

    /**
     * @param v
     * @param type
     */
    public static void setLayerType(View v, String type) {
        try {
            Field field = View.class.getField(type);
            if (field != null) {
                int LAYER_TYPE = field.getInt(null);
                Method method = View.class.getMethod("setLayerType", int.class, Paint.class);
                if (method != null) {
                    method.invoke(v, new Object[]{LAYER_TYPE, null});
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 生成和时间相关的图片名
     */
    public static String makePhotoName(Context context) {
        return makePhotoName(context, ".jpg");
    }

    /**
     * 生成与时间相关的视频名
     */
    public static String makeVideoName(Context context) {
        return makePhotoName(context, ".mp4");
    }

    public static String makePhotoName(Context context, String type) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String strDate = df.format(date);
        String strRand = String.format("%d", (int) (Math.random() * 100));
        if (strRand.length() < 4) {
            strRand = "0000".substring(strRand.length()) + strRand;
        }
        if (context == null) {
            return strDate + strRand + type;
        }
        String str = "JA" + strDate + strRand + "-00-000000" + type;
        return str;
    }

    /**
     * 获取app里面的AndroidManifest.xml里面的VersionCodeAPP版本号，这个拿来判断是否更新了程序
     */
    public static int getAppVersionCodeSuffix(Context context, int def) {

        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    return pi.versionCode;
                }
            } catch (NameNotFoundException e) {
            }
        }
        return def;
    }

    public static String copyFileTo(String src, String dst) throws Exception {
        if (src == null || dst == null) {
            return null;
        }
        File fsrc = new File(src);
        if (fsrc.exists()) {
            int index = dst.lastIndexOf('/');
            if (index != -1) {
                File dir = new File(dst.substring(0, index));
                if (dir.exists() == false) {
                    if (dir.mkdirs() == false) {
                        return null;
                    }
                }
            }
            FileInputStream fis = new FileInputStream(fsrc);
            FileOutputStream fos = new FileOutputStream(dst);
            byte[] buffer = new byte[10240];
            int size = 0;
            while ((size = fis.read(buffer, 0, buffer.length)) != -1) {
                fos.write(buffer, 0, size);
            }
            fos.close();
            fis.close();
            return dst;
        }
        return null;
    }

    public static int getJpgRotation(String img) {
        if (img == null) {
            return 0;
        }
        if (img.endsWith(".jpg") == false && img.endsWith(".JPG") == false
                && img.endsWith(".dat") == false && img.endsWith(".dat") == false) {
            return 0;
        }
        ExifInterface exif;
        try {
            exif = new ExifInterface(img);
            String r = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            if (r != null && r.length() > 0) {
                int ori = Integer.parseInt(r);
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return 90;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return 180;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return 270;
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * 通过包名判断是否已经安装了app
     */

    public static boolean checkAppinstall(Context context, String packName) {
        if (context == null || TextUtils.isEmpty(packName)) {
            return false;
        }
        if (checkApkExist(context, packName)) {
            return true;
        }
        return isMobile_spExist(context, packName);
    }

    /**
     * 通过包名查找APK是否被安装
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            synchronized (context) {
                context.getPackageManager()
                        .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                return true;
            }
        } catch (NameNotFoundException e) {
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isMobile_spExist(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }
}
