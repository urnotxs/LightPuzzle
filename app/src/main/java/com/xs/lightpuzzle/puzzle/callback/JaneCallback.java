package com.xs.lightpuzzle.puzzle.callback;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by xs on 2018/11/12.
 */
public interface JaneCallback<T> extends Serializable {

    void onSuccess(T t, Bundle data);

    void onFailure(T t, int errorCode, Bundle data);
}
