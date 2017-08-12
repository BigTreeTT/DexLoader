package com.iekie.pluginloader.Process;

import java.util.ArrayList;

/**
 * Created by longteng on 2017/8/11.
 */

public class ProcessManager {
    private static ProcessManager instance;

    private ArrayList<Class<? extends SubProcessService>> all;

    private ArrayList<Class<? extends SubProcessService>> runningService = new ArrayList<>();

    private ProcessManager() {
        all.add(SubProcessService1.class);
        all.add(SubProcessService2.class);
        all.add(SubProcessService3.class);
        all.add(SubProcessService4.class);
        all.add(SubProcessService5.class);
    }

    public synchronized static ProcessManager getInstance(){
     if (instance == null){
         instance = new ProcessManager();
     }
        return instance;
    }

    public Class<? extends SubProcessService> getFreeServiceClass() {
        Class<? extends SubProcessService> result = null;
        for (Class clazz : all) {
            if (!runningService.contains(clazz)) {
                result = clazz;
                break;
            }
        }
        return result;

    }

    public void addRunningService(Class<? extends SubProcessService> subProcessServiceClazz) {
        if (subProcessServiceClazz != null) {
            runningService.add(subProcessServiceClazz);
        }
    }

    public void removeRunningService(Class<? extends SubProcessService> subProcessServiceClazz) {
        if (subProcessServiceClazz != null) {
            runningService.remove(subProcessServiceClazz);
        }
    }


}
