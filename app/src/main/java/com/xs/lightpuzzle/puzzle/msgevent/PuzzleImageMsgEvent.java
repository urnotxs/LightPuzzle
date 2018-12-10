package com.xs.lightpuzzle.puzzle.msgevent;

/**
 * Created by xs on 2018/11/29.
 */

public class PuzzleImageMsgEvent {
    private String msgName;

    private Object object;
    //事件， move， up， down
    private int action;

    public PuzzleImageMsgEvent(String msgName, int action) {
        this.msgName = msgName;
        this.action = action;
    }

    public PuzzleImageMsgEvent(String msgName, int action, Object object) {
        this.msgName = msgName;
        this.object = object;
        this.action = action;
    }

    public String getMsgName() {
        return msgName;
    }

    public int getAction() {
        return action;
    }

    public Object getObject() {
        return object;
    }

}
