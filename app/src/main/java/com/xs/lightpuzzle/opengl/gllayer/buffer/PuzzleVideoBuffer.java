package com.xs.lightpuzzle.opengl.gllayer.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by xs on 2017/6/12.
 * 顶点坐标，纹理坐标 Buffer
 */

public class PuzzleVideoBuffer {
    private static final int SIZEOF_FLOAT = 4;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexCoordBuffer;

    private int mVertexCount;
    private int mCoordsPerVertex;
    private int mVertexStride;
    private int mTexCoordStride;

    private float[] mVertexCoords ;
    private float[] mTextureCoords ;

    public PuzzleVideoBuffer(float[] vertexCoors , float[] textureCoors, float[] filterTexturCoors) {
        mVertexCoords = vertexCoors ;
        mTextureCoords = textureCoors ;

        mVertexBuffer = getFloatBuffer(mVertexCoords);
        mTexCoordBuffer = getFloatBuffer(mTextureCoords);

        mCoordsPerVertex = 2;
        mVertexStride = mCoordsPerVertex * SIZEOF_FLOAT;
        mVertexCount = vertexCoors.length / mCoordsPerVertex;
        mTexCoordStride = 2 * SIZEOF_FLOAT;
    }

    public PuzzleVideoBuffer(float[] vertexCoors , float[] textureCoors) {
        mVertexCoords = vertexCoors ;
        mTextureCoords = textureCoors ;

        mVertexBuffer = getFloatBuffer(mVertexCoords);
        mTexCoordBuffer = getFloatBuffer(mTextureCoords);

        mCoordsPerVertex = 2;
        mVertexStride = mCoordsPerVertex * SIZEOF_FLOAT;
        mVertexCount = vertexCoors.length / mCoordsPerVertex;
        mTexCoordStride = 2 * SIZEOF_FLOAT;
    }

    /**
     * Returns the array of vertices.
     * <p>
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    /**
     * Returns the array of texture coordinates.
     * <p>
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    public FloatBuffer getTexCoordBuffer() {
        return mTexCoordBuffer;
    }


    /**
     * Returns the number of vertices stored in the vertex array.
     */
    public int getVertexCount() {
        return mVertexCount;
    }

    /**
     * Returns the width, in bytes, of the data for each vertex.
     */
    public int getVertexStride() {
        return mVertexStride;
    }

    /**
     * Returns the width, in bytes, of the data for each texture coordinate.
     */
    public int getTexCoordStride() {
        return mTexCoordStride;
    }

    /**
     * Returns the number of position coordinates per vertex.  This will be 2 or 3.
     */
    public int getCoordsPerVertex() {
        return mCoordsPerVertex;
    }

    public void clearBuffer(){
        if (mVertexBuffer != null){
            mVertexBuffer.clear();
        }
        if (mTexCoordBuffer != null){
            mTexCoordBuffer.clear();
        }

        mVertexCoords = null;
        mTextureCoords = null;
    }

    public static FloatBuffer getFloatBuffer(float[] vertex){
        FloatBuffer floatBuffer = ByteBuffer
                .allocateDirect(vertex.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static ShortBuffer getShortBuffer(short[] vertex){
        ShortBuffer ShortBuffer = ByteBuffer.allocateDirect(vertex.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(vertex);
        ShortBuffer.position(0);
        return ShortBuffer;
    }
}
