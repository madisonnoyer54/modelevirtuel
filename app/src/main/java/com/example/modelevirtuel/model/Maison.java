package com.example.modelevirtuel.model;

import androidx.annotation.NonNull;
import com.example.modelevirtuel.SujetObserve;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.Orientation;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Maison extends SujetObserve implements Iterable<Piece> {
    private String nom;
    private int id;
    private HashMap<Integer, Piece> listPiece;
    private Piece pieceSelect;

    private Piece pieceVisu;

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
     * Fonction qui permet de retourner la piece d e la visualisation
     * @return
     */
    public Piece getPieceVisu() {
        return pieceVisu;
    }


    /**
     * Fonction qui permet de modifier la piece de la visualisation
     * @param pieceVisu
     */
    public void setPieceVisu(Piece pieceVisu) {
        this.pieceVisu = pieceVisu;
    }


    /**
     * Fonction qui permet de modifer la piece de visualisation
     * @param pieceVisu
     */
    public void setPieceVisu(String pieceVisu) {

        Iterator<Piece> i = iterator();
        while(i.hasNext()){
            Piece m = i.next();
            if(Objects.equals(pieceVisu, m.getNom())){
                this.pieceVisu = m;
            }
        }

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

    /**
     * Fonction qui permet d'ajouter une nouvelle piece a la maison
     */
    public void ajouterPiece(String nomPiece, int num){
        int num2 = FabriqueIdentifiant.getInstance().getIdPiece();
        Piece p = new Piece(nomPiece,num);
        listPiece.put(num,p);

        pieceSelect = p;
    }


    /**
     * Fonction qui permet de retourner le nom de la maison
     * @return
     */
    public String getNom() {
        return nom;
    }


    /**
     * Fonction qui permet de changer le nom de la maison
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }


    /**
     * Fonction qui retourne l'identifiant de la maison
     * @return
     */
    public int getId() {
        return id;
    }


    /**
     * Fonction qui permet de changer l'indentifiant de la maison
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Fonction qui retourne la liste de les piece de la maison
     * @return
     */
    public HashMap<Integer, Piece> getListPiece() {
        return listPiece;
    }


    /**
     * Fonction qui permet de changer la liste des pieces de la maison
     * @param listPiece
     */
    public void setListPiece(HashMap<Integer, Piece> listPiece) {
        this.listPiece = listPiece;
    }


    /**
     * Fonction qui permet de retourner la piece selectionner
     * @return
     */
    public Piece getPieceSelect() {
        return pieceSelect;
    }


    /**
     * Fonction qui permet de changer la piece selectionner
     * @param pieceSelect
     */
    public void setPieceSelect(Piece pieceSelect) {
        this.pieceSelect = pieceSelect;
    }


    /**
     * Fonction qui permet de surpprimer la piece selectionner
     */
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



    /**
     * Iterator
     * @return
     */
    @NonNull
    @NotNull
    @Override
    public Iterator<Piece> iterator() {
        return listPiece.values().iterator();
    }


    /**
     * Fonction qui permet de changer le nom de la piece selectionner
     * @param nom
     */
    public void nomPieceSelect(String nom){
        pieceSelect.setNom(nom);
    }


    /**
     * Fonction qui permet d'ajouter une mur
     * @param o
     * @param nom
     */
    public void ajouterMur(Orientation o,String nom){
        pieceSelect.ajouterMur(o, nom);
    }


    /**
     * Fonction qui retourne la piece avec l'id rentrer
     * @param id
     * @return
     */
   public Piece setPiece(int id){
        return listPiece.get(id);
   }


    /**
     * Fonction qui permet de retourner la piece avec le nom rentrer
     * @param nom
     * @return
     */
    public Piece setPiece(String nom){
        Piece f = null;
        Iterator<Piece> i = iterator();
        while(i.hasNext()){
            Piece m = i.next();

            if(m.getNom() == nom){
                f = m;
            }

        }
        return f;
    }


    /**
     * Fonction qui retourner une array a la place d'une hashmap
     * @return
     */
   public ArrayList<String> transformeEnArray(){
        ArrayList<String> piece= new ArrayList<String>();

       Iterator<Piece> i = iterator();
       while(i.hasNext()){
           Piece m = i.next();

         piece.add(m.getNom());

       }
        return piece;

   }



}
