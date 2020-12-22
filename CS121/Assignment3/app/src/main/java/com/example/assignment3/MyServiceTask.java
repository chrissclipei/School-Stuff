package com.example.assignment3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.service.carrier.CarrierMessagingService;
import android.util.Log;

import java.util.Random;

class MyServiceTask implements SensorEventListener, Runnable{
    public static final String LOG_TAG = "MyService";
    private Context context;
    private boolean running;
    public boolean moved = false;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    float threshold;

    float x, y = 0;

    ResultCallback resultCallback;


    public MyServiceTask(Context context) {
        this.context = context;
    }

    public boolean didItMove() {
        return moved;
    }

    public void clear() {
        x = 0;
        y = 0;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            didItMove();
            notifyResultCallback(moved);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float cx, cy;
        threshold = accelerometer.getMaximumRange()/2;
        cx = event.values[0];
        cy = event.values[1];

        if (cx < 2)
            cx = 0;
        if (cy < 2)
            cy = 0;

        if (x < cx)
            x = cx;
        if (y < cy)
            y = cy;

        MainActivity.accX.setText(Float.toString(cx));
        MainActivity.accY.setText(Float.toString(cy));

        if (cx > threshold || cy > threshold)
            moved = true;
    }

    private void notifyResultCallback(boolean b) {
        ServiceResult result = new ServiceResult();
        if (result != null) {
            result.moved = b;
            Log.i(LOG_TAG, "calling resultCallback for " + result.moved);
            resultCallback.onResultReady(result);
        }
    }


    public void stopProcessing() {
        running = false;
    }


    public void updateResultCallback(ResultCallback result) {
        Log.i(LOG_TAG, "Adding result callback");
        resultCallback = result;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface ResultCallback {
        void onResultReady(ServiceResult result);
    }
}
