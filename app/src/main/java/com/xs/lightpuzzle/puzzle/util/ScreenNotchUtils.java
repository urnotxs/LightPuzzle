package com.xs.lightpuzzle.puzzle.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by zwq on 2018/04/27 18:50.<br/><br/>
 * 判断是否是刘海屏
 */
public class ScreenNotchUtils {

    private static int sInit;
    public static boolean sHasNotch;

    /**
     * 是否有刘海
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInScreen(Context context) {
        if (sInit == 1) {
            return sHasNotch;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*if (Build.VERSION.SDK_INT >= 28*//*Build.VERSION_CODES.P*//*) {
                Window window = null;
                if (context instanceof Activity) {
                    window = ((Activity) context).getWindow();
                }
                if (window == null) {
                    return false;
                }
                View decorView = window.getDecorView();
                if (decorView != null) {
                    WindowInsets windowInsets = decorView.getRootWindowInsets();
                    if (windowInsets != null) {
                        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                        if (displayCutout.getBoundingRects() != null) {
                            sHasNotch = true;
                        }
                    }
                }
                sInit = 1;
                return sHasNotch;
            }*/
            String menufacturer = Build.MANUFACTURER.toUpperCase(Locale.CHINA);
            String model = Build.MODEL.toUpperCase(Locale.CHINA);
            //Log.i(TAG, "hasNotchInScreen: " + menufacturer + ", " + model);
            if ("HUAWEI".equals(menufacturer)) {//华为
                sHasNotch = huawei_HasNotchInScreen(context);

            } else if ("VIVO".equals(menufacturer)) {
                sHasNotch = vivo_HasNotchInScreen(context);

            } else if ("OPPO".equals(menufacturer)) {
                sHasNotch = oppo_HasNotchInScreen(context);

            } else if ("ONEPLUS".equals(menufacturer)) {
                if ("ONEPLUS A6000".equals(model) || "ONEPLUS A6003".equals(model)) {
                    sHasNotch = true;
                }
            } else if ("XIAOMI".equals(menufacturer)) {
                //property  ro.miui.notch  小米8 https://dev.mi.com/console/doc/detail?pId=1293
                if (getSystemPropertyInt(context, "ro.miui.notch", 0) == 1) {
                    sHasNotch = true;
                }
            }
        }
        sInit = 1;
        return sHasNotch;
    }

    /**
     * 根据给定的key返回int类型值.
     *
     * @param key 要查询的key
     * @param def 默认返回值
     * @return 返回一个int类型的值, 如果没有发现则返回默认值
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    private static Integer getSystemPropertyInt(Context context, String key, int def) throws IllegalArgumentException {
        Integer ret = def;
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = int.class;
            Method getInt = SystemProperties.getMethod("getInt", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Integer(def);
            ret = (Integer) getInt.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
        }
        return ret;
    }

    /**
     * 判断是否刘海屏接口
     *
     * @param context
     * @return
     */
    public static boolean huawei_HasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class cls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = cls.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(cls);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        } finally {
            return ret;
        }
    }

    /**
     * 刘海尺信息接口
     *
     * @param context
     * @return
     */
    public static int[] huawei_GetNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class cls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method m = cls.getMethod("getNotchSize");
            m.setAccessible(true);
            ret = (int[]) m.invoke(cls);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        } finally {
            return ret;
        }
    }

    /**
     * 设置应用窗口在华为刘海屏手机使用挖孔区
     *
     * @param window 应用页面window对象
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void huawei_SetFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class cls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = cls.getConstructor(WindowManager.LayoutParams.class);
            Object obj = con.newInstance(layoutParams);
            Method m = cls.getMethod("addHwFlags", int.class);
            m.setAccessible(true);
            m.invoke(obj, 0x00010000);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
        } catch (Exception e) {
        }
    }

    /**
     * 判断是否刘海屏接口
     *
     * @param context
     * @return
     */
    public static boolean vivo_HasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class cls = cl.loadClass("android.util.FtFeature");
            Method m = cls.getMethod("isFeatureSupport", int.class);
            m.setAccessible(true);
            ret = (boolean) m.invoke(cls, 0x00000020);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
        } finally {
            return ret;
        }
    }

    public static boolean oppo_HasNotchInScreen(Context context) {
        try {
            return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }
}
