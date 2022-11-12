package com.example.modelevirtuel.model;

import androidx.annotation.NonNull;
import com.example.modelevirtuel.SujetObserve;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;

public class Maison extends SujetObserve implements Iterable<Piece> {
    private String nom;

    private int id;
    private HashMap<Integer, Piece> listPiece;
    private Piece pieceSelect;

    /**
     * Constructeur
     * @param nom
     */
    public Maison(String nom, int id) {
        listPiece = new HashMap<>();
        this.nom = nom;
        this.id = id;
        pieceSelect = null;
    }


    /**
     * Fonction qui permet d'ajouter une nouvelle piece a la maison
     */
    public void nouvellePiece(String nomPiece){
        int num = FabriqueIdentifiant.getInstance().getIdPiece();
        Piece p = new Piece(nomPiece,num);
        listPiece.put(num,p);

        pieceSelect = p;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Integer, Piece> getListPiece() {
        return listPiece;
    }

    public void setListPiece(HashMap<Integer, Piece> listPiece) {
        this.listPiece = listPiece;
    }

    public Piece getPieceSelect() {
        return pieceSelect;
    }

    public void setPieceSelect(Piece pieceSelect) {
        this.pieceSelect = pieceSelect;
    }


    public void supprimerPieceOuvert(){
        listPiece.remove(pieceSelect.getId());

        HashMap<Integer, Piece> nv = new HashMap<>();
        listPiece.remove(pieceSelect.getId());

        FabriqueIdentifiant.getInstance().removePiece();

        Iterator<Piece> i = iterator();
        while(i.hasNext()){
            Piece m = i.next();
            int num = FabriqueIdentifiant.getInstance().getIdPiece();
            m.setId(num);

            nv.put(num,m);
        }

        listPiece = nv;

        if(!listPiece.isEmpty()){
            pieceSelect = listPiece.get(listPiece.size()-1);
        }else{
            pieceSelect = null;
        }
    }

    @NonNull
    @NotNull
    @Override
    public Iterator<Piece> iterator() {
        return listPiece.values().iterator();
    }


    public void nomPieceSelect(String nom){
        pieceSelect.setNom(nom);
    }
}
