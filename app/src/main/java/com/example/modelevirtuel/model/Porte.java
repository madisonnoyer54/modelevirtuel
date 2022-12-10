package com.example.modelevirtuel.model;

import android.graphics.Rect;

public class Porte {

    private String nomArriver;
    private int id;

    private Rect rect;



    /**
     * Constructeur
     * @param arriver
     * @param id
     */


    public Porte(String arriver, int id, Rect rect) {
        this.rect = rect;
        this.nomArriver= arriver;
        this.id = id;
    }

    public String getNomArriver() {
        return nomArriver;
    }

    public void setNomArriver(String nomArriver) {
        this.nomArriver = nomArriver;
    }

    public String getArriver() {
        return nomArriver;
    }

    public void setArriver(String arriver) {
        this.nomArriver = arriver;
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
