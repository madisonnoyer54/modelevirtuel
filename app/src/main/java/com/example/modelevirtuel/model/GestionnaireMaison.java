package com.example.modelevirtuel.model;

import androidx.annotation.NonNull;
import com.example.modelevirtuel.SujetObserve;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class GestionnaireMaison extends SujetObserve implements Iterable<Maison> {
    private HashMap<Integer,Maison> listMaison;
    private Maison selectMaison;
    private static GestionnaireMaison gestionnaireMaison = new GestionnaireMaison();

    /**
     * Constructeur
     */
    private GestionnaireMaison() {
        listMaison = new HashMap<>();
        selectMaison = null;
    }


    /**
     * Fonction qui retourne le champs GestionnaireMaison pour former le singleton
     * @return
     */
    public static GestionnaireMaison getInstance() {
        return gestionnaireMaison;
    }


    /**
     * Fonction qui retourne la maison qui a étais selectionné
     * @return la maison selectionné
     */
    public Maison getSelectMaison() {
        return selectMaison;
    }


    /**
     * Fonction qui changer la maison selectionné
     * @param selectMaison
     */
    public void setSelectMaison(Maison selectMaison) {
        this.selectMaison = selectMaison;
    }


    /**
     * Fonction qui ajoute une maison dans la liste des maison
     * @param m, le nom de la maison a ajouter
     */
    public void ajouterUneMaison(String m){
        int num =FabriqueIdentifiant.getInstance().getIdMaison();
        listMaison.put(num,new Maison(m,num));
    }


    /**
     * Fonction qui retourne la list des maisons
     * @return
     */
    public HashMap<Integer, Maison> getListMaison() {
        return listMaison;
    }


    /**
     * Fonction qui retourne une maison dans la liste
     * @param i, la numéro de la maison a retourner
     * @return la maison
     */
    public Maison getMaison(int i){
        return listMaison.get(i);
    }


    /**
     * Iterateur sur la liste des maisons
     * @return l'interateur
     */
    @NonNull
    @NotNull
    @Override
    public Iterator iterator() {
        return listMaison.values().iterator();
    }


    /**
     * Fonction qui permet de changer la liste des maisons
     * @param listMaison, la nouvelle liste des maisons
     */
    public void setListMaison(HashMap<Integer,Maison> listMaison) {
        this.listMaison = listMaison;
    }


    /**
     * Fonction qui permet de supprimer la maison demander
     * @param ouvertMaison
     */
    public void supprimerMaison(Maison ouvertMaison) {
        HashMap<Integer, Maison> nv = new HashMap<>();
        listMaison.remove(ouvertMaison.getId());

        FabriqueIdentifiant.getInstance().removeMaison();

        Iterator<Maison> i = iterator();
        while(i.hasNext()){
            Maison m = i.next();
            int num = FabriqueIdentifiant.getInstance().getIdMaison();
            m.setId(num);

            nv.put(num,m);
        }

        listMaison = nv;
    }



}

