package com.example.modelevirtuel;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.model.*;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.Orientation;
import com.example.modelevirtuel.outils.PieceAdapter;
import com.example.modelevirtuel.outils.VueCapteurActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Iterator;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;
import static java.lang.Thread.sleep;

public class MaisonActivity extends AppCompatActivity implements SensorEventListener,Observateur {

    private GestionnaireMaison listMaison;
    private Maison ouvertMaison;
    private Dialog dialog;
    private PieceAdapter pieceAdapt;

    static final int PHOTO = 1;
    private Bitmap photo;
    private ImageView imagePhoto;

    private Sensor sensorMA;

    VueCapteurActivity vue;

    private SensorManager sensorManager;
    private Sensor sensorACC;

    private Orientation orientationSelec;

    float[] magneticVector = new float[3];
    float[] acceleromterVector = new float[3];


    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maison);

        // On bloque en mode portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // On recupaire le gestionnaire
        listMaison = GestionnaireMaison.getInstance();
        ouvertMaison = listMaison.getSelectMaison();

        // On met le nom de la maison
        TextView nomText = findViewById(R.id.nom_maison);
        nomText.setText(String.valueOf(listMaison.getSelectMaison().getNom()));

        // Liste des pieces
        RecyclerView recycler = findViewById(R.id.RecyPiece);
        pieceAdapt = new PieceAdapter(ouvertMaison.getListPiece());
        recycler.setAdapter(pieceAdapt);

        // Mettre la recy horizontalement
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(linearLayoutManager);


        // Les capteurs
        vue = findViewById(R.id.capteur);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorACC = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMA = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        listMaison.ajouterObservateur(this);
        try {
            listMaison.notifierObservateur();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Fonction qui annule le dialogue vue au dessus
     *
     * @param view
     */
    public void annuler2(View view) {
        dialog.cancel();
    }


    //  Pour ajouter la piece

    /**
     * Fonction qui ouvre le dialogue pour ajouter la piece
     *
     * @param view
     */
    public void ajouterPiece(View view) {
        String id = null;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_nom_piece);

        dialog.show();
    }


    /**
     * Fonction qui permet de officaliser lajout de piece
     *
     * @param view
     */
    @SuppressLint("NotifyDataSetChanged")
    public void continuerPiece(View view) throws JSONException, IOException {
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


    // Selection de la piece

    /**
     * Fonction qui permet de selectionner la piece
     *
     * @param view
     */
    public void PieceSelectionner(View view) throws JSONException, IOException {
        TextView num = view.findViewById(R.id.item_num_piece);
        int id = Integer.parseInt((String) num.getText());
        ouvertMaison.setPieceSelect(ouvertMaison.getListPiece().get(id));
        String nom = ouvertMaison.getPieceSelect().getNom();

        listMaison.notifierObservateur();
    }


    /**
     * Fonction qui supprime la maison actuelle
     *
     * @param view
     */
    public void suppMaison(View view) throws JSONException, IOException {
        finish();


        listMaison.supprimerMaison(ouvertMaison);
        listMaison.notifierObservateur();

        listMaison.enregistrement(openFileOutput("sauvegarde.json", Context.MODE_PRIVATE));

        // On retourne sur la page d'acceuille car cette maison vas etre supprimer
        Intent ic = new Intent(MaisonActivity.this, MainActivity.class);
        startActivity(ic);
    }


    /**
     * FOnction qui supprime la piece actuelle
     *
     * @param view
     */
    @SuppressLint("NotifyDataSetChanged")
    public void suppPiece(View view) throws JSONException, IOException {
        ouvertMaison.supprimerPieceOuvert();
        pieceAdapt.setList(ouvertMaison.getListPiece());
        pieceAdapt.notifyDataSetChanged();

        listMaison.notifierObservateur();

    }


    /**
     * Ouvre le dialogue pour la fonctionaliter des photo
     */
    public void dialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_photo);

        dialog.show();
    }

    /**
     * C'est la photo du nord qui est selectionner
     * @param view
     */
    public void selectPhotoNord(View view) {
        dialog();
        imagePhoto = findViewById(R.id.interrogationNord);
        orientationSelec = Orientation.NORD;


    }


    /**
     * C'est la photo du sud qui est selectionner
     * @param view
     */
    public void selectPhotoSud(View view) {
        dialog();
        imagePhoto = findViewById(R.id.interrogationSud);
        orientationSelec = Orientation.SUD;
    }


    /**
     * C'est la photo de Ouest qui est selectionner
     * @param view
     */
    public void selectPhotoOuest(View view) {
        dialog();
        imagePhoto = findViewById(R.id.interrogationOuest);
        orientationSelec = Orientation.OUEST;
    }


    /**
     * C'est la photo de l'est qui est selectionner
     * @param view
     */
    public void selectPhotoEst(View view) {
        dialog();
        imagePhoto = findViewById(R.id.interrogationEst);
        orientationSelec = Orientation.EST;
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
                        photo = (Bitmap) data.getExtras().get("data");
                        if(photo != null){
                            String nom = ouvertMaison.getNom()+"_"+ouvertMaison.getPieceSelect().getNom()+"_"+orientationSelec.toString();
                            ouvertMaison.ajouterMur(orientationSelec,nom);
                            Log.i("nom",nom);
                            FileOutputStream fos = null;
                            try {
                                fos = openFileOutput(nom+".data", MODE_PRIVATE);
                                photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                listMaison.notifierObservateur();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }


                    }
                }

            });


    public void photo(View view) throws IOException, InterruptedException, JSONException {
        dialog.cancel();

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        someActivityResultLauncher.launch(intent);

        photo = null;
    }

    public void selectionPhoto(View view){
        dialog.cancel();

        // A remplir
    }

    // Pour changer le nom de la piece

    /**
     * Fonction qui ouvre le dialogue pour le changement de nom de la piece
     * @param view
     */
    public void changerNomPiece(View view){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_nom_piece);

        dialog.show();
    }

    /**
     * Fonction qui changer officielement le nom de la piece
     * @param view
     */
    public void continuerChangerP(View view) throws JSONException, IOException {
        EditText editText = dialog.findViewById(R.id.username);
        editText.getText();
        ouvertMaison.nomPieceSelect(editText.getText().toString().trim());
        dialog.dismiss();

        listMaison.notifierObservateur();

    }

    // Pour changer le nom de la maison

    /**
     * Fonction qui ouvre un dialogue de le changement de nom d'une maison
     * @param view
     */
    public void changerNomMaison(View view){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_nom_maison);

        dialog.show();
    }


    /**
     * Fonction qui changement officiellement le nom d'une maison
     * @param view
     */
    public void continuerChangerM(View view) throws JSONException, IOException {
        EditText editText = dialog.findViewById(R.id.username);
        editText.getText();
        ouvertMaison.setNom(editText.getText().toString().trim());

        dialog.dismiss();

        listMaison.notifierObservateur();

    }




// Pour le TYPE_MAGNETIC_FIELD
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        float[] resultMatrix = new float[9];
        float[] values = new float[3];

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acceleromterVector= sensorEvent.values;

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticVector= sensorEvent.values;

        }

        SensorManager.getRotationMatrix(resultMatrix, null, acceleromterVector, magneticVector);
        SensorManager.getOrientation(resultMatrix, values);


        // Acellerometre
        vue.passage(values[0]);
        vue.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void onResume() {
        super.onResume();
        this.sensorManager.unregisterListener(this);
        this.sensorManager.registerListener(this, this.sensorACC, SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(this, this.sensorMA, SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    /**
     * Fonction qui permet de gérer l'affichage
     */
    @Override
    public void reagir() throws JSONException, IOException {
      //  listMaison.enregistrement(fichierEnregistrement);

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
            vue.setVisibility(View.INVISIBLE);
            i1.setVisibility(View.INVISIBLE);
            i2.setVisibility(View.INVISIBLE);
            i3.setVisibility(View.INVISIBLE);
            i4.setVisibility(View.INVISIBLE);

            FabriqueIdentifiant.getInstance().removePiece();
            nomPiece.setText("");

        }else{
            poubellePiece.setVisibility(View.VISIBLE);
            aucunePiece.setVisibility(View.INVISIBLE);
            vue.setVisibility(View.VISIBLE);
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


        ImageView image = null;


        // On remet les point d'interrogation
        image = findViewById(R.id.interrogationSud);
        image.setImageResource(R.drawable.interrogation);


        image = findViewById(R.id.interrogationNord);
        image.setImageResource(R.drawable.interrogation);

        image = findViewById(R.id.interrogationOuest);
        image.setImageResource(R.drawable.interrogation);

        image = findViewById(R.id.interrogationEst);
        image.setImageResource(R.drawable.interrogation);


        // On ouvre les photo des murs
        if(!ouvertMaison.getListPiece().isEmpty()){
            for(int i =0; i< ouvertMaison.getPieceSelect().getListMur().size(); i++){
                Mur m = ouvertMaison.getPieceSelect().getListMur().get(i);
                if(m.getOrientation() == Orientation.SUD){
                    image = findViewById(R.id.interrogationSud);
                }
                if(m.getOrientation() == Orientation.NORD){
                    image = findViewById(R.id.interrogationNord);
                }
                if(m.getOrientation() == Orientation.OUEST){
                    image = findViewById(R.id.interrogationOuest);
                }
                if(m.getOrientation() == Orientation.EST){
                    image = findViewById(R.id.interrogationEst);
                }
                FileInputStream fis = openFileInput(m.getNom()+".data");
                Bitmap bm = BitmapFactory.decodeStream(fis);
                image.setImageBitmap(bm);

            }


        }




        // On enregistre
        listMaison.enregistrement(openFileOutput("sauvegarde.json", Context.MODE_PRIVATE));

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



}