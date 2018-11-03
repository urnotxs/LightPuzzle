package com.xs.lightpuzzle;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by xs on 2018/11/2.
 */

@RuntimePermissions
public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.welcome_iv_logo)
    AppCompatImageView mLogoImage;
    @BindView(R.id.welcome_txt_intro)
    AppCompatTextView mIntroText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        ViewAnimator.animate(mLogoImage)
                .dp()
                .translationY(48, 0)
                .alpha(0.3F, 1)
                .duration(800)
                .andAnimate(mIntroText)
                .dp()
                .translationY(64, 0)
                .alpha(0.3F, 1)
                .duration(1067)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        WelcomeActivityPermissionsDispatcher
                                .requestPermissionWithPermissionCheck(WelcomeActivity.this);
                    }
                })
                .start();

    }

    @OnClick(R.id.welcome_iv_logo)
    public void click() {
        Toast.makeText(this, "is a click", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission({Manifest.permission.INTERNET})
    public void requestPermission() {
        Navigator.navigatorToMaterrailListActivity(WelcomeActivity.this);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        WelcomeActivityPermissionsDispatcher
                .onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
