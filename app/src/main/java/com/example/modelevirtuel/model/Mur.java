package com.example.modelevirtuel.model;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.Orientation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Mur implements Iterable<Porte>{
   // Pas d'id car l'orientation fait out

    private HashMap<Integer,Porte> listPorte;
    private Orientation orientation;
    private String nom;

    private double temperature;
    private String loca;




    /**
     * Constructeur
     */
    public Mur(Orientation orientation, String nom) {
        this.nom = nom;
        this.orientation = orientation;
        listPorte = new HashMap<>();
        temperature = 0;
        loca = " ";


    }

    /**
     * Constructeur
     * @param orientation
     * @param nom
     * @param temp
     * @param loca
     */
    public Mur(Orientation orientation, String nom,Double temp,String loca) {
        this.nom = nom;
        this.orientation = orientation;
        listPorte = new HashMap<>();
        temperature = temp;
        this.loca = loca;


    }


    /**
     * Fonction qui permet de retourner la localisation
     * @return
     */
    public String getLoca() {
        return loca;
    }


    /**
     * Fonction qui permet de modifier la localisation
     * @param loca
     */
    public void setLoca(String loca) {
        this.loca = loca;
    }


    /**
     * Fonction qui permet de modifier le nom
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }


    /**
     * Fonction qui permet de retourner la température
     * @return
     */
    public double getTemperature() {
        return temperature;
    }


    /**
     * Fonction qui permet de modifier la temperature
     * @param temperature
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }


    /**
     * Fonction qui permet de retourner l'orientation
     * @return
     */
    public Orientation getOrientation() {
        return orientation;
    }


    /**
     * Fonction qui permet de modifier l'orientation
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }


    /**
     * Fonction qui retourne la liste des porte
     * @return
     */
    public HashMap<Integer, Porte> getListPorte() {
        return listPorte;
    }



    @NonNull
    @NotNull
    @Override
    public Iterator<Porte> iterator() {
        return listPorte.values().iterator();
    }


    /**
     * Fonction qui permet d'ajouter une porte
     * @param id
     * @param arriver
     * @param rect
     */
    public void ajoutePorte(int id, String arriver, Rect rect){
     //   int idp = FabriqueIdentifiant.getInstance().getIdMur();

            listPorte.put(id, new Porte(arriver,id,rect));


    }


    /**
     * Fonction qui permet d'ajouter une porte
     * @param arriver
     * @param rect
     */
    public void ajoutePorte( String arriver, Rect rect){
        if(arriver != null){
            int id;
            if(listPorte.isEmpty()){
                id = 0;
            }else{
                id = listPorte.size();
            }

            listPorte.put(id, new Porte(arriver,id,rect));
        }

    }


    /**
     * Fonction qui permet de supprimer la porte
     * @param id
     */
    public void suppPorte(int id){


        HashMap<Integer, Porte> nv = new HashMap<>();
        listPorte.remove(id);
        FabriqueIdentifiant.getInstance().removePorte();

        Iterator<Porte> i = iterator();
        while(i.hasNext()){
            Porte m = i.next();
            int idp =  FabriqueIdentifiant.getInstance().getIdPorte();
            m.setId(idp);
            nv.put(idp,m);
        }

        listPorte = nv;

        Log.i("cc",listPorte.toString());

    }


    public String getNom() {
        return nom;
    }
}
