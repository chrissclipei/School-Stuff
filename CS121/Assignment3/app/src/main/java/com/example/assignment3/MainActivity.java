package com.example.assignment3;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements com.example.assignment3.MyServiceTask.ResultCallback {
    Context context;
    public MyService myService;
    public boolean serviceBound;
    private CountDownTimer timer;
    private long timeLeft = 30000;

    public static final String LOG_TAG = "MyService";

    private Handler mUiHandler;

    public static final int DISPLAY_NUMBER = 10;

    public MyServiceTask myServiceTask = new MyServiceTask(context);

    TextView status;
    public static EditText accX, accY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);
        accX = findViewById(R.id.x);
        accY = findViewById(R.id.y);
        mUiHandler = new Handler(getMainLooper(), new UiCallback());
        serviceBound = false;
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                status.setText(R.string.quiet);
                bindMyService();
            }
        }.start();
    }

    private void updateTimer() {
        int seconds = (int) timeLeft / 1000;
        String timeLeftText = "0:" + seconds;
        status.setText(timeLeftText);
    }

    private void bindMyService() {
        Log.i(LOG_TAG, "Starting the service");
        Intent intent = new Intent(this, MyService.class);
        Log.i(LOG_TAG, "Trying to bind");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(this, "Binded", Toast.LENGTH_LONG).show();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            myService = binder.getService();
            serviceBound = true;
            Log.i("MyService", "Bound succeeded, adding the callback");
            myService.updateResultCallback(MainActivity.this);
            Toast.makeText(MainActivity.this, "serviceconnection", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onPause() {
        if (serviceBound) {
            Log.i("MyService", "Unbinding");
            unbindService(serviceConnection);
            serviceBound = false;
            if (true) {
                Log.i(LOG_TAG, "Stopping.");
            }
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindMyService();
    }

    public void clear(View view) {
        onPause();
        myServiceTask.clear();
        onResume();
        status.setText(R.string.quiet);
    }

    public void onResultReady(ServiceResult result) {
        if (result != null) {
            Log.i(LOG_TAG, "Preparing a message for " + result.moved);
        } else {
            Log.e(LOG_TAG, "Received an empty result!");
        }
        mUiHandler.obtainMessage(DISPLAY_NUMBER, result).sendToTarget();
    }

    private class UiCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == DISPLAY_NUMBER) {
                ServiceResult result = new ServiceResult();
                result.moved = (boolean) msg.obj;
                if (result != null) {
                    Log.i(LOG_TAG, "Displaying: " + result.moved);
                    status.setText("Something happened");
                } else {
                    Log.e(LOG_TAG, "Error: received empty message!");
                }
            }
            return true;
        }
    }

    public void exit(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        Toast.makeText(MainActivity.this, "Service is terminated", Toast.LENGTH_LONG).show();
        finish();
    }
}
