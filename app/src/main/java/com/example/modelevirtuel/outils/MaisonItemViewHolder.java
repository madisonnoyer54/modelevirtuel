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
    private Context context;



    public MaisonItemViewHolder( Context context,View itemView ) {
        super(itemView);
        nom = itemView.findViewById(R.id.item_Nom_piece);
        num = itemView.findViewById(R.id.item_num_piece);
        this.context = context;

    }



    public void updateMaison(Maison maison){
        this.maison = maison;
        this.nom.setText(maison.getNom());
        Log.i("id", String.valueOf(maison.getId()));
        this.num.setText(String.valueOf(maison.getId()));

    }



}