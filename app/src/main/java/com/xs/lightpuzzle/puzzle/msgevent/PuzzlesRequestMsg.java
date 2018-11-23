package com.xs.lightpuzzle.puzzle.msgevent;

/**
 * Created by xs on 2018/4/25.
 */

public class PuzzlesRequestMsg {

    private String msgName;

    private Object object;
    //事件， move， up， down
    private int action;

    public PuzzlesRequestMsg(String msgName, int action) {
        this.msgName = msgName;
        this.action = action;
    }

    public PuzzlesRequestMsg(String msgName, int action, Object object) {
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
