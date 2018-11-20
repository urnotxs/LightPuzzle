package com.xs.lightpuzzle.puzzle.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Lin on 2016/12/7.
 */
public class AnimUtils {

    /**
     * 设置可以直接使用平移动画
     *
     * @param view
     * @param trans_x     x轴方向移动的距离
     * @param trans_y     y轴方向移动的距离
     * @param duration    动画执行时间
     * @param isFillAfter true:表示需要view停留在动画结束的时候;false:表示只是动画执行到结束位置,重启动画仍然回到原点
     */

    public static void setTransAnim(final View view, final float trans_x, final float trans_y,
                                    long duration, final boolean isFillAfter, final AnimEndCallBack callBack) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, trans_x,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, trans_y);
        animation.setDuration(duration);
        animation.setFillAfter(!isFillAfter);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFillAfter) {
                    view.clearAnimation();
                    int left = view.getLeft();
                    if (trans_x <= 1 && trans_x >= -1) {
                        left += trans_x * view.getWidth();
                    } else {
                        left += trans_x;
                    }
                    int top = view.getRight();
                    if (trans_y <= 1 && trans_y >= -1) {
                        left += trans_y * view.getHeight();
                    } else {
                        left += trans_y;
                    }
                    int right = left + view.getWidth();
                    int botton = top + view.getHeight();
                    view.layout(left, top, right, botton);
                }
                if (callBack != null) {
                    callBack.endCallBack(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    /**
     * 获取平移动画(用于添加入动画集,监听效果自己加)
     */

    public static void setTransAnim(final View view, float form_x, float to_x, float from_y, float
            to_y, long duration, final AnimEndCallBack callBack) {
            view.clearAnimation();
        if (view.getAnimation() == null){
            TranslateAnimation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, form_x, Animation.RELATIVE_TO_SELF, to_x,
                    Animation.RELATIVE_TO_SELF, from_y, Animation.RELATIVE_TO_SELF, to_y);
            animation.setDuration(duration);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                    if (callBack != null) {
                        callBack.endCallBack(animation);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        }
    }

    public interface AnimEndCallBack {
        void endCallBack(Animation animation);
    }

    /**
     * 设置淡入淡出效果
     *
     * @param view
     * @param alpha_b  动画开始执行的时候透明度
     * @param alpha_e  动画结束的时候的透明度
     * @param duration
     */

    public static void setAlphaAnim(final View view, float alpha_b, float alpha_e, long duration, final AnimEndCallBack callBack) {
        AlphaAnimation animation = new AlphaAnimation(alpha_b, alpha_e);
        animation.setDuration(duration);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (callBack != null) {
                    callBack.endCallBack(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    /**
     * 获取平移动画(用于添加入动画集,监听效果自己加)
     * @return
     */

    public static TranslateAnimation getTransAnim(float from_x, float to_x, float from_y, float to_y, long duration) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, from_x, Animation.RELATIVE_TO_SELF, to_x,
                Animation.RELATIVE_TO_SELF, from_y, Animation.RELATIVE_TO_SELF, to_y);
        animation.setDuration(duration);
        return animation;
    }

    /**
     * 获取淡入淡出效果(用于添加入动画集,监听效果自己加)
     * @return
     */

    public static AlphaAnimation getAlphaAnim(float alpha_b, float alpha_e, long duration) {
        AlphaAnimation animation = new AlphaAnimation(alpha_b, alpha_e);
        animation.setDuration(duration);
        return animation;
    }

}
