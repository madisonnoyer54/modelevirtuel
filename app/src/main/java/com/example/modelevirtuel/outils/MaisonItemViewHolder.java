package com.example.modelevirtuel.outils;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.R;
import com.example.modelevirtuel.model.Maison;


public class MaisonItemViewHolder extends RecyclerView.ViewHolder {

    private TextView nom;
    private TextView num;


    public MaisonItemViewHolder(View itemView) {
        super(itemView);
        nom = itemView.findViewById(R.id.item_Nom);

    }

    public void updateMaison(Maison maison){
        this.nom.setText(maison.getNom());

    }

}