package com.xs.lightpuzzle.yszx;

/**
 * Created by xs on 2018/11/6.
 */

public final class BitHelper {

    public static int toInteger(String value) {
        return Integer.parseInt(value, 2);
    }

    public static String fillingZero(int value) {
        String binaryStr = Integer.toBinaryString(value);
        StringBuilder zeroSb = new StringBuilder();
        for (int i = 0; i < 32 - binaryStr.length(); i++) {
            zeroSb.append("0");
        }
        return zeroSb + binaryStr;
    }
}
