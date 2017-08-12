package com.iekie.pluginloader.internal;

import android.content.Context;

import com.iekie.pluginloader.download.PluginInfo;

/**
 * Created by longteng on 2017/7/27.
 */

public interface PluginManager {

    void init(Context context);
    /**
     * 处理push消息
     * @param jsonStr
     */
    void handleMessage(Context context,String jsonStr);

    void loadPlugin();

    void loadPlugin(PluginInfo plugin);

    void stopPlugin();

    void stopPlugin(PluginInfo plugin);

    Context getContext();

}
