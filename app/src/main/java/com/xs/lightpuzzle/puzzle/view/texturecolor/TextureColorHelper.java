package com.xs.lightpuzzle.puzzle.view.texturecolor;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xs.lightpuzzle.LightPuzzleApplication;
import com.xs.lightpuzzle.puzzle.PuzzleConstant;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.EditFontColorBean;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleBackgroundBean;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleBlendAlphaBean;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleColorBean;
import com.xs.lightpuzzle.puzzle.view.texturecolor.bean.PuzzleTextureBean;
import com.xs.lightpuzzle.yszx.AssetManagerHelper;

import java.util.List;

/**
 * Created by xs on 2018/4/11.
 */

public class TextureColorHelper {
    private static TextureColorHelper INSTANCE;
    private List<PuzzleTextureBean> mTextureBeans;
    private PuzzleColorBean mColorBean;
    private List<PuzzleBlendAlphaBean> mBlendAlphaBeans;
    private List<EditFontColorBean> mEditFontColorBeans;

    public static TextureColorHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (TextureColorHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TextureColorHelper();
                }
            }
        }
        return INSTANCE;
    }

    private TextureColorHelper() {
        init();
    }

    /**
     * 获取背景颜色
     *
     * @return
     */
    public int[] loadBgColor() {
        int colorLen = 51;
        int[] colors = new int[colorLen];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = (int) (Long.parseLong("ff000000", 16) +
                    Long.parseLong(mColorBean.getColorInfos().get(i).getColorValue(), 16));

        }
        return colors;
    }

    /**
     * 获取纹理对应的资源id
     *
     * @return
     */
    public int[] loadTextureResId() {
        int textureLen = mTextureBeans.size();
        int[] resIds = new int[textureLen * 2];
        for (int i = 0; i < mTextureBeans.size(); i++) {
            String textureIcon = mTextureBeans.get(i).getTextureIcon();
            textureIcon = textureIcon.substring(0, textureIcon.lastIndexOf("."));
            String textureIconHover = mTextureBeans.get(i).getTextureIconHover();
            textureIconHover = textureIconHover.substring(0, textureIconHover.lastIndexOf("."));
            resIds[i * 2] = LightPuzzleApplication.getContext().getResources().getIdentifier(textureIcon
                    , "drawable", LightPuzzleApplication.getContext().getPackageName());
            resIds[i * 2 + 1] = LightPuzzleApplication.getContext().getResources().getIdentifier(textureIconHover
                    , "drawable", LightPuzzleApplication.getContext().getPackageName());
        }
        return resIds;
    }

    /**
     * 获取纹理vip锁
     *
     * @return
     */
    public boolean[] getTextureVips() {
        boolean[] isVips = new boolean[mTextureBeans.size()];
        for (int i = 0; i < mTextureBeans.size(); i++) {
            if ("vip".equals(mTextureBeans.get(i).getLock())) {
                isVips[i] = true;
            } else {
                isVips[i] = false;
            }
        }
        return isVips;
    }

    private void init() {
        mTextureBeans = getPuzzleTextureBean(LightPuzzleApplication.getContext());
        mColorBean = getBackgroundColorBean(LightPuzzleApplication.getContext());
        mBlendAlphaBeans = getPuzzleBlendAlphaBean(LightPuzzleApplication.getContext());
        mEditFontColorBeans = getFontColor(LightPuzzleApplication.getContext());
    }


    public PuzzleBackgroundBean getPuzzleBackgroundBean(int colorIndex, int textureIndex) {

        String bgColor = "";
        String fontColor = "";
        String blendModel = "";

        if (colorIndex != -1) {
            bgColor = mColorBean.getColorInfos().get(colorIndex).getColorValue();
            fontColor = mColorBean.getFontColors().get(colorIndex);
            blendModel = mColorBean.getBledModes().get(colorIndex);
        }

        String textureId = "";
        String texture = "";
        float alpha = 1.0f;

        if (textureIndex != -1) {
            textureId = mTextureBeans.get(textureIndex).getTextureId();
            texture = mTextureBeans.get(textureIndex).getTextureImage();

            // 获取对应的透明度
            String alphas = null;
            String textureIds = null;
            for (PuzzleBlendAlphaBean bean : mBlendAlphaBeans) {
                if (bgColor.equals(bean.getBgColor())) {
                    alphas = bean.getAlphas();
                    textureIds = bean.getTextureIds();
                    break;
                }
            }

            if (!TextUtils.isEmpty(alphas) && !TextUtils.isEmpty(textureIds)) {
                String[] alphaArr = alphas.split("(,)|(，)");
                String[] textureIdArr = textureIds.split("(,)|(，)");
                if (textureIdArr.length == alphaArr.length) {
                    for (int i = 0; i < textureIdArr.length; i++) {
                        String id = textureIdArr[i];
                        if (id.equals(textureId)) {
                            alpha = Float.parseFloat(alphaArr[i]);
                        }
                    }
                }
            }
        }

        return new PuzzleBackgroundBean(texture, bgColor, blendModel, fontColor, alpha);
    }

    /**
     * 获取纹理信息
     *
     * @param context
     * @return
     */
    private List<PuzzleTextureBean> getPuzzleTextureBean(Context context) {
        String json = AssetManagerHelper.convertInputString(context,
                PuzzleConstant.ASSET_DATA_PATH.TEXTURE_INFO_PATH);
        Gson gson = new Gson();
        List<PuzzleTextureBean> textureBeans =
                gson.fromJson(json, new TypeToken<List<PuzzleTextureBean>>() {
                }.getType());
        return textureBeans;
    }

    /**
     * 获取背景颜色
     *
     * @param context
     * @return
     */
    private PuzzleColorBean getBackgroundColorBean(Context context) {
        String json = AssetManagerHelper.convertInputString(context,
                PuzzleConstant.ASSET_DATA_PATH.BACKGROUND_COLOR_PATH);
        Gson gson = new Gson();
        PuzzleColorBean colorBean = gson.fromJson(json, PuzzleColorBean.class);
        return colorBean;
    }

    /**
     * 获取融合方式
     *
     * @param context
     * @return
     */
    private List<PuzzleBlendAlphaBean> getPuzzleBlendAlphaBean(Context context) {
        String json = AssetManagerHelper.convertInputString(context,
                PuzzleConstant.ASSET_DATA_PATH.TEXTURE_COLOR_ALPHA_PATH);
        Gson gson = new Gson();
        List<PuzzleBlendAlphaBean> blendAlphaBeans =
                gson.fromJson(json, new TypeToken<List<PuzzleBlendAlphaBean>>() {
                }.getType());
        return blendAlphaBeans;
    }

    /**
     * 获取修改字体的颜色值
     *
     * @param context
     * @return
     */
    public List<EditFontColorBean> getFontColor(Context context) {
        String json = AssetManagerHelper.convertInputString(context,
                PuzzleConstant.ASSET_DATA_PATH.FONT_COLOR_PATH);
        Gson gson = new Gson();
        List<EditFontColorBean> colorBeans = gson.fromJson(json,
                new TypeToken<List<EditFontColorBean>>() {
                }.getType());
        return colorBeans;
    }

    /**
     * 获取字体颜色值
     *
     * @return
     */
    public int[] loadFontColorValue() {
        int colorLen = mEditFontColorBeans.size();
        int[] colors = new int[colorLen];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = (int) (Long.parseLong("ff000000", 16) +
                    Long.parseLong(mEditFontColorBeans.get(i).getColorValue(), 16));

        }
        return colors;
    }

    public int getColorIndex(String bgColor) {
        if (TextUtils.isEmpty(bgColor)) {
            return -1;
        }
        int index = -1;
        List<PuzzleColorBean.ColorInfosBean> mColorInfos = mColorBean.getColorInfos();
        for (int i = 0; i < mColorInfos.size(); i++) {
            String color = mColorBean.getColorInfos().get(i).getColorValue();
            if (color.equals(bgColor)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int getTextureIndex(String texture) {
        if (TextUtils.isEmpty(texture)) {
            return -1;
        }
        int index = -1;
        for (int i = 0; i < mTextureBeans.size(); i++) {
            if (texture.equals(mTextureBeans.get(i).getTextureImage())) {
                index = i;
                break;
            }
        }
        return index;
    }

}
