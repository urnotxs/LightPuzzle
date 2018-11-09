package com.xs.lightpuzzle.materials;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import java.util.List;

/**
 * Created by xs on 2018/11/8.
 */

public class SimpleMaterialListPresenter extends
        MvpBasePresenter<MaterialListView>
        implements MaterialListPresenter {

    private Handler mHandler = new Handler();

    @Override
    public void loadMaterials(Context context, boolean pullToRefresh, final TemplateSetQuery query) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<TemplateSet> collections = TemplateManager.list(query);
                ifViewAttached(new ViewAction<MaterialListView>() {
                    @Override
                    public void run(@NonNull final MaterialListView view) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setData(collections);
                                view.showContent();
                            }
                        });

                    }
                });
            }
        }).start();
    }
}
