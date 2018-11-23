package com.xs.lightpuzzle.puzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.layout.MvpFrameLayout;
import com.xs.lightpuzzle.Navigator;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.puzzle.data.SignatureData;
import com.xs.lightpuzzle.puzzle.frame.PuzzleBottomView;
import com.xs.lightpuzzle.puzzle.frame.PuzzleFrame;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.info.TemplateInfo;
import com.xs.lightpuzzle.puzzle.msgevent.BottomMsgEvent;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesBottomMsgCode;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.signature.SignatureActivity;
import com.xs.lightpuzzle.puzzle.view.signature.SignatureUtils;
import com.xs.lightpuzzle.puzzle.view.texturecolor.EditBgTextureLayout;
import com.xs.lightpuzzle.puzzle.view.texturecolor.ViewCloseCallback;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleBackgroundBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.xs.lightpuzzle.puzzle.PuzzleActivity.EXTRA_PHOTOS;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzlePage extends MvpFrameLayout<PuzzleView, PuzzlePresenter>
        implements PuzzleView, View.OnClickListener {

    public static final int REQ_CODE_CHANGE_PHOTO = 1;
    public static final int REQ_CODE_TRANSFORM_TEMPLATE = 2;
    public static final int REQ_CODE_REORDER_TO_REPLACE = 3;
    public static final int REQ_CODE_EDIT_SIGNATURE = 4;

    private Context mContext;

    private RelativeLayout mMainContainer;

    private RelativeLayout mTopBar;
    private ImageView mCancelBtn;
    private ImageView mSaveBtn;

    private PuzzleFrame mPuzzleFrame;// view的容器(里面承载绘图所有布局)

    private PuzzleBottomView mBottomView;

    private int mPuzzleMode = -1;
    private String mTemplateId;
    private int mTemplateCategory;
    private ArrayList<Photo> mPhotos;

    public PuzzlePage(Context context, Intent intent) {
        super(context);
        mContext = context;
        setBackgroundResource(R.drawable.bg_toolbar);
        initView();
        getIntentData(intent, false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initData(mTemplateId, mPhotos, mTemplateCategory);
    }

    private void initData(String templateId, ArrayList<Photo> photos, int templateCategory) {
        if (mBottomView != null) {
            mBottomView.setVisibility(INVISIBLE);
        }
        getPresenter().initData(mContext, templateId, photos, templateCategory);
    }

    @Override
    public void setPageData(PuzzlesInfo puzzlesInfo) {
        if (puzzlesInfo == null || getPresenter().isPageClose()) {
            return;
        }
        mPuzzleFrame.setPuzzlePresenter(getPresenter());

        mPuzzleMode = puzzlesInfo.getPuzzleMode();

        List<TemplateInfo> templateInfoList = puzzlesInfo.getTemplateInfos();
        if (templateInfoList != null && templateInfoList.size() > 0) {
            if (mPuzzleFrame != null) {
                mPuzzleFrame.recycle();
                mPuzzleFrame.setPageData(puzzlesInfo);
            }

            if (mBottomView != null) {
                mBottomView.setPuzzleMode(mPuzzleMode);
                mBottomView.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * EventBus PuzzleBottomView的事件接收
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBottomMsgEventRequest(BottomMsgEvent bottomMsgEvent) {
        if (bottomMsgEvent != null) {
            switch (bottomMsgEvent.getMsgCode()) {
                case PuzzlesBottomMsgCode.CHANGE_TEMPALTE:
                    Navigator.navigateToMaterialListActivity(
                            (Activity) mContext, mPhotos.size(),
                            REQ_CODE_TRANSFORM_TEMPLATE);
                    break;
                case PuzzlesBottomMsgCode.CHANGE_BACKGROUND:
                    showBgTextureLayout();
                    break;
                case PuzzlesBottomMsgCode.ADD_TEXT:
                    break;
                case PuzzlesBottomMsgCode.ADD_SIGNATURE:
                    getPresenter().onSignBtnClick();
                    break;
                case PuzzlesBottomMsgCode.ADD_LABEL:
                    break;
                case PuzzlesBottomMsgCode.ADD_MUSIC:
                    break;
                case PuzzlesBottomMsgCode.PREVIEW_VIDEO:

                    break;
                case PuzzlesBottomMsgCode.CHANGE_LAYOUT:
                    break;
                case PuzzlesBottomMsgCode.CHANGE_LINE_FRAME:
                    break;
                case PuzzlesBottomMsgCode.ADJUST_PIC_FOR_VIDEO:
                    break;
                case PuzzlesBottomMsgCode.ORDER_PLAY:
                    break;
            }
        }
    }

    /**
     * EventBus 拼图页各个子元素的触发接收
     *
     * @param puzzlesRequestMsg PuzzleInfo的各层元素 Touch 触发
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void puzzlesInfoRequest(PuzzlesRequestMsg puzzlesRequestMsg) {
        if (puzzlesRequestMsg == null
                && puzzlesRequestMsg.getObject() == null) {
            return;
        }

        Object object = puzzlesRequestMsg.getObject();

        switch (puzzlesRequestMsg.getMsgName()){
            case PuzzlesRequestMsgName.PUZZLES_INVALIDATE_VIEW:
            case PuzzlesRequestMsgName.PUZZLES_SIGN:
            case PuzzlesRequestMsgName.PUZZLES_LABEL:
            case PuzzlesRequestMsgName.PUZZLES_SIGN_SHOW_FRAME:
                invalidateView();
                break;
            case PuzzlesRequestMsgName.PUZZLES_SIGN_DELETE:
                getPresenter().getPuzzlesInfo().recycleSign();
                invalidateView();
                break;
            case PuzzlesRequestMsgName.PUZZLES_SIGN_EDIT:
                // 历史有签名则直接绘制，无历史签名跳转至签名编辑页
                // 点击签名，跳转至签名编辑页
                if (object instanceof String) {
                    String signPath = SignatureUtils.getEditingSignature();
                    if (getPresenter().getPuzzlesInfo().getSignInfo() == null
                            && signPath != null && !signPath.equals("")) {
                        SignatureData signatureData = new SignatureData();
                        signatureData.setSignPic(signPath);
                        getPresenter().setSignData(getContext(),
                                signatureData, mPuzzleFrame.getScrollYOffset());
                        invalidateView();
                    } else {
                        String path = (String) puzzlesRequestMsg.getObject();
                        Navigator.navigateToSignatureActivity(
                                (Activity) mContext, REQ_CODE_EDIT_SIGNATURE, path);
                    }
                }
                break;
        }



    }
    private EditBgTextureLayout mEditBgTextureLayout; // 拼图通用

    private void showBgTextureLayout() {
//        mPuzzleFrame.onClearSelected();

        int bgColor = getPresenter().getPuzzlesInfo().getBgTextureInfo().getBgColor();
        String texture = getPresenter().getPuzzlesInfo().getBgTextureInfo().getTextureStr();
        if (mEditBgTextureLayout == null) {
            mEditBgTextureLayout = new EditBgTextureLayout(getContext(),
                    texture, bgColor, new ViewCloseCallback() {
                @Override
                public void close() {
                    PuzzlePage.this.removeView(mEditBgTextureLayout);
                    mEditBgTextureLayout = null;
                }
            });
            FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mEditBgTextureLayout.setListener(mTextureOrColorListener);
            this.addView(mEditBgTextureLayout, fParams);
        }

        mEditBgTextureLayout.setVisibility(VISIBLE);
    }

    private EditBgTextureLayout.TextureOrColorListener mTextureOrColorListener = new EditBgTextureLayout.TextureOrColorListener() {
        @Override
        public void onChange(PuzzleBackgroundBean backgroundBean) {
            if (backgroundBean == null || getPresenter() == null) {
                return;
            }
            getPresenter().changeBgTexture(getContext(), backgroundBean);
        }
    };

    public PuzzleFrame.OnBtnBgVisListener mOnBtnBgVisListener = new PuzzleFrame.OnBtnBgVisListener() {
        @Override
        public void onBtnBgVis(boolean visible) {
            if (mBottomView != null) {
                mBottomView.setBtnBgVisible(visible);
            }
        }
    };

    private void initView() {
        initContainer();
        initTopBar();
        initPuzzleFrame();
        initPuzzleBottomView();

        EventBus.getDefault().register(this);
    }

    private void initContainer() {
        mMainContainer = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mMainContainer, rParams);
    }

    private void initTopBar() {
        mTopBar = new RelativeLayout(mContext);
        mTopBar.setId(R.id.puzzle_page_top_bar);
        BitmapDrawable bmpDraw = new BitmapDrawable(BitmapFactory
                .decodeResource(getResources(), R.drawable.main_topbar_bg_fill));
        bmpDraw.setTileModeX(Shader.TileMode.REPEAT);
        mTopBar.setBackgroundDrawable(bmpDraw);
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getRealPixel3(90));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mMainContainer.addView(mTopBar, rParams);
        {
            // 返回到上一层按钮
            mCancelBtn = new ImageView(mContext);
            mCancelBtn.setImageResource(R.drawable.puzzles_cancel_btn);
            rParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rParams.topMargin = Utils.getRealPixel3(3);
            mTopBar.addView(mCancelBtn, rParams);
            mCancelBtn.setOnClickListener(this);

            // 中间提示简拼名字
            TextView centerText = new TextView(mContext);
            centerText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            centerText.setTextColor(Color.WHITE);
            rParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rParams.leftMargin = Utils.getRealPixel3(103);
            centerText.setText(R.string.longpage_title);
            mTopBar.addView(centerText, rParams);

            mSaveBtn = new ImageView(mContext);
            mSaveBtn.setImageResource(R.drawable.puzzles_ok_btn);
            rParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mSaveBtn.setPadding(Utils.getRealPixel3(30), Utils.getRealPixel3(3),
                    0, Utils.getRealPixel3(3));
            mTopBar.addView(mSaveBtn, rParams);
            mSaveBtn.setOnClickListener(this);
        }
    }

    private void initPuzzleFrame() {
        mPuzzleFrame = new PuzzleFrame(mContext);
        RelativeLayout.LayoutParams rParams =
                new RelativeLayout.LayoutParams(Utils.getScreenW(),
                        ViewGroup.LayoutParams.MATCH_PARENT);
        rParams.addRule(RelativeLayout.BELOW, mTopBar.getId());
//        mPuzzleFrame.setBlankClickListener(mBlankClickListener);
//        mPuzzleFrame.setOnBtnBgVisListener(mOnBtnBgVisListener);
//        mPuzzleFrame.setTemplateChangeListener(mTemplateChangeClickListener);
        mMainContainer.addView(mPuzzleFrame, rParams);
    }

    private void initPuzzleBottomView() {
        mBottomView = new PuzzleBottomView(mContext);
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.bottomMargin = Utils.getRealPixel3(12);
        mMainContainer.addView(mBottomView, rParams);
    }


    @Override
    public PuzzlePresenter createPresenter() {
        return new PuzzlePresenter();
    }

    @Override
    public void invalidateView() {
        mPuzzleFrame.invalidateView();
    }

    @Override
    public void invalidateView(int width, int height, int templateSize) {
        mPuzzleFrame.invalidateView(width, height, templateSize);
    }

    @Override
    public void invalidateView(int width, int height, int templateSize, int differ) {
        mPuzzleFrame.invalidateView(width, height, templateSize);
    }

    @Override
    public void invalidateViewToScroll(int width, int height, int templateSize, int bottom) {
        mPuzzleFrame.invalidateViewToScroll(width, height, templateSize, bottom);
    }

    @Override
    public void onClick(View v) {

    }


    private void getIntentData(Intent intent, boolean isBack) {
        if (!isBack) {
            mPhotos = intent.getParcelableArrayListExtra(EXTRA_PHOTOS);
        }
        mTemplateId = intent.getStringExtra(PuzzleActivity.EXTRA_TEMPLATE_ID);
        mTemplateCategory = intent.getIntExtra(
                PuzzleActivity.EXTRA_TEMPLATE_CATEGORY,
                DataConstant.TEMPLATE_CATEGORY.SIMPLE);
    }

    public void onClose() {
        EventBus.getDefault().unregister(this);
    }

    public void handleTransformTemplateResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            getIntentData(intent, true);
            initData(mTemplateId, mPhotos, mTemplateCategory);
        }
    }

    public void handleEditSignatureResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getPresenter().updateSignatureParams(mContext, data, mPuzzleFrame.getScrollYOffset());

        }
    }
}
