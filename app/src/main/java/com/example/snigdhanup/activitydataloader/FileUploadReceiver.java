/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.example.snigdhanup.activitydataloader;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.example.snigdhanup.activitydataloader.FileUploadService;

public class FileUploadReceiver
extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        Log.d((String)"Service Receiver", (String)"onReceive: ");
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                Log.d((String)"Service Receiver", (String)"onReceive: connected");
                context.startService(new Intent(context, (Class)FileUploadService.class));
                return;
            }
            Log.d((String)"Service Receiver", (String)"onReceive: disconnected");
            context.stopService(new Intent(context, (Class)FileUploadService.class));
            return;
        }
        Log.d((String)"Service Receiver", (String)"onReceive: disconnected");
        context.stopService(new Intent(context, (Class)FileUploadService.class));
    }
}

