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

    public void ajouterMur(Orientation orientation, String nom,Double temp, String loca ){
        Mur m = new Mur(orientation, nom,temp,loca);
        listMur.add(m);
        murSelect = m;

    }

    public void ajouterMur(Orientation orientation, String nom){
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

    public Orientation tournerGauche(Orientation orientation){
        if(orientation == Orientation.NORD){
            return Orientation.EST;
        }
        if(orientation == Orientation.EST){
            return (Orientation.SUD);
        }
        if(orientation == Orientation.SUD){
            return (Orientation.OUEST);
        }
        if(orientation == Orientation.OUEST){
            return (Orientation.NORD);
        }
        return (Orientation.NORD);
    }

    public Orientation tournerDroite(Orientation orientation){
        if(orientation == Orientation.NORD){
            return (Orientation.OUEST);
        }
        if(orientation == Orientation.EST){
            return (Orientation.NORD);
        }
        if(orientation == Orientation.SUD){
            return (Orientation.EST);
        }
        if(orientation == Orientation.OUEST){
            return (Orientation.SUD);
        }
        return (Orientation.NORD);
    }
}
