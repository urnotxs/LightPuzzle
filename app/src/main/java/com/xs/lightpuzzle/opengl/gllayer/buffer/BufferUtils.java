package com.xs.lightpuzzle.opengl.gllayer.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Lin on 2017/5/15.
 */

public class BufferUtils {

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
