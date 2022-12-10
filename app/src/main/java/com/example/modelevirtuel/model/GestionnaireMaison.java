package com.example.modelevirtuel.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.modelevirtuel.SujetObserve;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

import static android.content.Context.MODE_PRIVATE;

public class GestionnaireMaison extends SujetObserve implements Iterable<Maison> {
    private HashMap<Integer,Maison> listMaison;
    private Maison selectMaison;
    private static GestionnaireMaison gestionnaireMaison = new GestionnaireMaison();

    /**
     * Constructeur
     */
    private GestionnaireMaison() {
        listMaison = new HashMap<>();
        selectMaison = null;
    }


    /**
     * Fonction qui retourne le champs GestionnaireMaison pour former le singleton
     * @return
     */
    public static GestionnaireMaison getInstance() {
        return gestionnaireMaison;
    }


    /**
     * Fonction qui retourne la maison qui a étais selectionné
     * @return la maison selectionné
     */
    public Maison getSelectMaison() {
        return selectMaison;
    }


    /**
     * Fonction qui changer la maison selectionné
     * @param selectMaison
     */
    public void setSelectMaison(Maison selectMaison) {
        this.selectMaison = selectMaison;
    }


    /**
     * Fonction qui ajoute une maison dans la liste des maison
     * @param m, le nom de la maison a ajouter
     */
    public void ajouterUneMaison(String m, int numero){
        int num =FabriqueIdentifiant.getInstance().getIdMaison(); // pour incrémenter la maison
        if ( !numeroIdentique(numero)  ){
            listMaison.put(numero,new Maison(m,numero));
        }

    }

    public void ajouterUneMaison(String m){
        int num =FabriqueIdentifiant.getInstance().getIdMaison();
        listMaison.put(num,new Maison(m,num));
    }

    public boolean numeroIdentique(int numero){
        boolean result = false ;
        Iterator<Maison> i = iterator();
        while(i.hasNext()){
            Maison m = i.next();
            if( numero == m.getId()){
                result = true;
            }
        }
        return result;
    }

    /**
     * Fonction qui retourne la list des maisons
     * @return
     */
    public HashMap<Integer, Maison> getListMaison() {
        return listMaison;
    }


    /**
     * Fonction qui retourne une maison dans la liste
     * @param i, la numéro de la maison a retourner
     * @return la maison
     */
    public Maison getMaison(int i){
        return listMaison.get(i);
    }


    /**
     * Iterateur sur la liste des maisons
     * @return l'interateur
     */
    @NonNull
    @NotNull
    @Override
    public Iterator iterator() {
        return listMaison.values().iterator();
    }


    /**
     * Fonction qui permet de changer la liste des maisons
     * @param listMaison, la nouvelle liste des maisons
     */
    public void setListMaison(HashMap<Integer,Maison> listMaison) {
        this.listMaison = listMaison;
    }


    /**
     * Fonction qui permet de supprimer la maison demander
     * @param ouvertMaison
     */
    public void supprimerMaison(Maison ouvertMaison) {
        HashMap<Integer, Maison> nv = new HashMap<>();
        listMaison.remove(ouvertMaison.getId());

        FabriqueIdentifiant.getInstance().removeMaison();

        Iterator<Maison> i = iterator();
        while(i.hasNext()){
            Maison m = i.next();
            int num = FabriqueIdentifiant.getInstance().getIdMaison();
            m.setId(num);

            nv.put(num,m);
        }

        listMaison = nv;


    }





    // SE QUI CONSERNE L'ENREGISTREMENT


    /**
     * Enregistrement
     * @throws IOException
     * @throws JSONException
     */
    public void enregistrement(OutputStream fichier) throws IOException, JSONException {

        OutputStreamWriter writer = new OutputStreamWriter(fichier);

        JSONObject jsonMaison = new JSONObject();
        jsonMaison.put("listeMaison", maisonJSON());

        String json = jsonMaison.toString();
        writer.write(json);
        writer.flush();

    }


    /**
     * Enregistrement de la liste des maison
     * @return
     * @throws JSONException
     */
    public JSONArray maisonJSON() throws JSONException{
        JSONArray JSONListMaison = new JSONArray();
        Maison m;
        Iterator<Maison> maison = iterator();
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

            Log.i("id", String.valueOf(p.getId()));
            if(p.getArriver() != null){

                jsonPorte.put("arriver", p.getArriver());
                jsonPorte.put("id", p.getId());
                jsonPorte.put("RectTop", p.getRect().top);
                jsonPorte.put("RectLeft", p.getRect().left);
                jsonPorte.put("RectBottom", p.getRect().bottom);
                jsonPorte.put("RectRight", p.getRect().right);
            }


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

        for (int i = 0; i < p.getListMur().size(); i++) {
            m = p.getListMur().get(i);
            JSONObject jsonMurs = new JSONObject();
            jsonMurs.put("listPorte", porteJSON(m));
            if( m.getOrientation() != null){
                jsonMurs.put("orientation", m.getOrientation().toString());
            }else{
                jsonMurs.put("orientation", " ");
            }
            jsonMurs.put("nom", m.getNom());
            jsonMurs.put("temperature", m.getTemperature());
            jsonMurs.put("loca", m.getLoca());

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





}

