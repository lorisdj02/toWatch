package it.digirolamopescetti.towatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements RemoveDialog.DialogListener {

    FloatingActionButton btnAdd, btnRemove, btnNext, btnPrev, btnSort;
    TextView outName, outWebSite;
    ImageView outImg;
    ImageButton btnFavourite, btnStatus;
    Movie curMovie;
    dbHelper db = new dbHelper(this);
    Cursor cursor;

    String recName = "";
    String recWebSite = "";
    int recFavorite = -1;
    int recStatus = -1;
    boolean applyFilter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startBtnAdd();              //use function to init layouts things (devo scrivere in inglese sennò mi da ste cose verdi)
        startBtnRemove();
        startBtnFav();
        startBtnStatus();
        startBtnNext();
        startBtnPrev();
        startBtnSort();
        startImg();

        initData();

        receiveFilters();

        getMoviesCount();
    }

    private void startBtnAdd(){
        //config Add (floating) button
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddMovie.class));
            finish();
        });
    }

    private void startBtnRemove(){
        //config Remove (floating) button
        btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(view -> openRemDialog());
    }



    private void startBtnFav(){
        btnFavourite = findViewById(R.id.btnFavourite);

        btnFavourite.setOnClickListener(v -> {
            if(curMovie.getFavourite()) {
                curMovie.setFavourite(Boolean.FALSE);
                Sirol.showText(MainActivity.this,curMovie.getName() + " is no longer a favorite.");
            }
            else {
                curMovie.setFavourite(Boolean.TRUE);
                Sirol.showText(MainActivity.this,curMovie.getName() + " is a favorite!");
            }
            if(!db.changeFavourite(curMovie.getName(), curMovie.getFavourite()))
                Sirol.showText(MainActivity.this, "Error in favorite.");
            else{
                if(!applyFilter)
                    updateData();
                else
                    updateData(recName, recWebSite, recFavorite, recStatus, true);
                displayData();
            }
        });
    }

    private void startBtnStatus() {
        btnStatus = findViewById(R.id.btnStatus);

        btnStatus.setOnClickListener(v -> {

            switch (curMovie.getStatus()) {
                case 0:
                    Sirol.showText(MainActivity.this,"Watching");
                    curMovie.setStatus(curMovie.getStatus() + 1);
                    break;
                case 1:
                    Sirol.showText(MainActivity.this,"Watched");
                    curMovie.setStatus(curMovie.getStatus() + 1);
                    break;
                case 2:
                    Sirol.showText(MainActivity.this,"To watch");
                    curMovie.setStatus(0);
                    break;
            }
            if(!db.changeStatus(curMovie.getName(), curMovie.getStatus()))
                Sirol.showText(MainActivity.this, "Error in status.");
            else {
                if (!applyFilter)
                    updateData();
                else
                    updateData(recName, recWebSite, recFavorite, recStatus, true);
                displayData();
            }
        });
    }

    private void startBtnNext(){
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            if(!cursor.moveToNext())
                cursor.moveToFirst();
            displayData();
        });
    }

    private void startBtnPrev(){
        btnPrev = findViewById(R.id.btnPrev);

        btnPrev.setOnClickListener(v -> {
            if(!cursor.moveToPrevious())
                cursor.moveToLast();
            displayData();
        });
    }

    private void startBtnSort(){
        btnSort = findViewById(R.id.btnSort);

        btnSort.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FilterMovies.class));
            finish();
        });
    }

    private void startImg(){
        outImg = findViewById(R.id.outImg);

        outImg.setOnClickListener(v -> {
            if(curMovie.getUrl() != null)
                gotoUrl(curMovie.getUrl());
            else
                Sirol.showText(MainActivity.this, "No url for " + curMovie.getName() + "!");
        });
    }

    private void initData(){
        cursor = db.selectAll();     //put in cursor SELECT *

        outName = findViewById(R.id.outName);
        outWebSite = findViewById(R.id.outWebSite);
        outImg = findViewById(R.id.outImg);
        cursor.moveToNext();
        displayData();
    }

    private void receiveFilters(){
        Intent intent = getIntent();
        applyFilter = intent.getBooleanExtra(FilterMovies.keyApply, false);

        if(applyFilter) {
            recName = intent.getStringExtra(FilterMovies.keyName);
            recWebSite = intent.getStringExtra(FilterMovies.keyWebSite);
            recFavorite = intent.getIntExtra(FilterMovies.keyFavorite, -1);
            recStatus = intent.getIntExtra(FilterMovies.keyStatus, -1);

            updateData(recName, recWebSite, recFavorite, recStatus, false);
            displayData();
        }
    }
    @SuppressLint("SetTextI18n")
    private void displayData(){
        if(cursor.getCount() == 0){
            //if TABLE is empty, disable
            outWebSite.setText("");
            outName.setText("No movies.");
            if(applyFilter)
                outName.setText("No results.");
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
                btnNext.setEnabled(false);
                btnPrev.setEnabled(false);
            }
        }

    }

    private void updateData(){
        Cursor tempCursor = cursor;
        cursor = db.selectAll();
        cursor.moveToPosition(tempCursor.getPosition());
    }

    private void updateData(String name, String webSite, int favorite, int status, boolean savePosition){
        Cursor tempCursor = cursor;
        cursor = db.applyFilters(name, webSite, favorite, status);
        if(savePosition){
            cursor.moveToPosition(tempCursor.getPosition());
        }
        else
            cursor.moveToPosition(0);
    }

    private void onOff(boolean x){
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
        Uri url = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, url));
    }

    private void openRemDialog(){
        RemoveDialog dialog = new RemoveDialog();
        dialog.show(getSupportFragmentManager(), "RemoveDialog");
    }

    @Override
    public void confirmRemove() {
        if(db.removeMovie(curMovie.getName()))
            Sirol.showText(MainActivity.this,curMovie.getName() + " removed.");
        else
            Sirol.showText(MainActivity.this,"Error removing " + curMovie.getName());

        initData();
        getMoviesCount();
    }

    private void getMoviesCount(){
        if(cursor.getCount() == 1)
            Sirol.showText(MainActivity.this," 1 movie founded.");
        else
            Sirol.showText(MainActivity.this,cursor.getCount() + " movies founded.");
    }
}