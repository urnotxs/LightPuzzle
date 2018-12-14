package com.xs.lightpuzzle.puzzle.view.textedit.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.data.FontManager;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.puzzle.data.editdata.TemporaryTextData;
import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;
import com.xs.lightpuzzle.puzzle.dialog.UIAlertViewDialog;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.textedit.adapter.FontAdapter;
import com.xs.lightpuzzle.puzzle.view.textedit.adapter.SizeAdapter;
import com.xs.lightpuzzle.puzzle.view.textedit.model.FontItemInfo;
import com.xs.lightpuzzle.puzzle.view.textedit.model.SizeItemInfo;
import com.xs.lightpuzzle.puzzle.view.textedit.textinterface.OnInputListener;
import com.xs.lightpuzzle.puzzle.view.texturecolor.ColorLayout;
import com.xs.lightpuzzle.puzzle.view.texturecolor.TextureColorHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.xs.lightpuzzle.puzzle.view.textedit.BottomEditTextView.EDIT_TEMPLATE_TEXT;

/**
 * Created by urnot_XS on 2018/4/11.
 */

public class TextEditBottomView extends LinearLayout implements View.OnClickListener{

    private Context mContext;

    private LinearLayout mTopLayout;
    private LinearLayout mFontBtn;
    private ImageView mFontIcon;
    private TextView mFontText;
    private LinearLayout mColorBtn;
    private ImageView mColorIcon;
    private TextView mColorText;
    private LinearLayout mSizeBtn;
    private ImageView mSizeIcon;
    private TextView mSizeText;
    private LinearLayout mBottomLayout;
    private ListView mFontList;
    private ListView mSizeList;
    private LinearLayout mColorContainer;
    private ColorLayout mColorLayout;

    private FontAdapter mFontAdapter;
    private SizeAdapter mSizeAdapter;

    private ArrayList<FontItemInfo> mFontItemInfos = new ArrayList<>();
    private ArrayList<SizeItemInfo> mSizeItemInfos = new ArrayList<>();
    protected int[] mColorItemInfos;

    private int mMiddleHeight = TextEditTotalView.EDIT_MIDDLE_HEIGHT;
    private int mViewHeight = TextEditTotalView.EDIT_BOTTOM_HEIGHT;

    private int mEditModel;
    private int MaxFontSize;
    private int MinFontSize;
    private int mColorIndex = -1;
    private int mFontIndex = -1;
    private int mSizeIndex = -1;
    private TextData mTextData;
    private OnInputListener mOnInputListener;

    public void unLockVip(){
        mColorLayout.unLock();
    }

    public TextEditBottomView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutParams llParams;
        llParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, mMiddleHeight);
        mTopLayout = new LinearLayout(mContext);//ll2
        mTopLayout.setGravity(LinearLayout.HORIZONTAL);
        addView(mTopLayout, llParams);
        {
            //字体类型
            llParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT,1);
            mFontBtn = new LinearLayout(mContext);//ll2_1
            mFontBtn.setGravity(Gravity.CENTER);
            mFontBtn.setOnClickListener(this);
            mTopLayout.addView(mFontBtn, llParams);
            {
                llParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                LinearLayout fontLayout = new LinearLayout(mContext);
                fontLayout.setOrientation(HORIZONTAL);
                mFontBtn.addView(fontLayout, llParams);
                {
                    llParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    mFontIcon = new ImageView(mContext);
                    mFontIcon.setImageResource(R.drawable.text_font_hover);
                    fontLayout.addView(mFontIcon, llParams);

                    llParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    mFontText = new TextView(mContext);
                    mFontText.setTextColor(Color.argb(100, 255, 255, 255));
                    mFontText.setText(R.string.longpage_font);
                    fontLayout.addView(mFontText, llParams);
                }
            }

            //分割线
            llParams = new LayoutParams(
                    1, (int) (Utils.getScreenH() * 0.043) , 0);
            llParams.gravity = Gravity.CENTER_VERTICAL;
            ImageView bar_spite = new ImageView(mContext);
            bar_spite.setImageResource(R.drawable.bar_splic);
            bar_spite.setScaleType(ImageView.ScaleType.FIT_XY);
            mTopLayout.addView(bar_spite, llParams);

            //字体颜色
            llParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT,1);
            mColorBtn = new LinearLayout(mContext);
            mColorBtn.setGravity(Gravity.CENTER);
            mColorBtn.setOnClickListener(this);
            mTopLayout.addView(mColorBtn, llParams);
            {
                llParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                LinearLayout colorLayout = new LinearLayout(mContext);
                colorLayout.setOrientation(HORIZONTAL);
                mColorBtn.addView(colorLayout, llParams);
                {
                    llParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    mColorIcon = new ImageView(mContext);
                    mColorIcon.setImageResource(R.drawable.text_color);
                    colorLayout.addView(mColorIcon, llParams);

                    llParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    mColorText = new TextView(mContext);
                    mColorText.setTextColor(Color.WHITE);
                    mColorText.setText(R.string.longpage_color);
                    colorLayout.addView(mColorText, llParams);
                }
            }

            //分割线
            llParams = new LayoutParams(
                    1, (int) (Utils.getScreenH() * 0.043),0);
            llParams.gravity = Gravity.CENTER_VERTICAL;
            bar_spite = new ImageView(mContext);
            bar_spite.setImageResource(R.drawable.bar_splic);
            bar_spite.setScaleType(ImageView.ScaleType.FIT_XY);
            mTopLayout.addView(bar_spite, llParams);

            //字体大小
            llParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT , 1);
            mSizeBtn = new LinearLayout(mContext);
            mSizeBtn.setGravity(Gravity.CENTER);
            mSizeBtn.setOnClickListener(this);
            mTopLayout.addView(mSizeBtn, llParams);
            {
                llParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                LinearLayout sizeLayout = new LinearLayout(mContext);
                sizeLayout.setOrientation(HORIZONTAL);
                mSizeBtn.addView(sizeLayout, llParams);
                {
                    llParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    mSizeIcon = new ImageView(mContext);
                    mSizeIcon.setImageResource(R.drawable.text_size);
                    sizeLayout.addView(mSizeIcon, llParams);

                    llParams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    mSizeText = new TextView(mContext);
                    mSizeText.setTextColor(Color.WHITE);
                    mSizeText.setText(R.string.longpage_size);
                    sizeLayout.addView(mSizeText, llParams);
                }
            }
        }

        //分割线
        llParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, 1);
        ImageView line = new ImageView(mContext);
        line.setImageResource(R.drawable.black_line);
        line.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(line, llParams);

        //底部列表
        llParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                0 , 1);
        mBottomLayout = new LinearLayout(mContext);
        mBottomLayout.setOnClickListener(null);
        addView(mBottomLayout, llParams);
        {
            llParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            mFontList = new ListView(mContext);
            mFontList.setCacheColorHint(0x00000000);
            mFontList.setDividerHeight(0);
            mFontList.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mFontList.setSelector(R.drawable.listview_selector);
            mFontAdapter = new FontAdapter(mContext);
            mFontList.setAdapter(mFontAdapter);
            mFontAdapter.setData(mFontItemInfos);
            mFontList.setOnItemClickListener(mFontListOnItemClickListener);
            mFontList.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
            mBottomLayout.addView(mFontList, llParams);

            llParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            mSizeList = new ListView(mContext);
            mSizeList.setCacheColorHint(0x00000000);
            mSizeList.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mSizeList.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
            mSizeList.setDividerHeight(0);
            mSizeList.setSelector(R.drawable.listview_selector);
            mSizeList.setVisibility(GONE);
            mSizeAdapter = new SizeAdapter(mContext);
            mSizeList.setAdapter(mSizeAdapter);
            mSizeList.setOnItemClickListener(mSizeListOnItemClickListener);
            mSizeAdapter.setData(mSizeItemInfos);
            mBottomLayout.addView(mSizeList, llParams);

            llParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    Utils.getRealPixel3(372));
            llParams.gravity = Gravity.CENTER_VERTICAL;
            mColorContainer = new LinearLayout(mContext);
            mBottomLayout.addView(mColorContainer, llParams);
        }
    }

    public void setEditModel(int editModel){
        mEditModel = editModel;
    }

    public void setOnInputListener(OnInputListener listener){
        mOnInputListener = listener;
    }

    public void setTextInfo(TemporaryTextData data){
        mTextData = data.getTextData();

        initSizeDatas();
        initFontDatas();
        initColorDatas(); // 获取颜色素材，并初始颜色布局
        initSelectStatus();
    }

    private void initSizeDatas() {
        //清理数据
        for (int i = 0; i < mSizeItemInfos.size(); i++) {
            SizeItemInfo listInfo = mSizeItemInfos.get(i);
            listInfo.setText(null);
        }
        mSizeItemInfos.clear();

        //load数据
        if (mEditModel == EDIT_TEMPLATE_TEXT) {
            MaxFontSize = mTextData.getMaxFontSize();
            MinFontSize = mTextData.getMinFontSize();
            //模板字体6个档位
            int separate = (MaxFontSize - MinFontSize) / 4;

            if (separate == 0) {//兼容视频模板MinFontSize==MaxFontSize的情况
                separate = 5;
                setListItemInfoSize((MaxFontSize - 3 * separate));//MaxFontSize，文本给的字体大小已经做了比例适配
                setListItemInfoSize((MaxFontSize - 2 * separate));
                setListItemInfoSize((MaxFontSize - 1 * separate));
                setListItemInfoSize((MaxFontSize));
                setListItemInfoSize((MaxFontSize));
            } else {
                setListItemInfoSize((MinFontSize));
                setListItemInfoSize((MinFontSize + separate));
                setListItemInfoSize((MinFontSize + 2 * separate));
                setListItemInfoSize((MinFontSize + 3 * separate));
                setListItemInfoSize((MaxFontSize));
            }
        } else {
            //手动输入文字10个档位
            MinFontSize = 10 ;
            MaxFontSize = 100;
            setListItemInfoSize(10);
            setListItemInfoSize(20);
            setListItemInfoSize(30);
            setListItemInfoSize(40);
            setListItemInfoSize(50);
            setListItemInfoSize(60);
            setListItemInfoSize(70);
            setListItemInfoSize(80);
            setListItemInfoSize(90);
            setListItemInfoSize(100);

        }
        if (mSizeAdapter != null) {
            mSizeAdapter.notifyDataSetChanged();
        }
    }

    private void initFontDatas() {

        if (mFontItemInfos != null && mFontItemInfos.size() > 0) {
            mFontItemInfos.clear();
        }
        //获取内置China字体
        List<Font> fontList = null;

        fontList = FontManager.list();
        for (Font font : fontList){
            FontItemInfo listInfo = new FontItemInfo();
            listInfo.setImageUrl(font.getThumbUrl());
            listInfo.setFont(font.getName());
            listInfo.setDownLoadBmp(false);
            float c = (float) font.getSize() / 1024f / 1024f;
            String DownSize = new DecimalFormat("0.0").format(c) + "M";
            if (DownSize.contains("0.0")) {
                DownSize = "0.1M";
            }
            listInfo.setDownText(DownSize);
            listInfo.setNeedDownFont(!font.isDownloaded());
            listInfo.setDownTextShow(true);
            listInfo.setFontInfo(font);
            mFontItemInfos.add(listInfo);
        }

        if (mFontItemInfos != null) {
            for (int i = 0; i < mFontItemInfos.size(); i++) {
                if (mFontItemInfos.get(i).getFontInfo() != null) {
                    mFontItemInfos.get(i).setDowning(false);
                }
            }

            for (FontItemInfo tempFont : mFontItemInfos) {

                String path = tempFont.getFontInfo().getFilePath();
                String temp = tempFont.getFont();
                String filename = temp.substring(temp.lastIndexOf('/') + 1, temp.length());
                if (FileUtils.isFileExists(path)) {
//                    tempFont.getFontInfo().setDownedSuccess(true);
                    tempFont.setNeedDownFont(false);
                    tempFont.setDownTextShow(false);
                    tempFont.setFont(path);
//                    tempFont.getFontInfo().fontResDownload = null;
                } else {
//                    tempFont.getFontInfo().setDownedSuccess(false);
                    tempFont.setNeedDownFont(true);
                    tempFont.setDownTextShow(true);
                }
            }
        }

        if (mFontAdapter != null) {
            mFontAdapter.setData(mFontItemInfos);
            mFontAdapter.notifyDataSetChanged();
        }
    }

    private void initColorDatas(){
        if (mColorLayout == null){
            LayoutParams llParams;
            llParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            mColorItemInfos = TextureColorHelper.getInstance().loadFontColorValue();
            mColorLayout = new ColorLayout(getContext(),mColorItemInfos,ColorLayout.TEXT_COLOR);
            mColorLayout.setColorClickCallback(new ColorLayout.ClickColorListener() {
                @Override
                public void clickCallback(int index) {
                    mColorIndex = index;
                    if (mOnInputListener != null) {
                        // 转成16进制字符串
                        byte[] b = new byte[3];
                        for (int i = 1; i < 4; i++) {
                            b[i - 1] = (byte) (mColorItemInfos[index] >> (24 - i * 8));
                        }
                        mOnInputListener.changeColor(ConvertUtils.bytes2HexString(b).toLowerCase());
                    }

                }
            });
            mColorContainer.addView(mColorLayout ,llParams);
        }
    }

    private void initSelectStatus() {
        String font = null;
        int fontColor = -2;
        int fontSize = -1;
        font = mTextData.getFont();
        fontColor = mTextData.getFontColor();
        fontSize = mTextData.getFontSize();
        //选中当前的字体
        if (mFontItemInfos != null && font != null) {
            for (int i = 0; i < mFontItemInfos.size(); i++) {
                if (mFontItemInfos.get(i).getFont().equals(font)){
                    mFontIndex = i ;
                    mFontAdapter.setFontIndex(i);
                }
            }
        }
        // 选中当前颜色
        boolean isFind = false;
        if (mColorItemInfos != null && fontColor != -2) {
            for (int i = 0; i < mColorItemInfos.length; i++) {
                if (fontColor == mColorItemInfos[i]) {
                    mColorIndex = i;
                    mColorLayout.setIndex(mColorIndex);
                    isFind = true;
                    break;
                }
            }
        }
        if (!isFind) {
            mColorLayout.clearSelect();
        }

        // 选中当前字号
        if (mSizeItemInfos != null && fontSize != -1) {
            for (int i = 0; i < mSizeItemInfos.size(); i++) {
                //可能有1- 2px误差
                float dataSize = mSizeItemInfos.get(i).getTextSize();
                if (Math.abs(((float)fontSize) - dataSize) <= 1.2f){
                    mSizeIndex = i;
                    mSizeItemInfos.get(i).setCheck(true);
                    break;
                }
            }
        }
        mSizeList.setSelection(mSizeIndex);
        mFontList.setSelection(mFontIndex);
    }

    private AdapterView.OnItemClickListener mSizeListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            //通知编辑页重绘字体大小
            //
            if (mOnInputListener != null) {
                mOnInputListener.changeSize(mSizeItemInfos.get(position).getTextSize());
            }

            if (mSizeIndex != -1 && mSizeIndex < mSizeItemInfos.size()) {
                mSizeItemInfos.get(mSizeIndex).setCheck(false);
            }
            mSizeItemInfos.get(position).setCheck(true);
            mSizeIndex = position;

            mSizeAdapter.notifyDataSetChanged();
        }
    };

    private AdapterView.OnItemClickListener mFontListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            // TODO: 2018/12/11  
            // 字体是否正在下载中
            boolean tempDowning = false;
            if (mFontItemInfos.get(position).getFontInfo() != null) {
                tempDowning = !mFontItemInfos.get(position).getFontInfo().isDownloaded();
            }
            // 下载一半再次进入页面时更新数据
            if (mFontItemInfos.get(position).getFontInfo() != null) {
                mFontItemInfos.get(position).setNeedDownFont(mFontItemInfos.get(position).getFontInfo().isDownloaded());
            }
            // 不需下载或下载完情况下改变字体跟选中位置
            if (!mFontItemInfos.get(position).isNeedDownFont() && !tempDowning) {
                //区分2种字体获取方式
                //从SD卡 是需要下载的字体
                //从assert 是不需要下载的字体
                //通知编辑页重绘字体大小
                //
                if (mOnInputListener != null) {
                    mOnInputListener.changeFont(mFontItemInfos.get(position).getFont(),
                            mFontItemInfos.get(position).isDownLoadBmp());
                }
                //勾选位置
                mFontIndex = position ;
                mFontAdapter.setFontIndex(position);
            }

            if (mFontItemInfos.get(position).isDownLoadBmp()) { // 需要下载Bmp
                if (mFontItemInfos.get(position).getFontInfo().isDownloaded() && !tempDowning) {
                    //需要下载字体

                    final int pos = position;
                    UIAlertViewDialog uiAlertViewDialog = new UIAlertViewDialog(getContext());
                    uiAlertViewDialog/*.setTitle("提示")*/
                            .setMessage(getResources().getString(R.string.watermatkedittext_free_download))
                            .setNegativeButton(getResources().getString(R.string.watermatkedittext_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.watermatkedittext_download), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (mFontAdapter != null)
                                        mFontAdapter.setReadyDown(true , pos);
                                    mFontAdapter.notifyDataSetChanged();
                                }
                            })
                            .build()
                            .show();
                }
            }
            mFontAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View view) {
        if (view == mFontBtn){
            mFontList.setVisibility(VISIBLE);
            mSizeList.setVisibility(GONE);
            //mColorLayout.setVisibility(GONE);
            mFontIcon.setImageResource(R.drawable.text_font_hover);
            mColorIcon.setImageResource(R.drawable.text_color);
            mSizeIcon.setImageResource(R.drawable.text_size);
            mFontText.setTextColor(Color.argb(100, 255, 255, 255));
            mColorText.setTextColor(Color.argb(255, 255, 255, 255));
            mSizeText.setTextColor(Color.argb(255, 255, 255, 255));
        }else if (view == mColorBtn){
            mSizeList.setVisibility(GONE);
            mFontList.setVisibility(GONE);
            //mColorLayout.setVisibility(VISIBLE);
            mFontIcon.setImageResource(R.drawable.text_font);
            mColorIcon.setImageResource(R.drawable.text_color_hover);
            mSizeIcon.setImageResource(R.drawable.text_size);
            mFontText.setTextColor(Color.argb(255, 255, 255, 255));
            mColorText.setTextColor(Color.argb(100, 255, 255, 255));
            mSizeText.setTextColor(Color.argb(255, 255, 255, 255));
        }else if (view == mSizeBtn){
            mFontList.setVisibility(GONE);
            mSizeList.setVisibility(VISIBLE);
            //mColorLayout.setVisibility(GONE);
            mFontIcon.setImageResource(R.drawable.text_font);
            mColorIcon.setImageResource(R.drawable.text_color);
            mSizeIcon.setImageResource(R.drawable.text_size_hover);
            mFontText.setTextColor(Color.argb(255, 255, 255, 255));
            mColorText.setTextColor(Color.argb(255, 255, 255, 255));
            mSizeText.setTextColor(Color.argb(100, 255, 255, 255));
        }

    }

    /**
     * 设置字号List数据
     *
     * @param size 字号
     */
    private void setListItemInfoSize(float size) {
        SizeItemInfo listInfo = new SizeItemInfo();
        listInfo.setTextSize(size);
        mSizeItemInfos.add(listInfo);
    }
}