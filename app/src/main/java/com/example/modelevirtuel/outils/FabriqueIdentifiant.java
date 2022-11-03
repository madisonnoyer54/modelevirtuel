package com.example.modelevirtuel.outils;

public class FabriqueIdentifiant {
    private int idPiece;
    private int idPorte;
    private int idMur;

    private int idMaison;
    private static FabriqueIdentifiant fabriqueIdentifiant = new FabriqueIdentifiant();


    /**
     * Constructeur
     */
    private FabriqueIdentifiant() {
        this.idMur = 0;
        this.idPorte = 0;
        this.idPiece = 0;
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
     * Getteur du numéro du mur
     * @return
     */
    public int getIdMur() {
        idMur++;
        return idMur;
    }

    public int getIdMaison(){
        idMaison++;
        return  idMaison;
    }

    public void removeMaison(){
        idMaison = -1;
    }

}
