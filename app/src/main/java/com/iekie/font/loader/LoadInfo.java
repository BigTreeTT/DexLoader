package com.iekie.font.loader;

/**
 * Created by longteng on 2017/7/19.
 */

public class LoadInfo {
    public String mDexPath;
    public String mClassName;
    public String mMethod;

    public LoadInfo(String json) {

    }

    public String getmDexPath() {
        return mDexPath;
    }

    public void setmDexPath(String mDexPath) {
        this.mDexPath = mDexPath;
    }

    public String getmClassName() {
        return mClassName;
    }

    public void setmClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public String getmMethod() {
        return mMethod;
    }

    public void setmMethod(String mMethod) {
        this.mMethod = mMethod;
    }
}
