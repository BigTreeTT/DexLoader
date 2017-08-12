package com.iekie.font;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iekie.pluginloader.internal.PluginManager;
import com.iekie.pluginloader.internal.PluginManagerImpl;

/**
 * 测试动态加载
 */
public class MainActivity extends AppCompatActivity {
    public static final String JSONSTRING = "{\"action\":\"load_jar\",\"name\":\"PluginTest.apk\",\"method\":\"init\",\"className\":\"com.iekie.plugintest.Sdk\",\"url\":\"http://sw.bos.baidu.com/sw-search-sp/software/c4048cea91ade/QQPhoneManager_5.6.1.5129.exe\",\"process\":\"subProcess\",\"version\":\"1.0\",\"type\":\"0\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendPushMessage(View view){
        PluginManager pluginManager = PluginManagerImpl.getInstance();
        pluginManager.handleMessage(this,JSONSTRING);
    }

    public void loadPlugin(View view){
        PluginManager pluginManager = PluginManagerImpl.getInstance();
        pluginManager.loadPlugin();
    }

    public void stopPlugin(View view){
        PluginManager pluginManager = PluginManagerImpl.getInstance();
        pluginManager.stopPlugin();
    }

    public void saveTestData(View v){
         PluginManagerImpl.getInstance().createTestData("PluginTest.apk","init","com.iekie.plugintest.Sdk");
    }


}
