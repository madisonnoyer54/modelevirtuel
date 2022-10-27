package com.example.modelevirtuel.model;

import java.util.HashMap;

public class Maison {
    private String nom;

    private String id;
    private HashMap<String,Piece> listPiece;

    /**
     * Constructeur
     * @param nom
     */
    public Maison(String nom, int id) {
        listPiece = new HashMap<>();
        this.nom = nom;
    }


    /**
     * Fonction qui permet d'ajouter une nouvelle piece a la maison
     * @param p
     */
    public void nouvellePiece(Piece p){
        listPiece.put("", p);
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

    public HashMap<String, Piece> getListPiece() {
        return listPiece;
    }

    public void setListPiece(HashMap<String, Piece> listPiece) {
        this.listPiece = listPiece;
    }
}
