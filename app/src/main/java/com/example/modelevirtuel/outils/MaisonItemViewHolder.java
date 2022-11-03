package com.example.modelevirtuel.outils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.MainActivity;
import com.example.modelevirtuel.R;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Maison;


public class MaisonItemViewHolder extends RecyclerView.ViewHolder   {

    private TextView nom;
    private TextView num;
    private Maison maison;
    private Context context;



    public MaisonItemViewHolder( Context context,View itemView ) {
        super(itemView);
        nom = itemView.findViewById(R.id.item_Nom);
        num = itemView.findViewById(R.id.item_Num);
        this.context = context;

    }



    public void updateMaison(Maison maison){
        this.maison = maison;
        this.nom.setText(maison.getNom());
        Log.i("id", String.valueOf(maison.getId()));
        this.num.setText(String.valueOf(maison.getId()));

    }



}