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

    /**
     * Fonction qui permet de retourner le nom de la piece
     * @return
     */
    public String getNom() {
        return nom;
    }


    /**
     * Fonction qui permet de modifier le nom
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }


    /**
     * Fonction qui permet de retourner l'id de la piece
     * @return
     */
    public int getId() {
        return id;
    }


    /**
     * Foncton qui permet de modifier l'id de la piece
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Fonction qui pertmet de retourner la liste des murs
     * @return
     */
    public ArrayList<Mur> getListMur() {
        return listMur;
    }


    /**
     * Fonction qui permet d'ajouter un mur
     * @param orientation
     * @param nom
     * @param temp
     * @param loca
     */
    public void ajouterMur(Orientation orientation, String nom,Double temp, String loca ){
        Mur m = new Mur(orientation, nom,temp,loca);
        listMur.add(m);
        murSelect = m;

    }

    /**
     * FOnction qui permet d'ajouter un mur
     * @param orientation
     * @param nom
     */
    public void ajouterMur(Orientation orientation, String nom){
        Mur m = new Mur(orientation, nom);
        listMur.add(m);
        murSelect = m;

    }


    /**
     * Fonction qui permet de retourner le mur selectionner
     * @return
     */
    public Mur getMurSelect() {
        return murSelect;
    }


    /**
     * Fonction qui permet de modifier le mur selectionner
     * @param murSelect
     */
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


    /**
     * Fonction qui permet de retourner l'orientation a gauhce
     * @param orientation
     * @return
     */
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


    /**
     * Fonction qui permet de retourner l'orientation a droite
     * @param orientation
     * @return
     */
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
