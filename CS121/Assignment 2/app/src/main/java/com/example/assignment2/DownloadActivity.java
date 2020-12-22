package com.example.assignment2;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DownloadActivity extends AppCompatActivity {
    static EditText editURL;
    static EditText editTitle;
    static ImageView imageView;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        editURL = findViewById(R.id.imageURL);
        editTitle = findViewById(R.id.imageText);
        imageView = findViewById(R.id.imageView);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    public void insert(View view) {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String imageURL = editURL.getText().toString();
            ImageDownloader imageDownloader = new ImageDownloader();
            imageDownloader.execute(imageURL);
            finish();
            Toast.makeText(this, "Image Download Successful", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
        }
    }
}
