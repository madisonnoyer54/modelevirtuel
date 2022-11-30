package com.example.modelevirtuel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.example.modelevirtuel.model.*;
import com.example.modelevirtuel.outils.PieceAdapter;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Thread.sleep;

public class MurActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    GestionnaireMaison listMaison;
    Maison ouvertMaison;

    Mur selectMur;

    String item;

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

        Log.i(selectMur.getNom(),"cc");

        // On met l'image
        if(selectMur != null){
            FileInputStream fis = null;
            try {
                fis = openFileInput(selectMur.getNom()+".data");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, 1000,1400,false));
        }

        this.imageView.setOnTouchListener((v, event) -> {
            SurfaceHolder sfhTrackHolder = MurActivity.this.surfaceView.getHolder();
            sfhTrackHolder.setFormat(-2);
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

                try {
                    reagirPorte();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (event.getPointerCount() == 1 && select ) {
                select =false;
                dialogue();
                return false;
            }


            return true;

        });




    }

    public boolean comparerXY(Rect rect,int x1, int x2, int y1, int y2){
       // if(x1>rect. && x2< rect)

            return true;
    }

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

    public void continuerPorte(View view) throws InterruptedException, JSONException, IOException {

        dialog.cancel();
        Thread.sleep(100);
        System.out.println(item);
        selectMur.ajoutePorte(ouvertMaison.setPiece(item), rectangle);

       reagirPorte();

    }

    /**
     * Fonction qui annule le dialogue vue au dessus
     *
     * @param view
     */
    public void annulerPorte(View view) {
        dialog.cancel();
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


    public void reagirPorte() throws JSONException, IOException {
        Porte porte;
        SurfaceHolder sfhTrackHolder = MurActivity.this.surfaceView.getHolder();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        paint.setTextSize(20);

        Paint paint1 = new Paint();
        paint1.setColor(Color.RED);
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(1.5f);
        paint1.setTextSize(20);

        Canvas canvas1 = new Canvas(bitmap);
        canvas1 = sfhTrackHolder.lockCanvas();
         canvas1.drawColor(0, PorterDuff.Mode.CLEAR);

        for(int i = 0; i< selectMur.getListPorte().size(); i++){
            Log.i("cc", "dd");
            porte = selectMur.getListPorte().get(i);
            assert porte != null;
            Log.i("rec",porte.getId() + porte.getRect().toString());
            canvas1.drawRect(porte.getRect(), paint);

            canvas1.drawText(porte.getArriver().getNom(),porte.getRect().left,porte.getRect().top-2, paint1);
        }
        sfhTrackHolder.unlockCanvasAndPost(canvas1);

    }
}