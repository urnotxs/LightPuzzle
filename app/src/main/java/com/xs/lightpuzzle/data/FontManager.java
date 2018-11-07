package com.xs.lightpuzzle.data;

import com.blankj.utilcode.util.FileUtils;
import com.xs.lightpuzzle.data.dao.FontQuery;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.yszx.Scheme;

import java.util.List;

/**
 * Created by xs on 2018/11/7.
 */

public class FontManager {

    public static boolean save(Font font) {
        return DataManager.getDefault().saveFont(font);
    }

    public static Font get(String id) {
        return DataManager.getDefault().getFont(id);
    }

    public static List<Font> list() {
        return DataManager.getDefault().getFonts(new FontQuery.Builder().setOrderTypeAsc(true).build());
    }

    public static String getTypefaceUri(String id) {
        Font font = DataManager.getDefault().getFont(id);
        if (font != null && font.isDownloaded()) {
            String filePath = font.getFilePath();
            if (FileUtils.isFileExists(filePath)){
                return Scheme.FILE.wrap(filePath);
            }else{
                font.setDownloaded(false);
                FontManager.save(font);
            }
        }
        return null;
    }
}
