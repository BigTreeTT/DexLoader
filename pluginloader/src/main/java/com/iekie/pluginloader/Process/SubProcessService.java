package com.iekie.pluginloader.Process;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.iekie.pluginloader.download.PluginInfo;
import com.iekie.pluginloader.internal.ClassDynamicLoader;

public class SubProcessService extends Service {
    public SubProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null){
            return super.onStartCommand(intent, flags, startId);
        }
        PluginInfo info = intent.getParcelableExtra("info");
        if (info == null){
            return super.onStartCommand(intent, flags, startId);
        }
        ClassDynamicLoader.loadPlugin(this,info);
        return super.onStartCommand(intent, flags, startId);
    }
}

