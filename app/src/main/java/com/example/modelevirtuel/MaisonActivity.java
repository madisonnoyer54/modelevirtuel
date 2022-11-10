package com.example.modelevirtuel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Maison;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.PieceAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MaisonActivity extends AppCompatActivity implements Observateur{

    private GestionnaireMaison listMaison;
    private Maison ouvertMaison;
    private Dialog dialog;
    private PieceAdapter pieceAdapt;

    static final int PHOTO = 1;
    private  Bitmap photo;
    private ImageView imagePhoto;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maison);


        // On bloque en mode portrait
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        // On recupaire le gestionnaire
        listMaison = GestionnaireMaison.getInstance();
        ouvertMaison = listMaison.getSelectMaison();

        // On met le nom de la maison
        TextView nomText = findViewById(R.id.nom_maison);
        nomText.setText(String.valueOf(listMaison.getSelectMaison().getNom()));




        // Liste des pieces
        RecyclerView recycler =  findViewById(R.id.RecyPiece);
        pieceAdapt = new PieceAdapter(ouvertMaison.getListPiece());
        recycler.setAdapter(pieceAdapt);

        // Mettre la recy horizontalement
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(linearLayoutManager);

        ouvertMaison.ajouterObservateur(this);
        ouvertMaison.notifierObservateur();

    }


    public void ajouterPiece(View view){
        String id = null;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_nom_piece);

        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void continuerPiece(View view){
        // Fermer le dialogue
        dialog.cancel();


        String nom;
        EditText editText = dialog.findViewById(R.id.usernamePiece);
        editText.getText();
        nom = editText.getText().toString().trim();

        ouvertMaison.nouvellePiece(nom);

        ouvertMaison.notifierObservateur();
        dialog.dismiss();

    }

    public void PieceSelectionner(View view){
        TextView num =  view.findViewById(R.id.item_num_piece);
        int id =Integer.parseInt((String) num.getText()) ;
        ouvertMaison.setPieceSelect(ouvertMaison.getListPiece().get(id));
        String nom = ouvertMaison.getPieceSelect().getNom();

        ouvertMaison.notifierObservateur();
    }

    public void annulerPiece(View view){
        dialog.cancel();
    }

    public void suppMaison(View view){
        finish();
        // On retourne sur la page d'acceuille car cette maison vas etre supprimer
        Intent ic = new Intent(MaisonActivity.this, MainActivity.class);
        startActivity(ic);

        listMaison.supprimerMaison(ouvertMaison);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void suppPiece(View view){
        ouvertMaison.supprimerPieceOuvert();
        pieceAdapt.setList(ouvertMaison.getListPiece());
        pieceAdapt.notifyDataSetChanged();

       ouvertMaison.notifierObservateur();

    }

    public void dialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_photo);

        dialog.show();
    }

    public void selectPhotoNord(View view){
        dialog();

        imagePhoto = findViewById(R.id.interrogationNord);
    }

    public void selectPhotoSud(View view){
        dialog();
        imagePhoto = findViewById(R.id.interrogationSud);

    }

    public void selectPhotoOuest(View view){
        dialog();
        imagePhoto = findViewById(R.id.interrogationOuest);
    }
    public void selectPhotoEst(View view){
        dialog();

        imagePhoto = findViewById(R.id.interrogationEst);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        photo = (Bitmap)data.getExtras().get("data");

                    }


                }

            });

    public void photo(View view) throws IOException {
        dialog.cancel();

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        someActivityResultLauncher.launch(intent);

        if(photo != null){
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("image.data", MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
            try {
                fos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileInputStream fis = null;
            try {
                fis = openFileInput("image.data");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap bm = BitmapFactory.decodeStream(fis);

            imagePhoto.setImageBitmap(bm);


        }
    }

    public void selectionPhoto(View view){
        dialog.cancel();

        // A remplir
    }

    @Override
    public void reagir() {
        // Gere les invisible

        TextView aucunePiece = findViewById(R.id.aucunePiece);
        ImageButton poubellePiece = findViewById(R.id.PoubellePiece);
        ImageView bousole = findViewById(R.id.boussole);
        ImageView i1 = findViewById(R.id.interrogationEst);
        ImageView i2 = findViewById(R.id.interrogationOuest);
        ImageView i3 = findViewById(R.id.interrogationNord);
        ImageView i4 = findViewById(R.id.interrogationSud);
        TextView nomPiece = findViewById(R.id.nom_piece);

        if(ouvertMaison.getListPiece().isEmpty()){
            poubellePiece.setVisibility(View.INVISIBLE);
            aucunePiece.setVisibility(View.VISIBLE);


            bousole.setVisibility(View.INVISIBLE);
            i1.setVisibility(View.INVISIBLE);
            i2.setVisibility(View.INVISIBLE);
            i3.setVisibility(View.INVISIBLE);
            i4.setVisibility(View.INVISIBLE);

            FabriqueIdentifiant.getInstance().removePiece();
            nomPiece.setText("");

        }else{
            poubellePiece.setVisibility(View.VISIBLE);
            aucunePiece.setVisibility(View.INVISIBLE);

            bousole.setVisibility(View.VISIBLE);
            i1.setVisibility(View.VISIBLE);
            i2.setVisibility(View.VISIBLE);
            i3.setVisibility(View.VISIBLE);
            i4.setVisibility(View.VISIBLE);

            FabriqueIdentifiant.getInstance().setPiece(ouvertMaison.getListPiece().size()-1);

            // La piece ouvert
            nomPiece.setText(ouvertMaison.getPieceSelect().getNom());

        }
        // Mise a jour de la Recy
        pieceAdapt.notifyDataSetChanged();
    }


    /**
     * Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Item du menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == R.id.quitter) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.pageacceuille) {
            Intent ic = new Intent(MaisonActivity.this, MainActivity.class);
            startActivity(ic);

            return true;
        }
        return false;
    }



}