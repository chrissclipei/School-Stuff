package com.example.aboutme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    static int gamingAct = 1;
    static int sportsAct = 2;
    static int gymAct = 3;
    boolean gamingDelete, sportsDelete, gymDelete = false;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sh = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Log.d(MainActivity.class.getSimpleName(), "app is created");
        Log.v("", Boolean.toString(gamingDelete));
        if (sh.getBoolean("gamingDelete", true)) {
            Log.d(MainActivity.class.getSimpleName(), "gamingDelete is true onCreate");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = sh.edit();
        if (gamingDelete) {
            edit.putBoolean("deleteGaming", true).apply();
            Log.d(MainActivity.class.getSimpleName(), "gamingDelete is true");
        }
        if (sportsDelete) {
            edit.putBoolean("sportsDelete", true).apply();
            Log.d(MainActivity.class.getSimpleName(), "sportsDelete is true");
        }
        if (gymDelete) {
            edit.putBoolean("gymDelete", true).apply();
            Log.d(MainActivity.class.getSimpleName(), "gymDelete is true");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (gamingDelete){
            savedInstanceState.putBoolean("gamingDelete", true);
        }
        if (sportsDelete){
            savedInstanceState.putBoolean("sportsDelete", true);
        }
        if (gymDelete){
            savedInstanceState.putBoolean("gymDelete", true);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Button gaming = findViewById(R.id.gaming);
        Button sports = findViewById(R.id.sports);
        Button gym = findViewById(R.id.gym);
        if (savedInstanceState != null){
            if (savedInstanceState.getBoolean("gamingDelete")){
                gaming.setVisibility(View.INVISIBLE);
            }
            if (savedInstanceState.getBoolean("sportsDelete")){
                sports.setVisibility(View.INVISIBLE);
            }
            if (savedInstanceState.getBoolean("gymDelete")){
                gym.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void openGaming(View view) {
        Intent intent = new Intent(this, GamingActivity.class);
        startActivityForResult(intent, MainActivity.gamingAct);
    }

    public void openSports(View view) {
        Intent intent = new Intent(this, SportsActivity.class);
        startActivityForResult(intent, MainActivity.sportsAct);
    }

    public void openGym(View view) {
        Intent intent = new Intent(this, GymActivity.class);
        startActivityForResult(intent, MainActivity.gymAct);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LinearLayout layout = findViewById(R.id.layout);
        Button gaming = findViewById(R.id.gaming);
        Button sports = findViewById(R.id.sports);
        Button gym = findViewById(R.id.gym);
        if (resultCode == -1 && requestCode == 1){
            gamingDelete = true;
            gaming.setVisibility(View.INVISIBLE);
        }
        if (resultCode == -1 && requestCode == 2){
            sportsDelete = true;
            sports.setVisibility(View.INVISIBLE);
        }
        if (resultCode == -1 && requestCode == 3){
            gymDelete = true;
            gym.setVisibility(View.INVISIBLE);
        }
    }

    public void exitApp(View view) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(home);
    }
}
