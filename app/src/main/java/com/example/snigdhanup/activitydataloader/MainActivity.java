/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningServiceInfo
 *  android.app.Notification
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.location.Location
 *  android.location.LocationListener
 *  android.location.LocationManager
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.support.v4.app.ActivityCompat
 *  android.support.v4.app.NotificationCompat
 *  android.support.v4.app.NotificationCompat$BigTextStyle
 *  android.support.v4.app.NotificationCompat$Builder
 *  android.support.v4.app.NotificationCompat$Style
 *  android.support.v7.app.AppCompatActivity
 *  android.telephony.TelephonyManager
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
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.List
 */
package com.example.snigdhanup.activitydataloader;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.snigdhanup.activitydataloader.DataWriter;
import com.example.snigdhanup.activitydataloader.FileUploadService;
import com.example.snigdhanup.activitydataloader.Main2Activity;
import com.example.snigdhanup.activitydataloader.SensorDataRecorder;
import com.kyleduo.switchbutton.SwitchButton;
import java.util.List;

public class MainActivity
extends AppCompatActivity
implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "com.snigdhanup.activitydataloader.MainActivity";
    public static MainActivity mainActivity;
    private String data;
    private DataWriter dataWriter;
    private SensorDataRecorder mSimulationView;
    private PowerManager.WakeLock mWakeLock;
    private boolean started = false;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    private boolean isMyServiceRunning(Class<?> class_) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager)this.getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (!class_.getName().equals((Object)runningServiceInfo.service.getClassName())) continue;
            return true;
        }
        return false;
    }

    void StartFileUploadService() {
        this.startService(new Intent((Context)this, (Class)FileUploadService.class));
    }

    void StopFileUploadService() {
        this.stopService(new Intent((Context)this, (Class)FileUploadService.class));
    }

    public void endLog(View view) {
        if (this.data != null && !this.data.equalsIgnoreCase("None of above")) {
            this.mSimulationView.stopSimulation();
        }
        this.mWakeLock.release();
        this.finish();
        new Handler().postDelayed(new Runnable(){

            public void run() {
                if (Main2Activity.isActive) {
                    MainActivity.this.startActivity(new Intent((Context)MainActivity.this, (Class)MainActivity.class));
                }
            }
        }, 60000);
    }

    public void notif(String string2) {
        Intent intent = new Intent((Context)this, (Class)MainActivity.class);
        intent.setFlags(872415232);
        PendingIntent pendingIntent = PendingIntent.getActivity((Context)this, (int)0, (Intent)intent, (int)268435456);
        NotificationCompat.Builder builder = new NotificationCompat.Builder((Context)this).setContentTitle((CharSequence)"Activity Logger").setStyle((NotificationCompat.Style)new NotificationCompat.BigTextStyle().bigText((CharSequence)("Current ongoing activity: " + string2))).setContentText((CharSequence)" needs your help!").setAutoCancel(true).setColor(-16711936).setFullScreenIntent(pendingIntent, true).setPriority(2).setLights(-65536, 500, 3000).setSmallIcon(2130903040).setOngoing(true);
        builder.setContentIntent(pendingIntent);
        builder.build();
    }

    public void onCheckedChanged(RadioGroup radioGroup, int n) {
        this.data = ((RadioButton)this.findViewById(n)).getText().toString();
        this.mSimulationView.setVehicleType(this.data);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mainActivity = this;
        this.mWakeLock = ((PowerManager)this.getSystemService("power")).newWakeLock(10, "com.snigdhanup.activitydataloader.MainActivity");
        String string2 = ((TelephonyManager)this.getSystemService("phone")).getDeviceId();
        if (string2 == null) {
            string2 = "000000";
        }
        this.mSimulationView = new SensorDataRecorder((Context)this, string2);
        this.dataWriter = new DataWriter(string2);
        LocationManager locationManager = (LocationManager)this.getSystemService("location");
        LocationListener locationListener = new LocationListener(){

            public void onLocationChanged(Location location) {
                double d = location.getLatitude();
                double d2 = location.getLongitude();
                MainActivity.this.dataWriter.writeGpsData(d, d2);
            }

            public void onProviderDisabled(String string2) {
            }

            public void onProviderEnabled(String string2) {
            }

            public void onStatusChanged(String string2, int n, Bundle bundle) {
            }
        };
        if (ActivityCompat.checkSelfPermission((Context)this, (String)"android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission((Context)this, (String)"android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return;
        }
        locationManager.requestLocationUpdates("gps", 0, 0.0f, locationListener);
        this.setContentView(2130968601);
        ((RadioGroup)this.findViewById(2131492972)).setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener)this);
        ((SwitchButton)this.findViewById(2131492982)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(final CompoundButton compoundButton, final boolean bl) {
                new Thread(new Runnable(){

                    /*
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     */
                    public void run() {
                        try {
                            Thread.sleep((long)500);
                        }
                        catch (InterruptedException var1_1) {
                            var1_1.printStackTrace();
                        }
                        MainActivity.this.runOnUiThread(new Runnable(){

                            public void run() {
                                try {
                                    if (bl) {
                                        MainActivity.this.onTrue((View)compoundButton);
                                        return;
                                    }
                                    MainActivity.this.onFalse((View)compoundButton);
                                    return;
                                }
                                catch (Exception var1_1) {
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

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(2131558400, menu2);
        return true;
    }

    public void onFalse(View view) {
        this.started = false;
        this.endLog(view);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131493005) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onTrue(View view) {
        this.started = true;
        this.StartFileUploadService();
        this.startLog(view);
    }

    public void startLog(View view) {
        this.mWakeLock.acquire();
        if (this.data != null && !this.data.equalsIgnoreCase("None of above")) {
            this.mSimulationView.startSimulation();
        }
    }

}

