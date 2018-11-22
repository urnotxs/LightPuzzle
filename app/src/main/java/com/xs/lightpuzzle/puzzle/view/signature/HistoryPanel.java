package com.xs.lightpuzzle.puzzle.view.signature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.FileUtils;
import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.minnie.PuzzleConstant;
import com.xs.lightpuzzle.puzzle.util.Utils;
import com.xs.lightpuzzle.puzzle.view.EffectiveImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xs on 2018/8/23.
 */
public class HistoryPanel extends LinearLayout {

    private Context mContext;
    private ArrayList<String> mPicPaths = new ArrayList<String>();
    private ArrayList<Bitmap> mBitmapLists = new ArrayList<Bitmap>();
    private final int MAX_NUM = 5;
    private ImageView mSelectDeleteIcon = null;

    private int mItemWidth = Utils.getRealPixel3(160);
    private int mItemHeight = Utils.getRealPixel3(294);
    private int mSpace = Utils.getRealPixel3(5);

    private boolean mIsDeleteFirst = false;//是否是删除第0个，用来判断签名是否改变过，因为0个是显示的签名

    public HistoryPanel(Context context) {
        super(context);
        mContext = context;
        initUI();
        initDatas();
    }

    public void initDatas() {
        //多次调用，先清一下
        mPicPaths.clear();
        mBitmapLists.clear();

        String filePath = PuzzleConstant.SD_SIGNATURE_HISTORY_LIST_PATH;
        File baseDir = new File(filePath); // 创建一个File对象
        if (!baseDir.exists() || !baseDir.isDirectory()) { // 判断目录是否存在
            return;
        }
        //从时间最新到最旧取出
        File[] files = baseDir.listFiles();
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(files));
        while (fileList.size() > 0) {
            String string1 = fileList.get(0).getName();
            int min = 0;
            long temp = Long.parseLong(string1.substring(0, string1.indexOf(".")));
            for (int j = 1; j < fileList.size(); j++) {
                String string2 = fileList.get(j).getName();
                long temp2 = Long.parseLong(string2.substring(0, string2.indexOf(".")));
                if (temp > temp2) {
                    min = j;
                    temp = temp2;
                }//记录最小
            }

            //处理文件
            File tempFile = fileList.get(min);
            if (tempFile.isFile()) {
                String absolutePath = tempFile.getAbsoluteFile().toString();

                float minPaintWidth = mItemWidth/100.0f;
                Bitmap bitmap = SignaturePadHelper.getSignatureBitmap(
                        SignaturePadHelper.getSignatureSaveVO(absolutePath), mItemWidth , 0 ,
                        minPaintWidth , 0.5f , 30, false);

                if (bitmap != null) {
                    addItem(absolutePath, bitmap);
                } else {
                    FileUtils.deleteDir(absolutePath);//解码不出删除
                }
            }
            //移除处理完
            fileList.remove(min);
        }
    }

    private void initUI() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setOnClickListener(mOnClickListener);

        // 空控件
        for (int i = 0; i < MAX_NUM; i++) {
            LayoutParams llParams = new LayoutParams(mItemWidth, mItemHeight);
            llParams.leftMargin = mSpace;
            llParams.rightMargin = mSpace;
            llParams.bottomMargin = 2 * mSpace;
            FrameLayout item = new FrameLayout(mContext);
            addView(item, llParams);
            item.setOnLongClickListener(mOnLongClickListener);
            item.setOnClickListener(mOnClickListener);
            item.setTag(i);

            // 签名
            FrameLayout.LayoutParams fllp = new FrameLayout.LayoutParams(mItemWidth, mItemHeight);
            ImageView signature = new ImageView(mContext);
            item.addView(signature, fllp);
            signature.setImageBitmap(null);
            signature.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            // 删除
            fllp = new FrameLayout.LayoutParams(Utils.getRealPixel3(90), Utils.getRealPixel3(90));
            fllp.gravity = Gravity.CENTER;
            EffectiveImageButton delete = new EffectiveImageButton(mContext, R.drawable.signature_delete, R.drawable.signature_deletehover);
            item.addView(delete, fllp);
            delete.setOnClickListener(mOnClickListener);
            delete.setVisibility(View.GONE);
        }
    }

    /**
     * 增加一个条目
     */
    public void addItem(String picPath, Bitmap bitmap) {
        mPicPaths.add(picPath);
        mBitmapLists.add(bitmap);

        /**判断是否超出最大容量*/
        if (mBitmapLists.size() > MAX_NUM) {
            Log.v("test", "mPic:" + mPicPaths.toString() + mPicPaths.size() + "mbilis" + mBitmapLists.size());
            FileUtils.deleteDir(mPicPaths.get(0));//删除文件
            mPicPaths.remove(0);
            Bitmap bitmap1 = mBitmapLists.remove(0);
            Log.v("test", "mPic:" + mPicPaths.toString() + mPicPaths.size() + "mbilis" + mBitmapLists.size());
            if (bitmap1 != null && !bitmap1.isRecycled()) {
                bitmap1.recycle();
                bitmap1 = null;
            }
        }
        update();
    }

    /**
     * 保存成功时判断是否要删除文件
     */
    public void deleteSignatureFile() {
        if (mPicPaths.size() == MAX_NUM) {
            FileUtils.deleteDir(mPicPaths.get(0));//删除文件
        }
    }

    private void deleteItem(int select) {
        if (select == 0) mIsDeleteFirst = true;

        String filePath = mPicPaths.remove(select);
        FileUtils.deleteDir(filePath);//删除文件
        Bitmap bitmap = mBitmapLists.remove(select);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        update();
    }

    /**
     * 全部刷新
     */
    private void update() {
        FrameLayout frameLayout = null;
        ImageView imageView = null;
        /**设置内容*/
        int select = 0;
        for (int i = 0; i < MAX_NUM; i++) {
            frameLayout = (FrameLayout) getChildAt(i);
            frameLayout.getChildAt(1).setVisibility(View.GONE);//隐含删除控件
            imageView = (ImageView) frameLayout.getChildAt(0);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setImageBitmap(null);
            if (select <= mBitmapLists.size() - 1) {
                imageView.setBackgroundColor(0x66ffffff);
                imageView.setImageBitmap(mBitmapLists.get(select));
                Log.i("test", "i:" + i + " select:" + select);
            } else {
                break;
            }
            imageView.requestLayout();
            frameLayout.requestLayout();
            select++;
        }
    }

    /**
     * 是否有历史
     */
    public boolean hasItem() {
        if (mBitmapLists.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 隐藏删除控件
     */
    public void hideDeleteControl() {
        if (mSelectDeleteIcon != null) {
            mSelectDeleteIcon = null;
            update();
        }
    }

    private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int select = Integer.parseInt(((FrameLayout) v).getTag().toString());
            if (select > mBitmapLists.size() - 1) {
                return true;//不在有效控件中
            } else if (mSelectDeleteIcon != null) {
                hideDeleteControl();
                mSelectDeleteIcon = (ImageView) ((FrameLayout) v).getChildAt(1);
            } else {
                mSelectDeleteIcon = (ImageView) ((FrameLayout) v).getChildAt(1);
            }

            ImageView image = (ImageView) ((FrameLayout) v).getChildAt(0);
            image.setBackgroundColor(0xf0ffffff);
            image.setDrawingCacheEnabled(true);
            Bitmap tempBmp = Bitmap.createBitmap(image.getDrawingCache());
            image.setDrawingCacheEnabled(false);

//            tempBmp = JaneStyleBlur.blurCoverColor(tempBmp, 0x33000000);
            if (tempBmp != null) {
                image.setImageBitmap(tempBmp);
                ((FrameLayout) v).getChildAt(1).setVisibility(View.VISIBLE);
            }
            return true;
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof EffectiveImageButton) {
                FrameLayout layout = (FrameLayout) v.getParent();
                HistoryPanel.this.deleteItem(Integer.parseInt(layout.getTag().toString()));
                layout.getChildAt(1).setVisibility(View.GONE);
                mSelectDeleteIcon = null;//清除选中
            } else {
                if (mSelectDeleteIcon != null) {
                    hideDeleteControl();
                } else {
                    int select = Integer.parseInt(v.getTag().toString());
                    if (select > mBitmapLists.size() - 1) {
                        return;//不在有效控件中
                    } else {
                        String picPath = mPicPaths.get(select);
                        onClickOk(getContext(), picPath);
                    }
                }
            }
        }
    };

    private void onClickOk(Context context, String picPath) {
        // TODO: 2018/11/22  
    }

    /**
     * 释放内存
     */
    public void release() {
        for (int i = 0; i < mBitmapLists.size(); i++) {
            Bitmap temp = mBitmapLists.get(i);
            if (temp != null && !temp.isRecycled()) {
                temp.recycle();
                mBitmapLists.set(i, null);
            }
        }
        mBitmapLists.clear();
    }

    public boolean getIsDeleteFrist() {
        return mIsDeleteFirst;
    }

    public  int getItem(){
        return mPicPaths.size();
    }
}
