/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningServiceInfo
 *  android.app.Notification
 *  android.app.NotificationManager
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.res.Resources
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.support.v4.app.NotificationCompat
 *  android.support.v4.app.NotificationCompat$BigTextStyle
 *  android.support.v4.app.NotificationCompat$Builder
 *  android.support.v4.app.NotificationCompat$Style
 *  android.support.v7.app.AppCompatActivity
 *  android.util.Log
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.RadioButton
 *  android.widget.RadioGroup
 *  android.widget.RadioGroup$OnCheckedChangeListener
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.example.snigdhanup.activitydataloader;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.HashMap;

public class SelectActivity
        extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "com.snigdhanup.activitydataloader.SelectActivity";
    public static SelectActivity mainActivity;
    private HashMap<String, Integer> activities;
    private String data;
    private NotificationCompat.Builder mBuilder;
    private PowerManager.WakeLock mWakeLock;

    public static SelectActivity getInstance() {
        return mainActivity;
    }

    private boolean isMyServiceRunning(Class<?> class_) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) this.getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (!class_.getName().equals((Object) runningServiceInfo.service.getClassName()))
                continue;
            return true;
        }
        return false;
    }

    void StartFileUploadService() {
        this.startService(new Intent((Context) this, (Class<FileUploadService>) FileUploadService.class));
    }

    public void endLog(View view) {
        ((NotificationManager) this.getSystemService(NOTIFICATION_SERVICE)).cancel(1234);
        if (this.data != null && !this.data.equalsIgnoreCase("None of above")) {
            Intent intent = new Intent((Context) this, (Class<DataRecorderService>) DataRecorderService.class);
            intent.putExtra("type", this.data);
            this.stopService(intent);
        }
        this.mWakeLock.release();
        new Handler().postDelayed(new Runnable() {

            public void run() {
                if (Main2Activity.isActive) {
                    SelectActivity.this.startActivity(new Intent((Context) SelectActivity.this, (Class<SelectActivity>) SelectActivity.class));
                }
            }
        }, 60000);
    }

    public void finish() {
        super.finish();
    }

    public void notif(String string2) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", string2);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        this.mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Activity Logger")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Current ongoing activity: " + string2))
                .setContentText(("Current ongoing activity: " + string2))
                .setAutoCancel(false)
                .setUsesChronometer(true)
                .setColor(Color.GREEN)
                .setPriority(1)
                .setLights(Color.CYAN, 500, 3000)
                .setSmallIcon(R.drawable.run)
                .setOngoing(true);
        this.mBuilder.setContentIntent(pendingIntent);
        ((NotificationManager) this.getSystemService(NOTIFICATION_SERVICE)).notify(1234, this.mBuilder.build());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onCheckedChanged(RadioGroup radioGroup, int n) {
        this.data = ((RadioButton) this.findViewById(n)).getText().toString();
        Log.d((String) "Select", (String) ("onCheckedChanged: " + this.data));
        if (isMyServiceRunning(DataRecorderService.class) && this.data != null && !this.data.equalsIgnoreCase("None of above")) {
            Intent intent = new Intent((Context) this, (Class<DataRecorderService>) DataRecorderService.class);
            intent.putExtra("type", this.data);
            SharedPreferences.Editor editor = this.getSharedPreferences("HelloData", 0).edit();
            editor.putString("key", this.data);
            editor.commit();
            this.stopService(intent);
            this.notif(this.data);
            this.startService(intent);
        } else {
            if (!this.data.equalsIgnoreCase("None of above")) return;
            {
                ((NotificationManager) this.getSystemService(NOTIFICATION_SERVICE)).cancel(1234);
                new Intent((Context) this, (Class) DataRecorderService.class).putExtra("type", this.data);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activities = new HashMap<String, Integer>();
        mainActivity = this;
        this.mWakeLock = ((PowerManager) this.getSystemService(POWER_SERVICE)).newWakeLock(10, "com.snigdhanup.activitydataloader.SelectActivity");
        this.setContentView(R.layout.activity_main);
        this.activities.put(this.getResources().getString(R.string.Cycling), R.id.Cycling);
        this.activities.put(this.getResources().getString(R.string.Driving), R.id.Driving);
        this.activities.put(this.getResources().getString(R.string.Stationary), R.id.Stationary);
        this.activities.put(this.getResources().getString(R.string.ClimbingDown), R.id.ClimbingDown);
        this.activities.put(this.getResources().getString(R.string.ClimbingUp), R.id.ClimbingUp);
        this.activities.put(this.getResources().getString(R.string.Running), R.id.Running);
        this.activities.put(this.getResources().getString(R.string.Walking), R.id.Walking);
        this.activities.put(this.getResources().getString(R.string.NoneOfAbove), R.id.NoneOfAbove);
        this.activities.put(this.getResources().getString(R.string.SitUp), R.id.SitUp);
        ((RadioGroup) this.findViewById(R.id.rg)).setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) this);
        SwitchButton switchButton = (SwitchButton) this.findViewById(R.id.sb2);
        if (isMyServiceRunning(DataRecorderService.class)) {
            Log.d((String) "Select", (String) ("DATA " + this.getIntent().getStringExtra("data")));
            ((RadioButton) this.findViewById(this.activities.get(this.getIntent().getStringExtra("data")))).setChecked(true);
            switchButton.setChecked(true);
        }
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(final CompoundButton compoundButton, final boolean bl) {
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
                        SelectActivity.this.runOnUiThread(new Runnable() {

                            public void run() {
                                try {
                                    if (bl) {
                                        SelectActivity.this.onTrue((View) compoundButton);
                                        return;
                                    }
                                    SelectActivity.this.onFalse((View) compoundButton);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onFalse(View view) {
        this.endLog(view);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onTrue(View view) {
        this.StartFileUploadService();
        this.startLog(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void startLog(View view) {
        this.mWakeLock.acquire();
        if (this.data != null && !this.data.equalsIgnoreCase("None of above")) {
            Intent intent = new Intent(this, DataRecorderService.class);
            intent.putExtra("type", this.data);
            SharedPreferences.Editor editor = this.getSharedPreferences("HelloData", 0).edit();
            editor.putString("key", this.data);
            editor.commit();
            this.stopService(intent);
            this.notif(this.data);
            this.startService(intent);
        } else {
            if (this.data != null && !this.data.equalsIgnoreCase("None of above")) return;
            {
                ((SwitchButton) this.findViewById(R.id.sb2)).setChecked(false);
            }
        }
    }

}

