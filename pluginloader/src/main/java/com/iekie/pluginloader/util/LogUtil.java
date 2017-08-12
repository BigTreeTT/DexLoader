package com.iekie.pluginloader.util;

import android.util.Log;

/**
 * Created by longteng on 2017/7/27.
 */

public class LogUtil {
    public static boolean DEBUG = true;
    private static String TAG = "plugin_";
    public static void d(String tag,String message){
        if (DEBUG){
            Log.d(TAG+tag,message);
        }
    }
    public static void i(String tag,String message){
        if (DEBUG){
            Log.i(TAG+tag,message);
        }
    }
    public static void w(String tag,String message){
        if (DEBUG){
            Log.w(TAG+tag,message);
        }
    }
    public static void e(String tag,String message){
        if (DEBUG){
            Log.e(TAG+tag,message);
        }
    }

}
