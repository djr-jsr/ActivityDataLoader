/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.hardware.Sensor
 *  android.hardware.SensorEvent
 *  android.hardware.SensorEventListener
 *  android.hardware.SensorManager
 *  android.view.View
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.example.snigdhanup.activitydataloader;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import com.example.snigdhanup.activitydataloader.DataWriter;

public class ExperimentDataRecorder
extends View
implements SensorEventListener {
    private float[] accValues = null;
    private DataWriter dataWriter;
    private Sensor mAccelerometer;
    private Sensor mLAccelerometer;
    private float[] mLastAccelerometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastMagnetometerSet = false;
    private Sensor mMagnetometer;
    private float[] mOrientation = new float[3];
    private float[] mR = new float[9];
    private SensorManager mSensorManager;
    private long timeStamp = 0;
    private String vehicleType = "";

    public ExperimentDataRecorder(Context context, String string2) {
        super(context);
        this.mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
        this.mMagnetometer = this.mSensorManager.getDefaultSensor(2);
        this.mLAccelerometer = this.mSensorManager.getDefaultSensor(10);
        this.dataWriter = new DataWriter(string2);
    }

    private float[] lowPass(float[] arrf, float[] arrf2) {
        if (arrf2 == null) {
            return arrf;
        }
        for (int i = 0; i < arrf.length; ++i) {
            arrf2[i] = arrf2[i] + 0.15f * (arrf[i] - arrf2[i]);
        }
        return arrf2;
    }

    public void onAccuracyChanged(Sensor sensor, int n) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 1) {
            this.accValues = lowPass((float[])sensorEvent.values.clone(), this.accValues);
            this.dataWriter.writeAccData(this.accValues[0], this.accValues[1], this.accValues[2], false);
            System.arraycopy((Object)sensorEvent.values, (int)0, (Object)this.mLastAccelerometer, (int)0, (int)sensorEvent.values.length);
            this.mLastAccelerometerSet = true;
        }
        if (sensorEvent.sensor.getType() == 10) {
            this.accValues = sensorEvent.values;
            this.dataWriter.writeLaccData(this.accValues[0], this.accValues[1], this.accValues[2], false);
        }
        if (sensorEvent.sensor.getType() == 2) {
            this.accValues = sensorEvent.values;
            this.dataWriter.writeMagData(this.accValues[0], this.accValues[1], this.accValues[2], false);
            System.arraycopy((Object)sensorEvent.values, (int)0, (Object)this.mLastMagnetometer, (int)0, (int)sensorEvent.values.length);
            this.mLastMagnetometerSet = true;
        }
        if (this.mLastAccelerometerSet && this.mLastMagnetometerSet) {
            SensorManager.getRotationMatrix((float[])this.mR, (float[])null, (float[])this.mLastAccelerometer, (float[])this.mLastMagnetometer);
            SensorManager.getOrientation((float[])this.mR, (float[])this.mOrientation);
            float f = (float)(360.0 + Math.toDegrees((double)this.mOrientation[0])) % 360.0f;
            long l = sensorEvent.timestamp;
            if (this.timeStamp == 0) {
                this.timeStamp = l;
            }
            this.dataWriter.writeComData(f, false);
        }
    }

    public void startSimulation() {
        this.mSensorManager.registerListener((SensorEventListener)this, this.mAccelerometer, 2);
        this.mSensorManager.registerListener((SensorEventListener)this, this.mMagnetometer, 2);
        this.mSensorManager.registerListener((SensorEventListener)this, this.mLAccelerometer, 2);
        this.dataWriter.writeVehicleData(this.vehicleType);
    }

    public void stopSimulation() {
        this.mSensorManager.unregisterListener((SensorEventListener)this);
        this.timeStamp = 0;
    }
}

