package com.example.modelevirtuel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Maison;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.MaisonAdapter;
import com.example.modelevirtuel.outils.PieceAdapter;

public class MaisonActivity extends AppCompatActivity {

    GestionnaireMaison listMaison;
    Maison ouvertMaison;
    PieceAdapter pieceAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maison);

        // On bloque en mode portrait
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        // On recupaire le gestionnaire
        listMaison = GestionnaireMaison.getInstance();
        ouvertMaison = listMaison.getSelectMaison();


        // On met le nom de la maison
        TextView nomText = findViewById(R.id.nom_maison);
        nomText.setText(String.valueOf(listMaison.getSelectMaison().getNom()));




        // Gere les invisible
        TextView aucunePiece = findViewById(R.id.nom_piece);
        ImageButton poubellePiece = findViewById(R.id.PoubellePiece);
        ImageView bousole = findViewById(R.id.boussole);
        ImageView i1 = findViewById(R.id.interrogationEst);
        ImageView i2 = findViewById(R.id.interrogationOuest);
        ImageView i3 = findViewById(R.id.interrogationNord);
        ImageView i4 = findViewById(R.id.interrogationSud);
        if(ouvertMaison.getListPiece().isEmpty()){
            poubellePiece.setVisibility(View.INVISIBLE);
            aucunePiece.setVisibility(View.VISIBLE);


            bousole.setVisibility(View.INVISIBLE);
            i1.setVisibility(View.INVISIBLE);
            i2.setVisibility(View.INVISIBLE);
            i3.setVisibility(View.INVISIBLE);
            i4.setVisibility(View.INVISIBLE);

        }else{
            poubellePiece.setVisibility(View.VISIBLE);
            aucunePiece.setVisibility(View.INVISIBLE);

            bousole.setVisibility(View.VISIBLE);
            i1.setVisibility(View.VISIBLE);
            i2.setVisibility(View.VISIBLE);
            i3.setVisibility(View.VISIBLE);
            i4.setVisibility(View.VISIBLE);

        }


        // Liste des maisons
        RecyclerView recycler =  findViewById(R.id.RecyPiece);
        pieceAdapt = new PieceAdapter(ouvertMaison.getListPiece());
        recycler.setAdapter(pieceAdapt);

        // Mettre la recy horizontalement
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(linearLayoutManager);


    }

    public void suppMaison(View view){
        finish();
        // On retourne sur la page d'acceuille car cette maison vas etre supprimer
        Intent ic = new Intent(MaisonActivity.this, MainActivity.class);
        startActivity(ic);

        listMaison.supprimerMaison(ouvertMaison);
    }

    public void suppPiece(View view){

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