package com.xs.lightpuzzle.data.entity.adapter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @version 1.0
 * @since 12/22/17
 */
public class BackgroundMapper {

    @SerializedName("color")
    private int color;
    @SerializedName("tint_color")
    private int tintColor;
    @SerializedName("texture_composite")
    private int textureComposite;
    @SerializedName("texture_alpha_mapper_list")
    private List<TextureAlphaMapper> textureAlphaMappers;

    public BackgroundMapper() {
    }

    public BackgroundMapper(int color, int tintColor, int textureComposite,
                            List<TextureAlphaMapper> textureAlphaMappers) {
        this.color = color;
        this.tintColor = tintColor;
        this.textureComposite = textureComposite;
        this.textureAlphaMappers = textureAlphaMappers;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTintColor() {
        return tintColor;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public int getTextureComposite() {
        return textureComposite;
    }

    public void setTextureComposite(int textureComposite) {
        this.textureComposite = textureComposite;
    }

    public List<TextureAlphaMapper> getTextureAlphaMappers() {
        return textureAlphaMappers;
    }

    public void setTextureAlphaMappers(List<TextureAlphaMapper> textureAlphaMappers) {
        this.textureAlphaMappers = textureAlphaMappers;
    }

    public static class TextureAlphaMapper {

        @SerializedName("texture_id")
        private long textureId;
        @SerializedName("alpha")
        private int alpha;

        public TextureAlphaMapper() {
        }

        public TextureAlphaMapper(long textureId, int alpha) {
            this.textureId = textureId;
            this.alpha = alpha;
        }

        public long getTextureId() {
            return textureId;
        }

        public void setTextureId(long textureId) {
            this.textureId = textureId;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }
    }
}
