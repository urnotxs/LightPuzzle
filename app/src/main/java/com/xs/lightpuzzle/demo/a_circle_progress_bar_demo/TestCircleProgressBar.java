package com.xs.lightpuzzle.demo.a_circle_progress_bar_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * @author xs
 * @description
 * @since 2019/2/22
 */

public class TestCircleProgressBar extends FrameLayout {
    private ProgressBarsView progressbar;
    private Button btnRefresh;

    public TestCircleProgressBar(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_circle_progressbar_layout, null);
        addView(view);
        {
            progressbar = view.findViewById(R.id.team_diary_data_progress_rate_bar);
            btnRefresh = view.findViewById(R.id.refresh_btn);

            progressbar.clearProgressBar();
            progressbar.setTextSize(30);

            progressbar.addProgressBar("胜", 0XBBEA595C, Utils.getRealPixel3(15), 100, 80, true, true);
            progressbar.addProgressBar("平", 0XBBD6A20E, Utils.getRealPixel3(15), 100, 99, false, true);
            progressbar.addProgressBar("负", 0XBBD7D5DA, Utils.getRealPixel3(15), 100, 30, false, true);

            progressbar.showProgress();
        }
    }
}
