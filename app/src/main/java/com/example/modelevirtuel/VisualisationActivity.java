package com.example.modelevirtuel;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.modelevirtuel.model.GestionnaireMaison;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VisualisationActivity extends AppCompatActivity {
    private ImageView imageView;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imageView = findViewById(R.id.photo_mur_visu);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_visu);
        surfaceView.setZOrderOnTop(true);


    }

    public void gauche(View view){

    }

    public void droite(View view){

    }
}