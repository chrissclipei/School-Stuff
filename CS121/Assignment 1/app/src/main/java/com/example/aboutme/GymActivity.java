package com.example.aboutme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GymActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
    }

    public void goBack(View view) {
        finish();
    }

    public void delete(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
