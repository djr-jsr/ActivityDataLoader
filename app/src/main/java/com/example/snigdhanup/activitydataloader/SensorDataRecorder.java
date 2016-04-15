package com.example.snigdhanup.activitydataloader;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

import java.io.File;

/**
 * Created by SnigdhaNUP on 1/15/2016.
 */
public class SensorDataRecorder extends View implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mLAccelerometer;
    private Sensor mMagnetometer;
    //private GPSTracker gpsTracker;

    public DataWriter dataWriter;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    private long timeStamp = 0;
    private float[] accValues = null;

    private String vehicleType = "";
    public static boolean runnin = false;

    File file;

    public SensorDataRecorder(Context context, String deviceId) {
        super(context);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mLAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //gpsTracker = new GPSTracker(context);
        dataWriter = new DataWriter(deviceId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            accValues = lowPass(event.values.clone(), accValues);
            dataWriter.writeAccData(accValues[0], accValues[1], accValues[2], true);
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        }

        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            accValues = event.values;
            dataWriter.writeLaccData(accValues[0], accValues[1], accValues[2], true);
        }

        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            accValues = event.values;
            dataWriter.writeMagData(accValues[0], accValues[1], accValues[2], true);
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

            long mSensorTimeStamp = event.timestamp;
            if(timeStamp == 0)
            {
                timeStamp = mSensorTimeStamp;
            }

            dataWriter.writeComData(azimuthInDegress, true);
        }

        //updateGPS();
    }

    /*private void updateGPS() {
        double longitude = gpsTracker.getCurrentLongitude();
        double latitude = gpsTracker.getCurrentLatitude();

        dataWriter.writeGpsData(latitude, longitude);
    }*/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float[] lowPass(float[] input, float[] output)
    {
        final float alpha = (float) 0.15;
        if(output == null) return input;

        for(int i = 0; i < input.length; i++)
        {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }

    public void startSimulation() {
        runnin = true;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mLAccelerometer, SensorManager.SENSOR_DELAY_UI);
        dataWriter.writeVehicleData(vehicleType);
    }
    public void stopSimulation() {
        runnin = false;
        mSensorManager.unregisterListener(this);
        timeStamp = 0;
    }

    public void setVehicleType(String vehicle){
        vehicleType = vehicle;
    }
}
