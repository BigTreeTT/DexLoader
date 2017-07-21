package com.iekie.font.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by longteng on 2017/7/21.
 */

public class SubProcessReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String[] strs = new String[3];
        strs[4] = "1";
    }
}
