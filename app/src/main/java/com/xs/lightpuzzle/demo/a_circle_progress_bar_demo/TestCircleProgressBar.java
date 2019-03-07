package com.xs.lightpuzzle.demo.a_circle_progress_bar_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.xs.lightpuzzle.R;

/**
 * @author xs
 * @description
 * @since 2019/2/22
 */

public class TestCircleProgressBar extends FrameLayout {
    private ProgressBarsView appleWatchProgressBar;
    private ProgressBarsView ddqProgressBar;
    private ChildProgressBar childProgressbar;
    private Button btnRefresh;

    public TestCircleProgressBar(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_circle_progressbar_layout, null);
        addView(view);
        {
            appleWatchProgressBar = view.findViewById(R.id.apple_watch_progress_bar_array);
            ddqProgressBar = view.findViewById(R.id.ddq_progress_bar_array);
            childProgressbar = view.findViewById(R.id.ddq_progress_bar);
            btnRefresh = view.findViewById(R.id.refresh_btn);
            btnRefresh.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    appleWatchProgressBar.showProgress();
                    ddqProgressBar.showProgress();

                    childProgressbar.setText("胜");
                    childProgressbar.refreshProgress();
                }
            });

            appleWatchProgressBar.clearProgressBar();
            appleWatchProgressBar.addProgressBar("胜", 0XFFFF0C38, 100, 60);
            appleWatchProgressBar.addProgressBar("平", 0XFFA0FF00, 100, 45);
            appleWatchProgressBar.addProgressBar("负", 0XFF19D8D1, 100, 30);
            appleWatchProgressBar.showProgress();

            ddqProgressBar.clearProgressBar();
            ddqProgressBar.addProgressBar("胜", 0XFFEA595C, 100, 88);
            ddqProgressBar.addProgressBar("平", 0XFFEAFF00, 100, 65);
            ddqProgressBar.addProgressBar("负", 0XFFD7D5DA, 100, 30);
            ddqProgressBar.showProgress();

            childProgressbar.setText("胜");
            childProgressbar.refreshProgress();
//            progressbar.addProgressBar("胜", 0XBBEA595C, 100, 88);
//            progressbar.addProgressBar("平", 0XBBD6A20E, 100, 65);
//            progressbar.addProgressBar("负", 0XBBD7D5DA, 100, 30);

//            progressbar.addProgressBar("胜", 0XFFEA595C, 100, 88);
//            progressbar.addProgressBar("平", 0XFFEAFF00, 100, 65);
//            progressbar.addProgressBar("负", 0XFFD7D5DA, 100, 30);
        }
    }
}
