package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyDB extends SQLiteOpenHelper {
    Context ctx;
    SQLiteDatabase db;
    String UPDATED_TABLE = "TABLE_UPDATE";
    static String DB_NAME = "DATABASE";
    static String TABLE_NAME = "NAME_TABLE";
    static int VERSION = 1;

    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        ctx = context;
        VERSION = version;
        DB_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, IMAGE_TITLE TEXT, IMAGE_DATA BLOB);");
        Toast.makeText(ctx, "Table is created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (VERSION == oldVersion){
            VERSION = newVersion;
            db =getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            onCreate(db);
        }
    }

    public void insert(String title, byte[] image) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IMAGE_TITLE", title);
        cv.put("IMAGE_DATA", image);
        db.insert(TABLE_NAME, null, cv);
    }

    public void view() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY _id";
        db = getReadableDatabase();
        List<Images> imageList = new ArrayList<Images>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                String textTitle = cursor.getString(1);
                byte[] imageByte = cursor.getBlob(2);
                Images image = new Images();
                image.setTitle(textTitle);
                image.setImage(imageByte);
                imageList.add(image);
            }while(cursor.moveToNext());
        }

    }

    public void delete(String id, String title) {
        db = getWritableDatabase();
        int queries;
        if (TextUtils.isEmpty(title)){
            queries = (int)DatabaseUtils.queryNumEntries(db, TABLE_NAME, "_id=?",new String[] {id});
            if (queries == 0) {
                Toast.makeText(ctx, "No Entry with ID: " + id + " exists", Toast.LENGTH_LONG).show();
            } else {
                db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = \"" + id + "\";");
                Toast.makeText(ctx, "Deleted ID: " + id + " from table", Toast.LENGTH_LONG).show();
            }
        } else if (TextUtils.isEmpty(id)){
            queries = (int)DatabaseUtils.queryNumEntries(db, TABLE_NAME, "IMAGE_TITLE=?",new String[] {title});
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE IMAGE_TITLE = '" + title + "';", null);
            if (queries == 0) {
                Toast.makeText(ctx, "No Entry with the title: " + title + " exists", Toast.LENGTH_LONG).show();
            } else {
                int counter = 0;
                while(counter < queries) {
                    db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE IMAGE_TITLE = \"" + title + "\";");
                    counter++;
                }
                Toast.makeText(ctx, "Deleted all images with the title: " + title + " from table", Toast.LENGTH_LONG).show();
            }
        } else {
            queries = (int)DatabaseUtils.queryNumEntries(db, TABLE_NAME, "_id=? AND IMAGE_TITLE=?",new String[] {id, title});
            if (queries == 0) {
                Toast.makeText(ctx, "No Entry with ID: " + id + " and title: " + title + " exists", Toast.LENGTH_LONG).show();
            } else {
                db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE IMAGE_TITLE = \"" + title + "\" AND _id = \"" + id + "\";");
                Toast.makeText(ctx, "Deleted ID: " + id + " with title: " + title + " from table", Toast.LENGTH_LONG).show();
            }
        }
    }
    public List<Images> getImages() {
        db = getReadableDatabase();
        List<Images> imageList = new ArrayList<Images>();
        String selectQuery = "SELECT * FROM pictures ORDER BY _id ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                String textTitle = cursor.getString(1);
                byte[] imageByte = cursor.getBlob(2);
                Images image = new Images();
                image.setTitle(textTitle);
                image.setImage(imageByte);
                imageList.add(image);
            }while(cursor.moveToNext());
        }
        return imageList;
    }
}
