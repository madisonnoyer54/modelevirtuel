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


    public static GestionnaireMaison getInstance() {
        return gestionnaireMaison;
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

    @NonNull
    @NotNull
    @Override
    public Iterator iterator() {
        return listMaison.values().iterator();
    }

    public void setListMaison(HashMap<Integer,Maison> listMaison) {
        this.listMaison = listMaison;
    }


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

