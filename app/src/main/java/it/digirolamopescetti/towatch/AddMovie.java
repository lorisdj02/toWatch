package it.digirolamopescetti.towatch;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//https://media.netflix.com/it/only-on-netflix/IDNETFLIX to retrieve images

public class AddMovie extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    final int IMAGE_CODE = 1;
    Spinner spnWebSite;
    Button btnConfirm, btnCancel, btnUrl;
    TextView inWebSite, inUrl;
    ImageButton inImg;
    final int WHITE = 0;      //color white

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        //init layout
        startBtnUrl();
        startSpnWebSite();
        startBtnConfirm();
        startBtnCancel();
        startImg();
    }

    private void startBtnUrl(){
        inUrl = findViewById(R.id.inUrl);
        btnUrl = findViewById(R.id.btnUrl);

        btnUrl.setEnabled(true);
        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = inUrl.getText().toString();

                if(URL.contains("www.netflix.com/") && URL.contains("/title/")){
                    //SCRIPT
                    //add to database
                }
                else
                    Toast.makeText(AddMovie.this, "Insert netflix URL!", Toast.LENGTH_SHORT).show();        //use this to create popups
                }
            });
    }

    private void startSpnWebSite(){
        //to attach the spinner to String array in strings.xml called "websites"
        spnWebSite = findViewById(R.id.spnWebSite);
        ArrayAdapter<CharSequence> adapterWebSite = ArrayAdapter.createFromResource(this, R.array.websites, android.R.layout.simple_spinner_item);
        adapterWebSite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWebSite.setAdapter(adapterWebSite);
        spnWebSite.setOnItemSelectedListener(this); //you need to implement AdapterView.OnItemSelectedListener
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //when you select an item, check if it is Custom or not (if you implement AdapterView.OnItemSelectedListener you have to implement these onItemSelected and onNothingSelected)
        inWebSite = findViewById(R.id.inWebSite);
        String webSiteTXT = adapterView.getItemAtPosition(i).toString();
        if(webSiteTXT.equals("Custom"))
            inWebSite.setEnabled(true);
        else
            inWebSite.setEnabled(false);
        inWebSite.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //you can't select nothing, impossible to use this function
    }

    private void startBtnConfirm(){
        //config Confirm Button
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert data into database (after checked that the variables are not empty)
            }
        });
    }

    private void startBtnCancel(){
        //config Cancel button, simply close the activity and come back to main activity
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startImg(){
        //config Image Button
        //tutorial https://www.youtube.com/watch?v=6E5ODrmUtmo
        inImg = (ImageButton) findViewById(R.id.inImg);
        ActivityResultLauncher<String> takeImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        inImg.setImageURI(result);
                        inImg.setBackgroundColor(WHITE);
                    }
                }
        );
        inImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage.launch("image/*");
            }
        });
    }

}