package com.example.modelevirtuel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
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
import com.example.modelevirtuel.outils.VueCapteurActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MaisonActivity extends AppCompatActivity  implements SensorEventListener,Observateur{

    private GestionnaireMaison listMaison;
    private Maison ouvertMaison;
    private Dialog dialog;
    private PieceAdapter pieceAdapt;

    static final int PHOTO = 1;
    private  Bitmap photo;
    private ImageView imagePhoto;

     private SensorManager sensorManagerMA;
     private Sensor sensorMA;

    VueCapteurActivity vue;

    private boolean act;
    private float orianta;

    private float[] linear_acceleration;
    private SensorManager sensorManagerACC;
    private Sensor sensorACC;








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

        // Magnétometre

        act = false;
        linear_acceleration = new float[3];
        orianta = 0;
        // Accelerometer
        sensorManagerACC = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorACC = sensorManagerACC.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        vue = findViewById(R.id.capteur);
        sensorManagerMA = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorMA = sensorManagerMA.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        listMaison.ajouterObservateur(this);
        listMaison.notifierObservateur();

    }

    //  Pour ajouter la piece

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

        listMaison.notifierObservateur();
        dialog.dismiss();

    }
    public void annulerPiece(View view){
        dialog.cancel();
    }


    // Selection de la piece
    public void PieceSelectionner(View view){
        TextView num =  view.findViewById(R.id.item_num_piece);
        int id =Integer.parseInt((String) num.getText()) ;
        ouvertMaison.setPieceSelect(ouvertMaison.getListPiece().get(id));
        String nom = ouvertMaison.getPieceSelect().getNom();

        listMaison.notifierObservateur();
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

        listMaison.notifierObservateur();

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

    // Pour changer le nom de la piece

    public void changerNomPiece(View view){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_nom_piece);

        dialog.show();
    }

    public void annulerChangerP(View view){
        dialog.dismiss();
    }

    public void continuerChangerP(View view){
        EditText editText = dialog.findViewById(R.id.username);
        editText.getText();
        ouvertMaison.nomPieceSelect(editText.getText().toString().trim());
        dialog.dismiss();

        listMaison.notifierObservateur();

    }


    // Pour changer le nom de la maison

    public void changerNomMaison(View view){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_nom_maison);

        dialog.show();
    }

    public void annulerChangerM(View view){
        dialog.dismiss();
    }

    public void continuerChangerM(View view){
        EditText editText = dialog.findViewById(R.id.username);
        editText.getText();
        ouvertMaison.setNom(editText.getText().toString().trim());

        dialog.dismiss();

        listMaison.notifierObservateur();

    }


// Pour le menu
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

// Pour le TYPE_MAGNETIC_FIELD
    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] gravity = new float[3];


        float[] mGeomagnetic = new float[0];
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = (float) 0.8;
            // Isolate the force of gravity with the low-pass filter.

            gravity[0] = (alpha * gravity[0] + (1 - alpha) * event.values[0]);
            gravity[1] = (alpha * gravity[1] + (1 - alpha) * event.values[1]);
            gravity[2] = (alpha * gravity[2] + (1 - alpha) * event.values[2]);


            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

        }



        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (gravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, mGeomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                orianta = orientation[0]; // orientation contains: azimut, pitch and roll
            }
        }


        vue.passage(linear_acceleration/*,orianta */);
        vue.invalidate();




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onResume() {
        super.onResume();

        this.sensorManagerACC.unregisterListener(this);
        this.sensorManagerACC.registerListener(this, this.sensorACC, 10000);


        this.sensorManagerMA.unregisterListener(this);
        this.sensorManagerMA.registerListener(this, this.sensorMA, 10000);

    }

    @Override
    public void onPause() {
        super.onPause();
        this.sensorManagerACC.unregisterListener(this);
        this.sensorManagerMA.unregisterListener(this);

    }




    // Pour l'implementation du observateur
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

        TextView nomText = findViewById(R.id.nom_maison);
        nomText.setText(String.valueOf(listMaison.getSelectMaison().getNom()));

        // Mise a jour de la Recy
        pieceAdapt.notifyDataSetChanged();
    }
}