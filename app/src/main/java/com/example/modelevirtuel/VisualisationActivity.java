package com.example.modelevirtuel;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Mur;
import com.example.modelevirtuel.model.Porte;
import com.example.modelevirtuel.outils.Orientation;
import org.json.JSONException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

public class VisualisationActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private ImageView imageView;
    private SurfaceView surfaceView;
    private GestionnaireMaison listMaison;

    private Orientation orientation;
    private TextView temp;
    private TextView loca;
    private Porte porteSelect;

    private  SurfaceHolder sfhTrackHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        temp = findViewById(R.id.temp);
        loca = findViewById(R.id.loca);
        imageView = findViewById(R.id.photo_mur_visu);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_visu);

        surfaceView.setZOrderOnTop(true);


        // On recupaire le gestionnaire
        listMaison = GestionnaireMaison.getInstance();


        FileInputStream fis = null;
        orientation = Orientation.NORD;
        refreche();


       sfhTrackHolder = surfaceView.getHolder();
        sfhTrackHolder.addCallback(this);
        sfhTrackHolder.setFormat(-2);



        this.imageView.setOnTouchListener((v, event) -> {
            event.getActionMasked();




            try {
                reagirPorte();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            if (event.getPointerCount() == 1 && comparerXY((int) event.getX(0),(int) event.getY(0))) {
                listMaison.getSelectMaison().setPieceVisu(porteSelect.getArriver());
                refreche();
                return false;
            }
            return true;

        });

        try {
            listMaison.enregistrement(openFileOutput("sauvegarde.json", Context.MODE_PRIVATE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }


    /*
    public void onStart() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_visu);
        surfaceView.setZOrderOnTop(true);


        super.onStart();
        try {
            reagirPorte();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/
    public void gauche(View view) throws JSONException, IOException {
        orientation = listMaison.getSelectMaison().getPieceVisu().tournerGauche(orientation);

        refreche();
        reagirPorte();
    }



    public void droite(View view) throws JSONException, IOException {
        orientation = listMaison.getSelectMaison().getPieceVisu().tournerDroite(orientation);

     refreche();
        reagirPorte();
    }

    public void refreche(){
        FileInputStream fis = null;
        if(listMaison.getSelectMaison().getPieceVisu().getMur(orientation) != null){
            temp.setText(MessageFormat.format("{0}°C", Integer.valueOf((int)listMaison.getSelectMaison().getPieceVisu().getMur(orientation).getTemperature())));
            loca.setText(MessageFormat.format("{0}", listMaison.getSelectMaison().getPieceVisu().getMur(orientation).getLoca()));

            try {
                fis = openFileInput(listMaison.getSelectMaison().getPieceVisu().getMur(orientation).getNom()+".data");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, 1000,1400,false));
        }else{

            temp.setText(" ");
            loca.setText(" ");
            imageView.setImageResource(R.drawable.piece2);
        }

    }

    public boolean comparerXY(int x1, int y1){
        Rect r;
        Mur mur = listMaison.getSelectMaison().getPieceVisu().getMur(orientation);

        if(mur != null){
            for(int i =0; i< mur.getListPorte().size(); i++){
                r = mur.getListPorte().get(i).getRect();
                if(x1>= r.left && x1<= r.right && y1<= r.bottom && y1>= r.top){
                    porteSelect = mur.getListPorte().get(i);
                    return true;
                }
            }
        }

        return false;
    }

    public void reagirPorte() throws JSONException, IOException {
        Porte porte;
        Mur mur = listMaison.getSelectMaison().getPieceVisu().getMur(orientation);



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


        Bitmap bitmap = Bitmap.createBitmap(this.surfaceView.getWidth(),this.surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap);
        canvas1 = sfhTrackHolder.lockCanvas();
        canvas1.drawColor(0, PorterDuff.Mode.CLEAR);

        if(mur != null){
            for (Porte value : mur) {
                porte = value;
                canvas1.drawRect(porte.getRect(), paint);

                canvas1.drawText(porte.getArriver().getNom()+porte.getArriver().getId(), porte.getRect().left, porte.getRect().top - 2, paint1);
            }

        }

        sfhTrackHolder.unlockCanvasAndPost(canvas1);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            reagirPorte();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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