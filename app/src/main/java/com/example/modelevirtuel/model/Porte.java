package com.example.modelevirtuel.model;

import android.graphics.Rect;

public class Porte {
    private Piece arriver;
    private int id;

    private Rect rect;



    /**
     * Constructeur
     * @param arriver
     * @param id
     */
    public Porte(Piece arriver, int id, Rect rect) {
        this.rect = rect;
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

    public Rect getRect() {
        return rect;
    }
}
