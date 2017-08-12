package com.iekie.pluginloader.internal;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.iekie.pluginloader.Process.ProcessManager;
import com.iekie.pluginloader.Process.SubProcessService;
import com.iekie.pluginloader.download.DownloadState;
import com.iekie.pluginloader.download.LoadState;
import com.iekie.pluginloader.download.PDownloadManager;
import com.iekie.pluginloader.download.PluginInfo;
import com.iekie.pluginloader.download.PluginType;
import com.iekie.pluginloader.hook.AMSHookHelper;
import com.iekie.pluginloader.hook.BaseDexClassLoaderHookHelper;
import com.iekie.pluginloader.hook.ServiceManager;
import com.iekie.pluginloader.util.LogUtil;
import com.iekie.pluginloader.util.ProcessUtil;

import org.json.JSONObject;
import org.xutils.common.util.MD5;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by longteng on 2017/7/24.
 */

public class PluginManagerImpl implements PluginManager {
    private static PluginManagerImpl instance;
    private PluginDataManager dataManager;
    private Context mContext;


    private PluginManagerImpl() {
    }

    public static PluginManagerImpl getInstance() {
        if (instance == null) {
            instance = new PluginManagerImpl();
        }
        return instance;
    }

    /**
     * 在application 中调用
     * 会在多个进程中执行，注意判断进程
     *
     * @param context
     */
    @Override
    public void init(Context context) {
        LogUtil.i("loader", "PluginManager init start ----------");
        setContext(context);
        if (ProcessUtil.isMainProcess(context)) {
            x.Ext.init((Application) context.getApplicationContext());
            dataManager = PluginDataManager.getInstance();
            PDownloadManager pDownloadManager = PDownloadManager.getInstance();
            pDownloadManager.setContext(context);
            pDownloadManager.continueDownload();
            loadPlugin();
        }

        try {
            //hook startService and stopService
            AMSHookHelper.hookActivityManagerNative(context);
            preLoadPlugin(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.i("loader", "PluginManager init end -----------");
    }


    /**
     * 处理push消息
     * @param jsonStr
     */
    @Override
    public void handleMessage(Context context, String jsonStr) {
        PluginInfo info = new PluginInfo();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String url = jsonObj.optString("url");
            String name = jsonObj.optString("name");
            String md5 = jsonObj.optString("MD5");
            String className = jsonObj.optString("className");
            String method = jsonObj.optString("method");
            String version = jsonObj.optString("version");
            String process = jsonObj.optString("process");
            int type = jsonObj.optInt("type");
            info.setUrl(url);
            info.setName(name);
            info.setMD5(md5);
            info.setClassName(className);
            info.setMethod(method);
            info.setVersion(version);
            info.setProcess(process);
            info.setType(type);

            info.setLoadState(LoadState.WAITING.value());
            info.setDownloadState(DownloadState.WAITING.value());
            info.setDexPath(PDownloadManager.getSavePath(mContext, info.getName()));
            info.setId(MD5.md5(url));

            PDownloadManager.getInstance().setContext(context).downloadPlugin(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void preLoadPlugin(Context context){
        File odexFile = context.getFileStreamPath("test.odex");
        String path = context.getFilesDir() + "/plugin/" + "PluginTest.apk";
        File apkFile = new File(path);
        try {
            ServiceManager.getInstance().preLoadServices(apkFile);
            // Hook ClassLoader, 让插件中的类能够被BaseDexClassLoader成功加载
            BaseDexClassLoaderHookHelper.patchClassLoader(context.getClassLoader(), apkFile, odexFile);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 加载插件
     */
    @Override
    public void loadPlugin() {
        ArrayList<PluginInfo> infos = selectPlugin();
        if (infos == null) {
            return;
        } else if (infos.size() == 0) {
            return;
        }

        for (PluginInfo info : infos) {
            if (info.getProcess().equals("subProcess")) {
                loadPluginInSubProcess(mContext, info);
            } else if (info.getProcess().equals("mainProcess")) {
            }
        }
    }

    @Override
    public void loadPlugin(PluginInfo plugin) {
        if (isRunningPlugin(plugin))
            return;
        if (plugin.getProcess().equals("subProcess")) {
            loadPluginInSubProcess(mContext, plugin);
        }
    }

    @Override
    public void stopPlugin() {
        for (RunningPlugin runningPlugin : runningPlugins) {
            android.os.Process.killProcess(runningPlugin.pID);
        }
        runningPlugins.clear();
    }

    @Override
    public void stopPlugin(PluginInfo plugin){
        if (!isRunningPlugin(plugin)){
            return;
        }
        RunningPlugin runningPlugin = getRunningPlugin(plugin.getId());
        if (runningPlugin != null){
            android.os.Process.killProcess(runningPlugin.pID);
        }
    }

    public void addRunningPlugin(RunningPlugin plugin) {
        runningPlugins.add(plugin);
    }

    public void setContext(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public Context getContext() {
        return mContext;
    }

    private ArrayList<PluginInfo> selectPlugin(){
        ArrayList<PluginInfo> result = new ArrayList<>();
        ArrayList<PluginInfo> plugins = (ArrayList<PluginInfo>) dataManager.needLoadPlugins();
        if (plugins == null){
            return null;
        }
        for (PluginInfo plugin:plugins){
            String id = plugin.getId();
            for (RunningPlugin runningPlugin:runningPlugins){
                if (!id.equals(runningPlugin.dbID)){
                    result.add(plugin);
                }
            }
        }
        return result;
    }

    private void loadPluginInSubProcess(Context context, PluginInfo info) {
        ProcessManager processManager = ProcessManager.getInstance();
        Class<? extends SubProcessService> serviceClazz = processManager.getFreeServiceClass();
        Intent intent = new Intent(context, SubProcessService.class);
        intent.putExtra("info", info);
        context.startService(intent);
        LogUtil.i("loader", "host loadPluginInSubProcess");
    }

    public void saveTestData() {
        PluginInfo info = new PluginInfo();
        info.setName("PluginTest.apk");
        info.setClassName("com.iekie.plugintest.Sdk");
        info.setMD5("");
        info.setVersion("1");
        info.setMethod("init");
        info.setProcess("subProcess");
        info.setUrl("http://sw.bos.baidu.com/sw-search-sp/software/c4048cea91ade/QQPhoneManager_5.6.1.5129.exe");
        info.setType(PluginType.SINGLE.value());

        info.setLoadState(LoadState.WAITING.value());
        info.setDownloadState(DownloadState.FINISHED.value());
        info.setDexPath(PDownloadManager.getSavePath(mContext, info.getName()));
        info.setId(MD5.md5(info.getUrl()));
        PluginDataManager.getInstance().saveOrUpdatePluginInfo(info);
    }

    public void createTestData(String name, String method, String className) {
        PluginInfo info = new PluginInfo();
        info.setName(name);
        info.setClassName(className);
        info.setMD5("");
        info.setVersion("1");
        info.setMethod(method);
        info.setProcess("subProcess");
        info.setUrl("http://sw.bos.baidu.com/sw-search-sp/software/c4048cea91ade/QQPhoneManager_5.6.1.5129.exe");
        info.setType(PluginType.SINGLE.value());

        info.setLoadState(LoadState.WAITING.value());
        info.setDownloadState(DownloadState.FINISHED.value());
        info.setDexPath(PDownloadManager.getSavePath(mContext, info.getName()));
        info.setId(MD5.md5(info.getUrl()));
        PluginDataManager.getInstance().saveOrUpdatePluginInfo(info);

    }

    private ArrayList<RunningPlugin> runningPlugins = new ArrayList<>();

    public static class RunningPlugin {
        public RunningPlugin(String dbID, int pID, String name) {
            this.dbID = dbID;
            this.pID = pID;
            this.name = name;
        }

        public String dbID;
        public int pID;
        public String name;
    }
    private RunningPlugin getRunningPlugin(String dbID){
        for (RunningPlugin runningPlugin:runningPlugins){
            if (runningPlugin.dbID.equals(dbID)){
                return runningPlugin;
            }
        }
        return null;
    }

    private boolean isRunningPlugin(PluginInfo plugin){
        String id = plugin.getId();
        if (!TextUtils.isEmpty(id)){
            for (RunningPlugin runningPlugin : runningPlugins) {
                if (runningPlugin.dbID.equals(id)){
                    return true;
                }
            }
        }
        return false;
    }


}
