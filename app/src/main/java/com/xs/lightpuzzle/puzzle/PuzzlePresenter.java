package com.xs.lightpuzzle.puzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.entity.Template;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.puzzle.adapter.PuzzleDataAdapter;
import com.xs.lightpuzzle.puzzle.data.BgTextureData;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.TemplateData;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzlePresenter extends MvpBasePresenter<PuzzleView> {
    private Context mContext;

    private boolean firstInit = true;
    private volatile boolean isPageClose = false;
    private final int animationDuration = 370;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private String mTemplateId;
    private int mTemplateCategory;
    private String mAdditionalTemplateId;
    private int mAdditionalTemplateCategory;
    private int mPhotoNum;
    private Template mTemplate;
    private TemplateSet mTemplateSet;
    private PuzzlesInfo mPuzzlesInfo;

    private ArrayList<String> mPhotoFilePaths = new ArrayList<>();
    private Map<Integer, ArrayList<String>> mPlatterPhotoFile;

    public PuzzlePresenter() {

    }

    public void draw(Canvas canvas) {
        if (mPuzzlesInfo != null) {
            mPuzzlesInfo.draw(canvas);
        }
    }

    public void initData(final Context context, final String templateId,
                         final ArrayList<Photo> photos, final int templateCategory) {
        if (firstInit) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!isPageClose) {

                        long starTime = System.currentTimeMillis();

                        if (initPuzzleInfo(context, templateId, photos, templateCategory)) {

                            long waitTime = animationDuration - (System.currentTimeMillis() - starTime);
                            final long finalWaitTime = waitTime < 0 ? 0 : waitTime;

                            ifViewAttached(new ViewAction<PuzzleView>() {
                                @Override
                                public void run(@NonNull final PuzzleView view) {
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            view.setPageData(mPuzzlesInfo);

                                            mPuzzlesInfo.setFirstInit(); // 第一次打开谈toolbar
                                            firstInit = false;
                                        }
                                    }, finalWaitTime);
                                }
                            });
                        }
                    }
                }
            }).start();
        } else {
            if (initPuzzleInfo(context, templateId, photos, templateCategory)) {
                ifViewAttached(new ViewAction<PuzzleView>() {
                    @Override
                    public void run(@NonNull PuzzleView view) {
                        view.setPageData(mPuzzlesInfo);
                    }
                });
            }
        }

    }

    private boolean initPuzzleInfo(Context context, String templateId,
                                   ArrayList<Photo> photos, int templateCategory) {
        if (photos == null || photos.isEmpty()) {
            throw new NullPointerException("Photo list is null or empty");
        }

        mPhotoFilePaths = new ArrayList<>();
        for (Photo photo : photos) {
            mPhotoFilePaths.add(photo.getPath());
        }
        mPlatterPhotoFile = new TreeMap<>();
        mPlatterPhotoFile.put(1, mPhotoFilePaths);

        loadTemplate(templateId, templateCategory, mPhotoFilePaths.size());

        initPuzzleInfo(context);

        if (mPuzzlesInfo != null) {
            return true;
        } else {
            return false;
        }
    }

    private void initPuzzleInfo(Context context) {
        int puzzleMode = PuzzleMode.getMode(mTemplateSet.getCategory());

        TemplateData templateData = PuzzleDataAdapter.getTemplateData(mTemplateSet, mPhotoFilePaths.size());

        BgTextureData bgTextureData = PuzzleDataAdapter.getBgTextureData(mTemplate.getBackground());

//        SignatureData signatureData = PuzzleDataAdapter.getSignatureData(

        RotationImg[] rotationImgArr = PuzzleDataAdapter.toRotationImgs(mPhotoFilePaths);

        mPuzzlesInfo = PuzzleHelper.createSimplePuzzlesInfo(mContext, puzzleMode,
                rotationImgArr, templateData, bgTextureData, null);

        if (mPuzzlesInfo!=null){
            mPuzzlesInfo.resetId();
            mPuzzlesInfo.init();
            mPuzzlesInfo.initBitmap(context);
            mPuzzlesInfo.reSavePoints();
        }
    }


    private void loadTemplate(String templateId, int templateCategory, int photoNum) {
        TemplateSet templateSet = TemplateManager.get(templateCategory, templateId);
        if (templateSet == null) {
            throw new RuntimeException("No corresponding transformTemplate was found");
        }

        mTemplateCategory = templateCategory;
        mAdditionalTemplateId = mTemplateId = templateId;
        mAdditionalTemplateCategory = mapAdditionalTemplateCategory(templateCategory);

        try {
            mTemplate = templateSet.getTemplateMap().get(photoNum);
            mTemplateSet = templateSet;
            mPhotoNum = photoNum;
        } catch (Exception e) {
            throw new RuntimeException("Photo number over limit");
        }
    }


    private int mapAdditionalTemplateCategory(int templateCategory) {
        int additional = templateCategory;
        if (templateCategory == DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_GROUP) {
            additional = DataConstant.TEMPLATE_CATEGORY.LONG_COLLAGE_SUB;
        }
        return additional;
    }

    public boolean isPageClose() {
        return isPageClose;
    }

}
