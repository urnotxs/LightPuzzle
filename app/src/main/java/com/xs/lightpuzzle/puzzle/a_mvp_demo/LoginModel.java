package com.xs.lightpuzzle.puzzle.a_mvp_demo;

/**
 * Created by xs on 2018/11/13.
 */

public class LoginModel {

    public static void login(String username, String password, Listener<String> listener) {

        if (username.equals("admin") && password.equals("admin")) {
            listener.onSuccess(null);
        } else if (username.equals("server") && password.equals("server")) {
            listener.onFailure(LoginView.SERVER_ERROR);
        } else {
            listener.onFailure(LoginView.USERNAME_OR_PASSWORD_ERROR);
        }
    }

}
