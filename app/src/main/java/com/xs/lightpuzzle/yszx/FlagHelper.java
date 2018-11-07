package com.xs.lightpuzzle.yszx;

/**
 * Created by xs on 2018/11/6.
 */

public class FlagHelper {
    public static boolean isOn(int FLAG, int flag) {
        return (FLAG & flag) == FLAG;
    }

    public static boolean isOff(int FLAG, int flag) {
        return !isOn(FLAG, flag);
    }

    public static int toggle(int FLAG, int flag) {
        if (isOn(FLAG, flag)) {
            return off(FLAG, flag);
        } else {
            return on(FLAG, flag);
        }
    }

    public static int toggle(int FLAG, int flag, boolean isOn) {
        if (isOn) {
            if (isOff(FLAG, flag)) {
                return on(FLAG, flag);
            } else {
                return flag;
            }
        } else { // off
            if (isOn(FLAG, flag)) {
                return off(FLAG, flag);
            } else {
                return flag;
            }
        }
    }

    public static int on(int FLAG, int flag) {
        if (isOff(FLAG, flag)) {
            flag = flag | FLAG;
        }
        return flag;
    }

    public static int off(int FLAG, int flag) {
        if (isOn(FLAG, flag)) {
            flag = flag & (~FLAG);
        }
        return flag;
    }
}
