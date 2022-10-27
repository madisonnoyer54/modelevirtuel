package com.example.modelevirtuel.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Mur {
   // Pas d'id car l'orientation fait out

    private HashMap<String,Porte> listPorte;


    /**
     * Constructeur
     */
    public Mur() {
        listPorte = new HashMap<>();

    }

    public void nouvellePorte(Porte p){
        listPorte.put(p.getId(),p);
    }

    public HashMap<String, Porte> getListPorte() {
        return listPorte;
    }

    public void setListPorte(HashMap<String,Porte> listPorte) {
        this.listPorte = listPorte;
    }
}
