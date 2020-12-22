package com.example.aboutme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
    }

    public void goBack(View view) {
        finish();
    }

    public void delete(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
