package com.xs.lightpuzzle.puzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.layout.MvpFrameLayout;
import com.xs.lightpuzzle.Navigator;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.photopicker.entity.Photo;
import com.xs.lightpuzzle.puzzle.data.LabelData;
import com.xs.lightpuzzle.puzzle.data.RotationImg;
import com.xs.lightpuzzle.puzzle.data.SignatureData;
import com.xs.lightpuzzle.puzzle.data.editdata.TemporaryTextData;
import com.xs.lightpuzzle.puzzle.frame.PuzzleBottomView;
import com.xs.lightpuzzle.puzzle.frame.PuzzleFrame;
import com.xs.lightpuzzle.puzzle.info.PuzzlesInfo;
import com.xs.lightpuzzle.puzzle.info.PuzzlesLabelInfo;
import com.xs.lightpuzzle.puzzle.info.TemplateInfo;
import com.xs.lightpuzzle.puzzle.info.low.PuzzlesLayoutInfo;
import com.xs.lightpuzzle.puzzle.layout.info.model.LayoutData;
import com.xs.lightpuzzle.puzzle.layout.layoutframepage.BottomEditLineFrameView;
import com.xs.lightpuzzle.puzzle.layout.layoutpage.BottomEditLayoutView;
import com.xs.lightpuzzle.puzzle.msgevent.BottomMsgEvent;
import com.xs.lightpuzzle.puzzle.msgevent.LabelBarMsgEvent;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzelsLabelBarMsgCode;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesBottomMsgCode;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.util.AnimUtils;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.signature.SignatureUtils;
import com.xs.lightpuzzle.puzzle.view.textedit.BottomEditTextView;
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
import static com.xs.lightpuzzle.puzzle.view.textedit.BottomEditTextView.ADD_TEXT;
import static com.xs.lightpuzzle.puzzle.view.textedit.BottomEditTextView.EDIT_TEMPLATE_TEXT;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzlePage extends MvpFrameLayout<PuzzleView, PuzzlePresenter>
        implements PuzzleView, View.OnClickListener {

    public static final int REQ_CODE_CHANGE_PHOTO = 1;
    public static final int REQ_CODE_TRANSFORM_TEMPLATE = 2;
    public static final int REQ_CODE_REORDER_TO_REPLACE = 3;
    public static final int REQ_CODE_EDIT_SIGNATURE = 4;
    public static final int REQ_CODE_EDIT_LABEL = 5;

    private Context mContext;

    private RelativeLayout mMainContainer;

    private RelativeLayout mTopBar;
    private ImageView mCancelBtn;
    private ImageView mSaveBtn;

    private PuzzleFrame mPuzzleFrame;// view的容器(里面承载绘图所有布局)

    private PuzzleBottomView mBottomView;
    private EditBgTextureLayout mEditBgTextureLayout; // 拼图通用
    private BottomEditLayoutView mEditLayoutView; // 布局
    private BottomEditLineFrameView mEditLineFrameView; // 布局

    private int mPuzzleMode = -1;
    private String mTemplateId;
    private float mTemplateRatio;
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
        initData(mTemplateId, mPhotos, mTemplateCategory, mTemplateRatio);
    }

    private void initData(String templateId, ArrayList<Photo> photos, int templateCategory, float templateRatio) {
        if (mBottomView != null) {
            mBottomView.setVisibility(INVISIBLE);
        }
        getPresenter().initData(mContext, templateId, photos, templateCategory, templateRatio);
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
                    getPresenter().createAddTextInfo(mContext, mPuzzleFrame.getScrollYOffset());

                    // 弹出底部输入框页面
                    TemporaryTextData textData = getPresenter().getTemporaryTextData();
                    // TODO: 2018/11/30 文字弹框
                    if (textData != null) {
                        popBottomEditTextView(ADD_TEXT, textData);
                        mPuzzleFrame.scrollFromEditText(textData.getPoints());
                    }
/*
                    mPuzzleFrame.onClearSelected();
                    getPresenter().resetSignShowFram();*/
                    break;
                case PuzzlesBottomMsgCode.ADD_SIGNATURE:
                    getPresenter().onSignBtnClick();
                    break;
                case PuzzlesBottomMsgCode.ADD_LABEL:
                    Navigator.navigateToLabelActivity(
                            (Activity) mContext, REQ_CODE_EDIT_LABEL,
                            null, 0, 0,
                            null, false);
                    break;
                case PuzzlesBottomMsgCode.ADD_MUSIC:
                    break;
                case PuzzlesBottomMsgCode.PREVIEW_VIDEO:

                    break;
                case PuzzlesBottomMsgCode.CHANGE_LAYOUT:
                    if (mEditLayoutView == null || mEditLayoutView.isDismiss()) {
                        popBottomEditLayoutView();
                    }
                    break;
                case PuzzlesBottomMsgCode.CHANGE_LINE_FRAME:
                    if (mEditLineFrameView == null || mEditLineFrameView.isDismiss()) {
                        popBottomLineFrameView();
                    }
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

        switch (puzzlesRequestMsg.getMsgName()) {
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
            case PuzzlesRequestMsgName.PUZZLES_LABEL_SHOW_BAR:
                switch (puzzlesRequestMsg.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPuzzleFrame.showLabelBar((Rect) object);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 隐藏toolbar
                        mPuzzleFrame.disLabelBar();
                        break;
                    case -1:
                        for (int i = 0; i < getPresenter().getPuzzlesInfo().getPuzzlesLabelInfos().size(); i++) {
                            if (getPresenter().getPuzzlesInfo().getPuzzlesLabelInfos().get(i).isLongTouch()) {
                                return;
                            }
                        }
                        mPuzzleFrame.disLabelBar();
                        break;
                    default:
                        mPuzzleFrame.disLabelBar();
                        break;
                }
                break;
            case PuzzlesRequestMsgName.PUZZLES_ADD_TEXT:
                if (object instanceof TemporaryTextData) {
                    TemporaryTextData textData = (TemporaryTextData) object;
                    popBottomEditTextView(ADD_TEXT, textData);
//                    mPuzzleFrame.onClearSelected();
                    mPuzzleFrame.scrollFromEditText(textData.getPoints());
                } else if (object instanceof Point[]) {
                    //删除
                    Point[] points = (Point[]) object;
//                    getPresenter().deleteAddTextItem(points);
                }
                invalidateView();
                break;
            case PuzzlesRequestMsgName.PUZZLES_TEXT_EDIT:
                if (object != null && object instanceof TemporaryTextData) {
                    TemporaryTextData textData = (TemporaryTextData) object;
                    popBottomEditTextView(EDIT_TEMPLATE_TEXT, textData);
                    if (mPuzzleFrame != null) {
//                        mPuzzleFrame.onClearSelected();
                        mPuzzleFrame.scrollFromEditText(textData.getPoints());
//                        mPuzzleFrame.resetSignShowFram();
                    }
                }
                break;
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLabelBarMsgEvent(LabelBarMsgEvent labelBarMsgEvent) {
        if (labelBarMsgEvent != null) {
            switch (labelBarMsgEvent.getMsgCode()) {
                case PuzzelsLabelBarMsgCode.LABELBAR_DEL:
                    getPresenter().deleteLabelItem();
                    break;
                case PuzzelsLabelBarMsgCode.LABELBAR_EDIT:
                    // 跳转至标签编辑页
                    List<PuzzlesLabelInfo> labelInfoList = getPresenter()
                            .getPuzzlesInfo().getPuzzlesLabelInfos();
                    LabelData labelData = new LabelData();
                    for (int i = 0; i < labelInfoList.size(); i++) {
                        if (labelInfoList.get(i).isLongTouch()) {
                            labelData.setInvert(labelInfoList.get(i).isInvert());
                            labelData.setLabelType(labelInfoList.get(i).getLabelType());
                            labelData.setIconType(labelInfoList.get(i).getIconType());
                            labelData.setLabelPic(labelInfoList.get(i).getPicPath());
                            labelData.setText(labelInfoList.get(i).getText());

                            break;
                        }
                    }
                    Navigator.navigateToLabelActivity(
                            (Activity) mContext, REQ_CODE_EDIT_LABEL,
                            labelData.getLabelPic(), labelData.getIconType().ordinal(),
                            labelData.getLabelType().ordinal(),
                            labelData.getText(), labelData.isInvert());
                    break;
            }
        }
    }

    private BottomEditTextView mEditTextView; // 通用
    private void popBottomEditTextView(int model, TemporaryTextData temporaryTextData) {
        if (mEditTextView == null) {
            mEditTextView = new BottomEditTextView(getContext());
            FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mEditTextView, mParams);
            mEditTextView.setEditInteractionListener(mEditInteractionListener);
        } else {
            mEditTextView.showView();
        }
        mEditTextView.open(model, temporaryTextData);
        AnimUtils.setTransAnim(mEditTextView, 0,
                0, 1, 0, 500, null);
    }

    private BottomEditTextView.OnEditInteractionListener mEditInteractionListener = new BottomEditTextView.OnEditInteractionListener() {

        @Override
        public void onTranslate(String originalText) {
            //文字编辑页跳转至翻译页面

        }

        @Override
        public void changeText(int textMode, TemporaryTextData temporaryTextData, String text) {
            if (temporaryTextData != null && getPresenter() != null) {
                getPresenter().changeTextAutoStr(textMode, text, temporaryTextData.getPoints());
            }
        }

        @Override
        public void changeSize(int textMode, TemporaryTextData temporaryTextData, float size) {
            if (temporaryTextData != null && getPresenter() != null) {
                getPresenter().changeTextSize(textMode, (int) size, temporaryTextData.getPoints());
            }
        }

        @Override
        public void changeFont(int textMode, TemporaryTextData temporaryTextData, String font, boolean down) {
            if (temporaryTextData != null && getPresenter() != null) {
                getPresenter().changeTextFont(getContext(), textMode, font, temporaryTextData.getPoints());
            }
        }

        @Override
        public void changeColor(int textMode, TemporaryTextData temporaryTextData, String color) {
            if (temporaryTextData != null && getPresenter() != null) {
                getPresenter().changeTextColor(textMode, PuzzlesUtils.strColor2Int(color), temporaryTextData.getPoints());
            }
        }

        @Override
        public void save(int textMode, TemporaryTextData temporaryTextData, String text) {
            //保存昵称
            if (temporaryTextData != null && temporaryTextData.getTextData() != null &&
                    temporaryTextData.getTextData().isNickname() && !TextUtils.isEmpty(text)) {
                if (!temporaryTextData.getTextData().getAutoStr().equals(text)) {
//                    AppConfiguration.getDefault().setPuzzleNickname(text);
                }
            }
        }

        @Override
        public void onClose() {
            if (mPuzzleFrame != null) {
                mPuzzleFrame.scrollEditBack();
            }
        }
    };

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


    private void popBottomEditLayoutView() {
//        mPuzzleFrame.onClearSelected();
//        getPresenter().setAddInfoVisible(false);

        if (mEditLayoutView == null) {
            mEditLayoutView = new BottomEditLayoutView(getContext());
            ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            this.addView(mEditLayoutView, mParams);
        } else {
            mEditLayoutView.setVisibility(VISIBLE);
        }

        // 获取数据
        RotationImg[] rotationImgs = getPresenter().getFirstTemplateImage();

        PuzzlesLayoutInfo layoutInfo = getPresenter()
                .getIndexOfTemplateInfo(0).getPuzzlesLayoutInfo();

        // 改变底部View的高度，从而改变画布大小
        PuzzlesUtils.setBottomViewHeight(Utils.getRealPixel3(346));
        getPresenter().changeLayoutView(getContext());

        // open布局View
        mEditLayoutView.open(rotationImgs, layoutInfo.getSelectedRatio(),
                layoutInfo.getSelectedLayout());

        // 设置接口回调
        mEditLayoutView.setOnLayoutChangedListener(mOnLayoutChangeListener);

        AnimUtils.setTransAnim(mEditLayoutView, 0,
                0, 1, 0, 500, new AnimUtils.AnimEndCallBack() {
                    @Override
                    public void endCallBack(Animation animation) {
                        mEditLayoutView.setDismiss(false);
                    }
                });
    }

    private void popBottomLineFrameView() {
//        mPuzzleFrame.onClearSelected();
        if (mPuzzleMode == PuzzleMode.MODE_LAYOUT) {
            getPresenter().setAddInfoVisible(false);
        }

        if (mEditLineFrameView == null) {
            mEditLineFrameView = new BottomEditLineFrameView(getContext());
            ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            this.addView(mEditLineFrameView, mParams);
        } else {
            mEditLineFrameView.setVisibility(VISIBLE);
        }

        // 获取数据
        RotationImg[] rotationImgArr = getPresenter().getFirstTemplateImage();
        if (rotationImgArr == null || rotationImgArr.length == 0) {
            return;
        }

        if (mPuzzleMode == PuzzleMode.MODE_LAYOUT) {
            // 改变底部View的高度，从而改变画布大小
            PuzzlesUtils.setBottomViewHeight(Utils.getRealPixel3(346));

            PuzzlesLayoutInfo layoutInfo = getPresenter().getIndexOfTemplateInfo(0).getPuzzlesLayoutInfo();

            getPresenter().changeLayoutView(getContext());
            mEditLineFrameView.open(rotationImgArr.length,
                    layoutInfo.getPiecePaddingRatio(),
                    layoutInfo.getOuterPaddingRatio(),
                    layoutInfo.getPieceRadianRatio());

        } else {
//            PuzzlesLayoutJointInfo puzzlesLayoutJointInfo = getPresenter()
//                    .getIndexOfTemplateInfo(0).getPuzzlesLayoutJointInfo();
//            mEditLineFrameView.open(rotationImgArr.length,
//                    puzzlesLayoutJointInfo.getPiecePaddingRatio(),
//                    puzzlesLayoutJointInfo.getOuterPaddingRatio(),
//                    puzzlesLayoutJointInfo.getPieceRadianRatio());

        }

        mEditLineFrameView.setOnParameChangedListener(mParameChangedListener);

        AnimUtils.setTransAnim(mEditLineFrameView, 0,
                0, 1, 0, 500, new AnimUtils.AnimEndCallBack() {
                    @Override
                    public void endCallBack(Animation animation) {
                        mEditLineFrameView.setDismiss(false);
                    }
                });
    }

    private BottomEditLayoutView.OnLayoutChangedListener mOnLayoutChangeListener = new BottomEditLayoutView.OnLayoutChangedListener() {
        @Override
        public void onLayoutChanged(float ratio, int layout, LayoutData layoutData) {
            getPresenter().onLayoutChanged(getContext(), false, ratio, layout, layoutData);
        }

        @Override
        public void onViewChanged(float ratio, int layout, LayoutData layoutData) {
            getPresenter().onLayoutChanged(getContext(), true, ratio, layout, layoutData);
        }

        @Override
        public void onDismiss() {
            PuzzlesUtils.setBottomViewHeight(0);
            getPresenter().changeLayoutView(getContext());
            getPresenter().setAddInfoVisible(true);
        }
    };

    private BottomEditLineFrameView.OnParameChangedListener mParameChangedListener = new BottomEditLineFrameView.OnParameChangedListener() {
        @Override
        public void onChanged(BottomEditLineFrameView.CHANGEDMode mode, int value) {
            if (mPuzzleMode == PuzzleMode.MODE_LAYOUT) {
                getPresenter().onLayoutPaddingChanged(getContext(), mode, value);
            } else {
//                getPresenter().changeLayoutJointParame(mode, value);
            }
        }

        @Override
        public void onDismiss() {

            if (mPuzzleMode == PuzzleMode.MODE_LAYOUT) {
                PuzzlesUtils.setBottomViewHeight(0);
                getPresenter().changeLayoutView(getContext());
            }
            getPresenter().setAddInfoVisible(true);
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
    public void onClick(View view) {
        if (view == mCancelBtn) {
            onBack();
        }
    }

    public void onBack() {
        if (isOnBackShowView()) {
            return;
        }
        EventBus.getDefault().unregister(this);
        ((Activity)mContext).finish();
    }

    /**
     * pop出来的辅助view先退出
     *
     * @return boolean
     */
    private boolean isOnBackShowView() {
        boolean isOnBackView = false;
        if (mEditBgTextureLayout != null) {
            mEditBgTextureLayout.onBack();
            isOnBackView = true;
        }

        if (mEditTextView != null && mEditTextView.getVisibility() == VISIBLE) {
            mEditTextView.onBack();
            isOnBackView = true;
        }

        if (mEditLineFrameView != null && mEditLineFrameView.getVisibility() == VISIBLE) {
            mEditLineFrameView.onBack();
            isOnBackView = true;
        }

        if (mEditLayoutView != null && mEditLayoutView.getVisibility() == VISIBLE) {
            mEditLayoutView.onBack();
            isOnBackView = true;
        }

        return isOnBackView;
    }

    private void getIntentData(Intent intent, boolean isBack) {
        if (!isBack) {
            mPhotos = intent.getParcelableArrayListExtra(EXTRA_PHOTOS);
        }
        mTemplateId = intent.getStringExtra(PuzzleActivity.EXTRA_TEMPLATE_ID);
        mTemplateRatio = intent.getFloatExtra(PuzzleActivity.EXTRA_TEMPLATE_RATIO, 0);
        mTemplateCategory = intent.getIntExtra(
                PuzzleActivity.EXTRA_TEMPLATE_CATEGORY,
                DataConstant.TEMPLATE_CATEGORY.SIMPLE);
    }

    public void handleTransformTemplateResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            getIntentData(intent, true);
            initData(mTemplateId, mPhotos, mTemplateCategory, mTemplateRatio);
        }
    }

    public void handleEditSignatureResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getPresenter().updateSignatureParams(mContext, data, mPuzzleFrame.getScrollYOffset());
        }
    }

    public void handleEditLabelResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getPresenter().updateLabelPageParams(mContext, data, mPuzzleFrame.getScrollYOffset());
        }
    }
}
