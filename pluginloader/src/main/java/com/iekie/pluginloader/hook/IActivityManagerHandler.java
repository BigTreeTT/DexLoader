package com.iekie.pluginloader.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.iekie.pluginloader.Process.ProxyService;
import com.iekie.pluginloader.Process.ProxyService1;
import com.iekie.pluginloader.Process.ProxyService2;
import com.iekie.pluginloader.Process.ProxyService3;
import com.iekie.pluginloader.Process.ProxyService4;
import com.iekie.pluginloader.Process.ProxyService5;
import com.iekie.pluginloader.internal.PluginManagerImpl;
import com.iekie.pluginloader.util.ProcessUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author weishu
 * @dete 16/1/7.
 */
/* package */ class IActivityManagerHandler implements InvocationHandler {

    private static final String TAG = "plugin_IActivity";
    private Context mContext;

    Object mBase;

    public IActivityManagerHandler(Context context,Object base) {
        mBase = base;
        mContext = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("startService".equals(method.getName())) {
            // 只拦截这个方法
            // API 23:
            // public ComponentName startService(IApplicationThread caller, Intent service,
            //        String resolvedType, int userId) throws RemoteException

            // 找到参数里面的第一个Intent 对象
            Pair<Integer, Intent> integerIntentPair = foundFirstIntentOfArgs(args);
            String preLoadPckName = ServiceManager.getInstance().selectPluginService(
                    integerIntentPair.second.getComponent().getClassName());

            if (preLoadPckName == null){
                return method.invoke(mBase, args);
            }
            if (preLoadPckName.equals(PluginManagerImpl.getInstance().getContext().getPackageName())) {
                return method.invoke(mBase, args);
            }
            Intent newIntent = new Intent();

            // 代理Service的包名, 也就是我们自己的包名
            String stubPackage = PluginManagerImpl.getInstance().getContext().getPackageName();

            // 这里我们把启动的Service替换为ProxyService, 让ProxyService接收生命周期回调,根据当前进程选择代理service
            Class<?> proxyClazz = null;
            String currentProcessName = ProcessUtil.currentProcessName(mContext);
            if (currentProcessName == null){
                proxyClazz = ProxyService.class;
            }else if (currentProcessName.equals("plugin1")){
                proxyClazz = ProxyService1.class;
            }else if (currentProcessName.equals("plugin2")){
                proxyClazz = ProxyService2.class;
            }else if (currentProcessName.equals("plugin3")){
                proxyClazz = ProxyService3.class;
            }else if (currentProcessName.equals("plugin4")){
                proxyClazz = ProxyService4.class;
            }else if (currentProcessName.equals("plugin5")){
                proxyClazz = ProxyService5.class;
            }else {
                proxyClazz = ProxyService.class;
            }
            ComponentName componentName = new ComponentName(stubPackage, proxyClazz.getName());
            newIntent.setComponent(componentName);

            // 把我们原始要启动的TargetService先存起来
            newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, integerIntentPair.second);

            // 替换掉Intent, 达到欺骗AMS的目的
            args[integerIntentPair.first] = newIntent;

            Log.v(TAG, "hook method startService success");
            return method.invoke(mBase, args);

        }

        // public int stopService(IApplicationThread caller, Intent service,String resolvedType, int userId) throws RemoteException
        if ("stopService".equals(method.getName())) {
            Intent raw = foundFirstIntentOfArgs(args).second;
            String preLoadPckName = ServiceManager.getInstance().
                    selectPluginService(raw.getComponent().getClassName());

            if (preLoadPckName == null){
                return method.invoke(mBase, args);
            }

            if (!preLoadPckName.equals(PluginManagerImpl.getInstance().getContext().getPackageName())) {
                // 插件的intent才做hook
                Log.v(TAG, "hook method stopService success");
                return ServiceManager.getInstance().stopService(raw);
            }
        }

        return method.invoke(mBase, args);
    }

    private Pair<Integer, Intent> foundFirstIntentOfArgs(Object... args) {
        int index = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Intent) {
                index = i;
                break;
            }
        }
        return Pair.create(index, (Intent) args[index]);
    }
}
