package it.digirolamopescetti.towatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//this activity is used to select movies applying certain filters to SELECT
public class FilterMovies extends AppCompatActivity {

    Button btnCancel;
    Button btnApply;
    EditText inName;
    RadioGroup groupWebSite, groupFavorite;
    RadioButton selectedWeb, selectedFavorite;
    ImageButton btnStatus;

    //these are default values to know if these information are useless or not
    String name = "";
    int status = -1;
    String webSite = "";
    int favorite = -1;

    //these are KEYS to communicate with MainActivity
    public static final String keyName = "_NAME";
    public static final String keyStatus = "_STATUS";
    public static final String keyWebSite = "_WEBSITE";
    public static final String keyFavorite = "_FAVORITE";
    public static final String keyApply = "_APPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        darkCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_movies);

        startBtnStatus();
        startBtnCancel();
        startBtnApply();

    }

    private void startBtnStatus(){
        //simple function to change status and color of the square
        btnStatus = findViewById(R.id.selectStatus);

        btnStatus.setOnClickListener(v -> {
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
        });
    }

    private void startBtnCancel(){
        //if the user doesn't want filters, go back (and reset SELECT)
        btnCancel = findViewById(R.id.btnSelectCancel);

        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(FilterMovies.this, MainActivity.class));
            finish();
        });
    }

    private void startBtnApply(){
        //apply all filters and send it to main activity
        btnApply = findViewById(R.id.btnSelectApply);
        btnStatus = findViewById(R.id.selectStatus);
        inName = findViewById(R.id.selectName);
        groupWebSite = findViewById(R.id.selectWebSite);
        groupFavorite = findViewById(R.id.selectFavorite);


        btnApply.setOnClickListener(v -> {
            //controls if a website is selected, if yes save it
            if (findViewById(groupWebSite.getCheckedRadioButtonId()) != null) {
                selectedWeb = findViewById(groupWebSite.getCheckedRadioButtonId());
                if(selectedWeb.getText().equals(getString(R.string.custom)))
                    webSite = "Others";
                else
                    webSite = selectedWeb.getText().toString();
            }
            //controls if favorite is checked, if yes change it to 1 (TRUE) or 0 (FALSE)
            if (findViewById(groupFavorite.getCheckedRadioButtonId()) != null){
                selectedFavorite = findViewById(groupFavorite.getCheckedRadioButtonId());
                if(selectedFavorite.getText().equals(getString(R.string.favorite))){
                    favorite = 1;
                }
                else{
                    favorite = 0;
                }
            }
            name = inName.getText().toString().trim();

            //i apply filters even if one of this is checked.
            if(!(name.equals("") && status == -1 && webSite.equals("") && favorite == -1)){
                Intent intent = new Intent(FilterMovies.this, MainActivity.class);

                //.putExtra is used to communicate with the intent (MainActivity)
                intent.putExtra(keyName, name);
                intent.putExtra(keyWebSite, webSite);
                intent.putExtra(keyFavorite, favorite);
                intent.putExtra(keyStatus, status);
                intent.putExtra(keyApply, true);
                startActivity(intent);
                finish();
            }
            else {
                Sirol.showText(FilterMovies.this,getString(R.string.noFilters));

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
}