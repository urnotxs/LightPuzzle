package com.xs.lightpuzzle.materials;

import android.support.v7.app.AppCompatActivity;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xs on 2018/11/7.
 */

public abstract class BaseMaterialListActivity extends AppCompatActivity {

    // TODO: 2018/11/7
    protected void download(final int position, final TemplateSet templateSet) {

        MaterialDownloadHelper.download(templateSet, new MaterialDownloadHelper.Listener() {
            @Override
            public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                EventBus.getDefault().post(
                        new MaterialListEventBus.DownloadingTemplate(position, templateSet));
            }

            @Override
            public void completed(BaseDownloadTask task) {

                EventBus.getDefault().post(
                        new MaterialListEventBus.DownloadedTemplate(position, templateSet));
            }
        });
    }

    protected void postTemplateUsed(int position, TemplateSet templateSet) {

        templateSet.setUnused(false);
        templateSet.setHistory(true);
        if (TemplateManager.save(templateSet)) {
            if (position >= 0) {
                EventBus.getDefault().post(
                        new MaterialListEventBus.UsedTemplate(position, templateSet));
            }
        }
    }

}
