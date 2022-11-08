package com.example.modelevirtuel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.*;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Maison;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.MaisonAdapter;

public class MainActivity extends AppCompatActivity {
    GestionnaireMaison listMaison;
    Dialog dialog;
    TextView aucune;
    MaisonAdapter maisonAdapter;

    TextView numMaisonSelect;
    private static final int DIALOG_ALERT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On bloque en mode portrait
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );


        setContentView(R.layout.activity_main);
        listMaison = GestionnaireMaison.getInstance();

         aucune = findViewById(R.id.AucunEnregistrer);

        if(listMaison.getListMaison().isEmpty()){
            aucune.setVisibility(View.VISIBLE);
        }else{
            aucune.setVisibility(View.INVISIBLE);
        }


        // Liste des maisons
        RecyclerView recycler =  findViewById(R.id.RecyMaison);
        maisonAdapter = new MaisonAdapter(listMaison);
        recycler.setAdapter(maisonAdapter);

        // Mettre la recy horizontalement
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(linearLayoutManager);


    }

    public void maisonSelectionner(View view){
         numMaisonSelect =  view.findViewById(R.id.item_num_maison);

        showDialog(DIALOG_ALERT);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_mode);

        dialog.show();

    }

    public void construire(View view){

        dialog.cancel();

        int id =Integer.parseInt((String) numMaisonSelect.getText()) ;
        listMaison.setSelectMaison(listMaison.getMaison(id));

        // Ouvrir la page avec la maison
        Intent ic = new Intent(MainActivity.this, MaisonActivity.class);
        startActivity(ic);
    }



    public void visualiser(View view){
        dialog.cancel();
    }



    public void ajouterMaison(View view){
        String id = null;

        showDialog(DIALOG_ALERT);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_nom_maison);

        dialog.show();
    }


    public void continuer(View view){
        dialog.cancel();

        String nom = null;
        EditText editText = dialog.findViewById(R.id.username);
        editText.getText();
        nom = editText.getText().toString().trim();

        int num =FabriqueIdentifiant.getInstance().getIdMaison();
        listMaison.ajouterUneMaison(new Maison(nom, num));

        dialog.cancel();

        if(listMaison.getListMaison().isEmpty()){
            aucune.setVisibility(View.VISIBLE);
        }else{
            aucune.setVisibility(View.INVISIBLE);
        }

        // Mise a jour de la Recy
        maisonAdapter.notifyDataSetChanged();
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