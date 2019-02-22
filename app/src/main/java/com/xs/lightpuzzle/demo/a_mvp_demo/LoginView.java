package com.xs.lightpuzzle.demo.a_mvp_demo;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by xs on 2018/11/13.
 */

public interface LoginView extends MvpView {

    public static final int USERNAME_OR_PASSWORD_EMPTY = 0x01;
    public static final int USERNAME_OR_PASSWORD_ERROR = 0x02;
    public static final int SERVER_ERROR = 0x03;

    void onLoginSuccess();

    void onLoginFailure(int code);
}
