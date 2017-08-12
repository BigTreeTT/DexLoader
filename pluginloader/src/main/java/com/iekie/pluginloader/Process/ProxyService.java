package com.iekie.pluginloader.Process;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.iekie.pluginloader.hook.ServiceManager;

public class ProxyService extends Service {

    private static final String TAG = "plugin_loader";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStart() called with " + "intent = [" + intent + "], startId = [" + startId + "]");

        // 分发Service
        ServiceManager.getInstance().onStartCommand(intent, flags,startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }
}
