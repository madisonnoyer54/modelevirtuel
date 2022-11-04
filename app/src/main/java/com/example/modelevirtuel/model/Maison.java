package com.example.modelevirtuel.model;

import java.util.HashMap;

public class Maison {
    private String nom;

    private int id;
    private HashMap<String,Piece> listPiece;
    private Piece pieceOuvert;

    /**
     * Constructeur
     * @param nom
     */
    public Maison(String nom, int id) {
        listPiece = new HashMap<>();
        this.nom = nom;
        this.id = id;
        pieceOuvert = null;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, Piece> getListPiece() {
        return listPiece;
    }

    public void setListPiece(HashMap<String, Piece> listPiece) {
        this.listPiece = listPiece;
    }

    public Piece getPieceOuvert() {
        return pieceOuvert;
    }

    public void setPieceOuvert(Piece pieceOuvert) {
        this.pieceOuvert = pieceOuvert;
    }
}
