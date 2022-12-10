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


    /**
     * Fonction qui permet de retourner le nomArriver
     * @return
     */
    public String getArriver() {
        return nomArriver;
    }

    /**
     * Fonction qui permet de changer le nomArriver
     * @param arriver
     */
    public void setArriver(String arriver) {
        this.nomArriver = arriver;
    }


    /**
     * Fonction qui retourne l'id de la porte
     * @return
     */
    public int getId() {
        return id;
    }


    /**
     * Fonction qui permet de modifier l'id de la porte
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Fonction qui permet de retourner le rectangle
     * @return
     */
    public Rect getRect() {
        return rect;
    }
}
