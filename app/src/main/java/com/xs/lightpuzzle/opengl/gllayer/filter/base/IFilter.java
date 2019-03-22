package com.xs.lightpuzzle.opengl.gllayer.filter.base;

import com.xs.lightpuzzle.opengl.gllayer.buffer.PuzzleVideoBuffer;

public interface IFilter {
    int getTextureTarget();

    void setRenderScale(float renderScale);

    void setTextureSize(int width, int height);

    void onDraw(float[] mvpMatrix, float[] texMatrix, int textureId, PuzzleVideoBuffer buffer);

    void releaseProgram();
}
