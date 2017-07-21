package com.iekie.font.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.iekie.font.aidl.ITest;

import java.util.ArrayList;

public class SubProcessService extends Service {
    public SubProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    ITest.Stub mBinder = new ITest.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {



            int myPid = Process.myPid();
            String processName = null;
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<ActivityManager.RunningAppProcessInfo> processInfos
                    = (ArrayList<ActivityManager.RunningAppProcessInfo>) am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo:processInfos){
                int pid = processInfo.pid;
                if (pid == myPid){
                    processName = processInfo.processName;
                }
            }
            Log.i("lt_debug","current process = "+processName+"  pid = "+myPid);
//            Intent intent = new Intent();
//            intent.setAction("me.plugin.action.example");
//            sendBroadcast(intent);


            return a + b;
        }
    };
}
