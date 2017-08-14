package com.iekie.pluginloader.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iekie.pluginloader.util.LogUtil;

/**
 * Created by longteng on 2017/8/2.
 */

public class LoadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.i("loader","action "+action);
        if (action.equals("me.plugin.broadcast.load.success")){
            int pID = intent.getIntExtra("pID",0);
            String dbID = intent.getStringExtra("dbID");
            String name = intent.getStringExtra("name");
            String processName = intent.getStringExtra("name");
            PluginManagerImpl.RunningPlugin runningPlugin =
                    new PluginManagerImpl.RunningPlugin(dbID,pID,name,processName);
            PluginManagerImpl.getInstance().addRunningPlugin(runningPlugin);
        }else if (action.equals("me.plugin.broadcast.load.fail")){

        }else if (action.equals("me.plugin.broadcast.load.remove")){
//            String name = intent.getStringExtra("name");
//            PluginInfo info = PluginDataManager.getInstance().selectByName(name);
//            PluginManagerImpl.getInstance().stopPlugin();
//            PluginDataManager.getInstance().deletePluginFromFile(info);
//            PluginDataManager.getInstance().logAllInfos();
        }
    }
}
