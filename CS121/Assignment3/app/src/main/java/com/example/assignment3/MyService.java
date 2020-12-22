package com.example.assignment3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.service.carrier.CarrierMessagingService;
import android.util.Log;

public class MyService extends Service {
    private static final String LOG_TAG = "MyService";

    private Thread myThread;
    private MyServiceTask myTask;

    PowerManager.WakeLock wakeLock;

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    private final IBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "Service is being bound");
        return myBinder;
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Service is being created");
        myTask = new MyServiceTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
        if (!myThread.isAlive()) {
            myThread.start();
        }
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();
        return START_STICKY;
    }


    public void updateResultCallback(MyServiceTask.ResultCallback resultCallback) {
        myTask.updateResultCallback(resultCallback);
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "Stopping.");
        wakeLock.release();
        myTask.stopProcessing();
        Log.i(LOG_TAG, "Stopped.");
    }

}
