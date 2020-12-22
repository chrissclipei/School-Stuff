package com.example.assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.List;

public class ViewActivity extends AppCompatActivity {
    static ScrollView gallery;
    static LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        gallery = findViewById(R.id.gallery);
        layout = findViewById(R.id.textPics);
        MainActivity.db.view();
    }

}
