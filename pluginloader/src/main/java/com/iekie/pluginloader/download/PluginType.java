package com.iekie.pluginloader.download;

/**
 * Created by longteng on 2017/7/27.
 */

public enum PluginType {
    SINGLE(0),REUSE(1);

    private int code;

    private PluginType(int code){
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
