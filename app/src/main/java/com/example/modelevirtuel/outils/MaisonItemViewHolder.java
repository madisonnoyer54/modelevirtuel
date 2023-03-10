package com.example.modelevirtuel.outils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.R;
import com.example.modelevirtuel.model.Maison;


public class MaisonItemViewHolder extends RecyclerView.ViewHolder   {

    private TextView nom;
    private TextView num;



    /**
     * Constructeur
     * @param itemView
     */
    public MaisonItemViewHolder( View itemView ) {
        super(itemView);
        nom = itemView.findViewById(R.id.item_Nom_maison);
        num = itemView.findViewById(R.id.item_num_maison);


    }


    /**
     * Fonction update
     * @param maison
     */
    public void updateMaison(Maison maison){
        if(maison != null){
            this.nom.setText(String.valueOf(maison.getNom()));
            this.num.setText(String.valueOf(maison.getId()));
        }


    }



}