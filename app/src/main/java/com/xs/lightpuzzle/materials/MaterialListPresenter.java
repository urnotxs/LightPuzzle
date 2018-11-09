package com.xs.lightpuzzle.materials;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;

/**
 * Created by xs on 2018/11/8.
 */

public interface MaterialListPresenter extends MvpPresenter<MaterialListView> {

    void loadMaterials(Context context, boolean pullToRefresh, TemplateSetQuery query);
}
