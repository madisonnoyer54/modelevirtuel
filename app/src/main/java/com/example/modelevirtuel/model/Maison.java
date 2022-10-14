package com.example.modelevirtuel.model;

import java.util.ArrayList;

public class Maison {
    private String nom;

    private String id;
    private ArrayList<Piece> listPiece;

    /**
     * Constructeur
     * @param nom
     */
    public Maison(String nom, String id) {
        listPiece = new ArrayList<>();
        this.nom = nom;
    }
}
