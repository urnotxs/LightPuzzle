package com.xs.lightpuzzle.puzzle;

public class PuzzleMode {
    //普通模板
    public static final int MODE_WAG = 0x4001;
    //拼接
    public static final int MODE_JOIN = 0x4003;
    //长图
    public static final int MODE_LONG = 0x4004;
    //视频
    public static final int MODE_VIDEO = 0x4005;
    //布局
    public static final int MODE_LAYOUT = 0x4006;
    //基础拼接
    public static final int MODE_LAYOUT_JOIN = 0x4007;

    public static int getMode(int theme) {
//        switch (theme) {
//            case CATEGORY.LONG_COLLAGE:
//            case CATEGORY.UNIVERSAL_LONG_COLLAGE:
//                return MODE_LONG;
//            case CATEGORY.COLLAGE:
//                return MODE_JOIN;
//            case CATEGORY.VIDEO:
//                return MODE_VIDEO;
//            case CATEGORY.LAYOUT:
//                return MODE_LAYOUT;
//            default:
//                return MODE_WAG;
//        }
        return 0;
    }
}
