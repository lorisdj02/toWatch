package it.digirolamopescetti.towatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements RemoveDialog.DialogListener {

    FloatingActionButton btnAdd, btnRemove, btnNext, btnPrev;
    TextView outName, outWebSite;
    ImageView outImg;
    ImageButton btnFavourite, btnStatus;
    Movie curMovie;
    dbHelper db = new dbHelper(this);
    Cursor cursor;

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

        initData();
    }

    private void startBtnStatus() {
        btnStatus = findViewById(R.id.btnStatus);

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (curMovie.getStatus()) {
                    case 0:
                        showText("Watching");
                        curMovie.setStatus(curMovie.getStatus() + 1);
                        break;
                    case 1:
                        showText("Watched");
                        curMovie.setStatus(curMovie.getStatus() + 1);
                        break;
                    case 2:
                        showText("To watch");
                        curMovie.setStatus(0);
                        break;
                }
                db.changeStatus(curMovie.getName(), curMovie.getStatus());
                updateData();
                displayData();
            }
        });
    }

    private void startBtnFav(){
        btnFavourite = findViewById(R.id.btnFavourite);

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curMovie.getFavourite()) {
                    curMovie.setFavourite(Boolean.FALSE);
                    showText(curMovie.getName() + " is no longer a favorite.");
                }
                else {
                    curMovie.setFavourite(Boolean.TRUE);
                    showText(curMovie.getName() + " is a favorite!");
                }
                db.changeFavourite(curMovie.getName(), curMovie.getFavourite());
                updateData();
                displayData();
            }
        });
    }

    private void startBtnNext(){
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cursor.getCount() == 0){
                    showText("No movies!");
                }
                else if(!cursor.moveToNext())
                    cursor.moveToFirst();
                displayData();
            }
        });
    }

    private void startBtnPrev(){
        btnPrev = findViewById(R.id.btnPrev);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cursor.getCount() == 0){
                    showText("No movies");
                }
                else if(!cursor.moveToPrevious())
                    cursor.moveToLast();
                displayData();
            }
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

    private void updateData(){
        Cursor tempCursor = cursor;
        cursor = db.selectAll();
        cursor.moveToPosition(tempCursor.getPosition());
    }

    @SuppressLint("SetTextI18n")
    private void displayData(){
        if(cursor.getCount() == 0){
            //if TABLE is empty, disable
            outWebSite.setText("");
            outName.setText("No movies.");
            btnFavourite.setEnabled(false);
            btnStatus.setEnabled(false);
            btnRemove.setEnabled(false);
        }
        else{
            //get data
            outImg.setEnabled(true);
            btnFavourite.setEnabled(true);
            btnStatus.setEnabled(true);
            btnRemove.setEnabled(true);

            Bitmap bm = BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length);          //transform ByteArray into bitmap
            curMovie = new Movie(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, cursor.getInt(4), bm);

            outName.setText(curMovie.getName());
            outWebSite.setText(curMovie.getWebsite());
            outImg.setImageBitmap(curMovie.getImg());
            switch (curMovie.getStatus()){
                case 0:
                    btnStatus.setBackgroundColor(Color.RED);
                    break;
                case 1:
                    btnStatus.setBackgroundColor(Color.YELLOW);
                    break;
                case 2:
                    btnStatus.setBackgroundColor(Color.GREEN);
                    break;
            }

            if(curMovie.getFavourite() == Boolean.TRUE)
                btnFavourite.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.star_on));
            else
                btnFavourite.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.star_off));
        }
    }

    private void startBtnAdd(){
        //config Add (floating) button
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddMovie.class));
                finish();
            }
        });
    }

    private void startBtnRemove(){
        //config Remove (floating) button
        btnRemove = (FloatingActionButton) findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRemDialog();
            }
        });
    }

    private void openRemDialog(){
        RemoveDialog dialog = new RemoveDialog();
        dialog.show(getSupportFragmentManager(), "RemoveDialog");
    }

    @Override
    public void confirmRemove() {
        if(db.removeMovie(curMovie.getName()))
            showText(curMovie.getName() + " removed.");
        else
            showText("Error removing " + curMovie.getName());
        initData();
    }

    private void showText(String string){
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }
}