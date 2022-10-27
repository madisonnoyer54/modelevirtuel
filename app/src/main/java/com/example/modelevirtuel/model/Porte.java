package com.example.modelevirtuel.model;

public class Porte {
    private Piece arriver;
    private String id;

    private Piece pieceArriver;


    /**
     * Constructeur
     * @param arriver
     * @param id
     */
    public Porte(Piece arriver, String id) {
        this.arriver = arriver;
        this.id = id;
    }

    public Piece getArriver() {
        return arriver;
    }

    public void setArriver(Piece arriver) {
        this.arriver = arriver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Piece getPieceArriver() {
        return pieceArriver;
    }

    public void setPieceArriver(Piece pieceArriver) {
        this.pieceArriver = pieceArriver;
    }
}
