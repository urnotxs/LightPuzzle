package com.xs.lightpuzzle.opengl.gllayer.util;

import android.graphics.Rect;
import android.graphics.RectF;

import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/9/18.
 */

public class RendererHelper {

    /**
     * 截帧
     *
     * @param context
     * @param bmpW
     * @param bmpH
     * @param textureId
     * @return
     */
//    public static Bitmap shot(Context context, int bmpW, int bmpH, int textureId) {
//        OffscreenBuffer buffer = new OffscreenBuffer(bmpW, bmpH);
//        buffer.bind();
//        NoneFilter noneFilter = new NoneFilter(context);
//        noneFilter.draw(textureId, GlUtil.IDENTITY_MATRIX, GlUtil.IDENTITY_MATRIX);
//        noneFilter.release();
//
//        ByteBuffer buf = ByteBuffer.allocateDirect(buffer.getWidth() * buffer.getHeight() * 4);
//        buf.order(ByteOrder.LITTLE_ENDIAN);
//        GLES20.glReadPixels(0, 0, buffer.getWidth(), buffer.getHeight(), GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buf);
//        buf.rewind();
//        Bitmap bmp = Bitmap.createBitmap(buffer.getWidth(), buffer.getHeight(), Bitmap.Config.ARGB_8888);
//        bmp.copyPixelsFromBuffer(buf);
//        buf.clear();
//        buffer.unbind();
//        Matrix matrix = new Matrix();
//        matrix.postScale(1, -1);   //镜像垂直翻转
//
//        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//    }

    /**
     * 根据屏幕坐标，计算OpenGL坐标系的顶点坐标
     * 和满铺的纹理坐标
     * 生成 PuzzleVideoBuffer
     *
     * @param rect
     * @return
     */
    public static PuzzleVideoBuffer calculateScreenRectBuffer(RectF rect) {
        float left = 2.0f * rect.left - 1;
        float right = 2.0f * rect.right - 1;
        float top = -(2.0f * rect.top - 1);
        float bottom = -(2.0f * rect.bottom - 1);
        float[] vertexPots = {left, bottom, left, top, right, bottom, right, top};

        float texturePots[] = {
                0.0f, 1.0f,     // 0 bottom left
                0.0f, 0.0f,     // 1 top left
                1.0f, 1.0f,     // 2 bottom right
                1.0f, 0.0f      // 3 top right
        };
        return new PuzzleVideoBuffer(vertexPots, texturePots);
    }

    /**
     * 单元Buffer , 满铺的纹理坐标和顶点坐标
     *
     * @return
     */
    public static PuzzleVideoBuffer calculateUnitBuffer() {
        float vertexPots[] = {
                -1.0f, -1.0f,     // 0 bottom left
                -1.0f, 1.0f,     // 1 top left
                1.0f, -1.0f,     // 2 bottom right
                1.0f, 1.0f      // 3 top right
        };
        float texturePots[] = {
                0.0f, 1.0f,     // 0 bottom left
                0.0f, 0.0f,     // 1 top left
                1.0f, 1.0f,     // 2 bottom right
                1.0f, 0.0f      // 3 top right
        };
        return new PuzzleVideoBuffer(vertexPots, texturePots);
    }

    public static PuzzleVideoBuffer calculateFaceDetectionBuffer() {
        float vertexPots[] = {
                -1.0f, -1.0f,     // 0 bottom left
                1.0f, -1.0f,     // 2 bottom right
                -1.0f, 1.0f,     // 1 top left
                1.0f, 1.0f      // 3 top right
        };
        float texturePots[] = {
                0.0f, 0.0f,     // 0 bottom left
                1.0f, 0.0f,     // 1 bottom right
                0.0f, 1.0f,     // 2 top left
                1.0f, 1.0f      // 3 top right
        };

        return new PuzzleVideoBuffer(vertexPots, texturePots);
    }

    // 预览页不含误差，纹理计算
    public static float[] calculateCenterTexturePosts(RectF rect, int srcVideoWidth, int srcVideoHeight, int rotation,
                                                      float viewWidth, float viewHeight) {

        RectF childRect = new RectF(rect.left * viewWidth,
                rect.top * viewHeight,
                rect.right * viewWidth,
                rect.bottom * viewHeight);

        return calculateCenterTexturePosts(srcVideoWidth, srcVideoHeight, rotation, childRect);
    }

    // 镜头拍摄时含误差，纹理计算
    public static float[] calculateCenterTexturePosts(Rect rect, int srcVideoWidth, int srcVideoHeight, int rotation) {

        RectF rectF = new RectF(rect.left, rect.top, rect.right, rect.bottom);

        return calculateCenterTexturePosts(srcVideoWidth, srcVideoHeight, rotation, rectF);
    }
    /**
     * 根据矩形比例和图片比例 ，计算居中的纹理坐标
     *
     * @param srcVideoWidth
     * @param srcVideoHeight
     * @param rotation
     * @param childRect
     */
    public static float[] calculateCenterTexturePosts(int srcVideoWidth, int srcVideoHeight, int rotation, RectF childRect) {
        float videoRatio = srcVideoWidth * 1.0f / srcVideoHeight;
        if (rotation % 180 != 0) {
            videoRatio = 1 / videoRatio;
        }

        float videoWidth;
        float videoHeight;
        float rectRatio = childRect.width() * 1.0f / childRect.height();

        if (rectRatio > videoRatio) {
            // 齐宽
            videoHeight = childRect.width() * 1.0f / videoRatio;
            float off = ((videoHeight - childRect.height()) / 2.0f) / videoHeight;

            // 左下角纹理坐标 ( 0 , 1-0 )
            float x1 = 0;
            float y1 = 1 - off;

            // 左上角纹理坐标 ( 0 , 1-1 )
            float x2 = 0;
            float y2 = 1 - (1 - off);

            // 右下角纹理坐标 ( 1 , 1-0 )
            float x3 = 1;
            float y3 = 1 - off;

            // 右上角纹理坐标 ( 1 , 1-1 )
            float x4 = 1;
            float y4 = 1 - (1 - off);

            float[] texturePots = {x1, y1, x2, y2, x3, y3, x4, y4};
            return texturePots;
        } else {
            // 齐高
            videoWidth = childRect.height() * videoRatio;
            float off = ((videoWidth - childRect.width()) / 2.0f) / videoWidth;

            // 左下角纹理坐标 ( 0 , 1-0 )
            float x1 = off;
            float y1 = 1;

            // 左上角纹理坐标 ( 0 , 1-1 )
            float x2 = off;
            float y2 = 0;

            // 右下角纹理坐标 ( 1 , 1-0 )
            float x3 = 1 - off;
            float y3 = 1;

            // 右上角纹理坐标 ( 1 , 1-1 )
            float x4 = 1 - off;
            float y4 = 0;

            float[] texturePots = {x1, y1, x2, y2, x3, y3, x4, y4};
            return texturePots;
        }
    }

    /**
     * 计算模板每个洞所对应的顶点坐标
     *
     * @param templateRectFs
     * @return
     */
    public static ArrayList<float[]> calculateVertexPotsFromTemplate(List<RectF> templateRectFs) {
        ArrayList<float[]> vertexPotsArray = new ArrayList<>();
        for (RectF rectF : templateRectFs) {
            vertexPotsArray.add(calculateVertexPotsFromRect(rectF));
        }
        return vertexPotsArray;
    }

    public static float[] calculateVertexPotsFromRect(RectF rectF) {
        float[] vertexPots = new float[8];
        float left = 2.0f * rectF.left - 1;
        float right = 2.0f * rectF.right - 1;
        float top = -(2.0f * rectF.top - 1);
        float bottom = -(2.0f * rectF.bottom - 1);

        // 左下角顶点坐标
        vertexPots[0] = left;
        vertexPots[1] = bottom;
        // 左上角顶点坐标
        vertexPots[2] = left;
        vertexPots[3] = top;
        // 右下角顶点坐标
        vertexPots[4] = right;
        vertexPots[5] = bottom;
        // 右上角顶点坐标
        vertexPots[6] = right;
        vertexPots[7] = top;

        return vertexPots;
    }
}
