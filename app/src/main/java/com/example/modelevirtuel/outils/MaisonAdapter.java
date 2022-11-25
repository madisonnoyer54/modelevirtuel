package com.example.modelevirtuel.outils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.R;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Maison;
import com.example.modelevirtuel.model.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MaisonAdapter extends RecyclerView.Adapter<MaisonItemViewHolder> {

    private GestionnaireMaison list;


    /**
     * Constructeur
     * @param maison
     */
    public MaisonAdapter(GestionnaireMaison maison){
        this.list = maison;
    }


    @Override
    public MaisonItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_maison_item, parent, false);


        return new MaisonItemViewHolder(view);
    }


    // UPDATE VIEW HOLDER WITH A GITHUBUSER
    @Override
    public void onBindViewHolder(MaisonItemViewHolder viewHolder, int position) {
        viewHolder.updateMaison(this.list.getMaison(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.list.getListMaison().size();
    }

    public void setList(GestionnaireMaison maison) {
       this.list = maison;
    }



}




