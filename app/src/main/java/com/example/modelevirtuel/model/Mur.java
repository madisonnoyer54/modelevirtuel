package com.example.modelevirtuel.model;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
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

    private double temperature;
    private String loca;




    /**
     * Constructeur
     */
    public Mur(Orientation orientation, String nom) {
        this.nom = nom;
        this.orientation = orientation;
        listPorte = new HashMap<>();
        temperature = 0;
        loca = " ";


    }

    public Mur(Orientation orientation, String nom,Double temp,String loca) {
        this.nom = nom;
        this.orientation = orientation;
        listPorte = new HashMap<>();
        temperature = temp;
        this.loca = loca;


    }

    public String getLoca() {
        return loca;
    }

    public void setLoca(String loca) {
        this.loca = loca;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
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
        if(arriver != null){
            listPorte.put(id, new Porte(arriver,id,rect));
        }

    }

    public void ajoutePorte( Piece arriver, Rect rect){
        if(arriver != null){
            int id;
            if(listPorte.isEmpty()){
                id = 0;
            }else{
                id = listPorte.size();
            }

            listPorte.put(id, new Porte(arriver,id,rect));
        }

    }


    public void suppPorte(int id){


        HashMap<Integer, Porte> nv = new HashMap<>();
        listPorte.remove(id);
        FabriqueIdentifiant.getInstance().removePorte();

        Iterator<Porte> i = iterator();
        while(i.hasNext()){
            Porte m = i.next();
            int idp =  FabriqueIdentifiant.getInstance().getIdPorte();
            m.setId(idp);
            nv.put(idp,m);
        }

        listPorte = nv;

        Log.i("cc",listPorte.toString());

    }


    public String getNom() {
        return nom;
    }
}
