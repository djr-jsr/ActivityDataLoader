/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningServiceInfo
 *  android.app.NotificationManager
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.os.Bundle
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.support.v7.app.AppCompatActivity
 *  android.util.Log
 *  android.view.View
 *  android.view.Window
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.TextView
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.List
 */
package com.example.snigdhanup.activitydataloader;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.snigdhanup.activitydataloader.DataRecorderService;
import com.example.snigdhanup.activitydataloader.SelectActivity;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import static android.app.ActivityManager.*;

public class Main2Activity
        extends AppCompatActivity {
    private static final String TAG = "com.snigdhanup.activitydataloader.Main2Activity";
    public static boolean isActive = false;
    private PowerManager.WakeLock mWakeLock;
    private boolean started = false;

    private boolean isMyServiceRunning(Class<?> class_) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) this.getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (!class_.getName().equals(runningServiceInfo.service.getClassName())) continue;
            return true;
        }
        return false;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        setContentView(R.layout.activity_main2);
        SwitchButton switchButton = (SwitchButton) this.findViewById(R.id.sb1);
        TextView tv = (TextView) this.findViewById(R.id.tv1);
        if (isMyServiceRunning(DataRecorderService.class)) {
            SharedPreferences sharedPreferences = this.getSharedPreferences("HelloData", 0);
            switchButton.setChecked(true);
            Log.d("Main 2 Activity", "onCreate: " + sharedPreferences.getString("key", null));
            Intent intent = new Intent((Context) this, (Class) SelectActivity.class);
            intent.putExtra("data", sharedPreferences.getString("key", null));
            this.startActivity(intent);
        }
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundButton, final boolean bl) {
                new Thread(new Runnable() {

                    /*
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     */
                    public void run() {
                        try {
                            Thread.sleep((long) 500);
                        } catch (InterruptedException var1_1) {
                            var1_1.printStackTrace();
                        }
                        Main2Activity.this.runOnUiThread(new Runnable() {

                            public void run() {
                                try {
                                    if (bl) {
                                        Main2Activity.this.onTrue();
                                        return;
                                    }
                                    Main2Activity.this.onFalse();
                                    return;
                                } catch (Exception var1_1) {
                                    var1_1.printStackTrace();
                                    return;
                                }
                            }
                        });
                    }

                }).start();
            }

        });
    }

    public void onFalse() throws InterruptedException {
        this.started = false;
        this.mWakeLock.release();
        isActive = false;
        ((NotificationManager) this.getSystemService(NOTIFICATION_SERVICE)).cancel(1234);
        if (this.isMyServiceRunning(DataRecorderService.class)) {
            Intent intent = new Intent(this, DataRecorderService.class);
            intent.putExtra("type", " ");
            this.stopService(intent);
        }
        SelectActivity.getInstance().finish();
        new Thread(new Runnable() {

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void run() {
                try {
                    Thread.sleep((long) 500);
                } catch (InterruptedException var1_1) {
                    var1_1.printStackTrace();
                }
                Main2Activity.this.runOnUiThread(new Runnable() {

                    public void run() {
                        try {
                            Main2Activity.this.finish();
                            return;
                        } catch (Exception var1_1) {
                            var1_1.printStackTrace();
                            return;
                        }
                    }
                });
            }

        }).start();
    }

    public void onTrue() {
        this.started = true;
        this.mWakeLock.acquire();
        isActive = true;
        this.startActivity(new Intent(this, SelectActivity.class));
    }

}

