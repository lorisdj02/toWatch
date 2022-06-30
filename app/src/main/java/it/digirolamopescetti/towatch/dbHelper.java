package it.digirolamopescetti.towatch;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import androidx.annotation.Nullable;

//database:
//URL (NULL)
//webSite
//name PK
//isFavourite
//image
//status

public class dbHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "Movies.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "my_movies";
    public static final String COL_URL = "url";
    public static final String COL_WEBSITE = "website";
    public static final String COL_NAME = "name";
    public static final String COL_FAVOURITE = "favourite";
    public static final String COL_IMG = "img";
    public static final String COL_STATUS = "status";

    public dbHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
