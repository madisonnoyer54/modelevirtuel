package com.example.modelevirtuel.model;

import android.util.Log;
import com.example.modelevirtuel.outils.Orientation;

import java.util.ArrayList;
import java.util.Iterator;

public class Piece {
   private String nom;
   private int id;

   private ArrayList<Mur> listMur;
   private Mur murSelect;


    /**
     * Constructeur
     * @param nom
     * @param id
     */
    public Piece(String nom, int id){
        listMur = new ArrayList(4);

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


    public ArrayList<Mur> getListMur() {
        return listMur;
    }

    public void ajouterMur(Orientation orientation, String nom ){
        Mur m = new Mur(orientation, nom);
        listMur.add(m);
        murSelect = m;

    }

    public Mur getMurSelect() {
        return murSelect;
    }

    public void setMurSelect(Mur murSelect) {
        this.murSelect = murSelect;
    }

    public Mur getMur(Orientation valueOf) {
        for (int l = 0; l < listMur.size(); l++) {
            Mur mur = listMur.get(l);
            if(mur.getOrientation().equals(valueOf)){
                return mur;
            }
        }
        return  null;

    }
}
