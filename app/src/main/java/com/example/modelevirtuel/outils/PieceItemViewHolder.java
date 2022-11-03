package com.example.modelevirtuel.outils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.R;
import com.example.modelevirtuel.model.Piece;
import org.jetbrains.annotations.NotNull;

public class PieceItemViewHolder extends RecyclerView.ViewHolder  {
    private Piece piece;
   private TextView nom;
   private TextView num;


    public PieceItemViewHolder( @NonNull @NotNull View itemView) {
        super(itemView);
        nom = itemView.findViewById(R.id.item_Nom_piece);
        num = itemView.findViewById(R.id.item_num_piece);
    }



    public void updatePiece(Piece piece){
        this.piece = piece;
        this.nom.setText(piece.getNom());
        this.num.setText(String.valueOf(piece.getId()));

    }

}
