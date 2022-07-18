package it.digirolamopescetti.towatch;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

//https://media.netflix.com/it/only-on-netflix/IDNETFLIX to retrieve images

//this is the activity used to add a new movie to the db.
public class AddMovie extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spnWebSite;
    private Button btnConfirm, btnCancel, btnUrl;
    private EditText inWebSite, inUrl, inName;
    private ImageButton inImg;
    private String imgPath = null;
    String webSite;
    dbHelper db;
    boolean custom = false;
    boolean queryRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        darkCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        //init layout
        startBtnUrl();
        startSpnWebSite();
        startBtnCancel();
        startBtnConfirm();
        startImg();
    }


    private void startBtnUrl(){
        //When i click this button, i will add automatically AddMovie layout movie's name, lock the spinner with Netflix
        //download the image and set ImageButton background to that image
        inUrl = findViewById(R.id.inUrl);
        btnUrl = findViewById(R.id.btnUrl);

        btnUrl.setOnClickListener(view -> {
            if(inUrl.getText().toString().trim().contains("www.netflix.com/") && inUrl.getText().toString().trim().contains("/title/")){
                //SCRIPT
            }
            else
                Sirol.showText(AddMovie.this,getString(R.string.errorNetflixUrl));        //use this to create popups
            });
    }

    private void startSpnWebSite(){
        //to attach the spinner to String array in strings.xml called "websites" (Netflix, HBO...)
        spnWebSite = findViewById(R.id.spnWebSite);
        ArrayAdapter<CharSequence> adapterWebSite = ArrayAdapter.createFromResource(this, R.array.websites, android.R.layout.simple_spinner_item);
        adapterWebSite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWebSite.setAdapter(adapterWebSite);
        spnWebSite.setOnItemSelectedListener(this); //you need to implement AdapterView.OnItemSelectedListener
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //when you select an item, check if it is Custom or not (if you implement AdapterView.OnItemSelectedListener
        // you have to implement these onItemSelected and onNothingSelected)
        inWebSite = findViewById(R.id.inWebSite);
        String webSiteTXT = adapterView.getItemAtPosition(i).toString();
        if(webSiteTXT.equals(getString(R.string.customAdd)))
            inWebSite.setEnabled(true);
        else {
            inWebSite.setEnabled(false);
            webSite = webSiteTXT;
        }
        inWebSite.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //you can't select nothing, impossible to use this function
    }

    private void startBtnConfirm(){
        //config Confirm Button
        btnConfirm = findViewById(R.id.btnConfirm);
        inWebSite = findViewById(R.id.inWebSite);
        inName = findViewById(R.id.inName);
        spnWebSite = findViewById(R.id.spnWebSite);
        inImg = findViewById(R.id.inImg);
        db = new dbHelper(AddMovie.this);

        btnConfirm.setOnClickListener(view -> {
            //insert data into database (after checked that the variables are not empty)

            if(!inName.getText().toString().trim().isEmpty() && (!inWebSite.getText().toString().trim().isEmpty() || !inWebSite.isEnabled()) && imgPath != null){
                if(inWebSite.isEnabled()) {
                    //custom becomes true if "Custom" is selected, i added this variable to simplify
                    //SELECT query.
                    webSite = inWebSite.getText().toString();
                    custom = true;
                }

                //url can be NULL -> if url is null, .toString() generate error (can't convert null to String)
                //so i do this simple control
                if(inUrl.getText().toString().trim().equals("") || inUrl.getText().toString().isEmpty())
                    queryRes = db.addMovie(inName.getText().toString().trim(), null, webSite.trim(), Sirol.imgToByte(inImg), custom);
                else
                    queryRes = db.addMovie(inName.getText().toString().trim(), inUrl.getText().toString().trim(), webSite.trim(), Sirol.imgToByte(inImg), custom);

                //queryRes -> 1 if the movie is added (TRUE) OR 0 if not (FALSE)
                if(queryRes){
                    Sirol.showText(AddMovie.this,getString(R.string.addedMovie));
                    back();
                }
                else
                    Sirol.showText(AddMovie.this,getString(R.string.errorAdd));
            }
            else
                Sirol.showText(AddMovie.this,getString(R.string.fillFieldsAdd));
        });
    }

    private void startBtnCancel(){
        //config Cancel button, simply close the activity and come back to main activity
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> back());
    }

    private void back(){
        //used to simplify code, open new Intent (back to MainActivity) and close this page
        startActivity(new Intent(AddMovie.this, MainActivity.class));
        finish();
    }

    private void startImg(){
        //config Image Button
        //tutorial https://www.youtube.com/watch?v=6E5ODrmUtmo
        inImg = findViewById(R.id.inImg);
        ActivityResultLauncher<String> takeImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {                 //before was 'new ActivityResultCallback<Uri>()' now is lambda
                    if (result != null && !result.equals(Uri.EMPTY)) {
                        inImg.setImageURI(result);
                        imgPath = result.getPath();
                        //i set background to transparent to make prettier it.
                        inImg.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
        );
        inImg.setOnClickListener(view -> takeImage.launch("image/*"));      //before was 'new View.OnClickListener()', now is lambda
    }

    private void darkCheck(){
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.Theme_DarkMode);
        else
            setTheme(R.style.Theme_Light);
    }
}