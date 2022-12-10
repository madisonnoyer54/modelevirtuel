package com.example.modelevirtuel.outils;

public class FabriqueIdentifiant {
    private int idPiece;
    private int idPorte;


    private int idMaison;
    private static FabriqueIdentifiant fabriqueIdentifiant = new FabriqueIdentifiant();


    /**
     * Constructeur
     */
    private FabriqueIdentifiant() {

        this.idPorte = -1;
        this.idPiece = -1;
        this.idMaison = -1;
    }


    /**
     * @return une FabriqueIdentifiant
     */
    public static FabriqueIdentifiant getInstance() {
        return fabriqueIdentifiant;
    }


    /**
     * Getteur du numero de la pièce
     * @return
     */
    public int getIdPiece() {
        idPiece++;
        return idPiece;
    }


    /**
     * Getteur du numéro de la porte
     * @return
     */
    public int getIdPorte() {
        idPorte++;
        return idPorte;
    }




    /**
     * Fonction qui retourne l'identifiant +1 de la dernière maison
     * @return
     */
    public int getIdMaison(){
        idMaison++;
        return  idMaison;
    }


    /**
     * Fonction qui remet l'identifiant de la maison a son depart
     */
    public void removeMaison(){
        idMaison = -1;
    }


    /**
     * Fonction qui remet l'identifiant de la piece a son départ
     */
    public void removePiece() {
        idPiece = -1;
    }

    /**
     * Fonction qui permet de remove l'idPorte
     */
    public void removePorte() {
        idPorte = -1;
    }


    /**
     * Fonction qui permet de changer l'indentifiant de la piece
     * @param size
     */
    public void setPiece(int size) {
        idPiece = size;
    }
}
