package com.iekie.font.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class ProxyService extends Service {
    private Class loadClass;
    private Object instance;

    public ProxyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("lt_debug", "ProxyService onCreate");
        loadClass(this.getApplicationContext());
        Method onCreate = null;
        try {
            onCreate = loadClass.getDeclaredMethod("onCreate");
            onCreate.invoke(instance);
        } catch (Exception e) {
            Log.i("lt_debug","ProxyService onCreate error:"+e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("lt_debug", "ProxyService onStartCommand");
        Method onStartCommand = null;
        try {
            Class[] params = new Class[]{Intent.class,int.class,int.class};
            onStartCommand = loadClass.getDeclaredMethod("onStartCommand",params);
            onStartCommand.invoke(instance,intent,flags,startId);
        } catch (Exception e) {
            Log.i("lt_debug","ProxyService onStartCommand error:"+e.getMessage());
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("lt_debug", "ProxyService onDestroy");
        Method onDestroy = null;
        try {
            onDestroy = loadClass.getDeclaredMethod("onDestroy");
            onDestroy.invoke(instance);
        } catch (Exception e) {
            Log.i("lt_debug","ProxyService onDestroy error:"+e.getMessage());
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void loadClass(Context context) {
        String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lovelyfonts/apk/LibApk.apk";
        String optimizedPath = context.getFilesDir() + "/optimizedPath";
        File file = new File(optimizedPath);
        if (!file.exists()) {
            file.mkdir();
        }
        DexClassLoader classLoader = new DexClassLoader(dexPath, optimizedPath, null, context.getClassLoader());
        Class loadClass = null;
        Object instance = null;
        try {
            loadClass = classLoader.loadClass("com.iekie.libapk.LibService");
            Constructor constructor = loadClass.getConstructor(new Class[]{});
            Object[] params = new Object[]{};
            instance = constructor.newInstance(params);
            //Method init = loadClass.getDeclaredMethod("init",new Class[]{Context.class});

            this.instance = instance;
            this.loadClass = loadClass;

        } catch (Exception e) {
            Log.e("lt_debug", "loadClass error:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
