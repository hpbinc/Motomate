package com.hashim.motomate;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import static com.hashim.motomate.R.id.smile_rating;
import static com.hashim.motomate.R.id.speed;

public class rating extends AppCompatActivity {


    float a[]=new float[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        a=getIntent().getFloatArrayExtra("pass");



        final SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);

        int i;

        RatingBar r1=(RatingBar)findViewById(R.id.r1);
        RatingBar r2=(RatingBar)findViewById(R.id.r2);
        RatingBar r3=(RatingBar)findViewById(R.id.r3);

        TextView t=(TextView)findViewById(speed) ;

        r1.setRating(a[0]);
        r2.setRating(a[1]);
        r3.setRating(a[2]);


       // t.setText(" "+a[0]+" "+a[1]+" "+ a[2]+" "+a[3]);

        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_MULTI_PROCESS).edit();
        editor.putFloat("rating1", a[0]);
        editor.putFloat("rating2", a[1]);
        editor.putFloat("rating3", a[2]);
        editor.putFloat("driverrating", a[3]);

        editor.commit();





        Handler handlerTimer = new Handler();


        smileRating.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    return true;
            }
        });



        handlerTimer.postDelayed(new Runnable(){
            public void run() {
                // do something

                if(a[3]>=5)
                    smileRating.setSelectedSmile(BaseRating.GREAT, true);

                else if(a[3]>=4)

                smileRating.setSelectedSmile(BaseRating.GOOD, true);


              else if ( a[3]>=3)

                smileRating.setSelectedSmile(BaseRating.OKAY, true);

               else if(a[3]>=2)

                smileRating.setSelectedSmile(BaseRating.BAD, true);
                else if((int)a[3]>=1)

                smileRating.setSelectedSmile(BaseRating.TERRIBLE, true);

            }}, 3000);




    }
}

