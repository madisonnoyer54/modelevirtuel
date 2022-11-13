package com.example.modelevirtuel.model;

import android.util.Log;
import com.example.modelevirtuel.outils.Orientation;

import java.util.Iterator;

public class Piece {
   private String nom;
   private int id;

   private Mur selectMur;
   private Mur[] listMur;


    /**
     * Constructeur
     * @param nom
     * @param id
     */
    public Piece(String nom, int id){
        listMur = new Mur[4];
        listMur[0] = new Mur(null,null);
        listMur[1] = new Mur(null,null);
        listMur[2] = new Mur(null,null);
        listMur[3] = new Mur(null,null);

        this.nom = nom;
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Mur[] getListMur() {
        return listMur;
    }

    public void setListMur(Mur[] listMur) {
        this.listMur = listMur;
    }

    public void ajouterMur(Orientation orientation, String nom ){
        if(orientation == Orientation.NORD){
            Log.i("s","on paasse");
            listMur[0].setNom(nom);
            listMur[0].setOrientation(orientation);

        }
        if(orientation == Orientation.SUD){
            listMur[1].setNom(nom);
            listMur[1].setOrientation(orientation);
        }
        if(orientation == Orientation.OUEST){
            listMur[2].setNom(nom);
            listMur[2].setOrientation(orientation);
        }
        if(orientation == Orientation.OUEST){
            listMur[3].setNom(nom);
            listMur[3].setOrientation(orientation);
        }
    }


}
