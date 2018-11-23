package com.xs.lightpuzzle.puzzle.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xs on 2018/04/27
 */
public class ShareData {
    public static int m_screenWidth = 0;
    public static int m_screenHeight = 0;
    public static int m_screenRealWidth = 0;
    public static int m_screenRealHeight = 0;
    public static int m_dpi = 0;
    public static int m_realDpi = 0;//精确密度
    public static float m_resScale = 0;

    private static boolean sInit = false;
    public static int m_NavigationBarHeight = -1;
    public static float m_ratio = 9f / 16;  //宽高比

    public static boolean m_HasNotch;//是否是刘海屏
    public static int m_realStatusBarHeight;
    public static boolean m_HasShowVirtualKey;//是否有显示虚拟键

    /**
     * 初始化获取屏幕参数
     *
     * @param update true:重新获取数据
     * @return 成功/失败
     */
    public static boolean InitData(Context context, boolean update) {
        boolean out = false;
        if (!sInit || update) {
            try {
                if (context != null) {
                    Display display;
                    DisplayMetrics dm = new DisplayMetrics();
                    if (context instanceof Activity) {
                        display = ((Activity) context).getWindowManager().getDefaultDisplay();
                        display.getMetrics(dm);
                    } else {
                        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
                        display = wm.getDefaultDisplay();
                        display.getMetrics(dm);
                    }

                    boolean initDisplayInfo = false;
                    try {
                        if (Build.VERSION.SDK_INT <= 27) {
                            ClassLoader cl = ClassLoader.getSystemClassLoader();
                            Class<?> DisplayInfoClass = cl.loadClass("android.view.DisplayInfo");
                            Object o = DisplayInfoClass.newInstance();
                            Field appWidth = DisplayInfoClass.getDeclaredField("appWidth");
                            appWidth.setAccessible(true);
                            Field appHeight = DisplayInfoClass.getDeclaredField("appHeight");
                            appHeight.setAccessible(true);
                            Field logicalWidth = DisplayInfoClass.getDeclaredField("logicalWidth");
                            logicalWidth.setAccessible(true);
                            Field logicalHeight = DisplayInfoClass.getDeclaredField("logicalHeight");
                            logicalHeight.setAccessible(true);
                            Class<? extends Display> aClass = display.getClass();
                            Method getDisplayInfo = aClass.getDeclaredMethod("getDisplayInfo", DisplayInfoClass);
                            getDisplayInfo.setAccessible(true);
                            getDisplayInfo.invoke(display, o);
                            m_screenWidth = appWidth.getInt(o);
                            m_screenHeight = appHeight.getInt(o);
                            m_screenRealWidth = logicalWidth.getInt(o);
                            m_screenRealHeight = logicalHeight.getInt(o);
                            initDisplayInfo = true;
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                        initDisplayInfo = false;
                    }

                    if (!initDisplayInfo) {
                        m_screenWidth = dm.widthPixels;
                        m_screenHeight = dm.heightPixels;
                        if (m_screenWidth > m_screenHeight) {
                            m_screenWidth += m_screenHeight;
                            m_screenHeight = m_screenWidth - m_screenHeight;
                            m_screenWidth -= m_screenHeight;
                        }

                        m_screenRealWidth = m_screenWidth;
                        m_screenRealHeight = m_screenHeight;

                        if (Build.VERSION.SDK_INT >= 17) {
                            try {
                                Point p = new Point();
                                display.getRealSize(p);
                                m_screenRealWidth = p.x;
                                m_screenRealHeight = p.y;
                            } catch (Throwable t) {
                                t.printStackTrace();
                                try {
                                    Method method = Display.class.getMethod("getRealMetrics", new Class[]{DisplayMetrics.class});
                                    method.invoke(display, new Object[]{dm});
                                    m_screenRealWidth = dm.widthPixels;
                                    m_screenRealHeight = dm.heightPixels;
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    m_ratio = m_screenWidth * 1.0f / m_screenHeight;
                    m_dpi = dm.densityDpi;
                    m_realDpi = Math.min(Math.round(dm.xdpi), Math.round(dm.ydpi));
                    m_resScale = dm.density;
                    if (m_screenRealWidth > m_screenRealHeight) {
                        m_screenRealWidth += m_screenRealHeight;
                        m_screenRealHeight = m_screenRealWidth - m_screenRealHeight;
                        m_screenRealWidth -= m_screenRealHeight;
                    }

                    sInit = true;
                    out = true;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            m_HasNotch = ScreenNotchUtils.hasNotchInScreen(context);
            if (context instanceof Activity) {
                m_realStatusBarHeight = GetStatusBarHeight2((Activity) context);
            }

        } else {
            out = true;
        }
        return out;
    }

    public static boolean InitData(Context context) {
        return InitData(context, false);
    }

    public static int getScreenW() {
        return m_screenWidth;
    }

    public static int getScreenH() {
        return m_screenHeight;
    }

    /**
     * 素材以480x800屏幕为标准
     *
     * @param size
     * @return
     */
    public static int PxToDpi_hdpi(int size) {
        return (int) (size / 1.5f * m_resScale + 0.5f);
    }

    public static int getRealPixel2(int pxSrc) {
        return (int) (pxSrc * m_screenWidth / 480);
    }

    /**
     * @param size
     * @return
     * @deprecated use {@link #PxToDpi_hdpi(int)}
     */
    @Deprecated
    public static int PxToDpi(int size) {
        return PxToDpi_hdpi(size);
    }

    /**
     * 素材以720*1280屏幕为标准
     *
     * @param size
     * @return
     */
    public static int PxToDpi_xhdpi(int size) {
        //return PxToDpi_xhdpi2(size);
        return (int) (size / 2f * m_resScale + 0.5f);
    }

    public static int PxToDpi_xhdpi2(int size) {
        return (int) (size * m_realDpi / 320f + 0.5f);
    }

    /**
     * 素材以1080*1920屏幕为标准
     *
     * @param size
     * @return
     */
    public static int PxToDpi_xxhdpi(int size) {
        //return PxToDpi_xxhdpi2(size);
        return (int) (size / 3f * m_resScale + 0.5f);
    }

    public static int PxToDpi_xxhdpi2(int size) {
        return (int) (size * m_realDpi / 480f + 0.5f);
    }

    public static int getRealPixel_720P(int size) {
        return PxToDpi_xhdpi(size);
    }

    /**
     * 获取状态栏高度(顶部),即使全屏也能获取
     *
     * @param ac
     * @return
     */
    public static int GetStatusBarHeight2(Activity ac) {
        int out = 0;
        try {
            if (ac != null) {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                out = ac.getResources().getDimensionPixelSize(x);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * 获取当前状态栏高度(顶部),全屏状态下返回0
     *
     * @param ac
     * @return
     */
    public static int GetCurrentStatusBarHeight(Activity ac) {
        int out = 0;
        //非全屏,有状态栏
        if (ac != null && (ac.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0) {
            out = GetStatusBarHeight2(ac);
        }
        return out;
    }

    /**
     * 获取NavigationBar的高度
     */
    public static int getNavigationBarHeight(Context context) {
        if (m_NavigationBarHeight == -1) {
            m_NavigationBarHeight = 0;
            Resources rs = context.getResources();
            int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id > 0 && checkDeviceHasNavigationBar(context)) {
                m_NavigationBarHeight = rs.getDimensionPixelSize(id);
            }
        }
        return m_NavigationBarHeight;
    }

    public static int getNavigationBarHeight2(Context context) {
        if (Build.VERSION.SDK_INT < 17) {
            return getNavigationBarHeight(context);
        }
        return 0;
    }

    /**
     * 判断是否存在NavigationBar
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 是否有物理Home键，返回true则有，否则为false
     */
    public static boolean hasPhysicalKey(Context context) {
        boolean hasPermanentMenuKey = false;
        if (Build.VERSION.SDK_INT >= 14) {
            ViewConfiguration vc = ViewConfiguration.get(context);
            try {
                Method m = ViewConfiguration.get(context).getClass().getMethod("hasPermanentMenuKey");
                hasPermanentMenuKey = (Boolean) m.invoke(vc);
            } catch (Throwable e) {
                e.printStackTrace();
                hasPermanentMenuKey = false;
            }
        }
        return hasPermanentMenuKey;
    }

    public static int getCurrentVirtualKeyHeight(Activity activity) {
        int virtualKeyHeight = m_screenRealHeight - getCurrentScreenH(activity);
        if (virtualKeyHeight == 0 && Build.VERSION.SDK_INT < 17) {
            virtualKeyHeight = getNavigationBarHeight(activity);
        }
        return virtualKeyHeight;
    }


    public static int getCurrentScreenW(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getCurrentScreenH(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static boolean checkDeviceIsHasNavigationBar(Context activity) {
        // 特效手机判断
        if (Build.MODEL != null && (Build.MODEL.equals("NEM-AL10") || Build.MODEL.equals("H60-L02"))) {
            return true;
        }

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            // 有导航栏
            return true;
        }
        return false;
    }

    public static int getDeviceNavigationBarHeight(Context activity) {
        if (checkDeviceIsHasNavigationBar(activity)) {
            return getNavigationBarHeight(activity);
        } else {
            return 0;
        }
    }

    // 获取屏幕View被挡住的高度 必须在 onSizeChanged 重新初始化 ShareData
    public static int getViewCoverHeight() {
        return m_screenRealHeight - m_screenHeight;
    }

    /**
     * 沉浸模式 & 开启透明状态栏和透明虚拟按键
     * 在 onWindowFocusChanged(hasFocus:true) 时调用
     *
     * @param windowObj activity or dialog
     */
    public static void hideStatusAndNavigation(Object windowObj) {
        Window window = getWindow(windowObj);
        if (window != null) {
            changeWindowFlags(window, true, new int[]{
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN});
            int visibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(visibility);
        }
    }

    public static void hideStatusBar(Object windowObj) {
        Window window = getWindow(windowObj);
        if (window != null) {
            changeWindowFlags(window, true, new int[]{
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN});
            int visibility = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(visibility);
        }
    }

    public static Window getWindow(Object windowObj) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (windowObj == null) return null;
            Window window = null;
            if (windowObj instanceof Activity) {
                window = ((Activity) windowObj).getWindow();
            } else if (windowObj instanceof Dialog) {
                window = ((Dialog) windowObj).getWindow();
            }
            return window;
        }
        return null;
    }

    private static int changeWindowFlags(Window window, boolean isAddFlags, int[] allFlags) {
        if (window == null || allFlags == null) {
            return -1;
        }
        WindowManager.LayoutParams attrs = window.getAttributes();

        int newFlags = attrs.flags;
        for (int mask : allFlags) {
            newFlags = (newFlags & ~mask) | ((isAddFlags ? mask : 0) & mask);
        }
        //Log.i("vvv", "changeWindowFlags: " + attrs.flags + ", " + newFlags);
        if (attrs.flags != newFlags) {
            for (int flag : allFlags) {
                if (isAddFlags) {
                    window.addFlags(flag);
                } else {
                    window.clearFlags(flag);
                }
            }
            return 1;
        }
        return 0;
    }

    public static int showStatusAndNavigation(Object windowObj, int originSystemUiVisibility, boolean restore) {
        Window window = getWindow(windowObj);
        if (window != null) {
            if (originSystemUiVisibility == -1) {
                originSystemUiVisibility = window.getDecorView().getSystemUiVisibility();
            }
            changeWindowFlags(window, false, new int[]{
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION});

            int visibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
            if (restore) {
                visibility = originSystemUiVisibility;
            }
            if (visibility >= 0) {
                window.getDecorView().setSystemUiVisibility(visibility);
            }
            return originSystemUiVisibility;
        }
        return -1;
    }

    public static int showOrHideStatusAndNavigation(Object windowObj, boolean isShow, int originSystemUiVisibility, boolean restore) {
        Window window = getWindow(windowObj);
        if (window != null) {
            if (originSystemUiVisibility == -1) {
                originSystemUiVisibility = window.getDecorView().getSystemUiVisibility();
            }
            int visibility = -1;
            if (isShow) {
                changeWindowFlags(window, false, new int[]{
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION});

                visibility = View.SYSTEM_UI_FLAG_FULLSCREEN;

            } else {
                changeWindowFlags(window, true, new int[]{
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN});

                visibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
            if (restore) {
                visibility = originSystemUiVisibility;
            }
            if (visibility >= 0) {
                window.getDecorView().setSystemUiVisibility(visibility);
            }
            return originSystemUiVisibility;
        }
        return -1;
    }

    public static int showOrHideStatusAndNavigation(Object windowObj, boolean isShow, int originSystemUiVisibility, boolean restore, boolean hasNotch) {
        Window window = getWindow(windowObj);
        if (window != null) {
            if (originSystemUiVisibility == -1) {
                originSystemUiVisibility = window.getDecorView().getSystemUiVisibility();
            }
            int visibility = -1;
            if (isShow) {
                changeWindowFlags(window, false, new int[]{
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION});

                if (hasNotch) {
                    visibility = View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                } else {
                    visibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
                }

            } else {
                if (hasNotch) {
                    changeWindowFlags(window, true, new int[]{
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION});
                    visibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    //设置状态栏颜色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(0);
                    }
                    //让view不根据系统窗口来调整自己的布局
                    ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
                    if (mContentView != null) {
                        View mChildView = mContentView.getChildAt(0);
                        if (mChildView != null) {
                            ViewCompat.setFitsSystemWindows(mChildView, false);
                            ViewCompat.requestApplyInsets(mChildView);
                        }
                    }
                } else {
                    changeWindowFlags(window, true, new int[]{
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN});
                    visibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                }
            }
            if (restore) {
                visibility = originSystemUiVisibility;
            }
            if (visibility >= 0) {
                window.getDecorView().setSystemUiVisibility(visibility);
            }
            return originSystemUiVisibility;
        }
        return -1;
    }

    public static void setStatusBarColor(Object windowObj, int statusColor) {
        Window window = getWindow(windowObj);
        if (window != null) {
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            //设置状态栏颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(statusColor);
            }
            //让view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    public static int changeSystemUiVisibility(Object windowObj, boolean fullScreen) {
        Window window = getWindow(windowObj);
        if (window != null) {
            int visibility = fullScreen ? View.SYSTEM_UI_FLAG_FULLSCREEN : View.SYSTEM_UI_FLAG_VISIBLE;
            if (visibility >= 0) {
                window.getDecorView().setSystemUiVisibility(visibility);
            }
            return visibility;
        }
        return -1;
    }

    public static boolean hasStatusBar(Activity activity) {
        boolean out = false;
        if (activity != null) {
            if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0 &&
                    (activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                out = true;
            }
        }
        return out;
    }
}
