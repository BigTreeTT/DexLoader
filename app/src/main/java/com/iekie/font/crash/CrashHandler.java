package com.iekie.font.crash;

import android.content.Context;
import android.util.Log;

import com.iekie.font.utils.ProcessUtil;

/**
 * Created by longteng on 2017/7/21.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private static CrashHandler instance = new CrashHandler();

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }


    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            Log.i("lt_debug", "uncaught exception happened; current process = " +
                    ProcessUtil.currentProcessName(mContext) + "  pid = " + ProcessUtil.currentPid());
            mDefaultHandler.uncaughtException(thread, throwable);
        }
    }

    private boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        return true;
    }

}
