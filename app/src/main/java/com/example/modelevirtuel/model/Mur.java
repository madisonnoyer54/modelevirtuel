package com.example.modelevirtuel.model;

import androidx.annotation.NonNull;
import com.example.modelevirtuel.outils.Orientation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Mur implements Iterable<Porte>{
   // Pas d'id car l'orientation fait out

    private HashMap<String,Porte> listPorte;
    private Orientation orientation;
    private boolean photoPrise;
    private String nom;


    /**
     * Constructeur
     */
    public Mur(Orientation orientation, String nom) {
        this.orientation = orientation;
        listPorte = new HashMap<>();
        photoPrise = false;
        this.nom = nom;
    }


    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean getPhotoPrise() {
        return photoPrise;
    }

    public void setPhotoPrise(boolean photoPrise) {
        this.photoPrise = photoPrise;
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

    @NonNull
    @NotNull
    @Override
    public Iterator<Porte> iterator() {
        return listPorte.values().iterator();
    }


}
