package com.xs.lightpuzzle.puzzle.msgevent.code;

/**
 * Created by xs on 2017/12/12.
 * 编辑页底部按钮监听
 */

public interface PuzzlesBottomMsgCode {

    //切换模板
    int CHANGE_TEMPALTE = 0x3000;
    //切换背景
    int CHANGE_BACKGROUND = 0x3001;
    //添加文本
    int ADD_TEXT = 0x3002;
    //添加签名
    int ADD_SIGNATURE = 0x3003;
    //添加标签
    int ADD_LABEL = 0x3004;
    //添加音乐
    int ADD_MUSIC = 0x3005;
    //预览
    int PREVIEW_VIDEO = 0x3006;
    //布局
    int CHANGE_LAYOUT = 0x3007;
    //线框
    int CHANGE_LINE_FRAME = 0x3008;
    // 调整
    int ADJUST_PIC_FOR_VIDEO = 0x3009;
    // 顺序播放
    int ORDER_PLAY = 0x300A;
}
