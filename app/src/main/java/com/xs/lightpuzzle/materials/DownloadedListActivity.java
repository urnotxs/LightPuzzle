package com.xs.lightpuzzle.materials;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.FileUtils;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.dao.TemplateSetQuery;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xs on 2018/11/9.
 */

public class DownloadedListActivity extends SingleMaterialListActivity {

    private Menu mOptionMenu;
    private boolean mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mOptionMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_downloaded_material_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.material_list_aty_menu_delete) {
            manager(true);
            return true;
        } else if (id == android.R.id.home && mManager) {
            manager(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void manager(boolean isManager) {
        getAdapter().applyManagerContrarily();
        mManager = isManager;
        setTitle(isManager ?
                R.string.downloaded_list_aty_management
                : R.string.downloaded_list_aty_name);
        updateOptionMenu();
    }

    private void updateOptionMenu() {
        if (mOptionMenu == null) {
            return;
        }
        for (int i = 0; i < mOptionMenu.size(); i++) {
            MenuItem item = mOptionMenu.getItem(i);
            if (item.getItemId() == R.id.material_list_aty_menu_delete) {
                item.setVisible(!mManager);
                item.setEnabled(!mManager);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteTemplateEvent(MaterialListEventBus.DeleteTemplate event) {
        TemplateSet collection = event.getTemplateSet();
        collection.setDownloaded(false);
        if (TemplateManager.save(collection)) {
            FileUtils.deleteDir(collection.getDirPath()); // ignored result
            EventBus.getDefault().post(new MaterialListEventBus.TemplateDeleted(event.getPosition(), event.getTemplateSet()));
        }
    }

    @Override
    protected void selectTemplate(MaterialListEventBus.SelectTemplate selected) {
        TemplateSet collection = selected.getTemplateSet();
        if (collection.isUnused() || !collection.isHistory()) {
            collection.setUnused(false);
            collection.setHistory(true);
            TemplateManager.save(collection);
        }
        final int category = collection.getCategory();
        final String id = collection.getId();
        final int maxPhotoNum = collection.getMaxPhotoNum();

        // 跳转至拼图页
        // TODO: 2018/11/9
    }

    @Override
    protected TemplateSetQuery provideQuery() {
        return new TemplateSetQuery.Builder()
                .setFlag(TemplateSet.FLAG.DOWNLOADED)
                .build();
    }

    @Override
    protected int provideMaterialList() {
        return MATERIAL_LIST.DOWNLOADED;
    }

}
