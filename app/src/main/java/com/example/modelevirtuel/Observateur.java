package com.example.modelevirtuel;

import org.json.JSONException;

import java.io.IOException;

public interface Observateur {
    /**
     * Fonction qui permet de faire réagir l'interface graphique
     */
    public void reagir() throws JSONException, IOException;
}
