package com.iekie.font;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.iekie.font.aidl.ITest;
import com.iekie.font.loader.ClassDynamicLoader;
import com.iekie.font.service.SubProcessService;

public class MainActivity extends AppCompatActivity {
    ITest mServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService();
    }

    public void bindService() {
        Intent intent = new Intent(MainActivity.this, SubProcessService.class);
        bindService(intent, mConnect, BIND_AUTO_CREATE);
    }


    /**
     * 测试子进程中方法
     * @param v
     */
    public void invokeSubProcess(View v) {
        try {
            if (mServiceBinder != null) {
                int sum = mServiceBinder.add(1, 3);
                Toast.makeText(this, "sun = " + sum, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "mServiceBinder == null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.i("lt_debug", "ServiceBinder error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 启动插件apk Service（占坑方式）
     * @param v
     */
    public void startService1(View v) {
        Intent intent = new Intent();
        intent.setAction("me.plugin.action.libService");
        intent.setPackage("com.iekie.libapk2");
        startService(intent);
    }

    /**
     * 启动插件apk Service(代理方式)
     * @param v
     */
    public void startProxyService(View v) {
        Intent intent = new Intent();
        intent.setAction("me.plugin.action.ProxyService");
        intent.setPackage(getPackageName());
        startService(intent);
    }

    /**
     * 调用插件中方法（无资源文件，无组件）
     * @param v
     */

    public void loadClass(View v) {
        try {
            ClassDynamicLoader.getInstance().loadClass(getApplicationContext());

        } catch (Exception e) {
            Log.i("lt_debug", "invokeSubProcess loadClass error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mServiceBinder = ITest.Stub.asInterface(iBinder);
            Log.i("lt_debug", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
