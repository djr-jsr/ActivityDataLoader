/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.app.NotificationManager
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.location.Location
 *  android.location.LocationListener
 *  android.location.LocationManager
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.support.v4.app.ActivityCompat
 *  android.telephony.TelephonyManager
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 */
package com.example.snigdhanup.activitydataloader;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DataRecorderService
        extends Service {
    public static String data;
    public static MyThread myThread;
    private static DataWriter dataWriter;
    private static SensorDataRecorder mSimulationView;
    private NotificationCompat.Builder mBuilder;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d( "DATA RECORDER", "onCreate: ");
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

    public void onDestroy() {
        if (SensorDataRecorder.runnin) {
            mSimulationView.stopSimulation();
        }
        ((NotificationManager) this.getSystemService(NOTIFICATION_SERVICE)).cancel(1234);
        Log.d("Service", "Destroyed");
        data = null;
        super.onDestroy();
//        myThread.interrupt();
//        myThread = null;
    }

    public int onStartCommand(Intent intent, int n, int n2) {
        super.onStartCommand(intent, n, n2);
        if (SensorDataRecorder.runnin) {
            mSimulationView.stopSimulation();
        }
        String string2 = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        if (string2 == null) {
            string2 = "000000";
        }
        mSimulationView = new SensorDataRecorder(this, string2);
        dataWriter = new DataWriter(string2);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                double d = location.getLatitude();
                double d2 = location.getLongitude();
                dataWriter.writeGpsData(d, d2);
            }

            public void onProviderDisabled(String string2) {
            }

            public void onProviderEnabled(String string2) {
            }

            public void onStatusChanged(String string2, int n, Bundle bundle) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_NOT_STICKY;
        }
        locationManager.requestLocationUpdates("gps", 0, 0.0f, locationListener);
        Log.d("DATA RECORDER", "onStartCommand: " + intent.getStringExtra("type"));
        data = intent.getStringExtra("type");
        notif(data);
        if (SensorDataRecorder.runnin) {
            mSimulationView.setVehicleType(data);
        }
        mSimulationView.setVehicleType(data);

        mSimulationView.startSimulation();
        return START_REDELIVER_INTENT;
    }

    class MyThread extends Thread {
        Intent intent;
        DataRecorderService context;


        public MyThread(Intent in, DataRecorderService c) {
            this.intent = in;
            this.context = c;
            //System.out.println("MyThread started");
        }

        @Override
        public void run() {

        }
    }

}

