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
import com.example.modelevirtuel.model.*;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.MaisonAdapter;
import com.example.modelevirtuel.outils.Orientation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

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



        listMaison.ajouterObservateur(this);
        // Ajouter les enregistrement des maison




        try {
           lireEnregistrement();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        try {
            listMaison.notifierObservateur();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }

// CE QUI CONSERNE LA SELECTION DE LA MAISON
    /**
     * Fonction qui selectionne la maison, sa ouvre un dialogue
     * @param view
     */
    public void maisonSelectionner(View view) throws JSONException, IOException {
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
        maisonAdapter.setList(listMaison);
        maisonAdapter.notifyDataSetChanged();
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
    public void continuer(View view) throws JSONException, IOException {
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
    public void reagir() throws JSONException, IOException {
        maisonAdapter.setList(listMaison);
        maisonAdapter.notifyDataSetChanged();

        TextView aucune = findViewById(R.id.AucunEnregistrer);
       // listMaison.enregistrement(fichierEnregistrement);

        if(listMaison.getListMaison().isEmpty()){
            aucune.setVisibility(View.VISIBLE);
        }else{
            aucune.setVisibility(View.INVISIBLE);
        }

        maisonAdapter.notifyDataSetChanged();
        listMaison.enregistrement(openFileOutput("sauvegarde.json", Context.MODE_PRIVATE));



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



    private JSONObject readStream(InputStream is) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for(String line = r.readLine(); line != null; line = r.readLine()){
            sb.append(line);
        }
        is.close();
        return new JSONObject((sb.toString()));
    }

    /**
     * Lecture de l'enregistrement
     * @throws FileNotFoundException
     */
    public void lireEnregistrement( ) throws IOException, JSONException {
      FabriqueIdentifiant.getInstance().removeMaison();


        FileInputStream fichier= openFileInput("sauvegarde.json");
        JSONObject jsonObject = readStream(fichier);
        JSONArray array = new JSONArray(jsonObject.getString("listeMaison"));

        for (int i = 0; i < array.length(); i++) {
            // On récupère un objet JSON du tableau
            JSONObject obj = new JSONObject(array.getString(i));
            String nom = obj.getString("nom");
            int id = Integer.parseInt(obj.getString("id"));

            listMaison.ajouterUneMaison(nom, id);

            // Lecture des piece
            lirePiece(id, obj);

        }


    }


    /**
     * Fonction qui permet de lire les piece
     * @param id
     * @param jsonObject
     * @throws JSONException
     */
    public void lirePiece(int id, JSONObject jsonObject) throws JSONException {
        JSONArray array = new JSONArray(jsonObject.getString("listePiece"));

        for (int i = 0; i < array.length(); i++) {
            // On récupère un objet JSON du tableau
            JSONObject obj = new JSONObject(array.getString(i));
            String nom = obj.getString("nom");
            int idp = Integer.parseInt(obj.getString("id"));

            listMaison.getMaison(id).ajouterPiece(nom,idp);

            // On passe au murs
            lireMur(listMaison.getMaison(id),idp,obj);

        }
    }


    /**
     * Fonction qui permet de lire le murs
     * @param m
     * @param id
     * @param jsonObject
     * @throws JSONException
     */
    public void lireMur(Maison m,int id, JSONObject jsonObject) throws JSONException {

        JSONArray array = new JSONArray(jsonObject.getString("listMur"));

        for (int i = 0; i < array.length(); i++) {
            // On récupère un objet JSON du tableau
            JSONObject obj = new JSONObject(array.getString(i));

            String orientation = obj.getString("orientation");

            String nom = obj.getString("nom");


            Piece p  = m.setPiece(id);


            p.ajouterMur(Orientation.valueOf(orientation),nom);

            lirePorte(m,p.getMur(Orientation.valueOf(orientation)), obj, Orientation.valueOf(orientation));

        }
    }


    /**
     * Fonction qui permet de lire les porte
     * @param maison
     * @param m
     * @param jsonObject
     * @param orientation
     * @throws JSONException
     */
    public void lirePorte(Maison maison,Mur m,JSONObject jsonObject, Orientation orientation) throws JSONException {
        JSONArray array = new JSONArray(jsonObject.getString("listPorte"));

        for (int i = 0; i < array.length(); i++) {
            // On récupère un objet JSON du tableau
            JSONObject obj = new JSONObject(array.getString(i));
            String arriver = obj.getString("arriver");
            int id = Integer.parseInt(obj.getString("id"));

           m.ajoutePorte(id,maison.setPiece(Integer.parseInt(arriver)));

        }

    }



}