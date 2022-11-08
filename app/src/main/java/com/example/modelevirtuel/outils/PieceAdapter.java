package com.example.modelevirtuel.outils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.R;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Piece;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class PieceAdapter extends RecyclerView.Adapter<PieceItemViewHolder>{

    private HashMap<Integer, Piece> list;

    public PieceAdapter(HashMap<Integer,Piece> piece){
        this.list = piece;
    }


    @Override
    public PieceItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_piece_item, parent, false);


        return new PieceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PieceItemViewHolder holder, int position) {
        holder.updatePiece(this.list.get(position));
    }

    public void setList(HashMap<Integer, Piece> list) {
        this.list = list;
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
