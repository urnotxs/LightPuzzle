package com.xs.lightpuzzle.imagedecode.core;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.xs.lightpuzzle.imagedecode.core.ImageLoader.Scheme.DRAWABLE;


/**
 * Created by xs on 2018/3/29.
 */

public class ImageLoader {

    private Context mContext;

    public ImageLoader(Context mContext) {
        this.mContext = mContext;
    }

    public InputStream getStream(String uri) throws IOException {
        switch (Scheme.ofUri(uri)) {
            case FILE:
                return getStreamFromFile(uri);
            case ASSETS:
                return getStreamFromAssets(uri);
            case DRAWABLE:
                return getStreamFromDrawble(uri);
            case CONTENT:
                return getStreamFromContent(uri);
            default:
                return getStreamFromOtherSource(uri);
        }
    }

    private InputStream getStreamFromFile(String uri) throws FileNotFoundException {
        String filePath = Scheme.FILE.crop(uri);
        return new FileInputStream(filePath);
    }

    private InputStream getStreamFromAssets(String uri) throws IOException {
        String filePath = Scheme.ASSETS.crop(uri);
        return mContext.getAssets().open(filePath);
    }

    private InputStream getStreamFromDrawble(String uri) {
        String strDrawableId = DRAWABLE.crop(uri);
        if (!isNumeric(strDrawableId)) {
            return null;
        }
        int drawableId = Integer.parseInt(strDrawableId);
        final TypedValue value = new TypedValue();
        return mContext.getResources().openRawResource(drawableId, value);
    }

    private InputStream getStreamFromContent(String uri) {
        return null;
    }

    private InputStream getStreamFromOtherSource(String uri) throws IOException {
        throw new IOException("uri is error!");
    }

    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * uri组合划分
     */
    public enum Scheme {
        FILE("file"), CONTENT("content"),
        ASSETS("assets"), DRAWABLE("drawable"), UNKNOWN("");
        private String mScheme;
        private String mUriPrefix;

        Scheme(String mScheme) {
            this.mScheme = mScheme;
            this.mUriPrefix = mScheme + "://";
        }

        /**
         * Defines scheme of incoming URI
         *
         * @param uri URI for scheme detection
         * @return Scheme of incoming URI
         */
        public static Scheme ofUri(String uri) {
            if (TextUtils.isEmpty(uri)) {
                return UNKNOWN;
            }

            for (Scheme s : values()) {
                if (s.belongsTo(uri)) {
                    return s;
                }
            }

            return UNKNOWN;
        }

        private boolean belongsTo(String uri) {
            return uri.toLowerCase(Locale.US).startsWith(mUriPrefix);
        }

        /**
         * Appends scheme to incoming path
         */
        public String wrap(String path) {
            return mUriPrefix + path;
        }

        /**
         * Removed scheme part ("scheme://") from incoming URI
         */
        public String crop(String uri) {
            if (!belongsTo(uri)) {
                throw new IllegalArgumentException(String.format("URI [%_1$s] doesn't have " +
                        "expected scheme [%_2$s]", uri, mScheme));
            }

            return uri.substring(mUriPrefix.length());
        }
    }
}
