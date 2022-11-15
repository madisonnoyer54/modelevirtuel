package com.example.modelevirtuel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.*;
import com.example.modelevirtuel.model.*;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.MaisonAdapter;
import com.example.modelevirtuel.outils.Orientation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements Observateur{
    GestionnaireMaison listMaison;
    Dialog dialog;
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

        // Liste des maisons
        RecyclerView recycler =  findViewById(R.id.RecyMaison);
        maisonAdapter = new MaisonAdapter(listMaison);
        recycler.setAdapter(maisonAdapter);

        // Mettre la recy horizontalement
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(linearLayoutManager);

        // Ajouter les enregistrement des maison
        try {
            lireEnregistrement(listMaison);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        listMaison.ajouterObservateur(this);
        listMaison.notifierObservateur();

    }

// CE QUI CONSERNE LA SELECTION DE LA MAISON
    /**
     * Fonction qui selectionne la maison, sa ouvre un dialogue
     * @param view
     */
    public void maisonSelectionner(View view){
         numMaisonSelect =  view.findViewById(R.id.item_num_maison);

        showDialog(DIALOG_ALERT);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_mode);

        dialog.show();


        listMaison.notifierObservateur();
    }


    /**
     * Fonction qui permet d'accèder a la construction de la maison selectionner
     * @param view
     */
    public void construire(View view){
        dialog.cancel();

        int id =Integer.parseInt((String) numMaisonSelect.getText()) ;
        listMaison.setSelectMaison(listMaison.getMaison(id));

        // Ouvrir la page avec la maison
        Intent ic = new Intent(MainActivity.this, MaisonActivity.class);
        startActivity(ic);
    }


    /**
     * Fonction qui permet de visualiser la maison selectionner
     * @param view
     */
    public void visualiser(View view){
        dialog.cancel();
    }


    // CE QUI CONSERNE L'AJOUT DES MAISON

    /**
     * Fonction qui ouvre le dialogue pour l'ajout des maisons (gerer avec les 2 autre fonction obligatoirement
     * @param view
     */
    public void ajouterMaison(View view){
        String id = null;

        showDialog(DIALOG_ALERT);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_nom_maison);

        dialog.show();
    }


    /**
     * Fonction qui permet de d'ajouter officieement la maison
     * @param view
     */
    public void continuer(View view){
        dialog.cancel();

        String nom = null;
        EditText editText = dialog.findViewById(R.id.username);
        editText.getText();
        nom = editText.getText().toString().trim();

        listMaison.ajouterUneMaison(nom);

        dialog.cancel();

        listMaison.notifierObservateur();
    }


    /**
     *  Fonction qui annule le dialogue vue au dessus
     * @param view
     */
    public void annuler(View view){
        dialog.cancel();
    }


    /**
     * Fonction reagir, qui permet de mettre a jour l'affichage graphique
     */
    @Override
    public void reagir() {
        TextView aucune = findViewById(R.id.AucunEnregistrer);

        if(listMaison.getListMaison().isEmpty()){
            aucune.setVisibility(View.VISIBLE);
        }else{
            aucune.setVisibility(View.INVISIBLE);
        }

        maisonAdapter.notifyDataSetChanged();

    }



    // SE QUI CONSERNE L'ENREGISTREMENT

    /**
     * Lecture de l'enregistrement
     * @throws FileNotFoundException
     */
    public void lireEnregistrement(GestionnaireMaison list) throws FileNotFoundException {
        FileOutputStream fOut = openFileOutput("sauvegarde.json", Context.MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject();


    }


    /**
     * Enregistrement
     * @throws IOException
     * @throws JSONException
     */
    public void enregistrement() throws IOException, JSONException {
        FileOutputStream fOut = openFileOutput("sauvegarde.json", Context.MODE_PRIVATE);
        fOut.write(maisonJSON().toString().getBytes());
        fOut.close();
    }


    /**
     * Enregistrement de la liste des maison
     * @return
     * @throws JSONException
     */
    public JSONArray maisonJSON() throws JSONException{
        JSONArray JSONListMaison = new JSONArray();
        Maison m;
        Iterator<Maison> maison = listMaison.iterator();
        while (maison.hasNext()) {
            m = maison.next();
            JSONObject jsonPorte = new JSONObject();
            jsonPorte.put("nom", m.getNom());
            jsonPorte.put("id", m.getId());
            jsonPorte.put("listePiece", pieceJSON(m));
            JSONListMaison.put(jsonPorte);
        }
        return JSONListMaison;
    }


    /**
     * Enregistrement de la liste des porte
     * @param m
     * @return
     * @throws JSONException
     */
    public JSONArray porteJSON(Mur m) throws JSONException {
        JSONArray JSONListPorte = new JSONArray();
        Porte p;
        Iterator<Porte> porte = m.iterator();
        while (porte.hasNext()) {
            p = porte.next();
            JSONObject jsonPorte = new JSONObject();
            jsonPorte.put("arriver", p.getArriver());
            jsonPorte.put("id", p.getId());
            JSONListPorte.put(jsonPorte);
        }
        return JSONListPorte;
    }


    /**
     * Enregistrement de la liste des murs
     * @param p
     * @return
     * @throws JSONException
     */
    public JSONArray mursJSON(Piece p) throws JSONException {
        JSONArray JSONListMurs = new JSONArray();
        Mur m;

        for (int i = 0; i < p.getListMur().length; i++) {
            m = p.getListMur()[i];
            JSONObject jsonMurs = new JSONObject();
            jsonMurs.put("listPorte", porteJSON(m));
            jsonMurs.put("orientation", m.getOrientation());
            jsonMurs.put("photoPrise", m.getPhotoPrise());
            jsonMurs.put("nom", m.getNom());
            JSONListMurs.put(jsonMurs);
        }

        return JSONListMurs;
    }


    /**
     * Enregistrement de la liste des porte
     * @param m
     * @return
     * @throws JSONException
     */
    public JSONArray pieceJSON(Maison m) throws JSONException {

        JSONArray JSONListPorte = new JSONArray();
        Piece p;
        Iterator<Piece> piece = m.iterator();
        while (piece.hasNext()) {
            p = piece.next();
            JSONObject jsonPiece = new JSONObject();
            jsonPiece.put("nom", p.getNom());
            jsonPiece.put("id", p.getId());
            jsonPiece.put("listMur", mursJSON(p));
            JSONListPorte.put(jsonPiece);
        }
        return JSONListPorte;
    }

    public void sauvegardeImage(View view) throws JSONException, IOException {
        enregistrement();
    }


    // CE QUI CONCERNE LE MENU

    /**
     * Permet de relier le menu au main
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Permet d'accerder au item
     * @param item
     * @return
     */
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