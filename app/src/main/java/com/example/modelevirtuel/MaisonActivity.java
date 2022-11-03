package com.example.modelevirtuel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MaisonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maison);

        // On bloque en mode portrait
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == R.id.quitter) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.pageacceuille) {
            Intent ic = new Intent(MaisonActivity.this, MainActivity.class);
            startActivity(ic);

            return true;
        }
        return false;
    }
}