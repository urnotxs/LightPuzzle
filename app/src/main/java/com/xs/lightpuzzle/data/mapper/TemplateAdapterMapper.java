package com.xs.lightpuzzle.data.mapper;

import com.xs.lightpuzzle.data.PuzzleFileExtension;
import com.xs.lightpuzzle.data.entity.Template;
import com.xs.lightpuzzle.data.entity.Template.Background;
import com.xs.lightpuzzle.data.entity.adapter.TemplateAdapter;
import com.xs.lightpuzzle.data.entity.adapter.TemplateAdapter.BackgroundAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xs on 2018/11/7.
 */

public class TemplateAdapterMapper {

    public static Template transform(TemplateAdapter adapter){
        if (adapter == null){
            return null;
        }

        Template template = new Template();

        template.setWidth(adapter.getWidth());
        template.setHeight(adapter.getHeight());
        template.setPhotoNum(adapter.getPhotoNum());
        template.setThumbFileName(PuzzleFileExtension.mapFile(adapter.getThumbFileName()));
        template.setBackground(transformBackground(adapter.getBackgroundAdapter()));
        template.setPhotos(transformPhoto(adapter.getPhotoAdapters()));
        template.setForegroundFileName(PuzzleFileExtension.mapFile(adapter.getForegroundFileName()));
        template.setForegroundMaskFileName(PuzzleFileExtension.mapFile(adapter.getForegroundMaskFileName()));
        template.setOrnaments(transformOrnament(adapter.getOrnamentAdapters()));
        template.setTexts(transformText(adapter.getTextAdapters()));
        template.setWatermark(transformWatermark(adapter.getWatermarkAdapter()));
        template.setAvatar(transformAvatar(adapter.getAvatarAdapter()));
        template.setQrCode(transformQrCode(adapter.getQrCodeAdapter()));
        template.setBusinessCard(transformBusinessCard(adapter.getBusinessCardAdapter()));

        return template;
    }

    private static Background transformBackground(BackgroundAdapter adapter) {
        if (adapter == null){
            return null;
        }

        Background background = new Background();
        background.setColor(adapter.getColor());
        return background;
    }

    private static List<Template.Photo> transformPhoto(
            Collection<TemplateAdapter.PhotoAdapter> adapterCollection) {
        if (adapterCollection == null || adapterCollection.isEmpty()) {
            return null;
        }

        List<Template.Photo> photos = new ArrayList<>();
        for (TemplateAdapter.PhotoAdapter adapter : adapterCollection){
            Template.Photo photo = transformPhoto(adapter);
            if (photo != null){
                photos.add(photo);
            }
        }
        return photos;
    }

    private static Template.Photo transformPhoto(
            TemplateAdapter.PhotoAdapter adapter) {
        if (adapter == null){
            return null;
        }
        Template.Photo photo = new Template.Photo();
        photo.setRegion(adapter.getRegion());
        photo.setRegionPathPointArr(adapter.getRegionPathPointArr());
        return photo;
    }

    private static List<Template.Ornament> transformOrnament(
            Collection<TemplateAdapter.OrnamentAdapter> ornamentAdapters) {
        if (ornamentAdapters == null || ornamentAdapters.isEmpty()){
            return null;
        }
        List<Template.Ornament> ornaments = new ArrayList<>();
        for (TemplateAdapter.OrnamentAdapter adapter
                : ornamentAdapters){
            Template.Ornament ornament = transformOrnament(adapter);
            if (ornament!=null){
                ornaments.add(ornament);
            }
        }
        return ornaments;
    }

    private static Template.Ornament transformOrnament(
            TemplateAdapter.OrnamentAdapter adapter) {
        if (adapter == null){
            return null;
        }
        Template.Ornament ornament = new Template.Ornament();
        ornament.setRegion(adapter.getRegion());
        ornament.setFileName(PuzzleFileExtension.mapFile(adapter.getFileName()));
        return ornament;
    }

    private static List<Template.Text> transformText(
            Collection<TemplateAdapter.TextAdapter> textCollection) {

        if (textCollection == null || textCollection.isEmpty()){
            return null;
        }
        List<Template.Text> texts = new ArrayList<>();
        for (TemplateAdapter.TextAdapter adapter : textCollection){
            Template.Text text = transformText(adapter);
            if (text != null){
                texts.add(text);
            }
        }
        return texts;
    }

    private static Template.Text transformText(
            TemplateAdapter.TextAdapter adapter) {
        if (adapter == null){
            return null;
        }
        Template.Text text = new Template.Text();

        text.setRegion(adapter.getRegion());
        text.setText(adapter.getText());
        text.setTextColor(adapter.getTextColor());
        text.setTypefaceId(adapter.getTypefaceId());
        text.setMinTextSize(adapter.getMinTextSize());
        text.setMaxTextSize(adapter.getMaxTextSize());
        text.setAlignment(adapter.getAlignment());
        text.setPaddingTop(adapter.getPaddingTop());

        return text;
    }

    private static Template.Watermark transformWatermark(
            TemplateAdapter.WatermarkAdapter adapter) {
        if (adapter == null) {
            return null;
        }
        Template.Watermark watermark = new Template.Watermark();
        watermark.setRegion(adapter.getRegion());
        watermark.setFileName(PuzzleFileExtension.mapFile(adapter.getFileName()));
        return watermark;
    }

    private static Template.Avatar transformAvatar(TemplateAdapter.AvatarAdapter adapter) {
        if (adapter == null) {
            return null;
        }
        Template.Avatar avatar = new Template.Avatar();
        avatar.setRegion(adapter.getRegion());
        avatar.setText(transformText(adapter.getTextAdapter()));
        return avatar;
    }

    private static Template.QrCode transformQrCode(TemplateAdapter.QrCodeAdapter adapter) {
        if (adapter == null) {
            return null;
        }
        Template.QrCode qrCode = new Template.QrCode();
        qrCode.setRegion(adapter.getRegion());
        return qrCode;
    }

    private static Template.BusinessCard transformBusinessCard(
            TemplateAdapter.BusinessCardAdapter adapter) {
        if (adapter == null) {
            return null;
        }
        Template.BusinessCard businessCard = new Template.BusinessCard();
        businessCard.setRegion(adapter.getRegion());
        businessCard.setTextColor(adapter.getTextColor());
        businessCard.setItemTintColor(adapter.getItemTintColor());
        businessCard.setMaxItemCount(adapter.getMaxItemCount());
        businessCard.setItemType(adapter.getItemType());
        businessCard.setItemFileNames(adapter.getItemFileNames()); // assets
        return businessCard;
    }

}
