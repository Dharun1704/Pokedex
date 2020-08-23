package com.example.pokedex.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    String PokeImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    private Context context;
    public static final String DATABASE_NAME = "favorite.db";
    public static final String TABLE_NAME = "favorite_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "URL";

    public Database(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL2 + " TEXT, " +
                        COL3 + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String pokeName, String pokeUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, pokeName);
        contentValues.put(COL3, pokeUrl);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public int  deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "NAME = ?", new String[] {name});
    }
}
