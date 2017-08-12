package com.iekie.pluginloader.download;

/**
 * Created by longteng on 2017/7/27.
 */

public enum DownloadState {
    WAITING(0), STARTED(1), FINISHED(2), STOPPED(3), ERROR(4),LOCAL(5);

    private int code;

    private DownloadState(int code){
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
