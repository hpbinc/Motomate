package com.hashim.motomate;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class login extends AppCompatActivity {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;




    EditText name;
    EditText pass;
    Button btn;
    FirebaseAuth firebaseAuth;
    String name1;
    String pass1;
    ProgressDialog progressDialog;

    private static final String REGISTER_URL = "http://motomate-hashimn33911457.codeanyapp.com/register.php";


    public void mail(View v)
    {

        Intent intent = new Intent(this,sentmail.class);
        startActivity(intent);


    }


    public void login(View v)
    {
        name = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        btn = (Button) findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");


        name1 = name.getText().toString();
        pass1 = pass.getText().toString();
        if (TextUtils.isEmpty(name1)) {
            Toast.makeText(login.this, "Please Enter Email ", Toast.LENGTH_SHORT).show();


            return;
        }
        if (TextUtils.isEmpty(pass1)) {
            Toast.makeText(login.this, "Please Enter password ", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(name1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    SharedPreferences prefs = getSharedPreferences("userdata", MODE_MULTI_PROCESS);

                    String restoredText = prefs.getString("name", null);

                    if (restoredText == null)

                    {
                        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_MULTI_PROCESS).edit();

                        editor.putString("name", name1);
                        editor.putFloat("rating1", 0);
                        editor.putFloat("rating2", 0);
                        editor.putFloat("rating3", 0);
                        editor.putFloat("driverrating", 0);

                        editor.commit();
                    }
                    data ob = new data(login.this, null, null, 1);

                    ob.cleardata();

                    //registerUser();
                    if(!name1.equals(restoredText))
                    {
                        progressDialog.dismiss();

                        Toast.makeText(login.this, " Login with your orginal id ", Toast.LENGTH_SHORT).show();
                        return;


                    }

                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Welcome  " + name1, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    finish();

                } else {
                    progressDialog.dismiss();
                    StyleableToast st = new StyleableToast(login.this, "Invalid username/password", Toast.LENGTH_SHORT);
                    st.setBackgroundColor(Color.parseColor("#00003C"));
                    st.setTextColor(Color.WHITE);

                    st.setMaxAlpha();
                    st.show();
                }


            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonSignIn = (Button) findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);


        TextView u=(TextView)findViewById(R.id.username);
        TextView p=(TextView)findViewById(R.id.password);
        TextView l=(TextView)findViewById(R.id.login);


        Typeface zeronero= Typeface.createFromAsset(getAssets(),"fonts/zeronero.ttf");
        Typeface anson=Typeface.createFromAsset(getAssets(),"fonts/Anson-Regular.otf");
        Typeface gillsans=Typeface.createFromAsset(getAssets(),"fonts/gillsans.ttf");

        u.setTypeface(anson);
        p.setTypeface(anson);
        l.setTypeface(anson);

    }


    public void registerUser() {

        SharedPreferences prefs = getSharedPreferences("userdata", MODE_PRIVATE);

        String name = "sffsf";
        String flag = "1";

        String rating = ""+((prefs.getFloat("driverrating",3.5f))/10)*10+"/5";
        String email = name1;

        register(flag,rating,name,email);
    }

    private void register(String name, String username, String password, String email) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   loading = ProgressDialog.show(php.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("flag",params[0]);
                data.put("rating",params[1]);
                data.put("name",params[2]);
                data.put("email",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,username,password,email);
    }



}








