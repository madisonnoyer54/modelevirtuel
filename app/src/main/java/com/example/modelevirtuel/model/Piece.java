package com.example.modelevirtuel.model;

public class Piece {
   private String nom;
   private String id;
   private Mur[] listMur;


    /**
     * Constructeur
     * @param nom
     * @param id
     */
    public Piece(String nom, String id){
        listMur = new Mur[4];
        this.nom = nom;
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Mur[] getListMur() {
        return listMur;
    }

    public void setListMur(Mur[] listMur) {
        this.listMur = listMur;
    }
}
