package com.iekie.pluginloader.download;

/**
 * Created by longteng on 2017/7/27.
 */

public enum LoadState {
    RUNNING(0),WAITING(1),ERROR(2);

    private int code;

    private LoadState(int code){
        this.code = code;
    }

    public int value(){
        return code;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
