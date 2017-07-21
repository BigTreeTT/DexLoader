package com.iekie.font.loader;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by longteng on 2017/7/13.
 */

public class ClassDynamicLoader {
    private static ClassDynamicLoader instance;

    private ClassDynamicLoader() {

    }

    public static ClassDynamicLoader getInstance() {
        if (instance == null) {
            instance = new ClassDynamicLoader();
        }
        return instance;
    }

    public void func(Context context, String json) {
        LoadInfo loadInfo = new LoadInfo(json);
        String optimizedDirectory = context.getDir("dex", 0).getAbsolutePath();
        String jarPath;
        String className;
        DexClassLoader classLoader = new DexClassLoader(loadInfo.mDexPath, optimizedDirectory, null, context.getClassLoader());
        try {
            // 通过反射机制调用，  类名为className
            Class mLoadClass = classLoader.loadClass(loadInfo.mClassName);
            Constructor constructor = mLoadClass.getConstructor(new Class[]{});
            Object startObject = constructor.newInstance(new Object[]{});

            // 获取mMethod方法
            Class[] params = new Class[3];
            params[0] = Context.class;
            params[1] = String.class;
            params[2] = String.class;
            Method startMethod = mLoadClass.getMethod(loadInfo.mMethod, params);
            startMethod.setAccessible(true);
//            Object content = startMethod.invoke(startObject, context,loadInfo.mDexPath,loadInfo.sParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadClass(Context context) {
        String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lovelyfonts/apk/LibApk.apk";
        String optimizedPath = context.getFilesDir() + "/optimizedPath";
        File file = new File(optimizedPath);
        if (!file.exists()){
            file.mkdir();
        }
        Log.i("lt_debug", "dexPath = " + dexPath);

        DexClassLoader classLoader = new DexClassLoader(dexPath, optimizedPath, null, context.getClassLoader());
        Class loadClass = null;
        Object instance = null;
        try {
            loadClass = classLoader.loadClass("com.iekie.libapk.utils.CoolUtil");
            Constructor constructor = loadClass.getConstructor(new Class[]{String.class});
            Object[] params = new Object[]{"load"};
            instance = constructor.newInstance(params);
            Method apendString = loadClass.getDeclaredMethod("apendString",new Class[]{String.class,String.class});
            Method toast1 = loadClass.getDeclaredMethod("toast",new Class[]{Context.class,String.class});
            //Method init = loadClass.getDeclaredMethod("init",new Class[]{Context.class});

            String result = (String) apendString.invoke(instance,"hello!"," apend string test.");
            Toast.makeText(context,result!=null?result:"result == null",Toast.LENGTH_SHORT).show();
            toast1.invoke(instance,context,"hi");
            //init.invoke(instance,context);


        } catch (Exception e) {
            Log.e("lt_debug","loadClass error:"+e.getMessage());
            e.printStackTrace();
        }


    }
}
