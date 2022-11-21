package com.example.modelevirtuel.model;

public class Porte {
    private Piece arriver;
    private int id;



    /**
     * Constructeur
     * @param arriver
     * @param id
     */
    public Porte(Piece arriver, int id) {
        this.arriver = arriver;
        this.id = id;
    }

    public Piece getArriver() {
        return arriver;
    }

    public void setArriver(Piece arriver) {
        this.arriver = arriver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
