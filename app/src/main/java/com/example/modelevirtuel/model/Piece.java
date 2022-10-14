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
}
