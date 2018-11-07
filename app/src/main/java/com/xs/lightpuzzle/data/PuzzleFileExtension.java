package com.xs.lightpuzzle.data;

import android.text.TextUtils;
import android.util.Pair;

import com.blankj.utilcode.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/6.
 */

public final class PuzzleFileExtension {

    private static List<Pair<String, String>> sMapper = new ArrayList<>();
    private static final String PREFIX = "pmx"; // x -> a x b -> a_b

    static {
        sMapper.add(new Pair<>("png", PREFIX + "png"));
        sMapper.add(new Pair<>("jpg", PREFIX + "jpg"));
        sMapper.add(new Pair<>("jpeg", PREFIX + "jpeg"));
    }

    public static String mapFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            return filePath.substring(0, filePath.lastIndexOf('.') + 1)
                    + mapExtension(FileUtils.getFileExtension(filePath));
        }
    }

    private static String mapExtension(String fileExtension) {
        for (Pair<String, String> pair : sMapper) {
            if (pair.first.equalsIgnoreCase(fileExtension)) {
                return pair.second;
            }
        }
        return fileExtension;
    }
}
