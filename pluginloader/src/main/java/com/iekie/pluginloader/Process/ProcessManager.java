package com.iekie.pluginloader.Process;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by longteng on 2017/8/11.
 */

public class ProcessManager {
    private static ProcessManager instance;

    private ArrayList<Class<? extends SubProcessService>> all;

    private ArrayList<Class<? extends SubProcessService>> runningService = new ArrayList<>();

    private HashMap<String,Class<? extends SubProcessService>> servicePidMap;

    private ProcessManager() {
        all = new ArrayList<>();
        all.add(SubProcessService1.class);
        all.add(SubProcessService2.class);
        all.add(SubProcessService3.class);
        all.add(SubProcessService4.class);
        all.add(SubProcessService5.class);

        servicePidMap = new HashMap<>();
        servicePidMap.put("com.iekie.font:plugin1",SubProcessService1.class);
        servicePidMap.put("com.iekie.font:plugin2",SubProcessService2.class);
        servicePidMap.put("com.iekie.font:plugin3",SubProcessService3.class);
        servicePidMap.put("com.iekie.font:plugin4",SubProcessService4.class);
        servicePidMap.put("com.iekie.font:plugin5",SubProcessService5.class);
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
                addRunningService(clazz);
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

    public void removeRunningService(String processName) {
        Class<? extends SubProcessService> subProcessServiceClazz = null;
        subProcessServiceClazz = servicePidMap.get(processName);
        if (subProcessServiceClazz != null) {
            runningService.remove(subProcessServiceClazz);
        }
    }

    public void removeAll(){
        runningService.clear();
    }




}
