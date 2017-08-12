package com.iekie.font;

import android.app.Application;
import android.util.Log;

import com.iekie.font.utils.ProcessUtil;
import com.iekie.pluginloader.internal.PluginManager;
import com.iekie.pluginloader.internal.PluginManagerImpl;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("lt_debug",
                "current process = "+ ProcessUtil.currentProcessName(this) +"  pid = "+ProcessUtil.currentPid());
        PluginManager pluginManager = PluginManagerImpl.getInstance();
        pluginManager.init(this);


    }
}
