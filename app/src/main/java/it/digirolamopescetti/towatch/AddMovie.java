package it.digirolamopescetti.towatch;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

//https://media.netflix.com/it/only-on-netflix/IDNETFLIX to retrieve images

public class AddMovie extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spnWebSite;
    private Button btnConfirm, btnCancel, btnUrl;
    private EditText inWebSite, inUrl, inName;
    private ImageButton inImg;
    private final int WHITE = 0;      //color white
    private String imgPath = null;
    String webSite;
    dbHelper db;

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

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = inUrl.getText().toString();

                if(URL.contains("www.netflix.com/") && URL.contains("/title/")){
                    //SCRIPT
                    //add to database
                }
                else
                    showText("Insert netflix URL!");        //use this to create popups
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
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        inWebSite = findViewById(R.id.inWebSite);
        inName = findViewById(R.id.inName);
        spnWebSite = findViewById(R.id.spnWebSite);
        inImg = findViewById(R.id.inImg);
        db = new dbHelper(AddMovie.this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //insert data into database (after checked that the variables are not empty)
                if(!inName.getText().toString().trim().isEmpty() && (!inWebSite.getText().toString().trim().isEmpty() || !inWebSite.isEnabled()) && imgPath != null){
                    if(inWebSite.isEnabled())
                        webSite = inWebSite.getText().toString();

                    byte[] imgByte = imgToByte(inImg);
                    if(db.addMovie(inName.getText().toString().trim(), null, webSite.trim(), imgByte)){
                        showText("Movie added!");
                        back();
                    }
                    else
                        showText("Error adding movie!");
                }
                else
                    showText("Fill all fields!");
            }
        });
    }

    private byte[] imgToByte(ImageButton image){
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void startBtnCancel(){
        //config Cancel button, simply close the activity and come back to main activity
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               back();
            }
        });
    }

    private void back(){
        startActivity(new Intent(AddMovie.this, MainActivity.class));
        finish();
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
                        if (result != null && !result.equals(Uri.EMPTY)) {
                            inImg.setImageURI(result);
                            imgPath = result.getPath();
                            inImg.setBackgroundColor(WHITE);
                        }
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

    private void showText(String string){
        Toast.makeText(AddMovie.this, string, Toast.LENGTH_SHORT).show();
    }
}