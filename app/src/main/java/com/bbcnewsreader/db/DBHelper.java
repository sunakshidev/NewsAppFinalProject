package com.bbcnewsreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context){
            super(context, "BBCNewsReader.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table NewsDetails(id INTEGER primary key, title TEXT UNIQUE, description  TEXT, link  TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) db.execSQL("drop Table if exists NewsDetails");
    }

    public Boolean insertNewsFeed(String title, String description, String link) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //use to put values inside table
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("link", link);

        long result = DB.insert("NewsDetails", null, contentValues);
        //if insertion fails it will return false
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getNewsFeed() {
        SQLiteDatabase DB = this.getWritableDatabase();
        //cursor is usually selecting the row. whatever you seleted is loaded in the cursor
        Cursor cursor = DB.rawQuery("Select * from NewsDetails", null);
        return cursor;
    }

    public boolean deleteNewsFeed(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("NewsDetails", "title=?", new String[]{title});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


}
