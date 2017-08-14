package com.iekie.pluginloader.internal;

import android.text.TextUtils;
import android.util.Log;

import com.iekie.pluginloader.download.DownloadState;
import com.iekie.pluginloader.download.PluginInfo;
import com.iekie.pluginloader.util.LogUtil;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longteng on 2017/7/27.
 */

public class PluginDataManager {
    private static PluginDataManager instance;
    private DbManager dbManager;

    public static PluginDataManager getInstance() {
        if (instance == null) {
            instance = new PluginDataManager();
        }
        return instance;
    }

    private PluginDataManager() {
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        config.setDbName("plugin.db")
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                })
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("JAVA", "onTableCreated：" + table.getName());
                    }
                });
        dbManager = x.getDb(config);
    }

    public boolean saveOrUpdatePluginInfo(PluginInfo info) {

        deleteAll();

        try {
            PluginInfo oldInfo = dbManager.findById(PluginInfo.class, info.getId());
            if (oldInfo == null) {
                savePluginInfo(info);
            } else {
                updatePluginInfo(info);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean savePluginInfo(PluginInfo info) {
        try {
            dbManager.saveBindingId(info);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updatePluginInfo(PluginInfo info) {
        try {
            dbManager.saveOrUpdate(info);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deleteAll(){
        try {
            dbManager.delete(PluginInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public boolean deletePluginInfo(PluginInfo info) {
        try {
            dbManager.delete(info);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PluginInfo> selectAllPluginInfos() {
        List<PluginInfo> infos = null;
        try {
            infos = dbManager.findAll(PluginInfo.class);
            return infos;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PluginInfo selectByName(String name){
        List<PluginInfo> infos = null;
        infos = selectAllPluginInfos();
        for (PluginInfo info:infos){
            if (info.getName().equals(name)){
                return info;
            }
        }
        return null;
    }

    public List<PluginInfo> needLoadPlugins() {
        List<PluginInfo> all = selectAllPluginInfos();
        if (all != null) {
            List<PluginInfo> result = new ArrayList<>();
            boolean needLoad = false;
            for (PluginInfo info : all) {
                LogUtil.i("loader","all "+info.toString());
                if (DownloadState.FINISHED.value() == info.getDownloadState()) {
                    needLoad = true;
                }
                if (needLoad) {
                    result.add(info);
                }
            }
            return result;
        }
        return null;
    }

    public void deletePluginFromFile(PluginInfo pluginInfo){
        if (pluginInfo == null)
            return;
        String savePath = pluginInfo.getDexPath();
        if (!TextUtils.isEmpty(savePath)){
            File file = new File(savePath);
            file.delete();
        }
        deletePluginInfo(pluginInfo);

    }

    public void logAllInfos() {
        List<PluginInfo> infos = null;
        infos = selectAllPluginInfos();
        for (PluginInfo info : infos) {
            LogUtil.i("download", info.toString());
        }
    }


}
