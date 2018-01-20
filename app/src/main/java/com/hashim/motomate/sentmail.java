package com.hashim.motomate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//call eda am in call what




public class sentmail extends Activity {
    Button button;
    GMailSender sender;

    String name1;
    String pass1;

    EditText name;
    EditText pass;

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentmail);



        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 550;
        params.width = 725;
        params.y = -10;


        this.getWindow().setAttributes(params);



        button = (Button)findViewById(R.id.send);
        // Add your mail Id and Password




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
              Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                name = (EditText) findViewById(R.id.p2);
                pass = (EditText) findViewById(R.id.p3);



                name1 = name.getText().toString();
                pass1 = pass.getText().toString();

                if (TextUtils.isEmpty(name1)) {
            Toast.makeText(sentmail.this, "Please Enter Registered Email ", Toast.LENGTH_SHORT).show();

            return;
        }
        if (TextUtils.isEmpty(pass1)) {
            Toast.makeText(sentmail.this, "Please Enter password ", Toast.LENGTH_SHORT).show();
            return;
        }



                sender = new GMailSender(name1,pass1);




                try {
                    new MyAsyncClass().execute();

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        TextView l=(TextView)findViewById(R.id.p1);
        TextView u=(TextView)findViewById(R.id.p2);
        TextView w=(TextView)findViewById(R.id.p3);



        Typeface zeronero= Typeface.createFromAsset(getAssets(),"fonts/zeronero.ttf");
        Typeface anson=Typeface.createFromAsset(getAssets(),"fonts/Anson-Regular.otf");
        Typeface gillsans=Typeface.createFromAsset(getAssets(),"fonts/gillsans.ttf");

        l.setTypeface(anson);
        u.setTypeface(anson);
        w.setTypeface(anson);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it          //     is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(sentmail.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.


                sender.sendMail("Request for password reset", " Forgot my motomate android app password ", name1, "happysanil1@gmail.com");


            }

            catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();

            Intent intent = new Intent(sentmail.this,login.class);
            startActivity(intent);


        }
    }




}
