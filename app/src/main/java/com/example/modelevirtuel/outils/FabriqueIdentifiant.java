package com.example.modelevirtuel.outils;

public class FabriqueIdentifiant {
    private int idPiece;
    private int idPorte;
    private int idMur;
    private static FabriqueIdentifiant fabriqueIdentifiant = new FabriqueIdentifiant();


    /**
     * Constructeur
     */
    private FabriqueIdentifiant() {
        this.idMur = 0;
        this.idPorte = 0;
        this.idPiece = 0;
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
        return idPiece;
    }


    /**
     * Getteur du numéro de la porte
     * @return
     */
    public int getIdPorte() {
        return idPorte;
    }


    /**
     * Getteur du numéro du mur
     * @return
     */
    public int getIdMur() {
        return idMur;
    }

}
