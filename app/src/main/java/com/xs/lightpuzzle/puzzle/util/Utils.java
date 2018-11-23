package com.xs.lightpuzzle.puzzle.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static android.content.pm.PackageManager.GET_INTENT_FILTERS;
import static android.content.pm.PackageManager.GET_META_DATA;
import static android.content.pm.PackageManager.GET_RESOLVED_FILTER;
import static android.content.pm.PackageManager.MATCH_DEFAULT_ONLY;

/**
 * Created by xs on 2018/04/27
 */
public class Utils extends UtilsInitializer {

    public static void hideKeyboard(Context context, IBinder iBinder) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        if (imm != null) {
            imm.hideSoftInputFromWindow(iBinder, 0);
        }
    }

    /**
     * 适配华为的底部占屏高
     */
    public static int getRealPixel6(int pxSrc, int virtualBarHeigh) {
//        int resultPx = pxSrc * (sScreenH - virtualBarHeigh) / 1280;
        int resultPx = pxSrc * (sScreenH + virtualBarHeigh) / 1280 - virtualBarHeigh;
        if (pxSrc != 0 && resultPx == 0) {
            resultPx = 1;
        }
        return resultPx;
    }

    // **获取华为虚拟功能键高度 */
    public static int getVirtualBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    private static int sCount;

    //刷新文件
    public static void sendScanBroadcast(Context context, String filePath) {
        File scanFile = new File(filePath);
        if (scanFile != null && scanFile.exists()) {
            Uri data = Uri.fromFile(scanFile);
            File external = Environment.getExternalStorageDirectory();
            if (external == null) {
                return;
            }
            String externalDir = external.getPath();

            if (externalDir == null) {
                return;
            }
            if (filePath.startsWith(externalDir) == false) {
                return;
            }
            if (context != null) {
                context.sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
            }
        }
    }

//    public static void updateSysImg(Context context, String image) {
//        if (image == null || image.length() == 0) {
//            return;
//        }
//        //重命名->delete->重命名回来，因为cr.delete这句会同时删除图片，这样做避免图片被删
//        long time = new File(image).lastModified();
//        String temp = image;
//        int pos = image.lastIndexOf('.');
//        if (pos != -1) {
//            temp = image.substring(0, pos) + "-temp" + image.substring(pos);
//            if (new File(image).renameTo(new File(temp)) == false) {
//                temp = image;
//                PLog.d("SF", "temp=>" + temp);
//            }
//        }
//        if (temp != image) {
//            try {
//                ContentResolver cr = context.getContentResolver();
//                String where = Media.DATA + "='" + image + "'";
//                if (cr != null) {
//                    cr.delete(Media.EXTERNAL_CONTENT_URI, where, null);
//                }
//            } catch (Exception e) {
//            }
//            if (new File(temp).renameTo(new File(image)) == true) {
//                File file = new File(image);
//                file.setLastModified(time);
//            }
//        }
//        Uri data = insertImgToSys(context, image);
//        if (context != null) {
//            PickerManager.getInstance(context).setUpDataAll();
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
//        }
//    }

    public static String getPhotoSavePath() {
        String out = null;
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (dcim != null) {
            out = dcim.getAbsolutePath() + File.separator + "Camera" + File.separator;
        } else {
            out = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera" + File.separator;
        }
        //魅族的默认相册路径不同，原来的路径图库不显示
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer != null) {
            manufacturer = manufacturer.toLowerCase(Locale.getDefault());
            if (manufacturer.contains("meizu")) {
//                out = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Camera" + File.separator ;
                //魅族本地录像系统变了, 草
                out = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File
                        .separator + "Video" + File.separator;
            }
        }
        return out;
    }

    // 获取默认保存文件夹
    public static String getDefaultSavePath() {
        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard == null
                || !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        String strDir = sdcard.getAbsolutePath() + "/DCIM/Camera"; // 每一个App都应该在这里该路径
        // 魅族的默认相册路径不同, 原来的路径图库不显示
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer != null) {
            manufacturer = manufacturer.toLowerCase();
            if (manufacturer.contains("meizu")) {
                String temp = sdcard.getAbsolutePath() + "/Camera";
                if (new File(temp).exists()) {
                    strDir = temp;
                }
            }
        }
        File picdir = new File(strDir);
        if (picdir.exists() == false) {
            if (false == picdir.mkdirs()) {
                return null;
            }
        }
        return strDir;
    }

    public static String getPath(Context context, Uri uri) {
        String out = null;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Video.Media.DATA}, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            if (column_index > -1) {
                cursor.moveToFirst();
                out = cursor.getString(column_index);
            }
            cursor.close();
        }
        return out;
    }

    /**
     * 插入数据库;
     */
    public static Uri insertImgToSys(Context context, String picPath) {
        File file = new File(picPath);
        if (!file.exists()) {
            return null;
        }
        ContentResolver resolver = context.getContentResolver();
        int degree = Utils.getJpgRotation(picPath);
        long dateTaken = System.currentTimeMillis();
        long size = file.length();
        String fileName = file.getName();
        ContentValues values = new ContentValues(7);
        values.put(Media.DATE_TAKEN, dateTaken);//时间;
        values.put(Media.DATE_MODIFIED, dateTaken / 1000);//时间;
        values.put(Media.DATE_ADDED, dateTaken / 1000);//时间;
        values.put(ImageColumns.DATA, picPath);//路径;
        values.put(Media.DISPLAY_NAME, fileName);//文件名;
        values.put(Media.ORIENTATION, degree);//角度;
        values.put(Media.SIZE, size);//图片的大小;
        Uri uri = null;
        try {
            if (resolver != null) {
                uri = resolver.insert(Media.EXTERNAL_CONTENT_URI, values);
            }
        } catch (Throwable th) {
        }
        return uri;
    }

    /**
     * 判断系统是否安装了浏览器
     */
    @SuppressWarnings("WrongConstant")
    public static boolean hasBrowser(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://"));
        List<ResolveInfo> list = pm.queryIntentActivities(intent, GET_INTENT_FILTERS);
        final int size = (list == null) ? 0 : list.size();
        return size > 0;
    }

    /**
     * 插入数据库;
     */
    public static Uri newImage(Context context, String picPath) {
        File file = new File(picPath);
        if (!file.exists()) {
            return null;
        }
        ContentResolver resolver = context.getContentResolver();
        int degree = Utils.getJpgRotation(picPath);
        long dateTaken = System.currentTimeMillis();
        long size = file.length();
        String fileName = file.getName();
        ContentValues values = new ContentValues(7);
        values.put(Media.DATE_TAKEN, dateTaken);//时间;
        values.put(Media.DATE_MODIFIED, dateTaken / 1000);//时间;
        values.put(Media.DATE_ADDED, dateTaken / 1000);//时间;
        values.put(ImageColumns.DATA, picPath);//路径;
        values.put(Media.DISPLAY_NAME, fileName);//文件名;
        values.put(Media.ORIENTATION, degree);//角度;
        values.put(Media.SIZE, size);//图片的大小;
        Uri uri = null;
        try {
            if (resolver != null) {
                uri = resolver.insert(Media.EXTERNAL_CONTENT_URI, values);
            }
        } catch (Throwable th) {
        }
        return uri;
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
        double avaliableMem = Utils.getAvailableMem();
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

    public static int getJpgRotation(String img) {
        if (img == null) {
            return 0;
        }

        if (!img.toLowerCase().endsWith(".jpg") && !img.toLowerCase().endsWith(".img") && !img
                .toLowerCase().endsWith(".ing") && !img.toLowerCase().endsWith(".dat")) {
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

    public static Bitmap scaleBitmap(Bitmap bmp, int size, boolean keepQuality) {
        Config config = bmp.getConfig();
        if (config == null) {
            config = Config.ARGB_8888;
        }
        return scaleBitmap(bmp, size, config, keepQuality);
    }

    public static Bitmap scaleBitmap(Bitmap bmp, int size, Config config, boolean keepQuality) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if (keepQuality == true && w < size && h < size) {
            return bmp;
        }
        int dw = 0;
        int dh = 0;
        float r1 = (float) w / (float) h;
        if (r1 < 1) {
            dh = size;
            dw = (int) (size * r1);
        } else {
            dw = size;
            dh = (int) (size / r1);
        }
        Bitmap bitmap = scaleBitmap(bmp, dw, dh, config);
        return bitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int w, int h, Config config) {
        Bitmap bmp = null;
        while (true) {
            try {
                if (w < 1) {
                    w = 1;
                }
                if (h < 1) {
                    h = 1;
                }
                bmp = Bitmap.createBitmap(w, h, config);
                break;
            } catch (OutOfMemoryError e) {
                float r1 = (float) w / (float) h;
                if (r1 < 1) {
                    h = h - 100;
                    w = (int) (h * r1);
                } else {
                    w = w - 100;
                    h = (int) (w / r1);
                }
            }
        }
        if (bmp != null) {
            Canvas canvas = new Canvas(bmp);
            canvas.setDrawFilter(
                    new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.drawBitmap(bitmap, null, new Rect(0, 0, w, h), null);
        }
        return bmp;
    }

    private static String TAG = "Utils";

    /**
     * 检查是否是中文
     */
    public static boolean checkChineseChar(String str) {
        for (int i = 0; str != null && i < str.length(); i++) {
            String bb = str.substring(i, i + 1);
            boolean cc = Pattern.matches("[\u4E00-\u9FA5]", bb);
            if (cc) {
                return true;
            }
        }
        return false;
    }

    /**
     * 随机生成一个字符串
     *
     * @param length
     * @return
     */

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 检查是否是英文
     */
    public static boolean checkEnglishChar(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }


    //调用美人相机
    public static final int RESULT_FOR_CALLBEAUTYCAMERA = 1113;
    public static final int SUCCESS = 1114;
    public static final int UPDATE = 1115;
    public static final int DOWNLOAD = 1116;

    public static void callBeautyCamera(Activity c, String image, boolean needBack)//跳了不用回来
    {

        try {
            Intent intent = new Intent();
            intent.setAction("my.beautyCamera.EDIT");
            intent.putExtra("pic", image);
            intent.putExtra("type", "tietu");
            intent.putExtra("needBack", needBack);
            if (needBack) {
                c.startActivityForResult(intent, RESULT_FOR_CALLBEAUTYCAMERA);
            } else {
                c.startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    //http://m.poco.cn/app/beauty-camera.php
    public static int GetAppState(Context context) {
        int out = SUCCESS;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("my.beautyCamera", 0);
            int versionCode = info.versionCode;
            if (versionCode >= 100) {
                out = SUCCESS;
            } else {
                out = UPDATE;
            }
        } catch (Throwable e) {
            out = DOWNLOAD;
        }
        return out;
    }

    public static boolean isExitBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setComponent(null);
        List<ResolveInfo> rList = context.getPackageManager().
                queryIntentActivities(intent, MATCH_DEFAULT_ONLY |
                        GET_RESOLVED_FILTER);
        final int browsersize = rList.size();

        return browsersize > 0;
    }

    /**
     * 删除目录下所有文件
     */
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    /**
     * @param bmp          背景图 透明    颜色 |     |
     * @param blurColor    - 0x ** 000000 模糊度
     * @param overlayColor 模糊后叠加颜色
     */
//    public static Bitmap largeRblur4(Bitmap bmp, int blurColor, int overlayColor) {
//        if (bmp != null && !bmp.isRecycled()) {
//            Bitmap tempBmp = null;
//            tempBmp = filter.fakeGlass(bmp, blurColor);
//            Canvas canvas = new Canvas(tempBmp);
//            canvas.drawColor(overlayColor, PorterDuff.Mode.SRC_OVER);
//            return tempBmp;
//        }
//        return null;
//    }

    /**
     * 正则表达式是否含是在该字符集中
     *
     * @param strCheck 检验的字符串
     * @param regEx    正则表达式
     */
    public static boolean isInGather(String strCheck, String regEx) throws PatternSyntaxException {
        //生成Pattern对象并且编译一个正则表达式regEx
        Pattern p = Pattern.compile(regEx);
        //用Pattern类的matcher()方法生成一个Matcher对象
        return p.matcher(strCheck).matches();
    }


    public static int getTextWidth(TextView textView, String text) {
        TextPaint textPaint = textView.getPaint();
        float textPaintWidth = textPaint.measureText(text);
        return (int) textPaintWidth;
    }


    public static Bitmap largeRblur1(Bitmap bmp) {
//        if (bmp != null) {
//            Bitmap tempBmp = null;
//            tempBmp = filter.fakeGlass(bmp, 0x66000000);
//            return tempBmp;
//        }
//        return null;
//    }
        if (bmp != null) {
            return bmp;
        }
        return null;
    }

    public static Bundle getApplicationMetaData(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.metaData;
        }
        return null;
    }

    /**
     * 两条直线的交点
     *
     * @param line1 长度为4	直线1
     * @param line2 同上	直线2
     * @return
     */
    public static float[] crossPoints(float[] line1, float[] line2) {
        float x, y;
        float x1, y1, x2, y2;
        float x3, y3, x4, y4;
        if (line1 != null && line2 != null && line1.length == 4 && line2.length == 4) {
            x1 = line1[0];
            y1 = line1[1];
            x2 = line1[2];
            y2 = line1[3];
            x3 = line2[0];
            y3 = line2[1];
            x4 = line2[2];
            y4 = line2[3];

            boolean flag1 = false;
            boolean flag2 = false;
            float k1 = Float.MAX_VALUE, k2 = Float.MAX_VALUE;

            if ((x1 - x2) == 0)
                flag1 = true;
            if ((x3 - x4) == 0)
                flag2 = true;

            if (!flag1)
                k1 = (y1 - y2) / (x1 - x2);
            if (!flag2)
                k2 = (y3 - y4) / (x3 - x4);

            if (k1 == k2)
                return null;

            if (flag1) {
                if (flag2)
                    return null;
                x = x1;
                if (k2 == 0) {
                    y = y3;
                } else {
                    y = k2 * (x - x4) + y4;
                }
            } else if (flag2) {
                x = x3;
                if (k1 == 0) {
                    y = y1;
                } else {
                    y = k1 * (x - x2) + y2;
                }
            } else {
                if (k1 == 0) {
                    y = y1;
                    x = (y - y4) / k2 + x4;
                } else if (k2 == 0) {
                    y = y3;
                    x = (y - y2) / k1 + x2;
                } else {
                    x = (k1 * x2 - k2 * x4 + y4 - y2) / (k1 - k2);
                    y = k1 * (x - x2) + y2;
                }
            }
            float[] out = new float[2];
            out[0] = x;
            out[1] = y;
            return out;

        }
        return null;
    }
}
