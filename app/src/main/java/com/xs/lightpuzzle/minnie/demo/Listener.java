package com.xs.lightpuzzle.minnie.demo;

/**
 * Created by xs on 2018/11/13.
 */

public interface Listener<T> {

    void onSuccess(T t);

    void onFailure(int code);
}
