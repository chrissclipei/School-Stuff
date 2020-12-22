package com.example.assignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static MyDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDB(this, MyDB.DB_NAME, null, 1);
    }


    public void download(View view) {
        Intent intent = new Intent(this, DownloadActivity.class);
        startActivity(intent);
    }

    public void delete(View view) {
        Intent intent = new Intent(this, DeleteActivity.class);
        startActivity(intent);
    }

    public void view(View view) {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    public void range(View view) {
        Intent intent = new Intent(this, RangeActivity.class);
        startActivity(intent);
    }
}
