package com.example.modelevirtuel;

import android.graphics.*;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.modelevirtuel.model.GestionnaireMaison;
import com.example.modelevirtuel.model.Maison;
import com.example.modelevirtuel.model.Mur;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MurActivity extends AppCompatActivity {
    GestionnaireMaison listMaison;
    Maison ouvertMaison;

    Mur selectMur;

    private Rect rectangle;
    private SurfaceView surfaceView;
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mur);
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
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, 1100,1500,false));
        }

        this.imageView.setOnTouchListener((v, event) -> {
            SurfaceHolder sfhTrackHolder = MurActivity.this.surfaceView.getHolder();
            sfhTrackHolder.setFormat(-2);
            event.getActionMasked();
            Log.i("selectActivity", "nombre de click: " + event.getPointerCount());
            if (event.getPointerCount() == 2) {
                MurActivity.this.x1 = (int) event.getX(0);
                MurActivity.this.y1 = (int) event.getY(0);
                MurActivity.this.x2 = (int) event.getX(1);
                MurActivity.this.y2 = (int) event.getY(1);
                MurActivity.this.rectangle = new Rect(MurActivity.this.x1, MurActivity.this.y1, MurActivity.this.x2, MurActivity.this.y2);
                Log.i("rectangle", MurActivity.this.rectangle.toString());
                Bitmap bitmap = Bitmap.createBitmap(MurActivity.this.surfaceView.getWidth(), MurActivity.this.surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(8.0f);

                new Canvas(bitmap);
                Canvas canvas = sfhTrackHolder.lockCanvas();
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                canvas.drawRect(MurActivity.this.rectangle, paint);
                sfhTrackHolder.unlockCanvasAndPost(canvas);





            }

            return true;
        });

    }

}