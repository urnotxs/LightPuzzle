package com.xs.lightpuzzle.opengl.gllayer.util;

import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class CoordinateHelper {
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

    /**
     * 默认坐标系 Buffer， Y轴不做翻转
     *
     * @return
     */
    public static PuzzleVideoBuffer calculateDefaultBuffer() {
        float vertexPots[] = {
                -1.0f, -1.0f,     // 0 bottom left
                -1.0f, 1.0f,     // 1 top left
                1.0f, -1.0f,     // 2 bottom right
                1.0f, 1.0f      // 3 top right
        };
        float texturePots[] = {
                0.0f, 0.0f,     // 0 bottom left
                0.0f, 1.0f,     // 1 top left
                1.0f, 0.0f,     // 2 bottom right
                1.0f, 1.0f      // 3 top right
        };
        return new PuzzleVideoBuffer(vertexPots, texturePots);
    }
}
