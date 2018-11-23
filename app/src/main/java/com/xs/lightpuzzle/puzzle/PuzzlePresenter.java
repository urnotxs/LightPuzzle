package com.xs.lightpuzzle.puzzle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.data.TemplateManager;
import com.xs.lightpuzzle.data.entity.Template;
import com.xs.lightpuzzle.data.entity.TemplateSet;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.puzzle.adapter.PuzzleDataAdapter;
import com.xs.lightpuzzle.puzzle.data.BgTextureData;
import com.xs.lightpuzzle.puzzle.data.LabelData;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.SignatureData;
import com.xs.lightpuzzle.puzzle.data.TemplateData;
import com.xs.lightpuzzle.puzzle.info.PuzzlesBgTextureInfo;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.info.PuzzlesLabelInfo;
import com.xs.lightpuzzle.puzzle.info.PuzzlesSignInfo;
import com.xs.lightpuzzle.puzzle.model.PuzzlesLabelModel;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.view.label.view.EditLabelView;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleBackgroundBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_BITMAP;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_ICON_TYPE;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_IS_INVERT;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_IS_UPDATE;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_LABEL_TYPE;
import static com.xs.lightpuzzle.puzzle.view.label.LabelActivity.LABEL_TEXT;
import static com.xs.lightpuzzle.puzzle.view.signature.SignatureActivity.SIGNATURE_HAS_HISTORY;
import static com.xs.lightpuzzle.puzzle.view.signature.SignatureActivity.SIGNATURE_PATH;

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

    public void invalidateView() {
        ifViewAttached(new ViewAction<PuzzleView>() {
            @Override
            public void run(@NonNull PuzzleView view) {
                view.invalidateView();
            }
        });
    }

    public void invalidateView(final int width, final int height, final int templateSize) {
        ifViewAttached(new ViewAction<PuzzleView>() {
            @Override
            public void run(@NonNull PuzzleView view) {
                view.invalidateView(width, height, templateSize);
            }
        });
    }

    public void invalidateViewToScroll(final int width, final int height, final int templateSize, final int bottom) {
        ifViewAttached(new ViewAction<PuzzleView>() {
            @Override
            public void run(@NonNull PuzzleView view) {
                view.invalidateViewToScroll(width, height, templateSize, bottom);
            }
        });
    }

    public void draw(Canvas canvas) {
        if (mPuzzlesInfo != null) {
            mPuzzlesInfo.draw(canvas);
        }
    }

    // --- 背景
    public void changeBgTexture(Context context, PuzzleBackgroundBean backgroundBean) {
        if (mPuzzlesInfo == null) {
            return;
        }
        PuzzlesBgTextureInfo puzzlesBgTextureInfo = mPuzzlesInfo.getBgTextureInfo();

        BgTextureData bgTextureData = new BgTextureData();
        bgTextureData.setEffect(backgroundBean.getBlendModel());
        bgTextureData.setBgColor(PuzzlesUtils.strColor2Int(backgroundBean.getBgColor()));
        bgTextureData.setTexture(backgroundBean.getTexture());
        bgTextureData.setAlpha(backgroundBean.getAlpha());
        bgTextureData.setWaterColor(PuzzlesUtils.strColor2Int(backgroundBean.getFontColor()));

        if (context != null && puzzlesBgTextureInfo != null && bgTextureData != null) {
            puzzlesBgTextureInfo.setBgColor(bgTextureData.getBgColor());
            puzzlesBgTextureInfo.setTextureStr(bgTextureData.getTexture());
            puzzlesBgTextureInfo.setAlpha(bgTextureData.getAlpha());
            puzzlesBgTextureInfo.setEffect(bgTextureData.getEffect());
            puzzlesBgTextureInfo.setWaterColor(bgTextureData.getWaterColor());
            puzzlesBgTextureInfo.initBitmap(context);
            mPuzzlesInfo.changeBgTexture(bgTextureData.getWaterColor(), bgTextureData.getBgColor());
        }

        invalidateView();
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

        if (mPuzzlesInfo != null) {
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

    public PuzzlesInfo getPuzzlesInfo() {
        return mPuzzlesInfo;
    }

    public void reSetSelectForLong() {
        if (mPuzzlesInfo == null) {
            return;
        }
        mPuzzlesInfo.setImgSelectForLong(null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mPuzzlesInfo != null && mPuzzlesInfo.onTouchEvent(event)) {
            return true;
        }
        return false;
    }

    /**
     * 标签编辑页点击保存后
     * 更新LabelInfo，重绘编辑页
     * LabelPageSite.SAVE_KEY
     */
    public void updateLabelPageParams(Context context, Intent intent, int scrollY) {

        if (intent == null) {
            return;
        }
        int iconIndex = intent.getIntExtra(LABEL_ICON_TYPE, 0);
        EditLabelView.ICON_TYPE[] iconTypes = EditLabelView.ICON_TYPE.values();
        int labelIndex = intent.getIntExtra(LABEL_LABEL_TYPE, 0);
        EditLabelView.LABEL_TYPE[] labelTypes = EditLabelView.LABEL_TYPE.values();

        HashMap<String, Object> params = new HashMap<>();
        params.put(LABEL_BITMAP, intent.getStringExtra(LABEL_BITMAP));
        params.put(LABEL_ICON_TYPE, iconTypes[iconIndex]);
        params.put(LABEL_LABEL_TYPE, labelTypes[labelIndex]);
        params.put(LABEL_TEXT, intent.getStringExtra(LABEL_TEXT));
        params.put(LABEL_IS_INVERT, intent.getBooleanExtra(LABEL_IS_INVERT, false));
        params.put(LABEL_IS_UPDATE, intent.getBooleanExtra(LABEL_IS_UPDATE, false));

        if (!TextUtils.isEmpty((String) params.get(LABEL_TEXT))) {
            String text = (String) params.get(LABEL_TEXT);
            String labelPic = (String) params.get(LABEL_BITMAP);
            boolean isInvert = (boolean) params.get(LABEL_IS_INVERT);
            boolean isUpdate = (boolean) params.get(LABEL_IS_UPDATE);
            EditLabelView.ICON_TYPE iconType = (EditLabelView.ICON_TYPE)
                    params.get(LABEL_ICON_TYPE);
            EditLabelView.LABEL_TYPE labelType = (EditLabelView.LABEL_TYPE)
                    params.get(LABEL_LABEL_TYPE);

            if (text != null) {
                LabelData labelData = new LabelData();
                labelData.setText(text);
                labelData.setLabelPic(labelPic);
                labelData.setInvert(isInvert);
                labelData.setIconType(iconType);
                labelData.setLabelType(labelType);
                labelData.setUpdate(isUpdate);
                setLabelData(context, labelData, scrollY);
            }
        }
    }

    // --- 标签
    private void setLabelData(Context context, LabelData labelData, int scrollY) {
        if (mPuzzlesInfo == null) {
            return;
        }

        PuzzlesLabelInfo labelInfo = PuzzlesLabelModel.getLabelInfo(mPuzzlesInfo, labelData);
        if (labelInfo != null) {
            labelInfo.setScrollY(scrollY);
            labelInfo.init();
            labelInfo.initBitmap(context);
            if (!labelData.isUpdate()) {
                mPuzzlesInfo.addPuzzleLabelInfos(labelInfo);
            }
        }
    }

    public void deleteLabelItem() {
        if (mPuzzlesInfo == null) {
            return;
        }
        mPuzzlesInfo.deleteLabelItem();
        invalidateView();
    }

    public void updateSignatureParams(Context context, Intent intent, int scrollY) {
        if (intent != null && mPuzzlesInfo != null) {
            String signaturePath = intent.getStringExtra(SIGNATURE_PATH);
            boolean hasHistory = intent.getBooleanExtra(SIGNATURE_HAS_HISTORY, false);

            if (!TextUtils.isEmpty(signaturePath)) {
                SignatureData signatureData = new SignatureData();
                signatureData.setSignPic(signaturePath);
                setSignData(context, signatureData, scrollY);
                invalidateView();
            } else if (!hasHistory) {
                mPuzzlesInfo.recycleSign();
            }
        }
    }

    /**
     * 设置签名信息
     *
     * @param context       context
     * @param signatureData 签名信息
     * @param scrollY       y偏移量
     */
    public void setSignData(Context context, SignatureData signatureData, int scrollY) {
        if (mPuzzlesInfo == null || signatureData == null) {
            return;
        }

        PuzzlesSignInfo signInfo = mPuzzlesInfo.getSignInfo();
        if (signInfo == null) {
            // 点击底部签名按钮，历史有签名则直接绘制
//            signInfo = mSignModel.getSignInfo(mPuzzlesInfo, signatureData, scrollY);

            signInfo = new PuzzlesSignInfo();
            signInfo.setRect(mPuzzlesInfo.getPuzzlesRect());
            signInfo.setOutPutRect(mPuzzlesInfo.getOutPutRect());
            signInfo.setPuzzleModel(mPuzzlesInfo.getPuzzleMode());
            signInfo.setScrollY(scrollY);
            signInfo.setSignPic(signatureData.getSignPic());
            if (signatureData.getSignPoint() != null) {
                // 切换自带签名的模板
                signInfo.setSignPoint(signatureData.getSignPoint());
                signInfo.setSignModel(true);
            }
        } else {
            // 从签名页返回，
            signInfo.setSignPic(signatureData.getSignPic());
        }

        if (signInfo != null) {
            signInfo.init();
            signInfo.initBitmap(context);
            mPuzzlesInfo.addPuzzlesSignInfo(signInfo);
        }
    }

    public void onSignBtnClick() {
        if (mPuzzlesInfo == null) {
            return;
        }
        PuzzlesSignInfo signInfo = mPuzzlesInfo.getSignInfo();
        if (signInfo != null) {
            // 选中当前编辑页的签名
            signInfo.setShowFrame(true);
            invalidateView();
        } else {
            // 历史有签名则直接绘制，无历史签名跳转至签名编辑页
            EventBus.getDefault().post(new PuzzlesRequestMsg(
                    PuzzlesRequestMsgName.PUZZLES_SIGN_EDIT, MotionEvent.ACTION_UP, ""));
        }
    }
}
