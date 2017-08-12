package com.iekie.pluginloader.download;

import android.content.Context;

import com.iekie.pluginloader.internal.PluginDataManager;
import com.iekie.pluginloader.util.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by longteng on 2017/7/24.
 */

public class PDownloadManager {
    private static PDownloadManager instance;
    private Context context;

    private PDownloadManager() {
    }

    public static PDownloadManager getInstance() {
        if (instance == null) {
            instance = new PDownloadManager();
        }
        return instance;
    }

    public PDownloadManager setContext(Context context){
        this.context = context.getApplicationContext();
        return instance;
    }

    /**
     * 下载插件
     *
     * @param info
     */
    public void downloadPlugin(PluginInfo info) {
        RequestParams params = new RequestParams(info.getUrl());
        params.setAutoRename(true);
        params.setSaveFilePath(info.getDexPath());
        params.setCancelFast(true);

        DownloadCallBack callBack = new DownloadCallBack(info);
        LogUtil.i("download","start download plugin. Plugin name:"+info.getName());
        x.http().get(params, callBack);

    }

    /**
     * 检查数据库，继续没有下载完成的插件
     */
    public void continueDownload() {
        List<PluginInfo> infos = PluginDataManager.getInstance().selectAllPluginInfos();
        if (infos == null){
            return;
        }
        for (PluginInfo info:infos){
            int state = info.getDownloadState();
            if (state != DownloadState.FINISHED.value()){
                downloadPlugin(info);
            }
        }
    }

    public static String getSavePath(Context context, String name) {
        String dir = context.getFilesDir().getAbsolutePath()+"/plugin";
//        String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/plugin";
        String savePath = dir + "/" + name;
        return savePath;
    }


    public  class DownloadCallBack implements Callback.CommonCallback<File> {
        private PluginInfo info = null;

        public DownloadCallBack(PluginInfo info){
            if (info == null){
                throw new NullPointerException("plugin info must be not null!");
            }
            this.info = info;
        }


        @Override
        public void onSuccess(File result) {
            info.setLoadState(DownloadState.FINISHED.value());
            LogUtil.i("download","onSuccess");

//            // 发送广播，通知应用下载完成
//            Intent intent = new Intent();
//            intent.setAction("me.plugin.broadcast.load.download_success");
//            intent.putExtra("id",info.getId());
//            context.sendBroadcast(intent);

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            info.setLoadState(DownloadState.ERROR.value());
            LogUtil.i("download","onError "+ex.getMessage());
        }

        @Override
        public void onCancelled(CancelledException cex) {
            info.setLoadState(DownloadState.STOPPED.value());
            LogUtil.i("download","onCancelled");
        }

        @Override
        public void onFinished() {
            PluginDataManager.getInstance().saveOrUpdatePluginInfo(info);
            LogUtil.i("download","onFinished");
            PluginDataManager.getInstance().logAllInfos();
//            PluginManagerImpl.getInstance().stopPlugin();
        }
    }

}
