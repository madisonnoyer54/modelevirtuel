package com.example.modelevirtuel;




import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.model.*;
import com.example.modelevirtuel.outils.FabriqueIdentifiant;
import com.example.modelevirtuel.outils.Orientation;
import com.example.modelevirtuel.outils.PieceAdapter;
import com.example.modelevirtuel.outils.VueCapteurActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;
import static java.lang.Thread.sleep;

public class MaisonActivity extends AppCompatActivity implements SensorEventListener,Observateur {

    private GestionnaireMaison listMaison;
    private Maison ouvertMaison;
    private Dialog dialog;
    private PieceAdapter pieceAdapt;
    private Bitmap photo;
    private ImageView imagePhoto;
    private Orientation orientationSelec;

    private Sensor sensorMA;
    private VueCapteurActivity vue;
    private SensorManager sensorManager;
    private Sensor sensorACC;

    private float[] magneticVector = new float[3];
    private float[] acceleromterVector = new float[3];


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
    public Dialog dialog() {
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_photo);
        dialog.show();

        return dialog;
    }

    /**
     * C'est la photo du nord qui est selectionner
     * @param view
     */
    public void selectPhotoNord(View view) {
        Dialog dialog1 = dialog();
        imagePhoto = findViewById(R.id.interrogationNord);
        orientationSelec = Orientation.NORD;

        Button b = dialog1.findViewById(R.id.supphoto);
        Button b1 = dialog1.findViewById(R.id.selectBut);

        if(ouvertMaison.getPieceSelect().getMur(orientationSelec) == null){
            b.setBackgroundColor(Color.GRAY);
            b1.setBackgroundColor(Color.GRAY);
        }

    }


    /**
     * C'est la photo du sud qui est selectionner
     * @param view
     */
    public void selectPhotoSud(View view) {
        Dialog dialog1 = dialog();
        imagePhoto = findViewById(R.id.interrogationSud);
        orientationSelec = Orientation.SUD;

        Button b = dialog1.findViewById(R.id.supphoto);
        Button b1 = dialog1.findViewById(R.id.selectBut);

        if(ouvertMaison.getPieceSelect().getMur(orientationSelec) == null){
            b.setBackgroundColor(Color.GRAY);
            b1.setBackgroundColor(Color.GRAY);
        }


    }


    /**
     * C'est la photo de Ouest qui est selectionner
     * @param view
     */
    public void selectPhotoOuest(View view) {
        Dialog dialog1 = dialog();
        imagePhoto = findViewById(R.id.interrogationOuest);
        orientationSelec = Orientation.OUEST;

        Button b = dialog1.findViewById(R.id.supphoto);
        Button b1 = dialog1.findViewById(R.id.selectBut);

        if(ouvertMaison.getPieceSelect().getMur(orientationSelec) == null){
            b.setBackgroundColor(Color.GRAY);
            b1.setBackgroundColor(Color.GRAY);
        }

    }


    /**
     * C'est la photo de l'est qui est selectionner
     * @param view
     */
    public void selectPhotoEst(View view) {
        Dialog dialog1 = dialog();
        imagePhoto = findViewById(R.id.interrogationEst);
        orientationSelec = Orientation.EST;

        Button b = dialog1.findViewById(R.id.supphoto);
        Button b1 = dialog1.findViewById(R.id.selectBut);


        if(ouvertMaison.getPieceSelect().getMur(orientationSelec) == null){
            b.setBackgroundColor(Color.GRAY);
            b1.setBackgroundColor(Color.GRAY);
        }



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
                            String nom = ouvertMaison.getNom()+ouvertMaison.getId()+"_"+ouvertMaison.getPieceSelect().getNom()+ouvertMaison.getPieceSelect().getId()+"_"+orientationSelec.toString();
                            ouvertMaison.ajouterMur(orientationSelec,nom);
                            FileOutputStream fos = null;
                            try {
                                fos = openFileOutput(nom+".data", MODE_PRIVATE);
                                photo.compress(Bitmap.CompressFormat.PNG, 50, fos);
                                Mur mur = ouvertMaison.getPieceSelect().getMur(orientationSelec);
                                meteo(mur);
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


    /**
     * Fonction qui ouvre le dialogue de la photo
     * @param view
     */
    public void photo(View view){
        dialog.cancel();

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        someActivityResultLauncher.launch(intent);

        photo = null;
    }

    /**
     * Fonction qui permet la selection de la photo
     * @param view
     */
    public void selectionPhoto(View view){
        dialog.cancel();

        // On prend lre mur selectionner
        ouvertMaison.getPieceSelect().setMurSelect(ouvertMaison.getPieceSelect().getMur(orientationSelec));

        // Ouvrir la page avec le murs
        if(ouvertMaison.getPieceSelect().getMurSelect() != null){
            Intent ic = new Intent(MaisonActivity.this, MurActivity.class);
            startActivity(ic);
        }
    }

    /**
     * Fonction qui permet de supprimer la photo
     * @param view
     * @throws JSONException
     * @throws IOException
     */
    public void supphoto(View view) throws JSONException, IOException {
        dialog.cancel();
        Mur m = ouvertMaison.getPieceSelect().getMur(orientationSelec);

        ouvertMaison.getPieceSelect().getListMur().remove(m);

        listMaison.notifierObservateur();

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

        vue.passage(values[0]);
        vue.invalidate();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
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
                image.setImageBitmap(Bitmap.createScaledBitmap(bm,100,150,false));

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

        return false;
    }

    /**
     * Fonction qui permet de définir la météo
     * @param mur
     */
    public void meteo(Mur mur) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Les permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 48);
        }


        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    service.execute(() -> {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();

                        Log.i("lon", String.valueOf(longitude));
                        Log.i("la", String.valueOf(latitude));

                        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" +  latitude + "&lon=" + longitude + "&appid=6c2ad30eae15b61aae3d2e90079beb1d&lang=fr";
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        InputStream in = null;
                        try {
                            in = new URL(url).openStream();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                        JSONObject res = null;
                        try {
                            res = readStream(in);
                            //JSONObject desc = (JSONObject) res.getJSONArray("weather").get(0);

                            mur.setTemperature(res.getJSONObject("main").getDouble("temp") - 273.15d);
                            mur.setLoca(res.getString("name"));
                            //  mur.setIcone("http://openweathermap.org/img/wn/" + desc.get("icon") + "@2x.png");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    });
                }
            }
        });
    }

    private JSONObject readStream(InputStream is) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for(String line = r.readLine(); line != null; line = r.readLine()){
            sb.append(line);
        }
        is.close();
        return new JSONObject((sb.toString()));
    }


}