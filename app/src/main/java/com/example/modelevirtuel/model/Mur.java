package com.example.modelevirtuel.model;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.Orientation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Mur implements Iterable<Porte>{
   // Pas d'id car l'orientation fait out

    private HashMap<Integer,Porte> listPorte;
    private Orientation orientation;
    private String nom;


    /**
     * Constructeur
     */
    public Mur(Orientation orientation, String nom) {
        this.nom = nom;
        this.orientation = orientation;
        listPorte = new HashMap<>();
    }


    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }



    public void nouvellePorte(Porte p){
        listPorte.put(p.getId(),p);
    }

    public HashMap<Integer, Porte> getListPorte() {
        return listPorte;
    }

    public void setListPorte(HashMap<Integer,Porte> listPorte) {
        this.listPorte = listPorte;
    }

    @NonNull
    @NotNull
    @Override
    public Iterator<Porte> iterator() {
        return listPorte.values().iterator();
    }


    public void ajoutePorte(int id, Piece arriver, Rect rect){
     //   int idp = FabriqueIdentifiant.getInstance().getIdMur();
        listPorte.put(id, new Porte(arriver,id,rect));
    }

    public void ajoutePorte( Piece arriver, Rect rect){
        int id = FabriqueIdentifiant.getInstance().getIdPorte();
        listPorte.put(id, new Porte(arriver,id,rect));
    }


    public String getNom() {
        return nom;
    }
}
