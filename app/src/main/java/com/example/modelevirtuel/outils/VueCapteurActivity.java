package com.example.modelevirtuel.outils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VueCapteurActivity extends View {
    private Paint paint1;

    private  float orianta;



    public VueCapteurActivity(Context context) {
        super(context);
        init();
    }

    public VueCapteurActivity(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public VueCapteurActivity(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    public VueCapteurActivity(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //  canvas.drawLine(550, 700, 500 - (result[0] * 50), (result[1] * 50) + 700, this.paint);

        float compasX = (float)  - Math.sin(orianta)*100;
        float compasY = (float) - Math.cos(orianta)*100;
        canvas.drawLine(170, 120, 170 + compasX, 120+ compasY, this.paint1);

    }

    public void init() {

        paint1 = new Paint();
        paint1.setColor(Color.RED);
        this.paint1.setAntiAlias(true);
        this.paint1.setStyle(Paint.Style.STROKE);
        this.paint1.setStrokeWidth(8.0f);


    }

    public void passage(float orianta) {
        this.orianta = orianta;
    }



}
