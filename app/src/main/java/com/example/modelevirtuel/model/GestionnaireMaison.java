package com.example.modelevirtuel.model;

import java.util.ArrayList;
import java.util.HashMap;

public class GestionnaireMaison {
    private HashMap<String,Maison> listMaison;

    /**
     * Constructeur
     */
    public GestionnaireMaison() {
        listMaison = new HashMap<>();
    }


    public void ajouterUneMaison(Maison m){
        listMaison.put(m.getId(),m);
    }

    public HashMap<String, Maison> getListMaison() {
        return listMaison;
    }

    public void setListMaison(HashMap<String,Maison> listMaison) {
        this.listMaison = listMaison;
    }
}

