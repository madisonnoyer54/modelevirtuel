package com.example.modelevirtuel;

import android.Manifest;
import android.location.Location;
import androidx.annotation.NonNull;
import com.google.android.gms.location.FusedLocationProviderClient;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.modelevirtuel.model.*;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.callback.Callback;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class MurActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SurfaceHolder.Callback {
    private GestionnaireMaison listMaison;
    private Maison ouvertMaison;

    private Mur selectMur;

    private String item;

    private Rect rectangle;
    private SurfaceView surfaceView;
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    private Dialog dialog;

    private boolean select;

    private ImageView imageView;

    private Bitmap bitmap;

    private Canvas canvas;

    private Porte porteSelect;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mur);


        select =false;

        //Pour le spinner

        // On bloque en mode portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;



        imageView = findViewById(R.id.photo_mur);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setZOrderOnTop(true);


        // On recupaire le gestionnaire
        listMaison = GestionnaireMaison.getInstance();
        ouvertMaison = listMaison.getSelectMaison();
        selectMur = ouvertMaison.getPieceSelect().getMurSelect();



        // On met l'image
        if(selectMur != null){
            FileInputStream fis = null;
            try {
                fis = openFileInput(selectMur.getNom()+".data");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, 1000,1300,false));
        }



        SurfaceHolder sfhTrackHolder = MurActivity.this.surfaceView.getHolder();
        sfhTrackHolder.setFormat(-2);
        sfhTrackHolder.addCallback(this);





        this.imageView.setOnTouchListener((v, event) -> {
            event.getActionMasked();

            if (event.getPointerCount() == 2) {
                select = true;
                MurActivity.this.x1 = (int) event.getX(0);
                MurActivity.this.y1 = (int) event.getY(0);
                MurActivity.this.x2 = (int) event.getX(1);
                MurActivity.this.y2 = (int) event.getY(1);
                MurActivity.this.rectangle = new Rect(MurActivity.this.x1, MurActivity.this.y1, MurActivity.this.x2, MurActivity.this.y2);

                bitmap = Bitmap.createBitmap(MurActivity.this.surfaceView.getWidth(), MurActivity.this.surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(8.0f);

                canvas = new Canvas(bitmap);
                canvas = sfhTrackHolder.lockCanvas();
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                canvas.drawRect(MurActivity.this.rectangle, paint);
                sfhTrackHolder.unlockCanvasAndPost(canvas);
                rectangle.sort();



            } else if (event.getPointerCount() == 1 && comparerXY((int) event.getX(0),(int) event.getY(0))) {
                dialogChange();
                return false;
            }else if(event.getPointerCount() == 1 && select ){
                select =false;
                dialogue();
            }
            return true;

        });
        try {
            listMaison.notifierObservateur();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Fonction qui permet de comparer les coordonnée rentrer en paramètre avec les coordonnée des portes
     * @param x1
     * @param y1
     * @return
     */
    public boolean comparerXY(int x1, int y1){
        Rect r;

        for(int i =0; i<selectMur.getListPorte().size(); i++){
            r = selectMur.getListPorte().get(i).getRect();
            if(x1>= r.left && x1<= r.right && y1<= r.bottom && y1>= r.top){
                porteSelect =selectMur.getListPorte().get(i);
                return true;
            }
        }
           return false;
    }

    /**
     * Fonction qui ouvre le dialogue
     */
    public void dialogChange(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_porte);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        //  Log.i("Spinner", String.valueOf(spinner));
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ouvertMaison.transformeEnArray());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(0);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        dialog.show();
    }


    /**
     * Fonction qui permet de changer la direction de la porte
     * @param view
     * @throws InterruptedException
     * @throws JSONException
     * @throws IOException
     */
    public void continuerChangePorte(View view) throws InterruptedException, JSONException, IOException {

        dialog.cancel();
        Thread.sleep(100);
        porteSelect.setArriver(item);

        reagirPorte();
        listMaison.notifierObservateur();

    }

    /**
     * Fonction qui permet de supprimer la porte selectionner
     * @param view
     * @throws JSONException
     * @throws IOException
     */
    public void SuppPorte(View view) throws JSONException, IOException {
        dialog.cancel();
        selectMur.suppPorte(porteSelect.getId());
        reagirPorte();
        listMaison.notifierObservateur();
    }

    /**
     * Fonction qui permet d'ouvrire le dialogue
     */
    public void dialogue(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_porte);
         Spinner spinner = dialog.findViewById(R.id.spinner);
        //  Log.i("Spinner", String.valueOf(spinner));
        // Spinner click listener
          spinner.setOnItemSelectedListener(this);

        // Creating adapter for spinner
          ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ouvertMaison.transformeEnArray());

        // Drop down layout style - list view with radio button
          dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          spinner.setSelection(0);

        // attaching data adapter to spinner
         spinner.setAdapter(dataAdapter);


        dialog.show();
    }

    /**
     * Fonction qui permet de continuer apres la modification du changement de direction de la porte
     * @param view
     * @throws InterruptedException
     * @throws JSONException
     * @throws IOException
     */
    public void continuerPorte(View view) throws InterruptedException, JSONException, IOException {

        dialog.cancel();
        Thread.sleep(100);
        selectMur.ajoutePorte(item, rectangle);
        listMaison.notifierObservateur();
       reagirPorte();
       listMaison.notifierObservateur();

    }

    /**
     * Fonction qui annule le dialogue vue au dessus
     *
     * @param view
     */
    public void annulerPorte(View view) throws JSONException, IOException {
        dialog.cancel();
        reagirPorte();
        listMaison.notifierObservateur();
    }

    /**
     * Permet de relier le menu au main
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Permet d'accerder au item
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /**
     * Permet de crée une reaction et d'afficher les nouvelle porte avec la nouvelle image
     * @throws IOException
     * @throws JSONException
     */
    public void reagirPorte() throws IOException, JSONException {
        Porte porte;
        SurfaceHolder sfhTrackHolder = this.surfaceView.getHolder();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        paint.setTextSize(30);

        Paint paint1 = new Paint();
        paint1.setColor(Color.RED);
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(1.5f);
        paint1.setTextSize(30);


        bitmap = Bitmap.createBitmap(MurActivity.this.surfaceView.getWidth(), MurActivity.this.surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap);
        canvas1 = sfhTrackHolder.lockCanvas();
         canvas1.drawColor(0, PorterDuff.Mode.CLEAR);

        for (Porte value : selectMur) {
            porte = value;
            canvas1.drawRect(porte.getRect(), paint);

            canvas1.drawText(porte.getArriver(), porte.getRect().left, porte.getRect().top - 2, paint1);
        }

        sfhTrackHolder.unlockCanvasAndPost(canvas1);
        listMaison.notifierObservateur();

    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        try {
            reagirPorte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}