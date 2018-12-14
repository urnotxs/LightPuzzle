package com.xs.lightpuzzle.puzzle.view.textedit.textinterface;

/**
 * Created by pocouser on 2015/12/18 0018.
 */
public interface OnInputListener {

    void onSave(String text);

    void changeText(String text);

    void onopenText();

    void changeSize(float size);

//    void changeFont(String font);

    void changeFont(String font, boolean down);//第二参数down拼图时使用，拼图页不使用使用

    void changeColor(String color);

    void openInputMethod();

    void changeDownFont(boolean down);//这个方法保存页添加文本时使用，拼图页不使用该方法
}
