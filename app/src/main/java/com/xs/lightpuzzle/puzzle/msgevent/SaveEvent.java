package com.xs.lightpuzzle.puzzle.msgevent;

/**
 * Created by xs on 2019/1/10.
 */

public class SaveEvent {
    private final boolean isSuccess;
    private final String mPath;

    public SaveEvent(boolean isSuccess, String mPath) {
        this.isSuccess = isSuccess;
        this.mPath = mPath;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getPath() {
        return mPath;
    }
}
