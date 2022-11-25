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
    private Maison maison;




    public MaisonItemViewHolder( View itemView ) {
        super(itemView);
        nom = itemView.findViewById(R.id.item_Nom_maison);
        num = itemView.findViewById(R.id.item_num_maison);


    }



    public void updateMaison(Maison maison){
        if(maison != null){
            this.maison = maison;
            this.nom.setText(String.valueOf(maison.getNom()));
            this.num.setText(String.valueOf(maison.getId()));
        }


    }



}