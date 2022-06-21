package it.digirolamopescetti.towatch;

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
                //Toast.makeText(AddMovie.this, URL, Toast.LENGTH_SHORT).show();        DIALOG

                if(URL.contains("www.netflix.com/") && URL.contains("/title/")){
                    //SCRIPT
                }
                else
                    Toast.makeText(AddMovie.this, "Insert netflix URL!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void startSpnWebSite(){

        spnWebSite = findViewById(R.id.spnWebSite);
        ArrayAdapter<CharSequence> adapterWebSite = ArrayAdapter.createFromResource(this, R.array.websites, android.R.layout.simple_spinner_item);
        adapterWebSite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWebSite.setAdapter(adapterWebSite);
        spnWebSite.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
        //nothing
    }

    private void startBtnConfirm(){
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //connect to database
            }
        });
    }

    private void startBtnCancel(){
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startImg(){
        inImg = (ImageButton) findViewById(R.id.inImg);
        inImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choose Image"), IMAGE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Uri uri = data.getData();
            inImg.setImageURI(uri);
            inImg.setBackgroundColor(WHITE);
        }
    }
}