package com.xs.lightpuzzle.puzzle.msgevent;

/**
 * Created by xs on 2018/5/22.
 */

public class LabelBarMsgEvent {

    private int msgCode;

    public LabelBarMsgEvent(int msgCode) {
        this.msgCode = msgCode;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }
}
