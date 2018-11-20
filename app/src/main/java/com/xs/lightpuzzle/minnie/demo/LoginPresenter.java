package com.xs.lightpuzzle.minnie.demo;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created by xs on 2018/11/13.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    public void login(String username, String password){
        if (username == null || username.equals("")) {
            ifViewAttached(new ViewAction<LoginView>() {
                @Override
                public void run(@NonNull LoginView view) {
                    view.onLoginFailure(LoginView.USERNAME_OR_PASSWORD_EMPTY);
                }
            });
            return;
        }else if (password == null || password.equals("")) {
            ifViewAttached(new ViewAction<LoginView>() {
                @Override
                public void run(@NonNull LoginView view) {
                    view.onLoginFailure(LoginView.USERNAME_OR_PASSWORD_EMPTY);
                }
            });
            return;
        }

        Listener<String> listener = new Listener<String>() {
            @Override
            public void onSuccess(String s) {
                ifViewAttached(new ViewAction<LoginView>() {
                    @Override
                    public void run(@NonNull LoginView view) {
                        view.onLoginSuccess();
                    }
                });
            }

            @Override
            public void onFailure(final int code) {
                ifViewAttached(new ViewAction<LoginView>() {
                    @Override
                    public void run(@NonNull LoginView view) {
                        view.onLoginFailure(code);
                    }
                });
            }
        };

        LoginModel.login(username, password, listener);
    }

}
