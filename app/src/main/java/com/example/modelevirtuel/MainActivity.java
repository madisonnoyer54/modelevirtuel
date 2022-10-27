package com.example.modelevirtuel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Maison;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    GestionnaireMaison maison;
    Dialog dialog;
    private static final int DIALOG_ALERT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maison = new GestionnaireMaison();

    }


    public void ajouterMaison(View view){
        String id = null;

        showDialog(DIALOG_ALERT);
      //  Maison maison = new Maison(id,nom);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_signin);

        dialog.show();

    }

    public void continuer(View view){
        String nom = null;
        EditText editText = dialog.findViewById(R.id.username);
        editText.getText();
        nom = editText.getText().toString().trim();
        Toast.makeText((Context) MainActivity.this, (CharSequence) nom, Toast.LENGTH_SHORT).show();

        maison.ajouterUneMaison(new Maison(nom, FabriqueIdentifiant.getInstance().getIdMaison()));

        dialog.cancel();
    }

    public void annuler(View view){
        dialog.cancel();
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
            Toast.makeText((Context) MainActivity.this,"Vous etes déja sur la page d'acceuille", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }



}