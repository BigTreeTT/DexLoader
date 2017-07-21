package com.iekie.font;

import android.app.Application;
import android.util.Log;

import com.iekie.font.crash.CrashHandler;
import com.iekie.font.utils.ProcessUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("lt_debug",
                "current process = "+ ProcessUtil.currentProcessName(this) +"  pid = "+ProcessUtil.currentPid());
        CrashHandler.getInstance().init(this);
    }
}
