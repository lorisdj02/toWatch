package it.digirolamopescetti.towatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    Button btnBack;
    ImageView imgLanguage;
    Switch swNight;
    boolean english = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        startBtnBack();
        startImgLanguage();
        startSwNight();

    }

    private void startBtnBack(){
        btnBack = findViewById(R.id.btnBackToMain);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(Settings.this, MainActivity.class));
            finish();
        });
    }

    private void startImgLanguage(){
        imgLanguage = findViewById(R.id.imgLanguage);

        imgLanguage.setOnClickListener(v -> {
            if(english){
                //change things to italian
                imgLanguage.setBackground(AppCompatResources.getDrawable(Settings.this, R.drawable.italian));
                english = false;
            }
            else{
                //change things to english
                imgLanguage.setBackground(AppCompatResources.getDrawable(Settings.this, R.drawable.english));
                english = true;
            }
        });
    }

    private  void startSwNight(){
        swNight = findViewById(R.id.swNight);

        //night mode, idk how to use switch
    }
}