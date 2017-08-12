package com.iekie.pluginloader.download;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by longteng on 2017/7/24.
 */

@Table(name = "plugin_info",onCreated = "")
public class PluginInfo implements Parcelable{
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "id",isId = true)
    private String id;
    @Column(name = "version")
    private String version;
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name = "MD5")
    private String MD5;
    @Column(name = "process")
    private String process;
    @Column(name = "type")
    private int type;
    @Column(name = "className")
    private String className;
    @Column(name = "method")
    private String method;
    @Column(name = "dexPath")
    private String dexPath;
    @Column(name = "loadState")
    private int loadState;
    @Column(name = "downloadState")
    private int downloadState;

    protected PluginInfo(Parcel in) {
        id = in.readString();
        version = in.readString();
        name = in.readString();
        url = in.readString();
        MD5 = in.readString();
        process = in.readString();
        type = in.readInt();
        className = in.readString();
        method = in.readString();
        dexPath = in.readString();
        loadState = in.readInt();
        downloadState = in.readInt();
    }

    public static final Creator<PluginInfo> CREATOR = new Creator<PluginInfo>() {
        @Override
        public PluginInfo createFromParcel(Parcel in) {
            return new PluginInfo(in);
        }

        @Override
        public PluginInfo[] newArray(int size) {
            return new PluginInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDexPath() {
        return dexPath;
    }

    public void setDexPath(String dexPath) {
        this.dexPath = dexPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PluginInfo(){}

    @Override
    public String toString() {
        return "PluginInfo{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", MD5='" + MD5 + '\'' +
                ", process='" + process + '\'' +
                ", type=" + type +
                ", className='" + className + '\'' +
                ", method='" + method + '\'' +
                ", dexPath='" + dexPath + '\'' +
                ", loadState=" + loadState +
                ", downloadState=" + downloadState +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(version);
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeString(MD5);
        parcel.writeString(process);
        parcel.writeInt(type);
        parcel.writeString(className);
        parcel.writeString(method);
        parcel.writeString(dexPath);
        parcel.writeInt(loadState);
        parcel.writeInt(downloadState);
    }
}
