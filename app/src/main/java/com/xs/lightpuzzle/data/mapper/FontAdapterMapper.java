package com.xs.lightpuzzle.data.mapper;

import com.xs.lightpuzzle.data.PuzzleFileExtension;
import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.data.entity.adapter.FontAdapter;
import com.xs.lightpuzzle.data.util.MaterialDirPathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xs on 2018/11/5.
 */

public class FontAdapterMapper {
    public static Font transform(FontAdapter adapter) {
        if (adapter != null) {
            Font font = new Font();

            String id = adapter.getId();
            String name = adapter.getName();
            String url = adapter.getUrl();
            String thumbUrl = adapter.getThumbUrl();

            font.setId(id);
            font.setName(name);
            font.setSize(adapter.getSize());
            font.setThumbUrl(thumbUrl);
            font.setType(font.getType());
            font.setUrl(url);
            font.setOrder(adapter.getOrder());

            font.setFileName(adapter.getFileName());
            font.setThumbFileName(PuzzleFileExtension.mapFile(adapter.getThumbFileName()));

            String dirPath = MaterialDirPathHelper.font(id);
            font.setDirPath(dirPath);

            return font;
        }
        return null;
    }

    public static List<Font> transform(Collection<FontAdapter> adapterCollection) {
        if (adapterCollection != null && !adapterCollection.isEmpty()) {
            List<Font> fonts = new ArrayList<>();
            for (FontAdapter adapter : adapterCollection) {
                fonts.add(transform(adapter));
            }
            return fonts;
        }
        return null;
    }
}
