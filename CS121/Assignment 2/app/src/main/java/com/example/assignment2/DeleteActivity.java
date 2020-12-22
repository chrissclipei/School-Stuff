package com.example.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteActivity extends AppCompatActivity {
    EditText id;
    EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        id = findViewById(R.id.idToDelete);
        title = findViewById(R.id.titleToDelete);
    }

    public void remove(View view) {
        String idToDelete = id.getText().toString().trim();
        String titleToDelete = title.getText().toString().trim();
        if (TextUtils.isEmpty(idToDelete) && TextUtils.isEmpty(titleToDelete)){
            Toast.makeText(this, "ID and Title cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(titleToDelete)){
            MainActivity.db.delete(idToDelete, null);
        } else if (TextUtils.isEmpty(idToDelete)){
            MainActivity.db.delete(null, titleToDelete);
        } else {
            MainActivity.db.delete(idToDelete, titleToDelete);
        }
        finish();
    }
}
