package com.iekie.pluginloader.internal;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.iekie.pluginloader.download.PluginInfo;
import com.iekie.pluginloader.util.LogUtil;
import com.iekie.pluginloader.util.ProcessUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by longteng on 2017/7/13.
 */

public class ClassDynamicLoader {

    /**
     * 插件运行入口
     * @param context
     * @param pluginInfo
     */
    public static void loadPlugin(Context context, PluginInfo pluginInfo) {
        String dexPath = pluginInfo.getDexPath();
        String optimizedPath = context.getFilesDir() + "/optimizedPath/"+pluginInfo.getName();
        File file = new File(optimizedPath);
        if (!file.exists()){
            file.mkdirs();
        }
        LogUtil.i("loader","ClassDynamicLoader start loadPlugin");
        LogUtil.i("loader","dexPath " +dexPath);

        DexClassLoader classLoader = new DexClassLoader(dexPath, optimizedPath, null, context.getClassLoader());
        Class loadClass = null;
        Object instance = null;
        try {
            loadClass = classLoader.loadClass(pluginInfo.getClassName());
            Constructor constructor = loadClass.getConstructor(new Class[]{});
            Object[] params = new Object[]{};
            instance = constructor.newInstance(params);
            Method method = loadClass.getDeclaredMethod("init",new Class[]{Context.class,String.class});
            method.setAccessible(true);
            String jsonStr = "";
            method.invoke(instance,context,jsonStr);
            loadSuccess(context,pluginInfo);
        } catch (Exception e) {
            loadFail(context,pluginInfo);
            e.printStackTrace();
        }finally {
            PluginDataManager.getInstance().saveOrUpdatePluginInfo(pluginInfo);
        }

    }

    public static void loadSuccess(Context context,PluginInfo pluginInfo){
        Intent intent = new Intent();
        intent.setAction("me.plugin.broadcast.load.success");
        intent.putExtra("pID", Process.myPid());
        intent.putExtra("dbID",pluginInfo.getId());
        intent.putExtra("name",pluginInfo.getName());
        intent.putExtra("processName", ProcessUtil.currentProcessName(context));
        context.sendBroadcast(intent);
    }
    public static void loadFail(Context context,PluginInfo pluginInfo){
        Intent intent = new Intent();
        intent.setAction("me.plugin.broadcast.load.fail");
        intent.putExtra("dbID",pluginInfo.getId());
        intent.putExtra("name",pluginInfo.getName());
        context.sendBroadcast(intent);
    }
}
