package com.example.modelevirtuel.model;

import java.util.ArrayList;
import java.util.HashMap;

public class GestionnaireMaison {
    private HashMap<Integer,Maison> listMaison;

    /**
     * Constructeur
     */
    public GestionnaireMaison() {
        listMaison = new HashMap<>();
    }


    public void ajouterUneMaison(Maison m){
        listMaison.put(m.getId(),m);
    }

    public HashMap<Integer, Maison> getListMaison() {
        return listMaison;
    }

    public Maison getMaison(int i){
        return listMaison.get(i);
    }

    public void setListMaison(HashMap<Integer,Maison> listMaison) {
        this.listMaison = listMaison;
    }


}

