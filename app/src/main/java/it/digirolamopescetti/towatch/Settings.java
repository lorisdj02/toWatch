package it.digirolamopescetti.towatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    Button btnBack;
    ImageView imgLanguage;
    SwitchCompat swNight;
    boolean english = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        darkCheck();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        english = !loadLocale().equals("it");

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

        if(english)
            imgLanguage.setBackground(AppCompatResources.getDrawable(Settings.this, R.drawable.english));
        else
            imgLanguage.setBackground(AppCompatResources.getDrawable(Settings.this, R.drawable.italian));

        imgLanguage.setOnClickListener(v -> {
            if(english){
                //change things to italian
                imgLanguage.setBackground(AppCompatResources.getDrawable(Settings.this, R.drawable.italian));
                english = false;
                setLocale("it");
                Sirol.showText(Settings.this, "Italiano");
            }
            else{
                //change things to english
                imgLanguage.setBackground(AppCompatResources.getDrawable(Settings.this, R.drawable.english));
                english = true;
                setLocale("en");
                Sirol.showText(Settings.this, "English");
            }
            recreate();
        });


    }

    private  void startSwNight(){
        swNight = findViewById(R.id.swNight);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            swNight.setChecked(true);

        swNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void darkCheck(){
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.Theme_DarkMode);
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.Theme_Light);
        }
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

    private String loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
        return language;
    }
}