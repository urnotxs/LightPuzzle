package com.xs.lightpuzzle.demo.a_mvp_demo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby3.mvp.layout.MvpFrameLayout;
import com.xs.lightpuzzle.R;

/**
 * Created by xs on 2018/11/13.
 */
/**
 * @author xs
 * @description  MVP 设计模式
 * @since 2019/11/13
 */

public class TestFrameLayout extends MvpFrameLayout<LoginView, LoginPresenter>
        implements LoginView {

    EditText username;
    EditText password;

    public TestFrameLayout(Context context) {
        super(context);
        inflate(context, R.layout.test_activity, this);

        username = findViewById(R.id.test_username);
        password = findViewById(R.id.test_password);
        Button loginBtn = findViewById(R.id.test_login);

        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().login(username.getText().toString(), password.getText().toString());
            }
        });
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onLoginSuccess() {
        Log.e("xs", "登陆成功");
    }

    @Override
    public void onLoginFailure(int code) {
        switch (code) {
            case LoginView.USERNAME_OR_PASSWORD_EMPTY:
                Log.e("xs", "账号或密码不能为空");
                break;
            case LoginView.USERNAME_OR_PASSWORD_ERROR:
                Log.e("xs", "账号或密码错误");
                break;
            case LoginView.SERVER_ERROR:
                Log.e("xs", "服务器错误");
                break;
        }
    }
}
