package com.iekie.pluginloader.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.util.List;

/**
 * Created by longteng on 2017/7/21.
 */

public class ProcessUtil {
    public static int currentPid(){
        return Process.myPid();
    }

    public static String currentProcessName(Context context){
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos =  am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo:processInfos){
            if (currentPid() == processInfo.pid){
                processName = processInfo.processName;
                break;
            }
        }
        return processName;
    }
    public static boolean isMainProcess(Context context){
        return context.getPackageName().equals(currentProcessName(context));
    }


}
