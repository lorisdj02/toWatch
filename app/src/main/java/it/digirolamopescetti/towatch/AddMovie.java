package it.digirolamopescetti.towatch;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class AddMovie extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spnWebSite;
    private Button btnConfirm, btnCancel, btnUrl;
    private EditText inWebSite, inUrl, inName;
    private ImageButton inImg;
    private final int WHITE = 0;      //color white
    private String imgPath = null;
    String webSite;
    dbHelper db;
    boolean custom = false;
    boolean queryRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        inUrl = findViewById(R.id.inUrl);
        btnUrl = findViewById(R.id.btnUrl);

        btnUrl.setOnClickListener(view -> {
            if(inUrl.getText().toString().trim().contains("www.netflix.com/") && inUrl.getText().toString().trim().contains("/title/")){
                //SCRIPT
                //add to database
            }
            else
                Sirol.showText(AddMovie.this,"Insert netflix URL!");        //use this to create popups
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
                    webSite = inWebSite.getText().toString();
                    custom = true;
                }
                if(inUrl.getText().toString().trim().equals("") || inUrl.getText().toString().isEmpty())
                    queryRes = db.addMovie(inName.getText().toString().trim(), null, webSite.trim(), Sirol.imgToByte(inImg), custom);
                else
                    queryRes = db.addMovie(inName.getText().toString().trim(), inUrl.getText().toString().trim(), webSite.trim(), Sirol.imgToByte(inImg), custom);

                if(queryRes){
                    Sirol.showText(AddMovie.this,"Movie added!");
                    back();
                }
                else
                    Sirol.showText(AddMovie.this,"Error adding movie!");
            }
            else
                Sirol.showText(AddMovie.this,"Fill all fields!");
        });
    }

    private void startBtnCancel(){
        //config Cancel button, simply close the activity and come back to main activity
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> back());
    }

    private void back(){
        startActivity(new Intent(AddMovie.this, MainActivity.class));
        finish();
    }

    private void startImg(){
        //config Image Button
        //tutorial https://www.youtube.com/watch?v=6E5ODrmUtmo
        inImg = findViewById(R.id.inImg);
        //new ActivityResultCallback<Uri>()
        ActivityResultLauncher<String> takeImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {                 //before was 'new ActivityResultCallback<Uri>()' now is lambda
                    if (result != null && !result.equals(Uri.EMPTY)) {
                        inImg.setImageURI(result);
                        imgPath = result.getPath();
                        inImg.setBackgroundColor(WHITE);
                    }
                }
        );
        inImg.setOnClickListener(view -> takeImage.launch("image/*"));      //before was 'new View.OnClickListener()', now is lambda
    }
}