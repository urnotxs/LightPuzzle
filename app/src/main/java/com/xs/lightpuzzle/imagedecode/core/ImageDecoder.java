package com.xs.lightpuzzle.imagedecode.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.util.TypedValue;

import com.blankj.utilcode.util.CloseUtils;
import com.xs.lightpuzzle.imagedecode.BitmapHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lsq on 2018/3/29.
 */

public class ImageDecoder {
    private Context mContext;
    private ImageLoader mImageLoader;

    public ImageDecoder(Context context) {
        mContext = context;
        mImageLoader = new ImageLoader(mContext);
    }

    public Bitmap decode(ImageDecodingInfo imageDecodingInfo) {
        Bitmap decodeBitmap = null;
        InputStream imageStream = null;
        ImageFileInfo imageFileInfo = null;

        String uri = imageDecodingInfo.getUri();

        try {
            imageStream = mImageLoader.getStream(uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            imageFileInfo = defineImageSizeAndRotation(imageStream, imageDecodingInfo);
            Options options = prepareDecodingOptions(imageFileInfo.getImageSize(), imageDecodingInfo);

            imageStream = resetStream(imageStream, imageDecodingInfo);
            // drawable 的资源要考虑目录
            if (ImageLoader.Scheme.ofUri(uri).equals(ImageLoader.Scheme.DRAWABLE)) {
                TypedValue value = new TypedValue();
                // 对value赋值
                String strDrawableId = ImageLoader.Scheme.DRAWABLE.crop(uri);
                int drawableId = Integer.parseInt(strDrawableId);
                Resources res = mContext.getResources();
                res.getValue(drawableId, value, true);
                decodeBitmap = BitmapFactory.decodeResourceStream(res, value, imageStream, null, options);
            } else {
                if (imageFileInfo.isJpg()
                        && imageDecodingInfo.getDecodeImageOptions().isNativeDecode()) {
//                    byte[] data = CommonUtils.ReadData(imageStream);
//                    decodeBitmap = Utils.DecodeByteArr(data, options);
                } else {
                    decodeBitmap = BitmapFactory.decodeStream(imageStream, null, options);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(imageStream);
        }

        // 考虑旋转角度和外部decode参数
        if (BitmapHelper.isValid(decodeBitmap)) {
            if (imageDecodingInfo.getDecodeImageOptions().isNativeRetreatment()) {
//                decodeBitmap = considerExactScaleAndOrientationForNative(decodeBitmap, imageDecodingInfo,
//                        imageFileInfo.getExif().getRotation(), imageFileInfo.getExif().isFlipHorizontal());
            } else {
                decodeBitmap = considerExactScaleAndOrientation(decodeBitmap, imageDecodingInfo,
                        imageFileInfo.getExif().getRotation(), imageFileInfo.getExif().isFlipHorizontal());
            }
        }

        return decodeBitmap;
    }

    /**
     * 获取图片的宽高和角度
     *
     * @param imageStream
     * @param imageDecodingInfo
     * @return
     */
    private ImageFileInfo defineImageSizeAndRotation(
            InputStream imageStream, ImageDecodingInfo imageDecodingInfo) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);

        ExifInfo exifInfo = null;
        if (imageDecodingInfo.getDecodeImageOptions().isConsiderExifParams()
                && canDefineExifParams(imageDecodingInfo.getUri(), options.outMimeType)) {
            exifInfo = defineExifOritation(imageDecodingInfo.getUri(),
                    imageDecodingInfo.getDecodeImageOptions().getRotation());
        } else {
            exifInfo = new ExifInfo(imageDecodingInfo.getDecodeImageOptions().getRotation(),
                    false);
        }

        return new ImageFileInfo(new ImageSize(options.outWidth,
                options.outHeight, exifInfo.getRotation()), exifInfo,
                canDefineExifParams(imageDecodingInfo.getUri(), options.outMimeType));
    }

    /**
     * 获取decode Bitmap 的Options
     *
     * @param imageSize
     * @param decodingInfo
     * @return
     */
    private Options prepareDecodingOptions(ImageSize imageSize, ImageDecodingInfo decodingInfo) {

        // 外部传进的优先使用
        Options options = decodingInfo.getDecodeImageOptions().getDecodingOptions();
        if (options == null) {
            ImageScaleType imageScaleType = decodingInfo.getDecodeImageOptions().getImageScaleType();
            int scale = ImageSizeUtils.computePreareDecodingScale(imageSize,
                    decodingInfo.getTargetSize(), imageScaleType);
            options = DecodeImageOptions.createSimpleOptions();
            options.inSampleSize = scale;
        } else if (options.inSampleSize == 0) {
            ImageScaleType imageScaleType = decodingInfo.getDecodeImageOptions().getImageScaleType();
            int scale = ImageSizeUtils.computePreareDecodingScale(imageSize,
                    decodingInfo.getTargetSize(), imageScaleType);
            options.inSampleSize = scale;
        }

        options.inPreferredConfig = decodingInfo.getDecodeImageOptions().getBitmapConfig();

//         内存防爆处理
//        Log.d("imagedecoder:", "uri: " + decodingInfo.getUri());

        return preventOOMOptions(imageSize, options);
    }


    /**
     * 内存防爆计算
     *
     * @param imageSize
     * @param options
     * @return
     */
    private Options preventOOMOptions(ImageSize imageSize, Options options) {
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();           // 最大内存
        long totalMemory = rt.totalMemory();       // 已耗内存
        long remainMemory = maxMemory - totalMemory;// 剩余内存

//        Log.d("imagedecoder:", "最大内存: " + byte2M(maxMemory)
//                + " 已耗内存:" + byte2M(totalMemory) + " 剩余内存:" + byte2M(remainMemory));

        int inSampleSize = options.inSampleSize;
        long bmpMemory;
        do {
            bmpMemory = imageSize.getWidth() / inSampleSize
                    * imageSize.getHeight() / inSampleSize;
            switch (options.inPreferredConfig) {
                case RGB_565:
                case ARGB_4444:
                    bmpMemory = bmpMemory * 2;
                    break;
                case ARGB_8888:
                    bmpMemory = bmpMemory * 4;
                    break;
            }

            bmpMemory = bmpMemory * 2;

            // 容量足够，退出计算
            if (bmpMemory < remainMemory
                    || inSampleSize == 32) {
                break;
            }

            // 预留2倍的容量 options.inSampleSize=1，2，4，6，8，10 递增
            if (inSampleSize == 1) {
                inSampleSize = 2;
            } else {
                inSampleSize = inSampleSize + 2;
            }

        } while (true);

//        Log.d("imagedecoder:", "申请内存: " + byte2M(bmpMemory / 3) + " inSampleSize: " + inSampleSize);
        options.inSampleSize = inSampleSize;

        return options;
    }

    /**
     * 重新reset输入流，因为流可能被用过一次
     *
     * @param imageStream
     * @param decodingInfo
     * @return
     * @throws IOException
     */
    private InputStream resetStream(InputStream imageStream,
                                    ImageDecodingInfo decodingInfo) throws IOException {
        if (imageStream.markSupported()) {
            try {
                imageStream.reset();
                return imageStream;
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
        CloseUtils.closeIO(imageStream);
        return mImageLoader.getStream(decodingInfo.getUri());
    }

//    private Bitmap considerExactScaleAndOrientationForNative(Bitmap subsampledBitmap,
//                                                             ImageDecodingInfo decodingInfo,
//                                                             int rotation, boolean flipHorizontal) {
//        int bmpW = subsampledBitmap.getWidth();
//        int bmpH = subsampledBitmap.getHeight();
//        if (rotation % 180 != 0) {
//            int tmp = bmpW;
//            bmpW = bmpH;
//            bmpH = tmp;
//        }
//
//        int tarW = 1;
//        int tarH = 1;
//        ImageScaleType imageScaleType =
//                decodingInfo.getDecodeImageOptions().getImageScaleType();
//
//        if (decodingInfo.getTargetSize() != null) {
//            tarW = decodingInfo.getTargetSize().getWidth();
//            tarH = decodingInfo.getTargetSize().getHeight();
//        } else {
//            imageScaleType = ImageScaleType.NONE; // 目标大小为null 重设imageScaleType
//        }
//
//        float bmpRatio = 1.0f * bmpW / bmpH;
//        float tarRatio = 1.0f * tarW / tarH;
//
//        // 是否是裁剪某部分
//        boolean isClip = false;
//
//        // 计算缩放
//        // 无缩放
//        if (imageScaleType == ImageScaleType.NONE) {
//
//        }
//        // 安全缩放
//        else if (imageScaleType == ImageScaleType.NONE_SAFE) {
//
//        }
//        // 扩展到目标宽
//        else if (imageScaleType == ImageScaleType.EXACTLY_WIDTH) {
//
//        }
//        // 扩展到目标高
//        else if (imageScaleType == ImageScaleType.EXACTLY_HEIGHT) {
//
//        }
//        // 最短边铺满
//        else if (imageScaleType == ImageScaleType.EXACTLY) {
//            // 短边填满目标区域
//            if (bmpRatio > tarRatio) { // 位图高短
//                tarW = -1;
//            } else { // 位图宽短
//                tarH = -1;
//            }
//        }
//        // 完全在目标内部
//        else if (imageScaleType == ImageScaleType.CENTER_INSIDE) {
//
//        }
//        // 直接拉伸到xy
//        else if (imageScaleType == ImageScaleType.FIT_XY) {
//
//        }
//
//        // 居中裁剪
//        if (imageScaleType == ImageScaleType.EXACTLY_CENTER_CROP) {
//            isClip = true;
//        }
//
//        int flip = MakeBmpV2.FLIP_NONE;
//        if (flipHorizontal) {
//            flip = MakeBmpV2.FLIP_H;
//        }
//
//        Bitmap result = null;
//
//        if (isClip) {
//            result = MakeBmpV3.CreateFixBitmap(
//                    subsampledBitmap, rotation, flip, MakeBmpV2.POS_CENTER, tarW, tarH);
//        } else {
//            result = MakeBmpV3.CreateBitmap(
//                    subsampledBitmap, rotation, flip, -1, tarW, tarH);
//        }
//
//        if (subsampledBitmap != result) {
//            BitmapHelper.recycle(subsampledBitmap);
//        }
//
//        return result;
//    }

    /**
     * 考虑外部的参数和图片的本身自带信息
     *
     * @param subsampledBitmap
     * @param decodingInfo
     * @param rotation
     * @param flipHorizontal
     * @return
     */
    private Bitmap considerExactScaleAndOrientation(Bitmap subsampledBitmap,
                                                    ImageDecodingInfo decodingInfo,
                                                    int rotation, boolean flipHorizontal) {
        int bmpW = subsampledBitmap.getWidth();
        int bmpH = subsampledBitmap.getHeight();
        if (rotation % 180 != 0) {
            int tmp = bmpW;
            bmpW = bmpH;
            bmpH = tmp;
        }

        int tarW = 1;
        int tarH = 1;

        ImageScaleType imageScaleType =
                decodingInfo.getDecodeImageOptions().getImageScaleType();

        if (decodingInfo.getTargetSize() != null) {
            tarW = decodingInfo.getTargetSize().getWidth();
            tarH = decodingInfo.getTargetSize().getHeight();
        } else {
            imageScaleType = ImageScaleType.NONE; // 目标大小为null 重设imageScaleType
        }

        float bmpRatio = 1.0f * bmpW / bmpH;
        float tarRatio = 1.0f * tarW / tarH;

        float scaleX = 1.0f;
        float scaleY = 1.0f;
        // 计算缩放
        // 无缩放
        if (imageScaleType == ImageScaleType.NONE) {
            scaleX = 1;
            scaleY = scaleX;
        }
        // 安全缩放
        else if (imageScaleType == ImageScaleType.NONE_SAFE) {

        }
        // 扩展到目标宽
        else if (imageScaleType == ImageScaleType.EXACTLY_WIDTH) {
            scaleX = 1.0f * tarW / bmpW;
            scaleY = scaleX;
        }
        // 扩展到目标高
        else if (imageScaleType == ImageScaleType.EXACTLY_HEIGHT) {
            scaleY = 1.0f * tarH / bmpH;
            scaleX = scaleY;
        }
        // 最短边铺满
        else if (imageScaleType == ImageScaleType.EXACTLY
                || imageScaleType == ImageScaleType.EXACTLY_CENTER_CROP) {

            // 短边填满目标区域
            if (bmpRatio > tarRatio) { // 位图高短
                scaleX = 1.0f * tarH / bmpH;
            } else { // 位图宽短
                scaleX = 1.0f * tarW / bmpW;
            }
            scaleY = scaleX;
        }
        // 完全在目标内部
        else if (imageScaleType == ImageScaleType.CENTER_INSIDE) {
            if (bmpRatio > tarRatio) { // 位图宽长
                scaleX = 1.0f * tarW / bmpW;
            } else { // 位图高高
                scaleX = 1.0f * tarH / bmpH;
            }
            scaleY = scaleX;
        }
        // 直接拉伸到xy
        else if (imageScaleType == ImageScaleType.FIT_XY) {
            scaleX = 1.0f * tarW / bmpW;
            scaleY = 1.0f * tarH / bmpH;
        }

        // 计算原图片偏移和原图片的裁剪宽高
        int offsetX = 0;
        int offsetY = 0;
        int samplingW = subsampledBitmap.getWidth(); // 原图采样宽
        int samplingH = subsampledBitmap.getHeight(); // 原图采样高

        // 居中裁剪
        if (imageScaleType == ImageScaleType.EXACTLY_CENTER_CROP) {
            if (bmpRatio > tarRatio) { // 位图高窄
                float scale2H = 1.0f * bmpH / tarH;
                if (rotation % 180 != 0) {
                    samplingH = (int) (scale2H * tarW);
                    offsetX = 0;
                    offsetY = (bmpW - samplingH) / 2;
                } else {
                    samplingW = (int) (scale2H * tarW);
                    offsetX = (bmpW - samplingW) / 2;
                    offsetY = 0;
                }
            } else { // 位图宽窄
                float scale2W = 1.0f * bmpW / tarW;
                if (rotation % 180 != 0) {
                    samplingW = (int) (scale2W * tarH);
                    offsetX = (bmpH - samplingW) / 2;
                    offsetY = 0;
                } else {
                    samplingH = (int) (scale2W * tarH);
                    offsetX = 0;
                    offsetY = (bmpH - samplingH) / 2;
                }
            }
        }

        Matrix matrix = new Matrix();

        if (rotation != 0) {
            matrix.postRotate(rotation);
        }

        if (scaleX != 1.0f || scaleY != 1.0f) {
            matrix.postScale(scaleX, scaleY);
        }

        if (flipHorizontal) {
            matrix.postScale(-1, 1);
        }

        // 边界修正，防止崩溃
        if (offsetX < 0) {
            offsetX = 0;
            Log.e("ImageDecoder", "createBitmap error：offsetX < 0");
        }
        if (offsetY < 0) {
            offsetY = 0;
            Log.e("ImageDecoder", "createBitmap error：offsetY < 0");
        }
        if (samplingW <= 0) {
            samplingW = subsampledBitmap.getWidth();
            Log.e("ImageDecoder", "createBitmap error：samplingW <= 0");
        }
        if (samplingH <= 0) {
            samplingH = subsampledBitmap.getHeight();
            Log.e("ImageDecoder", "createBitmap error：samplingH <= 0");
        }

        Bitmap result = Bitmap.createBitmap(subsampledBitmap,
                offsetX, offsetY, samplingW, samplingH, matrix, true);
        if (subsampledBitmap != result) {
            BitmapHelper.recycle(subsampledBitmap);
        }

        return result;
    }

    private boolean canDefineExifParams(String imageUri, String mimeType) {
        return "image/jpeg".equalsIgnoreCase(mimeType)
                && (ImageLoader.Scheme.ofUri(imageUri) == ImageLoader.Scheme.FILE);
    }

    /**
     * 获取本地jpg图片的旋转角度和翻转并叠加外部的旋转角度
     *
     * @param imageUri
     * @param decodeImageOptionsRotation
     * @return
     */
    private ExifInfo defineExifOritation(String imageUri, int decodeImageOptionsRotation) {
        int rotation = 0;
        boolean flipHorizontal = false;

        try {
            ExifInterface exifInterface = new ExifInterface(ImageLoader.Scheme.FILE.crop(imageUri));
            int exifOrientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    flipHorizontal = true;
                case ExifInterface.ORIENTATION_NORMAL:
                    rotation = 0;
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    flipHorizontal = true;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    flipHorizontal = true;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    flipHorizontal = true;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        rotation = rotation + decodeImageOptionsRotation;
        return new ExifInfo(rotation, flipHorizontal);
    }

    private static class ExifInfo {
        private final int mRotation;
        private final boolean mFlipHorizontal;

        public ExifInfo() {
            mRotation = 0;
            mFlipHorizontal = false;
        }

        public ExifInfo(int mRotation, boolean mFlipHorizontal) {
            this.mRotation = mRotation;
            this.mFlipHorizontal = mFlipHorizontal;
        }

        public int getRotation() {
            return mRotation;
        }

        public boolean isFlipHorizontal() {
            return mFlipHorizontal;
        }
    }

    /**
     * decode 图片的信息
     */
    public static class ImageDecodingInfo {
        private String mUri;
        private ImageSize mTargetSize;
        private DecodeImageOptions mDecodeImageOptions;

        public ImageDecodingInfo(String uri,
                                 ImageSize targetSize,
                                 DecodeImageOptions decodeImageOptions) {
            mUri = uri;
            mTargetSize = targetSize;
            mDecodeImageOptions = decodeImageOptions;
        }

        public String getUri() {
            return mUri;
        }

        public void setUri(String uri) {
            mUri = uri;
        }

        public ImageSize getTargetSize() {
            return mTargetSize;
        }

        public void setTargetSize(ImageSize targetSize) {
            mTargetSize = targetSize;
        }

        public DecodeImageOptions getDecodeImageOptions() {
            return mDecodeImageOptions;
        }

        public void setDecodeImageOptions(DecodeImageOptions decodeImageOptions) {
            mDecodeImageOptions = decodeImageOptions;
        }
    }

    /**
     * 图片的信息
     */
    protected static class ImageFileInfo {
        private final ImageSize mImageSize;
        private final ExifInfo mExif;
        private final boolean isJpg;

        protected ImageFileInfo(ImageSize imageSize, ExifInfo exif, boolean isJpg) {
            this.mImageSize = imageSize;
            this.mExif = exif;
            this.isJpg = isJpg;
        }

        public ImageSize getImageSize() {
            return mImageSize;
        }

        public ExifInfo getExif() {
            return mExif;
        }

        public boolean isJpg() {
            return isJpg;
        }
    }
}
