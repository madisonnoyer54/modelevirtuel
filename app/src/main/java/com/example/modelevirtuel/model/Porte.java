package com.example.modelevirtuel.model;

public class Porte {
    private Piece arriver;
    private String id;


    /**
     * Constructeur
     * @param arriver
     * @param id
     */
    public Porte(Piece arriver, String id) {
        this.arriver = arriver;
        this.id = id;
    }
}
