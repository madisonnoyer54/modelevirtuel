package com.example.modelevirtuel.model;

import java.util.ArrayList;
import java.util.HashMap;

public class GestionnaireMaison {
    private HashMap<Integer,Maison> listMaison;
    private Maison selectMaison;

    /**
     * Constructeur
     */
    public GestionnaireMaison() {
        listMaison = new HashMap<>();
        selectMaison = null;
    }

    public Maison getSelectMaison() {
        return selectMaison;
    }

    public void setSelectMaison(Maison selectMaison) {
        this.selectMaison = selectMaison;
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

