package it.digirolamopescetti.towatch;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//database:
//URL (NULL)
//webSite
//name PK
//isFavourite
//image
//status

public class dbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Movies.db";
    private static final int DATABASE_VERSION = 7;

    private static final String TABLE_NAME = "my_movies";
    private static final String COL_URL = "url";
    private static final String COL_WEBSITE = "website";
    private static final String COL_NAME = "name";
    private static final String COL_FAVOURITE = "favourite";
    private static final String COL_IMG = "img";
    private static final String COL_STATUS = "status";
    private static final String COL_CUSTOM = "custom_website";

    public dbHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_NAME + " VARCHAR(256) PRIMARY KEY, " +      //0
                COL_URL + " VARCHAR(512) UNIQUE, " +            //1
                COL_WEBSITE + " VARCHAR(64) NOT NULL, " +       //2
                COL_FAVOURITE + " BOOLEAN NOT NULL, " +         //3
                COL_STATUS + " INTEGER NOT NULL, " +            //4
                COL_IMG + " BLOB NOT NULL, " +
                COL_CUSTOM + " BOOLEAN NOT NULL)" ;                   //5, blob is an image type
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this works only if the database version is newer
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addMovie(String name, String url, String webSite, byte[] img, boolean custom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();         //save values in this variable
        cv.put(COL_NAME, name);
        cv.put(COL_URL, url);
        cv.put(COL_WEBSITE, webSite);
        cv.put(COL_FAVOURITE, Boolean.FALSE);
        cv.put(COL_FAVOURITE, 0);
        cv.put(COL_IMG, img);
        cv.put(COL_CUSTOM, custom);
        cv.put(COL_STATUS, 0);
        return db.insert(TABLE_NAME, null, cv) != -1;

    }

    public boolean removeMovie(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_NAME + " = '" + name + "'", null) != 0;
    }

    public boolean changeStatus(String name, int newStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", newStatus);
        return db.update(TABLE_NAME, cv, COL_NAME + " = '"+ name +"'", null) != 0;
    }

    public boolean changeFavourite(String name, boolean newFav){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("favourite", newFav);
        return db.update(TABLE_NAME, cv, COL_NAME + " = '"+ name +"'", null) != 0;
    }

    public Cursor selectAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor applyFilters(String name, String webSite, int favorite, int status){
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM " + TABLE_NAME + " WHERE ");
        int cont = 0;

        if(!name.equals("")){
            query.append(COL_NAME + " LIKE '" + name + "%' ");
            cont++;
        }

        if(!webSite.equals("")){
            addAndToQuery(cont, query);
            if(!webSite.equals("Others"))
                query.append(COL_WEBSITE + " = '" + webSite + "' ");
            else
                query.append(COL_CUSTOM + " = TRUE ");
            cont++;
        }

        if(favorite != -1){
            addAndToQuery(cont, query);
            boolean fav = favorite == 1 ? Boolean.TRUE : Boolean.FALSE;
            query.append(COL_FAVOURITE + " = " + fav + " ");
            cont++;
        }

        if(status != -1){
            addAndToQuery(cont, query);
            query.append(COL_STATUS + " = " + status);
            cont++;
        }

        query.append(";");

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query.toString(), null);
    }

    private void addAndToQuery(int cont, StringBuilder query){
        if(cont > 0)
            query.append("AND ");
    }

}
