package it.digirolamopescetti.towatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

//As the name says, this is the main activity of the app.
//IMPORTANT -> ALL STRINGS NEED TO BE IMPLEMENTED IN strings.xml TO BE USED WITH DIFFERENT LANGUAGES (penso sia così lol)
public class MainActivity extends AppCompatActivity implements RemoveDialog.DialogListener {

    FloatingActionButton btnAdd, btnRemove, btnNext, btnPrev, btnSort, btnSettings;
    TextView outName, outWebSite;
    ImageView outImg;
    ImageButton btnFavourite, btnStatus;
    //current movie (shown movie) is in this variable
    Movie curMovie;
    dbHelper db = new dbHelper(this);
    //Cursor -> it collects data stored in database using SELECT
    Cursor cursor;

    //these are the received values from the FilterMovies class
    String recName = "";
    String recWebSite = "";
    int recFavorite = -1;
    int recStatus = -1;
    //applyFilter -> when it's false, there are no filters to apply.
    boolean applyFilter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        darkCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadLocale();
        startBtnAdd();              //use function to init layouts things (devo scrivere in inglese sennò mi da ste cose verdi)
        startBtnRemove();
        startBtnFav();
        startBtnStatus();
        startBtnNext();
        startBtnPrev();
        startBtnSort();
        startImg();
        startBtnSettings();

        initData();

        receiveFilters();

        getMoviesCount();
    }

    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    private void startBtnAdd(){
        //config Add (floating) button -> open AddMovie class
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddMovie.class));
            finish();
        });
    }

    private void startBtnRemove(){
        //config Remove (floating) button -> open RemoveDialog class
        btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(view -> openRemDialog());
    }



    private void startBtnFav(){
        //change favorite (TRUE/FALSE) to current movie
        btnFavourite = findViewById(R.id.btnFavourite);

        btnFavourite.setOnClickListener(v -> {
            if(curMovie.getFavourite()) {
                curMovie.setFavourite(Boolean.FALSE);
                Sirol.showText(MainActivity.this,curMovie.getName() + " " + getString(R.string.isNoFav));
            }
            else {
                curMovie.setFavourite(Boolean.TRUE);
                Sirol.showText(MainActivity.this,curMovie.getName() + " " + getString(R.string.isFav));
            }
            if(!db.changeFavourite(curMovie.getName(), curMovie.getFavourite()))
                Sirol.showText(MainActivity.this, getString(R.string.errorFavoriteMain));
            else{
                //if it's false, it will display all data, else only the data we need
                if(!applyFilter)
                    updateData();
                else
                    updateData(recName, recWebSite, recFavorite, recStatus, true);
                displayData();
            }
        });
    }

    private void startBtnStatus() {
        //change status of current movie
        btnStatus = findViewById(R.id.btnStatus);

        btnStatus.setOnClickListener(v -> {

            switch (curMovie.getStatus()) {
                case 0:
                    Sirol.showText(MainActivity.this,getString(R.string.watching));
                    curMovie.setStatus(curMovie.getStatus() + 1);
                    break;
                case 1:
                    Sirol.showText(MainActivity.this,getString(R.string.watched));
                    curMovie.setStatus(curMovie.getStatus() + 1);
                    break;
                case 2:
                    Sirol.showText(MainActivity.this,getString(R.string.toWatch));
                    curMovie.setStatus(0);
                    break;
            }
            if(!db.changeStatus(curMovie.getName(), curMovie.getStatus()))
                Sirol.showText(MainActivity.this, getString(R.string.errorStatusMain));
            else {
                //the same as before
                if (!applyFilter)
                    updateData();
                else
                    updateData(recName, recWebSite, recFavorite, recStatus, true);
                displayData();
            }
        });
    }

    private void startBtnNext(){
        //show the next movie (or the first)
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            if(!cursor.moveToNext())
                cursor.moveToFirst();
            displayData();
        });
    }

    private void startBtnPrev(){
        //show the movie before (or the last)
        btnPrev = findViewById(R.id.btnPrev);

        btnPrev.setOnClickListener(v -> {
            if(!cursor.moveToPrevious())
                cursor.moveToLast();
            displayData();
        });
    }

    private void startBtnSort(){
        //open FilterMovies class
        btnSort = findViewById(R.id.btnSort);

        btnSort.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FilterMovies.class));
            finish();
        });
    }

    private void startImg(){
        //When you click image, open Chrome and go to url
        outImg = findViewById(R.id.outImg);

        outImg.setOnClickListener(v -> {
            if(curMovie.getUrl() != null)
                gotoUrl(curMovie.getUrl());
            else
                Sirol.showText(MainActivity.this, getString(R.string.noUrlMain) + " " + curMovie.getName() + "!");
        });
    }

    private void startBtnSettings(){
        btnSettings = findViewById(R.id.btnSettings);

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Settings.class));
            finish();
        });
    }

    private void initData(){
        //select all data in database
        cursor = db.selectAll();     //put in cursor SELECT *

        outName = findViewById(R.id.outName);
        outWebSite = findViewById(R.id.outWebSite);
        outImg = findViewById(R.id.outImg);
        cursor.moveToNext();
        displayData();
    }

    private void receiveFilters(){
        //receive filters from FilterMovies class
        Intent intent = getIntent();
        applyFilter = intent.getBooleanExtra(FilterMovies.keyApply, false);

        if(applyFilter) {
            //if it's necessary (so if you want filters) this if will be activated and through keys, you'll receive
            //all data you need
            recName = intent.getStringExtra(FilterMovies.keyName);
            recWebSite = intent.getStringExtra(FilterMovies.keyWebSite);
            recFavorite = intent.getIntExtra(FilterMovies.keyFavorite, -1);
            recStatus = intent.getIntExtra(FilterMovies.keyStatus, -1);

            updateData(recName, recWebSite, recFavorite, recStatus, false);
            displayData();
        }
    }

    private void displayData(){
        //to show current movie (if the cursor isn't empty)
        if(cursor.getCount() == 0){
            //if TABLE is empty, disable
            outWebSite.setText("");
            outName.setText(getString(R.string.noMovies));
            if(applyFilter)
                outName.setText(getString(R.string.noResults));
            else
                btnSort.setEnabled(false);
            onOff(false);

        }
        else{
            //get data
            onOff(true);
            btnSort.setEnabled(true);

            Bitmap bm = Sirol.blobToImg(cursor.getBlob(5));          //transform ByteArray into bitmap
            curMovie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, cursor.getInt(4), bm);

            outName.setText(curMovie.getName());
            outWebSite.setText(curMovie.getWebsite());
            outImg.setImageBitmap(curMovie.getImg());
            //stus is memorized with 0 -> red ; 1 -> yellow ; 2 -> green
            switch (curMovie.getStatus()){
                case 0:
                    btnStatus.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.status_red));
                    break;
                case 1:
                    btnStatus.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.status_yellow));
                    break;
                case 2:
                    btnStatus.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.status_green));
                    break;
            }

            if(curMovie.getFavourite() == Boolean.TRUE)
                btnFavourite.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.star_on));
            else
                btnFavourite.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.star_off));
            if(cursor.getCount() == 1){
                //if there is only 1 movie, disable buttons (just for aesthetics)
                btnNext.setEnabled(false);
                btnPrev.setEnabled(false);
            }
        }

    }

    private void updateData(){
        //refresh data but use same position
        Cursor tempCursor = cursor;
        cursor = db.selectAll();
        cursor.moveToPosition(tempCursor.getPosition());
    }

    private void updateData(String name, String webSite, int favorite, int status, boolean savePosition){
        //refresh data WITH FILTERS
        Cursor tempCursor = cursor;
        cursor = db.applyFilters(name, webSite, favorite, status);
        if(savePosition && tempCursor.getPosition() < cursor.getCount()){
            //if savePosition is true, you'll have the same position (check if is a valid position or not)
            cursor.moveToPosition(tempCursor.getPosition());
        }
        else
            cursor.moveToPosition(0);
    }

    private void onOff(boolean x){
        //disable/enable buttons...
        int visibility = x ? View.VISIBLE : View.INVISIBLE;
        btnFavourite.setEnabled(x);
        btnRemove.setEnabled(x);
        btnNext.setEnabled(x);
        btnPrev.setEnabled(x);
        outImg.setVisibility(visibility);
        btnFavourite.setVisibility(visibility);
        btnStatus.setVisibility(visibility);
    }

    private void gotoUrl(String s){
        //open a page from web through url
        Uri url = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, url));
    }

    private void openRemDialog(){
        //open RemoveDialog class
        RemoveDialog dialog = new RemoveDialog();
        dialog.show(getSupportFragmentManager(), "RemoveDialog");
    }

    @Override
    public void confirmRemove() {
        //this activates when you press 'YES' on RemoveDialog
        if(db.removeMovie(curMovie.getName()))
            Sirol.showText(MainActivity.this,curMovie.getName() + " " + getString(R.string.removed));
        else
            Sirol.showText(MainActivity.this,getString(R.string.errorRemove) + " " + curMovie.getName());

        applyFilter = false;
        initData();
        getMoviesCount();
    }

    private void getMoviesCount(){
        //show how many movies are shown
        if(cursor.getCount() == 1)
            Sirol.showText(MainActivity.this, getString(R.string.oneMovieFounded));
        else
            Sirol.showText(MainActivity.this,cursor.getCount() + " " + getString(R.string.moreMoviesFounded));
    }

    private void darkCheck(){
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.Theme_DarkMode);
        else
            setTheme(R.style.Theme_Light);
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        Log.println(Log.ERROR, "TAG", lang);
        editor.apply();
    }

}