package com.example.modelevirtuel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class SujetObserve {
    private ArrayList<Observateur> listOb;

    /**
     * Constructeur
     */
    public SujetObserve() {
        this.listOb = new ArrayList<>();
    }


    /**
     * Fonction qui permet d'ajouter un observateur
     * @param v
     */
    public void ajouterObservateur(Observateur v){
        this.listOb.add(v);
    }

    /**
     * Fonction qui permet de notifier les observateurs
     */
    public void notifierObservateur() throws JSONException, IOException {
        Iterator<Observateur> i = listOb.iterator();

        while(i.hasNext()){
            i.next().reagir();
        }
    }

    /**
     * Getteur List Observateur
     * @return, la liste
     */
    public ArrayList<Observateur> getListOb() {
        return listOb;
    }
}
