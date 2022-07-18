package it.digirolamopescetti.towatch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import java.io.ByteArrayOutputStream;

//utility class, nothing special
public class Sirol {
    public static Bitmap blobToImg(byte[] img){
        //convert blob (saved in database) to an IMG
        return BitmapFactory.decodeByteArray(img,0,img.length);
    }

    public static void showText(Context context, String string){
        //show a popup on bottom of screen
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static byte[] imgToByte(ImageButton image){
        //convert an img of ImageButton to byte[] (so to blob)
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] imgToByte(ImageView image){
        //convert an img of ImageView to byte[] (so to blob)
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
