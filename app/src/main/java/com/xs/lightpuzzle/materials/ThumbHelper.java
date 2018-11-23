package com.xs.lightpuzzle.materials;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.PuzzleFileExtension;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Map;

/**
 * Created by xs on 2018/11/8.
 */

public final class ThumbHelper {

    interface STATE {

        int DOWNLOAD = 1; // 需要下载
        int DOWNLOADING = 2; // 正在下载
        int DOWNLOADED_UNUSED = 3; // 已经下载，没有使用
        int DOWNLOADED_USED = 4; // 已经下载，已使用
    }

    static void load(Context context, TemplateSet ts, boolean isDownloading,
                     Integer photoNum, RelativeLayout rootView,
                     FrameLayout labelRootView, AppCompatImageView labelView,
                     ProgressBar progressView, AppCompatImageView iv,
                     LikeButton favoriteView) {
        load(context, ts, isDownloading, Glide.with(context),
                photoNum, rootView, labelRootView, labelView,
                progressView, iv, favoriteView);
    }

    private static void load(Context context,
                             TemplateSet ts, boolean isDownloading,
                             RequestManager glide, Integer photoNum,
                             RelativeLayout rootView,
                             FrameLayout labelRootView,
                             AppCompatImageView labelView,
                             ProgressBar progressView,
                             AppCompatImageView iv,
                             LikeButton favoriteView) {

        layout(context, ts, rootView);
        loadLabel(ts, isDownloading, labelRootView, labelView, progressView);
        loadThumb(glide, ts, photoNum, iv);
        loadFavorite(ts, favoriteView);
    }

    private static void loadFavorite(final TemplateSet ts, LikeButton favoriteView) {
        favoriteView.setLiked(ts.isLike());
        favoriteView.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like(true, ts);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like(false, ts);
            }
        });
    }

    private static void loadThumb(RequestManager glide, TemplateSet ts,
                                  Integer photoNum, AppCompatImageView iv) {
        if (ts.isDownloaded() && photoNum != null) {
            loadLocal(glide, ts, photoNum, iv);
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.bg_placeholder)
                    .dontAnimate();
            glide.load(ts.getThumbUrl())
                    .apply(options)
                    .into(iv);
        }
    }

    private static void loadLocal(RequestManager glide, TemplateSet ts,
                                  Integer photoNum, AppCompatImageView iv) {
        String fileName = ts.getThumbFileName();
        Map<Integer, String> thumbMap = ts.getThumbFileNameMap();
        if (thumbMap != null && thumbMap.containsKey(photoNum)) {
            fileName = ts.getThumbFileNameMap().get(photoNum);
        }
        String filePath = PuzzleFileExtension.mapFile(ts.getDirPath() + File.separator + fileName);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.bg_placeholder)
                .dontAnimate();
        glide.load(new File(filePath))
                .apply(options)
                .into(iv);
    }

    private static void loadLabel(TemplateSet ts, boolean isDownloading,
                                  FrameLayout labelRootView,
                                  AppCompatImageView labelView,
                                  ProgressBar progressView) {
        int state = getState(ts, isDownloading);
        switch (state) {
            case STATE.DOWNLOADING:
                labelRootView.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.VISIBLE);
                break;
            case STATE.DOWNLOADED_UNUSED:
                labelRootView.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.INVISIBLE);
                labelView.setImageResource(R.drawable.ic_new_white_24dp);
                break;
            case STATE.DOWNLOADED_USED:
                labelRootView.setVisibility(View.INVISIBLE);
                progressView.setVisibility(View.INVISIBLE);
                break;
            case STATE.DOWNLOAD:
            default:
                labelRootView.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.INVISIBLE);
                labelView.setImageResource(R.drawable.ic_arrow_downward_white_24dp);
                break;
        }
    }

    private static void layout(Context context, TemplateSet ts,
                               RelativeLayout rootView) {

        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        int width = getWidth(context);
        int height = getHeight(context, ts);
        if (params.width != width || params.height != height) {
            params.width = width;
            params.height = height;
            rootView.setLayoutParams(params);
        }
    }

    private static void like(boolean isLike, TemplateSet templateSet) {
        templateSet.setLike(isLike);
        TemplateManager.save(templateSet);
        EventBus.getDefault().post(new MaterialListEventBus.LikeTemplate(isLike, templateSet));
    }

    private static int getState(TemplateSet templateSet, boolean isDownloading) {
        return isDownloading ? STATE.DOWNLOADING
                : !templateSet.isDownloaded() ? STATE.DOWNLOAD
                : templateSet.isUnused() ? STATE.DOWNLOADED_UNUSED
                : STATE.DOWNLOADED_USED;
    }

    private static int getHeight(Context context, TemplateSet templateSet) {
        return templateSet == null ? 0 :
                (int) (getWidth(context) / templateSet.getUiRatio());
    }

    private static int getWidth(Context context) {
        int space = (int) context.getResources()
                .getDimension(R.dimen.material_item_space);
        return (ScreenUtils.getScreenWidth() - 3 * space) / 2;
    }
}
