package com.xs.lightpuzzle.materials;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.xs.lightpuzzle.data.FontManager;
import com.xs.lightpuzzle.data.PuzzleFileExtension;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xs on 2018/11/9.
 */

public final class MaterialDownloadHelper {

    private interface TASK_TYPE {

        int KEY = -1;

        int TEMPLATE = 1;
        int FONT = 2;
        int NEXT_TEMPLATE = 3;
    }

    public static void download(Font font, Listener listener) {
        FileDownloader.getImpl()
                .create(font.getUrl())
                .setPath(font.getDirPath(), true)
                .setCallbackProgressTimes(10)
                .setListener(new FontListener(font, listener))
                .start();
    }

    public static class FontListener extends ListenerProxy<Font> {

        public FontListener(Font material, Listener listener) {
            super(material, listener);
        }

        @Override
        protected void materialCompleted(BaseDownloadTask task, Font material) {
            if (fontCompleted(task, material)) {
                proxyCompleted(task);
            } else {
                error(task, null);
            }
        }
    }

    public static void download(TemplateSet templateSet, Listener listener) {
        if (templateSet == null) {
            return;
        }

        List<BaseDownloadTask> tasks = new ArrayList<>();

        List<BaseDownloadTask> attachedFontTasks = getAttachedFontTask(
                templateSet.getAttachedFontIdSet());
        if (attachedFontTasks != null && !attachedFontTasks.isEmpty()) {
            tasks.addAll(attachedFontTasks);
        }

        List<BaseDownloadTask> attachedNodeTasks = getAttachedNodeTask(
                templateSet.getAttachedNodeMap());
        if (attachedNodeTasks != null && !attachedNodeTasks.isEmpty()) {
            tasks.addAll(attachedNodeTasks);
        }

        if (tasks.isEmpty()) {
            downloadDirectly(templateSet, listener);
        } else {
            BaseDownloadTask templateTask = FileDownloader.getImpl()
                    .create(templateSet.getUrl())
                    .setPath(templateSet.getDirPath(), true)
                    .setTag(TASK_TYPE.KEY, TASK_TYPE.TEMPLATE);
            tasks.add(templateTask);

            new FileDownloadQueueSet(new TemplateListener(templateSet, listener))
                    .disableCallbackProgressTimes()
                    .setAutoRetryTimes(1)
                    .downloadSequentially(tasks)
                    .start();
        }
    }

    private static void downloadDirectly(TemplateSet templateSet, Listener listener) {

        FileDownloader.getImpl()
                .create(templateSet.getUrl())
                .setPath(templateSet.getDirPath(), true)
                .setCallbackProgressIgnored()
                .setListener(new TemplateListener(templateSet, listener))
                .start();
    }

    private static List<BaseDownloadTask> getAttachedNodeTask(
            Map<Integer, TemplateSet.Node> attachedNodeMap) {
        if (attachedNodeMap == null || attachedNodeMap.isEmpty()) {
            return null;
        }

        List<BaseDownloadTask> tasks = new ArrayList<>();
        for (Map.Entry<Integer, TemplateSet.Node> entry : attachedNodeMap.entrySet()) {

            TemplateSet.Node node = entry.getValue();
            TemplateSet attachedTemplateSet = TemplateManager.list(
                    node.getCategory(), node.getId());
            if (attachedTemplateSet != null) {
                BaseDownloadTask task = FileDownloader.getImpl()
                        .create(attachedTemplateSet.getUrl())
                        .setPath(attachedTemplateSet.getDirPath())
                        .setTag(TASK_TYPE.KEY, TASK_TYPE.NEXT_TEMPLATE)
                        .setTag(attachedTemplateSet);
                tasks.add(task);

                List<BaseDownloadTask> attachedFontTasks = getAttachedFontTask(
                        attachedTemplateSet.getAttachedFontIdSet());
                if (attachedFontTasks != null && !attachedFontTasks.isEmpty()) {
                    tasks.addAll(attachedFontTasks);
                }
            }
        }
        return tasks;

    }

    private static List<BaseDownloadTask> getAttachedFontTask(
            Set<String> attachedFontIdSet) {
        if (attachedFontIdSet == null || attachedFontIdSet.isEmpty()) {
            return null;
        }

        List<BaseDownloadTask> tasks = new ArrayList<>();
        for (String fontId : attachedFontIdSet) {
            Font font = FontManager.get(fontId);
            if (font != null) {
                BaseDownloadTask task = FileDownloader.getImpl()
                        .create(font.getUrl())
                        .setPath(font.getDirPath(), true)
                        .setTag(TASK_TYPE.KEY, TASK_TYPE.FONT)
                        .setTag(font);
                tasks.add(task);
            }
        }
        return tasks;
    }

    private static class TemplateListener extends ListenerProxy<TemplateSet> {

        public TemplateListener(TemplateSet material, Listener listener) {
            super(material, listener);
        }

        @Override
        protected void materialCompleted(BaseDownloadTask task, TemplateSet material) {
            switch (getTaskType(task)) {
                case TASK_TYPE.FONT:
                    fontCompleted(task, (Font) task.getTag());
                    break;
                case TASK_TYPE.NEXT_TEMPLATE:
                    templateCompleted(task, (TemplateSet) task.getTag());
                    break;
                case TASK_TYPE.TEMPLATE:
                default:
                    if (templateCompleted(task, material)) {
                        proxyCompleted(task);
                    } else {
                        error(task, null);
                    }
                    break;
            }
        }

        private int getTaskType(BaseDownloadTask task) {
            return (task != null && task.getTag(TASK_TYPE.KEY) != null)
                    ? (int) task.getTag(TASK_TYPE.KEY) : TASK_TYPE.TEMPLATE;
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            if (getTaskType(task) == TASK_TYPE.TEMPLATE) {
                super.error(task, e);
            }
        }
    }

    public static abstract class ListenerProxy<M> extends FileDownloadListener {

        private final M mMaterial;
        private final Listener mListener;

        public ListenerProxy(M material, Listener listener) {
            this.mMaterial = material;
            this.mListener = listener;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (task.getListener() != this) {
                return;
            }
            if (mListener != null) {
                mListener.pending(task, soFarBytes, totalBytes);
            }
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (task.getListener() != this) {
                return;
            }
            if (mListener != null) {
                mListener.progress(task, soFarBytes, totalBytes);
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            if (task.getListener() != this) {
                return;
            }
            if (mMaterial != null) {
                materialCompleted(task, mMaterial);
            } else {
                proxyCompleted(task);
            }
        }

        protected abstract void materialCompleted(BaseDownloadTask task, M material);

        protected boolean templateCompleted(BaseDownloadTask task, TemplateSet templateSet) {
            if (templateSet != null) {
                String dirPath = task.getPath();
                if (dirPath.endsWith(templateSet.getId())) {
                    dirPath = FileUtils.getDirName(dirPath);
                }
                String filePath = task.getTargetFilePath();
                try {
                    List<File> unZipFiles = ZipUtils.unzipFile(filePath, dirPath);
                    if (unZipFiles != null && unZipFiles.size() > 0) {
                        PuzzleFileExtension.mapDir(dirPath);
                        FileUtils.deleteFile(filePath);
                        templateSet.setDownloaded(true);
                        templateSet.setUnused(true);
                        if (TemplateManager.save(templateSet)) {
                            return true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        protected boolean fontCompleted(BaseDownloadTask task, Font font) {
            if (font != null) {
                String dirPath = task.getPath();
                if (dirPath.endsWith(font.getId())) {
                    dirPath = FileUtils.getDirName(dirPath);
                }
                String filePath = task.getTargetFilePath();
                try {
                    List<File> unZipFiles = ZipUtils.unzipFile(filePath, dirPath);
                    if (unZipFiles != null && unZipFiles.size() > 0) {
                        PuzzleFileExtension.mapDir(dirPath);
                        FileUtils.deleteFile(filePath);
                        font.setDownloaded(true);
                        if (FontManager.save(font)) {
                            return true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        protected void proxyCompleted(BaseDownloadTask task) {
            if (mListener != null) {
                mListener.completed(task);
            }
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (task.getListener() != this) {
                return;
            }
            if (mListener != null) {
                mListener.paused(task, soFarBytes, totalBytes);
            }
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            if (task.getListener() != this) {
                return;
            }
            if (mListener != null) {
                mListener.error(task, e);
            }
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            if (task.getListener() != this) {
                return;
            }
            if (mListener != null) {
                mListener.warn(task);
            }
        }
    }

    public static class Listener {
        public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        public void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            // no-op by default
        }

        public void completed(BaseDownloadTask task) {
            // no-op by default
        }

        public void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            // no-op by default
        }

        public void error(BaseDownloadTask task, Throwable e) {
            // no-op by default
        }

        public void warn(BaseDownloadTask task) {
            // no-op by default
        }
    }


}
