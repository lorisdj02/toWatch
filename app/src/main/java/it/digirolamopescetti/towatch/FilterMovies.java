package it.digirolamopescetti.towatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FilterMovies extends AppCompatActivity {

    Button btnCancel;
    Button btnApply;
    EditText inName;
    RadioGroup groupWebSite, groupFavorite;
    RadioButton selectedWeb, selectedFavorite;
    ImageButton btnStatus;

    String name = "";
    int status = -1;
    String webSite = "";
    int favorite = -1;

    public static final String keyName = "_NAME";
    public static final String keyStatus = "_STATUS";
    public static final String keyWebSite = "_WEBSITE";
    public static final String keyFavorite = "_FAVORITE";
    public static final String keyApply = "_APPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_movies);

        startBtnStatus();
        startBtnCancel();
        startBtnApply();

    }

    private void startBtnStatus(){
        btnStatus = findViewById(R.id.selectStatus);

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status){
                    case -1:
                        btnStatus.setBackgroundColor(Color.RED);
                        status++;
                        break;
                    case 0:
                        btnStatus.setBackgroundColor(Color.YELLOW);
                        status++;
                        break;
                    case 1:
                        btnStatus.setBackgroundColor(Color.GREEN);
                        status++;
                        break;
                    case 2:
                        btnStatus.setBackgroundColor(Color.BLUE);
                        status = -1;
                        break;
                }
            }
        });
    }

    private void startBtnCancel(){
        btnCancel = findViewById(R.id.btnSelectCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilterMovies.this, MainActivity.class));
                finish();
            }
        });
    }

    private void startBtnApply(){
        btnApply = findViewById(R.id.btnSelectApply);
        btnStatus = findViewById(R.id.selectStatus);
        inName = findViewById(R.id.selectName);
        groupWebSite = findViewById(R.id.selectWebSite);
        groupFavorite = findViewById(R.id.selectFavorite);


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(groupWebSite.getCheckedRadioButtonId()) != null) {
                    selectedWeb = findViewById(groupWebSite.getCheckedRadioButtonId());
                    webSite = selectedWeb.getText().toString();
                }
                if (findViewById(groupFavorite.getCheckedRadioButtonId()) != null){
                    selectedFavorite = findViewById(groupFavorite.getCheckedRadioButtonId());
                    if(selectedFavorite.getText().equals("Favorite")){
                        favorite = 1;
                    }
                    else{
                        favorite = 0;
                    }
                }
                name = inName.getText().toString().trim();

                if(!(name.equals("") && status == -1 && webSite.equals("") && favorite == -1)){
                    Intent intent = new Intent(FilterMovies.this, MainActivity.class);

                    intent.putExtra(keyName, name);
                    intent.putExtra(keyWebSite, webSite);
                    intent.putExtra(keyFavorite, favorite);
                    intent.putExtra(keyStatus, status);
                    intent.putExtra(keyApply, true);
                    startActivity(intent);
                    finish();
                }
                else {
                    showText("Filters are empty!");

                }
            }
        });
    }

    private void showText(String string){
        Toast.makeText(FilterMovies.this, string, Toast.LENGTH_SHORT).show();
    }
}